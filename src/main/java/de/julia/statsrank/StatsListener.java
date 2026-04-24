package de.julia.statsrank;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class StatsListener implements Listener {
    private final StatsRankPlugin plugin;
    private final StatsStorage statsStorage;

    public StatsListener(StatsRankPlugin plugin, StatsStorage statsStorage) {
        this.plugin = plugin;
        this.statsStorage = statsStorage;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        statsStorage.getOrCreate(player.getUniqueId());
        plugin.refreshPlayerRank(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        statsStorage.getOrCreate(player.getUniqueId()).addDeath();
        plugin.refreshPlayerRank(player);

        Player killer = player.getKiller();
        if (killer != null) {
            statsStorage.getOrCreate(killer.getUniqueId()).addKill();
            plugin.refreshPlayerRank(killer);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        statsStorage.getOrCreate(player.getUniqueId()).addBrokenBlock();
        plugin.refreshPlayerRank(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        statsStorage.getOrCreate(player.getUniqueId()).addPlacedBlock();
        plugin.refreshPlayerRank(player);
    }
}