package com.mengfly.lib.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleConsumer;
import java.util.function.ObjIntConsumer;

import com.mengfly.lib.CollectionUtil;

/**
 * @author wangp
 */
public class TaskUtil {

    private ThreadPoolExecutor fixRoundExecutor;
    private ThreadPoolExecutor fix2RoundExecutor;
    private static volatile TaskUtil instance;
    private int processorsCount;

    private TaskUtil() {
        // 创建线程池
        processorsCount = Runtime.getRuntime().availableProcessors() + 1;
//		this.fixRoundExecutor = new ThreadPoolExecutor(processorsCount, processorsCount * 2, 60L, TimeUnit.SECONDS,
//				new LinkedBlockingDeque<>(), new TUThreadFactory("fixed"));
        this.fixRoundExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(processorsCount,
                new TUThreadFactory("fixed"));
//		this.fix2RoundExecutor = Executors.newCachedThreadPool(new TUThreadFactory("cache"));
    }

    /**
     * 将任务拆分为子任务使用线程进行执行，加快任务分析速度
     *
     * @param childTaskReses 子任务要处理的数据列表
     * @param childTaskLogic 每个子任务的处理逻辑
     */
    public <E> void splitTaskExec(List<List<E>> childTaskReses, ObjIntConsumer<List<E>> childTaskLogic,
                                  ObjIntConsumer<Exception> errorHandler, DoubleConsumer progressHandler) {
        int taskCount = childTaskReses.size();
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(taskCount);
        for (int i = 0; i < childTaskReses.size(); i++) {
            // 如果存在线程嵌套问题，那么把线程丢进Cache线程池中，防止阻塞发生
            List<E> childTaskRe = childTaskReses.get(i);
            int location = i;
            Runnable runnable = () -> {
                try {
                    begin.await();
                    childTaskLogic.accept(childTaskRe, location);
                } catch (InterruptedException e) {
                    System.err.println("子任务执行异常：" + Thread.currentThread().getName() + "->" + e.getMessage());
                    if (errorHandler != null) {
                        errorHandler.accept(e, location);
                    }
                } finally {
                    end.countDown();
                    if (progressHandler != null) {
                        progressHandler.accept((taskCount - end.getCount()) * 1.0 / taskCount);
                    }
                }
            };
            getExecutor().submit(runnable);
        }
        try {
            begin.countDown();
            end.await();
        } catch (InterruptedException e) {
            System.err.println("主任务执行异常：" + e.getMessage());
        }
    }

    /**
     * 将任务拆分为子任务使用线程进行执行，加快任务分析速度
     *
     * @param res              要拆分的总任务
     * @param childTaskItemNum 每个子任务中的item数量
     * @param childTaskLogic   每个子任务的处理逻辑
     */
    public <E> void splitTaskExec(List<E> res, int childTaskItemNum, ObjIntConsumer<List<E>> childTaskLogic) {
        List<List<E>> childTaskRes = CollectionUtil.split(res, childTaskItemNum);
        splitTaskExec(childTaskRes, childTaskLogic, null, null);
    }

    /**
     * 将任务拆分为子任务使用线程进行执行，加快任务分析速度
     *
     * @param res              要拆分的总任务
     * @param childTaskItemNum 每个子任务中的item数量
     * @param childTaskLogic   每个子任务的处理逻辑
     */
    public <E> void splitTaskExec(List<E> res, int childTaskItemNum, ObjIntConsumer<List<E>> childTaskLogic,
                                  ObjIntConsumer<Exception> exceptionHandler, DoubleConsumer processHandler) {
        List<List<E>> childTaskRes = CollectionUtil.split(res, childTaskItemNum);
        splitTaskExec(childTaskRes, childTaskLogic, exceptionHandler, processHandler);
    }

    public <E> void splitItemExec(List<E> res, ObjIntConsumer<E> childTaskLogic,
                                  ObjIntConsumer<Exception> exceptionHandler, DoubleConsumer processHandler) {
        splitTaskExec(res, 1,
                (es, value) -> childTaskLogic.accept(es.get(0), value), exceptionHandler, processHandler);
    }

    public <E> void splitItemExec(List<E> res, ObjIntConsumer<E> childTaskLogic) {
        splitItemExec(res, childTaskLogic, null, null);
    }


    public static TaskUtil getInstance() {
        if (instance == null) {
            synchronized (TaskUtil.class) {
                if (instance == null) {
                    instance = new TaskUtil();
                }
            }
        }
        return instance;
    }

    public ExecutorService getExecutor() {
        if (Thread.currentThread().getName().startsWith(TUThreadFactory.NAME_PREFIX + "-fixed")
                && fixRoundExecutor.getTaskCount() > fixRoundExecutor.getMaximumPoolSize()) {
            if (fix2RoundExecutor == null) {
                fix2RoundExecutor =
                        (ThreadPoolExecutor) Executors.newFixedThreadPool(processorsCount, new TUThreadFactory("2fixed"));
            }
            return fix2RoundExecutor;
        }
        return fixRoundExecutor;
    }

    private static class TUThreadFactory implements ThreadFactory {

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final static String NAME_PREFIX = "TUTheadPool";
        private final String namePrefix;

        public TUThreadFactory(String poolName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = NAME_PREFIX + "-" + poolName + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }

    }
}
