package owlbe.skriptLuckPerms.update;

import ch.njol.skript.util.Version;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

// Credit to
// https://github.com/ShaneBeee/SkBee/tree/master/src/main/java/com/shanebeestudios/skbee/api/util/update for the original versions
// & https://github.com/3add/PacketEventsSK/tree/main/src/main/java/dev/threeadd/packeteventssk/update for the version that this is build upon

public class ModrinthVersion {

    private final Version updateVersion;
    private final List<Version> supportedVersions = new ArrayList<>();

    public ModrinthVersion(JsonElement jsonElement) {
        JsonObject json = jsonElement.getAsJsonObject();
        this.updateVersion = new Version(json.get("version_number").getAsString());
        JsonArray gameVersions = json.getAsJsonArray("game_versions");
        gameVersions.forEach(version -> this.supportedVersions.add(new Version(version.getAsString())));
    }

    public Version getUpdateVersion() {
        return this.updateVersion;
    }

    public String getUpdateLink() {
        return "https://modrinth.com/plugin/skript-luckperms/version/" + this.updateVersion;
    }

    public List<Version> getSupportedVersions() {
        return this.supportedVersions;
    }

    public boolean isServerSupported(Version serverVersion) {
        return this.supportedVersions.contains(serverVersion);
    }
}
