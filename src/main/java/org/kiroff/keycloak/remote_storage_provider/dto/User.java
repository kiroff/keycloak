package org.kiroff.keycloak.remote_storage_provider.dto;

public record User(String firstName, String lastName, String email, String userName, String userId, String roles) {

}