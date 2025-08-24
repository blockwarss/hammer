package fr.survietiktok.hammeraddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HammerUpgradesPlugin extends JavaPlugin {

    private HammerHandle handle;
    private UpgradeItems upgradeItems;

    @Override
    public void onEnable() {
        Plugin hammer = Bukkit.getPluginManager().getPlugin("Hammer");
        if (hammer == null || !hammer.isEnabled()) {
            getLogger().severe("Le plugin Hammer n'est pas chargé. Addon désactivé.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.handle = new HammerHandle(hammer);
        if (!handle.isReady()) {
            getLogger().severe("Impossible d'accéder à HammerPlugin#isHammer(ItemStack). Addon désactivé.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.upgradeItems = new UpgradeItems(this);

        getServer().getPluginManager().registerEvents(new UpgradeFusionListener(this, handle, upgradeItems), this);

        HammerUpgradeCommand cmd = new HammerUpgradeCommand(this, upgradeItems);
        getCommand("hammerupgrade").setExecutor(cmd);
        getCommand("hammerupgrade").setTabCompleter(cmd);

        getLogger().info("HammerUpgrades (addon) actif.");
    }
}