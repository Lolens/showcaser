package io.github.lolens.showcaser.api;

public enum HandlerResult {
    /** Handler created context successfully. Stops further processing */
    SUCCESS,

    /** Handler cant create context and passes screen to another ClientHandler */
    PASS,

    /** Handler cant create context and stops further processing to avoid some unwanted behavior */
    STOP
}
