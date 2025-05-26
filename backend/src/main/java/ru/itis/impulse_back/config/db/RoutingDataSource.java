package ru.itis.impulse_back.config.db;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<Route> ctx = new ThreadLocal<>();

    public enum Route {
        PRIMARY, REPLICA
    }

    public static void clearReplicaRoute() {
        ctx.remove();
    }

    public static void setReplicaRoute() {
        ctx.set(Route.REPLICA);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Route route = ctx.get();
        Route effectiveRoute = (route == null) ? Route.PRIMARY : route;
        System.out.println("[RoutingDataSource] Using route: " + effectiveRoute);
        return effectiveRoute;
    }
    //проверка правильного направление запросов
//    @Override
//    protected DataSource determineTargetDataSource() {
//        Object lookupKey = determineCurrentLookupKey();
//        DataSource dataSource = (DataSource) this.resolveSpecifiedDataSource(this.getResolvedDataSources().get(lookupKey));
//
//        if (dataSource instanceof HikariDataSource) {
//            HikariDataSource hikari = (HikariDataSource) dataSource;
//            System.out.println("[RoutingDataSource] Using pool: " + hikari.getPoolName());
//        } else {
//            System.out.println("[RoutingDataSource] DataSource selected: " + dataSource);
//        }
//
//        return dataSource;
//    }



}
