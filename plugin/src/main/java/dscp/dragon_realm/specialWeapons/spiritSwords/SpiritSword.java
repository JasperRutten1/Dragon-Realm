package dscp.dragon_realm.specialWeapons.spiritSwords;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.ObjectIO;
import dscp.dragon_realm.builders.BookBuilder;
import dscp.dragon_realm.builders.TextBuilder;
import dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active.SpiritSwordActiveAbility;
import dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive.SpiritSwordPassiveAbility;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class SpiritSword implements Serializable {
    private static final long serialVersionUID = 8467593395445768824L;

    private String name;
    private SpiritElement element;
    private double experience;
    private double level;
    private UUID playerUUID;
    private UUID swordUUID;
    private SpiritSwordPassiveAbility passive;
    private SpiritSwordActiveAbility active;

    private static Map<UUID, ArrayList<SpiritSword>> soulBindMap;

    public final static NamespacedKey NAME = new NamespacedKey(Dragon_Realm.instance, "spirit-sword-name");
    public final static NamespacedKey ELEMENT = new NamespacedKey(Dragon_Realm.instance, "spirit-sword-element");
    public final static NamespacedKey EXPERIENCE = new NamespacedKey(Dragon_Realm.instance, "spirit-sword-experience");
    public final static NamespacedKey LEVEL = new NamespacedKey(Dragon_Realm.instance, "spirit-sword-level");
    public final static NamespacedKey PLAYER_UUID = new NamespacedKey(Dragon_Realm.instance, "spirit-sword-player-uuid");
    public final static NamespacedKey PASSIVE_ABILITY_ID = new NamespacedKey(Dragon_Realm.instance, "spirit-sword-passive");
    public final static NamespacedKey ACTIVE_ABILITY_ID = new NamespacedKey(Dragon_Realm.instance, "spirit-sword-passive");


    public final static PersistentDataType<String, String> NAME_PRIMITIVE = PersistentDataType.STRING;
    public final static PersistentDataType<Integer, Integer> ELEMENT_PRIMITIVE = PersistentDataType.INTEGER;
    public final static PersistentDataType<Double, Double> EXPERIENCE_PRIMITIVE = PersistentDataType.DOUBLE;
    public final static PersistentDataType<Double, Double> LEVEL_PRIMITIVE = PersistentDataType.DOUBLE;
    public final static PersistentDataType<byte[], UUID> PLAYER_UUID_PRIMITIVE = new UUIDDataType();
    public final static PersistentDataType<Integer, Integer> PASSIVE_ABILITY_ID_PRIMITIVE = PersistentDataType.INTEGER;
    public final static PersistentDataType<Integer, Integer> ACTIVE_ABILITY_ID_PRIMITIVE = PersistentDataType.INTEGER;


    public final static double MAX_LEVEL = 5;

    public final static double BASE_EXP = 20;
    public final static double LEVEL_EXP_MODIFIER = 2;

    public final static double BASE_DAMAGE = 4;
    public final static double LEVEL_DAMAGE_MODIFIER = 0.5;
    public final static double MAX_DAMAGE = 14;

    public SpiritSword(ItemMeta meta){
        this.name = getName(meta);
        this.playerUUID = getPlayer(meta).getUniqueId();
        this.element = getElement(meta);
        this.experience = getExperience(meta);
        this.level = getLevel(meta);
        this.swordUUID = UUID.randomUUID();
        this.passive = getPassiveAbility(meta);
        this.active = getActiveAbility(meta);
    }

    public static void createNewSpiritSword(Player player){
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD, 1);
        ItemMeta meta = sword.getItemMeta();
        assert meta != null;

        setName(meta, getRandomSpiritName());
        setElement(meta, SpiritElement.getRandomElement());
        setExperience(meta, 0);
        setLevel(meta, 0);
        setPlayer(meta, player);

        SpiritSword.updateSpiritSwordMeta(meta, sword);
        sword.setItemMeta(meta);
        player.getInventory().addItem(sword);
    }

    public static void giveSpiritSword(SpiritSword spiritSword, Player player){
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD, 1);
        ItemMeta meta = sword.getItemMeta();
        assert meta != null;

        setName(meta, spiritSword.name);
        setElement(meta, spiritSword.element);
        setExperience(meta, spiritSword.experience);
        setLevel(meta, spiritSword.level);
        setPlayer(meta, getPlayer(spiritSword.playerUUID));

        if(spiritSword.passive != null){
            setPassiveAbility(meta, spiritSword.passive);
        }
        if(spiritSword.active != null){
            setActiveAbility(meta, spiritSword.active);
        }

        SpiritSword.updateSpiritSwordMeta(meta, sword);
        sword.setItemMeta(meta);
        player.getInventory().addItem(sword);
    }

    public static void updateSpiritSwordMeta(ItemMeta meta, ItemStack item){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        OfflinePlayer player = getPlayer(meta);
        double currentLevel = getLevel(meta);
        double experience = getExperience(meta);
        SpiritElement element = getElement(meta);
        String name = getName(meta);
        SpiritSwordPassiveAbility sspa = getPassiveAbility(meta);
        SpiritSwordActiveAbility ssaa = getActiveAbility(meta);

        //level update
        if(experience >= calculateRequiredExp(currentLevel) && currentLevel < MAX_LEVEL){
            levelUp(meta);
            setExperience(meta, 0);
            currentLevel++;
            experience = 0;

            //passive ability
            switch ((int) currentLevel){
                case 3:
                    SpiritSwordPassiveAbility passiveAbility = SpiritSwordPassiveAbility.getRandomAbility(element);
                    setPassiveAbility(meta, passiveAbility);
                    sspa = passiveAbility;
                    break;
                case 4:
                    SpiritSwordActiveAbility activeAbility = SpiritSwordActiveAbility.getRandomAbility(element);
                    setActiveAbility(meta, activeAbility);
                    ssaa = activeAbility;
                    break;
            }

            SoundEffect.SUCCESS.play(Bukkit.getPlayer(player.getUniqueId()));
        }

        //damage update
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier("generic.attackDamage", calculateBaseDamage(currentLevel), AttributeModifier.Operation.ADD_NUMBER));

        //lore update
        List<String> lore = new ArrayList<>();
        lore.add("bound to: " + player.getName());
        if(currentLevel >= 1){
            meta.setDisplayName(element.getChatColor() + name);
            lore.add("element: " + element.getColoredName());
        }
        else{
            meta.setDisplayName("Spirit Sword");
        }
        if(sspa != null){
            lore.add("passive ability: " + element.getChatColor() + sspa.getAbility().getName());
        }
        if(ssaa != null){
            lore.add("active ability: " + element.getChatColor() + ssaa.getAbility().getName());
        }
        lore.add("damage: " + calculateBaseDamage(currentLevel));
        lore.add("");

        switch ((int) currentLevel){
            case 0:
                lore.add(ChatColor.GRAY + "This weapon seems holds tremendous power");
                lore.add("");
                lore.add(ChatColor.GRAY + "Use this weapon to unlock it's true potential");
                break;
            case 1:
                lore.add(ChatColor.GRAY + "the spirit in the sword has revealed it's name to you");
                lore.add("");
                lore.add(ChatColor.GRAY + "Use this weapon to unlock it's true potential");
                break;
            case 2:
                lore.add(ChatColor.GRAY + "the spirit has bound itself to your soul");
                lore.add("");
                lore.add(ChatColor.GRAY + "Use this weapon to unlock it's true potential");
                break;
            case 3:

                break;
        }
        if(currentLevel < MAX_LEVEL){
            lore.add(ChatColor.GRAY + "(" + Math.round(experience) + "/" + calculateRequiredExp(currentLevel) + ")");
        }
        else{
            lore.add(ChatColor.GRAY + "this sword has fully accepted you as it's master");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void loadSoulBindMap(){
        File dir = new File(Dragon_Realm.instance.getDataFolder(), "Utils");
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdir();
        }
        File loadFile = new File(dir, "SoulBindMap.dat");
        if(loadFile.exists()){
            Object obj = ObjectIO.loadObjectFromFile(loadFile);
            soulBindMap = (HashMap) obj;
        }
        else soulBindMap = new HashMap<>();
    }

    public static void saveSoulBindMap(){
        File dir = new File(Dragon_Realm.instance.getDataFolder(), "Utils");
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdir();
        }
        File saveFile = new File(dir, "SoulBindMap.dat");
        ObjectIO.writeObjectToFile(saveFile, soulBindMap);
    }

    public static void addSpiritSwordToSoulBindMap(UUID playerUUID, ItemMeta meta){
        SpiritSword sword = new SpiritSword(meta);
        if(!soulBindMap.containsKey(playerUUID)){
            soulBindMap.put(playerUUID, new ArrayList<>());
        }
        ArrayList<SpiritSword> list =  soulBindMap.get(playerUUID);
        list.add(sword);
    }

    public static void clearPlayerSoulBindMap(UUID playerUUID){
        soulBindMap.remove(playerUUID);
    }

    public static boolean playerInSoulBindMap(UUID playerUUID){
        return soulBindMap.containsKey(playerUUID);
    }

    public static ArrayList<SpiritSword> getSpiritSwordFromSoulBindMap(UUID playerUUID){
        return soulBindMap.get(playerUUID);
    }

    public static double calculateRequiredExp(double level){
        return BASE_EXP + BASE_EXP * level * LEVEL_EXP_MODIFIER;
    }

    public static double calculateBaseDamage(double level){
        return Math.min(BASE_DAMAGE + BASE_DAMAGE * level * LEVEL_DAMAGE_MODIFIER, MAX_DAMAGE);
    }

    private static OfflinePlayer getPlayer(UUID uuid){
        return Bukkit.getOfflinePlayer(uuid);
    }

    public static boolean isSpiritSword(ItemStack item){
        return item.getType() == Material.NETHERITE_SWORD
                && item.getItemMeta() != null
                && item.getItemMeta().getPersistentDataContainer().has(NAME, PersistentDataType.STRING)
                && item.getItemMeta().getPersistentDataContainer().has(PLAYER_UUID, PLAYER_UUID_PRIMITIVE);
    }

    public static String getRandomSpiritName(){
        List<String> names = new ArrayList<>();
        names.add("Huricus");
        names.add("Incantus");
        names.add("Lavis");
        names.add("Valanche");
        names.add("Firn");
        names.add("Gliss");
        names.add("Mayalis");
        names.add("Aros");
        names.add("Illume");
        names.add("Marble");
        names.add("Mortos");
        names.add("Typhis");
        names.add("Aeon");
        names.add("Veilios");
        names.add("Bronto");
        names.add("Fara");
        names.add("Parados");
        names.add("Clot");
        names.add("Necros");
        names.add("Halitus");
        names.add("Thrombus");
        names.add("Baraq");
        names.add("Baecos");
        names.add("Sod");
        names.add("Dusk");

        return names.get(new Random().nextInt(names.size()));
    }

    //get and set values to item meta

    /**
     * get the name of a spirit sword
     * @param meta the meta of the spirit sword
     * @return the name of this spirit sword, null if no name or not a spirit sword
     */
    public static String getName(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(meta.getPersistentDataContainer().has(NAME, NAME_PRIMITIVE)){
            return meta.getPersistentDataContainer().get(NAME, NAME_PRIMITIVE);
        }
        else return null;
    }

    /**
     * set the name of a spirit sword
     * @param meta the meta data of the spirit sword
     * @param name the name that will be given to the spirit sword
     */
    public static void setName(ItemMeta meta, String name){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        meta.getPersistentDataContainer().set(NAME, NAME_PRIMITIVE, name);
    }

    /**
     * get the element of the spirit sword
     * @param meta the meta data of the spirit sword
     * @return the element of the sword, null if no element or not spirit sword
     */
    public static SpiritElement getElement(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(meta.getPersistentDataContainer().has(ELEMENT, ELEMENT_PRIMITIVE)){
            int intValue = meta.getPersistentDataContainer().get(ELEMENT, ELEMENT_PRIMITIVE);
            return SpiritElement.getElementWithIntValue(intValue);
        }
        else return null;
    }

    /**
     * set the element for a spirit sword
     * @param meta the meta data of the spirit sword
     * @param element the element to give the spirit sword
     */
    public static void setElement(ItemMeta meta, SpiritElement element){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        meta.getPersistentDataContainer().set(ELEMENT, ELEMENT_PRIMITIVE, element.getIntValue());
    }

    /**
     * get the experience of this spirit sword
     * @param meta the meta data of the spirit sword
     * @return the exp of the spirit sword, -1 if no experience or not spirit sword
     */
    public static double getExperience(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(meta.getPersistentDataContainer().has(EXPERIENCE, EXPERIENCE_PRIMITIVE)){
            return meta.getPersistentDataContainer().get(EXPERIENCE, EXPERIENCE_PRIMITIVE);
        }
        else return -1;
    }

    /**
     * set the experience for this spirit sword
     * @param meta the meta data of the spirit sword
     * @param exp the exp to set for the spirit sword
     */
    public static void setExperience(ItemMeta meta, double exp){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        meta.getPersistentDataContainer().set(EXPERIENCE, EXPERIENCE_PRIMITIVE, exp);
    }

    /**
     * add experience for this spirit sword
     * @param meta the meta data of this spirit sword
     * @param toAdd the amount of exp to add to the spirit sword
     */
    public static void addExperience(ItemMeta meta, double toAdd){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        double exp = 0;
        if(meta.getPersistentDataContainer().has(EXPERIENCE, EXPERIENCE_PRIMITIVE)){
            exp = meta.getPersistentDataContainer().get(EXPERIENCE, EXPERIENCE_PRIMITIVE);
        }
        meta.getPersistentDataContainer().set(EXPERIENCE, EXPERIENCE_PRIMITIVE, exp + toAdd);
    }

    /**
     * get the level of the spirit sword
     * @param meta the meta data of the spirit sword
     * @return the level of the spirit sword, -1 if no level or not spirit sword
     */
    public static double getLevel(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(meta.getPersistentDataContainer().has(LEVEL, LEVEL_PRIMITIVE)){
            return meta.getPersistentDataContainer().get(LEVEL, LEVEL_PRIMITIVE);
        }
        return -1;
    }

    /**
     * set a level for the spirit sword
     * @param meta the meta data of the spirit sword
     * @param level the level to set for the spirit sword
     */
    public static void setLevel(ItemMeta meta, double level){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        meta.getPersistentDataContainer().set(LEVEL, LEVEL_PRIMITIVE, level);
    }

    /**
     * add a level to the level of the spirit sword
     * @param meta the meta data of the spirit sword
     */
    public static void levelUp(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        double level = 0;
        if(meta.getPersistentDataContainer().has(LEVEL, LEVEL_PRIMITIVE)){
            level = meta.getPersistentDataContainer().get(LEVEL, LEVEL_PRIMITIVE);
        }
        meta.getPersistentDataContainer().set(LEVEL, LEVEL_PRIMITIVE, level + 1);
    }

    /**
     * get the player the spirit sword is bound to
     * @param meta the meta of the spirit sword
     * @return the player that this spirit sword is bound to
     */
    public static OfflinePlayer getPlayer(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(meta.getPersistentDataContainer().has(PLAYER_UUID, PLAYER_UUID_PRIMITIVE)){
            return Bukkit.getOfflinePlayer(meta.getPersistentDataContainer().get(PLAYER_UUID, PLAYER_UUID_PRIMITIVE));
        }
        return null;
    }

    /**
     * bind a player to this spirit sword
     * @param meta the meta data of the spirit sword
     * @param player the player to bind to the spirit sword
     */
    public static void setPlayer(ItemMeta meta, OfflinePlayer player){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        meta.getPersistentDataContainer().set(PLAYER_UUID, PLAYER_UUID_PRIMITIVE, player.getUniqueId());
    }

    /**
     * get the passive ability of a spirit sword
     * @param meta the meta data of the spirit sword
     * @return the SpiritSwordPassiveAbility of this spirit sword
     */
    public static SpiritSwordPassiveAbility getPassiveAbility(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(meta.getPersistentDataContainer().has(PASSIVE_ABILITY_ID, PASSIVE_ABILITY_ID_PRIMITIVE)){
            return SpiritSwordPassiveAbility.getAbilityFromID(
                    meta.getPersistentDataContainer().get(PASSIVE_ABILITY_ID, PASSIVE_ABILITY_ID_PRIMITIVE)
            );
        }
        else return null;
    }

    /**
     * set the passive ability of a spirit sword
     * @param meta the meta data of the spirit sword
     * @param sspa the passive ability
     */
    public static void setPassiveAbility(ItemMeta meta, SpiritSwordPassiveAbility sspa){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        meta.getPersistentDataContainer().set(PASSIVE_ABILITY_ID, PASSIVE_ABILITY_ID_PRIMITIVE, sspa.getAbility().getID());
    }

    /**
     * check if a spirit sword has a given passive ability
     * @param meta the meta data of the spirit sword
     * @param sspa the passive ability to check for
     * @return true is spirit sword has ability, false if not
     */
    public static boolean hasPassiveAbility(ItemMeta meta, SpiritSwordPassiveAbility sspa){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        SpiritSwordPassiveAbility ability = getPassiveAbility(meta);
        if(ability == null) return false;
        else return ability == sspa;
    }

    public static SpiritSwordActiveAbility getActiveAbility(ItemMeta meta){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(meta.getPersistentDataContainer().has(ACTIVE_ABILITY_ID, ACTIVE_ABILITY_ID_PRIMITIVE)){
            return SpiritSwordActiveAbility.getAbilityFromID(
                    meta.getPersistentDataContainer().get(ACTIVE_ABILITY_ID, ACTIVE_ABILITY_ID_PRIMITIVE)
            );
        }
        else return null;
    }

    public static void setActiveAbility(ItemMeta meta, SpiritSwordActiveAbility ssaa){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        meta.getPersistentDataContainer().set(ACTIVE_ABILITY_ID, ACTIVE_ABILITY_ID_PRIMITIVE, ssaa.getAbility().getID());
    }

    /**
     * check if a player is the owner of a spirit sword
     * @param meta the meta data of the spirit sword
     * @param player the player to check for
     * @return true if the player is the owner, false if not
     */
    public static boolean isOwner(ItemMeta meta, OfflinePlayer player){
        if(meta == null) throw new IllegalArgumentException("meta can't be null");
        if(player == null) throw new IllegalArgumentException("player can't be null");
        return player.getUniqueId().equals(getPlayer(meta).getUniqueId());
    }

    public static ItemStack getInfoBook(){
        BookBuilder book = new BookBuilder()
                .setTitle(ChatColor.GOLD + "Spirit Swords")
                .setAuthor(ChatColor.MAGIC + "Jaspy007")
                .setLore("info book");

        book.addPage(new BookBuilder.BookPageBuilder()
                .addLine(ChatColor.GOLD + "Spirit Sword")
                .addBlankLine()
                .addLine(ChatColor.RESET + "This sword holds a spirit inside")
                .addBlankLine()
                .addLine("A spirit can be of one of the following elements:")
        );

        //passive abilities
        for(SpiritElement element : SpiritElement.values()){
            BookBuilder.BookPageBuilder pageBuilder = new BookBuilder.BookPageBuilder()
                    .addLine(ChatColor.GOLD + element.getName() + " passive abilities:")
                    .addBlankLine();

            for(SpiritSwordPassiveAbility sspa : SpiritSwordPassiveAbility.getAbilitiesFromElement(element)){
                    pageBuilder.addLine(sspa.getAbility().getName());
            }

            book.addPage(
                    pageBuilder
            );

            for(SpiritSwordPassiveAbility sspa : SpiritSwordPassiveAbility.getAbilitiesFromElement(element)){
                book.addPage(sspa.getAbility().bookPage());
            }
        }

        for(SpiritElement element : SpiritElement.values()){
            BookBuilder.BookPageBuilder pageBuilder = new BookBuilder.BookPageBuilder()
                    .addLine(ChatColor.GOLD + element.getName() + " active abilities:")
                    .addBlankLine();

            for(SpiritSwordActiveAbility ssaa : SpiritSwordActiveAbility.getAbilitiesFromElement(element)){
                pageBuilder.addLine(ssaa.getAbility().getName());
            }

            book.addPage(
                    pageBuilder
            );

            for(SpiritSwordActiveAbility ssaa : SpiritSwordActiveAbility.getAbilitiesFromElement(element)){
                book.addPage(ssaa.getAbility().bookPage());
            }
        }

        return book.getBook();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpiritSword)) return false;
        SpiritSword that = (SpiritSword) o;
        return Double.compare(that.experience, experience) == 0 && Double.compare(that.level, level) == 0 && Objects.equals(name, that.name) && element == that.element && Objects.equals(playerUUID, that.playerUUID) && Objects.equals(swordUUID, that.swordUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, element, experience, level, playerUUID, swordUUID);
    }
}
