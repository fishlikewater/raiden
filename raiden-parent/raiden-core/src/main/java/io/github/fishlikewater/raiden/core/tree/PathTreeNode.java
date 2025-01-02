/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code PathTreeNode}
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/07/25
 */
@Data
public class PathTreeNode implements Serializable {

    @Serial
    private static final long serialVersionUID = 8881964200916635226L;

    String path;

    private boolean end;

    public Map<String, PathTreeNode> children = new HashMap<>();

    public PathTreeNode() {}

    public PathTreeNode(String path) {
        this.path = path;
    }
}
