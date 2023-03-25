package de.svws_nrw.core.types.kaoa;

import java.util.Arrays;
import java.util.HashMap;

import de.svws_nrw.core.data.kaoa.KAOAMerkmalEintrag;
import de.svws_nrw.core.types.schule.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Merkmale.
 */
public enum KAOAMerkmal {

    /** KAoA-Merkmal: Schulische prozessorientierte Begleitung und Beratung */
    SBO_2_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(35, "SBO 2.1", "Schulische prozessorientierte Begleitung und Beratung", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Berufsorientierende Angebote der Berufsberatung der Bundesagentur für Arbeit (BA) */
    SBO_2_2(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(36, "SBO 2.2", "Berufsorientierende Angebote der Berufsberatung der Bundesagentur für Arbeit (BA)", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Individuelle Beratungsangebote außerschulischer Partner */
    SBO_2_3(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(37, "SBO 2.3", "Individuelle Beratungsangebote außerschulischer Partner", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: STAR - Berufswegekonferenz */
    SBO_2_4(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(38, "SBO 2.4", "STAR - Berufswegekonferenz", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Einbindung von Eltern bzw. Erziehungsberechtigten */
    SBO_2_5(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(39, "SBO 2.5", "Einbindung von Eltern bzw. Erziehungsberechtigten", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: STAR - Einbindung von Eltern bzw. Erziehungsberechtigten */
    SBO_2_6(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(40, "SBO 2.6", "STAR - Einbindung von Eltern bzw. Erziehungsberechtigten", KAOAKategorie.SBO_2, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Portfolioinstrument */
    SBO_3_4(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(41, "SBO 3.4", "Portfolioinstrument", KAOAKategorie.SBO_3, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Potenzialanalyse 1-tägig */
    SBO_4_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(42, "SBO 4.1", "Potenzialanalyse 1-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Lernen und Emotionale soziale Entwicklung– 2-tägig */
    SBO_4_2(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(43, "SBO 4.2", "Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Lernen und Emotionale soziale Entwicklung– 2-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Geistige Entwicklung, Körperliche und motorische Entwicklung, Hören und Kommunikation und Sprache - 2-tägig */
    SBO_4_3(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(44, "SBO 4.3", "STAR – Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Geistige Entwicklung, Körperliche und motorische Entwicklung, Hören und Kommunikation und Sprache - 2-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Feststellung des funktionalen Sehvermögens im Förderschwerpunkt Sehen */
    SBO_4_4(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(45, "SBO 4.4", "STAR – Feststellung des funktionalen Sehvermögens im Förderschwerpunkt Sehen", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Potenzialanalyse im Förderschwerpunkt Sehen – 2-tägig */
    SBO_4_5(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(46, "SBO 4.5", "STAR – Potenzialanalyse im Förderschwerpunkt Sehen – 2-tägig", KAOAKategorie.SBO_4, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Berufsfelderkundungen */
    SBO_5_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(47, "SBO 5.1", "Berufsfelderkundungen", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Berufsfelderkundungen */
    SBO_5_2(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(48, "SBO 5.2", "STAR – Berufsfelderkundungen", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining I im Förderschwerpunkt Hören und Kommunikation */
    SBO_5_3(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(49, "SBO 5.3", "STAR – Arbeitsplatzbezogenes Kommunikationstraining I im Förderschwerpunkt Hören und Kommunikation", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR - Berufsorientierungsseminar */
    SBO_5_4(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(50, "SBO 5.4", "STAR - Berufsorientierungsseminar", KAOAKategorie.SBO_5, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Betriebspraktika in der Sekundarstufe I (ggf. 1 Woche verlagert aus der Oberstufe) */
    SBO_6_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(51, "SBO 6.1", "Betriebspraktika in der Sekundarstufe I (ggf. 1 Woche verlagert aus der Oberstufe)", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Intensivtraining arbeitsrelevanter sozialer Kompetenzen (TASK) */
    SBO_6_2(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(52, "SBO 6.2", "STAR – Intensivtraining arbeitsrelevanter sozialer Kompetenzen (TASK)", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Betriebspraktikum im Block */
    SBO_6_3(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(53, "SBO 6.3", "STAR – Betriebspraktikum im Block", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Praxiskurse */
    SBO_6_4(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(54, "SBO 6.4", "Praxiskurse", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Langzeitpraktikum */
    SBO_6_5(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(55, "SBO 6.5", "Langzeitpraktikum", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Betriebspraktikum in Langzeit */
    SBO_6_6(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(56, "SBO 6.6", "STAR – Betriebspraktikum in Langzeit", KAOAKategorie.SBO_6, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: KAoA-kompakt */
    SBO_7_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(57, "SBO 7.1", "KAoA-kompakt", KAOAKategorie.SBO_7, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07), null, null)
    }),

    /** KAoA-Merkmal: Standortbestimmung - Reflexionsworkshop Sek. II */
    SBO_8_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(58, "SBO 8.1", "Standortbestimmung - Reflexionsworkshop Sek. II", KAOAKategorie.SBO_8, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Stärkung der Entscheidungskompetenz I – Sek. II */
    SBO_8_2(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(59, "SBO 8.2", "Stärkung der Entscheidungskompetenz I – Sek. II", KAOAKategorie.SBO_8, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Praxiselemente in Betrieben, Hochschulen, Institutionen */
    SBO_9_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(60, "SBO 9.1", "Praxiselemente in Betrieben, Hochschulen, Institutionen", KAOAKategorie.SBO_9, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Studienorientierung */
    SBO_9_2(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(61, "SBO 9.2", "Studienorientierung", KAOAKategorie.SBO_9, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Stärkung der Entscheidungskompetenz II - Sek. II */
    SBO_9_3(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(62, "SBO 9.3", "Stärkung der Entscheidungskompetenz II - Sek. II", KAOAKategorie.SBO_9, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Bewerbungsphase */
    SBO_10_1(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(63, "SBO 10.1", "Bewerbungsphase", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining II im Förderschwerpunkt Hören und Kommunikation */
    SBO_10_2(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(64, "SBO 10.2", "STAR – Arbeitsplatzbezogenes Kommunikationstraining II im Förderschwerpunkt Hören und Kommunikation", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: STAR – Betriebsnahes Bewerbungstraining/Umgang mit Dolmetschenden und Technik im Förderschwerpunkt Hören und Kommunikation */
    SBO_10_3(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(65, "SBO 10.3", "STAR – Betriebsnahes Bewerbungstraining/Umgang mit Dolmetschenden und Technik im Förderschwerpunkt Hören und Kommunikation", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Übergangsbegleitung */
    SBO_10_4(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(66, "SBO 10.4", "Übergangsbegleitung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: STAR - Übergangsbegleitung */
    SBO_10_5(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(67, "SBO 10.5", "STAR - Übergangsbegleitung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(),  null, null)
    }),

    /** KAoA-Merkmal: Anschlussvereinbarung */
    SBO_10_6(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(68, "SBO 10.6", "Anschlussvereinbarung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    }),

    /** KAoA-Merkmal: Koordinierte Übergangsgestaltung */
    SBO_10_7(new KAOAMerkmalEintrag[]{
        new KAOAMerkmalEintrag(69, "SBO 10.7", "Koordinierte Übergangsgestaltung", KAOAKategorie.SBO_10, KAOAMerkmaleOptionsarten.KEINE, Arrays.asList(Schulgliederung.A12, Schulgliederung.B06, Schulgliederung.B07, Schulgliederung.C03, Schulgliederung.C05, Schulgliederung.C06, Schulgliederung.D02), null, null)
    });


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;

	/** Die aktuellsten Daten des KAoA-Merkmals */
	public final @NotNull KAOAMerkmalEintrag daten;

	/** Die Historie mit den Einträgen des KAoA-Merkmals */
	public final @NotNull KAOAMerkmalEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull KAOAMerkmal> _statusByID = new HashMap<>();

	/** Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, @NotNull KAOAMerkmal> _statusByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAMerkmalEintrag} ist
	 */
	private KAOAMerkmal(final @NotNull KAOAMerkmalEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von der ID auf das zugehörige Merkmal zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID auf das zugehörige Merkmal
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull KAOAMerkmal> getMapStatusByID() {
		if (_statusByID.size() == 0)
			for (final KAOAMerkmal g : KAOAMerkmal.values())
				_statusByID.put(g.daten.id, g);
		return _statusByID;
	}


	/**
	 * Gibt eine Map von dem Kürzel auf das zugehörige Merkmal zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von dem Kürzel auf das zugehörige Merkmal
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull KAOAMerkmal> getMapStatusByKuerzel() {
		if (_statusByKuerzel.size() == 0)
			for (final KAOAMerkmal g : KAOAMerkmal.values())
				_statusByKuerzel.put(g.daten.kuerzel, g);
		return _statusByKuerzel;
	}


	/**
	 * Gibt das Merkmal anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Merkmals
	 *
	 * @return das Merkmal oder null, falls die ID ungültig ist
	 */
	public static KAOAMerkmal getByID(final Long id) {
		return getMapStatusByID().get(id);
	}


	/**
	 * Gibt das Merkmal anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Merkmals
	 *
	 * @return das Merkmal oder null, falls das Kürzel ungültig ist
	 */
	public static KAOAMerkmal getByKuerzel(final String kuerzel) {
		return getMapStatusByKuerzel().get(kuerzel);
	}

}
