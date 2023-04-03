package de.svws_nrw.core.types.lehrer;

import de.svws_nrw.core.data.lehrer.LehrerKatalogAbgangsgrundEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Gründe für das Verlassen der Schule
 * durch Lehrer zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerAbgangsgrund {

	/** Grund 'Eintritt in den Ruhestand' für das Verlassen der Schule durch den Lehrer */
	RUHEST(new LehrerKatalogAbgangsgrundEintrag[]{
		new LehrerKatalogAbgangsgrundEintrag(1, "RUHEST", "Eintritt in den Ruhestand", "11", null, null)
	}),

	/** Grund 'Dienst-, Erwerbs-, Berufsunfähigkeit' für das Verlassen der Schule durch den Lehrer */
	UNFAEHIGK(new LehrerKatalogAbgangsgrundEintrag[]{
		new LehrerKatalogAbgangsgrundEintrag(2, "UNFÄHIGK", "Dienst-, Erwerbs-, Berufsunfähigkeit", "12", null, null)
	}),

	/** Grund 'Tod' für das Verlassen der Schule durch den Lehrer */
	TOD(new LehrerKatalogAbgangsgrundEintrag[]{
		new LehrerKatalogAbgangsgrundEintrag(3, "TOD", "Tod", "13", null, null)
	}),

	/** Grund 'Übertritt in den Schuldienst eines anderen Bundeslandes' für das Verlassen der Schule durch den Lehrer */
	AndBuLand(new LehrerKatalogAbgangsgrundEintrag[]{
		new LehrerKatalogAbgangsgrundEintrag(4, "AndBuLand", "Übertritt in den Schuldienst eines anderen Bundeslandes", "14", null, null)
	}),

	/** Grund 'Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule' für das Verlassen der Schule durch den Lehrer */
	WECHSEL(new LehrerKatalogAbgangsgrundEintrag[]{
		new LehrerKatalogAbgangsgrundEintrag(5, "WECHSEL", "Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule", "15", null, null)
	}),

	/** Grund 'Befristete Abgänge' für das Verlassen der Schule durch den Lehrer */
	BEFRIST(new LehrerKatalogAbgangsgrundEintrag[]{
		new LehrerKatalogAbgangsgrundEintrag(6, "BEFRIST", "Befristete Abgänge", "16", null, null)
	}),

	/** Grund 'Sonstige Abgänge' für das Verlassen der Schule durch den Lehrer */
	SONSTIG(new LehrerKatalogAbgangsgrundEintrag[]{
		new LehrerKatalogAbgangsgrundEintrag(7, "SONSTIG", "Sonstige Abgänge", "17", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten des Abgangsgrundes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogAbgangsgrundEintrag daten;

	/** Die Historie mit den Einträgen des Abgangsgrundes */
	public final @NotNull LehrerKatalogAbgangsgrundEintrag@NotNull[] historie;


	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 *
	 * @param historie   die Historie des Abgangsgrundes, welches ein Array von {@link LehrerKatalogAbgangsgrundEintrag} ist
	 */
	LehrerAbgangsgrund(final @NotNull LehrerKatalogAbgangsgrundEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt den Grund anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Grundes
	 *
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls die ID ungültig ist
	 */
	public static LehrerAbgangsgrund getByID(final int id) {
		for (final @NotNull LehrerAbgangsgrund grund : LehrerAbgangsgrund.values())
			if (grund.daten.id == id)
				return grund;
		return null;
	}


	/**
	 * Gibt den Grund anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Grundes
	 *
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls das kuerzel ungültig ist
	 */
	public static LehrerAbgangsgrund getByKuerzel(final String kuerzel) {
		for (final @NotNull LehrerAbgangsgrund grund : LehrerAbgangsgrund.values())
			if (grund.daten.kuerzel.equals(kuerzel))
				return grund;
		return null;
	}


	/**
	 * Gibt den Grund anhand des angegebenen Schlüssels der
	 * amtlichen Schulstatistik zurück.
	 *
	 * @param schluessel   der Schlüssel der amtlichen Schulstatistik
	 *                     für den Grundes
	 *
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls der Schlüssel ungültig ist
	 */
	public static LehrerAbgangsgrund getByASDSchluessel(final String schluessel) {
		for (final @NotNull LehrerAbgangsgrund grund : LehrerAbgangsgrund.values())
			if (grund.daten.schluessel.equals(schluessel))
				return grund;
		return null;
	}

}
