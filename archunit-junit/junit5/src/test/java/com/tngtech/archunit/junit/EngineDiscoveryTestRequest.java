package com.tngtech.archunit.junit;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoveryFilter;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.UniqueIdSelector;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

class EngineDiscoveryTestRequest implements EngineDiscoveryRequest {
    private final List<UniqueId> idsToDiscover = new ArrayList<>();
    private final List<Class<?>> classesToDiscover = new ArrayList<>();
    private final List<URI> classpathRootsToDiscover = new ArrayList<>();

    private final List<ClassNameFilter> classNameFilters = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked") // compatibility is explicitly checked
    public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
        if (ClasspathRootSelector.class.equals(selectorType)) {
            return (List<T>) createClasspathRootSelectors(classpathRootsToDiscover);
        }
        if (ClassSelector.class.equals(selectorType)) {
            return (List<T>) createClassSelectors(classesToDiscover);
        }
        if (UniqueIdSelector.class.equals(selectorType)) {
            return (List<T>) createUniqueIdSelectors(idsToDiscover);
        }
        return emptyList();
    }

    private List<ClasspathRootSelector> createClasspathRootSelectors(List<URI> classpathRootsToDiscover) {
        return DiscoverySelectors.selectClasspathRoots(classpathRootsToDiscover.stream().map(Paths::get).collect(toSet()));
    }

    private List<ClassSelector> createClassSelectors(List<Class<?>> classesToDiscover) {
        return classesToDiscover.stream().map(DiscoverySelectors::selectClass).collect(toList());
    }

    private List<UniqueIdSelector> createUniqueIdSelectors(List<UniqueId> idsToDiscover) {
        return idsToDiscover.stream().map(DiscoverySelectors::selectUniqueId).collect(toList());
    }

    @Override
    @SuppressWarnings("unchecked") // compatibility is explicitly checked
    public <T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType) {
        if (ClassNameFilter.class.equals(filterType)) {
            return (List<T>) classNameFilters;
        }
        return emptyList();
    }

    @Override
    public ConfigurationParameters getConfigurationParameters() {
        return new EmptyConfigurationParameters();
    }

    EngineDiscoveryTestRequest withClasspathRoot(URI uri) {
        classpathRootsToDiscover.add(uri);
        return this;
    }

    EngineDiscoveryTestRequest withClass(Class<?> clazz) {
        classesToDiscover.add(clazz);
        return this;
    }

    EngineDiscoveryTestRequest withUniqueId(UniqueId id) {
        idsToDiscover.add(id);
        return this;
    }

    EngineDiscoveryTestRequest withClassNameFilter(ClassNameFilter filter) {
        classNameFilters.add(filter);
        return this;
    }

    private static class EmptyConfigurationParameters implements ConfigurationParameters {
        @Override
        public Optional<String> get(String key) {
            return Optional.empty();
        }

        @Override
        public Optional<Boolean> getBoolean(String key) {
            return Optional.empty();
        }

        @Override
        public int size() {
            return 0;
        }
    }
}
