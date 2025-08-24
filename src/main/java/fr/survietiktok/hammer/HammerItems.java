package fr.survietiktok.hammer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public final class HammerItems {

    private final HammerPlugin plugin;
    public final NamespacedKey IS_HAMMER_KEY;

    public HammerItems(HammerPlugin plugin) {
        this.plugin = plugin;
        this.IS_HAMMER_KEY = new NamespacedKey(plugin, "is_hammer");
    }

    public ItemStack createDefaultHammer() {
        ItemStack it = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = it.getItemMeta();
        meta.displayName(Component.text("Hammer").color(NamedTextColor.AQUA));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Casse 3x3 au sol").color(NamedTextColor.GRAY));
        meta.lore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 2, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(IS_HAMMER_KEY, PersistentDataType.BYTE, (byte)1);
        it.setItemMeta(meta);
        return it;
    }

    public boolean isHammer(ItemStack it) {
        if (it == null || it.getType().isAir()) return false;
        ItemMeta meta = it.getItemMeta();
        if (meta == null) return false;
        Byte mark = meta.getPersistentDataContainer().get(IS_HAMMER_KEY, PersistentDataType.BYTE);
        return mark != null && mark == (byte)1;
    }

    public int addDamage(ItemStack item, int amount) {
        if (!(item.getItemMeta() instanceof Damageable)) return 0;
        Damageable d = (Damageable) item.getItemMeta();
        int max = item.getType().getMaxDurability();
        int current = d.getDamage();
        int newDamage = current + amount;
        d.setDamage(newDamage);
        item.setItemMeta(d);
        return newDamage >= max ? 1 : 0; // retourne 1 si cassé
    }
}