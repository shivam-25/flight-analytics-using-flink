package models;

import lombok.*;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatistics {
    private String emailAddress;
    private Duration totalFlightDuration;
    private long numberOfFlights;

    public UserStatistics(FlightData flightData) {
        this.emailAddress = flightData.getEmailAddress();
        this.totalFlightDuration = Duration.between(
                flightData.getDepartureTime(),
                flightData.getArrivalTime()
        );
        this.numberOfFlights = 1;
    }

    @Override
    public String toString() {
        return "UserStatistics{" +
                "emailAddress='" + emailAddress + '\'' +
                ", totalFlightDuration=" + totalFlightDuration +
                ", numberOfFlights=" + numberOfFlights +
                '}';
    }

    public UserStatistics merge(UserStatistics userStats) {
        assert(this.emailAddress.equals(userStats.emailAddress));

        return new UserStatistics(this.emailAddress,
                this.totalFlightDuration.plus(userStats.getTotalFlightDuration()),
                this.numberOfFlights + userStats.getNumberOfFlights());
    }
}