/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.client.lienzo.shape.impl;

import org.kie.workbench.common.stunner.core.client.shape.Shape;
import org.kie.workbench.common.stunner.core.client.shape.impl.ShapeImpl;
import org.kie.workbench.common.stunner.core.client.shape.impl.ShapeWrapper;
import org.kie.workbench.common.stunner.core.client.shape.view.ShapeView;

public class LienzoShape<V extends ShapeView> extends ShapeWrapper<V, ShapeImpl<V>> {

    private final ShapeImpl<V> wrapped;

    public LienzoShape(final V view) {
        this.wrapped = new ShapeImpl<V>(view,
                                        new AnimationShapeStateHelper<>(this));
    }

    public LienzoShape(final V view,
                       final AnimationShapeStateHelper<V, Shape<V>> shapeStateHelper) {
        this.wrapped = new ShapeImpl<V>(view,
                                        shapeStateHelper);
    }

    @Override
    protected ShapeImpl<V> getWrappedShape() {
        return wrapped;
    }
}
