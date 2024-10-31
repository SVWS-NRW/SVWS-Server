package de.svws_nrw.asd.types.kaoa;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.kaoa.KAOABerufsfeldKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoABerufsfelder.
 */
public enum KAOABerufsfeld implements CoreType<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld> {

	/** KAoA-Berufsfeld: Bau, Architektur, Vermessung */
	BAV,

	/** KAoA-Berufsfeld: Dienstleistung */
	D,

	/** KAoA-Berufsfeld: Elektro */
	EL,

	/** KAoA-Berufsfeld: Gesundheit */
	G,

	/** KAoA-Berufsfeld: Gesellschafts-,Geisteswissenschaften */
	GESGE,

	/** KAoA-Berufsfeld: IT, Computer */
	ITC,

	/** KAoA-Berufsfeld: Kunst, Kultur, Gestaltung */
	KKG,

	/** KAoA-Berufsfeld: Landwirtschaft, Natur, Umwelt */
	LANAUM,

	/** KAoA-Berufsfeld: Metall, Maschinenbau */
	M,

	/** KAoA-Berufsfeld: Medien */
	ME,

	/** KAoA-Berufsfeld: Naturwissenschaft */
	N,

	/** KAoA-Berufsfeld: Produktion, Fertigung */
	PRFE,

	/** KAoA-Berufsfeld: Soziales, Pädagogik */
	SP,

	/** KAoA-Berufsfeld: Technik, Technologiefelder */
	TEC,

	/** KAoA-Berufsfeld: Verkehr, Logistik */
	VL,

	/** KAoA-Berufsfeld: Wirtschaft, Verwaltung */
	WIVE;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld> manager) {
		CoreTypeDataManager.putManager(KAOABerufsfeld.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld> data() {
		return CoreTypeDataManager.getManager(KAOABerufsfeld.class);
	}

	/**
	 * Liefert alle zulässigen KAoA-Berufsfeld-Historien-Einträge in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return alle zulässigen KAoA-Berufsfeld-Historien-Einträge in dem angegebenen Schuljahr.
	 */
	public static @NotNull List<KAOABerufsfeldKatalogEintrag> getEintraegeBySchuljahr(final int schuljahr) {
		final List<KAOABerufsfeldKatalogEintrag> berufsfeldHistorienEintraege = new ArrayList<>();
		final List<KAOABerufsfeld> berufsfelder = KAOABerufsfeld.data().getWerteBySchuljahr(schuljahr);
		for (final KAOABerufsfeld berufsfeld : berufsfelder) {
			final KAOABerufsfeldKatalogEintrag berufsfeldHistorienEintrag = KAOABerufsfeld.data().getEintragBySchuljahrUndWert(schuljahr, berufsfeld);
			if (berufsfeldHistorienEintrag != null)
				berufsfeldHistorienEintraege.add(berufsfeldHistorienEintrag);
		}
		return berufsfeldHistorienEintraege;
	}
}
