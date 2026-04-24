package de.julia.statsrank;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class TopStatsCommand implements CommandExecutor {
    private final StatsStorage statsStorage;
    private final RankService rankService;

    public TopStatsCommand(StatsStorage statsStorage, RankService rankService) {
        this.statsStorage = statsStorage;
        this.rankService = rankService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<Map.Entry<UUID, PlayerStats>> topEntries = statsStorage.getAllStats().entrySet().stream()
            .sorted(Comparator.comparingInt((Map.Entry<UUID, PlayerStats> entry) -> rankService.calculateScore(entry.getValue())).reversed())
            .limit(10)
            .toList();

        sender.sendMessage("§8§m----------------------------------------");
        sender.sendMessage("§6Top 10 Spieler nach Punkten");

        if (topEntries.isEmpty()) {
            sender.sendMessage("§7Noch keine Stats vorhanden.");
            sender.sendMessage("§8§m----------------------------------------");
            return true;
        }

        int rankIndex = 1;
        for (Map.Entry<UUID, PlayerStats> entry : topEntries) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
            String name = offlinePlayer.getName() != null ? offlinePlayer.getName() : entry.getKey().toString().substring(0, 8);
            int score = rankService.calculateScore(entry.getValue());
            RankService.RankDefinition rank = rankService.getCurrentRank(entry.getValue());
            sender.sendMessage("§e" + rankIndex + ". §f" + name + " §8- §7" + score + " Punkte §8(" + rank.displayName() + "§8)");
            rankIndex++;
        }

        sender.sendMessage("§8§m----------------------------------------");
        return true;
    }
}