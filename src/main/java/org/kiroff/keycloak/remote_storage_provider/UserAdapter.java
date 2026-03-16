package org.kiroff.keycloak.remote_storage_provider;

import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.UserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.kiroff.keycloak.remote_storage_provider.dto.User;

import java.util.List;
import java.util.Map;

public class UserAdapter extends AbstractUserAdapter
{
    private final User user;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, User user)
    {
        super(session, realm, storageProviderModel);
        this.user = user;
    }

    @Override
    public String getUsername()
    {
        return  user.userName();
    }

    @Override
    public Map<String, List<String>> getAttributes()
    {
        final MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
        attributes.add(UserModel.USERNAME, getUsername());
        attributes.add(UserModel.EMAIL, getEmail());
        attributes.add(UserModel.FIRST_NAME, getFirstName());
        attributes.add(UserModel.LAST_NAME, getLastName());
        return attributes;
    }

    @Override
    public String getFirstName()
    {
        return user.firstName();
    }

    @Override
    public String getLastName()
    {
        return user.lastName();
    }

    @Override
    public String getEmail()
    {
        return user.email();
    }

    @Override
    public SubjectCredentialManager credentialManager()
    {
        return new UserCredentialManager(session, realm, this);
    }
}
