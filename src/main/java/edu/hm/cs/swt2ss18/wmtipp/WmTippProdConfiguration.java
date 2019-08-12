
package edu.hm.cs.swt2ss18.wmtipp;

import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Diese Konfiguration enthält Spezifika für eine Produktive Umgebung. Dazu gehört die Aktivierung
 * der Autorisierung auf Methodenebene.
 * 
 * @author katz.bastian
 */
@Profile("!test")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WmTippProdConfiguration {

}
