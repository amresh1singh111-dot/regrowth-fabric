package com.mactso.regrowth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mactso.regrowth.events.MoveEntityEvent;

import net.minecraft.world.entity.LivingEntity;

// Fabric has no "living entity tick" event, so hook the start of LivingEntity.tick().
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	private void onEntityMoves(CallbackInfo ci) {
		MoveEntityEvent.handleEntityMoveEvents((LivingEntity) (Object) this);
	}
}
