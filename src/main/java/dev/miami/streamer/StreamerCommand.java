package dev.miami.streamer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

@Environment(EnvType.CLIENT)
public class StreamerCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("streamer")
                        .then(literal("toggle")
                                .executes(ctx -> {
                                    StreamerConfig.getInstance().toggleEnabled();
                                    sendFeedback(ctx, "Streamer mode " + (StreamerConfig.getInstance().isEnabled() ? "§aenabled" : "§cdisabled"));
                                    return 1;
                                })
                                .then(literal("hideothers")
                                        .executes(ctx -> {
                                            StreamerConfig.getInstance().toggleHideOthers();
                                            sendFeedback(ctx, "Hide others " + (StreamerConfig.getInstance().isHideOthers() ? "§aenabled" : "§cdisabled"));
                                            return 1;
                                        })
                                )
                                .then(literal("hideskin")
                                        .executes(ctx -> {
                                            StreamerConfig.getInstance().toggleHideSkin();
                                            sendFeedback(ctx, "Skin hider " + (StreamerConfig.getInstance().isHideSkin() ? "§aenabled" : "§cdisabled"));
                                            return 1;
                                        })
                                )
                                .then(literal("hidecoords")
                                        .executes(ctx -> {
                                            StreamerConfig.getInstance().toggleHideCoords();
                                            sendFeedback(ctx, "Hide coords " + (StreamerConfig.getInstance().isHideCoords() ? "§aenabled" : "§cdisabled"));
                                            return 1;
                                        })
                                )
                        )

                        .then(literal("change")
                                .then(literal("self")
                                        .then(argument("username", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String name = StringArgumentType.getString(ctx, "username");
                                                    StreamerConfig.getInstance().setSelfName(name);
                                                    sendFeedback(ctx, "§aYour display name set to: §f" + name);
                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("others")
                                        .then(argument("username", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String name = StringArgumentType.getString(ctx, "username");
                                                    StreamerConfig.getInstance().setOthersName(name);
                                                    sendFeedback(ctx, "§aOthers display name set to: §f" + name);
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }

    private static void sendFeedback(CommandContext<FabricClientCommandSource> ctx, String feedback) {
        ctx.getSource().sendFeedback(Component.literal("§6[Streamer] §f" + feedback));
    }
}