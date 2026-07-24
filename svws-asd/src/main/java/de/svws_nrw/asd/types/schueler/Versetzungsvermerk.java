package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.VersetzungsvermerkKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Versetzungsvermerke.
 */
public enum Versetzungsvermerk implements CoreType<VersetzungsvermerkKatalogEintrag, Versetzungsvermerk> {

	/** Versetzt */
	VERSETZT,

	/** Versetzt, Anforderungen nicht erfüllt */
	VERSETZT_ANFORERUNGEN_UNERFUELLT,

	/** Vorversetzt */
	VORVERSETZT,

	/** Freiwillig zurück */
	FREIWILLIG_ZURUECK,

	/** Nicht versetzt */
	NICHT_VERSETZT,

	/** Nicht versetzt, Nachprüfung möglich */
	NICHT_VERSETZT_NACHPRUEFUNG,

	/** Abschluss */
	ABSCHLUSS,

	/** Verbleib in der Schuleingangsphase */
	VERBLEIB_SCHULEINGANGSPHASE,

	/** Verbleib in Stufe */
	VERBLEIB_STUFE,

	/** Versetzung auf Probe */
	VERSETZUNG_PROBE;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<VersetzungsvermerkKatalogEintrag, Versetzungsvermerk> manager) {
		CoreTypeDataManager.putManager(Versetzungsvermerk.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die CoreType-Daten zurück.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<VersetzungsvermerkKatalogEintrag, Versetzungsvermerk> data() {
		return CoreTypeDataManager.getManager(Versetzungsvermerk.class);
	}

}
