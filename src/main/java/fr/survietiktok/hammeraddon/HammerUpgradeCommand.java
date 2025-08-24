package fr.survietiktok.hammeraddon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class HammerUpgradeCommand implements CommandExecutor, TabCompleter {

    private final HammerUpgradesPlugin plugin;
    private final UpgradeItems upgrades;

    public HammerUpgradeCommand(HammerUpgradesPlugin plugin, UpgradeItems upgrades) {
        this.plugin = plugin;
        this.upgrades = upgrades;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hammer.upgrade.give")) {
            sender.sendMessage(ChatColor.RED + "Tu n'as pas la permission.");
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /hammerupgrade <basic|rare|epic> [joueur]");
            return true;
        }

        UpgradeType type = UpgradeType.fromString(args[0]);
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Type inconnu. Utilise: basic, rare, epic");
            return true;
        }

        Player target;
        if (args.length == 2) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Précise un joueur: /hammerupgrade <type> <joueur>");
                return true;
            }
            target = (Player) sender;
        }

        target.getInventory().addItem(upgrades.create(type));
        sender.sendMessage(ChatColor.GREEN + "Upgrade envoyé à " + ChatColor.AQUA + target.getName()
                + ChatColor.GOLD + " : " + type.styled());

        if (sender != target) {
            target.sendMessage(ChatColor.GREEN + "Tu as reçu un Hammer Upgrade " + ChatColor.GOLD + type.styled());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("basic","rare","epic").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) names.add(p.getName());
            return names.stream()
                    .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}