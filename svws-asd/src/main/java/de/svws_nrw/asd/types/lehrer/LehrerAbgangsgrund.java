package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerAbgangsgrundKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Gründe für das Verlassen der Schule
 * durch Lehrer zur Verfügung.
 */
public enum LehrerAbgangsgrund implements @NotNull CoreType<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund> {

	/** Grund 'Eintritt in den Ruhestand' für das Verlassen der Schule durch den Lehrer */
	RUHEST,

	/** Grund 'Dienst-, Erwerbs-, Berufsunfähigkeit' für das Verlassen der Schule durch den Lehrer */
	UNFAEHIGK,

	/** Grund 'Tod' für das Verlassen der Schule durch den Lehrer */
	TOD,

	/** Grund 'Übertritt in den Schuldienst eines anderen Bundeslandes' für das Verlassen der Schule durch den Lehrer */
	AndBuLand,

	/** Grund 'Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule' für das Verlassen der Schule durch den Lehrer */
	WECHSEL,

	/** Grund 'Befristete Abgänge' für das Verlassen der Schule durch den Lehrer */
	BEFRIST,

	/** Grund 'Sonstige Abgänge' für das Verlassen der Schule durch den Lehrer */
	SONSTIG;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund> manager) {
		CoreTypeDataManager.putManager(LehrerAbgangsgrund.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund> data() {
		return CoreTypeDataManager.getManager(LehrerAbgangsgrund.class);
	}

}
