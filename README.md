# Showcaser

Showcaser allow you to share items, fluids, recipes and even quests with a single keybind!

### Requirements:
  - Architectury API

### Supported mods:
  - EMI
  - Roughly Enough Items
  - Just Enough Items
  - Applied Energistics 2
  - FTB Quests

  Although not many mods listed, Showcaser allows you to share items from many modded screens!

## ✨ Video demonstration ✨:

### Item sharing
<details>
  <summary>
    Click to expand
  </summary>
  
  ![Showcase demo](https://github.com/user-attachments/assets/73b0e592-16d7-49bf-9366-ba8b5bb4b2f9)
</details>

### EMI recipe sharing
<details>
  <summary>
    Click to expand
  </summary>
  
  ![showcase-demo-emi](https://github.com/user-attachments/assets/a9183ddc-a43c-4473-922e-7ae7ea4e7c5a)
</details>

### FTB Quest sharing
<details>
  <summary>
    Click to expand
  </summary>
  
  ![showcase-demo-quest](https://github.com/user-attachments/assets/b7baf478-98ae-4ba5-a2a8-2b3a55ba8a11)
</details>


## How to share

1. Item -> hover the slot and press share keybind (Shift + X)

2. EMI's / REI's / JEI's recipe -> hover resource and use share keybind.<br>To share specific recipe in EMI you can add it as favourite and then share it from EMI's favourites panel

3. FTB Quest -> open quest in quest book and press keybind

## Commands
- /showcaser
  - ban -> bans specified player from sharing items
  - unban -> unbans specified player from sharing items<br>
Banned players are "stored" in shocaser's server storage in the config folder
  - reload -> loads server config without the need to restart the server. To reload client's configs simply rejoin the world

## Configuration

You can find showcaser config files at "\config\showcaser"
### Client config
```
"addEmptySpaceBeforeVerifiedText": false
```
allows omitting empty line before verified message line on shared items, which may come in handy when sharing items with big tooltips
```
"ignoreCustomNames": false
```
sometimes shared item names can be inaproppriate and this config parameter will strip all custom names from shared items

<img width="847" height="363" alt="изображение" src="https://github.com/user-attachments/assets/cea21ee7-a4d6-4340-a148-71b86658dc75" />

### Server config
Regulates share cooldown. Self-explanatory
```
"chatSharingCooldown": 20
```
By default mod adds "Verified by the server" message to all items that server can get by accessing screen's screen handler. Some mods store items that player dont actually have on the server side and they are displayed as verified. You can hide this line if you dont need it, but that would make item flexing impossible so think twice. To ban screens that contain these "ghost" items you can blacklist them using config parameters below
```
"hideVerifiedTooltipLine": false
```
Screen class may be put here to disable sharing from it. Screen class that player is trying to share resource from is printed in debug.log upon sharing. This parameter bans specified class exactly
```
blacklistedClassesExact": []
```
Works same as blacklistedClassesExact, but also bans all classes that inherit banned class 
```
"blacklistedClassesWithInheritors": []
```



