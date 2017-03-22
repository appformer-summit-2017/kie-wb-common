/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.forms.dynamic.client.rendering.renderers;

import javax.enterprise.context.Dependent;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.LongBox;
import org.kie.workbench.common.forms.dynamic.client.rendering.FieldRenderer;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.integerBox.definition.IntegerBoxDefinition;

@Dependent
public class IntegerBoxFieldRenderer extends FieldRenderer<IntegerBoxDefinition> {

    private LongBox longBox = new LongBox();

    @Override
    public String getName() {
        return "IntegerBox";
    }

    @Override
    public void initInputWidget() {
        longBox = new LongBox();
        longBox.setId(field.getId() );
        longBox.setPlaceholder(field.getPlaceHolder() );
        longBox.setMaxLength(field.getMaxLength() );
        longBox.setEnabled(!field.getReadOnly() );
    }

    @Override
    public IsWidget getInputWidget() {
        return longBox;
    }

    @Override
    public IsWidget getPrettyViewWidget() {
        return new HTML();
    }

    @Override
    public String getSupportedCode() {
        return IntegerBoxDefinition.FIELD_TYPE.getTypeName();
    }

    @Override
    protected void setReadOnly( boolean readOnly ) {
        longBox.setEnabled(!readOnly );
    }
}
