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
                .line("Dylanchill was also here.")
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
                        .line("Governor: " + (settlement.getGovernor() != null
                                ? settlement.getGovernor().getPlayer().getName() : "none"))
                    )
                    .build()
                ).handler(e -> new SettlementMenu(settlement).open(viewer));
            } catch (IndexOutOfBoundsException ex) {
                bp.slot(i).item(new ItemStackBuilder(Material.BARRIER)
                    .name("&c&lUnclaimed Settlement")
                    .build()
                );
            }
        }


    }

    @Override
    protected void onClose(Player viewer) {

    }

}
