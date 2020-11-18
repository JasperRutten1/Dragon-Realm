package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KingdomOverviewMenu extends Container {

    private Kingdom kingdom;

    public KingdomOverviewMenu(Kingdom kingdom) {
        super("Kingdom Overview", 6);
        this.kingdom = kingdom;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        bp.slot(4).item(new ItemStackBuilder(Material.EMERALD)
            .name(kingdom.getName())
            .lore(new LoreBuilder()
                .blank()
                .line("Lorem ipsum")
            )
            .build()
        );

        KingdomMember kingdomKing = kingdom.getMembers().getKing();

        bp.slot(20).item(new ItemStackBuilder(Material.PLAYER_HEAD)
            .name("&6&lMembers")
            .setSkullOwner(kingdomKing != null ? kingdomKing.getPlayer() : null)
            .lore(new LoreBuilder()
                .blank()
                .line("View kingdom members")
                .line("members: " + kingdom.getMembers().getMembers().size())
            )
            .build()
        ).handler(e -> {
            new KingdomMembersMenu(this.kingdom).open(viewer);
        });

        bp.slot(22).item(new ItemStackBuilder(Material.SHIELD)
            .name("&6&lRelationships")
            .lore(new LoreBuilder()
                .blank()
                .line("View kingdom relationships")
            )
            .build()
        ).handler(e -> new KingdomRelationsMenu(this.kingdom).open(viewer));

        bp.slot(24).item(new ItemStackBuilder(Material.ACACIA_DOOR)
            .name("&6&lSettlements")
            .lore(new LoreBuilder()
                .blank()
                .line("View kingdom settlements")
            )
            .build()
        ).handler(e -> new KingdomSettlementMenu(kingdom).open(viewer));

        bp.slot(38).item(new ItemStackBuilder(Material.REDSTONE)
            .name("&6&lSettings")
            .lore(new LoreBuilder()
                .blank()
                .line("View kingdom settings")
                .line("&oComing soon")
            )
            .build()
        );

        bp.slot(38).item(new ItemStackBuilder(Material.IRON_SWORD)
            .name("&l&6Conflicts")
            .lore(new LoreBuilder()
                .blank()
                .line("View kingdom conflicts")
                .line("&oComing soon")
            )
            .build()
        );

        bp.slot(40).item(new ItemStackBuilder(Material.REDSTONE)
                .name("&l&6Settings")
                .lore(new LoreBuilder()
                        .blank()
                        .line("View kingdom settings")
                        .line("&oComing soon")
                )
                .build()
        );

        bp.slot(42).item(new ItemStackBuilder(Material.ENDER_CHEST)
            .name("&6&lVault")
            .lore(new LoreBuilder()
                .blank()
                .line("View kingdom vault")
                    .line("&oComing soon")
            )
            .build()
        );

        bp.slot(45).item(new ItemStackBuilder(Material.PAPER)
            .name("&6&lBug Report")
            .build()
        );

        bp.slot(53).item(new ItemStackBuilder(Material.BARRIER)
            .name("&c&lClose")
            .build()
        ).handler(e -> viewer.closeInventory());
    }

    @Override
    protected void onClose(Player viewer) {

    }

}
