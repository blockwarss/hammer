package fr.survietiktok.hammeraddon;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public final class UpgradeItems {

    private final HammerUpgradesPlugin plugin;
    public final NamespacedKey UPGRADE_TYPE_KEY;
    public final NamespacedKey IS_UPGRADE_KEY;

    public UpgradeItems(HammerUpgradesPlugin plugin) {
        this.plugin = plugin;
        this.UPGRADE_TYPE_KEY = new NamespacedKey(plugin, "upgrade_type");
        this.IS_UPGRADE_KEY   = new NamespacedKey(plugin, "is_upgrade");
    }

    public ItemStack create(UpgradeType type) {
        ItemStack it = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta meta = it.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Hammer Upgrade " + type.display());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Fusion: main = Hammer, offhand = Upgrade");
        lore.add(ChatColor.DARK_GRAY + "Sneak + clic droit pour fusionner");
        lore.add(ChatColor.YELLOW + "Rareté: " + type.display());
        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(IS_UPGRADE_KEY, PersistentDataType.BYTE, (byte)1);
        pdc.set(UPGRADE_TYPE_KEY, PersistentDataType.STRING, type.name());
        it.setItemMeta(meta);
        return it;
    }

    public boolean isUpgrade(ItemStack it) {
        if (it == null || it.getType().isAir()) return false;
        ItemMeta meta = it.getItemMeta();
        if (meta == null) return false;
        Byte mark = meta.getPersistentDataContainer().get(IS_UPGRADE_KEY, PersistentDataType.BYTE);
        return mark != null && mark == (byte)1;
    }

    public UpgradeType which(ItemStack it) {
        if (!isUpgrade(it)) return null;
        ItemMeta meta = it.getItemMeta();
        String s = meta.getPersistentDataContainer().get(UPGRADE_TYPE_KEY, PersistentDataType.STRING);
        if (s == null) return null;
        try {
            return UpgradeType.valueOf(s);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}