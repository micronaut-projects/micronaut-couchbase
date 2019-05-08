import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import io.micronaut.context.ApplicationContext;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests related to the default Couchbase configuration.
 *
 * Note that these are integration tests and require:
 *
 * A Couchbase cluster (any version) to be running on localhost
 * With credentials of Administrator:password
 * And a bucket created named "default"
 *
 * @author Graham Pople
 * @since 1.0.0
 */
public class DefaultTest {
    @Test
    public void injectCluster() {
        ApplicationContext applicationContext = ApplicationContext.run();
        Cluster cluster = applicationContext.getBean(Cluster.class);
        assertNotNull(cluster);
    }

    @Test
    public void basicKeyValueOperations() {
        ApplicationContext applicationContext = ApplicationContext.run();
        Cluster cluster = applicationContext.getBean(Cluster.class);

        Collection collection = cluster.bucket("default").defaultCollection();

        collection.upsert("id", JsonObject.create().put("foo", "bar"));

        Optional<GetResult> result = collection.get("id");

        assertTrue(result.isPresent());
        assertEquals("bar", result.get().contentAs(JsonObject.class).getString("foo"));
    }
}
