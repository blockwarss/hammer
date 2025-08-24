package fr.survietiktok.hammer;

import org.bukkit.plugin.java.JavaPlugin;

public final class HammerPlugin extends JavaPlugin {

    private UpgradeItems upgradeItems;
    private HammerItems hammerItems;

    @Override
    public void onEnable() {
        this.hammerItems = new HammerItems(this);
        this.upgradeItems = new UpgradeItems(this);

        // Events
        getServer().getPluginManager().registerEvents(new HammerListener(this, hammerItems), this);
        getServer().getPluginManager().registerEvents(new UpgradeFusionListener(this, upgradeItems), this);

        // Commands
        HammerCommand hammerCmd = new HammerCommand(this, hammerItems);
        getCommand("hammer").setExecutor(hammerCmd);
        getCommand("hammer").setTabCompleter(hammerCmd);

        HammerUpgradeCommand upCmd = new HammerUpgradeCommand(this, upgradeItems);
        getCommand("hammerupgrade").setExecutor(upCmd);
        getCommand("hammerupgrade").setTabCompleter(upCmd);

        getLogger().info("HammerPlugin enabled.");
    }

    public boolean isHammer(org.bukkit.inventory.ItemStack stack) {
        return this.hammerItems.isHammer(stack);
    }
}