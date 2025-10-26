public class trspo_4 {

    private static long collatzSteps(long n) {
        long steps = 0;
        while (n != 1) {
            if ((n & 1) == 0) {
                n = n / 2;
            } else {
                n = 3 * n + 1;
            }
            steps++;
        }
        return steps;
    }

    static class Worker extends Thread {
        private final int start, end;
        long sumSteps = 0;
        long count = 0;

        Worker(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                sumSteps += collatzSteps(i);
                count++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int N = 10_000_000;
        final int numThreads = Runtime.getRuntime().availableProcessors();
        final int chunkSize = N / numThreads;

        System.out.println("Обчислення для N = " + N);
        System.out.println("Без синхронiзацiї, використовується " + numThreads + " потокiв");

        long startTime = System.currentTimeMillis();

        Worker[] workers = new Worker[numThreads];

        // створюємо та запускаємо потоки
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize + 1;
            int end = (i == numThreads - 1) ? (N + 1) : (start + chunkSize);
            workers[i] = new Worker(start, end);
            workers[i].start();
        }

        // чекаємо завершення кожного потоку
        for (Worker w : workers) {
            w.join();
        }

        long totalCount = 0;
        long totalSteps = 0;

        // підсумовуємо результати (без синхронізації, після завершення потоків)
        for (Worker w : workers) {
            totalCount += w.count;
            totalSteps += w.sumSteps;
        }

        double averageSteps = (double) totalSteps / totalCount;
        long endTime = System.currentTimeMillis();

        System.out.println("Загальна кiлькiсть чисел: " + totalCount);
        System.out.println("Сумарна кiлькiсть крокiв: " + totalSteps);
        System.out.println("Середня кiлькiсть крокiв до 1: " + averageSteps);
        System.out.println("Час виконання: " + (endTime - startTime) / 1000.0 + " секунд");
    }
}
