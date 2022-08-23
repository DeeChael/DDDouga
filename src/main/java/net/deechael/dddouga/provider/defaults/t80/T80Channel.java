package net.deechael.dddouga.provider.defaults.t80;

import net.deechael.dddouga.item.Channel;

import java.util.List;

public class T80Channel implements Channel {

    private final String name;
    private final int index;
    private List<T80Episode> episodes;

    public T80Channel(String name, int index, List<T80Episode> episodes) {
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

    public List<T80Episode> getEpisodes() {
        return episodes;
    }

}
