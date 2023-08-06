package me.chelop.rptool.utils;

import org.bukkit.ChatColor;

public class ReplaceUtils {

    public static String replace(String string, Object... replacements) {
        String result = string;

        for (int i = 0; i < replacements.length; i += 2) {
            Object oldValue = replacements[i];
            Object newValue = replacements[i + 1];

            result = result.replace(oldValue.toString(), newValue.toString());
        }

        result = ChatColor.translateAlternateColorCodes('&', result);

        return result;
    }
}