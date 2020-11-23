package dscp.dragon_realm.discord;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import org.bukkit.Bukkit;

import java.io.IOException;

public class DiscordKingdomWebhook {
    Kingdom kingdom;

    public DiscordKingdomWebhook(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
    }

    public void sendKingdomCreateMessage() throws IOException{
        DiscordWebhook startUpWebhook = new DiscordWebhook(ToDiscordEvents.WEBHOOK_LINK);
        startUpWebhook.setUsername("Dragon-Realm");
        startUpWebhook.setContent("**" + kingdom.getMembers().getKing().getPlayer().getName()
                + "** created the kingdom **" + kingdom.getName() + "**");
        startUpWebhook.execute();
    }

    public void sendMemberJoinWebhook(KingdomMember member)throws IOException {
        if(member == null) throw new IllegalArgumentException("member can't be null");

        DiscordWebhook startUpWebhook = new DiscordWebhook(ToDiscordEvents.WEBHOOK_LINK);
        startUpWebhook.setUsername("Dragon-Realm");
        startUpWebhook.setContent("**" + member.getPlayer().getName()
                + "** joined the kingdom of **" + kingdom.getName() + "**");
        startUpWebhook.execute();
    }
}
