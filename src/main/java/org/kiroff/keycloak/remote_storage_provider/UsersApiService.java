package org.kiroff.keycloak.remote_storage_provider;

import jakarta.ws.rs.PathParam;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.keycloak.http.simple.SimpleHttp;
import org.keycloak.models.KeycloakSession;
import org.kiroff.keycloak.remote_storage_provider.address.LocalIPResolver;
import org.kiroff.keycloak.remote_storage_provider.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsersApiService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersApiService.class);
    public static String URL = "http://localhost:8080";

    KeycloakSession keycloakSession;

    public UsersApiService(KeycloakSession session)
    {
        this.keycloakSession = session;
    }

    public User getUserByUserName(String userName)
    {
        User user = null;
        LOGGER.info("Searching for user: {}", userName);
        try {
            URL = String.format("http://%s:8080/users/%s", LocalIPResolver.getHardcodedLocalIp(), userName);
            user = SimpleHttp.create(this.keycloakSession).doGet(URL).asJson(User.class);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("Returning user: {}", user);
        return user;
    }

    public boolean verifyUserPassword(@PathParam("username") String userName, String password)
    {
        int result = 0;
        try {
            URL = String.format("http://%s:8080/users/%s", LocalIPResolver.getHardcodedLocalIp(), userName);
            result = SimpleHttp.create(this.keycloakSession)
                    .doPost(URL)
                    .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                    .param("password", password)
                    .asStatus();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return result >= 200 && result <= 299;
    }

}
