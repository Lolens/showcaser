package io.github.lolens.showcaser.network.message.s2c.conditional.emi;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.serializer.EmiIngredientSerializer;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class EmiOpenScreenMessage extends BaseS2CMessage {
    private final Identifier recipeId;
    private final String jsonString;
    private final Type type;

    public enum Type {
        RECIPE_ID,
        INGREDIENT
    }

    public EmiOpenScreenMessage(Identifier recipeId) {
        this.type = Type.RECIPE_ID;
        this.recipeId = recipeId;
        this.jsonString = null;
    }

    public EmiOpenScreenMessage(NbtElement nbtElement) {
        this.type = Type.INGREDIENT;
        this.recipeId = null;

        JsonElement jsonElement = NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, nbtElement);
        this.jsonString = jsonElement.toString();
    }

    public EmiOpenScreenMessage(PacketByteBuf buf) {
        this.type = buf.readEnumConstant(Type.class);

        if (type == Type.RECIPE_ID) {
            this.recipeId = buf.readIdentifier();
            this.jsonString = null;
        } else {
            this.recipeId = null;
            this.jsonString = buf.readString();
        }
    }

    @Override
    public MessageType getType() {
        return Networking.Conditional.EMI.MessageTypes.S2C.EMI_OPEN_SCREEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(type);

        if (type == Type.RECIPE_ID) {
            buf.writeIdentifier(recipeId);
        } else {
            buf.writeString(jsonString);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (type == Type.RECIPE_ID) {
            EmiRecipe recipe = EmiApi.getRecipeManager().getRecipe(recipeId);
            if (recipe != null) {
                EmiApi.displayRecipe(recipe);
            }
        } else {
            // todo maybe add notification / message that resource does not have any recipes if it is
            JsonElement jsonElement = JsonParser.parseString(jsonString);
            EmiIngredient ingredient = EmiIngredientSerializer.getDeserialized(jsonElement);
            EmiApi.displayRecipes(ingredient);
        }
    }
}