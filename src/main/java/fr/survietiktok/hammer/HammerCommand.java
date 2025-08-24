package fr.survietiktok.hammer;

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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class HammerCommand implements CommandExecutor, TabCompleter {

    private final HammerPlugin plugin;
    private final HammerItems items;

    public HammerCommand(HammerPlugin plugin, HammerItems items) {
        this.plugin = plugin;
        this.items = items;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("hammer.give")) {
            sender.sendMessage(Component.text("Tu n'as pas la permission.").color(NamedTextColor.RED));
            return true;
        }

        Player target;
        if (args.length >= 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Joueur introuvable.").color(NamedTextColor.RED));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Usage: /hammer give <joueur>").color(NamedTextColor.YELLOW));
                return true;
            }
            target = (Player) sender;
        }

        target.getInventory().addItem(items.createDefaultHammer());
        sender.sendMessage(Component.text("Hammer donné à ").color(NamedTextColor.GREEN)
                .append(Component.text(target.getName()).color(NamedTextColor.AQUA)));
        if (sender != target) {
            target.sendMessage(Component.text("Tu as reçu un Hammer.").color(NamedTextColor.GREEN));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) names.add(p.getName());
            return names.stream().filter(n -> n.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))).collect(Collectors.toList());
        }
        return List.of();
    }
}