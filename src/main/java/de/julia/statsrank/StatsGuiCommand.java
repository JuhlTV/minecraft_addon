package de.julia.statsrank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class StatsGuiCommand implements CommandExecutor {
    public static final String GUI_TITLE = "§6Stats GUI";
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    public static final Component GUI_TITLE_COMPONENT = LEGACY_SERIALIZER.deserialize(GUI_TITLE);

    private final StatsStorage statsStorage;
    private final RankService rankService;

    public StatsGuiCommand(StatsStorage statsStorage, RankService rankService) {
        this.statsStorage = statsStorage;
        this.rankService = rankService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler koennen die GUI oeffnen.");
            return true;
        }

        PlayerStats playerStats = statsStorage.getOrCreate(player.getUniqueId());
        int score = rankService.calculateScore(playerStats);
        RankService.RankDefinition currentRank = rankService.getCurrentRank(playerStats);

        Inventory inventory = Bukkit.createInventory(null, 27, GUI_TITLE_COMPONENT);
        inventory.setItem(10, createItem(Material.DIAMOND_SWORD, "§cKills", "§f" + playerStats.getKills()));
        inventory.setItem(11, createItem(Material.SKELETON_SKULL, "§7Deaths", "§f" + playerStats.getDeaths()));
        inventory.setItem(12, createItem(Material.IRON_PICKAXE, "§6Abgebaute Bloecke", "§f" + playerStats.getBrokenBlocks()));
        inventory.setItem(13, createItem(Material.BRICKS, "§eGesetzte Bloecke", "§f" + playerStats.getPlacedBlocks()));
        inventory.setItem(14, createItem(Material.NETHER_STAR, "§bPunkte", "§f" + score));
        inventory.setItem(15, createItem(Material.NAME_TAG, "§dRang", currentRank.displayName()));

        player.openInventory(inventory);
        return true;
    }

    private ItemStack createItem(Material material, String title, String value) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(LEGACY_SERIALIZER.deserialize(title));
        meta.lore(java.util.List.of(LEGACY_SERIALIZER.deserialize("§7Wert: " + value)));
        item.setItemMeta(meta);
        return item;
    }
}