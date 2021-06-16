package net.dohaw.claim;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class EventListener implements Listener {

    private boolean isEventCancelled(Player player, Chunk chunk, Permission permission){
        ChunkCoordinates chunkCoordinates = new ChunkCoordinates(chunk);

        //check if chunk is claimed
        if(!CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.containsKey(chunkCoordinates)){
            return false;
        }

        ClaimedChunkProperties chunkProperties = CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.get(chunkCoordinates);
        return !chunkProperties.hasPermission(permission) && !chunkProperties.isPlayerTrusted(player);
    }

    //build event
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        Player player = e.getPlayer();

        Chunk chunk = e.getBlock().getChunk();

        if(isEventCancelled(player,chunk,Permission.BUILD)){
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
        Player player = e.getPlayer();

        Chunk chunk = e.getBlock().getChunk();
        if(isEventCancelled(player,chunk,Permission.BREAK)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if(isEventCancelled(player,player.getLocation().getChunk(),Permission.SPEAK)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEggHatch(ProjectileHitEvent e){
        //if its not a chicken
        if(!e.getEntity().getType().equals(EntityType.EGG)){
            return;
        }

        //check if thrower is player
        if(!(e.getEntity().getShooter() instanceof Player)){
            return;
        }
        Player player = (Player) e.getEntity().getShooter();
        Chunk chunk = e.getEntity().getLocation().getChunk();
        if(isEventCancelled(player,chunk,Permission.SPAWN_CHICKENS)){
            e.getEntity().remove();
        }

    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        if(e.isBlockInHand() && e.getClickedBlock() != null && !e.getClickedBlock().getBlockData().getMaterial().isInteractable()){
            return;
        }
        Chunk chunk = e.getClickedBlock().getChunk();
        Permission permission;
        //flint and steel
        if(e.getItem() != null && e.getItem().getType().equals(Material.FLINT_AND_STEEL)){
            permission = Permission.USE_FLINT_AND_STEEL;
        }
        else if(e.getClickedBlock().getType().equals(Material.CHEST)){
            permission = Permission.OPEN_CHESTS;
        }
        else{
            permission = Permission.INTERACT;
        }
        if(isEventCancelled(player,chunk,permission)){
            e.setCancelled(true);
        }

    }


}
