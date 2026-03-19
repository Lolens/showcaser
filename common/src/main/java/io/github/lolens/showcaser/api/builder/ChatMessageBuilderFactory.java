package io.github.lolens.showcaser.api.builder;

import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;

public interface ChatMessageBuilderFactory {

    ChatMessageBuilder create(
            ShareContext context,
            String senderName,
            ShareableResource resource,
            MessageVerification verified
    );

}
