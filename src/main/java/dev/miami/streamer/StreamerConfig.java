package dev.miami.streamer;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class StreamerConfig {
    @Getter private final static StreamerConfig instance = new StreamerConfig();

    @Getter @Setter private boolean enabled = false;
    @Getter @Setter boolean hideOthers = false;
    @Getter @Setter private String selfName = "Miami";
    @Setter @Getter private String othersName = "Player";
    @Getter @Setter private boolean hideSkin = false;
    @Getter @Setter private boolean hideCoords = false;

    public void toggleEnabled() {
        enabled = !enabled;
    }

    public void toggleHideOthers() {
        hideOthers = !hideOthers;
    }
    public void toggleHideSkin() {
        hideSkin = !hideSkin;
    }

    public void toggleHideCoords() {
        hideCoords = !hideCoords;
    }
}