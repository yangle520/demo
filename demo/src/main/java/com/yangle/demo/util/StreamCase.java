package com.yangle.demo.util;

import com.google.common.collect.Lists;
import com.yangle.demo.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamCase {

    public static final List<User>  userList  = Lists.newArrayList(
            new User(1L, "杨乐"),
            new User(2L, "小胡"),
            new User(3L, "小旭"));
    public static final List<Work>  workList  = Lists.newArrayList(
            new Work(1L, "0800", "1200", "1400", "1800"),
            new Work(2L, "0600", "1000", "1600", "2000"));
    public static final List<Order> orderList = Lists.newArrayList(
            new Order("20190701", 1L, 1L), new Order("20190701", 1L, 2L), new Order("20190701", 1L, 3L),
            new Order("20190702", 2L, 1L), new Order("20190702", 2L, 2L), new Order("20190702", 2L, 3L),
            new Order("20190703", 1L, 1L), new Order("20190703", 2L, 2L), new Order("20190703", 2L, 3L));
    public static final List<Time>  timeList  = Lists.newArrayList(
            new Time(1L, "201907010800"), new Time(1L, "201907010801"), new Time(1L, "201907011201"), new Time(1L, "201907011359"),
            new Time(1L, "201907020559"), new Time(1L, "201907021030"), new Time(1L, "201907021630"), new Time(1L, "201907022158"),
            new Time(1L, "201907030805"), new Time(1L, "201907031150"), new Time(1L, "201907031151"), new Time(1L, "201907031403"), new Time(1L, "201907031404"), new Time(1L, "201907031758"), new Time(1L, "201907031759"), new Time(1L, "201907031801"),
            new Time(2L, "201907010800"), new Time(2L, "201907011300"), new Time(2L, "201907011500"), new Time(2L, "201907011800"),
            new Time(2L, "201907020600"), new Time(2L, "201907020600"), new Time(2L, "201907021600"), new Time(2L, "201907022000"),
            new Time(2L, "201907030950"), new Time(2L, "201907031601"), new Time(2L, "201907032001"),
            new Time(3L, "201907010805"), new Time(3L, "201907011200"), new Time(3L, "201907011420"), new Time(3L, "201907011800"),
            new Time(3L, "201907020600"), new Time(3L, "201907021000"), new Time(3L, "201907021602"), new Time(3L, "201907022100"),
            new Time(3L, "201907030610"), new Time(3L, "201907031600"), new Time(3L, "201907032000"));


    public static void main(String[] args) {
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getUserId, u -> u));
        Map<Long, Work> workMap = workList.stream().collect(Collectors.toMap(Work::getWorkId, w -> w));

        List<WorkDetail> wdList = orderList.stream()
                .filter(o -> userMap.get(o.getUserId()) != null)
                .map(o -> {
                    // 用户
                    User u = userMap.get(o.getUserId());
                    // 获取班次信息
                    Work w = workMap.get(o.getWorkId());
                    // 获取当天打卡信息
                    List<Long> times = getTimesByUserIdAndWorkDate(timeList, o.getUserId(), o.getWorkDate());
                    // 计算迟到早退次数
                    Integer lateTimes = calLateTimes(w, times);
                    // 计算未打卡次数
                    Integer forgetTimes = calForgetTimes(w, times);

                    return WorkDetail.builder()
                            .userId(u.getUserId())
                            .userName(u.getUserName())
                            .workDate(o.getWorkDate())
                            .lateTimes(lateTimes)
                            .forgetTimes(forgetTimes)
                            .build();
                }).sorted(Comparator.comparing(WorkDetail::getUserId)).collect(Collectors.toList());

        System.out.println(wdList);
    }

    private static List<Long> getTimesByUserIdAndWorkDate(List<Time> timeList, Long userId, String workDate) {
        return timeList.stream()
                .filter(t -> Objects.equals(t.getUserId(), userId) && Objects.equals(t.getCheckTime().substring(0, 8), workDate))
                .map(t -> Long.valueOf(t.getCheckTime()) % 10000)
                .collect(Collectors.toList());
    }

    private static Integer calLateTimes(Work w, List<Long> times) {
        int lateTimes = 0;

        Long in1Time = times.stream().filter(t -> t < avg(w.getArrangeIn1(), w.getArrangeOut1())).min(Comparator.comparing(t -> t)).orElse(null);
        lateTimes += in1Time != null && in1Time > Integer.valueOf(w.getArrangeIn1()) ? 1 : 0;

        Long out1Time = times.stream().filter(t -> avg(w.getArrangeIn1(), w.getArrangeOut1()) <= t && t < avg(w.getArrangeOut1(), w.getArrangeIn2())).max(Comparator.comparing(t -> t)).orElse(null);
        lateTimes += out1Time != null && out1Time < Integer.valueOf(w.getArrangeOut1()) ? 1 : 0;

        Long in2Time = times.stream().filter(t -> avg(w.getArrangeOut1(), w.getArrangeIn2()) <= t && t <= avg(w.getArrangeIn2(), w.getArrangeOut2())).min(Comparator.comparing(t -> t)).orElse(null);
        lateTimes += in2Time != null && in2Time > Integer.valueOf(w.getArrangeIn2()) ? 1 : 0;

        Long out2Time = times.stream().filter(t -> avg(w.getArrangeIn2(), w.getArrangeOut2()) <= t).max(Comparator.comparing(t -> t)).orElse(null);
        lateTimes += out2Time != null && out2Time < Integer.valueOf(w.getArrangeOut2()) ? 1 : 0;

        return lateTimes;
    }

    private static Integer calForgetTimes(Work w, List<Long> times) {
        return 4 - times.stream().map(t -> groupByTime(w, t)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).keySet().size();
    }


    private static Integer avg(String num1, String num2) {
        return (Integer.valueOf(num1) + Integer.valueOf(num2)) / 2;
    }

    private static String groupByTime(Work w, Long t) {
        if (t < avg(w.getArrangeIn1(), w.getArrangeOut1())) {
            return w.getArrangeIn1();
        } else if (avg(w.getArrangeIn1(), w.getArrangeOut1()) <= t && t < avg(w.getArrangeOut1(), w.getArrangeIn2())) {
            return w.getArrangeOut1();
        } else if (avg(w.getArrangeOut1(), w.getArrangeIn2()) <= t && t < avg(w.getArrangeIn2(), w.getArrangeOut2())) {
            return w.getArrangeIn2();
        } else {
            return w.getArrangeOut2();
        }
    }

}
