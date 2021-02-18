package dscp.dragon_realm.currency;

import dscp.dragon_realm.dataContainer.DataContainerDataType;
import dscp.dragon_realm.dataContainer.PlayerDataContainer;
import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PlayerWallet extends CurrencyContainer{
    private UUID playerUUID;

    public PlayerWallet(OfflinePlayer player) {
        super(getKingdomCurrencyType(player), 25, 5);
        this.playerUUID = player.getUniqueId();
    }

    public void saveWallet(){
        PlayerDataContainer container = PlayerDataContainer.getPlayerData(playerUUID);
        container.saveObjectToContainer(DataContainerDataType.PlayerWalletType, "wallet", this);
    }

    private static DefaultCurrencyType getKingdomCurrencyType(OfflinePlayer player){
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) return DefaultCurrencyType.COINS;
        else return kingdom.getBank().getDefaultCurrencyType();
    }

    public static PlayerWallet getWalletFromPlayer(OfflinePlayer player){
        PlayerDataContainer container = PlayerDataContainer.getPlayerData(player.getUniqueId());
        PlayerWallet wallet = container.loadObjectFromContainer(DataContainerDataType.PlayerWalletType, "wallet");

        if(wallet == null){
            wallet = new PlayerWallet(player);
            wallet.saveWallet();
        }

        return wallet;
    }
}
