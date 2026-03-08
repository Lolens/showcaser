package io.github.lolens.showcaser.network.message.s2c.conditional.emi;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EmiOpenScreenMessage extends BaseS2CMessage {
    private final Identifier target;
    private final TargetType type;

    public enum TargetType {
        RECIPE,
        ITEM,
        FLUID
    }

    public EmiOpenScreenMessage(Identifier resourceId, TargetType type) {
        this.type = type;
        this.target = resourceId;
    }

    public EmiOpenScreenMessage(PacketByteBuf buf) {
        this.type = buf.readEnumConstant(TargetType.class);
        this.target = buf.readIdentifier();
    }

    @Override
    public MessageType getType() {
        return Networking.Conditional.EMI.MessageTypes.S2C.EMI_OPEN_SCREEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(type);
        buf.writeIdentifier(target);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        switch (type) {
            case RECIPE -> {
                EmiRecipe recipe = EmiApi.getRecipeManager().getRecipe(target);
                if (recipe != null) EmiApi.displayRecipe(recipe);
            }
            case ITEM -> EmiApi.displayRecipes(EmiStack.of(Registries.ITEM.get(target)));
            case FLUID -> EmiApi.displayRecipes(EmiStack.of(Registries.FLUID.get(target)));
        }

    }
}