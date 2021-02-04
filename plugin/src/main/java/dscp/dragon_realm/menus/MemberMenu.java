package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.builders.TextBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MemberMenu extends Container {
    private KingdomMember member;

    public MemberMenu(KingdomMember member) {
        super(member.getPlayer().getName(), 3);
        this.member = member;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        bp.slot(4).item(new ItemStackBuilder(Material.PLAYER_HEAD)
            .name(member.getRank().getColor() + member.getPlayer().getName())
            .setSkullOwner(member.getPlayer())
            .lore(new LoreBuilder()
                .blank()
                .line(member.getRank().getDisplayName())
            )
            .build()
        );

        bp.slot(13).item(new ItemStackBuilder(Material.EMERALD)
            .name(member.getRank().getDisplayName())
            .lore(new LoreBuilder()
                .blank()
                .line("Rank Information")
            )
            .build()
        );

        KingdomMember viewerMember = member.getKingdom().getMembers().getMember(viewer);
        if(KingdomMemberRank.canPromote(viewerMember, member)){
            KingdomMemberRank higherRank = member.getRank().getHigherRank();
            assert higherRank != null;
            bp.slot(14).item(new ItemStackBuilder(Material.GREEN_CONCRETE)
                .name("&aPromote member to " + higherRank.getDisplayName())
                .lore(new LoreBuilder()
                    .blank()
                        .lineIf("This action will pass the Kingdom on to this member, making them King.",
                                member.getRank() == KingdomMemberRank.ROYAL)
                    .line("Rank Information")
                )
                .build()
            ).handler(e -> {
                try{
                    if(member.getRank() == KingdomMemberRank.ROYAL){
                        member.getKingdom().getMembers().getKing().demoteMember();
                    }
                    member.promoteMember();
                    new MemberMenu(this.member).open(viewer);
                }catch (KingdomException ex){
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                        .text(ex.getMessage()).red()
                        .sendTo(viewer);
                }
            });
        }

        if(KingdomMemberRank.canDemote(viewerMember, member)){
            KingdomMemberRank lowerRank = member.getRank().getLowerRank();
            assert lowerRank != null;
            bp.slot(12).item(new ItemStackBuilder(Material.RED_CONCRETE)
                .name("&cDemote member to " + lowerRank.getDisplayName())
                .lore(new LoreBuilder()
                    .blank()
                    .line("Rank Information")
                )
                .build()
            ).handler(e -> {
                try{
                    member.demoteMember();
                    new MemberMenu(this.member).open(viewer);
                }catch (KingdomException ex){
                    SoundEffect.FAIL.play(viewer);
                    new TextBuilder()
                        .text(ex.getMessage()).red()
                        .sendTo(viewer);
                }
            });
        }

        bp.slot(26).item(new ItemStackBuilder(Material.BARRIER)
                .name("&c&lReturn")
                .build()
        ).handler(e -> new KingdomMembersMenu(member.getKingdom()).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
