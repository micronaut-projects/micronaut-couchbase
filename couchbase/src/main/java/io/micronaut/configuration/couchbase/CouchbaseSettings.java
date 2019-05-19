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

/**
 * Common constants for Couchbase settings.
 *
 * Note that this may not be the final names for these settings, this is just to get bootstrapping.
 *
 * @author Graham Pople
 * @since 1.0
 */
public interface CouchbaseSettings {
    /**
     * The prefix to use for all Couchbase settings.
     */
    String PREFIX = "couchbase";

    /**
     * The hostname of a Couchbase node in the cluster to connect to.
     */
    String URI = "uri";

    /**
     * The username of the Couchbase user that will be accessing the cluster.
     */
    String USERNAME = "username";

    /**
     * The password of the Couchbase user that will be accessing the cluster.
     */
    String PASSWORD = "password";

    /**
     * The configuration port (KV) to use for connecting to the cluster.  Usually only used in test or development
     * scenarios.
     */
    String PORT_KV = "port.kv";

    /**
     * The configuration port (HTTP) to use for connecting to the cluster.  Usually only used in test or development
     * scenarios.
     */
    String PORT_HTTP = "port.http";

}
