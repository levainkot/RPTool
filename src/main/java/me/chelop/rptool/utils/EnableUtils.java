package me.chelop.rptool.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class EnableUtils {
    private final FileConfiguration config;

    public EnableUtils(FileConfiguration config) {
        this.config = config;
    }

    public boolean check(String path) {
        String enable = config.getString(path);
        return enable != null && enable.equalsIgnoreCase("true");
    }
}