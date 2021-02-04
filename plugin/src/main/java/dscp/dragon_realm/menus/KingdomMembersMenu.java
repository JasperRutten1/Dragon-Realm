package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class KingdomMembersMenu extends Container {
    private Kingdom kingdom;

    public KingdomMembersMenu(Kingdom kingdom) {
        super("Members Menu", getRows(kingdom));
        this.kingdom = kingdom;
    }

    private static int getRows(Kingdom kingdom){
        int rows = 4;
        List<KingdomMember> members = kingdom.getMembers().getMembersWithRankLower(KingdomMemberRank.NOBEL);
        if(members.size() > 9) rows++;
        if(members.size() > 18) rows++;
        return rows;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {

    }

    @Override
    protected void onClose(Player viewer) {

    }
}
