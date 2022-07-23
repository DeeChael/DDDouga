package net.deechael.dddouga.item;

import java.util.List;

/**
 * A.K.A. Video Source
 */
public class Channel {

    private final String name;
    private final int index;
    private List<Episode> episodes;

    public Channel(String name, int index, List<Episode> episodes) {
        this.name = name;
        this.index = index;
        this.episodes = episodes;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

}
