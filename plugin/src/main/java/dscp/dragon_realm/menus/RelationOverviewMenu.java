package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.relations.Relation;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class RelationOverviewMenu extends Container {
    private Kingdom kingdom;
    private Relation relation;

    public RelationOverviewMenu(Kingdom kingdom, Relation relation) {
        super(relation.getRelName() + " kingdoms", 6);
        this.kingdom = kingdom;
        this.relation = relation;
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

        List<Kingdom> kingdomsWithRelation = this.kingdom.getRelations().getKingdomsWithRelation(this.relation);

        for(int i = 9, j = 0 ; j < kingdomsWithRelation.size() && i < 45; i++, j++){
            Kingdom kingdom = kingdomsWithRelation.get(j);
            bp.slot(i).item(new ItemStackBuilder(Material.GREEN_CONCRETE)
                    .name("&l&6" + kingdom.getName())
                    .lore(new LoreBuilder()
                            .blank()
                            .line(relation.getDisplayName())
                            .line(kingdom.toString())
                    )
                    .build()
            ).handler(e -> new RelationMenu(this.kingdom, kingdom).open(viewer));
        }

        bp.slot(53).item(new ItemStackBuilder(Material.BARRIER)
                .name("&l&4Return")
                .build()
        ).handler(e -> new KingdomRelationsMenu(this.kingdom).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
