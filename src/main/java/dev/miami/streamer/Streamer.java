package dev.miami.streamer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

@Environment(EnvType.CLIENT)
public class Streamer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			StreamerCommand.register(dispatcher);
		});
	}
}