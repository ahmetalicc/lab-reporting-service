package org.backend.project.reportingapp.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class LogAspect {

    @Pointcut("execution(* org.backend.project.reportingapp.service.Concrete.*.*(..))")
    public void loggingPointCut() {}
    @Around("loggingPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register the module
        String methodName = joinPoint.getSignature().getName();
        Object array = joinPoint.getArgs();
        log.trace("method invoked "  +": " + methodName + "() " + "arguments:" + mapper.writeValueAsString(array));
        Object object = joinPoint.proceed();
        log.trace("method ends ... " + methodName + "() " + "Response : "
                + mapper.writeValueAsString(object));
        return object;
    }
}
