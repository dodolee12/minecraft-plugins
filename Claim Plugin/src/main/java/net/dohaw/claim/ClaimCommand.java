package net.dohaw.claim;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {
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
        //if sender is not a player then dont do anything
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        //grab chunk
        Chunk chunk = player.getLocation().getChunk();
        ChunkCoordinates chunkcoordinate = new ChunkCoordinates(chunk);

        //check if chunk is already claimed
        if(CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.containsKey(chunkcoordinate)){
            sender.sendMessage("This chunk is already claimed");
            return false;
        }

        //otherwise claim the chunk and add command sender as trusted player
        ClaimedChunkProperties newChunkProperties = new ClaimedChunkProperties(player);
        CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.put(chunkcoordinate,newChunkProperties);
        sender.sendMessage("You have claimed the chunk");

        

        return true;
    }
}
