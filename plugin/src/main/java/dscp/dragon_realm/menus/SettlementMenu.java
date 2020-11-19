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
        bp.slot(4).item(new ItemStackBuilder(settlement.getLevel().getGuiItem())
            .name(settlement.getName())
            .lore(new LoreBuilder()
                .blank()
                .line("Dylanchill was here.")
            )
            .build()
        );

        KingdomMember currentGovernor = settlement.getGovernor();

        bp.slot(11).item(new ItemStackBuilder(Material.PLAYER_HEAD)
            .name("Current Governor: " + (currentGovernor != null ? currentGovernor.getPlayer().getName() : "None"))
            .setSkullOwner(currentGovernor != null ? currentGovernor.getPlayer() : null)
            .lore(new LoreBuilder()
                .blank()
                .line("Assign the Settlements Governor")
            )
            .build()
        ).handler(e -> {
            if(!settlement.getKingdom().getMembers().getMember(viewer).hasPermission(KingdomMemberRank.ROYAL)) {
                SoundEffect.FAIL.play(viewer);
                return;
            }

            List<KingdomMember> members = settlement.getKingdom().getMembers().getMembersWithRankHigher(KingdomMemberRank.NOBEL);
            int rows = 3;

            if(members.size() > 9) rows++;
            if(members.size() > 18) rows++;
            if(members.size() > 27) rows++;

            new SettlementGovernorMenu(settlement, members, rows).open(viewer);
        });

        if(settlement.getLevel() == SettlementLevel.getHighestLevel()) {
            bp.slot(15).item(new ItemStackBuilder(Material.NETHER_STAR)
                .name("&c&lMax Level")
                .lore(new LoreBuilder()
                    .blank()
                    .line("This Settlement has Reached The Max Level!")
                )
                .build()
            ).handler(e -> SoundEffect.FAIL.play(viewer));
        } else {
            bp.slot(15).item(new ItemStackBuilder(Material.ANVIL)
                .name("&6&lUpgrade Settlement")
                .lore(new LoreBuilder()
                    .blank()
                    .line("Price: ⛃" + Objects.requireNonNull(SettlementCosts.getCost(settlement.getLevel().getNextLevel())).getCoins())
                    .line("Vault: ⛃" + settlement.getKingdom().getVault().getCoins())
                )
                .build()
            ).handler(e -> {
                KingdomMember member = settlement.getKingdom().getMembers().getMember(viewer);
                if(member == null) return;
                if(!(member.hasPermission(KingdomMemberRank.ROYAL) || member.equals(settlement.getGovernor()))){
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                            .text("Only Royals or The Governor of This Settlement Can Upgrade This Settlement").red()
                            .sendTo(viewer);
                }
                try {
                    settlement.levelUp();
                    new SettlementMenu(this.settlement).open(viewer);
                } catch (KingdomException kingdomException) {
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                        .text(kingdomException.getMessage()).red()
                        .sendTo(viewer);
                }
            });
        }

        bp.slot(26).item(new ItemStackBuilder(Material.BARRIER)
            .name("&c&lReturn")
            .build()
        ).handler(e -> new KingdomSettlementMenu(settlement.getKingdom()).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }

}
