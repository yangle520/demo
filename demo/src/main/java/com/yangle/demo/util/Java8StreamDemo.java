package com.yangle.demo.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Java8StreamDemo {


    public static void main(String[] args) throws InterruptedException {

        // guava  Lists.newArrayList 用工厂构造带初始值的 list
        List<Integer> list = Lists.newArrayList(11, 1, 123, 34, 2, 5, 4365, 4, 4, 2, 2, 1, 5, 7, 8, 6, 4, 35, 0);

        CompletableFuture.supplyAsync(() -> list.stream().distinct().collect(Collectors.toList()))     // 异步执行方法
                .thenApply(res -> list.stream().reduce(Integer::sum).orElse(0)) // thenApply 执行完上一个任务后执行,入参是上一个方法的返回
                .thenCombine(                                                          // thenCombine 把两个任务链接起来
                        CompletableFuture.supplyAsync(() -> list.stream().max(Comparator.comparing(a -> a)).orElse(0)),
                        (a, b) -> a + b)
                .exceptionally(e -> {                                                  // 方法出线异常时
                    System.out.println(e.getMessage());
                    return 0;
                })
                .thenApply(a -> a * 2)                                                  // thenApply 执行完上一个任务后执行,入参是上一个方法的返回
                .whenComplete((a, e) -> {                                               // 完成方时 （如果上面没有 exceptionally 那么出线异常时直接进入此方法）
                    System.out.println(a);
                    System.out.println(e.getMessage());
                });



        Thread.sleep(1000);
    }

    private static Integer sortAndPrint(List<Integer> list) {
        list.sort(Comparator.comparing(a -> a));
        list.forEach(System.out::println);
        return list.size();
    }

    private static void whenComplete() {

    }


//        Integer product = list.stream().reduce((a, b) -> a * b).orElse(-1); // Lambda 表达式
//        Integer sum = list.stream().reduce(Integer::sum).orElse(-1);   // 方法引用
//        System.out.println(product);
//        System.out.println(sum);
//        Integer max = list.stream().max(Comparator.comparing(a -> a)).orElse(-1);
//        System.out.println(max);
//        Integer min = list.stream().min(Comparator.comparing(a -> a)).orElse(-1);
//        System.out.println(min);
//        // 遍历
//        list.forEach(a -> {
//            System.out.println(a + " ");
//        });
//
//        //  filter  过滤掉奇数
//        List<Integer> list1 = list.stream().filter(a -> a % 2 == 0).collect(Collectors.toList());
//        System.out.println(list1);
//
//        // distinct 去重
//        List<Integer> list2 = list.stream().distinct().collect(Collectors.toList());
//        System.out.println(list2);
//
//        // flatMap   原来一个元素，变成多个
//        List<Integer> list3 = list.stream().flatMap(a -> Lists.newArrayList(a, a * a).stream()).collect(Collectors.toList());
//        System.out.println(list3);
//
//        // map  原来一个元素 转换 成另外一个
//        List<Double> list4 = list.stream().map(a -> a * 0.125).collect(Collectors.toList());
//        System.out.println(list4);
//
//        // limit 切割
//        List<Integer> list5 = list.stream().limit(5).collect(Collectors.toList());
//        System.out.println(list5);
//
//        // sorted 排序  默认从小到大排序
//        List<Integer> list6 = list.stream().sorted().collect(Collectors.toList());
//        System.out.println(list6);
//
//        // 组合使用
//        List<Double> newList = list.stream().filter(a -> a % 2 == 0)
//                .map(a -> a * 0.125)
//                .sorted(Comparator.comparing(a -> a))
//                .distinct()
//                .limit(5).collect(Collectors.toList());
//        System.out.println(newList);
//
//        //  findAny  有一个找到大于6的就返回，没有大于6的 返回-1    parallelStream 并行流 与  stream 串行流 返回结果可能不同
//        Integer list8 = list.parallelStream().sorted().filter(a -> a > 6).findAny().orElse(-1);
//        Integer list9 = list.stream().sorted().filter(a -> a > 6).findAny().orElse(-1);
//        System.out.println(list8);
//        System.out.println(list9);
//        // parallelStream 与 stream
//        //是否需要并行  （要考虑初始化fork/join框架的时间  执行的是消耗大量时间操作(如io/数据库)）
//        //任务之间是否是独立的
//        //是否会引起任何竞态条件
//        //结果对顺序没有要求
//
//        //  findFirst  找到第一个大于6的返回，没有大于6的 返回-1    parallelStream 并行流 与  stream 串行流 返回结果相同
//        Integer list10 = list.parallelStream().sorted().filter(a -> a > 6).findFirst().orElse(-1);
//        Integer list11 = list.stream().sorted().filter(a -> a > 6).findFirst().orElse(-1);
//        System.out.println(list10);
//        System.out.println(list11);
//
//        // 获取最大值最小值
//
//
//        // reduce 合并计算
//        Integer product = list.stream().reduce((a, b) -> a * b).orElse(-1); // Lambda 表达式
//        Integer sum = list.stream().reduce(Integer::sum).orElse(-1);   // 方法引用
//        System.out.println(product);
//        System.out.println(sum);

    // -----------------------------------------------------------------------------------------------


//        .collect(Collectors.joining()) 							// -> String
//                .counting()												// -> Long
//                .collect(Collectors.summingInt())						// -> Integer
//                .collect(Collectors.averagingInt())						// -> Double
//
//                .collect(Collectors.toList())							// -> List<T>
//                .collect(Collectors.toSet())							// -> Set<T>
//                .collect(Collectors.toMap())							// -> Map<k, v>
//                .collect(Collectors.groupingBy())						// -> Map<K, List<T>>
//                .collect(Collectors.toConcurrentMap())					// -> ConcurrentMap<K,U>
//                .collect(Collectors.partitioningBy())					// ->  Map<Boolean, List<T>>
//
//                .collect(Collectors.summarizingInt())					// -> IntSummaryStatistics(count,sum,min,max)
//
//
//                .collect(Collectors.reducing())							// -> Optional<T>
//                .collect(Collectors.minBy())							// -> Optional<T>
//                .collect(Collectors.maxBy())							// -> Optional<T>
//        String list2Str = Joiner.on("$$").skipNulls().join(list);


}
