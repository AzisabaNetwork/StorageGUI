package dev.felnull.storagegui.GUI.Item;

import dev.felnull.Data.InventoryData;
import dev.felnull.bettergui.core.GUIItem;
import dev.felnull.bettergui.core.InventoryGUI;
import dev.felnull.storagegui.GUI.Page.ModularStoragePage;
import dev.felnull.storagegui.Utils.GUIUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ModularStorageItem extends GUIItem {
    InventoryData invData;
    int storageNumber;

    public ModularStorageItem(InventoryGUI gui, InventoryData invData, int storageNumber) {
        super(gui, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        if(invData != null){
            super.itemStack = GUIUtils.getCurrentCapacityGlass(invData);
        }
        this.invData = invData;
        this.storageNumber = storageNumber;
        if(invData == null || invData.displayName == null) {
            this.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Storage&f:") + storageNumber);
        } else {
            this.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Storage&f:") + ChatColor.translateAlternateColorCodes('&', invData.displayName));
        }
        List<Component> lore = new ArrayList<>();
        if(invData != null && invData.userTag != null) {
            for (String tag : invData.userTag) {
                lore.add(Component.text(tag));
            }
            this.setLore(lore);
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if(invData == null){
            //gui.openPage(new BuyNewModularStorage(gui));
            return;
        }
        gui.openPage(new ModularStoragePage(gui, invData, storageNumber));
    }
}
