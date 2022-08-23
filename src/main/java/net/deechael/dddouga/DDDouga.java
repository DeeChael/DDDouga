package net.deechael.dddouga;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.sun.jna.NativeLibrary;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.deechael.dddouga.frame.MainFrame;
import net.deechael.dddouga.provider.DougaProvider;
import net.deechael.dddouga.provider.defaults.t80.T80Provider;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class DDDouga {

    private static Logger logger = LoggerFactory.getLogger(DDDouga.class);

    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static DougaProvider currentProvider = new T80Provider();

    public static OkHttpClient getHttpClient() {
        return HTTP_CLIENT;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static DougaProvider getCurrentProvider() {
        return currentProvider;
    }

    public static void main(String[] args) {
        ch.qos.logback.classic.Logger lgr = (ch.qos.logback.classic.Logger) logger;
        LoggerContext loggerContext = lgr.getLoggerContext();
        // we are not interested in auto-configuration
        loggerContext.reset();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("[%date] [%logger{32}] [%thread] [%level] %message%n");
        encoder.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
        appender.setContext(loggerContext);
        appender.setEncoder(encoder);
        appender.start();

        lgr.addAppender(appender);
        lgr.setLevel(Level.INFO);
        logger = lgr;
        OptionParser parser = new OptionParser() {
            {
                acceptsAll(asList("debug"), "Show the help");
            }
        };
        OptionSet options = null;

        try {
            options = parser.parse(args);
        } catch (joptsimple.OptionException ex) {
            logger.error("Failed to parse options", ex);
        }
        if (options != null) {
            if (options.has("debug")) {
                lgr.setLevel(Level.DEBUG);
            }
        }
        NativeDiscovery discovery = new NativeDiscovery();
        boolean discovered = discovery.discover();
        logger.debug("Discovery status: " + discovered);
        if (discovered) {
            logger.debug("Found VLC at " + discovery.discoveredPath());
            logger.debug("VLC version: " + LibVlc.libvlc_get_version());
        }
        NativeLibrary.addSearchPath("RuntimeUtil.getLibVlcLibraryName()", discovery.discoveredPath());
        logger.debug("Setting up Look And Feel");
        FlatDarculaLaf.setup();
        UIManager.put("Button.arc", 7);
        UIManager.put("Component.arc", 7);
        UIManager.put("CheckBox.arc", 7);
        UIManager.put("ProgressBar.arc", 7);
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("ScrollBar.width", 16);
        logger.debug("Look And Feel was configured");
        new MainFrame();
    }

    private static List<String> asList(String... params) {
        return Arrays.asList(params);
    }
}
