package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.builders.TextBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class AllianceInvitationMenu extends Container {
    private Kingdom kingdom;

    public AllianceInvitationMenu(Kingdom kingdom) {
        super("Alliance invitations", calculateRows(kingdom));
        this.kingdom = kingdom;
    }

    private static int calculateRows(Kingdom kingdom){
        int rows = 3;
        int size = kingdom.getRelations().getAllianceInvites().size();
        if(size > 9) rows++;
        if(size > 18) rows++;
        if(size > 27) rows++;
        return rows;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        bp.slot(4).item(new ItemStackBuilder(Material.EMERALD)
                .name("&l&6" + kingdom.getName())
                .lore(new LoreBuilder()
                        .blank()
                        .line(kingdom.toString())
                )
                .build()
        );

        List<Kingdom> invites = kingdom.getRelations().getAllianceInvites();
        for(int i = 9, j = 0 ; j < invites.size() && i < 45 ; i++, j++){
            Kingdom invitedBy = invites.get(j);
            bp.slot(i).item(new ItemStackBuilder(Material.PAPER)
                    .name("&l&6" + invitedBy.getName())
                    .lore(new LoreBuilder()
                            .blank()
                            .line("invitation to join alliance")
                    )
                    .build()
            ).handler(e -> {
                if(this.kingdom.getMembers().getMember(viewer).hasPermission(KingdomMemberRank.ROYAL)){
                    try {
                        kingdom.getRelations().acceptAllianceInvite(invitedBy);
                    } catch (KingdomException kingdomException) {
                        SoundEffect.FAIL.play(viewer);
                        new TextBuilder()
                                .text(kingdomException.getMessage()).red()
                                .sendTo(viewer);
                    }
                }
                else{
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                            .text("only royals or higher can accept alliance invitations").red()
                            .sendTo(viewer);
                }
                new AllianceInvitationMenu(this.kingdom).open(viewer);
            });

            bp.slot(getSize() - 1).item(new ItemStackBuilder(Material.BARRIER)
                    .name("&l&4Return")
                    .build()
            ).handler(e -> new KingdomRelationsMenu(this.kingdom).open(viewer));
        }
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
