package io.github.fishlikewater.nacos.annotation;

import io.github.fishlikewater.nacos.constant.Constants;
import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

/**
 * {@code NacosRefresh}
 * 支持nacos 刷新标记的bean
 *
 * @author zhangxiang
 * @version 1.0.7
 * @since 2024/10/29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Scope(value = Constants.NACOS_REFRESH_SCOPE)
public @interface NacosRefreshScope {

}
