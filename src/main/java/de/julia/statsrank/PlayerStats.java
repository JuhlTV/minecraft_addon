package de.julia.statsrank;

public final class PlayerStats {
    private int kills;
    private int deaths;
    private int brokenBlocks;
    private int placedBlocks;

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
    }

    public int getBrokenBlocks() {
        return brokenBlocks;
    }

    public void addBrokenBlock() {
        brokenBlocks++;
    }

    public int getPlacedBlocks() {
        return placedBlocks;
    }

    public void addPlacedBlock() {
        placedBlocks++;
    }

    public void setKills(int kills) {
        this.kills = Math.max(0, kills);
    }

    public void setDeaths(int deaths) {
        this.deaths = Math.max(0, deaths);
    }

    public void setBrokenBlocks(int brokenBlocks) {
        this.brokenBlocks = Math.max(0, brokenBlocks);
    }

    public void setPlacedBlocks(int placedBlocks) {
        this.placedBlocks = Math.max(0, placedBlocks);
    }
}