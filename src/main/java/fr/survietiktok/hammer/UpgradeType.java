package fr.survietiktok.hammer;

public enum UpgradeType {
    BASIC("Basic", 2, false),
    RARE("Rare", 4, false),
    EPIC("Epic", 3, true);

    private final String display;
    private final int efficiencyLevel;
    private final boolean toDiamond;

    UpgradeType(String display, int efficiencyLevel, boolean toDiamond) {
        this.display = display;
        this.efficiencyLevel = efficiencyLevel;
        this.toDiamond = toDiamond;
    }

    public String display() {
        return display;
    }

    public int efficiencyLevel() {
        return efficiencyLevel;
    }

    public boolean convertToDiamond() {
        return toDiamond;
    }

    public static UpgradeType fromString(String s) {
        if (s == null) return null;
        switch (s.toLowerCase()) {
            case "basic":
            case "basique":
                return BASIC;
            case "rare":
                return RARE;
            case "epic":
            case "epique":
            case "épique":
                return EPIC;
            default:
                return null;
        }
    }
}