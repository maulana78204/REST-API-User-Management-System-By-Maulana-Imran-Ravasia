package com.example.usermanagement.domain;

import static com.example.usermanagement.domain.AppUserTestSamples.*;
import static com.example.usermanagement.domain.UserProfileTestSamples.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.usermanagement.web.rest.TestUtil;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void userProfileTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        appUser.setUserProfile(userProfileBack);
        assertThat(appUser.getUserProfile()).isEqualTo(userProfileBack);
        assertThat(userProfileBack.getUser()).isEqualTo(appUser);

        appUser.userProfile(null);
        assertThat(appUser.getUserProfile()).isNull();
        assertThat(userProfileBack.getUser()).isNull();
    }
}
