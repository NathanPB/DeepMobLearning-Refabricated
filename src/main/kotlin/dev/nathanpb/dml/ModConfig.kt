/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Refabricated.
 *
 * Deep Mob Learning: Refabricated is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deep Mob Learning: Refabricated is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Refabricated.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.nathanpb.dml

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer
import kotlin.math.max
import kotlin.math.min


fun registerConfigs() {
    AutoConfig.register(ModConfig::class.java, ::GsonConfigSerializer)
    config = AutoConfig.getConfigHolder(ModConfig::class.java).config
}

@Config(name = MOD_ID)
class ModConfig : ConfigData {

    @ConfigEntry.Category("trial")
    @ConfigEntry.Gui.TransitiveObject
    var trial = Trial()

    @ConfigEntry.Category("loot_fabricator")
    @ConfigEntry.Gui.TransitiveObject
    var lootFabricator = LootFabricator()

    @ConfigEntry.Category("data_model")
    @ConfigEntry.Gui.TransitiveObject
    var dataModel = DataModel()

    @ConfigEntry.Category("system_glitch")
    @ConfigEntry.Gui.TransitiveObject
    var systemGlitch = SystemGlitch()

    @ConfigEntry.Category("data_collection")
    @ConfigEntry.Gui.TransitiveObject
    var dataCollection = DataCollection()


    @ConfigEntry.Category("affix")
    @ConfigEntry.Gui.TransitiveObject
    var affix = TrialAffix()

    @ConfigEntry.Category("matter_condenser")
    @ConfigEntry.Gui.TransitiveObject
    var matterCondenser = MatterCondenser()

    @ConfigEntry.Category("glitch_armor")
    @ConfigEntry.Gui.TransitiveObject
    var glitchArmor = GlitchArmor()

    override fun validatePostLoad() {
        trial.validatePostLoad()
        lootFabricator.validatePostLoad()
        dataModel.validatePostLoad()
        affix.validatePostLoad()
        matterCondenser.validatePostLoad()
        glitchArmor.validatePostLoad()
    }

}

@Config(name = "trial")
class Trial : ConfigData {
    var maxMobsInArena = 8
    var postEndTimeout = 60
    var arenaRadius = 12
    var warmupTime = 60
    var maxTime = 24000

    var allowStartInWrongTerrain = false
    var allowPlayersLeavingArena = false
    var allowMobsLeavingArena = false
    var buildGriefPrevention = true
    var interactGriefPrevention = true
    var explosionGriefPrevention = true

    var trialKeyConsume = true
    var trialKeyReturnIfSucceed = true

    override fun validatePostLoad() {
        maxTime = max(0, maxTime)
        warmupTime = max(maxTime, warmupTime)
        if (maxMobsInArena < 0) {
            maxMobsInArena = 0
        }

        if (postEndTimeout < 0) {
            postEndTimeout = 0
        }

        if (arenaRadius < 1) {
            arenaRadius = 1
        }
    }
}

@Config(name = "affix")
class TrialAffix : ConfigData {
    var maxAffixesInKey = 3
    var enableMobStrength = true
    var enableMobSpeed = true
    var enableMobResistance = true
    var enableThunderstorm = true
    var enablePartyPoison = true

    var thunderstormBoltChance = .05F
    var partyPoisonChance = .005F

    override fun validatePostLoad() {
        maxAffixesInKey = max(0, maxAffixesInKey)
    }
}

@Config(name = "loot_fabricator")
class LootFabricator : ConfigData {
    var pristineExchangeRate = 16
    var processTime = 200

    override fun validatePostLoad() {
        if (pristineExchangeRate < 0) {
            pristineExchangeRate = 0
        }

        if (processTime < 0) {
            processTime = 0
        }
    }
}

@Config(name = "data_model")
class DataModel : ConfigData {

    var basicDataRequired = 8
    var advancedDataRequired = 16
    var superiorDataRequired = 32
    var selfAwareDataRequired = 64

    override fun validatePostLoad() {
        if (basicDataRequired <= 0) {
            basicDataRequired = 1
        }
        if (advancedDataRequired < basicDataRequired) {
            advancedDataRequired = basicDataRequired
        }
        if (superiorDataRequired < advancedDataRequired) {
            superiorDataRequired = basicDataRequired
        }
        if (selfAwareDataRequired < advancedDataRequired) {
            selfAwareDataRequired = advancedDataRequired
        }
    }
}

@Config(name = "system_glitch")
class SystemGlitch : ConfigData {

    var teleportChance = 0.05F
    var teleportMinDistance = 5
    var teleportDelay = 100
    var teleportAroundPlayerRadius = 2

    @ConfigEntry.Gui.Tooltip
    var damageLimiter = 20F

    override fun validatePostLoad() {
        teleportChance = max(0F, min(1F, teleportChance))
        teleportMinDistance = max(0, teleportMinDistance)
        teleportDelay = max(0, teleportDelay)
        teleportAroundPlayerRadius = max(1, teleportAroundPlayerRadius)
        damageLimiter = max(0F, damageLimiter)
    }
}

@Config(name = "data_collection")
class DataCollection : ConfigData {
    var baseDataGainPerKill = 1
}

@Config(name = "matter_condenser")
class MatterCondenser : ConfigData {
    var processTime = 40

    override fun validatePostLoad() {
        processTime = max(1, processTime)
    }
}

@Config(name = "glitch_armor")
class GlitchArmor : ConfigData {
    var dataAmountToBasic = 32
    var dataAmountToAdvanced = 96
    var dataAmountToSuperior = 192
    var dataAmountToSelfAware = 384
    var soulVisionRange = 12
    var maxFlightTicksPerLevel = 30 * 20
    var undyingCooldownTime = 36000

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    val costs = GlitchArmorDataConsume()

    override fun validatePostLoad() {
        dataAmountToBasic = max(0, dataAmountToBasic)
        dataAmountToAdvanced = max(dataAmountToBasic, dataAmountToAdvanced)
        dataAmountToSuperior = max(dataAmountToAdvanced, dataAmountToSuperior)
        dataAmountToSelfAware = max(dataAmountToSuperior, dataAmountToSelfAware)
    }
}

class GlitchArmorDataConsume : ConfigData {
    var fireProtection = 1F
    var autoExtinguish = 4F
    var featherFalling = 3F
    var fireImmunity = 1F
    var jumpBoost = .0075F
    var plenty = 1F
    var unrottenFlesh = .5F
    var rotResistance = .5F
    var zombieFriendly = .1F
    var archery = .5F
    var skeletonFriendly = .1F
    var fallImmunity = 1F
    var endermenProofVision = 1F
    var shulkerFriendly = .2F
    var teleports = 1.5F
    var soulVision = 8F
    var nightVision = .005F
    var fly = 0.05F
    var underwaterHaste = .01F
    var depthStrider = .01F
    var waterBreathing = .01F
    var poseidonBless = .015F
    var resistance = .3F
    var undying = 8F
}
