package com.dev.skyskill.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

  private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

  // 拦截 com.dev.skyskill 包下所有 Service 的方法
  @Around("execution(* com.dev.skyskill..*Service.*(..))")
  public Object monitorServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    String methodName = joinPoint.getSignature().toShortString();
    Object result = null;

    try {
      result = joinPoint.proceed(); // 执行目标方法
      return result;
    } catch (Throwable ex) {
      // 记录更详细的业务上下文信息
      log.error("服务层异常拦截! 方法: {}, 入参: {}", methodName, Arrays.toString(joinPoint.getArgs()), ex);
      throw ex; // 必须抛出，让全局异常处理器接手
    } finally {
      long duration = System.currentTimeMillis() - start;
      if (duration > 1000) { // 超过 1 秒记录慢方法
        log.warn("慢方法告警: {} 耗时 {} ms", methodName, duration);
      }
    }
  }
}