package com.carlospontual.twitch.list.data.models;

import java.io.Serializable;

/**
 * Created by carlospontual on 02/04/16.
 */
public class GameLinks implements Serializable {
    public final String self, next;

    public GameLinks() {
        this(null, null);
    }

    public GameLinks(String self, String next) {
        this.self = self;
        this.next = next;
    }
}
