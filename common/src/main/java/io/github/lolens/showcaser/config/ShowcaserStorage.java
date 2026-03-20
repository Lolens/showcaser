package io.github.lolens.showcaser.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShowcaserStorage {

    public static final int CURRENT_VERSION = 1;

    public int version = CURRENT_VERSION;

    public Map<UUID, String> shareBannedPlayer = new HashMap<>();

}
