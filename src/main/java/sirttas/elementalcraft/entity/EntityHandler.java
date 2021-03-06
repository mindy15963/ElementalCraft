package sirttas.elementalcraft.entity;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.network.message.ECMessage;
import sirttas.elementalcraft.network.message.MessageHandler;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EntityHandler {

	private static boolean lastJump = false; // we only need one because it's only used client side

	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		if (InfusionHelper.hasAirInfusionFasterDraw(event.getItem()) && event.getDuration() % 3 == 0) {
			event.setDuration(event.getDuration() - 1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void clientAirInfusionFly(ClientPlayerEntity player) {
		if (InfusionHelper.canAirInfusionFly(player) && (player.isElytraFlying() || (player.movementInput.jump && !lastJump && !player.isElytraFlying()))) {
			player.startFallFlying();
			MessageHandler.CHANNEL.sendToServer(ECMessage.AIR_INFUSION);
		} else {
			player.stopFallFlying();
		}
		lastJump = player.movementInput.jump || player.onGround;
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			if (entity instanceof ClientPlayerEntity) {
				clientAirInfusionFly((ClientPlayerEntity) event.getEntityLiving());
			}
		});
		if (entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

			if (player.isElytraFlying() && InfusionHelper.canAirInfusionFly(player)) {
				player.startFallFlying();
			} else {
				player.stopFallFlying();
			}
		}
	}
}
