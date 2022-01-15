package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.spi.Extension;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Extension(group = Configuration.CONFIG_EXTENSION_GROUP)
public class DefaultExecutorService extends ThreadPoolExecutor {
    public DefaultExecutorService() {
        super(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(50),
                new ThreadFactory() {
                    final AtomicInteger threadNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(null, r, "Kyrian-" + threadNumber.getAndIncrement());
                    }
                }, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
