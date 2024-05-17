package com.ivan.auditstarter.aspect;

import com.ivan.auditstarter.annotation.Auditable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * Aspect that logs method executions annotated with {@link Auditable}.
 */
@Aspect
@Slf4j
public class AuditAspect {

    @Pointcut("(within(@com.ivan.auditstarter.annotation.Auditable *) || execution(@com.ivan.auditstarter.annotation.Auditable * *(..))) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object auditMethod(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringType().getSimpleName();
        var result = jp.proceed();
        Object[] args = jp.getArgs();

        log.info("Method name: {}", methodName);
        log.info("Signature: {}", methodSignature);
        log.info("Type: {}; Value: {}", className, Arrays.toString(args));

        return result;
    }
}