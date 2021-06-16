package net.dohaw.claim;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FlagMenu extends Menu implements Listener {
    public FlagMenu(JavaPlugin plugin, String menuTitle, int numSlots) {
        super(plugin, null, menuTitle, numSlots);
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player p) {
        Map<Permission, ItemStack> map = new HashMap<>();
        map.put(Permission.BUILD, createGuiItem(Material.GRASS_BLOCK, "Allow building inside this chunk", new ArrayList<String>()));
        map.put(Permission.BREAK, createGuiItem(Material.DIAMOND_PICKAXE, "Allow breaking blocks inside this chunk", new ArrayList<String>()));
        map.put(Permission.USE_FLINT_AND_STEEL, createGuiItem(Material.FLINT_AND_STEEL, "Allow flint and steel usage inside this chunk", new ArrayList<String>()));
        map.put(Permission.SPAWN_CHICKENS, createGuiItem(Material.CHICKEN_SPAWN_EGG, "Allow chicken spawning inside this chunk", new ArrayList<String>()));
        map.put(Permission.SPEAK, createGuiItem(Material.OAK_SIGN, "Allow speaking inside this chunk", new ArrayList<String>()));
        map.put(Permission.OPEN_CHESTS, createGuiItem(Material.CHEST, "Allow opening chests inside this chunk", new ArrayList<String>()));
        map.put(Permission.INTERACT, createGuiItem(Material.CRAFTING_TABLE, "Allow interaction with blocks inside this chunk", new ArrayList<String>()));
        Chunk chunk = p.getLocation().getChunk();
        ChunkCoordinates chunkCoordinates = new ChunkCoordinates(chunk);
        ClaimedChunkProperties chunkProperties = CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.get(chunkCoordinates);

        for(Permission permission : map.keySet()){
            if(chunkProperties.hasPermission(permission)){
                ItemMeta itemMeta = map.get(permission).getItemMeta();
                itemMeta.setDisplayName("Disa" + itemMeta.getDisplayName().substring(1));
                map.get(permission).setItemMeta(itemMeta);
            }
            inv.setItem(permission.ordinal(),map.get(permission));
        }
    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Chunk chunk = player.getLocation().getChunk();
        ChunkCoordinates chunkCoordinates = new ChunkCoordinates(chunk);
        ClaimedChunkProperties chunkProperties = CLAIMED_CHUNKS.ALL_CLAIMED_CHUNKS.get(chunkCoordinates);
        ItemStack itemClicked = e.getCurrentItem();
        Inventory clickedInventory = e.getClickedInventory();
        Inventory topInventory = player.getOpenInventory().getTopInventory();

        //if the inventory was not clicked
        if (clickedInventory == null) {
            return;
        }
        //if the inventory that was clicked was not top inventory
        if (!topInventory.equals(inv) || !clickedInventory.equals(topInventory)){
            return;
        }
        //if no item was clicked or cursor is null
        if (itemClicked == null && e.getCursor() == null) {
            return;
        }
        e.setCancelled(true);
        ItemMeta clickedItemMeta = itemClicked.getItemMeta();
        Permission permission = null;
        switch(itemClicked.getType()){
            case GRASS_BLOCK:
                permission = Permission.BUILD;
                break;
            case DIAMOND_PICKAXE:
                permission = Permission.BREAK;
                break;
            case FLINT_AND_STEEL:
                permission = Permission.USE_FLINT_AND_STEEL;
                break;
            case CHICKEN_SPAWN_EGG:
                permission = Permission.SPAWN_CHICKENS;
                break;
            case OAK_SIGN:
                permission = Permission.SPEAK;
                break;
            case CRAFTING_TABLE:
                permission = Permission.INTERACT;
                break;
        }
        //check if permission is already enabled or not
        if(chunkProperties.hasPermission(permission)){
            String modified = "A" + clickedItemMeta.getDisplayName().substring(4);
            chunkProperties.removePermission(permission);
            clickedItemMeta.setDisplayName(modified);
        } else {
            String modified = "Disa" + clickedItemMeta.getDisplayName().substring(1);
            chunkProperties.addPermissions(permission);
            clickedItemMeta.setDisplayName(modified);
        }
        itemClicked.setItemMeta(clickedItemMeta);


    }
}
