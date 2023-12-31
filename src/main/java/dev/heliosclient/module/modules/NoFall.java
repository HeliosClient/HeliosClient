package dev.heliosclient.module.modules;

import dev.heliosclient.event.SubscribeEvent;
import dev.heliosclient.event.events.TickEvent;
import dev.heliosclient.module.Categories;
import dev.heliosclient.module.Module_;
import dev.heliosclient.module.settings.CycleSetting;
import dev.heliosclient.module.settings.DoubleSetting;
import dev.heliosclient.module.settings.SettingGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class NoFall extends Module_ {
    protected static MinecraftClient mc = MinecraftClient.getInstance();
    private final SettingGroup sgGeneral = new SettingGroup("General");
    DoubleSetting fallHeight = sgGeneral.add(new DoubleSetting.Builder()
            .name("Trigger height")
            .description("Height on which No Fall triggers")
            .onSettingChange(this)
            .value(2.5)
            .defaultValue(2.5)
            .min(2)
            .max(22)
            .roundingPlace(1)
            .build()
    );
    CycleSetting mode = sgGeneral.add(new CycleSetting.Builder()
            .name("Mode")
            .description("Mode which should save player from fall height ")
            .onSettingChange(this)
            .value(new ArrayList<String>(List.of("Classic", "Disconnect (annoying)")))
            .defaultValue(new ArrayList<String>(List.of("Classic", "Disconnect (annoying)")))
            .listValue(0)
            .build()
    );

    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage.", Categories.PLAYER);

        addSettingGroup(sgGeneral);

        addQuickSettings(sgGeneral.getSettings());
    }

    @SubscribeEvent
    public void onTick(TickEvent.PLAYER event) {
        assert mc.player != null;
        if (mc.player.fallDistance >= fallHeight.value && !mc.player.isCreative()) {
            if (mode.value == 0) {
                mc.player.networkHandler.sendPacket(
                        new PlayerMoveC2SPacket.OnGroundOnly(true)
                );
            } else if (mode.value == 1) {
                int distance = 0;
                int y = (int) mc.player.getY();
                int maxDistance = y - 1;
                while (distance < maxDistance) {
                    if (!mc.player.clientWorld.isAir(mc.player.getBlockPos().down(distance + 1))) {
                        break;
                    }
                    distance++;
                }
                if (distance <= 2) {
                    assert mc.world != null;
                    mc.player.networkHandler.sendPacket(
                            new PlayerMoveC2SPacket.OnGroundOnly(true)
                    );
                    mc.world.disconnect();
                }
            }
        }
    }
}
