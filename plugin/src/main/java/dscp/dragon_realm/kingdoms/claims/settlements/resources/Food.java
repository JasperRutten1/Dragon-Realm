package dscp.dragon_realm.kingdoms.claims.settlements.resources;

import org.bukkit.Material;

public enum Food {
    WHEAT(Material.WHEAT),
    CARROT(Material.CARROTS),
    Potato(Material.POTATOES);

    Material blockType;

    Food(Material blockType){
        this.blockType = blockType;
    }

    public Material getBlockType() {
        return blockType;
    }
}
