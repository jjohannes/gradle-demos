package software.onepiece.junit;

import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.vintage.engine.VintageTestEngine;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ClassloaderIsolationVintageTestEngine implements TestEngine {

    private static final String PROJECT_BASE_PACKAGE = "myproject.";

    VintageTestEngine vintageEngine = new VintageTestEngine();

    @Override
    public String getId() {
        return "classloader-isolation-junit-vintage";
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        DelegatingDiscoveryRequest delegatingDiscoveryRequest = new DelegatingDiscoveryRequest(discoveryRequest);
        return vintageEngine.discover(delegatingDiscoveryRequest, uniqueId);
    }

    @Override
    public void execute(ExecutionRequest request) {
        vintageEngine.execute(request);
    }

    private static class DelegatingDiscoveryRequest implements EngineDiscoveryRequest {
        private final EngineDiscoveryRequest delegate;

        DelegatingDiscoveryRequest(EngineDiscoveryRequest delegate) {
            this.delegate = delegate;
        }

        @Override
        public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
            List<T> selectorsByType = delegate.getSelectorsByType(selectorType);
            List<T> reLoadedSelectors = new ArrayList<>();
            for (T selector : selectorsByType) {
                ClassSelector classSelector = (ClassSelector) selector;
                @SuppressWarnings("unchecked")
                T reloadedClassSelector = (T) DiscoverySelectors.selectClass(loadNewClass(classSelector.getClassName()));
                reLoadedSelectors.add(reloadedClassSelector);
            }
            return reLoadedSelectors;
        }

        @Override
        public <T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType) {
            return delegate.getFiltersByType(filterType);
        }

        @Override
        public ConfigurationParameters getConfigurationParameters() {
            return delegate.getConfigurationParameters();
        }

        public EngineDiscoveryListener getDiscoveryListener() {
            return delegate.getDiscoveryListener();
        }

        private static Class<?> loadNewClass(String className) {
            try {
                String pathSeparator = System.getProperty("path.separator");
                String[] classPathEntries = System.getProperty("java.class.path").split(pathSeparator);
                URL[] urls = new URL[classPathEntries.length];
                for (int i = 0; i < classPathEntries.length; i++) {
                    urls[i] = Paths.get(classPathEntries[i]).toUri().toURL();
                }
                ClassLoader testClassLoader = new URLClassLoader(urls) {
                    @Override
                    public Class<?> loadClass(String name)
                            throws ClassNotFoundException {
                        if (name.startsWith(PROJECT_BASE_PACKAGE)) {
                            return super.findClass(name);
                        }
                        return super.loadClass(name);
                    }
                };
                return Class.forName(className, true, testClassLoader);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
