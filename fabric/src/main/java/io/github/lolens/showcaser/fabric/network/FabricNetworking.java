/*
 * Showcaser - <https://github.com/Lolens/showcaser>
 * Copyright (C) 2026-present Lolens <https://github.com/Lolens>
 *
 * This file is part of Showcaser.
 *
 * Showcaser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Showcaser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along Showcaser.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.lolens.showcaser.fabric.network;

import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.fabric.network.message.s2c.conditional.jei.JeiOpenScreenMessage;
import io.github.lolens.showcaser.fabric.network.message.s2c.conditional.patchouli.PatchouliOpenScreenMessage;
import io.github.lolens.showcaser.network.Networking;
import io.github.lolens.showcaser.fabric.network.message.s2c.conditional.ftbquests.FtbQuestsOpenScreenMessage;

public class FabricNetworking {


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


        } // end static class FTBQuests

        public static class JEI {

            public static class MessageTypes {

                public static class S2C {
                    public static MessageType JEI_OPEN_SCREEN;
                }

            }

            public static void register() {
                MessageTypes.S2C.JEI_OPEN_SCREEN = Networking.getNetworkManager().registerS2C(
                        "s2c_jei_open_screen",
                        JeiOpenScreenMessage::new
                );

            }

        } // end static class JEI

        public static class Patchouli {

            public static class MessageTypes {

                public static class S2C {
                    public static MessageType PATCHOULI_OPEN_SCREEN;
                }

            }

            public static void register() {
                MessageTypes.S2C.PATCHOULI_OPEN_SCREEN = Networking.getNetworkManager().registerS2C(
                        "s2c_patchouli_open_screen",
                        PatchouliOpenScreenMessage::new
                );

            }

        }

    } // end static class Conditional

}
