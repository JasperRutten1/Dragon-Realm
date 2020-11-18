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
import dscp.dragon_realm.kingdoms.relations.Relation;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RelationMenu extends Container {
    private Kingdom kingdomOfViewer;
    private Kingdom otherKingdom;

    public RelationMenu(Kingdom kingdomOfViewer, Kingdom otherKingdom) {
        super(kingdomOfViewer.getName() + " <---> " + otherKingdom.getName(), 1);
        this.kingdomOfViewer = kingdomOfViewer;
        this.otherKingdom = otherKingdom;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        Relation relation = kingdomOfViewer.getRelations().getRelationToKingdom(otherKingdom);
        KingdomMember member = kingdomOfViewer.getMembers().getMember(viewer);

        bp.slot(1).item(new ItemStackBuilder(Material.EMERALD)
            .name("&l&6" + kingdomOfViewer.getName())
            .lore(new LoreBuilder()
                .blank()
                .line("your kingdom")
                .line(kingdomOfViewer.toString())
            )
            .build()
        );

        switch (relation){
            case ALLIED:
                KingdomAlliance alliance = kingdomOfViewer.getRelations().getAlliance();

                if(member.hasPermission(KingdomMemberRank.ROYAL)){
                    bp.slot(3).item(new ItemStackBuilder(Material.RED_DYE)
                        .name("&l&cLeave alliance")
                        .lore(new LoreBuilder()
                            .blank()
                            .line("leave the alliance with this kingdom")
                        )
                        .build()
                    ).handler(e -> {
                        try {
                            kingdomOfViewer.getRelations().leaveAlliance();
                        } catch (KingdomException kingdomException) {
                            SoundEffect.FAIL.play(viewer);
                            new TextBuilder()
                                .text(kingdomException.getMessage()).red()
                                .sendTo(viewer);
                        }
                        new RelationMenu(kingdomOfViewer, otherKingdom).open(viewer);
                    });
                }

                bp.slot(4).item(new ItemStackBuilder(Material.NETHER_STAR)
                        .name("&l&6Alliance")
                        .lore(new LoreBuilder()
                                .blank()
                                .line(alliance.toString())
                                .blank()
                                .line("Open the alliance menu")
                        )
                        .build()
                ).handler( e -> {
                    if(kingdomOfViewer.getRelations().getAlliance() != null){
                        new AllianceMenu(alliance).open(viewer);
                    }
                    else{
                        SoundEffect.FAIL.play(viewer);
                        new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                    }
                });
                break;

            case FRIENDLY:
                bp.slot(4).item(new ItemStackBuilder(Material.GREEN_CONCRETE)
                        .name("&l&6Relation: Friendly")
                        .build()
                );

                if(member.hasPermission(KingdomMemberRank.NOBEL)){
                    bp.slot(3).item(new ItemStackBuilder(Material.RED_DYE)
                            .name("&cWorsen relation")
                            .lore(new LoreBuilder()
                                    .blank()
                                    .line("worsen relation to " +
                                            kingdomOfViewer.getRelations().getRelationToKingdom(otherKingdom)
                                                    .getRelationLower().getDisplayName())
                            )
                            .build()
                    ).handler(e -> {
                        try {
                            kingdomOfViewer.getRelations().worsenRelation(otherKingdom);
                            new TextBuilder()
                                    .text("worsened relation to kingdom").gray()
                                    .sendTo(viewer);
                            new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                        } catch (KingdomException kingdomException) {
                            SoundEffect.FAIL.play(viewer);
                            new TextBuilder()
                                    .text(kingdomException.getMessage()).red()
                                    .sendTo(viewer);
                        }
                        new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                    });
                }

                if(member.hasPermission(KingdomMemberRank.ROYAL)){
                    KingdomAlliance al = otherKingdom.getRelations().getAlliance();
                    if(kingdomOfViewer.getRelations().getAllianceInvites().contains(otherKingdom)){ //invited by kingdom
                        bp.slot(5).item(new ItemStackBuilder(Material.PAPER)
                                .name("&aAccept alliance invitation")
                                .lore(new LoreBuilder()
                                        .blank()
                                        .line("your kingdom has been invited to join a alliance with this kingdom")
                                        .lineIf((al != null ? al.toString() : ""),
                                                otherKingdom.getRelations().getAlliance() != null)
                                        .blank()
                                        .line("click to accept invitation")
                                )
                                .build()
                        ).handler(e ->{
                            try {
                                kingdomOfViewer.getRelations().acceptAllianceInvite(otherKingdom);
                            } catch (KingdomException kingdomException) {
                                SoundEffect.FAIL.play(viewer);
                                new TextBuilder()
                                        .text(kingdomException.getMessage()).red()
                                        .sendTo(viewer);
                            }
                            new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                        });
                    }
                    else if(kingdomOfViewer.getRelations().getInvitedKingdoms().contains(otherKingdom)){ //already invited
                        bp.slot(5).item(new ItemStackBuilder(Material.BOOK)
                                .name("&aAlliance invitation send!")
                                .lore(new LoreBuilder()
                                        .blank()
                                        .line("this kingdom has been invited to join a alliance with your kingdom")
                                )
                                .build()
                        );
                    }
                    else{
                        bp.slot(5).item(new ItemStackBuilder(Material.WRITABLE_BOOK)
                                .name("&aSend Alliance invitation")
                                .lore(new LoreBuilder()
                                        .blank()
                                        .lineIf("send this kingdom a invitation to join a alliance with you",
                                                kingdomOfViewer.getRelations().getAlliance() != null)
                                        .lineIf("send this kingdom a invitation to join your current alliance",
                                                kingdomOfViewer.getRelations().getAlliance() == null)
                                )
                                .build()
                        ).handler(e -> {
                            try {
                                kingdomOfViewer.getRelations().sendAllianceInvite(otherKingdom);
                            } catch (KingdomException kingdomException) {
                                SoundEffect.FAIL.play(viewer);
                                new TextBuilder()
                                        .text(kingdomException.getMessage()).red()
                                        .sendTo(viewer);
                            }
                            new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                        });
                    }
                }

                break;

            case NEUTRAL:
                bp.slot(4).item(new ItemStackBuilder(Material.WHITE_CONCRETE)
                        .name("&l&6Relation: Neutral")
                        .build()
                );

                if(member.hasPermission(KingdomMemberRank.NOBEL)){
                    bp.slot(3).item(new ItemStackBuilder(Material.RED_DYE)
                            .name("&cWorsen relation")
                            .lore(new LoreBuilder()
                                    .blank()
                                    .line("worsen relation to " +
                                            kingdomOfViewer.getRelations().getRelationToKingdom(otherKingdom)
                                                    .getRelationLower().getDisplayName())
                            )
                            .build()
                    ).handler(e -> {
                        try {
                            kingdomOfViewer.getRelations().worsenRelation(otherKingdom);
                            new TextBuilder()
                                    .text("worsened relation to kingdom").gray()
                                    .sendTo(viewer);
                            new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                        } catch (KingdomException kingdomException) {
                            SoundEffect.FAIL.play(viewer);
                            new TextBuilder()
                                    .text(kingdomException.getMessage()).red()
                                    .sendTo(viewer);
                        }
                        new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                    });

                    bp.slot(5).item(new ItemStackBuilder(Material.LIME_DYE)
                            .name("&cImprove relation")
                            .lore(new LoreBuilder()
                                    .blank()
                                    .line("Improve relation to " +
                                            kingdomOfViewer.getRelations().getRelationToKingdom(otherKingdom)
                                                    .getRelationHigher().getDisplayName())
                            )
                            .build()
                    ).handler(e -> {
                        try {
                            kingdomOfViewer.getRelations().improveRelation(otherKingdom);
                            new TextBuilder()
                                    .text("improved relation to kingdom").gray()
                                    .sendTo(viewer);
                            new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                        } catch (KingdomException kingdomException) {
                            SoundEffect.FAIL.play(viewer);
                            new TextBuilder()
                                    .text(kingdomException.getMessage()).red()
                                    .sendTo(viewer);
                        }
                        new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                    });
                }

                break;

            case AGGRESSIVE:
                bp.slot(4).item(new ItemStackBuilder(Material.RED_CONCRETE)
                        .name("&l&6Relation: Aggressive")
                        .build()
                );

                if(member.hasPermission(KingdomMemberRank.ROYAL)){
                    bp.slot(3).item(new ItemStackBuilder(Material.RED_DYE)
                            .name("&cWorsen relation")
                            .lore(new LoreBuilder()
                                    .blank()
                                    .line("worsen relation to " +
                                            kingdomOfViewer.getRelations().getRelationToKingdom(otherKingdom)
                                                    .getRelationLower().getDisplayName())
                            )
                            .build()
                    ).handler(e -> {
                        try {
                            kingdomOfViewer.getRelations().worsenRelation(otherKingdom);
                            new TextBuilder()
                                    .text("worsened relation to kingdom").gray()
                                    .sendTo(viewer);
                            new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                        } catch (KingdomException kingdomException) {
                            SoundEffect.FAIL.play(viewer);
                            new TextBuilder()
                                    .text(kingdomException.getMessage()).red()
                                    .sendTo(viewer);
                        }
                        new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                    });
                }

                if(member.hasPermission(KingdomMemberRank.NOBEL)){
                    bp.slot(5).item(new ItemStackBuilder(Material.LIME_DYE)
                            .name("&cImprove relation")
                            .lore(new LoreBuilder()
                                    .blank()
                                    .line("Improve relation to " +
                                            kingdomOfViewer.getRelations().getRelationToKingdom(otherKingdom)
                                                    .getRelationHigher().getDisplayName())
                            )
                            .build()
                    ).handler(e -> {
                        try {
                            kingdomOfViewer.getRelations().improveRelation(otherKingdom);
                            new TextBuilder()
                                    .text("improved relation to kingdom").gray()
                                    .sendTo(viewer);
                            new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                        } catch (KingdomException kingdomException) {
                            SoundEffect.FAIL.play(viewer);
                            new TextBuilder()
                                    .text(kingdomException.getMessage()).red()
                                    .sendTo(viewer);
                        }
                        new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                    });
                }


                break;

            case ENEMY:
                bp.slot(4).item(new ItemStackBuilder(Material.TNT)
                        .name("&l&6Relation: Enemy")
                        .build()
                );

                bp.slot(5).item(new ItemStackBuilder(Material.LIME_DYE)
                        .name("&cImprove relation")
                        .lore(new LoreBuilder()
                                .blank()
                                .line("Improve relation to " +
                                        kingdomOfViewer.getRelations().getRelationToKingdom(otherKingdom)
                                                .getRelationHigher().getDisplayName())
                        )
                        .build()
                ).handler(e -> {
                    try {
                        kingdomOfViewer.getRelations().improveRelation(otherKingdom);
                        new TextBuilder()
                                .text("improved relation to kingdom").gray()
                                .sendTo(viewer);
                        new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                    } catch (KingdomException kingdomException) {
                        SoundEffect.FAIL.play(viewer);
                        new TextBuilder()
                                .text(kingdomException.getMessage()).red()
                                .sendTo(viewer);
                    }
                    new RelationMenu(this.kingdomOfViewer, otherKingdom).open(viewer);
                });

                break;
        }

        bp.slot(7).item(new ItemStackBuilder(Material.EMERALD)
            .name(relation.getColor() + otherKingdom.getName())
            .lore(new LoreBuilder()
                .blank()
                .line("relation to your kingdom: " + otherKingdom.getRelations().getRelationToKingdom(kingdomOfViewer).getDisplayName())
                .line(otherKingdom.toString())
            )
            .build()
        );

        bp.slot(8).item(new ItemStackBuilder(Material.BARRIER)
                .name("&l&4Return")
                .build()
        ).handler(e -> new KingdomRelationsMenu(kingdomOfViewer).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
