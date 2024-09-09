package me.dillionweaver.battlegamemode.effects;

import me.dillionweaver.battlegamemode.BattleGamemode;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlaySoundEffect {
    private final BattleGamemode main;
    public PlaySoundEffect(BattleGamemode main){
        this.main = main;
    }

    public void playSoundEffectForPlayer(Player player, String soundName, SoundCategory soundCategory){
        soundName = soundName.toLowerCase();
        if(!soundName.startsWith("minecraft:")){
            soundName = "minecraft:" + soundName;
        }

        player.playSound((Entity)player, soundName, soundCategory, 1f, 1f);
    }

    public void stopPlayingSoundForPlayerByCategory(Player player, SoundCategory soundCategory){
        player.stopSound(soundCategory);
    }
    public void stopPlayingSoundForPlayer(Player player, String soundName, SoundCategory soundCategory){
        player.stopSound(soundName, soundCategory);
    }
}
