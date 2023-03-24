package de.nrw.schule.svws.core.types.kaoa;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.kaoa.KAOABerufsfeldEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Berufsfelder.
 */
public enum KAOABerufsfeld {

    /** KAoA-Berufsfeld: Bau, Architektur, Vermessung */
    BAV(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(1, "BAV", "Bau, Architektur, Vermessung", null, null)
    }),

    /** KAoA-Berufsfeld: Dienstleistung */
    D(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(2, "D", "Dienstleistung", null, null)
    }),

    /** KAoA-Berufsfeld: Elektro */
    EL(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(3, "EL", "Elektro", null, null)
    }),

    /** KAoA-Berufsfeld: Gesundheit */
    G(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(4, "G", "Gesundheit", null, null)
    }),

    /** KAoA-Berufsfeld: Gesellschafts-,Geisteswissenschaften */
    GESGE(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(5, "GESGE", "Gesellschafts-,Geisteswissenschaften", null, null)
    }),

    /** KAoA-Berufsfeld: IT, Computer */
    ITC(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(6, "ITC", "IT, Computer", null, null)
    }),

    /** KAoA-Berufsfeld: Kunst, Kultur, Gestaltung */
    KKG(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(7, "KKG", "Kunst, Kultur, Gestaltung", null, null)
    }),

    /** KAoA-Berufsfeld: Landwirtschaft, Natur, Umwelt */
    LANAUM(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(8, "LANAUM", "Landwirtschaft, Natur, Umwelt", null, null)
    }),

    /** KAoA-Berufsfeld: Metall, Maschinenbau */
    M(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(9, "M", "Metall, Maschinenbau", null, null)
    }),

    /** KAoA-Berufsfeld: Medien */
    ME(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(10, "ME", "Medien", null, null)
    }),

    /** KAoA-Berufsfeld: Naturwissenschaft */
    N(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(11, "N", "Naturwissenschaft", null, null)
    }),

    /** KAoA-Berufsfeld: Produktion, Fertigung */
    PRFE(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(12, "PRFE", "Produktion, Fertigung", null, null)
    }),

    /** KAoA-Berufsfeld: Soziales, Pädagogik */
    SP(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(13, "SP", "Soziales, Pädagogik", null, null)
    }),

    /** KAoA-Berufsfeld: Technik, Technologiefelder */
    TEC(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(14, "TEC", "Technik, Technologiefelder", null, null)
    }),

    /** KAoA-Berufsfeld: Verkehr, Logistik */
    VL(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(15, "VL", "Verkehr, Logistik", null, null)
    }),

    /** KAoA-Berufsfeld: Wirtschaft, Verwaltung */
    WIVE(new KAOABerufsfeldEintrag[]{
        new KAOABerufsfeldEintrag(16, "WIVE", "Wirtschaft, Verwaltung", null, null)
    });


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Die aktuellsten Daten des KAoA-Berufsfeldes */
	public final @NotNull KAOABerufsfeldEintrag daten;
	
	/** Die Historie mit den Einträgen des KAoA-Berufsfeldes */
	public final @NotNull KAOABerufsfeldEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull KAOABerufsfeld> _statusByID = new HashMap<>();

	/** Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, @NotNull KAOABerufsfeld> _statusByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOABerufsfeldEintrag} ist  
	 */
	private KAOABerufsfeld(final @NotNull KAOABerufsfeldEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von der ID auf das zugehörige Berufsfeld zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von der ID auf das zugehörige Berufsfeld
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull KAOABerufsfeld> getMapStatusByID() {
		if (_statusByID.size() == 0)
			for (final KAOABerufsfeld g : KAOABerufsfeld.values())
				_statusByID.put(g.daten.id, g);				
		return _statusByID;
	}

	
	/**
	 * Gibt eine Map von dem Kürzel auf das zugehörige Berufsfeld zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von dem Kürzel auf das zugehörige Berufsfeld
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull KAOABerufsfeld> getMapStatusByKuerzel() {
		if (_statusByKuerzel.size() == 0)
			for (final KAOABerufsfeld g : KAOABerufsfeld.values())
				_statusByKuerzel.put(g.daten.kuerzel, g);				
		return _statusByKuerzel;
	}
		

	/**
	 * Gibt das Berufsfeld anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Berufsfeldes
	 * 
	 * @return das Berufsfeld oder null, falls die ID ungültig ist
	 */
	public static KAOABerufsfeld getByID(final long id) {
		return getMapStatusByID().get(id);
	}


	/**
	 * Gibt das Berufsfeld anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Berufsfeldes
	 * 
	 * @return das Berufsfeld oder null, falls das Kürzel ungültig ist
	 */
	public static KAOABerufsfeld getByKuerzel(final String kuerzel) {
		return getMapStatusByKuerzel().get(kuerzel);
	}

}
