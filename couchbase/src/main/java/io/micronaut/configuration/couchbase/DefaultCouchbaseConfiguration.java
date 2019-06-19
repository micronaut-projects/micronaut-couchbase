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
import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.ApplicationConfiguration;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * The default Couchbase configuration class.
 *
 * @author Graham Pople
 * @since 1.0
 */
@Requires(classes = CouchbaseEnvironment.class)
@ConfigurationProperties(CouchbaseSettings.PREFIX)
public class DefaultCouchbaseConfiguration extends AbstractCouchbaseConfiguration {

    @NotBlank
    @NotNull
    String uri;

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String password;

    private boolean authDisabled = false;

    private Port port = new Port();

    @ConfigurationBuilder(prefixes = "")
    protected DefaultCouchbaseEnvironment.Builder config =
            DefaultCouchbaseEnvironment.builder();


    /**
     * Constructor.
     * @param applicationConfiguration applicationConfiguration
     */
    public DefaultCouchbaseConfiguration(ApplicationConfiguration applicationConfiguration) {
        super(applicationConfiguration);
    }

    @Inject
    public void setPort(Port port) {
        this.port = port;
    }

    public boolean isAuthDisabled() {
        return authDisabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthDisabled(boolean authDisabled) {
        this.authDisabled = authDisabled;
    }

    /**
     * @return Builds the Couchbase ClusterEnvironment
     */
    public CouchbaseEnvironment buildEnvironment() {


        port.getHttp().ifPresent(config::bootstrapHttpDirectPort);
        port.getCarrier().ifPresent(config::bootstrapCarrierDirectPort);
        // There are a number of other Couchbase parameters here than can be exposed

        return config.build();
    }

    /**
     * Port configuration, e.g. what ports the Couchbase cluster has exposed for management.
     */
    @ConfigurationProperties("port")
    public static class Port {
        private Integer http;
        private Integer carrier;

        /**
         * @return The HTTP port
         */
        public OptionalInt getHttp() {
            if (http != null) {
                return OptionalInt.of(http);
            }
            return OptionalInt.empty();
        }

        public void setHttp(Integer http) {
            this.http = http;
        }

        /**
         * @return The carrier port
         */
        public OptionalInt getCarrier() {
            if (carrier != null) {
                return OptionalInt.of(carrier);
            }
            return OptionalInt.empty();
        }

        public void setCarrier(Integer carrier) {
            this.carrier = carrier;
        }
    }
}
