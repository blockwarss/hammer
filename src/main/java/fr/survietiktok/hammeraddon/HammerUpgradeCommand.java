package fr.survietiktok.hammeraddon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("hammer.upgrade.give")) {
            sender.sendMessage(Component.text("Tu n'as pas la permission.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(Component.text("Usage: /hammerupgrade <basic|rare|epic> [joueur]").color(NamedTextColor.YELLOW));
            return true;
        }

        UpgradeType type = UpgradeType.fromString(args[0]);
        if (type == null) {
            sender.sendMessage(Component.text("Type inconnu. Utilise: basic, rare, epic").color(NamedTextColor.RED));
            return true;
        }

        Player target;
        if (args.length == 2) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Joueur introuvable.").color(NamedTextColor.RED));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Précise un joueur: /hammerupgrade <type> <joueur>").color(NamedTextColor.RED));
                return true;
            }
            target = (Player) sender;
        }

        target.getInventory().addItem(upgrades.create(type));
        sender.sendMessage(Component.text("Upgrade envoyé à ").color(NamedTextColor.GREEN)
                .append(Component.text(target.getName()).color(NamedTextColor.AQUA))
                .append(Component.text(" : " + type.display()).color(NamedTextColor.GOLD)));

        if (sender != target) {
            target.sendMessage(Component.text("Tu as reçu un Hammer Upgrade ").color(NamedTextColor.GREEN)
                    .append(Component.text(type.display()).color(NamedTextColor.GOLD)));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
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