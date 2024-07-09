/*
 * Copyright (c) 2024 zhangxiang (fishlikewater@126.com)
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

import java.util.List;
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
        Assert.assertEquals((int) list.getFirst(), 2);
    }

    @Test
    public void testLambdaDistinct() {
        List<Integer> integerList = List.of(1, 2, 4, 8, 2);
        List<Integer> list = LambdaUtils.toList(integerList, integer -> integer, true);
        Assert.assertEquals(list.size(), 4);
    }

    @Test
    public void testLambdaFindFirst() {
        List<Integer> integerList = List.of(1, 2, 4, 8, 2);
        Integer first = LambdaUtils.findFirst(integerList, integer -> integer > 2);
        Assert.assertEquals((int) first, 4);
    }

    @Test
    public void testLambdaFindAny() {
        List<Integer> integerList = List.of(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        Integer first = LambdaUtils.findAny(integerList, integer -> integer > 9);
        Assert.assertNotEquals((int) first, 1);
    }

    @Test
    public void testLambdaSort() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        LambdaUtils.sort(integerList, (o1, o2) -> o2 - o1);
        Assert.assertEquals((int) integerList.getFirst(), 456);
    }

    @Test
    public void testLambdaSort2() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        TreeSet<Integer> treeSet = new TreeSet<>(integerList);
        List<Integer> sort = LambdaUtils.sort(treeSet, (o1, o2) -> o2 - o1);
        Assert.assertEquals((int) sort.getFirst(), 456);
    }

    @Test
    public void testLambdaGroupBy() {
        List<Integer> integerList = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7, 6, 1, 7, 1, 9, 41, 456, 15, 46, 75, 165);
        var groupBy = LambdaUtils.groupBy(integerList, integer -> integer % 2);
        Assert.assertEquals(groupBy.get(0).size(), 7);
    }

    @Test
    public void testLambdaCombined() {
        List<Integer> integerList1 = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7);
        List<Integer> integerList2 = CollectionUtils.ofList(9, 11, 4, 8, 2, 5, 7);
        List<Integer> combined = LambdaUtils.combined(integerList1, integerList2);
        LambdaUtils.sort(combined, (o1, o2) -> o2 - o1);
        Assert.assertEquals(combined.size(), 8);
    }

    @Test
    public void testLambdaIntersection() {
        List<Integer> integerList1 = CollectionUtils.ofList(1, 2, 4, 8, 2, 5, 7);
        List<Integer> integerList2 = CollectionUtils.ofList(9, 11, 4, 8, 2, 5, 7);
        List<Integer> intersection = LambdaUtils.intersection(integerList1, integerList2);
        Assert.assertEquals(intersection.size(), 5);
    }
}
