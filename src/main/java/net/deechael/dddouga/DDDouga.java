package net.deechael.dddouga;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.sun.jna.NativeLibrary;
import net.deechael.dddouga.frame.MainFrame;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

import javax.swing.*;
public class DDDouga {

    private final static Logger logger = LoggerFactory.getLogger(DDDouga.class);

    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static OkHttpClient getHttpClient() {
        return HTTP_CLIENT;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        NativeDiscovery discovery = new NativeDiscovery();
        boolean discovered = discovery.discover();
        logger.debug("Discovery status: " + discovered);
        if (discovered) {
            logger.debug("Found VLC at " + discovery.discoveredPath());
            logger.debug("VLC version: " + LibVlc.libvlc_get_version());
        }
        NativeLibrary.addSearchPath("RuntimeUtil.getLibVlcLibraryName()", discovery.discoveredPath());
        FlatDarculaLaf.setup();
        UIManager.put("Button.arc", 7);
        UIManager.put("Component.arc", 7);
        UIManager.put("CheckBox.arc", 7);
        UIManager.put("ProgressBar.arc", 7);
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("ScrollBar.width", 16);
        new MainFrame();
    }

}
