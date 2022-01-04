package org.example.webdav;

import org.gradle.api.provider.Property;

public interface PublishWebDavExtension {
    Property<String> getRepositoryUrl();
    Property<String> getUsername();
    Property<String> getPassword();
}
