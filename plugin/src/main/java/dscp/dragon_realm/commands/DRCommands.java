package dscp.dragon_realm.commands;

import dscp.dragon_realm.commands.customCommands.*;
import dscp.dragon_realm.commands.customCommands.DragonProtect.*;
import dscp.dragon_realm.commands.customCommands.PlayerData.MinedCommand;
import dscp.dragon_realm.commands.customCommands.bounty.PlaceBountyCommand;
import dscp.dragon_realm.commands.customCommands.currency.CheckCurrencyCommand;
import dscp.dragon_realm.commands.customCommands.currency.GiveCoinsCommand;
import dscp.dragon_realm.commands.customCommands.essentials.*;
import dscp.dragon_realm.commands.customCommands.spiritSwords.SpiritSwordGive;
import dscp.dragon_realm.commands.customCommands.spiritSwords.SpiritSwordInfo;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

public enum DRCommands {
    KINGDOM(new KingdomCommand()),
    KINGDOM_CLAIM(new KingdomClaimCommand()),
    KINGDOM_UNCLAIM(new KingdomUnclaimCommand()),
    KINGDOM_CREATE(new KingdomCreateCommand()),
    KINGDOM_REMOVE(new KingdomRemoveCommand()),
    KINGDOM_MAP(new KingdomMapCommand()),

    KINGDOM_INVITE(new KingdomInviteCommand()),
    KINGDOM_INVITE_ACCEPT(new KingdomInviteAcceptCommand()),

    SETTLEMENT_CREATE(new KingdomCreateSettlementCommand()),

    FEED(new FeedCommand()),
    FLY(new FlyCommand()),
    FLY_SPEED(new FlySpeedCommand()),
    GOD(new GodCommand()),
    HEAL(new HealCommand()),

    DP_ASSIGN(new AssignChunkToProtectedZone()),
    DP_UNASSIGN(new UnAssignChunkFromProtectedZone()),
    DP_ZONE_CREATE(new CreateProtectedZone()),
    DP_ZONE_REMOVE(new RemoveProtectedZone()),
    DP_ZONE_LIST(new ListProtectedZones()),
    DP_EDIT(new ToggleEditMode()),
    SS_GIVE(new SpiritSwordGive()),
    SS_INFO(new SpiritSwordInfo()),

    PLAYER_DATA_MINED(new MinedCommand()),

    BOUNTY_PLACE(new PlaceBountyCommand()),

    CURRENCY_GIVE(new GiveCoinsCommand()),
    CURRENCY_CHECK(new CheckCurrencyCommand());

    private final CustomCommand command;

    DRCommands(CustomCommand command){
        this.command = command;
    }

    public CustomCommand getCommand(){
        return this.command;
    }

    static public CustomCommand getCommandFromArgs(String[] args){
        CustomCommand cmd = null;
        for(DRCommands drc : values()){
            CustomCommand command = drc.command;
            if(isCommand(args, command)){
                if(cmd == null){
                    cmd = command;
                    System.out.println(cmd);
                }
                else{
                    if(cmd.getCommandArgs().length < command.getCommandArgs().length){
                        cmd = command;
                        System.out.println(cmd);
                    }
                }
            }
        }
        return cmd;
    }

    static private boolean isCommand(String[] args, CustomCommand command){
        String[] commandArgs = command.getCommandArgs();
        if(args.length < commandArgs.length) return false;

        for(int i = 0 ; i < commandArgs.length ; i++){
            if(!(args[i].equals(commandArgs[i]))) return false;
        }
        return true;
    }

    static public void handleCommand(CommandSender sender, String commandName, String[] cmdArgs){
        String[] args = new String[1 + cmdArgs.length];
        args[0] = commandName;
        for(int i = 0 ; i < cmdArgs.length ; i++){
            args[1 + i] = cmdArgs[i];
        }

        System.out.println(Arrays.toString(args));
        CustomCommand command = getCommandFromArgs(args);
        if(command == null) return;

        HashMap<String, String> params = new HashMap<>();
        CustomCommand.CommandParams cmdParams = command.getParameters();
        System.out.println("command params: " + cmdParams.getParameters());
        for(int i = 0 ; i < cmdParams.count() ; i++){
            try{
                params.put(cmdParams.getParameters().get(i), args[command.getCommandArgs().length + i]);
            }
            catch(Exception ex){
                //
            }
        }
        System.out.println(params);
        command.runCommand(sender, params);
    }
}
