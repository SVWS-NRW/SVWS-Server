package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Lehramtsanerkennung von Lehrkräften
 * an der Schule zur Verfügung.
 */
public enum LehrerLehramtAnerkennung implements @NotNull CoreType<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung> {

	/** Lehramtsanerkennung 'Zweite Staatsprüfung für ein Lehramt' */
	ST,

	/** Lehramtsanerkennung 'Anerkennung Lehramt' */
	AL,

	/** Lehramtsanerkennung 'Anerkennung geeignete Prüfung' */
	AP,

	/** Lehramtsanerkennung 'Förderliche Berufstätigkeit' */
	BT,

	/** Lehramtsanerkennung 'ohne' */
	OH;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung> manager) {
		CoreTypeDataManager.putManager(LehrerLehramtAnerkennung.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung> data() {
		return CoreTypeDataManager.getManager(LehrerLehramtAnerkennung.class);
	}

}
