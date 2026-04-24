package de.julia.statsrank;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;

public final class StatsStorage {
    private final StatsRankPlugin plugin;
    private final File file;
    private final Map<UUID, PlayerStats> statsByPlayer = new HashMap<>();

    public StatsStorage(StatsRankPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "stats.yml");
    }

    public void load() {
        statsByPlayer.clear();

        if (!file.exists()) {
            return;
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        for (String key : configuration.getKeys(false)) {
            UUID uniqueId;
            try {
                uniqueId = UUID.fromString(key);
            } catch (IllegalArgumentException exception) {
                plugin.getLogger().warning("Ungueltige UUID in stats.yml uebersprungen: " + key);
                continue;
            }

            PlayerStats playerStats = new PlayerStats();
            playerStats.setKills(configuration.getInt(key + ".kills"));
            playerStats.setDeaths(configuration.getInt(key + ".deaths"));
            playerStats.setBrokenBlocks(configuration.getInt(key + ".brokenBlocks"));
            playerStats.setPlacedBlocks(configuration.getInt(key + ".placedBlocks"));
            statsByPlayer.put(uniqueId, playerStats);
        }
    }

    public PlayerStats getOrCreate(UUID uniqueId) {
        return statsByPlayer.computeIfAbsent(uniqueId, ignored -> new PlayerStats());
    }

    public Map<UUID, PlayerStats> getAllStats() {
        return Collections.unmodifiableMap(new HashMap<>(statsByPlayer));
    }

    public void save() {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs()) {
            plugin.getLogger().warning("Plugin-Ordner konnte nicht erstellt werden.");
            return;
        }

        YamlConfiguration configuration = new YamlConfiguration();
        for (Map.Entry<UUID, PlayerStats> entry : statsByPlayer.entrySet()) {
            String path = entry.getKey().toString();
            PlayerStats playerStats = entry.getValue();
            configuration.set(path + ".kills", playerStats.getKills());
            configuration.set(path + ".deaths", playerStats.getDeaths());
            configuration.set(path + ".brokenBlocks", playerStats.getBrokenBlocks());
            configuration.set(path + ".placedBlocks", playerStats.getPlacedBlocks());
        }

        try {
            configuration.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe("stats.yml konnte nicht gespeichert werden: " + exception.getMessage());
        }
    }
}