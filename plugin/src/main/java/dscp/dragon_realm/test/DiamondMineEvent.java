package dscp.dragon_realm.test;

import dscp.dragon_realm.dataContainer.DataContainerDataType;
import dscp.dragon_realm.dataContainer.ObjectData;
import dscp.dragon_realm.dataContainer.PlayerDataContainer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DiamondMineEvent implements Listener {
    @EventHandler
    public void diamondMine(BlockBreakEvent event){
        Player player = event.getPlayer();

        PlayerDataContainer container = PlayerDataContainer.getPlayerData(player.getUniqueId());
        assert container != null;

        if(event.getBlock().getType() == Material.DIAMOND_ORE){
            System.out.println("diamond mined!");
            ObjectData<Integer> data = container.loadObjectFromContainer(DataContainerDataType.IntegerType, "diamonds-mined");
            Integer diamonds;
            if(data == null) {
                diamonds = 0;
            }
            else{
                diamonds = data.getObject();
            }
            diamonds++;
            container.saveObjectToContainer(DataContainerDataType.IntegerType, "diamonds-mined",
                    new ObjectData<>(DataContainerDataType.IntegerType, diamonds));
        }
    }
}
