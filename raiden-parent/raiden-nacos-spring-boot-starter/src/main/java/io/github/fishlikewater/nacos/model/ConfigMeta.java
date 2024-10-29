package io.github.fishlikewater.nacos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * {@code ConfigMeta}
 * 配置元数据
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMeta implements Serializable {

    @Serial
    private static final long serialVersionUID = 9141795287777359573L;

    private String groupId;

    private String dataId;

    private String type;
}
