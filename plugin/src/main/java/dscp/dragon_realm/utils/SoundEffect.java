package dscp.dragon_realm.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * Represents a sound effect consisting of a sound, a pitch and a volume
 */
public class SoundEffect {

    public static final SoundEffect SELECT = new SoundEffect(Sound.ENTITY_ENDER_EYE_LAUNCH);
    public static final SoundEffect CLICK = new SoundEffect(Sound.UI_BUTTON_CLICK);
    public static final SoundEffect FAIL = new SoundEffect(Sound.ENTITY_ITEM_BREAK, 0);
    public static final SoundEffect SUCCESS = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);

    private Sound sound;
    private float pitch;
    private float volume;
    private boolean force;

    public SoundEffect(Sound sound) {
        this.sound = sound;
        this.pitch = 1;
        this.volume = 1;
    }

    public SoundEffect(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = 1;
    }

    public SoundEffect(Sound sound, float pitch, float volume) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    public Sound getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public SoundEffect force() {
        this.force = true;
        return this;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public SoundEffect setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    /**
     * Play this sound effect at the specified location
     *
     * @param pos Position to play the sound at
     */
    public void play(Location pos) {
        if(force) {
            pos.getWorld().playSound(pos, sound, SoundCategory.MASTER, volume, pitch);
        } else {
            pos.getWorld().playSound(pos, sound, volume, pitch);
        }
    }

    /**
     * Play this sound effect for the specified player
     *
     * @param players Players to play sound to
     */
    public void play(Player... players) {
        for(Player player : players) {
            if(force) {
                player.playSound(player.getLocation(), sound, SoundCategory.MASTER, volume, pitch);
            } else {
                player.playSound(player.getLocation(), sound, volume, pitch);
            }
        }
    }

    /**
     * Broadcast a sound to all players
     */
    public void broadcast() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            play(player);
        }
    }

}
