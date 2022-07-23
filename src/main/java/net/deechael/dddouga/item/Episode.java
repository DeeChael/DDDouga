package net.deechael.dddouga.item;

public class Episode {

    private final Douga owner;
    private final String name;
    private final String playLink;
    private final int index;

    public Episode(Douga owner, String name, String playLink, int index) {
        this.owner = owner;
        this.name = name;
        this.playLink = playLink;
        this.index = index;
    }

    public Douga getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getPlayLink() {
        return playLink;
    }

    public int getIndex() {
        return index;
    }

}
