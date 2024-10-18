package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerZugangsgrundKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Gründe für das Kommen von Lehrern an die Schule.
 */
public enum LehrerZugangsgrund implements @NotNull CoreType<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund> {

	/** Grund 'Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung' für das Kommen des Lehrers an die Schule */
	NEU,

	/** Grund 'Übertritt aus dem Schuldienst eines anderen Bundeslandes' für das Kommen des Lehrers an die Schule */
	AndBuLand,

	/** Grund 'Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule' für das Kommen des Lehrers an die Schule */
	WECHSEL,

	/** Grund 'Wiedereintritt in den Schuldienst' für das Kommen des Lehrers an die Schule */
	WIEDER,

	/** Grund 'Sonstige Zugänge' für das Kommen des Lehrers an die Schule */
	SONSTIG;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund> manager) {
		CoreTypeDataManager.putManager(LehrerZugangsgrund.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund> data() {
		return CoreTypeDataManager.getManager(LehrerZugangsgrund.class);
	}

}
