package dscp.dragon_realm.commands;

import dscp.dragon_realm.commands.customCommands.*;
import dscp.dragon_realm.commands.customCommands.essentials.FlyCommand;
import dscp.dragon_realm.commands.customCommands.essentials.FlySpeedCommand;
import dscp.dragon_realm.commands.customCommands.essentials.HealCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum DRCommands {
    KINGDOM("kingdom", new String[]{"overview"}, new KingdomCommand("dscp.dr.kingdom.default")),
    KINGDOM_MAP("kingdom", new String[]{"map"}, new KingdomMapCommand("dscp.dr.kingdom.default")),
    KINGDOM_CLAIM("kingdom", new String[]{"claim"}, new KingdomClaimCommand("dscp.dr.kingdom.default")),
    KINGDOM_CREATE("kingdom", new String[]{"create"}, new KingdomCreateCommand("dscp.dr.kingdom.create")),
    KINGDOM_SETTLEMENT_CREATE("kingdom", new String[]{"settlement", "create"}, new KingdomCreateSettlementCommand("dscp.dr.kingdom.default")),
    KINGDOM_SETTLEMENT_REMOVE("kingdom", new String[]{"settlement", "remove"}, new KingdomCreateSettlementCommand("dscp.dr.kingdom.default")),
    KINGDOM_REMOVE("kingdom", new String[]{"remove"}, new KingdomRemoveCommand("dscp.dr.kingdom.default")),
    KINGDOM_INVITE("kingdom", new String[]{"invite"}, new KingdomInviteCommand("dscp.dr.kingdom.default")),
    Kingdom_INVITEACCEPT("kingdom", new String[]{"acceptinvite"}, new KingdomInviteAcceptCommand("dscp.dr.kingdom.default")),
    KINGDOM_GIVE_COINS("kingdom", new String[]{"coins", "give"} , new KingdomGiveCoinsCommand("dscp.dr.admin")),
    KINGDOM_VAULT_COINS("kingdom", new String[]{"vault", "coins"}, new KingdomVaultCoinsCommand("dscp.dr.kingdom.default")),
    KINGDOM_UNCLAIM("kingdom", new String[]{"unclaim"}, new KingdomUnclaimCommand("dscp.dr.kingdom.default")),

    HEAL("heal", new String[]{}, new HealCommand("dscp.dr.mod")),
    FEED("feed", new String[]{}, new HealCommand("dscp.dr.mod")),
    FLY("fly", new String[]{}, new FlyCommand("dscp.dr.fly")),
    FLY_SPEED("flyspeed", new String[]{}, new FlySpeedCommand("dscp.dr.fly"));

    String commandName;
    String[] args;
    CustomCommand command;

    DRCommands(String commandName, String[] args, CustomCommand command){
        if(commandName == null)  throw new IllegalArgumentException("Command name can not be null.");
        if(args == null) throw new IllegalArgumentException("Arguments can not be null.");
        if(command == null) throw new IllegalArgumentException("Command can not be null.");

        this.commandName = commandName;
        this.args = args;
        this.command = command;
    }

    public CustomCommand getCommand() {
        return command;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isCommand(String commandName, String[] args){
        if(commandName == null)  throw new IllegalArgumentException("Command name can not be null.");
        if(args == null) throw new IllegalArgumentException("Arguments can not be null.");

        if(!this.commandName.equals(commandName)) return false;
        try{
            for(int i = 0 ; i < this.args.length ; i++){
                if(!this.args[i].equals(args[i])) return false;
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static void handleCommand(CommandSender sender, String commandName, String[] args){
        if(sender == null) throw new IllegalArgumentException("Sender can not be null.");
        if(commandName == null)  throw new IllegalArgumentException("Command name can not be null.");
        if(args == null) throw new IllegalArgumentException("Arguments can not be null.");

        try{
            for(DRCommands drc : values()){
                if(drc.isCommand(commandName, args)){
                    drc.getCommand().executeCommand(sender, commandName, args).sendExceptionsOrMessages();
                    return;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            if(sender instanceof Player){
                Player player = (Player) sender;
                player.sendMessage(ChatColor.RED + "Missing arguments, type '/kingdom help' for more information.");
            }
            return;
        }

        if(sender instanceof Player){
            Player player = (Player) sender;
            player.sendMessage(ChatColor.RED + "Unknown command, type '/kingdom help' for more information.");
        }
    }
}
