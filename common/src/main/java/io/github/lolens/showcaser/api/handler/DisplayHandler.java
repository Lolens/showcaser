package io.github.lolens.showcaser.api.handler;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.OverrideOnly
public interface DisplayHandler {

    Identifier getIdentifier();

    void display(String senderName, ShareContext context);



}
