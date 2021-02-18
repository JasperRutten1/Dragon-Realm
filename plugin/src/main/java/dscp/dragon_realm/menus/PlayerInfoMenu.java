package dscp.dragon_realm.menus;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.currency.DefaultCurrencyType;
import dscp.dragon_realm.currency.PlayerWallet;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.menus.wallet.PlayerWalletMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerInfoMenu extends Container {
    private OfflinePlayer player;
    private Container previousMenu;

    public PlayerInfoMenu(OfflinePlayer player, Container previousMenu) {
        super(player.getName(), 3);
        this.player = player;
        this.previousMenu = previousMenu;
    }

    @Override
    protected void onOpen(Player viewer, Blueprint bp) {
        boolean isViewer = player.getUniqueId().equals(viewer.getUniqueId());
        Kingdom playerKingdom = Kingdom.getKingdomFromPlayer(player);
        Kingdom viewerKingdom = Kingdom.getKingdomFromPlayer(viewer);
        PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(player);

        //player head information
        bp.slot(4).item(new ItemStackBuilder(Material.PLAYER_HEAD)
                .setSkullOwner(player)
                .name(ChatColor.GOLD + player.getName())
                .lore(new LoreBuilder()
                        .blank()
                        .line(playerKingdom == null
                                ? "Not part of a kingdom."
                                : "Member of the " + ChatColor.GOLD + playerKingdom.getName() + ChatColor.GRAY + " kingdom." )
                )
                .build()
        );

        //kingdom info
        if(playerKingdom != null){
            KingdomMemberRank rank = playerKingdom.getMembers().getMember(player).getRank();
            bp.slot(13).item(new ItemStackBuilder(Material.GOLDEN_HELMET)
                    .name(ChatColor.GOLD + playerKingdom.getName())
                    .lore(new LoreBuilder()
                            .blank()
                            .line(playerKingdom.equals(viewerKingdom)
                                    ? rank.getDisplayName()
                                    : "member of kingdom." )
                            .blank()
                            .line(ChatColor.GREEN + "Click to View Kingdom.")
                    )
                    .build()
            ).handler(e -> {
                new KingdomOverviewMenu(playerKingdom, this).open(viewer);
            });
        }
        else{
            bp.slot(13).item(new ItemStackBuilder(Material.CHAINMAIL_HELMET)
                    .name(ChatColor.GRAY + "" + ChatColor.BOLD + "Not in kingdom")
                    .build()
            );
        }

        //wallet
        if(viewer.equals(player)){
            bp.slot(14).item(new ItemStackBuilder(Material.FURNACE_MINECART)
                    .name(ChatColor.GOLD + "Wallet")
                    .lore(new LoreBuilder()
                            .blank()
                            .line(ChatColor.GOLD + "" + wallet.getDefaultCurrency() + " " + wallet.getCurrencyType().getName()
                                    + " (" + wallet.getCurrencyType().toCoins(wallet.getDefaultCurrency()) + ")")
                            .line(ChatColor.GRAY + "not implemented currency")
                            .line(ChatColor.GRAY + "not implemented currency")
                    )
                    .build()
            )
            .handler(e -> {
                if(viewer.equals(player)){
                    new PlayerWalletMenu(viewer, this).open(viewer);
                }
            });
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

    @Override
    protected void onClose(Player viewer) {

    }
}
