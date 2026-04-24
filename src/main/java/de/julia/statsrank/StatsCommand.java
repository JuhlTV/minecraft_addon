package de.julia.statsrank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class StatsCommand implements CommandExecutor, TabCompleter {
    private final StatsStorage statsStorage;
    private final RankService rankService;

    public StatsCommand(StatsStorage statsStorage, RankService rankService) {
        this.statsStorage = statsStorage;
        this.rankService = rankService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        OfflinePlayer target;

        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cBitte nutze /stats <spieler> aus der Konsole.");
                return true;
            }
            target = player;
        } else {
            target = Bukkit.getOfflinePlayer(args[0]);
            if (target.getName() == null && !target.hasPlayedBefore()) {
                sender.sendMessage("§cDieser Spieler wurde nicht gefunden.");
                return true;
            }
        }

        UUID uniqueId = target.getUniqueId();
        PlayerStats playerStats = statsStorage.getOrCreate(uniqueId);
        int score = rankService.calculateScore(playerStats);
        RankService.RankDefinition currentRank = rankService.getCurrentRank(playerStats);
        RankService.RankDefinition nextRank = rankService.getNextRank(playerStats);
        long playTicks = target.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long playHours = playTicks / 20L / 3600L;
        long playMinutes = (playTicks / 20L / 60L) % 60L;

        sender.sendMessage("§8§m----------------------------------------");
        sender.sendMessage("§6Stats von §e" + Objects.requireNonNullElse(target.getName(), "Unbekannt"));
        sender.sendMessage("§7Kills: §f" + playerStats.getKills());
        sender.sendMessage("§7Deaths: §f" + playerStats.getDeaths());
        sender.sendMessage("§7Abgebaute Bloecke: §f" + playerStats.getBrokenBlocks());
        sender.sendMessage("§7Gesetzte Bloecke: §f" + playerStats.getPlacedBlocks());
        sender.sendMessage("§7Spielzeit: §f" + playHours + "h " + playMinutes + "m");
        sender.sendMessage("§7Punkte: §f" + score);
        sender.sendMessage("§7Rang: " + currentRank.displayName());
        if (nextRank != null) {
            sender.sendMessage("§7Naechster Rang: " + nextRank.displayName() + " §8(noch " + (nextRank.threshold() - score) + " Punkte)");
        } else {
            sender.sendMessage("§7Naechster Rang: §6Maximaler Rang erreicht");
        }
        sender.sendMessage("§8§m----------------------------------------");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) {
            return List.of();
        }

        String search = args[0].toLowerCase();
        List<String> completions = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String name = onlinePlayer.getName();
            if (name.toLowerCase().startsWith(search)) {
                completions.add(name);
            }
        }
        return completions;
    }
}