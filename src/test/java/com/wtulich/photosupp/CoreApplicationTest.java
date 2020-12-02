package com.wtulich.photosupp;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@ComponentScan(basePackages = "com.wtulich.photosupp")
@ActiveProfiles("test")
public class CoreApplicationTest {

    @Test
    void loads(){
    }
}
