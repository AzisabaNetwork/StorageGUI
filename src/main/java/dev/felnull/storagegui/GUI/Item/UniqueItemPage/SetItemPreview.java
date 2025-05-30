package dev.felnull.storagegui.GUI.Item.UniqueItemPage;

import dev.felnull.bettergui.core.GUIItem;
import dev.felnull.bettergui.core.InventoryGUI;
import dev.felnull.storagegui.Data.UniqueItemData;
import dev.felnull.storagegui.Utils.GUIUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SetItemPreview extends GUIItem {
    public SetItemPreview(InventoryGUI gui, UniqueItemData uniqueItemData) {
        super(gui, new ItemStack(uniqueItemData.material));
        ItemMeta meta = itemStack.getItemMeta();
        meta.lore(GUIUtils.splitLoreFactory(uniqueItemData.lore));
        meta.setCustomModelData(uniqueItemData.cmdNumber);
        meta.displayName(PlainComponentSerializer.plain().deserialize(uniqueItemData.displayName));
        if(uniqueItemData.doEnchant){
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        }else {
            meta.removeEnchant(Enchantment.DURABILITY);
        }
        itemStack.setItemMeta(meta);
    }
}
