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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * {@code TreeNode}
 * 数节点
 *
 * @author zhangxiang
 * @since 2025/10/15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode<K, V> implements Serializable {

    @Serial
    private static final long serialVersionUID = -9080695642789105704L;

    /**
     * id
     */
    private K id;

    /**
     * 值
     */
    private V value;

    /**
     * 是否有叶子节点
     */
    private boolean leaf;

    /**
     * 禁用
     */
    private boolean disabled;

    /**
     * 子节点
     */
    private List<TreeNode<K, V>> children;
}
