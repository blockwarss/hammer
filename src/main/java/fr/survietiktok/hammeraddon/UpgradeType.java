package fr.survietiktok.hammeraddon;

import org.bukkit.ChatColor;

public enum UpgradeType {
    BASIC("Basic", 2, false, ChatColor.GOLD),          // &6
    RARE("Rare", 4, false, ChatColor.BLUE),            // &9
    EPIC("Epic", 3, true,  ChatColor.YELLOW);          // &e

    private final String display;
    private final int efficiencyLevel;
    private final boolean toDiamond;
    private final ChatColor color;

    UpgradeType(String display, int efficiencyLevel, boolean toDiamond, ChatColor color) {
        this.display = display;
        this.efficiencyLevel = efficiencyLevel;
        this.toDiamond = toDiamond;
        this.color = color;
    }

    public String display() { return display; }
    public int efficiencyLevel() { return efficiencyLevel; }
    public boolean convertToDiamond() { return toDiamond; }
    public ChatColor color() { return color; }

    /** Ex: "&6&lBasic" */
    public String styled() { return color.toString() + ChatColor.BOLD + display + ChatColor.RESET; }

    public static UpgradeType fromString(String s) {
        if (s == null) return null;
        switch (s.toLowerCase()) {
            case "basic":
            case "basique": return BASIC;
            case "rare": return RARE;
            case "epic":
            case "epique":
            case "épique": return EPIC;
            default: return null;
        }
    }
}