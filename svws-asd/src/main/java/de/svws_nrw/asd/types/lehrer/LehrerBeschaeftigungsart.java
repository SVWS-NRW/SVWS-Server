package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Beschäftigungsarten von Lehrkräften
 * an der Schule zur Verfügung.
 */
public enum LehrerBeschaeftigungsart implements @NotNull CoreType<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart> {

	/** Beschaeftigungsart 'Vollzeit' eines Lehrers */
	V,

	/** Beschaeftigungsart 'Teilzeit' eines Lehrers */
	T,

	/** Beschaeftigungsart 'Altersteilzeit (Beschäftigungsphase)' eines Lehrers */
	AT,

	/** Beschaeftigungsart 'Altersteilzeit, vorm. teilzeitbeschäftigt (Verzichtsphase Altersermäßigung)' eines Lehrers */
	TA,

	/** Beschaeftigungsart 'Altersteilzeit, vorm. vollzeitbeschäftigt (Verzichtsphase Altersermäßigung)' eines Lehrers */
	VA,

	/** Beschaeftigungsart 'Sabbatjahr' eines Lehrers */
	TS,

	/** Beschaeftigungsart 'Nebenberufliche Beschäftigung' eines Lehrers */
	SB,

	/** Beschaeftigungsart 'Geringfügige Beschäftigung' eines Lehrers */
	GB,

	/** Beschaeftigungsart 'Studierende' eines Lehrers */
	ST,

	/** Beschaeftigungsart 'Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)' eines Lehrers */
	NA,

	/** Beschaeftigungsart 'Gestellungsvertrag' eines Lehrers */
	G,

	/** Beschaeftigungsart 'Unentgeltlich Beschäftigte' eines Lehrers */
	X,

	/** Beschaeftigungsart 'Beamte auf Widerruf (LAA) in Teilzeit' eines Lehrers */
	WT,

	/** Beschaeftigungsart 'Beamte auf Widerruf (LAA) in Vollzeit' eines Lehrers */
	WV;
	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart> manager) {
		CoreTypeDataManager.putManager(LehrerBeschaeftigungsart.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart> data() {
		return CoreTypeDataManager.getManager(LehrerBeschaeftigungsart.class);
	}

}
