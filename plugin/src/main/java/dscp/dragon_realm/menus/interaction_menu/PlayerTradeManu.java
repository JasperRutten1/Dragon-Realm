package dscp.dragon_realm.menus.interaction_menu;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerTradeManu extends Container {
    public PlayerTradeManu(OfflinePlayer player) {
        super("", 3);
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {

    }

    @Override
    protected void onClose(Player viewer) {

    }
}
