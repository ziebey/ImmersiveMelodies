package immersive_melodies.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class Utils {
    public static String escapeString(String string) {
        return string.toLowerCase(Locale.ROOT).replaceAll("[^a-z\\d_.-]", "");
    }

    public static String toTitle(String string) {
        return StringUtils.capitalize(string.replace("_", " "));
    }

    public static String getPlayerName(Player player) {
        return escapeString(player.getGameProfile().getName());
    }

    public static boolean isPlayerMelody(ResourceLocation identifier) {
        return identifier.getNamespace().equals("player");
    }

    public static boolean ownsMelody(ResourceLocation identifier, Player player) {
        return isPlayerMelody(identifier) && identifier.getPath().startsWith(getPlayerName(player) + "/");
    }

    public static boolean canDelete(ResourceLocation identifier, Player player) {
        return ownsMelody(identifier, player) || (isPlayerMelody(identifier) && player.hasPermissions(2));
    }

    public static String removeLastPart(String input, String delimiter) {
        int lastDotIndex = input.lastIndexOf(delimiter);

        if (lastDotIndex != -1) {
            return input.substring(0, lastDotIndex);
        } else {
            return input;
        }
    }

    public static String getLastPart(String input, String delimiter) {
        int lastDotIndex = input.lastIndexOf(delimiter);

        if (lastDotIndex != -1) {
            return input.substring(lastDotIndex + delimiter.length());
        } else {
            return input;
        }
    }
}
