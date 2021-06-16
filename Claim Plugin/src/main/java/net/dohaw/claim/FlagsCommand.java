package net.dohaw.claim;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlagsCommand implements CommandExecutor {
    ClaimPlugin claimPlugin;
    public FlagsCommand(ClaimPlugin claimPlugin){
        this.claimPlugin = claimPlugin;
    }

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
        //if chunk has not been claimed, you cant unclaim
        if(!CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.containsKey(chunkcoordinate)){
            sender.sendMessage("Chunk is not claimed");
            return false;
        }
        ClaimedChunkProperties chunkProperties = CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.get(chunkcoordinate);
        //Check to see if this is the owner of the chunk, if not then cannot unclaim
        if(!player.getUniqueId().equals(chunkProperties.getOwner().getUniqueId())){
            sender.sendMessage("You are not the owner of this chunk");
            return false;
        }

        FlagMenu flagMenu = new FlagMenu(claimPlugin,"Flags Menu",9);
        flagMenu.initializeItems(player);
        flagMenu.openInventory(player);

        return true;
    }
}
