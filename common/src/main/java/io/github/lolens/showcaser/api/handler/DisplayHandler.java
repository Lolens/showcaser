package io.github.lolens.showcaser.api.handler;

import io.github.lolens.showcaser.core.ShareContext;
import net.minecraft.util.Identifier;

public interface DisplayHandler {

    Identifier getIdentifier();
    void display(String senderName, ShareContext context);



}
