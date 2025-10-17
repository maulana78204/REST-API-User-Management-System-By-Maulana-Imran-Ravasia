package com.example.usermanagement.domain;

import static com.example.usermanagement.domain.UserProfileTestSamples.*;
import static com.example.usermanagement.domain.AppUserTestSamples.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.usermanagement.web.rest.TestUtil;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void userTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        userProfile.setUser(appUserBack);
        assertThat(userProfile.getUser()).isEqualTo(appUserBack);

        userProfile.user(null);
        assertThat(userProfile.getUser()).isNull();
    }
}
