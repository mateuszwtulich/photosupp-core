package com.wtulich.photosupp.config;

import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.*;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = {
        AccountDao.class,
        PermissionDao.class,
        RoleDao.class,
        UserDao.class,
        VerificationTokenDao.class,
        IndicatorDao.class,
        BookingDao.class,
        AddressDao.class,
        PriceIndicatorDao.class,
        ServiceDao.class
})
@PropertySource("classpath:application.properties")
@EntityScan(basePackages = {
        "com.wtulich.photosupp.userhandling.dataaccess.api.entity",
        "com.wtulich.photosupp.orderhandling.dataaccess.api.entity",
        "com.wtulich.photosupp.serviceordering.dataaccess.api.entity"
})
public class H2JpaConfig {
}
