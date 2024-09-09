package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.data.kaoa.KAOAKategorieKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Kategorien.
 */
public enum KAOAKategorie implements CoreType<KAOAKategorieKatalogEintrag, KAOAKategorie> {

	/** KAoA-Kategorie: Formen der Orientierung und Beratung */
	SBO_2,

	/** KAoA-Kategorie: Strukturen an Schulen */
	SBO_3,

	/** KAoA-Kategorie: Potenziale entdecken und den eigenen Standort bestimmen */
	SBO_4,

	/** KAoA-Kategorie: Berufsfelder erkunden und Informationen sammeln */
	SBO_5,

	/** KAoA-Kategorie: Praxis der Arbeitswelt kennenlernen und erproben */
	SBO_6,

	/** KAoA-Kategorie: Nachholung der Erstberufsorientierung */
	SBO_7,

	/** KAoA-Kategorie: Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen */
	SBO_8,

	/** KAoA-Kategorie: Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren */
	SBO_9,

	/** KAoA-Kategorie: Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II */
	SBO_10;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> manager) {
		CoreTypeDataManager.putManager(KAOAKategorie.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> data() {
		return CoreTypeDataManager.getManager(KAOAKategorie.class);
	}

}
