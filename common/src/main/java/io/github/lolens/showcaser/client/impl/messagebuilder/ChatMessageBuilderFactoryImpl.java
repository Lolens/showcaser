package io.github.lolens.showcaser.client.impl.messagebuilder;

import io.github.lolens.showcaser.api.builder.ChatMessageBuilder;
import io.github.lolens.showcaser.api.builder.ChatMessageBuilderFactory;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;

public class ChatMessageBuilderFactoryImpl implements ChatMessageBuilderFactory {

    public static final ChatMessageBuilderFactory INSTANCE = new ChatMessageBuilderFactoryImpl();

    @Override
    public ChatMessageBuilder create(ShareContext context,
                                     String senderName,
                                     ShareableResource resource,
                                     MessageVerification verified)
    {
        return ClientChatMessageBuilderImpl.create(context, senderName, resource, verified);
    }
}
