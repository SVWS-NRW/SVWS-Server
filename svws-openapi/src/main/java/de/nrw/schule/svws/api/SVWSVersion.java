package de.nrw.schule.svws.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import de.nrw.schule.svws.utils.ResourceUtils;

/**
 * Diese Klasse stellt Hilfs-Methoden rund um die SVWS-Version zur Verfügung. 
 */
public class SVWSVersion {
    
    /** Die Version des SVWS-Servers aus der version.properties (cache-Wert)*/
    private static String _version = null;
    
    /**
     * Ermittelt die Version des SVWS-Servers anhand der version.properties.
     * 
     * @return die Version des SVWS-Servers 
     */
    public static String version() {
        if (_version == null) {
            Properties versionProperties = new Properties();
            Path path = ResourceUtils.getFile("config/version.properties");
            try (InputStream is = Files.newInputStream(path)) {
                versionProperties.load(is);
                _version = versionProperties.getProperty("svws.version");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _version;
    }

}
