package easypool;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TestObjectPool
{
    private ObjectPool<ExportingProcess> pool;

    private AtomicLong processNo = new AtomicLong(0);

    @Before
    public void setUp() {
        // Create a pool of objects of type ExportingProcess. Parameters:
        // 1) Minimum number of special ExportingProcess instances residing in the pool = 4
        // 2) Maximum number of special ExportingProcess instances residing in the pool = 10
        // 3) Time in seconds for periodical checking of minIdle / maxIdle conditions in a separate thread = 5.
        //    When the number of ExportingProcess instances is less than minIdle, missing instances will be created.
        //    When the number of ExportingProcess instances is greater than maxIdle, too many instances will be removed.
        //    If the validation interval is negative, no periodical checking of minIdle / maxIdle conditions
        //    in a separate thread take place. These boundaries are ignored then.
        pool = new ObjectPool<ExportingProcess>(4, 10, 5)
        {
            protected ExportingProcess createObject() {
                // create a test object which takes some time for creation
                return new ExportingProcess("/home/temp/", processNo.incrementAndGet());
            }
        };
    }

    @After
    public void tearDown() {
        pool.shutdown();
    }

    @Test
    public void testObjectPool() {
        ExecutorService executor = Executors.newFixedThreadPool(8);

        // execute 8 tasks in separate threads
        executor.execute(new ExportingTask(pool, 1));
        executor.execute(new ExportingTask(pool, 2));
        executor.execute(new ExportingTask(pool, 3));
        executor.execute(new ExportingTask(pool, 4));
        executor.execute(new ExportingTask(pool, 5));
        executor.execute(new ExportingTask(pool, 6));
        executor.execute(new ExportingTask(pool, 7));
        executor.execute(new ExportingTask(pool, 8));

        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
