package de.julia.statsrank;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.chat.ChatRenderer;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ChatPrefixListener implements Listener {
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacySection();
    private final StatsStorage statsStorage;
    private final RankService rankService;

    public ChatPrefixListener(StatsRankPlugin plugin, StatsStorage statsStorage, RankService rankService) {
        this.statsStorage = statsStorage;
        this.rankService = rankService;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.renderer(ChatRenderer.viewerUnaware((Player source, Component sourceDisplayName, Component message) -> {
            UUID uniqueId = source.getUniqueId();
            PlayerStats playerStats = statsStorage.getOrCreate(uniqueId);
            RankService.RankDefinition rank = rankService.getCurrentRank(playerStats);
            Component prefix = legacySerializer.deserialize(rank.displayName() + " §8| ");
            return Component.empty()
                .append(prefix)
                .append(sourceDisplayName)
                .append(Component.text("§7: "))
                .append(message);
        }));
    }
}