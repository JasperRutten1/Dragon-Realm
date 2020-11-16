package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KingdomSettlementMenu extends Container {

    private Kingdom kingdom;

    public KingdomSettlementMenu(Kingdom kingdom) {
        super("Kingdom Settlements", 5);
        this.kingdom = kingdom;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        List<Settlement> settlements = kingdom.getClaim().getSettlements();

        bp.slot(4).item(new ItemStackBuilder(Material.EMERALD)
            .name(kingdom.getName())
            .lore(new LoreBuilder()
                .blank()
                .line("Lorem ipsum")
            )
            .build()
        );

        for(int i = 19, j = 0; j < 4; i += 2, j++) {
            try {
                Settlement settlement = settlements.get(j);
                bp.slot(i).item(new ItemStackBuilder(settlement.getLevel().getGuiItem())
                    .name(settlement.getName())
                    .lore(new LoreBuilder()
                        .line(settlement.getLevel().getName())
                    )
                    .build()
                );
            } catch (ArrayIndexOutOfBoundsException ex) {
                bp.slot(i).item(new ItemStackBuilder(Material.BARRIER)
                    .name("&c&lUnclaimed Settlement")
                    .build()
                );
            }
        }

        bp.slot(44).item(new ItemStackBuilder(Material.BARRIER)
            .name("&c&lReturn")
            .build()
        ).handler(e -> new KingdomOverviewMenu(kingdom).open(viewer));
    }

    @Override
    protected void onClose(Player viewer) {

    }

}
