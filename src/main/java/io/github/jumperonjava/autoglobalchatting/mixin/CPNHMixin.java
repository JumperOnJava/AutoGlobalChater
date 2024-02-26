package io.github.jumperonjava.autoglobalchatting.mixin;

import io.github.jumperonjava.autoglobalchatting.AutoGlobalChatting;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPlayNetworkHandler.class)
public class CPNHMixin {
    @ModifyVariable(method = "sendChatMessage",at = @At("HEAD"),ordinal = 0)
    String addOrRemoveGlobal(String value){
        var hasGlobal = value.startsWith("!");
        var woGlobal = value;
        if(hasGlobal)
            woGlobal = value.substring(1);
        var autoGlobal = AutoGlobalChatting.reverse;
        return (hasGlobal ^ autoGlobal ? "!" : "") + woGlobal;
    }

}
