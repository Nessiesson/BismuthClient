package si.bismuth.bismuthclient;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;

public class ParticleShowItems extends ParticleSmokeNormal {
	protected ParticleShowItems(World world, double x, double y, double z, double dX, double dY, double dZ, float scale) {
		super(world, x, y, z, dX, dY, dZ, scale);
		this.particleMaxAge *= 7;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.motionY = 0D;
	}
}
