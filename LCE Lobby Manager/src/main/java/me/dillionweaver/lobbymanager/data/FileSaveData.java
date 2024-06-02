package me.dillionweaver.lobbymanager.data;

import me.dillionweaver.lobbymanager.LobbyManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileSaveData {
    private final LobbyManager main;
    public FileSaveData(LobbyManager main){
        this.main = main;
    }

    public boolean createFileIfNotExisting() throws IOException {
        File dataFile = new File(main.getDataFolder(), LobbyManagerConstants.saveDataFilename);
        return dataFile.createNewFile();
    }

    public void setDataFileKey(String key, Object value){
        File dataFile = new File(main.getDataFolder(), LobbyManagerConstants.saveDataFilename);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        config.set(key, value);

        try{
            config.save(dataFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Object getDataFileKey(String key){
        File dataFile = new File(main.getDataFolder(), LobbyManagerConstants.saveDataFilename);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        return config.get(key);
    }

    public Object[] getDataFileKeys(){
        File dataFile = new File(main.getDataFolder(), LobbyManagerConstants.saveDataFilename);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        return config.getKeys(true).toArray();
    }
}
