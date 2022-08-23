package net.deechael.dddouga.provider.defaults.t80;

import net.deechael.dddouga.item.Episode;
import net.deechael.dddouga.utils.T80Utils;

public class T80Episode implements Episode {

    private final T80Douga owner;
    private final String name;
    private final String playLink;
    private final int index;

    public T80Episode(T80Douga owner, String name, String playLink, int index) {
        this.owner = owner;
        this.name = name;
        this.playLink = playLink;
        this.index = index;
    }

    public T80Douga getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getPlayLink() {
        return T80Utils.getPlayLink(this, playLink);
    }

    public int getIndex() {
        return index;
    }

}
