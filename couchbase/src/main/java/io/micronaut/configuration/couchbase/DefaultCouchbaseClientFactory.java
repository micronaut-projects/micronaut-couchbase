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

import com.couchbase.client.java.Cluster;
import io.micronaut.context.annotation.Bean;
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
     */
//    @Bean(preDestroy = "shutdown") // TODO
    @Primary
    @Singleton
//    Cluster couchbaseCluster(DefaultCouchbaseConfiguration configuration) {
    Cluster couchbaseCluster() {

        // TODO: should not depend on a hardcoded config, but pull in DefaultCouchbaseConfiguration instead.  Currently
        // this hits error:
        // io.micronaut.context.exceptions.BeanInstantiationException: Error instantiating bean of type  [com.couchbase.client.java.Cluster]

        // Message: tried to access class com.couchbase.client.core.env.ServiceConfig$Builder from class io.micronaut.configuration.couchbase.$DefaultCouchbaseConfigurationDefinition
        // Path Taken: Cluster.couchbaseCluster([DefaultCouchbaseConfiguration configuration])

        return Cluster.connect("localhost", "Administrator", "password");
    }
}
