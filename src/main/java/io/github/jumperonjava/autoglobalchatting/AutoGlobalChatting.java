package io.github.jumperonjava.autoglobalchatting;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.nio.file.Path;
import java.sql.Struct;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AutoGlobalChatting implements ClientModInitializer {
    public static boolean reverse = false;
    public static final Path CFGDIR = FabricLoader.getInstance().getConfigDir().resolve("agchat.cfg");
    @Override
    public void onInitializeClient() {
        reverse = Boolean.parseBoolean(FileReadWrite.read(CFGDIR));
        ClientCommandRegistrationCallback.EVENT.register(this::regCmd);
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if(screen instanceof ChatScreen){
                screen.drawables.add((a,b,c,d)->{
                    if(reverse)
                    a.drawText(
                            client.textRenderer,
                            Text.literal("You automatically write into %s chat".formatted(reverse?"Global":"Local")),
                            2,
                            screen.height-23,
                            ColorHelper.Argb.getArgb(255,255,255,255),
                            true);
                });
            }
        });

    }

    private void regCmd(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        dispatcher.register(literal("agcglobal").executes((context)->{reverse=true;configSave();return 0;}));
        dispatcher.register(literal("agclocal").executes((context)->{reverse=false;configSave();return 0;}));
        dispatcher.register(literal("agctoggle").executes((context)->{reverse=!reverse;configSave();return 0;}));
    }
    private void configSave(){
        FileReadWrite.write(CFGDIR,Boolean.toString(AutoGlobalChatting.reverse));
    }
}
