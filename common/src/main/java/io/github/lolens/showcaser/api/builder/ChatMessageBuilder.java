package io.github.lolens.showcaser.api.builder;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public interface ChatMessageBuilder {

    ChatMessageBuilder withWidth(int width);

    ChatMessageBuilder useBrackets(boolean use);

    ChatMessageBuilder showAmount(boolean show);

    ChatMessageBuilder withClickEvent(ClickEvent event);

    ChatMessageBuilder withFormatting(Formatting... formatting);

    ChatMessageBuilder withTranslationKey(String key);

    ChatMessageBuilder withForcedDisplayName(Text name);

    MutableText build();

}
