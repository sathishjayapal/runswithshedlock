/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.providers;

import jakarta.annotation.PostConstruct;
import java.util.Optional;
import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;

/**
 * The type Hateoas hal provider.
 *
 * @author bnasslahsen
 */
public class HateoasHalProvider {

    /**
     * The Object mapper provider.
     */
    protected final ObjectMapperProvider objectMapperProvider;

    /**
     * The Hateoas properties optional.
     */
    private final Optional<HateoasProperties> hateoasPropertiesOptional;

    /**
     * Instantiates a new Hateoas hal provider.
     *
     * @param hateoasPropertiesOptional the hateoas properties optional
     * @param objectMapperProvider      the object mapper provider
     */
    public HateoasHalProvider(
            Optional<HateoasProperties> hateoasPropertiesOptional, ObjectMapperProvider objectMapperProvider) {
        this.hateoasPropertiesOptional = hateoasPropertiesOptional;
        this.objectMapperProvider = objectMapperProvider;
    }

    /**
     * Init.
     */
    @PostConstruct
    protected void init() {
        if (!isHalEnabled()) return;
        if (!Jackson2HalModule.isAlreadyRegisteredIn(objectMapperProvider.jsonMapper()))
            objectMapperProvider.jsonMapper().registerModule(new Jackson2HalModule());
    }

    /**
     * Is hal enabled boolean.
     *
     * @return the boolean
     */
    public boolean isHalEnabled() {
        return hateoasPropertiesOptional
                .map(HateoasProperties::isUseHalAsDefaultJsonMediaType)
                .orElse(true);
    }
}
