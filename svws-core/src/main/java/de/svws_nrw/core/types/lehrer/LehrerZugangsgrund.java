package de.svws_nrw.core.types.lehrer;

import de.svws_nrw.core.data.lehrer.LehrerKatalogZugangsgrundEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Gründe für das Kommen von
 * Lehrern an die Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerZugangsgrund {

	/** Grund 'Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung' für das Kommen des Lehrers an die Schule */
	NEU(new LehrerKatalogZugangsgrundEintrag[]{ 
		new LehrerKatalogZugangsgrundEintrag(1, "NEU", "Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung", "1", null, null)
	}),

	/** Grund 'Übertritt aus dem Schuldienst eines anderen Bundeslandes' für das Kommen des Lehrers an die Schule */
	AndBuLand(new LehrerKatalogZugangsgrundEintrag[]{ 
		new LehrerKatalogZugangsgrundEintrag(2, "AndBuLand", "Übertritt aus dem Schuldienst eines anderen Bundeslandes", "2", null, null)
	}),

	/** Grund 'Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule' für das Kommen des Lehrers an die Schule */
	WECHSEL(new LehrerKatalogZugangsgrundEintrag[]{ 
		new LehrerKatalogZugangsgrundEintrag(3, "WECHSEL", "Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule", "3", null, null)
	}),

	/** Grund 'Wiedereintritt in den Schuldienst' für das Kommen des Lehrers an die Schule */
	WIEDER(new LehrerKatalogZugangsgrundEintrag[]{ 
		new LehrerKatalogZugangsgrundEintrag(4, "WIEDER", "Wiedereintritt in den Schuldienst", "4", null, null)
	}),

	/** Grund 'Sonstige Zugänge' für das Kommen des Lehrers an die Schule */
	SONSTIG(new LehrerKatalogZugangsgrundEintrag[]{ 
		new LehrerKatalogZugangsgrundEintrag(5, "SONSTIG", "Sonstige Zugänge", "5", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten des Zugangsgrundes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogZugangsgrundEintrag daten;
	
	/** Die Historie mit den Einträgen des Zugangsgrundes */
	public final @NotNull LehrerKatalogZugangsgrundEintrag@NotNull[] historie;


	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 * 
	 * @param historie   die Historie des Zugangsgrundes, welches ein Array von {@link LehrerKatalogZugangsgrundEintrag} ist  
	 */
	private LehrerZugangsgrund(final @NotNull LehrerKatalogZugangsgrundEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}
	
	
	/**
	 * Gibt den Grund anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Grundes
	 * 
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls die ID ungültig ist
	 */
	public static LehrerZugangsgrund getByID(final long id) {
		for (final @NotNull LehrerZugangsgrund grund : LehrerZugangsgrund.values())
			if (grund.daten.id == id)
				return grund;
		return null;
	}
	
	
	/**
	 * Gibt den Grund anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Grundes
	 * 
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerZugangsgrund getByKuerzel(final String kuerzel) {
		for (final @NotNull LehrerZugangsgrund grund : LehrerZugangsgrund.values())
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
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls der Schlüssel ungültig ist
	 */
	public static LehrerZugangsgrund getByASDSchluessel(final String schluessel) {
		for (final @NotNull LehrerZugangsgrund grund : LehrerZugangsgrund.values())
			if (grund.daten.schluessel.equals(schluessel))
				return grund;
		return null;
	}
	
}
