/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.forms.dynamic.backend.server.context.generation.statik.impl.processors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.drools.workbench.models.datamodel.oracle.Annotation;
import org.kie.workbench.common.forms.dynamic.backend.server.context.generation.statik.impl.FieldSetting;
import org.kie.workbench.common.forms.dynamic.service.context.generation.TransformerContext;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.selectors.radioGroup.definition.RadioGroupBaseDefinition;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.selectors.radioGroup.provider.RadioGroupFieldProvider;
import org.kie.workbench.common.forms.metaModel.RadioGroup;

@Dependent
public class RadioGroupFieldAnnotationProcessor extends AbstractSelectorAnnotationProcessor<RadioGroupBaseDefinition, RadioGroupFieldProvider> {

    @Inject
    public RadioGroupFieldAnnotationProcessor(RadioGroupFieldProvider fieldProvider) {
        super(fieldProvider);
    }

    @Override
    protected void initField(RadioGroupBaseDefinition field,
                             Annotation annotation,
                             FieldSetting fieldSetting,
                             TransformerContext context) {
        super.initField(field,
                        annotation,
                        fieldSetting,
                        context);
        field.setInline(Boolean.TRUE.equals(annotation.getParameters().get("inline")));
    }

    @Override
    protected Class<RadioGroup> getSupportedAnnotation() {
        return RadioGroup.class;
    }
}
