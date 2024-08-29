import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalConfig {

    private static ExecutorService executor;
    private GlobalConfig(){
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    static {
        executor = Executors.newFixedThreadPool(10);
    }
}
