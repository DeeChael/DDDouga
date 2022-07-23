package net.deechael.dddouga.item;

import net.deechael.dddouga.DDDouga;
import net.deechael.dddouga.utils.FrameUtils;

import java.awt.*;
import java.util.Objects;

public class Douga {

    private final String name;
    private final String imageURL;
    private final String id;

    private Image image;

    public Douga(String name, String imageURL, String id) {
        this.name = name;
        this.imageURL = imageURL;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        if (image != null)
            return this.image;
        try {
            return this.image = FrameUtils.fromUrl(imageURL);
        } catch (RuntimeException e) {
            this.image = FrameUtils.getToolkit().getImage(Objects.requireNonNull(DDDouga.class.getResource("/notfound.png")));
            return null;
        }
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getId() {
        return id;
    }

}
