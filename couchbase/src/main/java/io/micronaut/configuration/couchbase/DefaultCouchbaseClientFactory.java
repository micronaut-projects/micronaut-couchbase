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

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;

import javax.inject.Singleton;

/**
 * Builds the primary Couchbase Cluster.
 *
 * @author Graham Pople
 * @since 1.0
 */
@Requires(classes = Cluster.class)
@Requires(beans = DefaultCouchbaseConfiguration.class)
@Factory
public class DefaultCouchbaseClientFactory {

    /**
     * Factory method to return a Couchbase Cluster.
     * @param configuration an injected DefaultCouchbaseConfiguration
     * @return a Couchbase Cluster
     */
    @Primary
    @Singleton
    Cluster couchbaseCluster(DefaultCouchbaseConfiguration configuration) {
        // Need to destroy env at end
        CouchbaseEnvironment env = configuration.buildEnvironment();
        Cluster cluster = CouchbaseCluster.create(env);

        if (!configuration.authDisabled) {
            cluster.authenticate(configuration.username, configuration.password);
        }

        return cluster;
    }
}
