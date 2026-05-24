package com.zhouyi.mc3d3k.limiter32k;

import com.zhouyi.mc3d3k.limiter32k.commands.LimiterCommand;
import com.zhouyi.mc3d3k.limiter32k.events.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LimiterMain extends JavaPlugin {
    private static LimiterMain INSTANCE;
    public static boolean isEnabled;

    // 各检测模块开关
    public static boolean detectAbnormalNBT;
    public static boolean detectAbnormalEnchantment;
    public static boolean detectAbnormalAmount;
    public static boolean detectUnbreakable;
    public static boolean detectIllegalEnchantments;
    public static boolean detectExtremeEnchantment;
    public static boolean detectHideFlags;
    public static boolean detectAbnormalNameLore;
    public static boolean detectAbnormalFoodEffects;
    public static boolean detectNonOpSpawnEgg;

    // 新增检测模块
    public static boolean detectInvalidPotionType;
    public static boolean detectInvalidItemModel;
    public static boolean detectCustomMapID;
    public static boolean detectExtremePotionEffects;
    public static boolean detectCustomModelData;

    public static LimiterMain getInstance() {
        return INSTANCE;
    }

    private void loadDetectionsConfig() {
        detectAbnormalNBT = getConfig().getBoolean("detections.abnormal-nbt", true);
        detectAbnormalEnchantment = getConfig().getBoolean("detections.abnormal-enchantment", true);
        detectAbnormalAmount = getConfig().getBoolean("detections.abnormal-amount", true);
        detectUnbreakable = getConfig().getBoolean("detections.unbreakable", true);
        detectIllegalEnchantments = getConfig().getBoolean("detections.illegal-enchantments", true);
        detectExtremeEnchantment = getConfig().getBoolean("detections.extreme-enchantment", true);
        detectHideFlags = getConfig().getBoolean("detections.hide-flags", true);
        detectAbnormalNameLore = getConfig().getBoolean("detections.abnormal-name-lore", true);
        detectAbnormalFoodEffects = getConfig().getBoolean("detections.abnormal-food-effects", true);
        detectNonOpSpawnEgg = getConfig().getBoolean("detections.remove-spawn-egg-for-non-op", true);
        detectInvalidPotionType = getConfig().getBoolean("detections.invalid-potion-type", true);
        detectInvalidItemModel = getConfig().getBoolean("detections.invalid-item-model", true);
        detectCustomMapID = getConfig().getBoolean("detections.custom-map-id", true);
        detectExtremePotionEffects = getConfig().getBoolean("detections.extreme-potion-effects", true);
        detectCustomModelData = getConfig().getBoolean("detections.custom-model-data", true);
    }
    @Override
    public void onEnable() {
        INSTANCE = this;
        
        getLogger().info("=========================================");
        getLogger().info("  32kLimiter by Zhouyi");
        getLogger().info("  github.com/ZhouyiStudio/32kLimiter");
        getLogger().info("  QQ: 823672854");
        getLogger().info("=========================================");
        
        saveDefaultConfig();
        getLogger().info("[1/7] 加载配置文件... 完成");
        
        isEnabled = getConfig().getBoolean("enabled");
        loadDetectionsConfig();
        getLogger().info("[2/7] 加载 " + getTotalDetections() + " 个检测模块 (已启用 " + countEnabledDetections() + ")");
        
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        getLogger().info("[3/7] 注册事件监听器... 完成");
        
        if (Bukkit.getPluginCommand("limiter") != null) {
            Bukkit.getPluginCommand("limiter").setExecutor(new LimiterCommand());
            Bukkit.getPluginCommand("limiter").setTabCompleter(new LimiterCommand());
            getLogger().info("[4/7] 注册命令 /limiter /32klimiter... 完成");
        } else {
            getLogger().info("[4/7] 警告: limiter 命令未找到!");
        }
        
        getLogger().info("[5/7] 插件主开关: " + (isEnabled ? "启用" : "禁用"));
        getLogger().info("[6/7] 检测模块状态:");
        printDetectionStatus();
        getLogger().info("[7/7] 32kLimiter 启动完成!");
        getLogger().info("=========================================");
    }

    private void printDetectionStatus() {
        if (detectAbnormalNBT) getLogger().info("  ✔ abnormal-nbt (异常NBT属性)");
        if (detectAbnormalEnchantment) getLogger().info("  ✔ abnormal-enchantment (异常附魔等级)");
        if (detectAbnormalAmount) getLogger().info("  ✔ abnormal-amount (异常堆叠数量)");
        if (detectUnbreakable) getLogger().info("  ✔ unbreakable (不可破坏标签)");
        if (detectIllegalEnchantments) getLogger().info("  ✔ illegal-enchantments (互斥附魔共存)");
        if (detectExtremeEnchantment) getLogger().info("  ✔ extreme-enchantment (极端附魔等级)");
        if (detectHideFlags) getLogger().info("  ✔ hide-flags (隐藏属性标志)");
        if (detectAbnormalNameLore) getLogger().info("  ✔ abnormal-name-lore (异常名称Lore)");
        if (detectAbnormalFoodEffects) getLogger().info("  ✔ abnormal-food-effects (异常食物效果)");
        if (detectNonOpSpawnEgg) getLogger().info("  ✔ remove-spawn-egg-for-non-op (移除非OP刷怪蛋)");
        if (detectInvalidPotionType) getLogger().info("  ✔ invalid-potion-type (无效药水类型)");
        if (detectInvalidItemModel) getLogger().info("  ✔ invalid-item-model (无效物品模型)");
        if (detectCustomMapID) getLogger().info("  ✔ custom-map-id (异常地图ID)");
        if (detectExtremePotionEffects) getLogger().info("  ✔ extreme-potion-effects (极端药水效果)");
        if (detectCustomModelData) getLogger().info("  ✔ custom-model-data (异常模型数据)");
    }

    private int countEnabledDetections() {
        int count = 0;
        if (detectAbnormalNBT) count++;
        if (detectAbnormalEnchantment) count++;
        if (detectAbnormalAmount) count++;
        if (detectUnbreakable) count++;
        if (detectIllegalEnchantments) count++;
        if (detectExtremeEnchantment) count++;
        if (detectHideFlags) count++;
        if (detectAbnormalNameLore) count++;
        if (detectAbnormalFoodEffects) count++;
        if (detectNonOpSpawnEgg) count++;
        if (detectInvalidPotionType) count++;
        if (detectInvalidItemModel) count++;
        if (detectCustomMapID) count++;
        if (detectCustomModelData) count++;
        return count;
    }

    private int getTotalDetections() {
        return 15;
    }

    // NOTE: reload() intentionally NOT annotated with @Override
    // JavaPlugin.reloadConfig() is available; custom reload() handles config sync.
    public void reload() {
        reloadConfig();
        isEnabled = getConfig().getBoolean("enabled");
        loadDetectionsConfig();
    }
}
