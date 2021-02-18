package dscp.dragon_realm.cosmetics;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public enum Cosmetic {
    TESTER_DIAMOND_SWORD("Test", new LoreBuilder()
            .blank()
            .line("For tester that helped in the alpha week"),
            Material.DIAMOND_SWORD, 1, ChatColor.BLUE),
    ENERGY_DIAMOND_SWORD("Energy Blade", new LoreBuilder(),
            Material.DIAMOND_SWORD, 2, ChatColor.GOLD),
    BURNING_NETHERITE_SWORD("Burning Blade", new LoreBuilder(),
            Material.NETHERITE_SWORD, 1, ChatColor.RED),
    APPLE_IRON_SHOVEL("Apple Spade", new LoreBuilder(),
            Material.IRON_SHOVEL, 1, ChatColor.WHITE);

    private String name;
    private LoreBuilder lore;
    private Material material;
    private int modelData;
    private ChatColor nameColor;

    public static NamespacedKey modelDataKey = new NamespacedKey(Dragon_Realm.instance, "modelData");
    public static NamespacedKey materialKey = new NamespacedKey(Dragon_Realm.instance, "material");

    Cosmetic(String name, LoreBuilder lore, Material material, int modelData, ChatColor nameColor){
        this.name = ChatColor.RESET + "" + nameColor + name;
        lore.blank()
            .line("this can be applied to:")
            .line(material.name())
            .blank()
            .line("(applying will overwrite existing cosmetics)");
        this.lore = lore;
        this.material = material;
        this.modelData = modelData;
        this.nameColor = nameColor;
    }

    public static Cosmetic getCosmetic(String name){
        for(Cosmetic cosmetic : values()){
            if(cosmetic.name.equalsIgnoreCase(name)) return cosmetic;
        }
        return null;
    }

    public ChatColor getNameColor() {
        return nameColor;
    }

    public ItemStack getCosmeticTicket(){
        return new ItemStackBuilder(Material.PAPER)
                .name(this.name)
                .lore(this.lore)
                .addPersistentData(modelDataKey, PersistentDataType.INTEGER, modelData)
                .addPersistentData(materialKey, PersistentDataType.STRING, material.name())
                .build();
    }

    public static boolean isCosmeticItem(ItemStack stack){
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;

        if(!meta.getPersistentDataContainer().has(modelDataKey, PersistentDataType.INTEGER)) return false;
        return meta.getPersistentDataContainer().has(materialKey, PersistentDataType.STRING);
    }

    public static Cosmetic getCosmetic(ItemStack stack){
        if(!isCosmeticItem(stack)) return null;
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;

        int modelData = meta.getPersistentDataContainer().get(modelDataKey, PersistentDataType.INTEGER);
        String materialName = meta.getPersistentDataContainer().get(materialKey, PersistentDataType.STRING);

        for(Cosmetic cosmetic : values()){
            if(cosmetic.material.name().equals(materialName) && cosmetic.modelData == modelData) return cosmetic;
        }
        return null;
    }
}
