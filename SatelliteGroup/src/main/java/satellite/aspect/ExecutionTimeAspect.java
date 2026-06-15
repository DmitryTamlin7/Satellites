package satellite.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();
        long executetime = System.currentTimeMillis() - start;

        System.out.println("Метод " + joinPoint.getSignature().getName() + " выполнен за " + executetime + " мс ");
        return proceed;
    }
}
