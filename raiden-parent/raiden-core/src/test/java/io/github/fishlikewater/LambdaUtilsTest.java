/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.CollectionUtils;
import io.github.fishlikewater.raiden.core.LambdaUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * {@code LambdaUtilsTest}
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/09
 */
public class LambdaUtilsTest {

    @Test
    public void testLambda() {
        List<Integer> integerList = List.of(1, 2, 4, 8);
        List<Integer> list = LambdaUtils.toList(integerList, integer -> integer * 2);
        Assert.assertEquals(2, (int) list.getFirst());
    }

    @Test
    public void testLambda2() {
        List<Integer> integerList = List.of(1, 2, 4, 8);
        List<Integer> list = LambdaUtils.toList(integerList, it -> it > 2, integer -> integer * 2);
        Assert.assertEquals(8, (int) list.getFirst());
    }

    @Test
    public void testLambdaToListMap() {
        List<Integer> integerList = List.of(1, 2, 4, 8);
        Map<Integer, Integer> map = LambdaUtils.toMap(integerList, it -> it, integer -> integer * 2);
        Assert.assertEquals(8, (int) map.get(4));
    }

    @Test
    public void testLambdaFilter() {
        List<Integer> integerList = List.of(1, 2, 4, 8);
        List<Integer> list = LambdaUtils.filter(integerList, integer -> integer > 2);
        Assert.assertEquals(4, (int) list.getFirst());
    }

    @Test
    public void testLambdaDistinct() {
        List<Integer> integerList = List.of(1, 2, 4, 8, 2);
        List<Integer> list = LambdaUtils.toList(integerList, integer -> integer, true);
        Assert.assertEquals(4, list.size());
    }

    @Test
    public void testLambdaFindFirst() {
        List<Integer> integerList = List.of(1, 2, 4, 8, 2);
        Integer first = LambdaUtils.findFirst(integerList, integer -> integer > 2);
        Assert.assertEquals(4, (int) first);
    }

    @Test
    public void testLambdaFindAny() {
        List<Integer> integerList = List.of(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        Integer first = LambdaUtils.findAny(integerList, integer -> integer > 9);
        Assert.assertNotEquals(1, (int) first);
    }

    @Test
    public void testLambdaSort() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        List<Integer> sort = LambdaUtils.sort(integerList, (o1, o2) -> o2 - o1);
        Assert.assertEquals(456, (int) sort.getFirst());
    }

    @Test
    public void testLambdaSort2() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        TreeSet<Integer> treeSet = new TreeSet<>(integerList);
        List<Integer> sort = LambdaUtils.sort(treeSet, (o1, o2) -> o2 - o1);
        Assert.assertEquals(456, (int) sort.getFirst());
    }

    @Test
    public void testLambdaGroupBy() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        var groupBy = LambdaUtils.groupBy(integerList, integer -> integer % 2);
        Assert.assertEquals(7, groupBy.get(0).size());
    }

    @Test
    public void testLambdaGroupBy2() {
        TestBean zs = new TestBean("zs", "1111");
        TestBean zs1 = new TestBean("zs", "2222");
        TestBean ls = new TestBean("ls", "1111");
        TestBean ls1 = new TestBean("ls", "2222");
        List<TestBean> integerList = CollectionUtils.ofList(zs, zs1, ls, ls1);
        Map<String, List<String>> groupBy = LambdaUtils.groupBy(integerList, TestBean::getName, TestBean::getAddress);
        Assert.assertEquals(2, groupBy.size());
    }

    @Test
    public void testLambdaCombined() {
        List<Integer> integerList1 = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7);
        List<Integer> integerList2 = CollectionUtils.ofList(9, 11, 4, 8, 2, 5, 7);
        List<Integer> combined = LambdaUtils.combined(integerList1, integerList2);
        LambdaUtils.sort(combined, (o1, o2) -> o2 - o1);
        Assert.assertEquals(8, combined.size());
    }

    @Test
    public void testLambdaIntersection() {
        List<Integer> integerList1 = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7);
        List<Integer> integerList2 = CollectionUtils.ofList(9, 11, 4, 8, 2, 5, 7);
        List<Integer> intersection = LambdaUtils.intersection(integerList1, integerList2);
        Assert.assertEquals(5, intersection.size());
    }

    @Test
    public void testLambdaReduceSum() {
        List<Integer> integerList1 = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7);
        long reduce = LambdaUtils.sum(integerList1);
        Assert.assertEquals(29, reduce);
    }

    @Test
    public void testLambdaReduceSum1() {
        List<Byte> integerList1 = CollectionUtils.ofList((byte) 1, (byte) 127, (byte) 3);
        int sum = LambdaUtils.sum(integerList1);
        Assert.assertEquals(131, sum);
    }

    @Test
    public void testLambdaReduceMin() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        Integer min = LambdaUtils.min(integerList);
        Assert.assertEquals(1, (int) min);
    }

    @Test
    public void testLambdaReduceMax() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        Integer max = LambdaUtils.max(integerList);
        Assert.assertEquals(456, (int) max);
    }

    @Test
    public void testLambdaReduceMax1() {
        List<Byte> byteList = new ArrayList<>();
        byteList.add((byte) 1);
        byteList.add((byte) 8);
        byteList.add((byte) 2);
        byteList.add((byte) 9);
        Byte max = LambdaUtils.max(byteList);
        Assert.assertEquals(9, (int) max);
    }

    @Test
    public void testLambdaReduceMin2() {
        List<BigDecimal> integerList = CollectionUtils.ofList(BigDecimal.valueOf(1.50), BigDecimal.valueOf(2.120));
        BigDecimal min = LambdaUtils.min(integerList);
        Assert.assertEquals(min, BigDecimal.valueOf(1.50));
    }

    @Test
    public void testLambdaReduceMin3() {
        List<Byte> byteList = new ArrayList<>();
        byteList.add((byte) 1);
        byteList.add((byte) 8);
        byteList.add((byte) 2);
        byteList.add((byte) 9);
        Byte min = LambdaUtils.min(byteList);
        Assert.assertEquals(1, (int) min);
    }

    @Test
    public void testLambdaAnyMatch() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        boolean b = LambdaUtils.anyMatch(integerList, t -> t == 1);
        Assert.assertEquals(Boolean.TRUE, b);
    }

    @Test
    public void testLambdaAllMatch() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        boolean b = LambdaUtils.allMatch(integerList, t -> t <= 456);
        Assert.assertEquals(Boolean.TRUE, b);
    }

    @Test
    public void testLambdaDifference() {
        List<Integer> difference1 = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7);
        List<Integer> difference2 = CollectionUtils.ofList(9, 11, 4, 8, 2, 5, 7);
        List<Integer> difference = LambdaUtils.difference(difference1, difference2);
        Assert.assertEquals(2, difference.size());
    }

}
