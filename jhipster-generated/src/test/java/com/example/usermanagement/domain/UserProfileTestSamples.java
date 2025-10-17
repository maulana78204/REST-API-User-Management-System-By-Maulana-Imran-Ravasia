package com.example.usermanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.usermanagement.web.rest.TestUtil;
import java.util.UUID;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + ( 2 * Integer.MAX_VALUE ));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile().id(1L).fullName("fullName1").phone("phone1").address("address1").avatar("avatar1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile().id(2L).fullName("fullName2").phone("phone2").address("address2").avatar("avatar2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile().id(longCount.incrementAndGet()).fullName(UUID.randomUUID().toString()).phone(UUID.randomUUID().toString()).address(UUID.randomUUID().toString()).avatar(UUID.randomUUID().toString());
    }
}
