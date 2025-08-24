package fr.survietiktok.hammeraddon;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class HammerUpgradesPlugin extends JavaPlugin {

    private HammerHandle handle;
    private UpgradeItems upgradeItems;

    @Override
    public void onEnable() {
        getLogger().info("Chargement HammerUpgrades v" + getDescription().getVersion());

        PluginCommand c = getCommand("hammerupgrade");
        if (c != null) {
            HammerUpgradeCommand cmd = new HammerUpgradeCommand(this, getUpgradeItems());
            c.setExecutor(cmd);
            c.setTabCompleter(cmd);
        } else {
            getLogger().severe("[FATAL] Commande hammerupgrade introuvable (plugin.yml).");
        }

        Plugin hammer = Bukkit.getPluginManager().getPlugin("Hammer");
        this.handle = new HammerHandle();
        if (hammer != null && hammer.isEnabled()) {
            boolean ok = this.handle.tryAttach(hammer);
            getLogger().info(ok ? "Plugin Hammer détecté: " + hammer.getDescription().getFullName()
                                : "Hammer détecté mais signature inconnue (pas de isHammer(ItemStack)).");
        } else {
            getLogger().warning("Plugin Hammer introuvable à l'activation, tentative de scan...");
            scanForHammer();
        }

        new BukkitRunnable() { @Override public void run() { scanForHammer(); } }.runTaskLater(this, 20L);
        getServer().getPluginManager().registerEvents(new UpgradeFusionListener(this, this.handle, getUpgradeItems()), this);
        getLogger().info("HammerUpgrades actif.");
    }

    private void scanForHammer() {
        PluginManager pm = Bukkit.getPluginManager();
        for (Plugin p : pm.getPlugins()) {
            if (p == null || !p.isEnabled()) continue;
            if (this.handle.isReady()) break;
            if (this.handle.tryAttach(p)) {
                getLogger().info("Détection par scan: " + p.getDescription().getFullName());
                break;
            }
        }
        if (!this.handle.isReady()) {
            getLogger().warning("Aucun plugin avec méthode isHammer(ItemStack) trouvé pour le moment.");
        }
    }

    private UpgradeItems getUpgradeItems() {
        if (this.upgradeItems == null) this.upgradeItems = new UpgradeItems(this);
        return this.upgradeItems;
    }
}