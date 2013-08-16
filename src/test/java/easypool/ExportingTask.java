package easypool;

public class ExportingTask implements Runnable
{
    private ObjectPool<ExportingProcess> pool;
    
    public ExportingTask(ObjectPool<ExportingProcess> pool) {
        this.pool = pool;
    }

    public void run() {
        // get an object from the pool
        ExportingProcess exportingProcess = pool.borrowObject();
        
        System.out.println("Object with process no. " + exportingProcess.getProcessNo() + " was borrowed");

        // do something
        // ...

        // for-loop is just for simulation
        for (int i = 0; i < 100000; i++) {
        }

        // return ExportingProcess instance back to the pool
        pool.returnObject(exportingProcess);
        
        System.out.println("Object with process no. " + exportingProcess.getProcessNo() + " was returned");
    }
}
