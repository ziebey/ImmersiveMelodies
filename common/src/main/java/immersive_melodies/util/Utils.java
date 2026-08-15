package immersive_melodies.util;

import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class Utils {
    public static boolean hasCommandLevel(Player player, int level) {
        return player.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(level)));
    }

    public static String toTitle(String string) {
        return StringUtils.capitalize(string.replace("_", " "));
    }

    public static String getPlayerName(Player player) {
        return player.getGameProfile().name().toLowerCase(Locale.ROOT);
    }

    public static boolean isPlayerMelody(Identifier identifier) {
        return identifier.getNamespace().equals("player");
    }

    public static boolean ownsMelody(Identifier identifier, Player player) {
        return isPlayerMelody(identifier) && identifier.getPath().startsWith(getPlayerName(player) + "/");
    }

    public static boolean canDelete(Identifier identifier, Player player) {
        return ownsMelody(identifier, player) || (isPlayerMelody(identifier) && hasCommandLevel(player, 2));
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
