package io.github.lolens.showcaser.config;

import jdk.jfr.Description;

public class ShowcaserClientConfig {
    // descriptions actually not doing anything

    @Description("Adds empty paragraph before '(un)verified by server' tooltip text.")
    public boolean addEmptySpaceBeforeVerifiedText = false;

    @Description("Items displayed in chat will use custom name that is set though anvil or any other way")
    public boolean ignoreCustomNames = false;

    @Description("Log screen class when context is created for it")
    public boolean logScreenClass = false;



}
