# Rotborn - Minecraft 26.1 NeoForge

一个最小可运行思路的“食物腐烂”模组工程。

## 功能

- 服务端每秒扫描玩家背包。
- 所有带 `minecraft:food` 数据组件的物品都会被标记腐烂时间。
- 默认 20 分钟现实时间后变成 `rotborn:rotten_food`。
- `data/rotborn/tags/item/never_rots.json` 中的物品不会腐烂。

## 环境

- Minecraft 26.1.x
- NeoForge 26.1.x
- Java 25
- Gradle 9.1+

NeoForge 26.1 文档要求 Java 25，并且官方 26.1 说明中提到资源包格式为 84、数据包格式为 101.1。若你使用的 NeoForge 构建比 `26.1.0.1-beta` 更新，可以改 `gradle.properties` 里的 `neoforge_version`。

## 运行

```bash
./gradlew runClient
```

构建：

```bash
./gradlew build
```

输出 jar 在 `build/libs/`。

## 后续建议

当前版本故意简单：只处理玩家背包，不处理箱子、地面掉落物、潜影盒内部、流体/温度/季节影响。下一步可以把 `DEFAULT_ROT_TICKS` 改成配置，并加 datapack 规则：不同食物有不同保质期。
