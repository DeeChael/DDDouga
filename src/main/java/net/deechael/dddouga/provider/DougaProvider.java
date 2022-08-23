package net.deechael.dddouga.provider;

import net.deechael.dddouga.item.Channel;
import net.deechael.dddouga.item.Douga;

import java.util.List;

/**
 * Douga provider, developers can provide the support of other anime (douga) website
 */
public abstract class DougaProvider {

    private final String name;

    public DougaProvider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<? extends Douga> list(int page);

    public abstract List<? extends Channel> listEpisodes(Douga douga);

}
