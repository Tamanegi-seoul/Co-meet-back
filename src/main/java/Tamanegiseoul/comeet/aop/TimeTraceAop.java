package Tamanegiseoul.comeet.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeTraceAop {

    @Around("execution(* Tamanegiseoul.comeet..*(..))")
    public Object execute(ProceedingJoinPoint jointPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.debug("START: " + jointPoint.toShortString());
        System.out.println("START: " + jointPoint.toShortString());

        try {
            return jointPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            log.debug("END: " + jointPoint.toShortString() + " " + timeMs + "ms");
            System.out.println("END: " + jointPoint.toShortString() + " " + timeMs + "ms");
        }
    }
}
