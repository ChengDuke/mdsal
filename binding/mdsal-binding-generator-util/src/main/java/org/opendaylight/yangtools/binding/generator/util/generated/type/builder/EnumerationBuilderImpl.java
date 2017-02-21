/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.binding.generator.util.generated.type.builder;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.opendaylight.mdsal.binding.model.api.AnnotationType;
import org.opendaylight.mdsal.binding.model.api.Constant;
import org.opendaylight.mdsal.binding.model.api.Enumeration;
import org.opendaylight.mdsal.binding.model.api.GeneratedProperty;
import org.opendaylight.mdsal.binding.model.api.GeneratedType;
import org.opendaylight.mdsal.binding.model.api.MethodSignature;
import org.opendaylight.mdsal.binding.model.api.Type;
import org.opendaylight.mdsal.binding.model.api.Enumeration.Pair;
import org.opendaylight.mdsal.binding.model.api.type.builder.AnnotationTypeBuilder;
import org.opendaylight.mdsal.binding.model.api.type.builder.EnumBuilder;
import org.opendaylight.yangtools.binding.generator.util.AbstractBaseType;
import org.opendaylight.yangtools.util.LazyCollections;
import org.opendaylight.yangtools.yang.binding.BindingMapping;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.Status;
import org.opendaylight.yangtools.yang.model.api.type.EnumTypeDefinition;
import org.opendaylight.yangtools.yang.model.api.type.EnumTypeDefinition.EnumPair;

/**
 * @deprecated Use {@link org.opendaylight.mdsal.binding.generator.util.generated.type.builder.EnumerationBuilderImpl} instead.
 */
@Deprecated
public final class EnumerationBuilderImpl extends AbstractBaseType implements EnumBuilder {
    private final String packageName;
    private final String name;
    private List<Enumeration.Pair> values = Collections.emptyList();
    private List<AnnotationTypeBuilder> annotationBuilders = Collections.emptyList();
    private List<Pair> unmodifiableValues  = Collections.emptyList();
    private String description;
    private String reference;
    private String moduleName;
    private Iterable<QName> schemaPath;

    public EnumerationBuilderImpl(final String packageName, final String name) {
        super(packageName, name);
        this.packageName = packageName;
        this.name = name;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public void setSchemaPath(final Iterable<QName> schemaPath) {
        this.schemaPath = schemaPath;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;

    }

    @Override
    public AnnotationTypeBuilder addAnnotation(final String packageName, final String name) {
        if (packageName != null && name != null) {
            final AnnotationTypeBuilder builder = new AnnotationTypeBuilderImpl(packageName, name);
            if (!annotationBuilders.contains(builder)) {
                annotationBuilders = LazyCollections.lazyAdd(annotationBuilders, builder);
                return builder;
            }
        }
        return null;
    }

    @Override
    public void addValue(final String name, final int value, final String description) {
        final EnumPairImpl p = new EnumPairImpl(name, value, description);
        values = LazyCollections.lazyAdd(values, p);
        unmodifiableValues = Collections.unmodifiableList(values);
    }

    @Override
    public Enumeration toInstance(final Type definingType) {
        return new EnumerationImpl(definingType, annotationBuilders, packageName, name, unmodifiableValues,
                description, reference, moduleName, schemaPath);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EnumerationBuilderImpl [packageName=");
        builder.append(packageName);
        builder.append(", name=");
        builder.append(name);
        builder.append(", values=");
        builder.append(values);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public void updateEnumPairsFromEnumTypeDef(final EnumTypeDefinition enumTypeDef) {
        final List<EnumPair> enums = enumTypeDef.getValues();
        if (enums != null) {
            for (final EnumPair enumPair : enums) {
                if (enumPair != null) {
                    this.addValue(enumPair.getName(), enumPair.getValue(), enumPair.getDescription());
                }
            }
        }

    }

    private static final class EnumPairImpl implements Enumeration.Pair {

        private final String name;
        private final String mappedName;
        private final int value;
        private final String description;

        public EnumPairImpl(final String name, final int value, final String description) {
            super();
            this.name = name;
            this.mappedName = BindingMapping.getClassName(name);
            this.value = value;
            this.description = description;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getMappedName() {
            return mappedName;
        }

        @Override
        public int getValue() {
            return value;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Objects.hashCode(name);
            result = prime * result + Objects.hashCode(value);
            return result;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            EnumPairImpl other = (EnumPairImpl) obj;
            return Objects.equals(name, other.name) && Objects.equals(value, other.value);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("EnumPair [name=");
            builder.append(name);
            builder.append(", mappedName=");
            builder.append(getMappedName());
            builder.append(", value=");
            builder.append(value);
            builder.append("]");
            return builder.toString();
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getReference() {
            return null;
        }

        @Override
        public Status getStatus() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private static final class EnumerationImpl extends AbstractBaseType implements Enumeration {

        private final Type definingType;
        private final String description;
        private final String reference;
        private final String moduleName;
        private final Iterable<QName> schemaPath;
        private final List<Pair> values;
        private final List<AnnotationType> annotations;

        public EnumerationImpl(final Type definingType, final List<AnnotationTypeBuilder> annotationBuilders,
                final String packageName, final String name, final List<Pair> values, final String description,
                final String reference, final String moduleName, final Iterable<QName> schemaPath) {
            super(packageName, name);
            this.definingType = definingType;
            this.values = values;
            this.description = description;
            this.moduleName = moduleName;
            this.schemaPath = schemaPath;
            this.reference = reference;

            final ArrayList<AnnotationType> a = new ArrayList<>();
            for (final AnnotationTypeBuilder builder : annotationBuilders) {
                a.add(builder.toInstance());
            }
            this.annotations = ImmutableList.copyOf(a);
        }

        @Override
        public Type getParentType() {
            return definingType;
        }

        @Override
        public List<Pair> getValues() {
            return values;
        }

        @Override
        public List<AnnotationType> getAnnotations() {
            return annotations;
        }

        @Override
        public String toFormattedString() {
            StringBuilder builder = new StringBuilder();
            builder.append("public enum");
            builder.append(" ");
            builder.append(getName());
            builder.append(" {");
            builder.append("\n");

            int i = 0;
            for (final Enumeration.Pair valPair : values) {
                builder.append("\t");
                builder.append(" ");
                builder.append(valPair.getMappedName());
                builder.append(" (");
                builder.append(valPair.getValue());

                if (i == (values.size() - 1)) {
                    builder.append(" );");
                } else {
                    builder.append(" ),");
                }
                ++i;
            }
            builder.append("\n}");
            return builder.toString();
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Enumeration [packageName=");
            builder.append(getPackageName());
            if (definingType != null) {
                builder.append(", definingType=");
                builder.append(definingType.getPackageName());
                builder.append(".");
                builder.append(definingType.getName());
            } else {
                builder.append(", definingType= null");
            }
            builder.append(", name=");
            builder.append(getName());
            builder.append(", values=");
            builder.append(values);
            builder.append("]");
            return builder.toString();
        }

        @Override
        public String getComment() {
            return null;
        }

        @Override
        public boolean isAbstract() {
            return false;
        }

        @Override
        public List<Type> getImplements() {
            return Collections.emptyList();
        }

        @Override
        public List<GeneratedType> getEnclosedTypes() {
            return Collections.emptyList();
        }

        @Override
        public List<Enumeration> getEnumerations() {
            return Collections.emptyList();
        }

        @Override
        public List<Constant> getConstantDefinitions() {
            return Collections.emptyList();
        }

        @Override
        public List<MethodSignature> getMethodDefinitions() {
            return Collections.emptyList();
        }

        @Override
        public List<GeneratedProperty> getProperties() {
            return Collections.emptyList();
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getReference() {
            return reference;
        }

        @Override
        public Iterable<QName> getSchemaPath() {
            return schemaPath;
        }

        @Override
        public String getModuleName() {
            return moduleName;
        }
    }
}
