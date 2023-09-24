package models;

import lombok.*;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkyOneAirlinesFlightData {
    private String emailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime flightDepartureTime;
    private String iataDepartureCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime flightArrivalTime;
    private String iataArrivalCode;
    private String flightNumber;
    private String confirmation;

    public FlightData toFlightData() {
        return new FlightData(
                this.emailAddress,
                this.flightDepartureTime,
                this.iataDepartureCode,
                this.flightArrivalTime,
                this.iataArrivalCode,
                this.flightNumber,
                this.confirmation
        );
    }

    @Override
    public String toString() {
        return "SkyOneAirlinesFlightData{" +
                "emailAddress='" + emailAddress + '\'' +
                ", flightDepartureTime=" + flightDepartureTime +
                ", iataDepartureCode='" + iataDepartureCode + '\'' +
                ", flightArrivalTime=" + flightArrivalTime +
                ", iataArrivalCode='" + iataArrivalCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", confirmation='" + confirmation + '\'' +
                '}';
    }

}
