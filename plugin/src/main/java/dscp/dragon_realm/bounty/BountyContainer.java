package dscp.dragon_realm.bounty;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dataContainer.DataContainerDataType;
import dscp.dragon_realm.dataContainer.ObjectData;
import dscp.dragon_realm.dataContainer.ObjectDataContainer;
import dscp.dragon_realm.utils.AdvancedObjectIO;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class BountyContainer extends ObjectDataContainer implements Serializable {
    private static final long serialVersionUID = 9151891114904718022L;

    private static File bountyFile = getBountyFile();
    private static BountyContainer bounties = getBountyContainer();

    public BountyContainer(){
        super();
    }

    private static BountyContainer getBountyContainer(){
        AdvancedObjectIO<BountyContainer> objectIO = new AdvancedObjectIO<>(getBountyFile());
        try{
            BountyContainer container = objectIO.loadObject();
            if(container == null) return new BountyContainer();
            else return container;
        } catch (IOException e) {
            e.printStackTrace();
            return new BountyContainer();
        }
    }

    private static File getBountyFile(){
        File file = new File(Dragon_Realm.instance.getDataFolder(), "bounties.dat");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void placeBounty(Bounty bounty){
        Bounty toSave = bounties.loadObjectFromContainer(DataContainerDataType.BountyType, bounty.getTargetUUID().toString());
        if(toSave != null){
            toSave.combineWith(bounty);
        }
        else{
            toSave = bounty;
        }
        bounties.saveObjectToContainer(DataContainerDataType.BountyType, bounty.getTargetUUID().toString(), toSave);
    }

    public static Bounty getBountyForPlayer(UUID playerUUID){
        return bounties.loadObjectFromContainer(DataContainerDataType.BountyType, playerUUID.toString());
    }

    public static boolean hasBounty(UUID playerUUID){
        return getBountyForPlayer(playerUUID) != null;
    }

    public static void initialise(){
        Dragon_Realm.instance.getServer().getPluginManager().registerEvents(new BountyEvent(), Dragon_Realm.instance);
    }

    public static void removeBounty(UUID player){
        bounties.removeObject(DataContainerDataType.BountyType, player.toString());
    }

    public static void saveContainer(){
        AdvancedObjectIO<BountyContainer> objectIO = new AdvancedObjectIO<>(bountyFile);
        try{
            objectIO.saveObject(bounties);
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }
    }
}
