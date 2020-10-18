package dscp.dragon_realm.customEnchants.events;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.customEnchants.CustomEnchants;
import dscp.dragon_realm.customEnchants.EnchantException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class EnchantsEvents implements Listener {

    private Map<Player, Long> enderEdgeTimeOutMap;
    private Map<Material, Material> materialMap;
    private static Map<Integer, String> enchantLevelMap;
    private Map<Player, Double> capacitorChargeMap;
    private Map<Player, Long> lightningChargeMap;

    public EnchantsEvents(){
        this.enderEdgeTimeOutMap = new HashMap<>();

        enchantLevelMap = new HashMap<>();
        enchantLevelMap.put(1, "I");
        enchantLevelMap.put(2, "II");
        enchantLevelMap.put(3, "III");
        enchantLevelMap.put(4, "IV");
        enchantLevelMap.put(5, "V");

        this.materialMap = new HashMap<>();
        materialMap.put(Material.IRON_ORE, Material.IRON_INGOT);
        materialMap.put(Material.GOLD_ORE, Material.GOLD_INGOT);

        this.capacitorChargeMap = new HashMap<>();
        this.lightningChargeMap = new HashMap<>();
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerAttackPlayer(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        ItemStack damagerWeapon = damager.getInventory().getItemInMainHand();
        ItemStack damagedWeapon = damaged.getInventory().getItemInMainHand();

        ItemMeta damagerWeaponMeta = damagerWeapon.getItemMeta();
        ItemMeta damagedWeaponMeta = damagedWeapon.getItemMeta();

        // maverick's nudes enchantment effect
        if((!damagerWeapon.getType().equals(Material.AIR)) && damagerWeaponMeta != null && damagerWeaponMeta.hasEnchant(CustomEnchants.MAVERICKS_NUDES)){
            if(damaged.getInventory().getArmorContents().length == 0) return;

            if(Math.random() < CustomEnchants.MAVERICS_NUDES_CHANCE){

                ItemStack armorPiece = getRandomArmorPiece(damaged);
                if(armorPiece == null) return;
                removeArmorPiece(damaged, armorPiece);
                Dragon_Realm_API.giveItem(damaged, armorPiece);

                damaged.sendMessage(ChatColor.GRAY + "Maverick compelled you to share your noods");
            }
        }
        // repulsor charge on damage effect
        else if((!damagedWeapon.getType().equals(Material.AIR)) && damagedWeaponMeta != null && damagedWeaponMeta.hasEnchant(CustomEnchants.REPULSOR)){
            this.capacitorChargeMap.putIfAbsent(damaged, 0.0);
            this.capacitorChargeMap.put(damaged, Math.min(CustomEnchants.REPULSOR_MAX_CHARGE, this.capacitorChargeMap.get(damaged) + (event.getDamage() / 2)));
        }


    }

    @EventHandler
    public void EntityDamagedByPlayer(EntityDamageByEntityEvent event) throws EnchantException {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;

        System.out.println("player hit living entity");

        Player player = (Player) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();

        ItemStack damagerWeapon = player.getInventory().getItemInMainHand();
        ItemMeta damagerWeaponMeta = damagerWeapon.getItemMeta();

        // lightning link effect
        if(Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).hasEnchant(CustomEnchants.LIGHTNING_LINK)){
            System.out.println(lightningChargeMap);
            if(!lightningChargeMap.containsKey(player) || lightningChargeMap.get(player) < System.currentTimeMillis()){
                lightningChargeMap.remove(player);
                return;
            }

            player.sendMessage(ChatColor.DARK_GRAY + "you hear the air crackle with energy");

            int range = CustomEnchants.LIGHTNING_LINK_RANGE;
            ArrayList<Entity> inRangeEntities = new ArrayList<>(entity.getWorld().getNearbyEntities(entity.getLocation(), range, range, range, LivingEntity.class::isInstance));

            LightningLink.lightningChainDamage(event.getDamage(), LightningLink.createLink(entity, 5, null));
            event.setDamage(0);
            lightningChargeMap.remove(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerInteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null) return;

        ItemMeta meta = item.getItemMeta();

        if(meta == null) return;

        // ender edge enchantment effect
        if(meta.hasEnchant(CustomEnchants.ENDER_EDGE)){
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Long timeout = enderEdgeTimeOutMap.get(player);
                if(!(timeout == null || System.currentTimeMillis() >= timeout)) {
                    player.sendMessage(ChatColor.GRAY + "still in cool down, " + ((timeout - System.currentTimeMillis())/1000) + " seconds");
                    return;
                }

                Block block = player.getTargetBlockExact(CustomEnchants.ENDER_EDGE_DISTANCE);
                if(block == null || block.getType().equals(Material.AIR)){
                    player.sendMessage(ChatColor.GRAY + "out of range");
                    return;
                }

                int y = block.getLocation().add(0, 1, 0 ).getBlockY();
                Block blockAbove1 = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
                Block blockAbove2 = block.getWorld().getBlockAt(block.getX(), y + 1, block.getZ());
                World world = block.getWorld();

                if(!(blockAbove1.getType().equals(Material.AIR) && blockAbove2.getType().equals(Material.AIR))){
                    player.sendMessage(ChatColor.GRAY + "invalid space");
                    return;
                }

                enderEdgeTimeOutMap.put(player, System.currentTimeMillis() + CustomEnchants.ENDER_EDGE_COOLDOWN);

                Location tpTo = new Location(world, block.getX(), y, block.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                world.spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, 1, 0), 200, 0.2, 0.4, 0.2);
                world.spawnParticle(Particle.DRAGON_BREATH, tpTo.add(0, 1, 0), 200, 0.2, 0.4, 0.2);

                player.teleport(tpTo);
                player.sendMessage(ChatColor.GRAY + "wooosh");
            }
        }

        // repulsor effect
        else if(meta.hasEnchant(CustomEnchants.REPULSOR)){
            if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
            if(this.capacitorChargeMap.get(player) == null || this.capacitorChargeMap.get(player) < 15){
                player.sendMessage(ChatColor.GRAY + "not charged enough");
                return;
            }

            for(Player p : getPlayersInRange(5, player)){
                p.setVelocity(getRepulseVector(player, p, (this.capacitorChargeMap.get(player) / 25) ));
            }
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation().add(0, 1, 0), 200);
            this.capacitorChargeMap.put(player, 0.0);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void clickOnItemWithBookEvent(InventoryClickEvent event){
        if(!event.isLeftClick()) return;
        if(event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) return;
        if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if(!(event.getCursor().getType().equals(Material.BOOK))) return;
        if(!(event.getInventory().getHolder() instanceof Player)) return;
        if(event.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)) return;
        if(event.getCursor().getEnchantments().size() == 0) return;

        ItemStack item = event.getCurrentItem();
        ItemMeta itemMeta = item.getItemMeta();
        ItemStack book = event.getCursor();
        ItemMeta bookMeta = book.getItemMeta();
        Player player = (Player) event.getWhoClicked();
        Map<Enchantment, Integer> enchantsOnBook = book.getEnchantments();
        Map<Enchantment, Integer> enchantsOnItem = item.getEnchantments();

        //check if item already has other custom enchantment
        if(getCustomEnchantsFromMap(getDuplicatesEnchantments(enchantsOnBook, enchantsOnItem)).size() > 1) return;

        if(containsOnlyCustomEnchants(enchantsOnBook)){
            try{
                //check for incompatible item type
                if(!checkValidItemMaterialForAll(item, enchantsOnBook)){
                    return;
                }

                //check for conflicting enchantments
                if(hasConflictingEnchants(item, enchantsOnBook)) return;

                //check if duplicates are all at max level
                if(checkAllAtMaxLevel(getDuplicatesEnchantments(enchantsOnBook, enchantsOnItem))){
                    return;
                }

                // adding enchantments
                addEnchantsToMeta(itemMeta, combineEnchants(enchantsOnBook, enchantsOnItem));
                item.setItemMeta(itemMeta);

                event.setCurrentItem(item);
                player.setItemOnCursor(null);
                event.setCancelled(true);
            }
            catch (Exception e){
                player.sendMessage(e.getMessage());
            }
        }
        else{
            player.sendMessage("book can only have custom enchantments");
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerUsesAnvil(PrepareAnvilEvent event){
        if(event.getResult() == null || event.getResult().getType().equals(Material.AIR)) return;

        ItemStack leftItem = event.getInventory().getItem(0);
        ItemStack rightItem = event.getInventory().getItem(1);
        ItemStack resultItem = event.getResult();

        if(leftItem == null) return;
        if(rightItem == null) return;

        Map<Enchantment, Integer> customEnchantsLeft = getCustomEnchantsFromMap(leftItem.getEnchantments());
        Map<Enchantment, Integer> customEnchantsRight = getCustomEnchantsFromMap(rightItem.getEnchantments());

        //check if enchantments on the items don't conflict
        if(hasConflictingEnchants(resultItem, rightItem.getEnchantments()) || hasConflictingEnchants(resultItem, leftItem.getEnchantments())){
            event.setResult(null);
            return;
        }

        Map<Enchantment, Integer> customEnchants = combineEnchants(customEnchantsLeft, customEnchantsRight);
        Map<Enchantment, Integer> vanillaEnchants = resultItem.getEnchantments();
        Map<Enchantment, Integer> totalEnchants = new HashMap<>();
        totalEnchants.putAll(customEnchants);
        totalEnchants.putAll(vanillaEnchants);

        ItemMeta resultMeta = resultItem.getItemMeta();
        addEnchantsToMeta(resultMeta, totalEnchants);
        resultItem.setItemMeta(resultMeta);

        event.setResult(resultItem);
    }

    @EventHandler
    public void playerBreakBlockEvent(BlockBreakEvent event){
        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOOK)) return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Map<Enchantment, Integer> enchantments = player.getInventory().getItemInMainHand().getEnchantments();

        //auto smelt enchantment effect
        if(enchantments.containsKey(CustomEnchants.AUTO_SMELT)){
            if(!materialMap.containsKey(block.getType())) return;
            event.setDropItems(false);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(materialMap.get(block.getType())));
        }

        //telepathy enchantment effect
        else if(enchantments.containsKey(CustomEnchants.TELEPATHY)){
            if(player.getGameMode() == GameMode.CREATIVE) return;
            Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
            if(drops.isEmpty()) return;
            Map<Integer, ItemStack> overflow = player.getInventory().addItem(drops.iterator().next());
            for(int spot : overflow.keySet()){
                block.getWorld().dropItemNaturally(block.getLocation(), overflow.get(spot));
            }
            event.setDropItems(false);
        }
    }

    @EventHandler
    public void arrowShootEvent(EntityShootBowEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getProjectile() instanceof Arrow)) return;
        if(event.getBow() == null) return;

        Arrow arrow = (Arrow) event.getProjectile();
        ItemStack bow = event.getBow();

        if(bow.getEnchantments().containsKey(CustomEnchants.VELOCITY)){
            if(getCustomEnchantsFromMap(bow.getEnchantments()).size() > 1) return;
            for(Map.Entry<Enchantment, Integer> entry : getCustomEnchantsFromMap(bow.getEnchantments()).entrySet()){
                arrow.setVelocity(arrow.getVelocity().multiply(( (double) 1 + (( (double) entry.getValue() / 10)))));
            }
        }
    }

    @EventHandler
    public void playerHitByArrow(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Arrow)) return;
        if(!(event.getEntity() instanceof Player)) return;
        System.out.println("player got hit by arrow");

        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();

        // arrow deflect effect
        if(getTotalLevelOfEnchantmentFromArmor(player, CustomEnchants.DEFLECT) > 0){
            if(getTotalLevelOfEnchantmentFromArmor(player, CustomEnchants.DEFLECT) < Math.floor(Math.random()*100)) return;
            Arrow newArrow = player.getWorld().spawnArrow(arrow.getLocation(), arrow.getVelocity().normalize().multiply(-1), (float) arrow.getVelocity().length(), 0);
            arrow.remove();
            event.setCancelled(true);
            newArrow.setVelocity(arrow.getVelocity().multiply(-0.5));
            player.sendMessage(ChatColor.GRAY + "deflected arrow");
        }
    }

    @EventHandler
    public void playerHitByLightning(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof LightningStrike)) return;
        if(!(event.getEntity() instanceof Player)) return;
        System.out.println("player hit by lightning");

        Player player = (Player) event.getEntity();
        LightningStrike lightning = (LightningStrike) event.getDamager();

        // lightning link charge
        if(Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).hasEnchant(CustomEnchants.LIGHTNING_LINK)){
            lightningChargeMap.put(player, System.currentTimeMillis() + CustomEnchants.LIGHTNING_LINK_MAX_CHARGE_TIME);
            player.sendMessage(ChatColor.GRAY + "sword is charged with lightning");
        }
    }


    private static List<Player> getPlayersInRange(double range, Player player){
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<Player> inRangePlayers = new ArrayList<>();
        for(Player p : players){
            if(!player.equals(p)){
                if(player.getLocation().distance(p.getLocation()) <= range){
                    inRangePlayers.add(p);
                }
            }
        }
        return inRangePlayers;
    }

    private static Vector getRepulseVector(Player user, Player other, double speedMultiplier){
        Vector direction = new Vector(other.getLocation().getX() - user.getLocation().getX(), 0.3333, other.getLocation().getZ() - user.getLocation().getZ());
        double x = direction.getX();
        double z = direction.getZ();
        double y = 0.3333;
        double multiplier = Math.sqrt((speedMultiplier*speedMultiplier) / (x*x + y*y + z*z)); // get a constant that, when multiplied by the vector, results in the speed we want
        return new Vector(x, y, z).multiply(multiplier).setY(y);
    }


    private ItemStack getRandomArmorPiece(Player player){
        List<ItemStack> armorPieces = new ArrayList<>();
        armorPieces.add(player.getInventory().getHelmet());
        armorPieces.add(player.getInventory().getChestplate());
        armorPieces.add(player.getInventory().getLeggings());
        armorPieces.add(player.getInventory().getBoots());

        List<ItemStack> duplicate = new ArrayList<>(armorPieces);
        for(ItemStack item : duplicate){
            if(item == null || item.getType().equals(Material.AIR)) armorPieces.remove(item);
        }
        if(armorPieces.size() == 0) return null;
        return armorPieces.get((int) Math.floor(Math.random() * armorPieces.size()));
    }

    private void removeArmorPiece(Player player, ItemStack item){
        PlayerInventory inventory = player.getInventory();
        if (Objects.equals(player.getInventory().getHelmet(), item)) {
            inventory.setHelmet(null);
        }
        else if (Objects.equals(player.getInventory().getChestplate(), item)) {
            inventory.setChestplate(null);
        }
        else if (Objects.equals(player.getInventory().getLeggings(), item)) {
            inventory.setLeggings(null);
        }
        else if (Objects.equals(player.getInventory().getBoots(), item)) {
            inventory.setBoots(null);
        }
    }

    private double getTotalLevelOfEnchantmentFromArmor(Player player, Enchantment enchantment){
        double count = 0;
        for(ItemStack armorPiece : player.getInventory().getArmorContents()){
            if(armorPiece != null && armorPiece.getItemMeta() != null) {
                if(armorPiece.getItemMeta().hasEnchant(enchantment)){
                    count = count + armorPiece.getItemMeta().getEnchantLevel(enchantment);
                }
            }

        }
        return count;
    }


    private boolean hasConflictingEnchants(ItemStack item, Enchantment enchantment){
        for(Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()){
            if(enchantment.conflictsWith(entry.getKey())) return true;
        }
        return false;
    }

    private boolean hasConflictingEnchants(ItemStack item, Map<Enchantment, Integer> enchantMap){
        for(Map.Entry<Enchantment, Integer> entry : enchantMap.entrySet()){
            if(hasConflictingEnchants(item, entry.getKey())) return true;
        }
        return false;
    }

    private boolean isCustomEnchant(Enchantment enchantment){
        return CustomEnchants.getCustomEnchants().contains(enchantment);
    }

    private boolean containsOnlyCustomEnchants(Map<Enchantment, Integer> enchantMap){
        for (Map.Entry<Enchantment, Integer> entry : enchantMap.entrySet()){
            if(!(isCustomEnchant(entry.getKey()))) return false;
        }
        return true;
    }

    private boolean containsCustomEnchants(Map<Enchantment, Integer> enchantMap){
        for (Map.Entry<Enchantment, Integer> entry : enchantMap.entrySet()){
            if(isCustomEnchant(entry.getKey())) return true;
        }
        return false;
    }

    private boolean containsEnchant(Enchantment enchantment, Map<Enchantment, Integer> map){
        for(Map.Entry<Enchantment, Integer> entry : map.entrySet()){
            if(entry.getKey().equals(enchantment)) return true;
        }
        return false;
    }

    private Map<Enchantment, Integer> getCustomEnchantsFromMap(Map<Enchantment, Integer> enchantmentMap){
        Map<Enchantment, Integer> customEnchants = new HashMap<>();
        for(Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()){
            if(CustomEnchants.getCustomEnchants().contains(entry.getKey())) customEnchants.put(entry.getKey(), entry.getValue());
        }
        return customEnchants;
    }

    private boolean checkValidItemMaterial(ItemStack item, Enchantment enchantment){
        return enchantment.getItemTarget().includes(item.getType());
    }

    private boolean checkValidItemMaterialForAll(ItemStack item, Map<Enchantment, Integer> enchantments){
        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()){
            if(!checkValidItemMaterial(item, entry.getKey())){
                return false;
            }
        }
        return true;
    }

    private Map<Enchantment, Integer> getDuplicatesEnchantments(Map<Enchantment, Integer> bookEnchants , Map<Enchantment, Integer> itemEnchants){
        Map<Enchantment, Integer> duplicates = new HashMap<>();
        for(Map.Entry<Enchantment, Integer> entry : bookEnchants.entrySet()){
            if(itemEnchants.containsKey(entry.getKey())){
                duplicates.put(entry.getKey(), itemEnchants.get(entry.getKey()));
            }
        }
        return duplicates;
    }

    private boolean isAtMaxLevel(Integer level, Enchantment enchantment){
        return level >= enchantment.getMaxLevel();
    }

    private boolean checkAllAtMaxLevel(Map<Enchantment, Integer> enchantments){
        if(enchantments.isEmpty()) return false;
        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()){
            if(!isAtMaxLevel(entry.getValue(), entry.getKey())) return false;
        }
        return true;
    }

    private Map<Enchantment, Integer> combineEnchants(Map<Enchantment, Integer> bookEnchants , Map<Enchantment, Integer> itemEnchants){
        Map<Enchantment, Integer> combinedEnchants = new HashMap<>(itemEnchants);
        if(itemEnchants.isEmpty()) return bookEnchants;

        for(Map.Entry<Enchantment, Integer> entry : bookEnchants.entrySet()){
            if(combinedEnchants.containsKey(entry.getKey())){
                int newLevel = Math.min(entry.getKey().getMaxLevel(), entry.getValue() + combinedEnchants.get(entry.getKey()));
                combinedEnchants.remove(entry.getKey());
                combinedEnchants.put(entry.getKey(), newLevel);
            }
            else {
                combinedEnchants.put(entry.getKey(), entry.getValue());
            }
        }

        return combinedEnchants;
    }

    private static void clearEnchantmentsFromMeta(ItemMeta meta){
        List<Enchantment> enchantments = new ArrayList<>();
        for(Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()){
            enchantments.add(entry.getKey());
        }
        for(Enchantment enchantment : enchantments){
            meta.removeEnchant(enchantment);
        }
    }

    private static void addEnchantsToMeta(ItemMeta meta, Map<Enchantment, Integer> enchantments){
        clearEnchantmentsFromMeta(meta);
        List<String> newLore = new ArrayList<>();
        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()){
            meta.addEnchant(entry.getKey(), entry.getValue(), false);
            if(CustomEnchants.getCustomEnchants().contains(entry.getKey())){
                newLore.add(ChatColor.GRAY + entry.getKey().getName() + ((entry.getKey().getMaxLevel() == 1) ? ""
                        : " " + ((enchantLevelMap.containsKey(entry.getValue())) ? enchantLevelMap.get(entry.getValue()) : entry.getValue()) ));
            }
        }

        meta.setLore(newLore);
    }
}