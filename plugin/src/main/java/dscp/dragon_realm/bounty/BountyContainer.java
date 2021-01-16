package dscp.dragon_realm.bounty;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dataContainer.DataContainerDataType;
import dscp.dragon_realm.dataContainer.ObjectData;
import dscp.dragon_realm.dataContainer.ObjectDataContainer;
import dscp.dragon_realm.utils.AdvancedObjectIO;

import java.io.File;
import java.io.IOException;

public class BountyContainer extends ObjectDataContainer {
    private static BountyContainer bounties;
    private static File bountyFile = getBountyFile();

    public BountyContainer(){

    }

    private static BountyContainer getBountyContainer(){
        AdvancedObjectIO<BountyContainer> objectIO = new AdvancedObjectIO<>(bountyFile);
        try{
            return objectIO.loadObject();
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

    private static void placeBounty(Bounty bounty){
        bounties.saveObjectToContainer(DataContainerDataType.BountyType,bounty.getTargetUUID().toString(),bounty);
    }
}
