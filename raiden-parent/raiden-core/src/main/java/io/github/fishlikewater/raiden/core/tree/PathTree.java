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

import io.github.fishlikewater.raiden.core.LambdaUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.references.org.springframework.util.AntPathMatcher;
import io.github.fishlikewater.raiden.core.references.org.springframework.util.PathMatcher;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * {@code PathTree}
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/07/25
 */
@Data
public class PathTree implements Serializable {

    @Serial
    private static final long serialVersionUID = 8719209180538579970L;

    private final PathTreeNode root;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    public PathTree() {
        this.root = new PathTreeNode();
    }

    public void insert(String path) {
        PathTreeNode node = root;
        String[] paths = path.split(CommonConstants.Symbol.SYMBOL_PATH);
        for (String str : paths) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            if (!node.children.containsKey(str)) {
                PathTreeNode pathTreeNode = new PathTreeNode(str);
                node.children.put(str, pathTreeNode);
            }
            node = node.children.get(str);
        }
        node.setEnd(true);
    }

    public String searchPath(String uri) {
        PathTreeNode node = root;
        String symbolPath = CommonConstants.Symbol.SYMBOL_PATH;
        String[] paths = uri.split(symbolPath);
        StringBuilder definedUrl = new StringBuilder();
        if (uri.startsWith(symbolPath)) {
            definedUrl.append(symbolPath);
        }
        for (String path : paths) {
            if (StringUtils.isBlank(path)) {
                continue;
            }
            if (node.children.containsKey(path)) {
                definedUrl.append(path).append(symbolPath);
                node = node.children.get(path);
            } else {
                Map<String, PathTreeNode> children = node.children;
                boolean matchEndNode = this.matchEndNode(children, definedUrl, uri);
                if (!matchEndNode) {
                    return null;
                }
                break;
            }
        }
        return definedUrl.deleteCharAt(definedUrl.length() - 1).toString();
    }

    public void clear() {
        root.children.clear();
    }

    private boolean matchEndNode(Map<String, PathTreeNode> children,
                                 StringBuilder definedUrl,
                                 String url) {
        Collection<PathTreeNode> values = children.values();
        PathTreeNode treeNode = LambdaUtils.findFirst(values, value -> value.isEnd() && value.getPath().contains("*"));
        if (ObjectUtils.isNotNullOrEmpty(treeNode)) {
            String s = definedUrl.toString() + treeNode.getPath();
            if (pathMatcher.match(s, url)) {
                definedUrl.append(treeNode.getPath()).append(CommonConstants.Symbol.SYMBOL_PATH);
                return true;
            }
        }
        return false;
    }
}
