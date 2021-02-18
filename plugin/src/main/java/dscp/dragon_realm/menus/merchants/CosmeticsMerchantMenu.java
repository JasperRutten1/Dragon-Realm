package dscp.dragon_realm.menus.merchants;

import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.cosmetics.Cosmetic;
import org.bukkit.entity.Player;

public class CosmeticsMerchantMenu extends MerchantMenu{
    public CosmeticsMerchantMenu() {
        super("Cosmetics Merchant", 3);
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        addSellableItem(bp, 10, Cosmetic.TESTER_DIAMOND_SWORD.getCosmeticTicket(), new int[]{1000, 0, 0});
        addSellableItem(bp, 11, Cosmetic.ENERGY_DIAMOND_SWORD.getCosmeticTicket(), new int[]{1500, 0, 0});
        addSellableItem(bp, 12, Cosmetic.BURNING_NETHERITE_SWORD.getCosmeticTicket(), new int[]{3000, 0, 0});
        addSellableItem(bp, 13, Cosmetic.APPLE_IRON_SHOVEL.getCosmeticTicket(), new int[]{2000, 0, 0});
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
