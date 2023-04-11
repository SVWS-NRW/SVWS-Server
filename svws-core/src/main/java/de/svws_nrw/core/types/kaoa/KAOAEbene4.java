package de.svws_nrw.core.types.kaoa;

import java.util.HashMap;

import de.svws_nrw.core.data.kaoa.KAOAEbene4Eintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Einträge der SBO Ebene 4.
 */
public enum KAOAEbene4 {

	/** KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 1-tägig */
	SBO_6_5_1_1(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(1, "SBO 6.5.1.1", "Langzeitpraktikum 1-tägig", KAOAZusatzmerkmal.SBO_6_5_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 2-tägig */
	SBO_6_5_1_2(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(2, "SBO 6.5.1.2", "Langzeitpraktikum 2-tägig", KAOAZusatzmerkmal.SBO_6_5_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Potenzialanalyse teilgenommen */
	SBO_7_1_1_1(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(3, "SBO 7.1.1.1", "An der KAoA-kompakt Potenzialanalyse teilgenommen", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an der KAoA-kompakt Potenzialanalyse */
	SBO_7_1_1_2(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(4, "SBO 7.1.1.2", "Keine Teilnahme an der KAoA-kompakt Potenzialanalyse", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 1. Tag  */
	SBO_7_1_1_3(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(5, "SBO 7.1.1.3", "An der KAoA-kompakt Berufsfelderkundung teilgenommen - 1. Tag ", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 2. Tag  */
	SBO_7_1_1_4(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(6, "SBO 7.1.1.4", "An der KAoA-kompakt Berufsfelderkundung teilgenommen - 2. Tag ", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 3. Tag  */
	SBO_7_1_1_5(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(7, "SBO 7.1.1.5", "An der KAoA-kompakt Berufsfelderkundung teilgenommen - 3. Tag ", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Berufsfelderkundungen */
	SBO_7_1_1_6(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(8, "SBO 7.1.1.6", "Keine Teilnahme an den KAoA-kompakt Berufsfelderkundungen", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: An KAoA-kompakt Praxiskursen teilgenommen */
	SBO_7_1_1_7(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(9, "SBO 7.1.1.7", "An KAoA-kompakt Praxiskursen teilgenommen", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Praxiskursen */
	SBO_7_1_1_8(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(10, "SBO 7.1.1.8", "Keine Teilnahme an den KAoA-kompakt Praxiskursen", KAOAZusatzmerkmal.SBO_7_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Einzeltag */
	SBO_9_1_1_1(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(11, "SBO 9.1.1.1", "Einzeltag", KAOAZusatzmerkmal.SBO_9_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Betriebspraktikum */
	SBO_9_1_1_2(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(12, "SBO 9.1.1.2", "Betriebspraktikum", KAOAZusatzmerkmal.SBO_9_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Auslandspraktikum */
	SBO_9_1_1_3(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(13, "SBO 9.1.1.3", "Auslandspraktikum", KAOAZusatzmerkmal.SBO_9_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Hochschulpraktikum/Schnupperstudium */
	SBO_9_1_1_4(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(14, "SBO 9.1.1.4", "Hochschulpraktikum/Schnupperstudium", KAOAZusatzmerkmal.SBO_9_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Duales Orientierungspraktikum */
	SBO_9_1_1_5(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(15, "SBO 9.1.1.5", "Duales Orientierungspraktikum", KAOAZusatzmerkmal.SBO_9_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Projektworkshop ( bei einem Bildungsträger) */
	SBO_9_1_1_6(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(16, "SBO 9.1.1.6", "Projektworkshop ( bei einem Bildungsträger)", KAOAZusatzmerkmal.SBO_9_1_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung an einer Hochschule */
	SBO_9_2_1_1(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(17, "SBO 9.2.1.1", "Veranstaltungen zur allgemeinen Studienorientierung an einer Hochschule", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung in der Schule */
	SBO_9_2_1_2(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(18, "SBO 9.2.1.2", "Veranstaltungen zur allgemeinen Studienorientierung in der Schule", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Wochen der Studienorientierung */
	SBO_9_2_1_3(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(19, "SBO 9.2.1.3", "Wochen der Studienorientierung", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Langer Abend der Studienberatung */
	SBO_9_2_1_4(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(20, "SBO 9.2.1.4", "Langer Abend der Studienberatung", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Individuelle Einzelberatung durch die Zentralen Studienberatungen der Hochschulen */
	SBO_9_2_1_5(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(21, "SBO 9.2.1.5", "Individuelle Einzelberatung durch die Zentralen Studienberatungen der Hochschulen", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Workshops für Schülerinnen und Schüler in der Zentralen Studienberatung */
	SBO_9_2_1_6(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(22, "SBO 9.2.1.6", "Workshops für Schülerinnen und Schüler in der Zentralen Studienberatung", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Besondere Formate für Schlerinnen und Schüler an der Hochschule (Hochschultag, Hochschulpraktikum i.S. eines „Schnupperstudiums“, allgemeine Boys‘ und Girls‘ Day Angebote) */
	SBO_9_2_1_7(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(23, "SBO 9.2.1.7", "Besondere Formate für Schlerinnen und Schüler an der Hochschule (Hochschultag, Hochschulpraktikum i.S. eines „Schnupperstudiums“, allgemeine Boys‘ und Girls‘ Day Angebote)", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	}),

	/** KAoA-Eintrag der SBO Ebene 4: Fachspezifische Angebote (z. B. Schülerstudium, Praktika bei Hochschullehrer*innen, Schülerlabore, zdi-Zentren, fachspezifische Boys‘ und Girls‘ Day Angebote) */
	SBO_9_2_1_8(new KAOAEbene4Eintrag[]{
			new KAOAEbene4Eintrag(24, "SBO 9.2.1.8", "Fachspezifische Angebote (z. B. Schülerstudium, Praktika bei Hochschullehrer*innen, Schülerlabore, zdi-Zentren, fachspezifische Boys‘ und Girls‘ Day Angebote)", KAOAZusatzmerkmal.SBO_9_2_1,  null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Die aktuellsten Daten des KAoA-Eintrags der SBO Ebene 4 */
	public final @NotNull KAOAEbene4Eintrag daten;

	/** Die Historie mit den Einträgen des KAoA-Eintrags der SBO Ebene 4 */
	public final @NotNull KAOAEbene4Eintrag@NotNull[] historie;

	/** Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull KAOAEbene4> _statusByID = new HashMap<>();

	/** Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, @NotNull KAOAEbene4> _statusByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAEbene4Eintrag} ist
	 */
	KAOAEbene4(final @NotNull KAOAEbene4Eintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von der ID auf den zugehörigen Eintrag der SBO Ebene 4 zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID auf den zugehörigen Eintrag der SBO Ebene 4
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull KAOAEbene4> getMapStatusByID() {
		if (_statusByID.size() == 0)
			for (final KAOAEbene4 g : KAOAEbene4.values())
				_statusByID.put(g.daten.id, g);
		return _statusByID;
	}


	/**
	 * Gibt eine Map von dem Kürzel auf den zugehörigen Eintrag der SBO Ebene 4 zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von dem Kürzel auf den zugehörigen Eintrag der SBO Ebene 4
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull KAOAEbene4> getMapStatusByKuerzel() {
		if (_statusByKuerzel.size() == 0)
			for (final KAOAEbene4 g : KAOAEbene4.values())
				_statusByKuerzel.put(g.daten.kuerzel, g);
		return _statusByKuerzel;
	}


	/**
	 * Gibt den Eintrag der SBO Ebene 4 anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Eintrags der SBO Ebene 4
	 *
	 * @return der Eintrag der SBO Ebene 4 oder null, falls die ID ungültig ist
	 */
	public static KAOAEbene4 getByID(final Long id) {
		return getMapStatusByID().get(id);
	}


	/**
	 * Gibt den Eintrag der SBO Ebene 4 anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Eintrags der SBO Ebene 4
	 *
	 * @return der Eintrag der SBO Ebene 4 oder null, falls das Kürzel ungültig ist
	 */
	public static KAOAEbene4 getByKuerzel(final String kuerzel) {
		return getMapStatusByKuerzel().get(kuerzel);
	}

}
