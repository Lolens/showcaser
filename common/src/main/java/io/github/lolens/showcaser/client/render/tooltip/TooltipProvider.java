package io.github.lolens.showcaser.client.render.tooltip;

import io.github.lolens.showcaser.model.ShareContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public interface TooltipProvider {

    List<Text> buildTooltip(ShareContext context, PlayerEntity player);

}
