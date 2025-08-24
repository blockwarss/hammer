package fr.survietiktok.hammer;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class HammerListener implements Listener {

    private final HammerPlugin plugin;
    private final HammerItems items;
    private final Random random = new Random();

    public HammerListener(HammerPlugin plugin, HammerItems items) {
        this.plugin = plugin;
        this.items = items;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack tool = p.getInventory().getItemInMainHand();
        if (!items.isHammer(tool)) return;
        if (p.getGameMode() == GameMode.CREATIVE) return;

        Block center = e.getBlock();

        // Ne casse que les blocs mineables à la pioche
        if (!isPickaxeMineable(center.getType())) return;

        List<Block> toBreak = getPlane3x3(center);
        // on retire le bloc central (déjà géré par l'évènement actuel)
        toBreak.remove(center);

        int unbreaking = tool.getEnchantmentLevel(Enchantment.DURABILITY);
        int extraBroken = 0;

        PluginManager pm = plugin.getServer().getPluginManager();
        for (Block b : toBreak) {
            if (!isPickaxeMineable(b.getType())) continue;
            // délencher un faux BlockBreakEvent pour respect protections
            BlockBreakEvent bb = new BlockBreakEvent(b, p);
            pm.callEvent(bb);
            if (bb.isCancelled()) continue;

            // casse avec drops en respectant Fortune/Silk
            b.breakNaturally(tool, true);
            extraBroken++;

            // gérer durabilité (simulation Unbreaking)
            if (p.getGameMode() != GameMode.CREATIVE) {
                boolean consume = shouldConsumeDurability(unbreaking);
                if (consume) {
                    int broke = items.addDamage(tool, 1);
                    if (broke == 1) {
                        p.getInventory().setItemInMainHand(null);
                        p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                        break;
                    }
                }
            }
        }
    }

    private boolean shouldConsumeDurability(int unbreakingLevel) {
        if (unbreakingLevel <= 0) return true;
        // probabilité 1/(level+1) de consommer
        return random.nextInt(unbreakingLevel + 1) == 0;
    }

    private boolean isPickaxeMineable(Material m) {
        return Tag.MINEABLE_PICKAXE.isTagged(m);
    }

    private List<Block> getPlane3x3(Block center) {
        List<Block> list = new ArrayList<>(9);
        int y = center.getY();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                list.add(center.getWorld().getBlockAt(center.getX() + dx, y, center.getZ() + dz));
            }
        }
        return list;
    }
}