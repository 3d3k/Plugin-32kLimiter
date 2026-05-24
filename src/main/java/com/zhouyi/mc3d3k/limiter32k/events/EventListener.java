package com.zhouyi.mc3d3k.limiter32k.events;

import com.zhouyi.mc3d3k.limiter32k.LimiterMain;
import com.zhouyi.mc3d3k.limiter32k.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EventListener implements Listener {
    private final Utils utils = new Utils();
    private final ItemStack AIR = new ItemStack(Material.AIR);

    /**
     * 获取当前启用的检测模块标志数组
     * 顺序必须与 Utils.checkItem(ItemStack, boolean...) 中 varargs 的顺序一致
     */
    private boolean[] getDetectionFlags() {
        return new boolean[]{
                LimiterMain.detectAbnormalNBT,
                LimiterMain.detectAbnormalEnchantment,
                LimiterMain.detectAbnormalAmount,
                LimiterMain.detectUnbreakable,
                LimiterMain.detectIllegalEnchantments,
                LimiterMain.detectExtremeEnchantment,
                LimiterMain.detectHideFlags,
                LimiterMain.detectAbnormalNameLore,
                LimiterMain.detectAbnormalFoodEffects
        };
    }

    /**
     * 判断非 OP 玩家是否持有生成蛋（移除非 OP 生成蛋检测）
     */
    private boolean isNonOpWithSpawnEgg(Player player, ItemStack item) {
        return !player.isOp() && LimiterMain.detectNonOpSpawnEgg && utils.isSpawnEgg(item);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (LimiterMain.isEnabled) {
            if (event.getDamage() > 30D) {
                if (event.getDamager() instanceof Player) {
                    Player player = (Player) event.getDamager();
                    if (utils.checkItem(player.getInventory().getItemInMainHand(), getDetectionFlags()) || isNonOpWithSpawnEgg(player, player.getInventory().getItemInMainHand())) {
                        event.setDamage(40D);
                        player.getInventory().setItemInMainHand(AIR);
                    }
                    if (utils.checkItem(player.getInventory().getItemInOffHand(), getDetectionFlags()) || isNonOpWithSpawnEgg(player, player.getInventory().getItemInOffHand())) {
                        event.setDamage(40D);
                        player.getInventory().setItemInOffHand(AIR);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (LimiterMain.isEnabled) {
            Player player = event.getPlayer();
            boolean mainHandResult = utils.checkItem(event.getMainHandItem(), getDetectionFlags()) || isNonOpWithSpawnEgg(player, event.getMainHandItem());
            boolean offHandResult = utils.checkItem(event.getOffHandItem(), getDetectionFlags()) || isNonOpWithSpawnEgg(player, event.getOffHandItem());
            if (mainHandResult) {
                event.setMainHandItem(AIR);
            }
            if (offHandResult) {
                event.setOffHandItem(AIR);
            }
        }
    }

    @EventHandler
    public void EntityPickupItem(EntityPickupItemEvent event) {
        if (LimiterMain.isEnabled) {
            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();
            ItemStack item = event.getItem().getItemStack();
            if (utils.checkItem(item, getDetectionFlags()) || isNonOpWithSpawnEgg(player, item)) {
                event.setCancelled(true);
                event.getItem().remove();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (LimiterMain.isEnabled) {
            Player player = null;
            if (event.getWhoClicked() instanceof Player) {
                player = (Player) event.getWhoClicked();
            }
            boolean abnormal = utils.checkItem(event.getCurrentItem(), getDetectionFlags())
                    || (player != null && isNonOpWithSpawnEgg(player, event.getCurrentItem()));
            if (abnormal) {
                event.setCurrentItem(AIR);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (LimiterMain.isEnabled) {
            Player player = (event.getPlayer() instanceof Player) ? (Player) event.getPlayer() : null;
            ItemStack[] items = event.getInventory().getStorageContents();
            if (items.length > 0) {
                ArrayList<ItemStack> abnormalItems = new ArrayList<>();
                for (ItemStack item : items) {
                    if (utils.checkItem(item, getDetectionFlags()) || (player != null && isNonOpWithSpawnEgg(player, item))) {
                        if (!abnormalItems.contains(item)) {
                            abnormalItems.add(item);
                        }
                    }
                }
                if (abnormalItems.size() > 0) {
                    for (ItemStack item : abnormalItems) {
                        event.getInventory().remove(item);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (LimiterMain.isEnabled) {
            Player player = (event.getPlayer() instanceof Player) ? (Player) event.getPlayer() : null;
            // 如果关闭的是玩家自己的背包（按E键），event.getInventory() 就是 player.getInventory()
            // 此时只需扫描一次，避免重复判定
            boolean isOwnInventory = event.getInventory().equals(event.getPlayer().getInventory());
            // Player
            ItemStack[] items = event.getPlayer().getInventory().getStorageContents();
            if (items.length > 0) {
                ArrayList<ItemStack> abnormalItems = new ArrayList<>();
                for (ItemStack item : items) {
                    if (utils.checkItem(item, getDetectionFlags()) || (player != null && isNonOpWithSpawnEgg(player, item))) {
                        if (!abnormalItems.contains(item)) {
                            abnormalItems.add(item);
                        }
                    }
                }
                if (abnormalItems.size() > 0) {
                    for (ItemStack item : abnormalItems) {
                        event.getPlayer().getInventory().remove(item);
                    }
                }
            }
            // Inventory（如果不是玩家自己的背包，再扫描事件窗口）
            if (!isOwnInventory) {
                ItemStack[] inventoryContents = event.getInventory().getStorageContents();
                if (inventoryContents.length > 0) {
                    ArrayList<ItemStack> abnormalItems = new ArrayList<>();
                    for (ItemStack item : inventoryContents) {
                        if (utils.checkItem(item, getDetectionFlags()) || (player != null && isNonOpWithSpawnEgg(player, item))) {
                            if (!abnormalItems.contains(item)) {
                                abnormalItems.add(item);
                            }
                        }
                    }
                    if (abnormalItems.size() > 0) {
                        for (ItemStack item : abnormalItems) {
                            event.getInventory().remove(item);
                        }
                    }
                }
            }
        }
    }
}
