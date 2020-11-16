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

public class SettlementMenu extends Container {

    private Settlement settlement;

    public SettlementMenu(Settlement settlement) {
        super("Kingdom Settlements", 5);
        this.settlement = settlement;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        bp.slot(4).item(new ItemStackBuilder(settlement.getLevel().getGuiItem())
            .name(settlement.getName())
            .lore(new LoreBuilder()
                .blank()
                .line("Lorem ipsum")
            )
            .build()
        );

        KingdomMember currentGovernor = settlement.getGovernor();

        bp.slot(20).item(new ItemStackBuilder(Material.PLAYER_HEAD)
            .name("Current Governor: " + (currentGovernor != null ? currentGovernor.getPlayer().getName() : "None"))
            .setSkullOwner(currentGovernor != null ? currentGovernor.getPlayer() : null)
            .lore(new LoreBuilder()
                .blank()
                .line("Assign the settlements governor")
            )
            .build()
        ).handler(e -> {
            if(!settlement.getKingdom().getMembers().getMember(viewer).hasPermission(KingdomMemberRank.ROYAL)) {
                SoundEffect.FAIL.play(viewer);
                return;
            }

            List<KingdomMember> members = settlement.getKingdom().getMembers().getMembersWithRank(KingdomMemberRank.NOBEL);
            int rows = 3;

            if(members.size() > 9) rows++;
            if(members.size() > 18) rows++;
            if(members.size() > 27) rows++;

            new SettlementGovernorMenu(settlement, members, rows).open(viewer);
        });

        if(settlement.getLevel() == SettlementLevel.getHighestLevel()) {
            bp.slot(16).item(new ItemStackBuilder(Material.NETHER_STAR)
                .name("&c&lMax Level")
                .lore(new LoreBuilder()
                    .blank()
                    .line("This settlement has reached the max level")
                )
                .build()
            ).handler(e -> SoundEffect.FAIL.play(viewer));
        } else {
            bp.slot(16).item(new ItemStackBuilder(Material.ANVIL)
                .name("&6&lUpgrade Settlement")
                .lore(new LoreBuilder()
                    .blank()
                    .line("Price: " + SettlementCosts.getCost(settlement.getLevel().getNextLevel()))
                    .line("Vault: " + settlement.getKingdom().getVault().getCoins())
                )
                .build()
            ).handler(e -> {
                try {
                    settlement.levelUp();
                } catch (KingdomException kingdomException) {
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                        .text(kingdomException.getMessage()).red()
                        .sendTo(viewer);
                }
            });
        }

        bp.slot(44).item(new ItemStackBuilder(Material.BARRIER)
            .name("&c&lReturn")
            .build()
        ).handler(e -> new KingdomSettlementMenu(settlement.getKingdom()).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }

}
