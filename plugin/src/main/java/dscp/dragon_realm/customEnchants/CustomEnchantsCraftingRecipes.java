package dscp.dragon_realm.customEnchants;

import dscp.dragon_realm.Dragon_Realm_API;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CustomEnchantsCraftingRecipes {

    public static void loadCraftingRecipes(){
        createRecipeForBook("telepathy-enchatned-book", CustomEnchants.TELEPATHY, new Material[]{
                Material.ENDER_PEARL, Material.ENDER_CHEST, Material.ENDER_PEARL,
                Material.ENDER_PEARL, Material.BOOK, Material.ENDER_PEARL,
                Material.ENDER_PEARL, Material.ENDER_CHEST, Material.ENDER_PEARL
        });

        createRecipeForBook("ender-edge-enchanted-book", CustomEnchants.ENDER_EDGE, new Material[]{
                Material.ENDER_EYE, Material.END_CRYSTAL, Material.ENDER_EYE,
                Material.NETHER_STAR, Material.BOOK, Material.NETHER_STAR,
                Material.ENDER_EYE, Material.END_CRYSTAL, Material.ENDER_EYE
        });

        createRecipeForBook("auto-smelt-enchanted-book", CustomEnchants.AUTO_SMELT, new Material[]{
                Material.BLAZE_POWDER, Material.DIAMOND_PICKAXE, Material.BLAZE_POWDER,
                Material.GHAST_TEAR, Material.BOOK, Material.GHAST_TEAR,
                Material.BLAZE_POWDER, Material.DIAMOND_PICKAXE, Material.BLAZE_POWDER
        });

        createRecipeForBook("velocity-enchanted-book", CustomEnchants.VELOCITY, new Material[]{
                Material.EMERALD, Material.BOW, Material.EMERALD,
                Material.SUGAR, Material.BOOK, Material.SUGAR,
                Material.EMERALD, Material.BOW, Material.EMERALD
        });

        createRecipeForBook("deflect-enchanted-book", CustomEnchants.DEFLECT, new Material[]{
                Material.IRON_BARS, Material.SHIELD, Material.IRON_BARS,
                Material.SLIME_BALL, Material.BOOK, Material.SLIME_BALL,
                Material.IRON_BARS, Material.SHIELD, Material.IRON_BARS
        });
    }

    public static void createRecipeForBook(String key, Enchantment enchantment, Material[] materials){
        if(key == null) throw new IllegalArgumentException("Key can not be null.");
        if(enchantment == null) throw new IllegalArgumentException("Enchantment can not be null.");
        if(materials == null) throw new IllegalArgumentException("Materials can not be null.");
        if(!(materials.length == 9)) throw new IllegalArgumentException("Material array must contain 9 materials.");
        for(Material material : materials){
            if(material == null) throw new IllegalArgumentException("Materials can not contain null values.");
        }

        ItemStack book = new ItemStack(Material.BOOK);
        CustomEnchants.enchantItem(book, enchantment);
        NamespacedKey nsk = NamespacedKey.minecraft(key);
        ShapedRecipe recipe = new ShapedRecipe(nsk, book);

        recipe.shape("ABC", "DEF", "GHI");
        recipe.setIngredient('A', materials[0]);
        recipe.setIngredient('B', materials[1]);
        recipe.setIngredient('C', materials[2]);
        recipe.setIngredient('D', materials[3]);
        recipe.setIngredient('E', materials[4]);
        recipe.setIngredient('F', materials[5]);
        recipe.setIngredient('G', materials[6]);
        recipe.setIngredient('H', materials[7]);
        recipe.setIngredient('I', materials[8]);
        Bukkit.addRecipe(recipe);

        Dragon_Realm_API.consoleLog("Registered new Custom Crafting Recipe.");
    }
}
