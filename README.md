# Immersive Miner

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](LICENSE)

> 一个为 Minecraft 打造的沉浸式连锁挖矿模组

## 简介

Immersive Miner 是一个连锁挖矿模组，让你能够快速挖掘相连的同种方块，大幅提升采矿效率。

## 功能特性

- **连锁挖矿**: 按住 `~` 键激活连锁模式，破坏一个方块时自动挖掘所有相连的同种方块
- **Jade 联动**: 安装 Jade 模组后，HUD 会实时显示连锁方块数量
- **多人游戏支持**: 完善的客户端-服务端同步，支持多人游戏
- **性能保护**: 单次连锁最多 256 个方块，避免卡顿

## 安装要求

- **Minecraft**: 1.21.1
- **Java**: 21 或更高版本
- **可选依赖**:
  - [Jade](https://modrinth.com/mod/jade) - HUD 信息显示联动

## 使用方法

1. 将模组 jar 文件放入 `mods` 文件夹
2. 启动游戏
3. 按住 `~` 键（键盘左上角的波浪键）
4. 破坏方块，所有相连的同种方块将被同时挖掘
5. 松开按键退出连锁模式

## 版本历史

详见 [CHANGELOG.md](CHANGELOG.md)

### 当前版本: 0.1.1-beta

**0.1.1-beta**
- 修复了客户端与服务端同步问题

**0.1.0-beta**
- 初始版本发布
- 支持连锁挖矿
- 支持 Jade 联动

## 许可证

Apache 2.0

## 作者

sxtkl
