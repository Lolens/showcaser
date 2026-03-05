package io.github.lolens.showcaser.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.network.message.c2s.ShareMessage;
import io.github.lolens.showcaser.network.message.s2c.ShareDisplayMessage;
import io.github.lolens.showcaser.network.message.s2c.conditional.emi.EmiOpenScreenMessage;
import io.github.lolens.showcaser.network.message.s2c.conditional.rei.ReiOpenScreenMessage;

public class Networking {

    private static final SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(Showcaser.MOD_ID);

    public static void register() {
        MessageTypes.C2S.SHARE_RESOURCE = NETWORK_MANAGER.registerC2S(
                "c2s_share_resource",
                ShareMessage::new
        );

        MessageTypes.S2C.SHARE_DISPLAY = NETWORK_MANAGER.registerS2C(
                "s2c_share_display",
                ShareDisplayMessage::new
        );
    }

    public static class MessageTypes {

        public static class C2S {
            public static MessageType SHARE_RESOURCE;
        }

        public static class S2C {
            public static MessageType SHARE_DISPLAY;
        }

    }

    public static class Conditional {

        public static class EMI {

            public static class MessageTypes {

                public static class S2C {
                    public static MessageType EMI_OPEN_SCREEN;
                }

            }

            public static void register() {
                MessageTypes.S2C.EMI_OPEN_SCREEN = NETWORK_MANAGER.registerS2C(
                        "s2c_emi_open_screen",
                        EmiOpenScreenMessage::new
                );

            }



        }


        public static class REI {

            public static class MessageTypes {

                public static class S2C {
                    public static MessageType REI_OPEN_SCREEN;
                }

            }

            public static void register() {
                MessageTypes.S2C.REI_OPEN_SCREEN = NETWORK_MANAGER.registerS2C(
                        "s2c_rei_open_screen",
                        ReiOpenScreenMessage::new
                );

            }


        }

    }


}
