package net.dohaw.claim;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrustCommand implements CommandExecutor {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /trust username

        if(args.length < 1){
            sender.sendMessage("You must include a user");
            return false;
        }
        //check if user is a player

        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;


        //grab chunk
        Chunk chunk = player.getLocation().getChunk();
        ChunkCoordinates chunkcoordinate = new ChunkCoordinates(chunk);

        //if chunk has not been claimed, you cant unclaim
        if(!CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.containsKey(chunkcoordinate)){
            sender.sendMessage("You are not standing in a claimed chunk");
            return false;
        }

        ClaimedChunkProperties chunkProperties = CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.get(chunkcoordinate);

        //if the player is not trusted or the permission is not set, then cannot trust player
        if(!chunkProperties.isPlayerTrusted(player)){
            sender.sendMessage("You do not have permission to trust other players");
            return false;
        }

        Player playerToTrust = Bukkit.getPlayer(args[0]);
        chunkProperties.addTrustedPlayer(playerToTrust);

        return true;
    }
}
