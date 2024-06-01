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
package com.github.fishlikewater.raiden.generate;

/**
 * <p>
 * {@code GenerateUtils}
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年06月01日 9:58
 **/
public interface GenerateUtils {

    Generate<String> NATION = new NationGenerate();

    Generate<String> USER_NAME = new UserNameGenerate();

    Generate<Integer> AGE = new AgeGenerate();

    Generate<String> TELEPHONE = new PhoneNumberGenerate.Telephone();

    Generate<String> TELEPHONE_AREA = new PhoneNumberGenerate.Telephone(true);

    Generate<String> MOBILEPHONE = new PhoneNumberGenerate.Mobilephone();

    Generate<String> EMAIL = new EmailGenerate();
}
