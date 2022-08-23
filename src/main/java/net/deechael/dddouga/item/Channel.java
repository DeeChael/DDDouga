package net.deechael.dddouga.item;

import java.util.List;

/**
 * A.K.A. Video Source
 */
public interface Channel {

    String getName();

    int getIndex();

    List<? extends Episode> getEpisodes();


}
