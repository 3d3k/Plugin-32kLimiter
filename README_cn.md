32kLimiter
---
![Version](https://img.shields.io/github/v/release/GuangChen2333/32kLimiter)
![Download](https://img.shields.io/github/downloads/GuangChen2333/32kLimiter/total)
![License](https://img.shields.io/github/license/GuangChen2333/32kLimiter)
![Stars](https://img.shields.io/github/stars/GuangChen2333/32kLimiter)

[English](https://github.com/ZhouyiStudio/32kLimiter/blob/master/README.md) | **简体中文**

一个用于限制32k武器的Spigot插件

受支持的Minecraft版本: 1.12.2

> 注意: 在 2.0.0 及以后的版本中, 本插件将依赖于 `NBT API`
>
> 你可以在 [这里](https://www.spigotmc.org/resources/nbt-api.7939/) 下载本依赖

---

## 功能特性

- **16 个检测模块** — 涵盖异常 NBT、附魔、堆叠数量、不可破坏标签、非法附魔组合、极端附魔等级、隐藏标志、异常名称/Lore、食物效果、刷怪蛋、药水类型、物品模型、地图 ID、药水效果、自定义模型数据等
- **可调检测强度** — 配置灵敏度从 1（宽松）到 10（严格）
- **白名单系统** — 玩家白名单、物品白名单、物品黑名单，持久化存储
- **潜影盒自动展开** — 将潜影盒加入白名单时自动展开其中所有物品
- **清理弹窗提示** — 物品被清理时玩家收到屏幕中央标题 + 聊天消息通知
- **3d3k 物品免疫** — 显示名称包含 "3d3k" 的物品自动跳过检测
- **内存开关** — 运行时启用/禁用插件，无需重启服务器

## 命令

| 命令                               | 权限               | 说明                                              |
|------------------------------------|-------------------|---------------------------------------------------|
| /32klimiter                        | 32klimiter.admin  | 获取帮助                                           |
| /32klimiter reload                 | ~                 | 重新加载配置文件                                     |
| /32klimiter config                 | ~                 | 查看当前配置                                        |
| /32klimiter enable                 | ~                 | 暂时启用该插件                                      |
| /32klimiter disable                | ~                 | 暂时停用该插件                                      |
| /32klimiter status                 | ~                 | 获取该插件的当前状态                                  |
| /32klimiter banitem                | ~                 | 将手中物品加入黑名单（仅玩家）                          |
| /32klimiter banlist                | ~                 | 查看黑名单物品列表                                    |
| /32klimiter whitelist add [玩家]    | ~                 | 添加玩家到白名单（仅控制台）                            |
| /32klimiter whitelist add          | ~                 | 将手中物品加入物品白名单（仅玩家，支持潜影盒自动展开）       |
| /32klimiter whitelist remove [玩家] | ~                 | 从白名单移除玩家                                    |
| /32klimiter whitelist remove       | ~                 | 从物品白名单移除手中物品（仅玩家）                       |
| /32klimiter whitelist list          | ~                 | 列出所有白名单玩家和物品                               |

## 配置文件

```yaml
# 插件总开关
enabled: true

# 检测强度 (1-10)。越高 = 检测越严格
# 1-3: 宽松（仅极端违规），4-7: 正常，8-10: 严格（边界物品也会被检测）
# 默认: 5
detection-intensity: 5

# 检测模块 — 设为 false 可关闭任意检测
detections:
  # 检测异常 NBT 属性修饰（如攻击伤害/生命值过高）
  abnormal-nbt: true

  # 检测异常附魔等级（超过原版最大等级，如锋利6+）
  abnormal-enchantment: true

  # 检测异常堆叠数量（如一组64个以上的物品）
  abnormal-amount: true

  # 检测不可破坏标签（Unbreakable:1，作弊物品常见）
  unbreakable: true

  # 检测非法附魔组合（互斥附魔共存，如精准采集+时运）
  illegal-enchantments: true

  # 检测极端附魔等级（超过 32767，真正的"32k"等级）
  extreme-enchantment: true

  # 检测隐藏属性标志（使用 HideFlags 隐藏物品信息）
  hide-flags: true

  # 检测异常物品名称（过长、过多颜色代码）
  abnormal-name-lore: true

  # 检测食品物品的异常药水效果（超过 3 种效果或效果等级 > 5）
  abnormal-food-effects: true

  # 移除非OP玩家持有的刷怪蛋
  remove-spawn-egg-for-non-op: true

  # 检测无效药水类型（Potion 标签指向不存在的药水）
  invalid-potion-type: true

  # 检测无效物品模型（ItemModel 指向不存在的物品）
  invalid-item-model: true

  # 检测自定义地图 ID（地图 ID 异常过大或负数）
  custom-map-id: true

  # 检测极端药水效果（持续时间 > 600000 tick 或效果等级 > 20）
  extreme-potion-effects: true

  # 检测异常 CustomModelData 值（超出 ±9999）
  custom-model-data: true
```

## 关于

本插件不会彻底封杀32k武器，玩家们可以在漏斗中正常使用32k（类似于2b2t）

如果你喜欢这个项目，请给我们点一个Star或是 [捐助](https://afdian.net/@GuangChen2333) 我们，这将会对我们的开发工作做出极大的贡献

如果你在您自己的服务器中使用了本插件，我很希望你可以在某个地方放上本插件的仓库链接

**本插件永久免费且开源，如果你在某个地方购买了本插件，建议联系退款**
