package com.example.usermanagement.web.rest;

import static com.example.usermanagement.domain.UserProfileAsserts.*;
import static com.example.usermanagement.web.rest.TestUtil.createUpdateProxyForBean;

import com.example.usermanagement.IntegrationTest;
import com.example.usermanagement.domain.UserProfile;
import com.example.usermanagement.repository.UserProfileRepository;
import com.example.usermanagement.service.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserProfileResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + ( 2 * Integer.MAX_VALUE ));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProfileRepository userProfileRepositoryMock;

    @Mock
    private UserProfileService userProfileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    private UserProfile insertedUserProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity() {
        return new UserProfile()
            .fullName(DEFAULT_FULL_NAME)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .avatar(DEFAULT_AVATAR)
;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity() {
        return new UserProfile()
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .avatar(UPDATED_AVATAR)
;
    }

    @BeforeEach
    void initTest() {
        userProfile = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserProfile != null) {
            userProfileRepository.delete(insertedUserProfile);
            insertedUserProfile = null;
        }
    }

    @Test
    @Transactional
    void createUserProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserProfile
        var returnedUserProfile = om.readValue(
            restUserProfileMockMvc
                .perform(
                  post(ENTITY_API_URL)
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(om.writeValueAsBytes(userProfile))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserProfile.class
        );

        // Validate the UserProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserProfileUpdatableFieldsEquals(returnedUserProfile, getPersistedUserProfile(returnedUserProfile));

        insertedUserProfile = returnedUserProfile;
    }

    @Test
    @Transactional
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(userProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setFullName(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post(ENTITY_API_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setPhone(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post(ENTITY_API_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setAddress(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post(ENTITY_API_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserProfiles() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)));
    }

    @SuppressWarnings({"unchecked"})
    void getAllUserProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true"))
            .andExpect(status().isOk());

        verify(userProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    void getAllUserProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk());
        verify(userProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get(ENTITY_API_URL_ID, userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.avatar").value(DEFAULT_AVATAR));
    }
    @Test
    @Transactional
    void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .avatar(UPDATED_AVATAR);

        restUserProfileMockMvc.perform(put(ENTITY_API_URL_ID, updatedUserProfile.getId()).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(updatedUserProfile)))
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserProfileToMatchAllProperties(updatedUserProfile);

    }

    @Test
    @Transactional
    void putNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(put(ENTITY_API_URL_ID, userProfile.getId()).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(put(ENTITY_API_URL_ID, longCount.incrementAndGet()).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(put(ENTITY_API_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

long databaseSizeBeforeUpdate = getRepositoryCount();

// Update the userProfile using partial update
UserProfile partialUpdatedUserProfile = new UserProfile();
partialUpdatedUserProfile.setId(userProfile.getId());

    partialUpdatedUserProfile
        .address(UPDATED_ADDRESS);

restUserProfileMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId()).with(csrf())
.contentType("application/merge-patch+json")
.content(om.writeValueAsBytes(partialUpdatedUserProfile)))
.andExpect(status().isOk());

// Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserProfile, userProfile),
            getPersistedUserProfile(userProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

  
long databaseSizeBeforeUpdate = getRepositoryCount();

// Update the userProfile using partial update
UserProfile partialUpdatedUserProfile = new UserProfile();
partialUpdatedUserProfile.setId(userProfile.getId());

    partialUpdatedUserProfile
        .fullName(UPDATED_FULL_NAME)
        .phone(UPDATED_PHONE)
        .address(UPDATED_ADDRESS)
        .avatar(UPDATED_AVATAR);

restUserProfileMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId()).with(csrf())
.contentType("application/merge-patch+json")
.content(om.writeValueAsBytes(partialUpdatedUserProfile)))
.andExpect(status().isOk());

// Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(
            partialUpdatedUserProfile,
            getPersistedUserProfile(partialUpdatedUserProfile)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(patch(ENTITY_API_URL_ID, userProfile.getId()).with(csrf())
            .contentType("application/merge-patch+json")
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(patch(ENTITY_API_URL_ID, longCount.incrementAndGet()).with(csrf())
            .contentType("application/merge-patch+json")
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(patch(ENTITY_API_URL).with(csrf())
            .contentType("application/merge-patch+json")
            .content(om.writeValueAsBytes(userProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);

    }

    @Test
    @Transactional
    void deleteUserProfile() throws Exception {
        // Initialize the database
insertedUserProfile =userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userProfile
        restUserProfileMockMvc.perform(delete(ENTITY_API_URL_ID, userProfile.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);

    }

    protected long getRepositoryCount() {
        return userProfileRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UserProfile getPersistedUserProfile(UserProfile userProfile) {
        return userProfileRepository.findById(userProfile.getId()).orElseThrow();
    }

    protected void assertPersistedUserProfileToMatchAllProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllPropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }

    protected void assertPersistedUserProfileToMatchUpdatableProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllUpdatablePropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }
}
