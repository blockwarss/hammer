package fr.survietiktok.hammeraddon;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HammerUpgradesPlugin extends JavaPlugin {

    private HammerHandle handle;
    private UpgradeItems upgradeItems;

    @Override
    public void onEnable() {
        getLogger().info("Chargement HammerUpgrades v" + getDescription().getVersion());

        Plugin hammer = Bukkit.getPluginManager().getPlugin("Hammer");
        if (hammer == null || !hammer.isEnabled()) {
            getLogger().severe("[FATAL] Le plugin Hammer n'est pas chargé. (softdepend) → désactivation.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Plugin Hammer détecté: " + hammer.getDescription().getFullName());

        this.handle = new HammerHandle(hammer);
        if (!handle.isReady()) {
            getLogger().severe("[FATAL] Impossible d'accéder à HammerPlugin#isHammer(ItemStack). Vérifie ta version de Hammer.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.upgradeItems = new UpgradeItems(this);

        getServer().getPluginManager().registerEvents(new UpgradeFusionListener(this, handle, upgradeItems), this);

        PluginCommand c = getCommand("hammerupgrade");
        if (c == null) {
            getLogger().severe("[FATAL] Commande hammerupgrade introuvable (plugin.yml).");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        HammerUpgradeCommand cmd = new HammerUpgradeCommand(this, upgradeItems);
        c.setExecutor(cmd);
        c.setTabCompleter(cmd);

        getLogger().info("HammerUpgrades (addon) actif.");
    }
}