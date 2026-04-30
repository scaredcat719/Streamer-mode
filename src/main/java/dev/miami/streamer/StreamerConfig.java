package dev.miami.streamer;

import lombok.Getter;
import lombok.Setter;

public class StreamerConfig {
    private static StreamerConfig instance;

    public static StreamerConfig getInstance() {
        if (instance == null)
            instance = new StreamerConfig();
        return instance;
    }

    @Getter @Setter private boolean enabled = false;
    @Getter @Setter boolean hideOthers = false;
    @Getter @Setter private String selfName = "Streamer";
    @Setter @Getter private String othersName = "Player";

    public void toggleEnabled()      {
        enabled = !enabled;
    }
    public void toggleHideOthers()   { hideOthers = !hideOthers; }
    @Getter @Setter private boolean hideSkin = false;
    public void toggleHideSkin() { hideSkin = !hideSkin; }
    @Getter @Setter private boolean hideCoords = false;
    public void toggleHideCoords() { hideCoords = !hideCoords; }
}