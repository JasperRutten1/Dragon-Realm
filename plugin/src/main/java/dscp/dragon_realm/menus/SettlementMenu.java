package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.builders.TextBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import dscp.dragon_realm.kingdoms.claims.settlements.SettlementCosts;
import dscp.dragon_realm.kingdoms.claims.settlements.SettlementLevel;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class SettlementMenu extends Container {

    private Settlement settlement;

    public SettlementMenu(Settlement settlement) {
        super("Kingdom Settlements", 3);
        this.settlement = settlement;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {

    }

    @Override
    protected void onClose(Player viewer) {

    }

}
