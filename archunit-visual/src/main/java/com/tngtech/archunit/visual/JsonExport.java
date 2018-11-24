/*
 * Copyright 2018 TNG Technology Consulting GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tngtech.archunit.visual;

import java.util.Objects;
import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;

class JsonExport {
    @Expose
    private final JsonJavaPackage root;
    @Expose
    private final Set<JsonJavaDependency> dependencies;

    JsonExport(JsonJavaPackage root, Set<JsonJavaDependency> dependencies) {
        this.root = root;
        this.dependencies = dependencies;
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, dependencies);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final JsonExport other = (JsonExport) obj;
        return Objects.equals(this.root, other.root)
                && Objects.equals(this.dependencies, other.dependencies);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("root", root)
                .add("dependencies", dependencies)
                .toString();
    }
}
