package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.builders.TextBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class SettlementGovernorMenu extends Container {

    Settlement settlement;
    List<KingdomMember> members;
    int rows;

    public SettlementGovernorMenu(Settlement settlement, List<KingdomMember> members, int rows) {
        super("Governor Assignment", rows);
        this.settlement = settlement;
        this.members = members;
        this.rows = rows;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        bp.slot(4).item(new ItemStackBuilder(settlement.getLevel().getGuiItem())
            .name(settlement.getName())
            .lore(new LoreBuilder()
                .blank()
                .line(settlement.getLevel().getName())
            )
            .build()
        );

        KingdomMember currentGovernor = settlement.getGovernor();

        bp.slot(5).item(new ItemStackBuilder(Material.PLAYER_HEAD)
            .name("Current Governor: " + (currentGovernor != null ? currentGovernor.getPlayer().getName() : "None"))
            .setSkullOwner(currentGovernor != null ? currentGovernor.getPlayer() : null)
            .lore(
                    new LoreBuilder()
                    .line("Click Here to Remove This Member as Governor")
            )
            .build()
        ).handler(e -> {
            if(settlement.getKingdom().getMembers().getMember(viewer).hasPermission(KingdomMemberRank.ROYAL)){
                try {
                    settlement.removeGovernor(settlement.getGovernor());
                    new SettlementGovernorMenu(this.settlement, this.members, this.rows).open(viewer);
                } catch (KingdomException kingdomException) {
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                        .text(kingdomException.getMessage()).red()
                        .sendTo(viewer);
                }
            }
            else {
                SoundEffect.FAIL.play(viewer);
            }
        });

        System.out.println(members.size());

        for(int i = 9; i < members.size() + 9; i++) {
            KingdomMember member = members.get(i - 9);
            bp.slot(i).item(new ItemStackBuilder(Material.PLAYER_HEAD)
                .name(member.getPlayer().getName())
                .setSkullOwner(member.getPlayer())
                .lore(new LoreBuilder()
                    .blank()
                    .line(member.getRank().getDisplayName())
                    .line("Click to Assign This Member as the New Governor")
                )
                .build()
            ).handler(e -> {
                try {
                    settlement.assignGovernor(member);
                    new SettlementGovernorMenu(this.settlement, this.members, this.rows).open(viewer);
                } catch (KingdomException kingdomException) {
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                        .text(kingdomException.getMessage()).red()
                        .sendTo(viewer);
                }
            });
        }

        bp.slot(rows * 9 - 1).item(new ItemStackBuilder(Material.BARRIER)
            .name("&c&lReturn")
            .build()
        ).handler(e -> new SettlementMenu(settlement).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
