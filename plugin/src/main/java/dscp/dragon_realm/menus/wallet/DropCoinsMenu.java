package dscp.dragon_realm.menus.wallet;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.currency.DroppedCoins;
import dscp.dragon_realm.currency.PlayerWallet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DropCoinsMenu extends Container {
    private Container previousMenu;
    private int coins;

    public DropCoinsMenu(Container previousMenu) {
        super("Drop Coins", 3);
        this.previousMenu = previousMenu;
        this.coins = 0;
    }

    public DropCoinsMenu(Container previousMenu, int coins){
        this(previousMenu);
        this.coins = coins;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(viewer);

        bp.slot(4).item(new ItemStackBuilder(Material.FURNACE_MINECART)
                .name(ChatColor.GOLD + "Wallet")
                .lore(new LoreBuilder()
                        .blank()
                        .line(ChatColor.GOLD + "" + wallet.getDefaultCurrency() + " " + wallet.getCurrencyType().getName()
                                + " (" + wallet.getCurrencyType().toCoins(wallet.getDefaultCurrency()) + ")")
                        .line(ChatColor.GRAY + "not implemented currency")
                        .line(ChatColor.GRAY + "not implemented currency")
                )
                .build()
        );

        bp.slot(10).item(new ItemStackBuilder(Material.EMERALD)
                .name(ChatColor.GREEN + "1000 Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Add 1000 coins to the amount you will drop")
                        .line(wallet.getCurrencyType().fromCoins(1000) + " " + wallet.getCurrencyType().getName())
                )
                .build()
        ).handler(e -> {
            new DropCoinsMenu(this.previousMenu, this.coins + 1000).open(viewer);
        });

        bp.slot(11).item(new ItemStackBuilder(Material.LIME_CONCRETE)
                .name(ChatColor.GREEN + "100 Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Add 100 coins to the amount you will drop")
                        .line(wallet.getCurrencyType().fromCoins(100) + " " + wallet.getCurrencyType().getName())
                )
                .build()
        ).handler(e -> {
            new DropCoinsMenu(this.previousMenu, this.coins + 100).open(viewer);
        });

        bp.slot(12).item(new ItemStackBuilder(Material.LIME_STAINED_GLASS)
                .name(ChatColor.GREEN + "10 Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Add 10 coins to the amount you will drop")
                        .line(wallet.getCurrencyType().fromCoins(10) + " " + wallet.getCurrencyType().getName())
                )
                .build()
        ).handler(e -> {
            new DropCoinsMenu(this.previousMenu, this.coins + 10).open(viewer);
        });

        bp.slot(13).item(new ItemStackBuilder(Material.SUNFLOWER)
                .name(ChatColor.GOLD + "" + this.coins + " Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("The amount of coins you are going to drop")
                        .line(wallet.getCurrencyType().fromCoins(this.coins) + " " + wallet.getCurrencyType().getName())
                )
                .build()
        );

        bp.slot(14).item(new ItemStackBuilder(Material.RED_STAINED_GLASS)
                .name(ChatColor.GREEN + "-10 Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Remove 10 coins to the amount you will drop")
                        .line(wallet.getCurrencyType().fromCoins(10) + " " + wallet.getCurrencyType().getName())
                )
                .build()
        ).handler(e -> {
            if((this.coins - 10) < 0) new DropCoinsMenu(this.previousMenu).open(viewer);
            else new DropCoinsMenu(this.previousMenu, this.coins - 10).open(viewer);
        });

        bp.slot(15).item(new ItemStackBuilder(Material.RED_CONCRETE)
                .name(ChatColor.GREEN + "-100 Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Remove 100 coins to the amount you will drop")
                        .line(wallet.getCurrencyType().fromCoins(100) + " " + wallet.getCurrencyType().getName())
                )
                .build()
        ).handler(e -> {
            if((this.coins - 100) < 0) new DropCoinsMenu(this.previousMenu).open(viewer);
            else new DropCoinsMenu(this.previousMenu, this.coins - 100).open(viewer);
        });

        bp.slot(16).item(new ItemStackBuilder(Material.REDSTONE_BLOCK)
                .name(ChatColor.GREEN + "-1000 Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Remove 1000 coins to the amount you will drop")
                        .line(wallet.getCurrencyType().fromCoins(1000) + " " + wallet.getCurrencyType().getName())
                )
                .build()
        ).handler(e -> {
            if((this.coins - 1000) < 0) new DropCoinsMenu(this.previousMenu).open(viewer);
            else new DropCoinsMenu(this.previousMenu, this.coins - 1000).open(viewer);
        });

        bp.slot(22).item(new ItemStackBuilder(Material.HOPPER)
                .name(ChatColor.GOLD + "Drop " + this.coins + " Coins")
                .lore(new LoreBuilder()
                        .blank()
                        .line("Drop " + this.coins + " on the ground.")
                )
                .build()
        ).handler(e -> {
            if(wallet.getCurrencyType().toCoins(wallet.getDefaultCurrency()) >= this.coins && this.coins > 0){
                DroppedCoins.dropCoinsNaturally(viewer.getLocation(), this.coins);
                wallet.changeDefaultCurrency(-this.coins);
                new DropCoinsMenu(this.previousMenu, this.coins).open(viewer);
            }
            else{
                viewer.sendMessage(ChatColor.RED + "Can not drop more coins than you have");
            }
        });

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
}
