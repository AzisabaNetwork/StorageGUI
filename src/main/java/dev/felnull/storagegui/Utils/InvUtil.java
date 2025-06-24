package dev.felnull.storagegui.Utils;

import dev.felnull.Data.GroupData;
import dev.felnull.Data.InventoryData;
import dev.felnull.Data.StorageData;
import dev.felnull.DataIO.DataIO;
import dev.felnull.bettergui.core.GUIPage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvUtil {

    /**
     * インベントリ内容を InventoryData に反映する
     */
    public static void applyInventoryToItemSlot(Inventory inv, InventoryData inventoryData) {
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);
            if (item == null || item.getType() == Material.AIR) {
                inventoryData.itemStackSlot.remove(slot);
            } else {
                inventoryData.itemStackSlot.put(slot, item.clone());
            }
        }
    }

    /**
     * プレイヤーのインベントリとカーソルをロールバックする
     */
    public static void rollbackPlayerInventory(Player player, ItemStack[] rollbackInv, ItemStack cursorItem) {
        player.getInventory().setContents(rollbackInv);
        player.setItemOnCursor(cursorItem);
    }

    /**
     * 保存処理 + ロールバック処理 + 成功ログ出力付き
     */
    public static boolean saveWithRollback(GUIPage page, StorageData storageData, InventoryData invData, int inventoryNumber, ItemStack[] rollbackInv, ItemStack cursorItem) {
        String pageId = String.valueOf(inventoryNumber);
        GroupData group = storageData.groupData;

        // 🔧 インベントリ内容を InventoryData に反映
        applyInventoryToItemSlot(page.getInventory(), invData);

        // 最新データを保持
        storageData.storageInventory.put(pageId, invData);

        // 保存処理
        if (!DataIO.saveInventoryOnly(group, storageData, pageId)) {
            rollbackPlayerInventory(page.gui.player, rollbackInv, cursorItem);
            page.gui.player.sendMessage(GUIUtils.c("&4アイテム更新が競合したため更新前にロールバックしました"));
            return false;
        }

        Bukkit.getLogger().info("[StorageGUI][Save] " + page.gui.player.getName() + "のストレージ" + inventoryNumber + "を保存しました");
        return true;
    }

    /**
     * InventoryDataを保存し、成功時にログを出力する（ロールバックなし）
     */
    public static boolean saveWithLog(StorageData storageData, int inventoryNumber) {
        String pageId = String.valueOf(inventoryNumber);
        GroupData group = storageData.groupData;

        if (!DataIO.saveInventoryOnly(group, storageData, pageId)) {
            return false;
        }

        Bukkit.getLogger().info("[StorageGUI][Save][" + group.groupName + "] に " + group.groupName + " の " + inventoryNumber + " 番インベントリを保存しました");
        return true;
    }
}
