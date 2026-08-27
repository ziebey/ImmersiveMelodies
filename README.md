# Immersive Melodies

Claude Coded
Working 26.2 

[![Crowdin](https://badges.crowdin.net/immersive-collection/localized.svg)](https://crowdin.com/project/immersive-melodies)

Hosted on CurseForge: https://www.curseforge.com/minecraft/mc-mods/immersive-melodies

Config docu here: https://github.com/Luke100000/ImmersiveMelodies/wiki/Config

Modpack/Datapack Creator help: https://github.com/Luke100000/ImmersiveMelodies/wiki/Custom-Melodies

Maven: https://maven.conczin.net/#/Artifacts/net/conczin/immersive_melodies

## API and Custom Instruments

1) Add sounds for every octave (1-8) into `resources/assets/{namespace}/sounds/instruments/{instrument_name}`, or use
   `scripts/convert.sh` to convert a C4 base audio automatically.
2) Register the sounds into the `sounds.json`
3) Register the item via
   `immersive_melodies.Items.register(java.lang.String, java.lang.String, long, org.joml.Vector3f)`
4) Register the animation handler via `immersive_melodies.client.animation.ItemAnimators.register`
5) Add `instrument.json` and `instrument_hand.json` item models and textures
6) And of course recipes, lang files, tags, and whatever else might be needed
