import com.couchbase.client.core.lang.Tuple2;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.mock.CouchbaseMock;
import io.micronaut.configuration.couchbase.CouchbaseSettings;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import org.junit.Test;
import util.TestUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests related to the default Couchbase configuration.
 *
 * @author Graham Pople
 * @since 1.0.0
 */
public class DefaultTest {
    @Test
    public void injectCluster() throws IOException {
        ApplicationContext applicationContext = ApplicationContext.run();
        CouchbaseEnvironment env = applicationContext.getBean(CouchbaseEnvironment.class);
        assertEquals(1000, env.managementTimeout());
        Cluster cluster = applicationContext.getBean(Cluster.class);
        assertNotNull(cluster);
    }

    @Test
    public void basicKeyValueOperations() throws IOException, InterruptedException {
        String bucketName = "default";
        // Start the Couchbase mock process
        Tuple2<ApplicationContext, CouchbaseMock> contentAndMock = TestUtil.initCouchbaseMock(bucketName);

        try {
            // Access a Couchbase cluster
            Cluster cluster = contentAndMock.value1().getBean(Cluster.class);

            // Access a Couchbase bucket resource on the cluster
            Bucket bucket = cluster.openBucket(bucketName);

            // Upsert some JSON to the key "id"
            bucket.upsert(JsonDocument.create("id", JsonObject.create().put("foo", "bar")));

            // Get that JSON back
            JsonDocument result = bucket.get("id");

            // Check it's what's expected
            assertEquals("bar", result.content().getString("foo"));
        }
        finally {
            // Finish by stopping the Couchbase mock
            contentAndMock.value2().stop();
        }
    }
}
