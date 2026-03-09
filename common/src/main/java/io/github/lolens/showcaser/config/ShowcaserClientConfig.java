package io.github.lolens.showcaser.config;

import jdk.jfr.Description;

public class ShowcaserClientConfig {

    @Description("Adds empty paragraph before '(un)verified by server' tooltip text.")
    public boolean addEmptySpaceBeforeVerifiedText = false;

    @Description("Items displayed in chat will use custom name that is set though anvil or any other way")
    public boolean ignoreCustomNames = false;


}
