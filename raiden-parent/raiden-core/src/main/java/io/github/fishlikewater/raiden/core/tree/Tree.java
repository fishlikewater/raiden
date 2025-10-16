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
package io.github.fishlikewater.raiden.core.tree;

import io.github.fishlikewater.raiden.core.LambdaUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * {@code Tree}
 * 树结构
 *
 * @author zhangxiang
 * @since 2025/10/15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tree<K extends Serializable, V> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1408984608206201831L;

    /**
     * 节点
     */
    private List<TreeNode<K, V>> nodes;

    public Tree<K, V> addNodes(List<TreeNode<K, V>> nodes) {
        this.nodes.addAll(nodes);
        return this;
    }

    /**
     * 构建树
     *
     * @param nodes 节点
     * @param <K>   id
     * @param <V>   value
     * @return 树
     */
    public static <K extends Serializable, V> Tree<K, V> build(List<TreeNode<K, V>> nodes) {
        return Tree.<K, V>builder()
                .nodes(nodes)
                .build();
    }

    /**
     * 构建树
     *
     * @param list          数据
     * @param keyFunc       id
     * @param parentKeyFunc 父id
     * @param valueFunc     value
     * @param disabledFunc  禁用
     * @param <T>           数据类型
     * @param <K>           id
     * @param <V>           value
     * @return 树
     */
    public static <T, K extends Serializable, V> Tree<K, V> build(List<T> list,
                                                                  Function<T, K> keyFunc,
                                                                  Function<T, K> parentKeyFunc,
                                                                  Function<T, V> valueFunc,
                                                                  Function<V, Boolean> disabledFunc) {
        List<K> keys = new ArrayList<>();
        Set<K> parentKeys = new HashSet<>();
        for (T item : list) {
            K key = keyFunc.apply(item);
            K parentKey = parentKeyFunc.apply(item);
            keys.add(key);
            parentKeys.add(parentKey);
        }
        keys.forEach(parentKeys::remove);
        List<TreeNode<K, V>> nodes = getTreeNode(parentKeys, list, keyFunc, parentKeyFunc, valueFunc, disabledFunc);
        LambdaUtils.handle(nodes, node -> addChildren(node, list, keyFunc, parentKeyFunc, valueFunc, disabledFunc));
        return Tree.<K, V>builder()
                .nodes(nodes)
                .build();
    }

    // ----------------------------------------------------------------

    /**
     * 构建树节点
     *
     * @param node          树节点
     * @param list          数据
     * @param keyFunc       id
     * @param parentKeyFunc 父id
     * @param valueFunc     value
     * @param disabledFunc  禁用
     * @param <T>           数据类型
     * @param <K>           id
     * @param <V>           value
     */
    private static <V, K extends Serializable, T> void addChildren(TreeNode<K, V> node,
                                                                   List<T> list,
                                                                   Function<T, K> keyFunc,
                                                                   Function<T, K> parentKeyFunc,
                                                                   Function<T, V> valueFunc,
                                                                   Function<V, Boolean> disabledFunc) {
        K id = node.getId();
        List<T> children = LambdaUtils.filter(list, t -> ObjectUtils.equals(parentKeyFunc.apply(t), id));
        if (ObjectUtils.isNotNullOrEmpty(children)) {
            List<TreeNode<K, V>> treeNodes = LambdaUtils.toList(children, t -> buildTreeNode(t, keyFunc, valueFunc, disabledFunc));
            node.setLeaf(true);
            node.setChildren(treeNodes);
            LambdaUtils.handle(treeNodes, cNode -> addChildren(cNode, list, keyFunc, parentKeyFunc, valueFunc, disabledFunc));
        }
    }

    /**
     * 获取树节点
     *
     * @param parentKeys    父id
     * @param list          数据
     * @param keyFunc       id
     * @param parentKeyFunc 父id函数
     * @param valueFunc     value
     * @param disabledFunc  禁用
     * @param <T>           数据类型
     * @param <K>           id
     * @param <V>           value
     * @return 树节点
     */
    private static <T, K extends Serializable, V> List<TreeNode<K, V>> getTreeNode(Set<K> parentKeys,
                                                                                   List<T> list,
                                                                                   Function<T, K> keyFunc,
                                                                                   Function<T, K> parentKeyFunc,
                                                                                   Function<T, V> valueFunc,
                                                                                   Function<V, Boolean> disabledFunc) {
        List<T> children = LambdaUtils.filter(list, t -> parentKeys.contains(parentKeyFunc.apply(t)));
        return LambdaUtils.toList(children, t -> buildTreeNode(t, keyFunc, valueFunc, disabledFunc));
    }

    /**
     * 构建树节点
     *
     * @param t            数据
     * @param keyFunc      id
     * @param valueFunc    value
     * @param disabledFunc 禁用
     * @param <T>          数据类型
     * @param <K>          id
     * @param <V>          value
     * @return 树节点
     */
    private static <T, K extends Serializable, V> TreeNode<K, V> buildTreeNode(T t,
                                                                               Function<T, K> keyFunc,
                                                                               Function<T, V> valueFunc,
                                                                               Function<V, Boolean> disabledFunc) {
        return TreeNode.<K, V>builder()
                .id(keyFunc.apply(t))
                .value(valueFunc.apply(t))
                .leaf(false)
                .disabled(disabledFunc.apply(valueFunc.apply(t)))
                .children(null)
                .build();
    }
}
