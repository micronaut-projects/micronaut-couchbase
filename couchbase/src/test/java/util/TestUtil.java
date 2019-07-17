package util;

import com.couchbase.client.core.lang.Tuple2;
import com.couchbase.mock.Bucket;
import com.couchbase.mock.BucketConfiguration;
import com.couchbase.mock.CouchbaseMock;
import com.couchbase.mock.memcached.MemcachedServer;
import io.micronaut.configuration.couchbase.CouchbaseSettings;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class TestUtil {
    private TestUtil() {
    }

    /**
     * The Couchbase Mock is a Java implementation of the Couchbase cluster used for testing.
     *
     * @return a tuple containing the ApplicationContext, configured appropriately for connecting to a constructed
     *          running Couchbase mock, and a reference to that mock.  The mock should be stopped once no longer needed.
     */
    public static Tuple2<ApplicationContext, CouchbaseMock> initCouchbaseMock(String bucketName) throws IOException, InterruptedException {
        BucketConfiguration bucketConfig = new BucketConfiguration();
        bucketConfig.type = Bucket.BucketType.COUCHBASE;
        bucketConfig.numVBuckets = 64;
        bucketConfig.numNodes = 1;
        bucketConfig.numReplicas = 0;
        bucketConfig.name = bucketName;

        CouchbaseMock mock = new CouchbaseMock(0, Collections.singletonList(bucketConfig));
        mock.start();
        mock.waitForStartup();

        System.out.println("Mock has started up on " + mock.getHttpHost() + ":" + mock.getHttpPort());

        for (Bucket bucket : mock.getBuckets().values()) {
            for (MemcachedServer server : bucket.getServers()) {
                server.setCccpEnabled(true);
            }
        }

        Map<String, Object> settings = CollectionUtils.mapOf(
                // These auth settings are hardcoded into the mock
                CouchbaseSettings.PREFIX + "." + CouchbaseSettings.USERNAME, "Administrator",
                CouchbaseSettings.PREFIX + "." + CouchbaseSettings.PASSWORD, "password");

        settings.put(CouchbaseSettings.PREFIX + "." + CouchbaseSettings.AUTH_DISABLED, true);
        settings.put(CouchbaseSettings.PREFIX + "." + CouchbaseSettings.URI, mock.getHttpHost());
        settings.put(CouchbaseSettings.PREFIX + "." + CouchbaseSettings.PORT_HTTP, mock.getHttpPort());
        settings.put(CouchbaseSettings.PREFIX + "." + CouchbaseSettings.PORT_CARRIER, mock.getCarrierPort(bucketName));

        ApplicationContext applicationContext = ApplicationContext.run(
                PropertySource.of("test", settings),
                "test");

        return com.couchbase.client.core.lang.Tuple.create(applicationContext, mock);
    }
}
