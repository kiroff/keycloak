package org.kiroff.keycloak.remote_storage_provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class RemoteUsersStorageProviderFactory implements UserStorageProviderFactory<RemoteUsersStorageProvider>
{

    public static final String STORAGE_PROVIDER_NAME = "my-remote-mysql-storage-provider";

    @Override
    public RemoteUsersStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel)
    {
        return new RemoteUsersStorageProvider(keycloakSession, componentModel, new UsersApiService(keycloakSession));
    }

    @Override
    public String getId()
    {
        return STORAGE_PROVIDER_NAME;
    }
}
