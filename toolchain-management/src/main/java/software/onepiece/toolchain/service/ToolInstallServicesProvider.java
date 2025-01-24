package software.onepiece.toolchain.service;

import org.gradle.api.internal.artifacts.DependencyManagementServices;
import org.gradle.api.internal.file.FileCollectionFactory;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.vfs.FileSystemAccess;

import javax.inject.Inject;

public interface ToolInstallServicesProvider {
    @Inject
    FileSystemAccess getFileSystemAccess();
    @Inject
    DependencyManagementServices getDependencyManagementServices();
    @Inject
    FileResolver getDependencyFileResolver();
    @Inject
    FileCollectionFactory getFileCollectionFactory();
}
