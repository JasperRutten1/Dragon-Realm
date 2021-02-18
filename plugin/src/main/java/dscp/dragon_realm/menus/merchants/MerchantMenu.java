package dscp.dragon_realm.menus.merchants;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.container.Blueprint;
import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.currency.DefaultCurrencyType;
import dscp.dragon_realm.currency.PlayerWallet;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class MerchantMenu extends Container {
    public MerchantMenu(String title, int rows) {
        super(title, rows);
    }

    @Override
    abstract protected void onOpen(Player viewer, Blueprint bp);

    @Override
    abstract protected void onClose(Player viewer);

    public void addSellableItem(Blueprint bp, int index, ItemStack item, int[] currency){
        ItemStackBuilder stackBuilder = new ItemStackBuilder(item);
        stackBuilder.lore(new LoreBuilder(item)
                .blank()
                .line(ChatColor.GOLD + "Prices: ")
                .line("Coins : " + currency[0])
                .lineIf("Ender dust: " + currency[1], currency[1] > 0)
                .lineIf("Dragon Pearl: " + currency[2], currency[2] > 0)
        );

        bp.slot(index).item(stackBuilder.build())
                .handler(e -> {
                    Player viewer = (Player) e.getWhoClicked();
                    PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(viewer);
                    if(wallet.hasEnoughDefault(currency[0], DefaultCurrencyType.COINS)
                            && wallet.hasEnderPowder(currency[1])
                            && wallet.hasDragonPearl(currency[2])) {
                        if(viewer.getInventory().addItem(item).isEmpty()){
                            //item give
                            wallet.changeDefaultCurrency(-currency[0]);
                            wallet.changeEnderPowder(-currency[1]);
                            wallet.changeDragonPearl(-currency[2]);
                            SoundEffect.SUCCESS.play(viewer);
                            this.close();
                            this.open(viewer);
                        }
                        else{
                            //item not given (inv full)
                            viewer.sendMessage(ChatColor.RED + "inventory full");
                            SoundEffect.FAIL.play(viewer);
                        }
                    }
                    else {
                        viewer.sendMessage(ChatColor.RED + "You can not afford this cosmetic");
                        SoundEffect.FAIL.play(viewer);
                    }
                });
    }
}
