package dscp.dragon_realm.menus.wallet;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.currency.DefaultCurrencyType;
import dscp.dragon_realm.currency.PlayerWallet;
import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WalletSettingsMenu extends Container {
    private Container previousMenu;

    public WalletSettingsMenu(Container previousMenu) {
        super("Wallet Settings", 3);
        this.previousMenu = previousMenu;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(viewer);
        PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(viewer);

        if(kingdom != null){
            if(previousMenu != null) previousMenu.open(viewer);
            else viewer.closeInventory();
        }

        //disclamer
        bp.slot(4).item(new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.DARK_PURPLE + "Disclaimer")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Changing currencies will result in")
                        .line("small rounding errors")
                )
                .build()
        );

        //coins
        bp.slot(12).item(currencyIcon(DefaultCurrencyType.COINS, wallet))
                .handler(e -> {
                    if(!isSelectedCurrencyType(DefaultCurrencyType.COINS, wallet)){
                        wallet.convertDefaultType(DefaultCurrencyType.COINS);
                        new WalletSettingsMenu(this.previousMenu).open(viewer);
                    }
                }
        );

        //drachma
        bp.slot(13).item(currencyIcon(DefaultCurrencyType.DRACHMA, wallet))
                .handler(e -> {
                    if(!isSelectedCurrencyType(DefaultCurrencyType.DRACHMA, wallet)){
                        wallet.convertDefaultType(DefaultCurrencyType.DRACHMA);
                        new WalletSettingsMenu(this.previousMenu).open(viewer);
                    }
                }
        );

        //obol
        bp.slot(14).item(currencyIcon(DefaultCurrencyType.OBOL, wallet))
                .handler(e -> {
                    if(!isSelectedCurrencyType(DefaultCurrencyType.OBOL, wallet)){
                        wallet.convertDefaultType(DefaultCurrencyType.OBOL);
                        new WalletSettingsMenu(this.previousMenu).open(viewer);
                    }
                }
        );

        //back
        bp.slot(26).item(new ItemStackBuilder(Material.BARRIER)
                .name("&c&lClose")
                .build()
        ).handler(e -> {
            if(previousMenu != null){
                previousMenu.open(viewer);
            }
            else viewer.closeInventory();
        });
    }

    @Override
    protected void onClose(Player viewer) {

    }

    private ItemStack currencyIcon(DefaultCurrencyType type, PlayerWallet wallet){
        ItemStackBuilder isb;
        LoreBuilder lb = new LoreBuilder().blank();

        lb.line(ChatColor.GRAY + "Currency type: " + type.getName());
        lb.line(ChatColor.GRAY + "Currency value: " + type.getValue());

        if(type == wallet.getCurrencyType()){
            isb = new ItemStackBuilder(Material.LIME_DYE);
            lb.line(ChatColor.GREEN + "" + ChatColor.BOLD + "Selected");
        }
        else{
            isb = new ItemStackBuilder(Material.GRAY_DYE);
            lb.line(ChatColor.GREEN + "" + ChatColor.BOLD + "Click to select");
        }
        isb.name(ChatColor.GOLD + type.getName());

        return isb.lore(lb).build();
    }

    private boolean isSelectedCurrencyType(DefaultCurrencyType type, PlayerWallet wallet){
        return type == wallet.getCurrencyType();
    }
}
