package com.codegeek.hogwartsartifactsonline.wizard;

public class WizardNotFoundException extends RuntimeException {
    public WizardNotFoundException(Integer wizardId) {
        super("Could not find wizard with Id " + wizardId + " :(");
    }
}
