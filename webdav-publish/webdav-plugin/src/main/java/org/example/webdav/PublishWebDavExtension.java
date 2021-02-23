package org.example.webdav;

import org.gradle.api.provider.Property;

abstract public class PublishWebDavExtension {
    public abstract Property<String> getRepositoryUrl();
    public abstract Property<String> getUsername();
    public abstract Property<String> getPassword();
}
