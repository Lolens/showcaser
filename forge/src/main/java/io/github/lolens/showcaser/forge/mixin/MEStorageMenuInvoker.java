package io.github.lolens.showcaser.forge.mixin;

import appeng.api.stacks.AEKey;
import appeng.menu.me.common.MEStorageMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = MEStorageMenu.class)
public interface MEStorageMenuInvoker {

    @Invoker(value = "getStackBySerial")
    AEKey showcaser$getStackBySerial(long serial);

}
