/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yangtools.binding.data.codec.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map.Entry;
import javassist.ClassPool;
import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.mdsal.binding.generator.util.JavassistUtils;
import org.opendaylight.yang.gen.v1.bug5446.rev151105.IpAddressBinary;
import org.opendaylight.yang.gen.v1.bug5446.rev151105.IpAddressBinaryBuilder;
import org.opendaylight.yang.gen.v1.bug5446.rev151105.Root;
import org.opendaylight.yang.gen.v1.bug5446.rev151105.RootBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.yangtools.test.union.rev150121.TopLevel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.yangtools.test.union.rev150121.TopLevelBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.yangtools.test.union.rev150121.Wrapper;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.yangtools.test.union.rev150121.WrapperBuilder;
import org.opendaylight.yangtools.binding.data.codec.gen.impl.StreamWriterGenerator;
import org.opendaylight.yangtools.binding.data.codec.impl.BindingNormalizedNodeCodecRegistry;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.schema.ContainerNode;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;
import org.opendaylight.yangtools.yang.data.impl.schema.ImmutableNodes;
import org.opendaylight.yangtools.yang.data.impl.schema.builder.impl.ImmutableContainerNodeBuilder;

public class UnionTypeTest extends AbstractBindingRuntimeTest {

    private static final String testString = "testtesttest";

    public static final QName WRAPPER_QNAME = QName.create("urn:opendaylight:params:xml:ns:yang:yangtools:test:union", "2015-01-21", "wrapper");
    public static final QName WRAP_LEAF_QNAME = QName.create(WRAPPER_QNAME, "wrap");

    private BindingNormalizedNodeCodecRegistry registry;

    @Override
    public void setup() {
        super.setup();
        JavassistUtils utils = JavassistUtils.forClassPool(ClassPool.getDefault());
        registry = new BindingNormalizedNodeCodecRegistry(StreamWriterGenerator.create(utils));
        registry.onBindingRuntimeContextUpdated(getRuntimeContext());
    }

    @Test
    public void unionTest() {
        TopLevel topLevel = TopLevelBuilder.getDefaultInstance(testString);
        Wrapper wrapper = new WrapperBuilder().setWrap(topLevel).build();
        NormalizedNode<?, ?> topLevelEntry = registry.toNormalizedNode(InstanceIdentifier.builder(Wrapper.class).build(), wrapper).getValue();

        ContainerNode containerNode = ImmutableContainerNodeBuilder.create()
                .withNodeIdentifier(new YangInstanceIdentifier.NodeIdentifier(WRAPPER_QNAME))
                .withChild(ImmutableNodes.leafNode(WRAP_LEAF_QNAME, testString))
                .build();
        Assert.assertEquals(topLevelEntry, containerNode);
    }

    @Test
    public void bug5446Test() {
        IpAddressBinary ipAddress = IpAddressBinaryBuilder.getDefaultInstance("fwAAAQ==");
        Root root = new RootBuilder().setIpAddress(ipAddress).build();
        NormalizedNode<?, ?> rootNode = registry.toNormalizedNode(InstanceIdentifier.builder(Root.class).build(), root)
                .getValue();

        Entry<InstanceIdentifier<?>, DataObject> rootEntry = registry.fromNormalizedNode(
                YangInstanceIdentifier.of(rootNode.getNodeType()), rootNode);

        DataObject rootObj = rootEntry.getValue();
        assertTrue(rootObj instanceof Root);
        IpAddressBinary desIpAddress = ((Root) rootObj).getIpAddress();
        assertEquals(ipAddress, desIpAddress);
    }
}
