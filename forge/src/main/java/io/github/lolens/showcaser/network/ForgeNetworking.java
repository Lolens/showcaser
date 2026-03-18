package io.github.lolens.showcaser.network;

import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.network.message.s2c.conditional.ftbquests.FtbQuestsOpenScreenMessage;

public class ForgeNetworking {



    public static class Conditional {

        public static class FTBQuests {

            public static class MessageTypes {

                public static class S2C {
                    public static MessageType FTBQUESTS_OPEN_SCREEN;
                }

            }

            public static void register() {
                MessageTypes.S2C.FTBQUESTS_OPEN_SCREEN = Networking.getNetworkManager().registerS2C(
                        "s2c_ftbquests_open_screen",
                        FtbQuestsOpenScreenMessage::new
                );

            }


        }

    }

}
