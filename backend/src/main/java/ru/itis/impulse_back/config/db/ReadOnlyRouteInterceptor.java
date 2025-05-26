package ru.itis.impulse_back.config.db;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Aspect
@Component
@Order(0)
public class ReadOnlyRouteInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ReadOnlyRouteInterceptor.class);

    @Around("@annotation(tx)")
    public Object route(ProceedingJoinPoint pjp, Transactional tx) throws Throwable {
        if (tx.readOnly()) {
            RoutingDataSource.setReplicaRoute();
        } else {
            RoutingDataSource.clearReplicaRoute();

        }
        try {
            return pjp.proceed();
        } finally {
            RoutingDataSource.clearReplicaRoute();
        }
    }

}
