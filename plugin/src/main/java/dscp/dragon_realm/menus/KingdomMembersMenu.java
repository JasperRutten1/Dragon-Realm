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
        bp.slot(4).item(new ItemStackBuilder(Material.EMERALD)
            .name(kingdom.getName())
            .lore(new LoreBuilder()
                    .blank()
                    .line("Dylanchill was here.")
            )
            .build()
        );

        KingdomMember kingdomKing = kingdom.getMembers().getKing();

        bp.slot(10).item(new ItemStackBuilder(Material.PLAYER_HEAD)
                .name("&6&l" + kingdomKing.getPlayer().getName())
                .setSkullOwner(kingdomKing != null ? kingdomKing.getPlayer() : null)
                .lore(new LoreBuilder()
                    .blank()
                    .line("&6The King of this Kingdom.")
                )
                .build()
        ).handler(e -> {
            if(kingdomKing == null){
                SoundEffect.FAIL.play(viewer);
                return;
            }
            new MemberMenu(kingdomKing).open(viewer);
        });

        for(int i = 0 ; i < 2 ; i++){
            try{
                KingdomMember member = kingdom.getMembers().getMembersWithRankExact(KingdomMemberRank.ROYAL).get(i);
                bp.slot(i + 12).item(new ItemStackBuilder(Material.PLAYER_HEAD)
                        .name("&e" + member.getPlayer().getName())
                        .setSkullOwner(member.getPlayer())
                        .lore(new LoreBuilder()
                                .blank()
                                .line(member.getRank().getDisplayName())
                        )
                        .build()
                ).handler(e -> new MemberMenu(member).open(viewer));
            }
            catch (IndexOutOfBoundsException ex){
                bp.slot(i + 12).item(new ItemStackBuilder(Material.HEART_OF_THE_SEA)
                    .name("&4Not assigned")
                    .build()
                );
            }
        }

        for(int i = 0 ; i < 3 ; i++){
            try{
                KingdomMember member = kingdom.getMembers().getMembersWithRankExact(KingdomMemberRank.NOBEL).get(i);
                bp.slot(i + 15).item(new ItemStackBuilder(Material.PLAYER_HEAD)
                        .name("&b" + member.getPlayer().getName())
                        .setSkullOwner(member.getPlayer())
                        .lore(new LoreBuilder()
                                .blank()
                                .line(member.getRank().getDisplayName())
                        )
                        .build()
                ).handler(e -> new MemberMenu(member).open(viewer));
            }
            catch (IndexOutOfBoundsException ex){
                bp.slot(i + 15).item(new ItemStackBuilder(Material.HEART_OF_THE_SEA)
                        .name("&4Not assigned")
                        .build()
                );
            }
        }

        for(int i = 18, j = 0 ; j < kingdom.getMembers().getMembersWithRankLower(KingdomMemberRank.NOBEL).size() ; i++, j++){
            if(i < 45){
                KingdomMember member = kingdom.getMembers().getMembersWithRankLower(KingdomMemberRank.NOBEL).get(j);
                bp.slot(i).item(new ItemStackBuilder(Material.PLAYER_HEAD)
                        .name("&b" + member.getPlayer().getName())
                        .setSkullOwner(member.getPlayer())
                        .lore(new LoreBuilder()
                            .blank()
                            .line(member.getRank().getDisplayName())
                        )
                        .build()
                ).handler(e -> new MemberMenu(member).open(viewer));
            }
        }

        bp.slot(this.getSize() - 1).item(new ItemStackBuilder(Material.BARRIER)
                .name("&c&lReturn")
                .build()
        ).handler(e -> new KingdomOverviewMenu(this.kingdom).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
