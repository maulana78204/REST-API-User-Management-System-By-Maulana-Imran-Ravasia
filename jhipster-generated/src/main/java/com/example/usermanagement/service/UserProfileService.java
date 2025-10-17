package com.example.usermanagement.service;

import com.example.usermanagement.domain.UserProfile;
import com.example.usermanagement.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link com.example.usermanagement.domain.UserProfile}.
 */
@Service
@Transactional
public class UserProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(
        UserProfileRepository userProfileRepository
    ) {
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Save a userProfile.
     *
     * @param userProfile the entity to save.
     * @return the persisted entity.
     */
    public UserProfile save(UserProfile userProfile) {
        LOG.debug("Request to save UserProfile : {}", userProfile);
        return userProfileRepository.save(userProfile);
    }

    /**
     * Update a userProfile.
     *
     * @param userProfile the entity to save.
     * @return the persisted entity.
    */
    public UserProfile update(UserProfile userProfile) {
        LOG.debug("Request to update UserProfile : {}", userProfile);
        return userProfileRepository.save(userProfile);
    }

    /**
     * Partially update a userProfile.
     *
     * @param userProfile the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserProfile> partialUpdate(UserProfile userProfile) {
        LOG.debug("Request to partially update UserProfile : {}", userProfile);

return userProfileRepository.findById(userProfile.getId())
    .map(existingUserProfile -> {
    if (userProfile.getFullName() != null) {
        existingUserProfile.setFullName(userProfile.getFullName());
    }
    if (userProfile.getPhone() != null) {
        existingUserProfile.setPhone(userProfile.getPhone());
    }
    if (userProfile.getAddress() != null) {
        existingUserProfile.setAddress(userProfile.getAddress());
    }
    if (userProfile.getAvatar() != null) {
        existingUserProfile.setAvatar(userProfile.getAvatar());
    }

  
    return existingUserProfile;
    })

  .map(userProfileRepository::save)
;

    }

    /**
     * Get all the userProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserProfile> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserProfiles");
        return userProfileRepository.findAll(pageable);
    }



    /**
     * Get all the userProfiles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserProfile> findAllWithEagerRelationships(Pageable pageable) {
        return userProfileRepository.findAllWithEagerRelationships(pageable);
    }
    /**
     * Get one userProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserProfile> findOne(Long id) {
        LOG.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the userProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
    }
}
