package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.builders.TextBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.kingdoms.relations.KingdomAlliance;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AllianceMenu extends Container {
    KingdomAlliance alliance;

    public AllianceMenu(KingdomAlliance alliance) {
        super("Alliance", 3);
        this.alliance = alliance;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        List<Kingdom> kingdoms = new ArrayList<>(alliance.getKingdoms());
        Kingdom kingdomOfViewer = Kingdom.getKingdomFromPlayer(viewer);
        if(kingdomOfViewer == null || !kingdoms.contains(kingdomOfViewer)) {
            viewer.closeInventory();
            return;
        }

        bp.slot(4).item(new ItemStackBuilder(Material.NETHER_STAR)
                .name("&l&6Alliance")
                .lore(new LoreBuilder()
                        .blank()
                        .line(alliance.toString())
                )
                .build()
        );

        for(int i = 10, j = 0 ; j < kingdoms.size() ; i += 3, j++){
            Kingdom kingdom = kingdoms.get(j);
            bp.slot(i).item(new ItemStackBuilder(Material.EMERALD_BLOCK)
                    .name("&l&6" + kingdom.getName())
                    .lore(new LoreBuilder()
                            .blank()
                            .lineIf("Your Kingdom: ", kingdom.getMembers().isMemberOfKingdom(viewer))
                            .line(kingdom.toString())
                    )
                    .build()
            );
        }

        KingdomMember member = kingdomOfViewer.getMembers().getMember(viewer);
        assert member != null;

        if(member.hasPermission(KingdomMemberRank.ROYAL)){
            bp.slot(22).item(new ItemStackBuilder(Material.DARK_OAK_DOOR)
                    .name("&l&6Leave Alliance")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("Click to leave this alliance.")
                    )
                    .build()
            ).handler(e -> {
                try {
                    kingdomOfViewer.getRelations().leaveAlliance();
                    new KingdomRelationsMenu(kingdomOfViewer).open(viewer);
                } catch (KingdomException kingdomException) {
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                            .text(kingdomException.getMessage()).red()
                            .sendTo(viewer);
                }
            });

            bp.slot(18).item(new ItemStackBuilder(Material.REDSTONE)
                    .name("&l&6Settings")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("COMING SOON!")
                    )
                    .build()
            ).handler(e -> SoundEffect.FAIL.play(viewer));
        }



        bp.slot(26).item(new ItemStackBuilder(Material.BARRIER)
                .name("&l&4Return")
                .build()
        ).handler(e -> new KingdomRelationsMenu(kingdomOfViewer).open(viewer));

    }

    @Override
    protected void onClose(Player viewer) {

    }
}
