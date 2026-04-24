package de.julia.statsrank;

import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class StatsRankPlugin extends JavaPlugin {
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacySection();
    private StatsStorage statsStorage;
    private RankService rankService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        statsStorage = new StatsStorage(this);
        statsStorage.load();

        rankService = new RankService(this);

        StatsCommand statsCommand = new StatsCommand(statsStorage, rankService);
        Objects.requireNonNull(getCommand("stats"), "stats command missing in plugin.yml").setExecutor(statsCommand);
        Objects.requireNonNull(getCommand("stats"), "stats command missing in plugin.yml").setTabCompleter(statsCommand);

        getServer().getPluginManager().registerEvents(new StatsListener(this, statsStorage), this);
        getServer().getScheduler().runTaskTimer(this, statsStorage::save, 6000L, 6000L);

        for (Player player : getServer().getOnlinePlayers()) {
            refreshPlayerRank(player);
        }

        getLogger().info("StatsRank wurde aktiviert.");
    }

    public void refreshPlayerRank(Player player) {
        PlayerStats playerStats = statsStorage.getOrCreate(player.getUniqueId());
        String rankedName = rankService.getCurrentRank(playerStats).displayName() + " §8| §f" + player.getName();
        Component displayComponent = legacySerializer.deserialize(rankedName);
        player.playerListName(displayComponent);
        player.displayName(displayComponent);
    }

    @Override
    public void onDisable() {
        if (statsStorage != null) {
            statsStorage.save();
        }
    }
}