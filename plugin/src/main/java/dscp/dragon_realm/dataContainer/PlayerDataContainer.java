package dscp.dragon_realm.dataContainer;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.utils.AdvancedObjectIO;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataContainer extends ObjectDataContainer{
    public UUID playerUUID;

    private static HashMap<UUID, PlayerDataContainer> playerDataMap = new HashMap<>();
    private static final File playerDataDir = getPlayerDataDir();

    public PlayerDataContainer(UUID playerUUID){
        super();
        if(playerUUID == null) throw new IllegalArgumentException("uuid can't be null");
        this.playerUUID = playerUUID;
    }

    public PlayerDataContainer(OfflinePlayer player){
        this(player.getUniqueId());
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public static HashMap<UUID, PlayerDataContainer> getPlayerDataMap() {
        return playerDataMap;
    }

    public void unloadPlayerData(){
        if(playerDataMap.containsKey(this.playerUUID)){
            AdvancedObjectIO<PlayerDataContainer> objectIO = new AdvancedObjectIO<>(getDataFileForPlayer(this.playerUUID));
            try{
                System.out.println("\nsaving data for " + this.playerUUID.toString());
                System.out.println(this.toString());
                objectIO.saveObject(this);
                playerDataMap.remove(this.playerUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static PlayerDataContainer getPlayerData(UUID playerUUID){
        if(!playerDataMap.containsKey(playerUUID)){
            AdvancedObjectIO<PlayerDataContainer> objectIO = new AdvancedObjectIO<>(getDataFileForPlayer(playerUUID));
            try{
                PlayerDataContainer container = objectIO.loadObject();
                if(container == null) container = new PlayerDataContainer(playerUUID);
                playerDataMap.put(playerUUID, container);
            }
            catch (IOException e){
                playerDataMap.put(playerUUID, new PlayerDataContainer(playerUUID));
            }
        }
        return playerDataMap.get(playerUUID);
    }

    public static void unloadPlayerData(UUID playerUUID){
        if(playerDataMap.containsKey(playerUUID)){
            playerDataMap.get(playerUUID).unloadPlayerData();
        }
    }

    public static  void unloadAllPlayerData(){
        HashMap<UUID, PlayerDataContainer> map = new HashMap<>(playerDataMap);
        for(Map.Entry<UUID, PlayerDataContainer> entry : map.entrySet()){
            PlayerDataContainer container = entry.getValue();
            container.unloadPlayerData();
        }
    }

    private static File getPlayerDataDir(){
        File dir = new File(Dragon_Realm.instance.getDataFolder(), "player-data");
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdir();
        }
        return dir;
    }

    public static File getDataFileForPlayer(UUID playerUUID){
        File file = new File(playerDataDir, playerUUID.toString() + ".dat");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
