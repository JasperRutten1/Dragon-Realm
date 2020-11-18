package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.relations.KingdomAlliance;
import dscp.dragon_realm.kingdoms.relations.Relation;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KingdomRelationsMenu extends Container {
    private Kingdom kingdom;

    public KingdomRelationsMenu(Kingdom kingdom){
        super("Relations", 3);
        this.kingdom = kingdom;
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

        if(kingdom.getRelations().getAlliance() != null){
            KingdomAlliance alliance = kingdom.getRelations().getAlliance();
            bp.slot(10).item(new ItemStackBuilder(Material.NETHER_STAR)
                    .name("&l&6Alliance")
                    .lore(new LoreBuilder()
                            .blank()
                            .line(alliance.toString())
                            .blank()
                            .line("Open the Alliance menu")
                    )
                    .build()
            ).handler(e -> {
                if(kingdom.getRelations().getAlliance() != null){
                    new AllianceMenu(alliance).open(viewer);
                }
                else{
                    SoundEffect.FAIL.play(viewer);
                    new KingdomRelationsMenu(this.kingdom).open(viewer);
                }
            });
        }
        else{
            bp.slot(10).item(new ItemStackBuilder(Material.NETHER_STAR)
                    .name("&l&6Alliance")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("&cNot in alliance")
                    )
                    .build()
            );
        }

        if(kingdom.getRelations().getAllianceInvites().size() > 0){
            bp.slot(11).item(new ItemStackBuilder(Material.PAPER)
                    .name("&l&6Alliance Invitations")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("invitations: " + kingdom.getRelations().getAllianceInvites().size())
                    )
                    .build()
            ).handler(e -> {
                new AllianceInvitationMenu(this.kingdom).open(viewer);
            });
        }
        else{
            bp.slot(11).item(new ItemStackBuilder(Material.PAPER)
                    .name("&l&6Alliance Invitations")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("no invitations")
                    )
                    .build()
            );
        }

        if(kingdom.getRelations().getKingdomsWithRelation(Relation.FRIENDLY).size() > 0){
            bp.slot(13).item(new ItemStackBuilder(Material.GREEN_CONCRETE)
                    .name("&l&6Friendly kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("overview of friendly kingdoms")
                    )
                    .build()
            ).handler(e -> {
                new RelationOverviewMenu(this.kingdom, Relation.FRIENDLY).open(viewer);
            });
        }
        else{
            bp.slot(13).item(new ItemStackBuilder(Material.GREEN_CONCRETE)
                    .name("&l&6Friendly kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("no friendly kingdoms")
                    )
                    .build()
            );
        }

        if(kingdom.getRelations().getKingdomsWithRelation(Relation.NEUTRAL).size() > 0){
            bp.slot(14).item(new ItemStackBuilder(Material.WHITE_CONCRETE)
                    .name("&l&6Neutral kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("overview of neutral kingdoms")
                    )
                    .build()
            ).handler(e -> {
                new RelationOverviewMenu(this.kingdom, Relation.NEUTRAL).open(viewer);
            });
        }
        else{
            bp.slot(14).item(new ItemStackBuilder(Material.WHITE_CONCRETE)
                    .name("&l&6Neutral kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("no neutral kingdoms")
                    )
                    .build()
            );
        }

        if(kingdom.getRelations().getKingdomsWithRelation(Relation.AGGRESSIVE).size() > 0){
            bp.slot(15).item(new ItemStackBuilder(Material.RED_CONCRETE)
                    .name("&l&6Aggressive kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("overview of aggressive kingdoms")
                    )
                    .build()
            ).handler(e -> {
                new RelationOverviewMenu(this.kingdom, Relation.AGGRESSIVE).open(viewer);
            });
        }
        else{
            bp.slot(15).item(new ItemStackBuilder(Material.RED_CONCRETE)
                    .name("&l&6Aggressive kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("no aggressive kingdoms")
                    )
                    .build()
            );
        }

        if(kingdom.getRelations().getKingdomsWithRelation(Relation.ENEMY).size() > 0){
            bp.slot(16).item(new ItemStackBuilder(Material.TNT)
                    .name("&l&6Enemy kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("overview of enemy kingdoms")
                    )
                    .build()
            ).handler(e -> {
                new RelationOverviewMenu(this.kingdom, Relation.ENEMY).open(viewer);
            });
        }
        else{
            bp.slot(16).item(new ItemStackBuilder(Material.TNT)
                    .name("&l&6Enemy kingdoms")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("no enemy kingdoms")
                    )
                    .build()
            );
        }

        bp.slot(26).item(new ItemStackBuilder(Material.BARRIER)
                .name("&l&4Return")
                .build()
        ).handler(e -> new KingdomOverviewMenu(this.kingdom).open(viewer));

    }

    @Override
    protected void onClose(Player viewer) {

    }
}
