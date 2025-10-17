package com.example.usermanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.usermanagement.web.rest.TestUtil;
import java.util.UUID;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + ( 2 * Integer.MAX_VALUE ));

    public static AppUser getAppUserSample1() {
        return new AppUser().id(1L).email("email1").password("password1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser().id(2L).email("email2").password("password2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser().id(longCount.incrementAndGet()).email(UUID.randomUUID().toString()).password(UUID.randomUUID().toString());
    }
}
