package com.carlospontual.twitch.list.data.models;

/**
 * Created by carlospontual on 02/04/16.
 */
public class GameData {

    public final String name;
    public final int id;
    public final int giantbomb_id;

    public final Images boxImages, logoImages;

    public GameData(String name, int id, int giantbomb_id, Images boxImages, Images logoImages) {
        this.name = name;
        this.id = id;
        this.giantbomb_id = giantbomb_id;
        this.boxImages = boxImages;
        this.logoImages = logoImages;
    }

    public static class Images {
        public final String large, medium, small, template;

        public Images(String large, String medium, String small, String template) {
            this.large = large;
            this.medium = medium;
            this.small = small;
            this.template = template;
        }
    }
}
