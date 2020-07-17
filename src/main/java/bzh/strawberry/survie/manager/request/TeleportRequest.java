package bzh.strawberry.survie.manager.request;

import java.util.UUID;

/*
 * This file (TeleportRequest) is part of a project Survie.
 * It was created on 17/07/2020 10:12 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class TeleportRequest {

    private final long requestDate;
    private final UUID sender;
    private final UUID receiver;
    private final boolean teleportHere;

    public TeleportRequest(UUID sender, UUID receiver, boolean here) {
        this.sender = sender;
        this.receiver = receiver;
        this.teleportHere = here;
        this.requestDate = System.currentTimeMillis();
    }

    public long getRequestDate() {
        return requestDate;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public boolean isTeleportHere() {
        return teleportHere;
    }
}
