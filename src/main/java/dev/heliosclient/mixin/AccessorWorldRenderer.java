package dev.heliosclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public interface AccessorWorldRenderer {
	
	@Accessor
	public abstract Frustum getFrustum();
	
	@Accessor
	public abstract void setFrustum(Frustum frustum);
}
