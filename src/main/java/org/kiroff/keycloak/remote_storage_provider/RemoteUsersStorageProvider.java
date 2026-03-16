package org.kiroff.keycloak.remote_storage_provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.kiroff.keycloak.remote_storage_provider.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RemoteUsersStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteUsersStorageProvider.class);

    private final KeycloakSession keycloakSession;
    private final ComponentModel componentModel;
    private final UsersApiService usersService;


    public RemoteUsersStorageProvider(KeycloakSession keycloakSession, ComponentModel componentModel, UsersApiService usersService)
    {
        this.keycloakSession = keycloakSession;
        this.componentModel = componentModel;
        this.usersService = usersService;
    }

    @Override
    public void close()
    {

    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String id)
    {
        final StorageId storageId = new StorageId(id);
        final String email = storageId.getExternalId();//Using email as an id
        return getUserByEmail(realmModel, email);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String userName)
    {
        UserModel result = null;
        try
        {
            result = Optional.ofNullable(usersService.getUserByUserName(userName)).map(user -> new UserAdapter(keycloakSession, realmModel, componentModel, user)).orElse(null);
            LOGGER.info("getUserByUserName({}) returns user: {}", userName, result);
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String email)
    {
        //Using email as an id
        return getUserByUsername(realmModel, email);
    }

    @Override
    public boolean supportsCredentialType(String credentialType)
    {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType)
    {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput)
    {
       return usersService.verifyUserPassword(userModel.getUsername(),
                credentialInput.getChallengeResponse());
    }
}
