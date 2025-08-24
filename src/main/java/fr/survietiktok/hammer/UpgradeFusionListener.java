package fr.survietiktok.hammer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public final class UpgradeFusionListener implements Listener {

    private final HammerPlugin plugin;
    private final UpgradeItems upgrades;

    public final NamespacedKey UPGRADE_APPLIED_KEY;
    public final NamespacedKey UPGRADE_TYPE_KEY;

    public UpgradeFusionListener(HammerPlugin plugin, UpgradeItems upgrades) {
        this.plugin = plugin;
        this.upgrades = upgrades;
        this.UPGRADE_APPLIED_KEY = new NamespacedKey(plugin, "hammer_upgrade_applied");
        this.UPGRADE_TYPE_KEY    = new NamespacedKey(plugin, "hammer_upgrade_type");
    }

    @EventHandler(ignoreCancelled = true)
    public void onSneakRightClick(PlayerInteractEvent e) {
        Action a = e.getAction();
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return;

        Player p = e.getPlayer();
        if (!p.isSneaking()) return;

        ItemStack main = p.getInventory().getItemInMainHand();
        ItemStack off  = p.getInventory().getItemInOffHand();

        if (main == null || main.getType().isAir()) return;
        if (!plugin.isHammer(main)) return;
        if (!upgrades.isUpgrade(off)) return;

        ItemMeta meta = main.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (pdc.has(UPGRADE_APPLIED_KEY, PersistentDataType.BYTE)) {
            p.sendMessage(Component.text("Ce Hammer a déjà reçu un Upgrade.").color(NamedTextColor.RED));
            return;
        }

        UpgradeType type = upgrades.which(off);
        if (type == null) return;

        if (type.convertToDiamond()) {
            main.setType(Material.DIAMOND_PICKAXE);
        }

        if (meta.hasEnchant(Enchantment.DIG_SPEED)) {
            meta.removeEnchant(Enchantment.DIG_SPEED);
        }
        meta.addEnchant(Enchantment.DIG_SPEED, type.efficiencyLevel(), true);

        List<net.kyori.adventure.text.Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(Component.text("Rareté: " + type.display()).color(NamedTextColor.GOLD));
        meta.lore(lore);

        pdc.set(UPGRADE_APPLIED_KEY, PersistentDataType.BYTE, (byte)1);
        pdc.set(UPGRADE_TYPE_KEY, PersistentDataType.STRING, type.name());

        main.setItemMeta(meta);

        int amt = off.getAmount();
        if (amt <= 1) p.getInventory().setItemInOffHand(null);
        else off.setAmount(amt - 1);

        p.sendMessage(Component.text("Fusion réussie: ").color(NamedTextColor.GREEN)
                .append(Component.text("Hammer Upgrade " + type.display()).color(NamedTextColor.AQUA)));

        e.setCancelled(true);
    }
}