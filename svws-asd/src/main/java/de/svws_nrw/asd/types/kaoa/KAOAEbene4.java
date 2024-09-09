package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.data.kaoa.KAOAEbene4KatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Einträge der SBO Ebene 4.
 */
public enum KAOAEbene4 implements CoreType<KAOAEbene4KatalogEintrag, KAOAEbene4> {

	/** KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 1-tägig */
	SBO_6_5_1_1,

	/** KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 2-tägig */
	SBO_6_5_1_2,

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Potenzialanalyse teilgenommen */
	SBO_7_1_1_1,

	/** KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an der KAoA-kompakt Potenzialanalyse */
	SBO_7_1_1_2,

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 1. Tag  */
	SBO_7_1_1_3,

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 2. Tag  */
	SBO_7_1_1_4,

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 3. Tag  */
	SBO_7_1_1_5,

	/** KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Berufsfelderkundungen */
	SBO_7_1_1_6,

	/** KAoA-Eintrag der SBO Ebene 4: An KAoA-kompakt Praxiskursen teilgenommen */
	SBO_7_1_1_7,

	/** KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Praxiskursen */
	SBO_7_1_1_8,

	/** KAoA-Eintrag der SBO Ebene 4: Einzeltag */
	SBO_9_1_1_1,

	/** KAoA-Eintrag der SBO Ebene 4: Betriebspraktikum */
	SBO_9_1_1_2,

	/** KAoA-Eintrag der SBO Ebene 4: Auslandspraktikum */
	SBO_9_1_1_3,

	/** KAoA-Eintrag der SBO Ebene 4: Hochschulpraktikum/Schnupperstudium */
	SBO_9_1_1_4,

	/** KAoA-Eintrag der SBO Ebene 4: Duales Orientierungspraktikum */
	SBO_9_1_1_5,

	/** KAoA-Eintrag der SBO Ebene 4: Projektworkshop ( bei einem Bildungsträger) */
	SBO_9_1_1_6,

	/** KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung an einer Hochschule */
	SBO_9_2_1_1,

	/** KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung in der Schule */
	SBO_9_2_1_2,

	/** KAoA-Eintrag der SBO Ebene 4: Wochen der Studienorientierung */
	SBO_9_2_1_3,

	/** KAoA-Eintrag der SBO Ebene 4: Langer Abend der Studienberatung */
	SBO_9_2_1_4,

	/** KAoA-Eintrag der SBO Ebene 4: Individuelle Einzelberatung durch die Zentralen Studienberatungen der Hochschulen */
	SBO_9_2_1_5,

	/** KAoA-Eintrag der SBO Ebene 4: Workshops für Schülerinnen und Schüler in der Zentralen Studienberatung */
	SBO_9_2_1_6,

	/** KAoA-Eintrag der SBO Ebene 4: Besondere Formate für Schülerinnen und Schüler an der Hochschule (Hochschultag, Hochschulpraktikum i.S. eines „Schnupperstudiums“, allgemeine Boys‘ und Girls‘ Day Angebote) */
	SBO_9_2_1_7,

	/** KAoA-Eintrag der SBO Ebene 4: Fachspezifische Angebote (z. B. Schülerstudium, Praktika bei Hochschullehrer*innen, Schülerlabore, zdi-Zentren, fachspezifische Boys‘ und Girls‘ Day Angebote) */
	SBO_9_2_1_8;
	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAEbene4KatalogEintrag, KAOAEbene4> manager) {
		CoreTypeDataManager.putManager(KAOAEbene4.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAEbene4KatalogEintrag, KAOAEbene4> data() {
		return CoreTypeDataManager.getManager(KAOAEbene4.class);
	}

}
