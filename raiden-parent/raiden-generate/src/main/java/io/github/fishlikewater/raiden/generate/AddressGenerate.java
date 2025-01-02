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
package io.github.fishlikewater.raiden.generate;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.generate.model.AddressModel;

/**
 * {@code AddressGenerate}
 * 地址生成器
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/18
 */
public class AddressGenerate implements FileConfigRead {

    private JSONArray provinces;

    public AddressModel generate() {
        this.init();
        int i = RandomUtils.randomInt(0, provinces.size());
        JSONObject province = provinces.getJSONObject(i);
        AddressModel addressModel = AddressModel.builder()
                .province(province.getStr("name"))
                .provinceAreaCode(province.getStr("areaCode"))
                .build();
        JSONArray cs = province.getJSONArray("districts");
        if (ObjectUtils.isNullOrEmpty(cs)) {
            return addressModel;
        }
        int j = RandomUtils.randomInt(0, cs.size());
        JSONObject city = cs.getJSONObject(j);
        addressModel.setCity(city.getStr("name"));
        addressModel.setCityCode(city.getStr("cityCode"));
        addressModel.setCityAreaCode(city.getStr("areaCode"));
        JSONArray districts = city.getJSONArray("districts");
        if (ObjectUtils.isNullOrEmpty(districts)) {
            return addressModel;
        }
        int k = RandomUtils.randomInt(0, districts.size());
        JSONObject district = districts.getJSONObject(k);

        addressModel.setDistrict(district.getStr("name"));
        addressModel.setDistrictAreaCode(district.getStr("areaCode"));

        return addressModel;
    }

    private void init() {
        if (ObjectUtils.isNotNullOrEmpty(this.provinces)) {
            return;
        }
        this.provinces = this.readFileAsJsonArray("area-full.json");
    }
}
