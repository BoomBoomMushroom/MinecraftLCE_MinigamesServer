package me.dillionweaver.tumblegamemode.player;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerSpectating {
    private final TumbleGamemode main;
    public PlayerSpectating(TumbleGamemode main){
        this.main = main;
    }

    public HashMap<Player, Entity> playerToSpecAnimal = new HashMap<>();

    public HashMap<EntityType, Sound> entityToSound = new HashMap<>();
    {
        entityToSound.put(EntityType.PARROT, Sound.ENTITY_PARROT_AMBIENT);
    }

    public void spawnPlayerSpectatingAnimal(Player player, EntityType animal){
        Entity entity = playerToSpecAnimal.get(player);
        if(entity != null){return;}

        if(animal == EntityType.PARROT){
            Parrot spectatingParrot = (Parrot) main.tumbleWorld.spawnEntity(player.getLocation(), EntityType.PARROT);
            spectatingParrot.setInvulnerable(true);
            spectatingParrot.setAI(false);
            spectatingParrot.setCollidable(false);
            spectatingParrot.setGravity(false);
            spectatingParrot.setSitting(false);
            spectatingParrot.setSilent(true);
            playerToSpecAnimal.put(player, (Entity)spectatingParrot);
        }
    }

    public void removePlayerSpectatingAnimal(Player player){
        Entity entity = playerToSpecAnimal.get(player);
        if(entity == null){return;}

        entity.remove();
        playerToSpecAnimal.remove(player);
    }
}
