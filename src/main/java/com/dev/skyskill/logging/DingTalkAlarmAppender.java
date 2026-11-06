package com.dev.skyskill.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DingTalkAlarmAppender extends AppenderBase<ILoggingEvent> {

  // 异步线程池，防止发送告警阻塞主线程
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  // 告警去重缓存：Key 为异常信息，Value 为上次告警时间
  private final Map<String, Long> alarmCache = new ConcurrentHashMap<>();
  // 限制同类异常告警频率：5分钟
  private static final long ALARM_INTERVAL_MS = 5 * 60 * 1000;

  @Override
  protected void append(ILoggingEvent event) {
    // 1. 只处理 ERROR 级别的日志
    if (!event.getLevel().isGreaterOrEqual(Level.ERROR)) {
      return;
    }

    String message = event.getFormattedMessage();
    IThrowableProxy throwable = event.getThrowableProxy();
    String exceptionType = throwable != null ? throwable.getClassName() : "NoException";

    // 2. 构建去重 Key (例如：java.lang.NullPointerException:发生空指针的类)
    String alarmKey = exceptionType + ":" + event.getLoggerName();

    // 3. 去重与限流判断
    long now = System.currentTimeMillis();
    Long lastTime = alarmCache.get(alarmKey);
    if (lastTime != null && (now - lastTime) < ALARM_INTERVAL_MS) {
      return; // 未达到告警间隔，跳过
    }
    alarmCache.put(alarmKey, now);

    // 4. 异步发送告警
    executor.submit(() -> sendAlarm(event, message, exceptionType));
  }

  private void sendAlarm(ILoggingEvent event, String message, String exceptionType) {
    try {
      // 这里实现真实的 Webhook 请求，此处为了演示直接打印
      System.out.println("========================= 🚨 钉钉告警触发 🚨 =========================");
      System.out.println("【服务名称】: sky-skill");
      System.out.println("【异常类型】: " + exceptionType);
      System.out.println("【错误信息】: " + message);
      System.out.println("【发生时间】: " + new java.util.Date(event.getTimeStamp()));
      System.out.println("【接口路径】: " + event.getLoggerName());
      System.out.println("【日志级别】: " + event.getLevel());
      System.out.println("=====================================================================");

      // 模拟网络请求耗时
      Thread.sleep(500);
    } catch (Exception e) {
      System.err.println("告警发送失败，降级记录到本地文件...");
    }
  }

  @Override
  public void stop() {
    // 应用关闭时，优雅关闭线程池
    executor.shutdown();
    try {
      if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
    super.stop();
  }
}