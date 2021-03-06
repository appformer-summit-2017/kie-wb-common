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

package org.kie.workbench.common.stunner.svg.gen.codegen.impl;

import org.kie.workbench.common.stunner.svg.gen.codegen.ShapeDefinitionGenerator;
import org.kie.workbench.common.stunner.svg.gen.exception.GeneratorException;
import org.kie.workbench.common.stunner.svg.gen.model.ShapeDefinition;
import org.kie.workbench.common.stunner.svg.gen.model.StyleDefinition;

public abstract class AbstractShapeDefinitionGenerator<I extends ShapeDefinition<?>>
        extends AbstractPrimitiveDefinitionGenerator<I>
        implements ShapeDefinitionGenerator<I> {

    private final static String FILL_COLOR = ".setFillColor(\"%1s\")";
    private final static String FILL_ALPHA = ".setFillAlpha(%1$.3f)";
    private final static String STROKE_COLOR = ".setStrokeColor(\"%1s\")";
    private final static String STROKE_ALPHA = ".setStrokeAlpha(%1$.3f)";
    private final static String STROKE_WIDTH = ".setStrokeWidth(%1$.3f)";

    @Override
    public StringBuffer generate(final I input) throws GeneratorException {
        final StringBuffer shapeRaw = super.generate(input);
        // Styles.
        final StyleDefinition styleDefinition = input.getStyleDefinition();
        if (null != styleDefinition) {
            if (null != styleDefinition.getFillColor()) {
                shapeRaw.append(String.format(FILL_COLOR,
                                              styleDefinition.getFillColor()));
            }
            shapeRaw.append(String.format(FILL_ALPHA,
                                          styleDefinition.getFillAlpha()));

            if (null != styleDefinition.getStrokeColor()) {
                shapeRaw.append(String.format(STROKE_COLOR,
                                              styleDefinition.getStrokeColor()));
            }
            shapeRaw.append(String.format(STROKE_ALPHA,
                                          styleDefinition.getStrokeAlpha()));
            shapeRaw.append(String.format(STROKE_WIDTH,
                                          styleDefinition.getStrokeWidth()));
            return shapeRaw;
        }
        return new StringBuffer();
    }
}
