package com.codegeek.hogwartsartifactsonline.wizard.converter;

import com.codegeek.hogwartsartifactsonline.wizard.Wizard;
import com.codegeek.hogwartsartifactsonline.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDTO, Wizard> {
    @Override
    public Wizard convert(WizardDTO source) {
        Wizard wizard = new Wizard();
        wizard.setName(source.name());
        wizard.setId(source.id());
        return wizard;
    }
}
