## AccountSwitch (FML 1.8) [![Build Status](http://vps40435.vps.ovh.ca:8080/job/AccountSwitch/badge/icon)](http://vps40435.vps.ovh.ca:8080/job/AccountSwitch/)
Account switching mod for minecraft

As the user of AccountSwitch you should be aware that it stores your passwords in a plain text file to recall them for easy switching. This has some inherent risks. I (the mod author) am not liable for you (the user) downloading and installing mods or other programs that steal this information. Don't be dumb, don't install shady, closed source mods, and you won't have your passwords stolen.

All the gradle stuff is from [Lunatrius](https://github.com/Lunatrius/Schematica) so ty for that

Installing and Using AccountSwitch
---
1. Run Minecraft 1.8 at least once (not 1.8.x, just regular 1.8)
2. Download the [Forge 1.8-11.14.1.1404 Installer](http://adfoc.us/serve/sitelinks/?id=271228&url=http://files.minecraftforge.net/maven/net/minecraftforge/forge/1.8-11.14.1.1404/forge-1.8-11.14.1.1404-installer.jar) or [another version](http://files.minecraftforge.net) (OTHER VERSIONS ARE NOT OFFICIALLY SUPPORTED BUT MAY WORK)
3. Run the installer and install forge
4. [Open your .minecraft folder](http://minecraft.gamepedia.com/.minecraft)
5. Download the [latest AccountSwitch release](http://biggestnerd:8080/job/accountswitch)
5. if you don't see a folder called 'mods', create one, then put the AccountSwitch jar in the mods folder
6. Open the minecraft launcher
7. Create a new profile and select the version 'release Forge 1.8-11.14.1.1404'
8. Run the forge profile and proceed to enjoy the mod!

Compiling from Source
---

This mod is compiled using the Forge Mod Loader (FML) mod pack which includes data from the Minecraft Coder Pack (MCP).

To compile this mod from the source code provided

(note that on unix based operating systems you will need to run 'chmod +x gradlew' and replace gradlew with ./gradlew in all commands)

1. Clone the repo
2. Open le command line
3. run gradlew setupDevWorkspace
4. run gradlew build
5. BOOM! it'll be in the build/libs folder
