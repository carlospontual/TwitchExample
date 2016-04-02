package com.carlospontual.twitch.list.data.models;

/**
 * Created by carlospontual on 02/04/16.
 */
public class GameLinks {
    public final String self, next;

    public GameLinks(String self, String next) {
        this.self = self;
        this.next = next;
    }
}
