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
package io.github.fishlikewater.raiden.generate.model;

import io.github.fishlikewater.raiden.core.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * {@code AddressModel}
 * 地址模型
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel implements Serializable {

    @Serial
    private static final long serialVersionUID = -269607744556442755L;

    private String province;

    private String provinceAreaCode;

    private String city;

    private String cityAreaCode;

    private String cityCode;

    private String district;

    private String districtAreaCode;

    public String areaCode() {
        if (StringUtils.isNotBlank(this.districtAreaCode)) {
            return this.districtAreaCode;
        }
        if (StringUtils.isNotBlank(this.cityAreaCode)) {
            return this.cityAreaCode;
        }
        return this.provinceAreaCode;
    }

    public String address() {
        String address = this.province;
        if (StringUtils.isNotBlank(this.city)) {
            address += this.city;
        }
        if (StringUtils.isNotBlank(this.district)) {
            address += this.district;
        }
        return address;
    }
}
