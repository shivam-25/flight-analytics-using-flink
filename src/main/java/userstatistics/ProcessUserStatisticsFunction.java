package userstatistics;

import lombok.Value;
import models.UserStatistics;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.ConfigurationException;

public class ProcessUserStatisticsFunction extends ProcessWindowFunction<UserStatistics, UserStatistics, String, TimeWindow> {

    private ValueStateDescriptor<UserStatistics> stateDescriptor;

    @Override
    public void open(Configuration config) throws Exception {
        stateDescriptor = new ValueStateDescriptor<>("User Statistics", UserStatistics.class);
        super.open(config);
    }

    @Override
    public void process(String s, ProcessWindowFunction<UserStatistics, UserStatistics, String, TimeWindow>.Context context, Iterable<UserStatistics> iterable, Collector<UserStatistics> collector) throws Exception {
        ValueState<UserStatistics> accumulatedState = context.globalState().getState(stateDescriptor);
        UserStatistics stats = accumulatedState.value();
        for(UserStatistics userStat : iterable) {
            if(stats==null) {
                stats = userStat;
            } else {
                stats = stats.merge(userStat);
            }
        }
        accumulatedState.update(stats);
        collector.collect(stats);
    }
}
