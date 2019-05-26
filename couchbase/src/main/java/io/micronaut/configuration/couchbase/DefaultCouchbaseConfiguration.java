/*
 * Copyright 2017-2019 original authors
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

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.runtime.ApplicationConfiguration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * The default Couchbase configuration class.
 *
 * @author Graham Pople
 * @since 1.0
 */
//@Requires(classes = ClusterEnvironment.class)
@ConfigurationProperties(CouchbaseSettings.PREFIX)
public class DefaultCouchbaseConfiguration extends AbstractCouchbaseConfiguration {

    @NotBlank
    @NotNull
    String uri;

    @NotBlank
    @NotNull
    String username;

    @NotBlank
    @NotNull
    String password;

    Boolean authDisabled = false;

    Port port = new Port();


    // Long term it would be preferable to pull in the config directly to a ClusterEnvironment.Builder using
    // ConfigurationBuilder, but this hits this error (Graal related?) so only supporting a limited config for now:
    // Message: tried to access class com.couchbase.client.core.env.ServiceConfig$Builder from class io.micronaut.configuration.couchbase.$DefaultCouchbaseConfigurationDefinition
    // Path Taken: Cluster.couchbaseCluster([DefaultCouchbaseConfiguration configuration])


    /**
     * Constructor.
     * @param applicationConfiguration applicationConfiguration
     */
    public DefaultCouchbaseConfiguration(ApplicationConfiguration applicationConfiguration) {
        super(applicationConfiguration);
    }


    /**
     * @return Builds the Couchbase ClusterEnvironment
     */
    public CouchbaseEnvironment buildEnvironment() {

        DefaultCouchbaseEnvironment.Builder builder = DefaultCouchbaseEnvironment.builder();

        port.http.ifPresent(builder::bootstrapHttpDirectPort);
        port.carrier.ifPresent(builder::bootstrapCarrierDirectPort);
        // There are a number of other Couchbase parameters here than can be exposed

        return builder.build();
    }

    /**
     * Port configuration, e.g. what ports the Couchbase cluster has exposed for management.
     */
    @ConfigurationProperties("port")
    public static class Port {
        Optional<Integer> http = Optional.empty();
        Optional<Integer> carrier = Optional.empty();
    }
}
