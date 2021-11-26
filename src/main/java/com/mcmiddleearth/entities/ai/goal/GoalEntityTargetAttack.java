package com.mcmiddleearth.entities.ai.goal;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.MovementSpeed;
import com.mcmiddleearth.entities.ai.pathfinding.Pathfinder;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.entities.entities.composite.animation.BakedAnimation;
import com.mcmiddleearth.entities.events.events.goal.GoalVirtualEntityIsClose;
import org.bukkit.Location;

public class GoalEntityTargetAttack extends GoalEntityTarget {

    public GoalEntityTargetAttack(VirtualEntity entity, VirtualEntityGoalFactory factory, Pathfinder pathfinder) {
        super(entity, factory, pathfinder);
    }

    int isCloseTicks;

    @Override
    public void doTick() {
        super.doTick();
        if(!targetIncomplete) {
            //if(getPath()!=null) Logger.getGlobal().info("Path: \n"+getPath().getStart()+" \n"+getPath().getEnd()+" \n"+getPath().getTarget());
            if (isCloseToTarget(GoalDistance.ATTACK)) {
                //Logger.getGlobal().info("delete path as entity is close.");
                EntitiesPlugin.getEntityServer().handleEvent(new GoalVirtualEntityIsClose(getEntity(), this));
                setIsMoving(false);//deletePath();
                movementSpeed = MovementSpeed.STAND;
                Location orientation =getEntity().getLocation().clone().setDirection(getTarget().getLocation().toVector()
                        .subtract(getEntity().getLocation().toVector()));
                setYaw(orientation.getYaw());
                setPitch(orientation.getPitch());
                if (isCloseTicks > 0 && !isFinished()) {
                    getEntity().attack(target);
                }
                isCloseTicks++;
                //}
            } else {
                setIsMoving(true);
                movementSpeed = MovementSpeed.WALK;
                isCloseTicks = 0;
            }
            if (target.isDead()) {
                setFinished();
            }
        }
    }

    /*@Override
    public void update() {
        if(!(isCloseToTarget(GoalDistance.CAUTION))) {
            super.update();
        }
    }*/

}
