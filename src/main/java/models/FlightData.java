package models;

import lombok.*;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class FlightData {
    private String emailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime departureTime;
    private String departureAirportCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime arrivalTime;
    private String arrivalAirportCode;
    private String flightNumber;
    private String confirmationCode;

    @Override
    public String toString() {
        return "FlightData{" +
                "emailAddress='" + emailAddress + '\'' +
                ", departureTime=" + departureTime +
                ", departureAirportCode='" + departureAirportCode + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", arrivalAirportCode='" + arrivalAirportCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", confirmationCode='" + confirmationCode + '\'' +
                '}';
    }
}
