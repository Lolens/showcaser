package io.github.lolens.showcaser.config;

import jdk.jfr.Description;

public class ShowcaserConfig {

    @Description("Adds empty paragraph before '(un)verified by server' tooltip text.")
    public boolean addEmptySpaceBeforeVerifiedText = false;

    @Description("Adds empty space after fluid amount in chat message and its tooltip")
    public boolean addEmptySpaceAfterFluidAmount = true;

    @Description("Items displayed in chat will use custom name that is set though anvil or any other way")
    public boolean ignoreCustomNames = true;

    @Description("Item sharing cooldown")
    public int chatSharingCooldown = 20;



}
