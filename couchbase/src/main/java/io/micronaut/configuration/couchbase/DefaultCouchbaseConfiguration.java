/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.configuration.couchbase;

import com.couchbase.client.java.env.ClusterEnvironment;
import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.ApplicationConfiguration;

/**
 * The default Couchbase configuration class.
 *
 * @author Graham Pople
 * @since 1.0
 */
//@Requires(property = CouchbaseSettings.PREFIX) // TODO
@Requires(classes = ClusterEnvironment.class)
@ConfigurationProperties(CouchbaseSettings.PREFIX)
public class DefaultCouchbaseConfiguration extends AbstractCouchbaseConfiguration {

    @ConfigurationBuilder(prefixes = "", configurationPrefix = "options")
    // TODO needs to get username and password, plus other settings, from application.yml
    protected ClusterEnvironment.Builder clientOptions = ClusterEnvironment.builder("", "");

    /**
     * Constructor.
     * @param applicationConfiguration applicationConfiguration
     */
    public DefaultCouchbaseConfiguration(ApplicationConfiguration applicationConfiguration) {
        super(applicationConfiguration);
    }


    /**
     * @return Builds the Couchbase URI
     */
    public ClusterEnvironment buildEnvironment() {
        return clientOptions.build();
    }
}
