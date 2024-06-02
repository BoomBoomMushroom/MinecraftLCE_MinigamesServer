package me.dillionweaver.tumblegamemode.data;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileSaveData {
    private final TumbleGamemode main;
    public FileSaveData(TumbleGamemode main){
        this.main = main;
    }

    public boolean createFileIfNotExisting() throws IOException {
        File dataFile = new File(main.getDataFolder(), TumbleConstants.saveDataFilename);
        return dataFile.createNewFile();
    }

    public void setDataFileKey(String key, Object value){
        File dataFile = new File(main.getDataFolder(), TumbleConstants.saveDataFilename);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        config.set(key, value);

        try{
            config.save(dataFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Object getDataFileKey(String key){
        File dataFile = new File(main.getDataFolder(), TumbleConstants.saveDataFilename);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        return config.get(key);
    }
}
