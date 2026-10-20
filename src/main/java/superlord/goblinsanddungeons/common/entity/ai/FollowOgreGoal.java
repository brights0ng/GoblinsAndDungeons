package superlord.goblinsanddungeons.common.entity.ai;

import java.util.List;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import superlord.goblinsanddungeons.common.entity.Ogre;

public class FollowOgreGoal extends Goal {
    private final Animal animal;
    private Ogre ogre;
    private final double moveSpeed;
    private int delayCounter;

    public FollowOgreGoal(Animal animal, double speed) {
        this.animal = animal;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else {
            List<Ogre> list = this.animal.level().getEntitiesOfClass(Ogre.class, this.animal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            if (!list.isEmpty()) {
                this.ogre = list.get(0);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.ogre == null || !this.ogre.isAlive()) {
            return false;
        } else {
            double d0 = this.animal.distanceToSqr(this.ogre);
            return !(d0 < 9.0D) && !(d0 > 256.0D);
        }
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        this.ogre = null;
    }

    @Override
    public void tick() {
        if (--this.delayCounter <= 0 && this.ogre != null) {
            this.delayCounter = 10;
            this.animal.getNavigation().moveTo(this.ogre, this.moveSpeed);
        }
    }
}
