package flightimporter;

import models.FlightData;
import models.SkyOneAirlinesFlightData;
import models.SunsetAirFlightData;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.formats.json.JsonSerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Properties;

public class FlightImporterJob {

    public static void main(String ar[]) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties consumerConfig = new Properties();
        Properties producerConfig = new Properties();
        try (InputStream stream = FlightImporterJob.class.getClassLoader().getResourceAsStream("consumer.properties")) {
            consumerConfig.load(stream);
        }

        try (InputStream stream = FlightImporterJob.class.getClassLoader().getResourceAsStream("producer.properties")) {
            producerConfig.load(stream);
        }

        KafkaSource<SkyOneAirlinesFlightData> skyOneSource = KafkaSource.<SkyOneAirlinesFlightData>builder()
                .setProperties(consumerConfig)
                .setTopics("skyone")
                .setGroupId("skyone_cg_v1")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(SkyOneAirlinesFlightData.class))
                .build();

        KafkaSource<SunsetAirFlightData> sunSetSource = KafkaSource.<SunsetAirFlightData>builder()
                .setProperties(consumerConfig)
                .setTopics("sunset")
                .setGroupId("sunset_cg_v1")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(SunsetAirFlightData.class))
                .build();

        DataStream<SkyOneAirlinesFlightData> skyOneFlightData = env.fromSource(
                skyOneSource,
                WatermarkStrategy.noWatermarks(),
                "skyone_source"
        );

        DataStream<SunsetAirFlightData> sunSetFlightData = env.fromSource(
                sunSetSource,
                WatermarkStrategy.noWatermarks(),
                "sunset_source"
        );

        KafkaRecordSerializationSchema<FlightData> serializationSchema = KafkaRecordSerializationSchema.<FlightData>builder()
                .setTopic("flightdata")
                .setValueSerializationSchema(new JsonSerializationSchema<FlightData>(() -> {
                    return new ObjectMapper().registerModule(new JavaTimeModule());
                }))
                .build();

        KafkaSink<FlightData> flightDataSink = KafkaSink.<FlightData>builder()
                .setKafkaProducerConfig(producerConfig)
                .setRecordSerializer(serializationSchema)
                .setDeliveryGuarantee(DeliveryGuarantee.NONE)
                .build();

        defineWorkflow(skyOneFlightData, sunSetFlightData).sinkTo(flightDataSink).name("flightdata_sink");

        env.execute("FlightImporter");
    }

    public static DataStream<FlightData> defineWorkflow(DataStream<SkyOneAirlinesFlightData> skyOneSource, DataStream<SunsetAirFlightData> sunSetSource) {
        DataStream<FlightData> stream1 = skyOneSource
                .filter(input -> input.getFlightArrivalTime().compareTo(ZonedDateTime.now()) > 0)
                .map(SkyOneAirlinesFlightData::toFlightData);

        DataStream<FlightData> stream2 = sunSetSource
                .filter(input -> input.getArrivalTime().compareTo(ZonedDateTime.now()) > 0)
                .map(SunsetAirFlightData::toFlightData);

        return stream1.union(stream2);
    }
}
