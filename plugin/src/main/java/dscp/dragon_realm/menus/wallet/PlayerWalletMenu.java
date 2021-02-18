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
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerWalletMenu extends Container {
    private Player player;
    private Container previousMenu;

    public PlayerWalletMenu(Player player, Container previousMenu) {
        super("Your Wallet", 3);
        this.player = player;
        this.previousMenu = previousMenu;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        if(player.equals(viewer)){
            PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(player);
            Kingdom kingdom = Kingdom.getKingdomFromPlayer(viewer);

            //default currency
            bp.slot(10).item(new ItemStackBuilder(Material.SUNFLOWER)
                    .name(ChatColor.GOLD + "" + wallet.getDefaultCurrency() + " " + wallet.getCurrencyType().getName())
                    .lore(new LoreBuilder()
                            .blank()
                            .line(ChatColor.GRAY + "Default Currency")
                            .line(ChatColor.GRAY + "Used for trading and needed for upgrading your kingdom")
                            .blank()
                            .line(ChatColor.GRAY + "Currency type: " + wallet.getCurrencyType().getName())
                            .line(ChatColor.GRAY + "Currency value: " + wallet.getCurrencyType().getValue())
                            .lineIf(ChatColor.GRAY + "Value in Coins: " + wallet.getCurrencyType().toCoins(wallet.getDefaultCurrency()),
                                    wallet.getCurrencyType() != DefaultCurrencyType.COINS)
                    )
                    .customModelData(1111111)
                    .build()
            );

            //ender dust
            bp.slot(11).item(new ItemStackBuilder(Material.BLAZE_POWDER)
                    .name(ChatColor.DARK_AQUA + "" + wallet.getEnderPowder() + " Ender Powder")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("Rare Recourse")
                            .line("Used for special items and upgrading your kingdom")
                            .blank()
                            .line("You can only hold " + wallet.getEnderPowder_max() + " Ender Powder in your wallet")
                            .line("This recourse can not be shared with other players")
                    )
                    .customModelData(1111111)
                    .build()
            );

            //dragon pearl
            bp.slot(12).item(new ItemStackBuilder(Material.ENDER_EYE)
                    .name(ChatColor.DARK_RED + "" + wallet.getDragonPearl() + " Dragon Pearl")
                    .lore(new LoreBuilder()
                            .blank()
                            .line("Exotic Recourse")
                            .line("Used for exotic items and upgrading your kingdom")
                            .blank()
                            .line("You can only hold " + wallet.getDragonPearl_max() + " Dragon Pearls in your wallet")
                            .line("This recourse can not be shared with other players")
                    )
                    .customModelData(1111111)
                    .build()
            );

            //drop coins
            bp.slot(14).item(new ItemStackBuilder(Material.HOPPER)
                    .name(ChatColor.GOLD + "Drop Coins")
                    .lore(new LoreBuilder()
                            .blank()
                            .line(ChatColor.GRAY + "Drop coins that other players can pick up")
                    )
                    .build()
            ).handler(e -> {
                if (viewer.equals(player)) {
                    new DropCoinsMenu(this).open(viewer);
                }
            });

            //bank
            bp.slot(15).item(new ItemStackBuilder(Material.DISPENSER)
                    .name(ChatColor.GRAY + "" + ChatColor.BOLD + "Not Implemented")
                    .build()
            );

            //currency settings
            if(kingdom == null){
                bp.slot(16).item(new ItemStackBuilder(Material.REDSTONE)
                        .name(ChatColor.GREEN + "Wallet Settings")
                        .lore(new LoreBuilder()
                                .blank()
                                .line(ChatColor.GRAY + "Open Wallet settings menu")
                        )
                        .build()
                ).handler(e -> {
                    if(viewer.equals(player)){
                        new WalletSettingsMenu(this).open(viewer);
                    }
                });
            }
            else{
                bp.slot(16).item(new ItemStackBuilder(Material.REDSTONE)
                        .name(ChatColor.GREEN + "Kingdom Currency Settings")
                        .build()
                );
            }

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
    }

    @Override
    protected void onClose(Player viewer) {

    }
}
