package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.data.kaoa.KAOAAnschlussoptionenKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Anschlussoptionen für Anschlussvereinbarungen nach SBO 10.7.
 */
public enum KAOAAnschlussoptionen implements CoreType<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> {

	/** KAoA-Anschlussoption: Berufspraxisstufe einer Förderschule für Geistige Entwicklung */
	BE,

	/** KAoA-Anschlussoption: Gymnasiale Oberstufe der Gesamtschule oder des Gymnasiums */
	GY,

	/** KAoA-Anschlussoption: Berufskolleg - Einjährige Ausbildungsvorbereitung in Vollzeit (AV) - Anlage A */
	AV,

	/** KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ I (BFS I) - Anlage B */
	BFSI,

	/** KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ II (BFS II) - Anlage B */
	BFSII,

	/** KAoA-Anschlussoption: Berufskolleg - Zweijährige [Höhere] Berufsfachschule - Anlage C */
	HOEBFS,

	/** KAoA-Anschlussoption: Berufskolleg - Zweijährige Fachoberschule (FOS 11/12) - Anlage C */
	FOS,

	/** KAoA-Anschlussoption: Berufskolleg - Berufliches Gymnasium - Anlage D */
	BERUFGY,

	/** KAoA-Anschlussoption: Duale Berufsausbildung (inkl. Beamtenlaufbahn im mittleren Dienst) */
	DUAL,

	/** KAoA-Anschlussoption: Berufskolleg - schulische Ausbildung zwei- oder dreijährig in der Berufsfachschule Anlage B oder C oder am Beruflichen Gymnasium */
	SCHUAUS,

	/** KAoA-Anschlussoption: Schulische Ausbildung in Berufen des Gesundheitswesens und der Altenpflege */
	SCHUGA,

	/** KAoA-Anschlussoption: Berufsausbildung in einer außerbetrieblichen Einrichtung (BaE kooperativ/integrativ) */
	BAE,

	/** KAoA-Anschlussoption: Betriebliche Ausbildung in gesondert geregelten Berufen (Fachpraktikerausbildung/Werkerausbildung) für Jugendliche mit Behinderung */
	FACHAUS,

	/** KAoA-Anschlussoption: Berufsvorbereitende Bildungsmaßnahme der Agentur für Arbeit (BvB), auch rehaspezifisch */
	BVB,

	/** KAoA-Anschlussoption: Einstiegsqualifizierung (EQ und EQ plus) */
	EQ,

	/** KAoA-Anschlussoption: Werkstattjahr.NRW */
	WERK,

	/** KAoA-Anschlussoption: Jugendwerkstatt */
	JUWERK,

	/** KAoA-Anschlussoption: weitere Maßnahmen gemäß SGB II/III/VIII (z. B. Aktivierungshilfen, Projekte der Jugendberufshilfe)  */
	MASS,

	/** KAoA-Anschlussoption: Abendrealschule oder VHS zum Nachholen des Schulabschlusses */
	VHS,

	/** KAoA-Anschlussoption: betriebliche Langzeitpraktika (ohne EQ) (z.B. zum Erwerb der vollen FHR) */
	PRAKT,

	/** KAoA-Anschlussoption: Freiwilligendienste, Freiwilliger Wehrdienst/Laufbahn Bundeswehr und ähnliche Anschlussoptionen */
	FREI,

	/** KAoA-Anschlussoption: Sozialversicherungspflichtige Beschäftigung als ungelernte Kraft */
	SVP,

	/** KAoA-Anschlussoption: Außerbetriebliche Ausbildung für Menschen mit Behinderung im Berufsbildungswerk (BBW) */
	BBW,

	/** KAoA-Anschlussoption: Begleitete betriebliche Ausbildung (bbA) für Menschen mit Behinderung */
	BBA,

	/** KAoA-Anschlussoption: Unterstützte Beschäftigung (UB) für Menschen mit Behinderung */
	UB,

	/** KAoA-Anschlussoption: Diagnose der Arbeitsmarktfähigkeit besonders betroffener behinderter Menschen (DIA-AM) */
	DIAAM,

	/** KAoA-Anschlussoption: Maßnahmen im Eingangsverfahren und im Berufsbildungsbereich in Werkstätten für behinderte Menschen (WfbM) */
	WFBM,

	/** KAoA-Anschlussoption: Betreuung in Tagesförderstätten für schwerst- und schwermehrfachbehinderte Menschen */
	TAG,

	/** KAoA-Anschlussoption: Verbleib zu Hause bei einer Schwerst- und Schwermehrfachbehinderung */
	HAUSE,

	/** KAoA-Anschlussoption: weitere rehaspezifische Maßnahmen für Menschen mit Behinderung */
	REHA,

	/** KAoA-Anschlussoption: Minijob */
	MINI,

	/** KAoA-Anschlussoption: Schwangerschaft/Elternzeit */
	SCHWAN,

	/** KAoA-Anschlussoption: Schulabsenz, daher Verbleib unbekannt */
	ABSENZ,

	/** KAoA-Anschlussoption: Verbleib unbekannt - andere Gründe */
	UNBE,

	/** KAoA-Anschlussoption: Noch kein Anschluss */
	NOKEAN,

	/** KAoA-Anschlussoption: Duales Studium (inkl. Beamtenausbildung im gehobenen Dienst) */
	DUASTUD,

	/** KAoA-Anschlussoption: Hochschulstudium */
	STUD;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> manager) {
		CoreTypeDataManager.putManager(KAOAAnschlussoptionen.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> data() {
		return CoreTypeDataManager.getManager(KAOAAnschlussoptionen.class);
	}

}
