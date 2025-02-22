package me.aunique.captureTheFlag.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GameRestrictions implements Listener {
    @EventHandler
    public void onArmorChange(InventoryClickEvent inventoryClickEvent) throws InterruptedException {
        if (inventoryClickEvent.getSlotType().equals(InventoryType.SlotType.ARMOR)){
            inventoryClickEvent.setCancelled(true);

            //Thread.sleep(50);
            inventoryClickEvent.getWhoClicked().setItemOnCursor(null);

        }
    }
    @EventHandler
    public void onHungerDepletion(FoodLevelChangeEvent depletionEvent){
        depletionEvent.setCancelled(true);
    }
    @EventHandler
    public void onFallDamage(EntityDamageEvent damageEvent){
        if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL){
            damageEvent.setCancelled(true);
        }
    }
}
