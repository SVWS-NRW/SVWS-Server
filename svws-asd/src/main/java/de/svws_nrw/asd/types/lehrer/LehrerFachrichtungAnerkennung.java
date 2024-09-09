package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Anerkennung von Fachrichtungen von Lehrern dar.
 */
public enum LehrerFachrichtungAnerkennung implements @NotNull CoreType<LehrerFachrichtungAnerkennungKatalogEintrag, LehrerFachrichtungAnerkennung> {

	/** Fachrichtungsanerkennung 'erworben durch LABG/OVP bzw. Laufbahnverordnung'  */
	ID4,

	/** Fachrichtungsanerkennung 'Unterrichtserlaubnis (z. B. Zertifikatskurs)'  */
	ID5,

	/** Fachrichtungsanerkennung 'mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis'  */
	ID6,

	/** Fachrichtungsanerkennung 'sonstige'  */
	ID7;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerFachrichtungAnerkennungKatalogEintrag, LehrerFachrichtungAnerkennung> manager) {
		CoreTypeDataManager.putManager(LehrerFachrichtungAnerkennung.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerFachrichtungAnerkennungKatalogEintrag, LehrerFachrichtungAnerkennung> data() {
		return CoreTypeDataManager.getManager(LehrerFachrichtungAnerkennung.class);
	}

}
