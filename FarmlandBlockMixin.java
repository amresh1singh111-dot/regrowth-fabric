package com.mactso.regrowth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mactso.regrowth.events.TrampleEventHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

// Forge has a FarmlandTrampleEvent.  Fabric doesn't, so cancel the dirt-turning step directly.
// (Hooking turnToDirt instead of fallOn keeps normal fall damage.)
@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {

	@Inject(method = "turnToDirt", at = @At("HEAD"), cancellable = true)
	private static void onFarmlandTrampled(Entity entity, BlockState state, Level level, BlockPos pos,
			CallbackInfo ci) {
		if (TrampleEventHandler.handleTrampleEvent(entity)) {
			ci.cancel();
		}
	}
}
