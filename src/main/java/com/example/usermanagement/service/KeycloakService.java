package com.example.usermanagement.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private Keycloak keycloak;
    private RealmResource realmResource;
    private UsersResource usersResource;

    @PostConstruct
    public void initKeycloak() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .build();

        realmResource = keycloak.realm(realm);
        usersResource = realmResource.users();
    }

    public String createUser(String email, String password, String firstName, String lastName, String role) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Arrays.asList(credential));

        var response = usersResource.create(user);
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // Assign role to user
        assignRoleToUser(userId, role);

        return userId;
    }

    public void assignRoleToUser(String userId, String roleName) {
        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
        usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(role));
    }

    public void updateUser(String userId, String email, String firstName, String lastName) {
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        usersResource.get(userId).update(user);
    }

    public void deleteUser(String userId) {
        usersResource.get(userId).remove();
    }

    public void enableUser(String userId) {
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        user.setEnabled(true);
        usersResource.get(userId).update(user);
    }

    public void disableUser(String userId) {
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        user.setEnabled(false);
        usersResource.get(userId).update(user);
    }

    public UserRepresentation getUserById(String userId) {
        return usersResource.get(userId).toRepresentation();
    }

    public List<UserRepresentation> getAllUsers() {
        return usersResource.list();
    }

    public UserRepresentation getUserByEmail(String email) {
        List<UserRepresentation> users = usersResource.search(email);
        return users.isEmpty() ? null : users.get(0);
    }
}
