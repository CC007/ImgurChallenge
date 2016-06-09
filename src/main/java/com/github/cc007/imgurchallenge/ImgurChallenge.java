package com.github.cc007.imgurchallenge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.CoalType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.Coal;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Flexo013
 */
public class ImgurChallenge extends JavaPlugin implements Listener {

    private ArrayList<Material> materialsToRemove;

    @Override
    public void onEnable() {
        materialsToRemove = new ArrayList<>();

        materialsToRemove.add(Material.WOOD_HOE);
        materialsToRemove.add(Material.STONE_HOE);
        materialsToRemove.add(Material.WOOD_PICKAXE);
        materialsToRemove.add(Material.STONE_PICKAXE);
        materialsToRemove.add(Material.IRON_PICKAXE);
        materialsToRemove.add(Material.GOLD_PICKAXE);
        materialsToRemove.add(Material.DIAMOND_PICKAXE);
        materialsToRemove.add(Material.BUCKET);
        materialsToRemove.add(Material.WATER_BUCKET);
        materialsToRemove.add(Material.LAVA_BUCKET);
        materialsToRemove.add(Material.MILK_BUCKET);

        ItemStack charCoalItemStack = new ItemStack(Material.COAL, 1, (byte) 1);
        List<Recipe> charCoalRecipies = getServer().getRecipesFor(charCoalItemStack);
        
        Iterator<Recipe> recipesIterator = getServer().recipeIterator();
        Recipe recipe;

        while (recipesIterator.hasNext()) {
            recipe = recipesIterator.next();
            if (recipe != null) {
                Material mat = recipe.getResult().getType();
                if (materialsToRemove.contains(mat)) {
                    recipesIterator.remove();
                }
                if (!charCoalRecipies.isEmpty() && charCoalRecipies.contains(recipe)) {
                        recipesIterator.remove();
                    charCoalRecipies.remove(recipe);
                }
            }
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemSpawn(ItemSpawnEvent e) {
        for (int i = 0; i < 6; i++) {
            ItemStack saplingItemStack = new ItemStack(Material.SAPLING, 1, (byte) i);
            if (e.getEntity().getItemStack().isSimilar(saplingItemStack)) {
                e.setCancelled(true);
            }
        }
        ItemStack charCoalItemStack = new ItemStack(Material.COAL, 1, (byte) 1);
        if (e.getEntity().getItemStack().isSimilar(charCoalItemStack)) {
            e.setCancelled(true);
        }
        for (Material materialToRemove : materialsToRemove) {
            ItemStack toolItemStack = new ItemStack(materialToRemove);
            if (e.getEntity().getItemStack().isSimilar(toolItemStack)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        try {
            for (int i = 0; i < 6; i++) {
                ItemStack saplingItemStack = new ItemStack(Material.SAPLING, 1, (byte) i);
                if (e.getCurrentItem().isSimilar(saplingItemStack)) {
                    e.setCancelled(true);
                    e.setCurrentItem(new ItemStack(Material.AIR));
                    ((Player) e.getWhoClicked()).updateInventory();
                }
            }
            ItemStack charCoalItemStack = new ItemStack(Material.COAL, 1, (byte) 1);
            if (e.getCurrentItem().isSimilar(charCoalItemStack)) {
                e.setCancelled(true);
                e.setCurrentItem(new ItemStack(Material.AIR));
                ((Player) e.getWhoClicked()).updateInventory();
            }
            for (Material materialToRemove : materialsToRemove) {
                ItemStack toolItemStack = new ItemStack(materialToRemove);
                if (e.getCurrentItem().isSimilar(toolItemStack)) {
                    e.setCancelled(true);
                    e.setCurrentItem(new ItemStack(Material.AIR));
                    ((Player) e.getWhoClicked()).updateInventory();
                }
            }
        } catch (NullPointerException ex) {
            //to be expected due to a minecraft/bukkit quirk
        }
    }
}
