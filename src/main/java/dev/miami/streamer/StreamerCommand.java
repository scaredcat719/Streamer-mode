package dev.miami.streamer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class StreamerCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("streamer")
                        .then(literal("toggle")
                                .executes(ctx -> {
                                    StreamerConfig.getInstance().toggleEnabled();
                                    ctx.getSource().sendFeedback(
                                            Text.literal("Streamer mode " + (StreamerConfig.getInstance().isEnabled() ? "§aenabled" : "§cdisabled"))
                                    );
                                    return 1;
                                })
                                .then(literal("hideothers")
                                        .executes(ctx -> {
                                            StreamerConfig.getInstance().toggleHideOthers();
                                            ctx.getSource().sendFeedback(
                                                    Text.literal("Hide others " + (StreamerConfig.getInstance().isHideOthers() ? "§aenabled" : "§cdisabled"))
                                            );
                                            return 1;
                                        })
                                )
                                .then(literal("hideskin")
                                        .executes(ctx -> {
                                            StreamerConfig.getInstance().toggleHideSkin();
                                            ctx.getSource().sendFeedback(
                                                    Text.literal("Skin hider " + (StreamerConfig.getInstance().isHideSkin() ? "§aenabled" : "§cdisabled"))
                                            );
                                            return 1;
                                        })
                                )
                                .then(literal("hidecoords")
                                        .executes(ctx -> {
                                            StreamerConfig.getInstance().toggleHideCoords();
                                            ctx.getSource().sendFeedback(
                                                    Text.literal("Hide coords " + (StreamerConfig.getInstance().isHideCoords() ? "§aenabled" : "§cdisabled"))
                                            );
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
                                                    ctx.getSource().sendFeedback(
                                                            Text.literal("§aYour display name set to: §f" + name)
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("others")
                                        .then(argument("username", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String name = StringArgumentType.getString(ctx, "username");
                                                    StreamerConfig.getInstance().setOthersName(name);
                                                    ctx.getSource().sendFeedback(
                                                            Text.literal("§aOthers display name set to: §f" + name)
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }
}