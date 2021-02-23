package org.example.webdav;

import org.apache.maven.wagon.ConnectionException;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.apache.maven.wagon.TransferFailedException;
import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.apache.maven.wagon.providers.webdav.WebDavWagon;
import org.apache.maven.wagon.repository.Repository;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;

import java.io.File;


abstract public class PublishWebDav extends DefaultTask {

    @Internal
    public abstract Property<String> getUsername();

    @Internal
    public abstract Property<String> getPassword();

    @InputDirectory
    public abstract DirectoryProperty getLocalRepo();

    @Internal
    public abstract Property<String> getRemote();

    @TaskAction
    public void publish() throws ConnectionException, AuthenticationException, AuthorizationException, TransferFailedException, ResourceDoesNotExistException {
        AuthenticationInfo authenticationInfo = new AuthenticationInfo();
        authenticationInfo.setUserName(getUsername().get());
        authenticationInfo.setPassword(getPassword().get());

        Repository repository = new Repository("maven", stripPrefix(getRemote().get()));
        WebDavWagon wagon = new WebDavWagon();
        wagon.connect(repository, authenticationInfo);

        Directory localRepoRoot = getLocalRepo().get();
        for (File file: localRepoRoot.getAsFileTree()) {
            wagon.put(file, localRepoRoot.getAsFile().toURI().relativize(file.toURI()).getPath());
        }
    }

    private String stripPrefix(String url) {
        return url.replaceFirst("^dav\\+", "");
    }
}
