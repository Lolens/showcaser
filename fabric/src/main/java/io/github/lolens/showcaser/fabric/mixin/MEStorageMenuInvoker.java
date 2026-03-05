package io.github.lolens.showcaser.fabric.mixin;

import appeng.api.stacks.AEKey;
import appeng.menu.me.common.MEStorageMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = MEStorageMenu.class, remap = false)
public interface MEStorageMenuInvoker {

    @Invoker(value = "getStackBySerial", remap = false)
    AEKey showcaser$getStackBySerial(long serial);

}
