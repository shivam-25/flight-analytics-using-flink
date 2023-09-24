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
public class SunsetAirFlightData {
    private String customerEmailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime departureTime;
    private String departureAirport;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime arrivalTime;
    private String arrivalAirport;
    private String flightId;
    private String referenceNumber;

    public FlightData toFlightData() {
        return new FlightData(
                this.customerEmailAddress,
                this.departureTime,
                this.departureAirport,
                this.arrivalTime,
                this.arrivalAirport,
                this.flightId,
                this.referenceNumber
        );
    }

    @Override
    public String toString() {
        return "SkyOneAirlinesFlightData{" +
                "emailAddress='" + customerEmailAddress + '\'' +
                ", flightDepartureTime=" + departureTime +
                ", iataDepartureCode='" + departureAirport + '\'' +
                ", flightArrivalTime=" + arrivalTime +
                ", iataArrivalCode='" + arrivalAirport + '\'' +
                ", flightNumber='" + flightId + '\'' +
                ", confirmation='" + referenceNumber + '\'' +
                '}';
    }
}
