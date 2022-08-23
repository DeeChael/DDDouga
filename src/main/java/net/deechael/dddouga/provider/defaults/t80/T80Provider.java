package net.deechael.dddouga.provider.defaults.t80;

import net.deechael.dddouga.DDDouga;
import net.deechael.dddouga.item.Douga;
import net.deechael.dddouga.provider.DougaProvider;
import net.deechael.dddouga.utils.T80Utils;
import org.slf4j.Logger;

import java.util.List;

public class T80Provider extends DougaProvider {

    public T80Provider() {
        super("T80");
    }

    @Override
    public List<T80Douga> list(int page) {
        return T80Utils.getPage(page);
    }

    @Override
    public List<T80Channel> listEpisodes(Douga douga) {
        Logger logger = DDDouga.getLogger();
        if (!(douga instanceof T80Douga))
            logger.error("Please provider a T80 type douga", new RuntimeException("The douga type is not supported by the provider"));
        assert douga instanceof T80Douga;
        return T80Utils.getEpisodes((T80Douga) douga);
    }

}
