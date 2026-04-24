package de.julia.statsrank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RankService {
    private final StatsRankPlugin plugin;
    private final List<RankDefinition> ranks = new ArrayList<>();

    public RankService(StatsRankPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        ranks.clear();

        List<?> configuredRanks = plugin.getConfig().getList("ranks");
        if (configuredRanks == null) {
            return;
        }

        for (Object entry : configuredRanks) {
            if (!(entry instanceof java.util.Map<?, ?> map)) {
                continue;
            }

            Object thresholdValue = map.get("threshold");
            Object nameValue = map.get("name");
            if (!(thresholdValue instanceof Number number) || nameValue == null) {
                continue;
            }

            ranks.add(new RankDefinition(number.intValue(), nameValue.toString().replace('&', '§')));
        }

        ranks.sort(Comparator.comparingInt(RankDefinition::threshold));
    }

    public int calculateScore(PlayerStats playerStats) {
        int killPoints = plugin.getConfig().getInt("score.kills", 10) * playerStats.getKills();
        int deathPoints = plugin.getConfig().getInt("score.deaths", -3) * playerStats.getDeaths();
        int blocksPerPoint = Math.max(1, plugin.getConfig().getInt("display.blocks-per-point", 50));
        int brokenPoints = (plugin.getConfig().getInt("score.broken-blocks", 1) * playerStats.getBrokenBlocks()) / blocksPerPoint;
        int placedPoints = (plugin.getConfig().getInt("score.placed-blocks", 1) * playerStats.getPlacedBlocks()) / blocksPerPoint;
        return Math.max(0, killPoints + deathPoints + brokenPoints + placedPoints);
    }

    public RankDefinition getCurrentRank(PlayerStats playerStats) {
        int score = calculateScore(playerStats);
        RankDefinition current = new RankDefinition(0, "§7Neuling");
        for (RankDefinition rank : ranks) {
            if (score >= rank.threshold()) {
                current = rank;
                continue;
            }
            break;
        }
        return current;
    }

    public RankDefinition getNextRank(PlayerStats playerStats) {
        int score = calculateScore(playerStats);
        for (RankDefinition rank : ranks) {
            if (rank.threshold() > score) {
                return rank;
            }
        }
        return null;
    }

    public record RankDefinition(int threshold, String displayName) {
    }
}