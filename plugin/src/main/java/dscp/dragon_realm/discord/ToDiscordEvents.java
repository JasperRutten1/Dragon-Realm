package dscp.dragon_realm.discord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class ToDiscordEvents implements Listener {
    public final static String WEBHOOK_LINK = "https://discordapp.com/api/webhooks/780450634998218754/HNZpq4Ah05V78JOjKaxx8hp3bpfYXWqSUFkEAycV4Ueb4S4DBOb3O-Pl2FEjyddYSSea";

    @EventHandler(priority = EventPriority.LOW)
    public void onChatMessage(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        //webhook
        try{
            DiscordWebhook webhook = new DiscordWebhook(WEBHOOK_LINK);
            webhook.setContent("**" + player.getName() + "**: " + event.getMessage());
            webhook.setUsername("Dragon-Realm");
            webhook.execute();
        }
        catch (IOException e){
            System.out.println("exception in sending webhook to discord");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        try{
            DiscordWebhook webhook = new DiscordWebhook(WEBHOOK_LINK);
            webhook.setContent("**" + player.getName() + "** joined the game");
            webhook.setUsername("Dragon-Realm");
            webhook.execute();
        }
        catch (IOException e){
            System.out.println("exception in sending webhook to discord");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        try{
            DiscordWebhook webhook = new DiscordWebhook(WEBHOOK_LINK);
            webhook.setContent("**" + player.getName() + "** left the game");
            webhook.setUsername("Dragon-Realm");
            webhook.execute();
        }
        catch (IOException e){
            System.out.println("exception in sending webhook to discord");
        }
    }
}
