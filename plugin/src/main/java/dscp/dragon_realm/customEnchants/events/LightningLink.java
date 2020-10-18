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

    public static LightningLink getShortestLink(ArrayList<Entity> entities, Entity startingEntity){
        LightningLink shortestLink = null;

        if(entities.size() <= 2){
            if(entities.size() == 0) return new LightningLink(startingEntity);
            else if(entities.size() == 1) return new LightningLink(startingEntity, new LightningLink(entities.get(0)), null);
            else {
                return new LightningLink(startingEntity, new LightningLink(entities.get(0)), new LightningLink(entities.get(1)));
            }
        }

        for(int i = 0 ; i < entities.size() ; i++){
            for(int j = i + 1 ; j < entities.size() ; j++){
                ArrayList<Entity> link1Array = new ArrayList<>(entities);
                ArrayList<Entity> link2Array = new ArrayList<>(entities);

                link1Array.remove(i);
                link2Array.remove(j);

                LightningLink link = new LightningLink(startingEntity, getShortestLink(link1Array, entities.get(i)), getShortestLink(link2Array, entities.get(j)));
                if(shortestLink == null) shortestLink = link;
                else if(link.getTotalDistanceOfLinks() < shortestLink.getTotalDistanceOfLinks()) shortestLink = link;
            }
        }

        return shortestLink;

    }

    public static void lightningChainDamage(double damage, LightningLink link) throws EnchantException {
        if(!(link.getEntity() instanceof LivingEntity)) throw new  EnchantException("exception in link damage, wrong entity type");
        LivingEntity entity = (LivingEntity) link.getEntity();

        entity.damage(damage);
        if(link.getLink1() != null){
            lightningChainDamage(damage/2, link.getLink1());
            Location loc = link.getLink1().getEntity().getLocation();
            loc.setY((Math.random() * 2) + loc.getY());
            Dragon_Realm_API.spawnParticlesBetween(entity.getLocation(), loc, Particle.CRIT, 0.1, 10);
        }
        if(link.getLink2() != null) {
            lightningChainDamage(damage/2, link.getLink2());
            Location loc = link.getLink2().getEntity().getLocation();
            loc.setY((Math.random() * 2) + loc.getY());
            Dragon_Realm_API.spawnParticlesBetween(entity.getLocation(), loc, Particle.CRIT, 0.1, 10);
        }
    }
}
