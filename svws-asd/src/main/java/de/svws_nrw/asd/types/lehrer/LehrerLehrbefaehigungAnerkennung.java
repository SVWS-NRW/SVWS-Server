package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Anerkennung der Lehrbefähigungen eines Lehrers dar.
 */
public enum LehrerLehrbefaehigungAnerkennung implements @NotNull CoreType<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung> {

	/** Anerkennung der Lehrbefähigung 'erworben durch LABG/OVP bzw. Laufbahnverordnung'  */
	ID_1,

	/** Anerkennung der Lehrbefähigung 'Unterrichtserlaubnis (z. B. Zertifikatskurs)'  */
	ID_2,

	/** Anerkennung der Lehrbefähigung 'mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis'  */
	ID_3,

	/** Anerkennung der Lehrbefähigung 'sonstige'  */
	ID_9;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung> manager) {
		CoreTypeDataManager.putManager(LehrerLehrbefaehigungAnerkennung.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung> data() {
		return CoreTypeDataManager.getManager(LehrerLehrbefaehigungAnerkennung.class);
	}

}
