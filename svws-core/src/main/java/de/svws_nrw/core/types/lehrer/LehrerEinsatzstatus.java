package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogEinsatzstatusEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für den Einsatzstatus für
 * Lehrer an der Schule zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerEinsatzstatus {

	/** Einsatzstatus: 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' */
	A(new LehrerKatalogEinsatzstatusEintrag[]{
		new LehrerKatalogEinsatzstatusEintrag(1, "A", "Stammschule, ganz oder teilweise auch an anderen Schulen tätig", null, null)
	}),

	/** Einsatzstatus: 'nicht Stammschule, aber auch hier tätig' */
	B(new LehrerKatalogEinsatzstatusEintrag[]{
		new LehrerKatalogEinsatzstatusEintrag(2, "B", "nicht Stammschule, aber auch hier tätig", null, null)
	}),

	/** Einsatzstatus: 'Stammschule, nur hier tätig' */
	DEFAULT(new LehrerKatalogEinsatzstatusEintrag[]{
		new LehrerKatalogEinsatzstatusEintrag(3, "*", "Stammschule, nur hier tätig", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten des Einsatzstatus, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogEinsatzstatusEintrag daten;

	/** Die Historie mit den Einträgen des Einsatzstatus */
	public final @NotNull LehrerKatalogEinsatzstatusEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen Einsatzstatus, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerEinsatzstatus> _statusByID = new HashMap<>();

	/** Eine Hashmap mit allen Einsatzstatus, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerEinsatzstatus> _statusByKuerzel = new HashMap<>();


	/**
	 * Erzeugt einen neuen Einsatzstatus in der Aufzählung.
	 *
	 * @param historie   die Historie des Einsatzstatus, welches ein Array von {@link LehrerKatalogEinsatzstatusEintrag} ist
	 */
	LehrerEinsatzstatus(final @NotNull LehrerKatalogEinsatzstatusEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von der ID des Einsatzstatus auf den zugehörigen Einsatzstatus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID des Einsatzstatus auf den zugehörigen Einsatzstatus
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerEinsatzstatus> getMapStatusByID() {
		if (_statusByID.size() == 0)
			for (final LehrerEinsatzstatus g : LehrerEinsatzstatus.values())
				_statusByID.put(g.daten.id, g);
		return _statusByID;
	}


	/**
	 * Gibt eine Map von de Kürzel des Einsatzstatus auf den zugehörigen Einsatzstatus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von de Kürzel des Einsatzstatus auf den zugehörigen Einsatzstatus
	 */
	private static @NotNull HashMap<@NotNull String, LehrerEinsatzstatus> getMapStatusByKuerzel() {
		if (_statusByKuerzel.size() == 0)
			for (final LehrerEinsatzstatus g : LehrerEinsatzstatus.values())
				_statusByKuerzel.put(g.daten.kuerzel, g);
		return _statusByKuerzel;
	}


	/**
	 * Gibt den Einsatzstatus anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Einsatzstatus
	 *
	 * @return der Einsatzstatus oder null, falls die ID ungültig ist
	 */
	public static LehrerEinsatzstatus getByID(final long id) {
		return getMapStatusByID().get(id);
	}


	/**
	 * Gibt den Einsatzstatus anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Einsatzstatus
	 *
	 * @return der Einsatzstatus oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerEinsatzstatus getByKuerzel(final String kuerzel) {
		return getMapStatusByKuerzel().get(kuerzel);
	}


}
