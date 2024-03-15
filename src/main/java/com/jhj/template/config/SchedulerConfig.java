package com.jhj.template.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableAsync
public class SchedulerConfig {

	// fixedDelay
	// 이전 Task의 종료 시점으로부터 정의된 시간만큼 지난 후 Task를 실행
	// @Scheduled(fixedDelay = 3000, initialDelay = 2000) // initailDelay: 초기 지연시간
	// 설정
	// // @Async // 병렬로 스케쥴러 실행할 경우 등록
	// public void scheduleFixedDelayTask() throws InterruptedException {
	// SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	// log.info("Fixed Delay Task - {}", dateFormat.format(new Date()));
	// Thread.sleep(1000);
	// }

	// @Scheduled(fixedDelay = 5000)
	// // @Async
	// public void scheduleFixedDelayTask2() throws InterruptedException {
	// SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	// log.info("Fixed Delay Task2 - {}", dateFormat.format(new Date()));
	// Thread.sleep(1000);
	// }

	// fixedRate
	// 이전 Task의 시작 시점으로부터 정의된 시간만큼 지난 후 Task를 실행
	// @Scheduled(fixedRate = 3000)
	// public void scheduleFixedRateTask() throws InterruptedException {
	// SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	// log.info("Fixed Rate Task - {}", dateFormat.format(new Date()));
	// Thread.sleep(5000);
	// }

	// @Scheduled(fixedRate = 3000)
	// public void scheduleFixedRateTask2() throws InterruptedException {
	// SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	// log.info("Fixed Rate Task2 - {}", dateFormat.format(new Date()));
	// Thread.sleep(5000);
	// }

	// cron
	// 지정된 시간마다 Task 실행
	// @Scheduled(cron = "10 * * * * *")
	// public void scheduleCronTask() throws InterruptedException {
	// SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	// log.info("Cron Task - {}", dateFormat.format(new Date()));
	// }
}
