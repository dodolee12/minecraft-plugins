package net.dohaw.claim;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClaimedChunkProperties {
    private Set<UUID> trustedPlayers;
    private Set<Permission> permissions;
    private Player Owner;
    public ClaimedChunkProperties(Player player){
        trustedPlayers = new HashSet<>();
        permissions = new HashSet<>();
        addTrustedPlayer(player);
        Owner = player;
    }

    public void addTrustedPlayer(Player player){
        trustedPlayers.add(player.getUniqueId());
    }

    public void removeTrustedPlayer(Player player){
        trustedPlayers.remove(player.getUniqueId());
    }

    public boolean isPlayerTrusted(Player player){
        return trustedPlayers.contains(player.getUniqueId());
    }

    public void addPermissions(Permission permission){
        permissions.add(permission);
    }

    public void removePermission(Permission permission){
        permissions.remove(permission);
    }

    public boolean hasPermission(Permission permission){
        return permissions.contains(permission);
    }

    public Player getOwner(){
        return Owner;
    }
}
