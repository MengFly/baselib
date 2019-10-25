package com.mengfly.lib;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import concurrent.TaskUtil;

class TaskUtilTest {

	@Test
	void test() {
		AtomicInteger totalTask = new AtomicInteger(0);
		AtomicInteger j = new AtomicInteger(0);
		long s = System.currentTimeMillis();
		TaskUtil.getInstance().splitTaskExec(TestUtil.getArangeList(50), 1, (lss, index) -> {
			System.out.println(Thread.currentThread().getName());
			j.addAndGet(1);
//			long id = Thread.currentThread().getId();
			List<String> res = TestUtil.getArangeList(200);
			AtomicInteger i = new AtomicInteger(0);
			TaskUtil.getInstance().splitTaskExec(res, 2, (ls,index2) -> {
				if (Thread.currentThread().getName().contains("fix")) {
					System.out.println(">>>>>>>>>>>>>" + Thread.currentThread().getName());
				}
				long start = System.currentTimeMillis();
				i.addAndGet(1);
				totalTask.addAndGet(1);
				while (System.currentTimeMillis() - start < 1000) {

				}
			});
			assertEquals(i.get(), 4);
		});
		assertEquals(j.get(), 50);
		System.out.println(totalTask.get());
		System.out.println(1000 * 5000 / (System.currentTimeMillis() - s));
		assertTrue((System.currentTimeMillis() - s) < (1000L * 5000));
	}

	@Test
	void test1() {
		AtomicInteger j = new AtomicInteger(0);
		while (j.get() < 10) {
			TaskUtil.getInstance().splitTaskExec(TestUtil.getArangeList(100), 50, (ls,index) -> {
				long start = System.currentTimeMillis();
				System.out.println("j=" + j);
				while (System.currentTimeMillis() - start < 1000) {
				}
			});
			j.addAndGet(1);
		}
	}

}
