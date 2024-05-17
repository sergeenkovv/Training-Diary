package com.ivan.loggingstarter.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;
import com.ivan.loggingstarter.annotations.LoggableInfo;

/**
 * Logs method execution info.
 *
 * @author sergeenkovv
 */
@Aspect
@Slf4j
public class LoggingMethodExecutionAspect {

    @Pointcut("(within(@com.ivan.loggingstarter.annotations.LoggableInfo *) || execution(@com.ivan.loggingstarter.annotations.LoggableInfo * *(..))) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logMethodExecution(ProceedingJoinPoint pjp) throws Throwable {
        var methodSignature = (MethodSignature) pjp.getSignature();
        var methodName = methodSignature.getName();
        var className = methodSignature.getDeclaringType().getSimpleName();

        log.info("Executing method {} in class {}", methodName, className);

        LoggableInfo loggableInfo = methodSignature.getMethod().getAnnotation(LoggableInfo.class);
        if (loggableInfo == null) {
            loggableInfo = methodSignature.getMethod().getDeclaringClass().getAnnotation(LoggableInfo.class);
        }

        String name = (loggableInfo != null) ? loggableInfo.name() : "";

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        var result = pjp.proceed();
        stopWatch.stop();

        log.info("Method {} in class {} completed in {} ms | {}", methodName, className, stopWatch.getTotalTimeMillis(), name);

        return result;
    }
}