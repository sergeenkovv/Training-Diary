package com.ivan.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AuditAspect {

    @Before("@annotation(com.ivan.annotations.Auditable)")
    public void auditMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        System.out.println("Method name " + methodName);
        System.out.println("Signature " + jp.getSignature().getName());
        Object[] args = jp.getArgs();

        for (Object arg : args) {
            System.out.println("Type: " + arg.getClass().toString() + "Value: " + arg.toString());
        }
    }
}