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

import io.github.fishlikewater.raiden.core.tree.Tree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code TreeTest}
 *
 * @author zhangxiang
 * @since 2025/10/16
 */
public class TreeTest {

    @Test
    public void test() {
        List<TestTreeModel> list = new ArrayList<>();
        list.add(new TestTreeModel("1", "一", "0"));
        list.add(new TestTreeModel("2", "二", "0"));
        list.add(new TestTreeModel("11", "一一", "1"));
        list.add(new TestTreeModel("112", "一一二", "11"));
        list.add(new TestTreeModel("21", "二一", "2"));

        Tree<String, String> tree = Tree.build(
                list,
                TestTreeModel::getId,
                TestTreeModel::getParentId,
                TestTreeModel::getName,
                t -> t.getName().equals("一"));

        System.out.println(tree);
    }

}
