package dscp.dragon_realm.dragonProtect.areaProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.utils.AdvancedObjectIO;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ProtectedArea implements Serializable {
    private static final long serialVersionUID = 2411526520982116874L;

    protected int centerX;
    protected int centerZ;
    protected int range;
    protected UUID worldUUID;
    protected String name;
    private ProtectedAreaSettings settings;
    private ArrayList<ProtectedArea> subAreas;
    private ProtectedArea parentArea;

    private static final File areasFile = new File(Dragon_Realm.instance.getDataFolder(), "areas.dat");
    private static AreaContainer areaContainer = loadAreas();

    public ProtectedArea(String name, Chunk center, int range){
        if(center == null) throw new IllegalArgumentException("center can't ben null");
        if(name == null) throw new IllegalArgumentException("name can't be null");
        this.name = name;
        this.range = range;
        this.settings = new ProtectedAreaSettings();
        this.subAreas = new ArrayList<>();
        this.parentArea = null;
        this.centerX = center.getX();
        this.centerZ = center.getZ();
        this.worldUUID = center.getWorld().getUID();
    }

    public ProtectedArea(String name, Chunk center, int range, ProtectedArea parent){
        this(name, center, range);
        this.parentArea = parent;
    }

    public Chunk getCenter() {
        return getWorld().getChunkAt(centerX, centerZ);
    }

    public int getRange() {
        return range;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldUUID);
    }

    public ProtectedAreaSettings getSettings() {
        return settings;
    }

    public ArrayList<ProtectedArea> getSubAreas() {
        return subAreas;
    }

    public ProtectedArea getParentArea() {
        return parentArea;
    }

    public String getName() {
        return name;
    }

    //sub areas

    public boolean hasSubAreas(){
        return subAreas.size() > 0;
    }

    public boolean hasParent(){
        return parentArea != null;
    }

    public void addSubArea(ProtectedArea area){
        this.subAreas.add(area);
    }

    public boolean subContains(Chunk chunk){
        if(!hasSubAreas()) return false;
        for(ProtectedArea area : subAreas){
            if(area.contains(chunk)) return true;
        }
        return false;
    }

    public ProtectedArea getSubArea(Chunk chunk){
        if(contains(chunk)){
            for(ProtectedArea area : subAreas){
                if(area.contains(chunk)){
                    return area.getSubArea(chunk);
                }
            }
            return this;
        }
        else return null;
    }

    public ProtectedArea getSubArea(String name){
        for(ProtectedArea area : subAreas){
            if(name.equalsIgnoreCase(area.name)) return area;
            else {
                ProtectedArea rec = area.getSubArea(name);
                if(rec != null){
                    return rec;
                }
            }
        }
        return null;
    }

    //contains

    public boolean contains(Chunk chunk){
        if(chunk.getWorld() != getCenter().getWorld()) return false;
        return distanceFromCenter(chunk) <= range;
    }

    //distances

    public static int distance(Chunk from, Chunk to){
        return Math.min(Math.abs(from.getX() - to.getX()), Math.abs(from.getZ() - to.getZ()));
    }

    public int distanceFromCenter(Chunk chunk){
        return distance(this.getCenter(), chunk);
    }

    //overlapping

    public boolean overlapsWith(ProtectedArea area){
        int x1 = centerX;
        int x2 = area.getCenter().getX();
        int z1 = centerZ;
        int z2 = area.getCenter().getX();
        int rangeSum = this.range + area.range;

        return Math.abs(x1 - x2) - 1 < rangeSum
                && Math.abs(z1 - z2) - 1 < rangeSum;
    }

    public ArrayList<ProtectedArea> overlappingAreas(){
        ArrayList<ProtectedArea> overlaps = new ArrayList<>();
        for(ProtectedArea area : areaContainer.getAreas()){
            if(overlapsWith(area)) overlaps.add(area);
        }
        overlaps.remove(this);
        return overlaps;
    }

    public boolean overlapsWithSubs(ProtectedArea area){
        for(ProtectedArea sub : area.subAreas){
            if(overlapsWith(sub)) return true;
        }
        return false;
    }

    public boolean isFullyInsideOf(ProtectedArea area){
        if(!this.getWorld().equals(area.getWorld())) return false;
        int x1 = centerX;
        int x2 = area.centerX;
        int z1 = centerZ;
        int z2 = area.centerZ;

        System.out.println(Math.abs(x1 - x2));
        System.out.println(Math.abs(z1 - z2));
        System.out.println(range);
        System.out.println(area.range);

        return Math.abs(x1 - x2) + range <= area.range
                && Math.abs(z1 - z2) + range <= area.range;
    }

    //statics

    public static AreaContainer getAreaContainer(){
        return areaContainer;
    }

    public static ProtectedArea getArea(String name){
        for(ProtectedArea area : areaContainer.getAreas()){
            if(area.name.equalsIgnoreCase(name)) return area;
            else{
                ProtectedArea sub = area.getSubArea(name);
                if(sub != null) return sub;
            }
        }
        return null;
    }

    public static ProtectedArea getArea(Chunk chunk, boolean ignoreSubAreas){
        for(ProtectedArea area : areaContainer.getAreas()){
            if(ignoreSubAreas){
                if(area.contains(chunk)) return area;
            }
            else{
                ProtectedArea containing = area.getSubArea(chunk);
                if(containing != null) return containing;
            }
        }
        return null;
    }

    public static ProtectedArea getArea(Chunk chunk){
        return getArea(chunk, false);
    }

    public static void addArea(ProtectedArea area){
        areaContainer.getAreas().add(area);
    }

    public static AreaContainer loadAreas(){
        try{
            if(!areasFile.exists()) areasFile.createNewFile();
            AdvancedObjectIO<AreaContainer> objectIO = new AdvancedObjectIO<>(areasFile);
            AreaContainer container = objectIO.loadObject();
            if(container == null){
                container = new AreaContainer();
            }
            return container;
        }
        catch (IOException ex){
            throw new  IllegalStateException("exception in loading areas");
        }
    }

    public static void saveAreas(){
        try{
            if(!areasFile.exists()) areasFile.createNewFile();
            AdvancedObjectIO<AreaContainer> objectIO = new AdvancedObjectIO<>(areasFile);
            objectIO.saveObject(areaContainer);
        }
        catch (IOException ex){
            ex.printStackTrace();
            throw new IllegalStateException("exception in saving areas");
        }
    }

    public class ProtectedAreaSettings implements Serializable{
        private static final long serialVersionUID = -902923122640693359L;

        private boolean pvpAllowed;
        private boolean buildingAllowed;

        public ProtectedAreaSettings(){
            this.pvpAllowed = true;
            this.buildingAllowed = false;

        }

        public void setPvpAllowed(boolean pvpAllowed) {
            this.pvpAllowed = pvpAllowed;
        }

        public boolean isPvpAllowed() {
            return pvpAllowed;
        }

        public void setBuildingAllowed(boolean buildingAllowed) {
            this.buildingAllowed = buildingAllowed;
        }

        public boolean isBuildingAllowed() {
            return buildingAllowed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProtectedAreaSettings)) return false;
            ProtectedAreaSettings that = (ProtectedAreaSettings) o;
            return pvpAllowed == that.pvpAllowed && buildingAllowed == that.buildingAllowed;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pvpAllowed, buildingAllowed);
        }
    }

    //equals and toHash

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtectedArea)) return false;
        ProtectedArea area = (ProtectedArea) o;
        return range == area.range && Objects.equals(centerX, area.centerX)
                && Objects.equals(centerZ, area.centerZ)
                && Objects.equals(worldUUID, area.worldUUID)
                && Objects.equals(settings, area.settings) && Objects.equals(subAreas, area.subAreas)
                && Objects.equals(parentArea, area.parentArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerX, centerZ, range, worldUUID, settings, subAreas, parentArea);
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String spaces) {
        if(spaces == null) spaces = "";
        StringBuilder sb = new StringBuilder();
        sb.append(spaces).append("-").append(this.name).append(" (").append(centerX).append(",")
                .append(centerZ).append(")[").append(this.range).append("]\n");
        if(this.hasSubAreas()){
            for(ProtectedArea sub : this.getSubAreas()){
                sb.append(sub.toString(spaces + "  ")).append("\n");
            }
        }
        return sb.toString();
    }
}
