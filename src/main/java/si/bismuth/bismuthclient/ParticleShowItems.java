package si.bismuth.bismuthclient;

import com.mumfrey.liteloader.gl.GL;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
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

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		final Tessellator tessellator = Tessellator.getInstance();
		tessellator.draw();
		GlStateManager.disableDepth();
		GlStateManager.color(1F, 1F, 1F);
		buffer.begin(GL.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		tessellator.draw();
		GlStateManager.enableDepth();
		buffer.begin(GL.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}
}
