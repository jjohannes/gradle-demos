package software.onepiece.toolchain.service;

import org.gradle.api.NonNullApi;
import org.gradle.internal.snapshot.FileSystemLocationSnapshot;
import org.gradle.internal.snapshot.SnapshottingFilter;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NonNullApi
class ToolExcludesFilter implements SnapshottingFilter {

    private final List<String> excludedDirectories;

    ToolExcludesFilter(List<String> excludedDirectories) {
        this.excludedDirectories = excludedDirectories;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public DirectoryWalkerPredicate getAsDirectoryWalkerPredicate() {
        return new DirectoryPredicate(excludedDirectories);
    }

    @Override
    public FileSystemSnapshotPredicate getAsSnapshotPredicate() {
        return new SnapshotPredicate();
    }

    private static class DirectoryPredicate implements DirectoryWalkerPredicate {
        private final List<String> excludedDirectories;

        private DirectoryPredicate(List<String> excludedDirectories) {
            this.excludedDirectories = excludedDirectories;
        }

        @Override
        public boolean test(Path path, String name, boolean isDirectory, Iterable<String> relativePath) {
            String pathAsString = StreamSupport.stream(relativePath.spliterator(), false).collect(Collectors.joining("/"));
            return excludedDirectories.stream().noneMatch(pathAsString::matches);
        }
    }

    private static class SnapshotPredicate implements FileSystemSnapshotPredicate {
        @Override
        public boolean test(FileSystemLocationSnapshot fileSystemLocation, Iterable<String> relativePath) {
            return true;
        }
    }
}
