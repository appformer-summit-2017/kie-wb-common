/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.slider.definition;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.forms.adf.definitions.annotations.i18n.I18nSettings;
import org.kie.workbench.common.forms.model.FieldDefinition;

@Portable
@Bindable
@FormDefinition(
        i18n = @I18nSettings(keyPreffix = "FieldProperties"),
        startElement = "label"
)
public class IntegerSliderDefinition extends SliderBaseDefinition<Integer> {

    @FormField(
            labelKey = "slider.min",
            afterElement = "label"
    )
    protected Integer min;

    @FormField(
            labelKey = "slider.max",
            afterElement = "min"
    )
    protected Integer max;

    @FormField(
            labelKey = "slider.step",
            afterElement = "max"
    )
    protected Integer step;

    public IntegerSliderDefinition() {
        min = new Integer(0);
        max = new Integer(50);
        step = new Integer(1);
    }

    @Override
    public Integer getMin() {
        return min;
    }

    @Override
    public void setMin(Integer min) {
        this.min = min;
    }

    @Override
    public Integer getMax() {
        return max;
    }

    @Override
    public void setMax(Integer max) {
        this.max = max;
    }

    @Override
    public Integer getStep() {
        return step;
    }

    @Override
    public void setStep(Integer step) {
        this.step = step;
    }

    @Override
    public Integer getPrecision() {
        return new Integer(0);
    }

    @Override
    public void setPrecision(Integer precision) {
        // Integer Slider doesn't require precission
    }

    @Override
    protected void doCopyFrom(FieldDefinition other) {
        if (other instanceof SliderBaseDefinition) {
            SliderBaseDefinition otherSlider = (SliderBaseDefinition) other;
            min = otherSlider.getMin().intValue();
            max = otherSlider.getMax().intValue();
            step = otherSlider.getStep().intValue();
        }
    }
}
