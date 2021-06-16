package net.dohaw.claim;

import org.bukkit.Chunk;

import java.util.Objects;

public class ChunkCoordinates {
    private int x;
    private int z;

    public ChunkCoordinates(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public ChunkCoordinates(Chunk chunk){
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkCoordinates that = (ChunkCoordinates) o;
        return x == that.x && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
