/*
 * Copyright 2014-2021 TNG Technology Consulting GmbH
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
package com.tngtech.archunit.core.importer;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.tngtech.archunit.core.domain.JavaClassDescriptor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;

class JavaClassDescriptorImporter {
    /**
     * Takes an 'internal' ASM object type name, i.e. the class name but with slashes instead of periods,
     * i.e. java/lang/Object (note that this is not a descriptor like Ljava/lang/Object;)
     */
    static JavaClassDescriptor createFromAsmObjectTypeName(String objectTypeName) {
        return JavaClassDescriptor.From.name(Type.getObjectType(objectTypeName).getClassName());
    }

    static JavaClassDescriptor importAsmType(Object type) {
        return importAsmType((Type) type);
    }

    private static JavaClassDescriptor importAsmType(Type type) {
        return JavaClassDescriptor.From.name(type.getClassName());
    }

    static boolean isAsmType(Object value) {
        return value instanceof Type;
    }

    static JavaClassDescriptor importAsmHandle(Object handle) {
        return importAsmHandle((Handle) handle);
    }

    private static JavaClassDescriptor importAsmHandle(Handle handle) {
        return JavaClassDescriptor.From.name(handle.getOwner() + handle.getName());
    }

    static boolean isAsmHandle(Object value) {
        return value instanceof Handle;
    }

    static Object importAsmTypeIfPossible(Object value) {
        return isAsmType(value) ? importAsmType(value) : value;
    }

    static JavaClassDescriptor importAsmTypeFromDescriptor(String typeDescriptor) {
        return importAsmType(Type.getType(typeDescriptor));
    }

    static List<JavaClassDescriptor> importAsmMethodArgumentTypes(String methodDescriptor) {
        ImmutableList.Builder<JavaClassDescriptor> result = ImmutableList.builder();
        for (Type type : Type.getArgumentTypes(methodDescriptor)) {
            result.add(importAsmType(type));
        }
        return result.build();
    }

    static JavaClassDescriptor importAsmMethodReturnType(String methodDescriptor) {
        return importAsmType(Type.getReturnType(methodDescriptor));
    }
}
