package runner;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

public class RunnerStats {

    private static final Map<String, StatResult> results = new LinkedHashMap<>();

    public static void measure(String runnerName, Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long end = System.nanoTime();
        long durationMs = (end - start) / 1_000_000;

        Runtime runtime = Runtime.getRuntime();
        long usedMemoryMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        long totalMemoryMb = runtime.totalMemory() / (1024 * 1024);

        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        int liveThreads = Thread.activeCount();
        long totalStartedThreads = threadBean.getTotalStartedThreadCount();

        results.put(runnerName, new StatResult(durationMs, usedMemoryMb, totalMemoryMb, liveThreads, totalStartedThreads));
    }

    public static void printAll() {
        System.out.println("\n========== Сводная статистика ==========");
        for (Map.Entry<String, StatResult> entry : results.entrySet()) {
            System.out.println("Runner: " + entry.getKey());
            StatResult r = entry.getValue();
            System.out.printf("Время: %d ms | Память: %d/%d MB | Активных потоков: %d | Всего потоков: %d%n",
                    r.durationMs, r.usedMemoryMb, r.totalMemoryMb, r.liveThreads, r.totalStartedThreads);
        }
        System.out.println("========================================");
    }

    private static class StatResult {
        long durationMs;
        long usedMemoryMb;
        long totalMemoryMb;
        int liveThreads;
        long totalStartedThreads;

        public StatResult(long durationMs, long usedMemoryMb, long totalMemoryMb, int liveThreads, long totalStartedThreads) {
            this.durationMs = durationMs;
            this.usedMemoryMb = usedMemoryMb;
            this.totalMemoryMb = totalMemoryMb;
            this.liveThreads = liveThreads;
            this.totalStartedThreads = totalStartedThreads;
        }
    }
}

