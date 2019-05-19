package util;

import com.couchbase.mock.Bucket;
import com.couchbase.mock.BucketConfiguration;
import com.couchbase.mock.CouchbaseMock;
import io.micronaut.configuration.couchbase.CouchbaseSettings;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.fail;

public class TestUtil {
    private TestUtil() {
    }

    /**
     * The Couchbase Mock is a Java implementation of the Couchbase cluster used for testing.
     *
     * @return the ApplicationContext, configured appropriately for connecting to a constructed running Couchbase mock
     */
    public static ApplicationContext initCouchbaseMock(String bucketName) throws IOException, InterruptedException {
        BucketConfiguration bucketConfig = new BucketConfiguration();
        bucketConfig.type = Bucket.BucketType.COUCHBASE;
        bucketConfig.numVBuckets = 64;
        bucketConfig.numNodes = 1;
        bucketConfig.numReplicas = 0;
        bucketConfig.name = bucketName;

        CouchbaseMock mock = new CouchbaseMock(9001, Collections.singletonList(bucketConfig));
        mock.start();
        mock.waitForStartup();

        Map<String, Object> settings = CollectionUtils.mapOf(
                // These auth settings are hardcoded into the mock
                CouchbaseSettings.PREFIX + "." + CouchbaseSettings.USERNAME, "Administrator",
                CouchbaseSettings.PREFIX + "." + CouchbaseSettings.PASSWORD, "password");

        settings.put(CouchbaseSettings.PREFIX + "." + CouchbaseSettings.URI, "localhost");
        settings.put(CouchbaseSettings.PREFIX + "." + CouchbaseSettings.PORT_HTTP, 9001);

        ApplicationContext applicationContext = ApplicationContext.run(
                PropertySource.of("test", settings),
                "test");

        return applicationContext;
    }
}
