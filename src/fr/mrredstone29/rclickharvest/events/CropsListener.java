package fr.mrredstone29.rclickharvest.events;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class CropsListener implements Listener{
	
	private final Random random = new Random();
	
	@EventHandler
	public void onPlayerClickOnCrop(PlayerInteractEvent event) {
		Block clicked_block = event.getClickedBlock();

		if (clicked_block == null)return;
		
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if ((clicked_block.getBlockData() instanceof Ageable) 
        && (!clicked_block.getType().toString().contains("_STEM")) 
        && ((clicked_block.getType() == Material.NETHER_WART) || (clicked_block.getType() == Material.COCOA) || (item.getType().toString().contains("_HOE")))) {

            Ageable ageable = (Ageable) clicked_block.getBlockData();

            if (ageable.getAge() == ageable.getMaximumAge()) {

            	//Give the drops
            	for (ItemStack i : clicked_block.getDrops()) {
            		
            		int amount = i.getAmount();
            		
            		if ((i.getType() != Material.WHEAT) && (i.getType() != Material.BEETROOT)) {
            			amount -= 1;
            			i.setAmount(amount);
            		}
            		
            		if (amount > 0) {
            			System.out.println(i);
                		Item item_entity = clicked_block.getWorld().dropItem(clicked_block.getLocation().add(0.5, 0.5, 0.5), i);
                        item_entity.setVelocity(new Vector(0, 0.2, 0));
            		}	
            	}

            	//Set age to 0
                ageable.setAge(0);
            	event.getClickedBlock().setBlockData(ageable);
            	
            	//Spawn particles
            	clicked_block.getWorld().spawnParticle(Particle.BLOCK_DUST, clicked_block.getLocation().add(0.5, 0.5, 0.5), 50, 0.5, 0.5, 0.5, clicked_block.getBlockData());
            	
            	//Give XP
            	if (random.nextInt(10) == 0) {
                    ExperienceOrb expOrb = clicked_block.getWorld().spawn(clicked_block.getLocation().add(0.5, 0.5, 0.5), ExperienceOrb.class);
                    expOrb.setExperience(1);
                }
            	
            	//Deal damage to Hoe    	
            	if ((event.getPlayer().getGameMode() != GameMode.CREATIVE) && (clicked_block.getType() != Material.NETHER_WART) && (clicked_block.getType() != Material.COCOA) && (item.getType().toString().contains("_HOE"))) {
                    Damageable damageable_item = (Damageable) item.getItemMeta();

                    damageable_item.setDamage(damageable_item.getDamage() + 1);
                    item.setItemMeta((ItemMeta) damageable_item);

                    if (damageable_item.getDamage() >= item.getType().getMaxDurability()) {
                        event.getPlayer().getInventory().setItemInMainHand(null);
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    }
                }    
            }
        }
	}
}
