package dscp.dragon_realm.customEnchants.events;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.customEnchants.EnchantException;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.*;

public class LightningLink {
    private LightningLink link1;
    private LightningLink link2;
    private Entity entity;

    public LightningLink(Entity entity){
        if(entity == null) throw new IllegalArgumentException("entity can't be null");
        this.entity = entity;
        this.link1 = null;
        this.link2 = null;
    }

    public LightningLink(Entity entity, LightningLink link1, LightningLink link2){
        this(entity);
        this.link1 = link1;
        this.link2 = link2;
    }

    public Entity getEntity() {
        return entity;
    }

    public LightningLink getLink1() {
        return link1;
    }

    public LightningLink getLink2() {
        return link2;
    }

    public void setLink1(LightningLink link1) {
        this.link1 = link1;
    }

    public void setLink2(LightningLink link2) {
        this.link2 = link2;
    }

    public double getTotalDistanceOfLinks(){
        if(this.link1 == null && this.link2 == null){
            return 0.0;
        }
        else if(link2 == null){
            return entity.getLocation().distance(link1.getEntity().getLocation()) + link1.getTotalDistanceOfLinks();
        }
        else if(link1 == null){
            return entity.getLocation().distance(link2.getEntity().getLocation()) + link2.getTotalDistanceOfLinks();
        }
        else{
            return entity.getLocation().distance(link1.getEntity().getLocation()) + link1.getTotalDistanceOfLinks()
                    + entity.getLocation().distance(link2.getEntity().getLocation()) + link2.getTotalDistanceOfLinks();
        }
    }

    public static LightningLink createLink(Entity startingEntity, int range, ArrayList<Entity> entitiesInLink){
        if(entitiesInLink == null) {
            entitiesInLink = new ArrayList<>();
            entitiesInLink.add(startingEntity);
        }
        ArrayList<Entity> inRangeEntities = new ArrayList<>(startingEntity.getWorld().getNearbyEntities(startingEntity.getLocation() ,range, range, range, LivingEntity.class::isInstance));

        //return link with no branches when range is 0
        if(range == 0) return new LightningLink(startingEntity);

        //remove entities that are already in link
        for(Entity entity : entitiesInLink){
            inRangeEntities.remove(entity);
        }

        //remove all entities except for 2 (random)
        while(inRangeEntities.size() > 2){
            inRangeEntities.remove((int) Math.floor(Math.random() * inRangeEntities.size()));
        }

        //create new LightningLink object with starting entity and new range
        LightningLink link = new LightningLink(startingEntity);
        int newRange = Math.max(0, range - 1);

        //branch off
        switch (inRangeEntities.size()){
            case 2:
                LivingEntity entity1 = (LivingEntity) inRangeEntities.get(0);
                LivingEntity entity2 = (LivingEntity) inRangeEntities.get(1);
                entitiesInLink.add(entity1);
                entitiesInLink.add(entity2);
                link.setLink1(createLink(entity1, newRange, entitiesInLink));
                link.setLink2(createLink(entity2, newRange, entitiesInLink));
                break;
            case 1:
                entitiesInLink.add(inRangeEntities.get(0));
                link.setLink1(createLink( (LivingEntity) inRangeEntities.get(0), newRange, entitiesInLink));
        }

        return link;
    }

    public static void lightningChainDamage(double damage, LightningLink link) throws EnchantException {
        if(!(link.getEntity() instanceof LivingEntity)) throw new  EnchantException("exception in link damage, wrong entity type");
        LivingEntity entity = (LivingEntity) link.getEntity();

        //damage
        entity.damage(damage);
        if(link.getLink1() != null){
            lightningChainDamage(damage/2, link.getLink1());
        }
        if(link.getLink2() != null) {
            lightningChainDamage(damage/2, link.getLink2());
        }

        //particles
        for(int i = 0 ; i < 3 ; i++){
            Location startLoc = entity.getLocation();
            startLoc.setY((Math.random() * 2) + startLoc.getY());

            if(link.getLink1() != null){
                Location loc = link.getLink1().getEntity().getLocation();
                loc.setY((Math.random() * 2) + loc.getY());
                Dragon_Realm_API.spawnParticlesBetween(startLoc, loc, Particle.BUBBLE_POP, 0.1, 10);
            }
            if(link.getLink2() != null) {
                Location loc = link.getLink2().getEntity().getLocation();
                loc.setY((Math.random() * 2) + loc.getY());
                Dragon_Realm_API.spawnParticlesBetween(startLoc, loc, Particle.BUBBLE_POP, 0.1, 10);
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.entity.getUniqueId().toString()).append("\n");
        if(this.link1 != null) sb.append("link1: ").append(link1.toString()).append("\n");
        if(this.link2 != null) sb.append("link2: ").append(link2.toString()).append("\n");

        return sb.toString();
    }
}
