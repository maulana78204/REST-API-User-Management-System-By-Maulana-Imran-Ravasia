package com.example.usermanagement.service;

import com.example.usermanagement.domain.AppUser;
import com.example.usermanagement.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link com.example.usermanagement.domain.AppUser}.
 */
@Service
@Transactional
public class AppUserService {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;

    public AppUserService(
        AppUserRepository appUserRepository
    ) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Save a appUser.
     *
     * @param appUser the entity to save.
     * @return the persisted entity.
     */
    public AppUser save(AppUser appUser) {
        LOG.debug("Request to save AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    /**
     * Update a appUser.
     *
     * @param appUser the entity to save.
     * @return the persisted entity.
    */
    public AppUser update(AppUser appUser) {
        LOG.debug("Request to update AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    /**
     * Partially update a appUser.
     *
     * @param appUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppUser> partialUpdate(AppUser appUser) {
        LOG.debug("Request to partially update AppUser : {}", appUser);

return appUserRepository.findById(appUser.getId())
    .map(existingAppUser -> {
    if (appUser.getEmail() != null) {
        existingAppUser.setEmail(appUser.getEmail());
    }
    if (appUser.getPassword() != null) {
        existingAppUser.setPassword(appUser.getPassword());
    }
    if (appUser.getRole() != null) {
        existingAppUser.setRole(appUser.getRole());
    }
    if (appUser.getStatus() != null) {
        existingAppUser.setStatus(appUser.getStatus());
    }
    if (appUser.getCreatedAt() != null) {
        existingAppUser.setCreatedAt(appUser.getCreatedAt());
    }
    if (appUser.getUpdatedAt() != null) {
        existingAppUser.setUpdatedAt(appUser.getUpdatedAt());
    }

  
    return existingAppUser;
    })

  .map(appUserRepository::save)
;

    }

    /**
     * Get all the appUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppUser> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppUsers");
        return appUserRepository.findAll(pageable);
    }



    /**
     *  Get all the appUsers where UserProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<AppUser> findAllWhereUserProfileIsNull() {
        LOG.debug("Request to get all appUsers where UserProfile is null");
        return StreamSupport
            .stream(appUserRepository.findAll().spliterator(), false)
            .filter(appUser -> appUser.getUserProfile() == null)
            .toList();
    }
    /**
     * Get one appUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppUser> findOne(Long id) {
        LOG.debug("Request to get AppUser : {}", id);
        return appUserRepository.findById(id);
    }

    /**
     * Delete the appUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
