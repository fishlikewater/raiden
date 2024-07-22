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
package io.github.fishlikewater.raiden.generate;

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

    io.github.fishlikewater.raiden.generate.Generate<String> NATION = new io.github.fishlikewater.raiden.generate.NationGenerate();

    io.github.fishlikewater.raiden.generate.Generate<String> USER_NAME = new io.github.fishlikewater.raiden.generate.UserNameGenerate();

    io.github.fishlikewater.raiden.generate.Generate<Integer> AGE = new io.github.fishlikewater.raiden.generate.AgeGenerate();

    io.github.fishlikewater.raiden.generate.Generate<String> TELEPHONE = new io.github.fishlikewater.raiden.generate.PhoneNumberGenerate.Telephone();

    io.github.fishlikewater.raiden.generate.Generate<String> TELEPHONE_AREA = new io.github.fishlikewater.raiden.generate.PhoneNumberGenerate.Telephone(true);

    io.github.fishlikewater.raiden.generate.Generate<String> MOBILE_PHONE = new PhoneNumberGenerate.MobilePhone();

    io.github.fishlikewater.raiden.generate.Generate<String> EMAIL = new io.github.fishlikewater.raiden.generate.EmailGenerate();

    io.github.fishlikewater.raiden.generate.DateTimeGenerate.DateGenerate DATE = new io.github.fishlikewater.raiden.generate.DateTimeGenerate.DateGenerate();

    io.github.fishlikewater.raiden.generate.DateTimeGenerate.TimeGenerate TIME = new io.github.fishlikewater.raiden.generate.DateTimeGenerate.TimeGenerate();

    io.github.fishlikewater.raiden.generate.DateTimeGenerate.FullDateTimeGenerate DATETIME = new io.github.fishlikewater.raiden.generate.DateTimeGenerate.FullDateTimeGenerate();

    io.github.fishlikewater.raiden.generate.DateTimeGenerate.TimeStampGenerate TIMESTAMP = new io.github.fishlikewater.raiden.generate.DateTimeGenerate.TimeStampGenerate();

    io.github.fishlikewater.raiden.generate.Generate<String> ID_CARD = new io.github.fishlikewater.raiden.generate.IdCardGenerate();

    io.github.fishlikewater.raiden.generate.Generate<String> IP = new io.github.fishlikewater.raiden.generate.IpGenerate();

    io.github.fishlikewater.raiden.generate.AddressGenerate ADDRESS = new io.github.fishlikewater.raiden.generate.AddressGenerate();
}
