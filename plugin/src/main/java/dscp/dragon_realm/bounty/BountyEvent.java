package dscp.dragon_realm.bounty;

import dscp.dragon_realm.currency.PlayerWallet;
import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BountyEvent implements Listener {

    @EventHandler
    public void onPlayerKillsPlayer(PlayerDeathEvent event){
        Player killed = event.getEntity();

        if(!BountyContainer.hasBounty(killed.getUniqueId())) return;

        Player killer = killed.getKiller();
        if(killer == null) return;
        if(killed.equals(killer)) return;
        Bounty bounty = BountyContainer.getBountyForPlayer(killed.getUniqueId());

        Kingdom kingdomKilled = Kingdom.getKingdomFromPlayer(killed);
        Kingdom kingdomKiller = Kingdom.getKingdomFromPlayer(killer);

        if(kingdomKilled != null){
            if(kingdomKilled.equals(kingdomKiller)) return;
        }
        assert killer != null;
        PlayerWallet killerWallet = PlayerWallet.getWalletFromPlayer(killer);

        int totalAmount = 0;
        HashMap<UUID, Integer> copy = new HashMap<>(bounty.getPlacerMap());
        ArrayList<UUID> claimedFrom = new ArrayList<>();
        for(Map.Entry<UUID, Integer> entry : copy.entrySet()){
            UUID placer = entry.getKey();
            Integer amount = entry.getValue();

            if(!killer.getUniqueId().equals(placer)){
                bounty.removePlacer(placer);
                totalAmount =+ amount;
                claimedFrom.add(placer);
            }
        }

        if(bounty.getTotalAmount() == 0){
            BountyContainer.removeBounty(killed.getUniqueId());
        }
        if(!(totalAmount == 0)){
            killerWallet.changeDefaultCurrency(totalAmount);
            Bukkit.broadcastMessage(ChatColor.GOLD + killer.getName() + ChatColor.RESET + " claimed the bounty of "
                    + ChatColor.GOLD + killed.getName() + ChatColor.RESET + ", value: " + totalAmount);

            for(UUID uuid : claimedFrom){
                Player player = Bukkit.getPlayer(uuid);
                if(player != null){
                    player.sendMessage("your bounty on " + killed.getName() + " has been claimed");
                }
            }
        }
    }
}
