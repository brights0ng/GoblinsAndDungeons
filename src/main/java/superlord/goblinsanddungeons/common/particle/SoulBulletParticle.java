package superlord.goblinsanddungeons.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulBulletParticle extends TextureSheetParticle {

    protected SoulBulletParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale, SpriteSet spriteWithAge) {
        super(world, x, y, z, 0.1F, 0.1F, 0.1F);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SoulBulletParticle particle = new SoulBulletParticle(worldIn, x, y, z, xSpeed, 0.01, zSpeed, 1.5F, this.spriteSet);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
