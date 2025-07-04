package dev.felnull.storagegui.GUI.Item.BuyModularStorage;

import dev.felnull.BetterStorage;
import dev.felnull.Data.GroupPermENUM;
import dev.felnull.Data.InventoryData;
import dev.felnull.Data.StorageData;
import dev.felnull.DataIO.DataIO;
import dev.felnull.DataIO.DatabaseManager;
import dev.felnull.bettergui.core.GUIItem;
import dev.felnull.bettergui.core.InventoryGUI;
import dev.felnull.storagegui.GUI.Page.MainStoragePage;
import dev.felnull.storagegui.StorageGUI;
import dev.felnull.storagegui.Utils.InvUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BuyModularStorageItem extends GUIItem {
    public StorageData storageData;
    public int inventoryNumber;

    public BuyModularStorageItem(InventoryGUI gui, StorageData storageData, int inventoryNumber) {
        super(gui, new ItemStack(Material.CHEST));
        this.storageData = storageData;
        this.inventoryNumber = inventoryNumber;
        setDisplayName(String.valueOf(inventoryNumber * 50) + "$でストレージを購入する!");
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Economy economy = StorageGUI.economy;
        OfflinePlayer player = (OfflinePlayer) e.getWhoClicked();

        int rowCount = 6; // 固定行数
        int pricePerRow = 50;
        int basePrice = inventoryNumber * rowCount * pricePerRow;

        // 0〜2番のストレージは無料
        boolean isFree = inventoryNumber <= 2;
        int finalPrice = isFree ? 0 : basePrice;

        if (!isFree && !economy.has(player, finalPrice)) {
            double lack = finalPrice - economy.getBalance(player);
            player.getPlayer().sendMessage("お金が " + lack + "$ 足りません! 現在: " + economy.getBalance(player) + "$");
            return;
        }

        Set<String> perm = new HashSet<>();
        perm.add(GroupPermENUM.MEMBER.getPermName());

        InventoryData invData = new InventoryData(null, rowCount, perm, new HashMap<>());
        storageData.storageInventory.put(String.valueOf(inventoryNumber), invData);

        if (!isFree) {
            economy.withdrawPlayer(player, finalPrice);
        }

        InvUtil.saveWithLog(storageData, inventoryNumber);
        gui.openPage(new MainStoragePage(gui, storageData));
    }
}
