package de.nrw.schule.svws.core.types.kaoa;

import java.util.Arrays;
import java.util.HashMap;

import de.nrw.schule.svws.core.data.kaoa.KAOAKategorieEintrag;
import de.nrw.schule.svws.core.types.jahrgang.Jahrgaenge;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Kategorien.
 */
public enum KAOAKategorie {

    /** KAoA-Kategorie: Formen der Orientierung und Beratung */
    SBO_2(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(6, "SBO 2", "Formen der Orientierung und Beratung", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09, Jahrgaenge.JG_10, Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)
    }),

    /** KAoA-Kategorie: Strukturen an Schulen */
    SBO_3(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(7, "SBO 3", "Strukturen an Schulen", Arrays.asList(Jahrgaenge.JG_00), null, null)
    }),

    /** KAoA-Kategorie: Potenziale entdecken und den eigenen Standort bestimmen */
    SBO_4(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(8, "SBO 4", "Potenziale entdecken und den eigenen Standort bestimmen", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09), null, null)
    }),

    /** KAoA-Kategorie: Berufsfelder erkunden und Informationen sammeln */
    SBO_5(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(9, "SBO 5", "Berufsfelder erkunden und Informationen sammeln", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09), null, null)
    }),

    /** KAoA-Kategorie: Praxis der Arbeitswelt kennenlernen und erproben */
    SBO_6(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(10, "SBO 6", "Praxis der Arbeitswelt kennenlernen und erproben", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09, Jahrgaenge.JG_10), null, null)
    }),

    /** KAoA-Kategorie: Nachholung der Erstberufsorientierung */
    SBO_7(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(11, "SBO 7", "Nachholung der Erstberufsorientierung", Arrays.asList(Jahrgaenge.JG_10), null, null)
    }),

    /** KAoA-Kategorie: Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen */
    SBO_8(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(12, "SBO 8", "Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen", Arrays.asList(Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)
    }),

    /** KAoA-Kategorie: Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren */
    SBO_9(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(13, "SBO 9", "Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren", Arrays.asList(Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)
    }),

    /** KAoA-Kategorie: Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II */
    SBO_10(new KAOAKategorieEintrag[]{
        new KAOAKategorieEintrag(14, "SBO 10", "Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II", Arrays.asList(Jahrgaenge.JG_09, Jahrgaenge.JG_10, Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)
    });


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Die aktuellsten Daten der KAoA-Kategorie*/
	public final @NotNull KAOAKategorieEintrag daten;
	
	/** Die Historie mit den Einträgen der KAoA-Kategorie */
	public final @NotNull KAOAKategorieEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull KAOAKategorie> _statusByID = new HashMap<>();

	/** Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, @NotNull KAOAKategorie> _statusByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAKategorieEintrag} ist  
	 */
	private KAOAKategorie(@NotNull KAOAKategorieEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von der ID auf die zugehörige Kategorie zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von der ID auf die zugehörige Kategorie
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull KAOAKategorie> getMapStatusByID() {
		if (_statusByID.size() == 0)
			for (KAOAKategorie g : KAOAKategorie.values())
				_statusByID.put(g.daten.id, g);				
		return _statusByID;
	}

	
	/**
	 * Gibt eine Map von dem Kürzel auf die zugehörige Kategorie zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von dem Kürzel auf die zugehörige Kategorie
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull KAOAKategorie> getMapStatusByKuerzel() {
		if (_statusByKuerzel.size() == 0)
			for (KAOAKategorie g : KAOAKategorie.values())
				_statusByKuerzel.put(g.daten.kuerzel, g);				
		return _statusByKuerzel;
	}
		

	/**
	 * Gibt die Kategorie anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Kategorie
	 * 
	 * @return die Kategorie oder null, falls die ID ungültig ist
	 */
	public static KAOAKategorie getByID(long id) {
		return getMapStatusByID().get(id);
	}


	/**
	 * Gibt die Kategorie anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Kategorie
	 * 
	 * @return die Kategorie oder null, falls das Kürzel ungültig ist
	 */
	public static KAOAKategorie getByKuerzel(String kuerzel) {
		return getMapStatusByKuerzel().get(kuerzel);
	}

}
