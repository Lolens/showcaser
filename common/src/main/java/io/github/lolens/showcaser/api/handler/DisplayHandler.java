package io.github.lolens.showcaser.api.handler;

import io.github.lolens.showcaser.api.shareContext.ShareContext;
import net.minecraft.util.Identifier;

public interface DisplayHandler {

    Identifier getIdentifier();
    void display(String senderName, ShareContext context);



}
