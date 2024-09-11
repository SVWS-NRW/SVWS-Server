package de.svws_nrw.asd.types.kurse;

import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.schule.Schulform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für zulässigen Kursarten.
 */
public enum ZulaessigeKursart implements CoreType<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart> {

	/** Kursart 3. Abiturfach */
	AB3,

	/** Kursart 4. Abiturfach */
	AB4,

	/** Kursart Arbeitsgemeinschaft gemäß APO SI */
	AG,

	/** Kursart Arbeitsgemeinschaft im Ganztagsbereich */
	AGGT,

	/** Kursart Stütz- und Angleichungskurs / Förderunterricht */
	AGKWB,

	/** Kursart Angleichungskurs */
	AGK,

	/** Kursart Arbeits- bzw. Übungsstunde  */
	AST,

	/** Kursart Begegnung mit Sprachen in der Primarstufe */
	BSP,

	/** Kursart Unterricht im Rahmen von KAOA einschl. Schule trifft Arbeitswelt */
	BUS,

	/** Kursart Erweiterungsebene/-kurs */
	E,

	/** Kursart Erweiterungskurs – Bildungsgang Hauptschule */
	E_H,

	/** Kursart Erweitertes Bildungsangebot */
	EBA,

	/** Kursart Einführung in die 2. Fremdsprache oder Ersatzfach */
	EF2,

	/** Kursart Ersatzfach für Sport als 4. Abiturfach */
	EFSP,

	/** Kursart Ergänzungsstunden */
	EGS1,

	/** Kursart Ergänzungsstunden mit Benotung */
	EGSN,

	/** Kursart Ergänzungs- oder Vertiefungskurs */
	EV,

	/** Kursart Erweiterungsunterricht Wahlbereich - berufsbezogen, fachübergreifend */
	EWBF,

	/** Kursart Erweiterungsunterricht Wahlbereich - fach-, jedoch nicht abschlussbezogen */
	EWF,

	/** Kursart Erweiterungsunterricht Wahlbereich - fach- und abschlussbezogen */
	EWFA,

	/** Kursart Wahlplflichtunterricht: Einzelfach im math.-naturw., techn., gesellschaftsw., künstlerischen Schwerpunkt */
	F3,

	/** Kursart freiwillige Arbeitsgemeinschaft */
	FAG,

	/** Kursart Fachbezogener Förderunterricht */
	FFU,

	/** Kursart Förderangebot Ganztagsschule */
	FOGT,

	/** Kursart Förderung neu zugewanderter Schüler in Deutschfördergruppen (teilweise äußere und innere Differenzierung) */
	DFG,

	/** Kursart Wahlpflichtbereich II (Kl. 09 und 10, bei G8: 08 und 09): 3. Fremdsprache */
	FS3,

	/** Kursart Förderung neu zugewanderter Schüler in Deutschförderklassen (vollständige äußere Differenzierung) */
	DFK,

	/** Kursart Förderunterricht */
	FU,

	/** Kursart Stützunterricht Wahlbereich - Förderunterricht für ausländische u. ausgesiedelte Schüler */
	FUAUS,

	/** Kursart Fachunabhängiger Förderunterricht */
	FUF,

	/** Kursart Förderunterricht im Klassenverband */
	FUK,

	/** Kursart Förderunterricht für Teile von Klassen */
	FUT,

	/** Kursart fächerübergreifender Kurs */
	FUEK,

	/** Kursart Grundebene/-kurs */
	G,

	/** Kursart Grundkurs – Bildungsgang Hauptschule */
	G_H,

	/** Kursart Grundkurs mündlich */
	GKM,

	/** Kursart Grundkurs schriftlich */
	GKS,

	/** Kursart Hausunterricht */
	HU,

	/** Kursart Zusätzlicher Förderunterricht im Rahmen der Initiative Komm mit */
	KMFOE,

	/** Kursart Leistungskurs I */
	LK1,

	/** Kursart Leistungskurs II */
	LK2,

	/** Kursart Schülerinnen und Schüler mit besonderen Schwierigkeiten im Erlernen des Lesens und Rechtschreibens (LRS) */
	LRS,

	/** Kursart Unterricht in der Herkunftssprache (Muttersprachlicher Unterricht) */
	MEU,

	/** Kursart Förderung in der deutschen Sprache außerhalb von Sprachfördermaßnahmen */
	FDS,

	/** Kursart Neigungs- und Projektgruppe */
	NPG,

	/** Kursart Projektkurs */
	PJK,

	/** Kursart Profilklasse */
	PROJ,

	/** Kursart Unterricht im Klassenverband */
	PUK,

	/** Kursart Pflichtunterricht für Teile von Klassen */
	PUT,

	/** Kursart Stütz- oder Förderkurs */
	SF,

	/** Kursart Selbstlernphase */
	SLP,

	/** Kursart Wahlpflichtunterricht: Schwerpunktübergreifende Angebote */
	SPA,

	/** Kursart Sportförderunterricht */
	SPFU,

	/** Kursart sonderpädagogische Förderung */
	SPF,

	/** Kursart Stützunterricht Wahlbereich - fachbezogen */
	SWFB,

	/** Kursart Stützunterricht Wahlbereich - fachübergreifend */
	SWFW,

	/** Kursart Unterricht im Rahmen des Schulversuchs Talentschule */
	TAL,

	/** Kursart Unterricht in der Herkunftssprache anstelle einer Pflichtfremdsprache oder eines Wahlpflichtfaches */
	UMPF,

	/** Kursart Förderunterricht */
	VSU,

	/** Kursart Vertiefungsfach */
	VTF,

	/** Kursart Vertiefungsunterricht Wahlbereich - berufsfeld- / berufsbezogener fachpraxisorientierter Kurs */
	VUW,

	/** Kursart Wahlpflichtbereich */
	WP,

	/** Kursart Wahlpflichtbereich: Fremdsprachlich */
	WP1FS,

	/** Kursart Wahlpflichtbereich: Musisch-künstlerisch */
	WP1MU,

	/** Kursart Wahlpflichtbereich: Naturwissenschaftlich - technisch */
	WP1NT,

	/** Kursart Wahlpflichtbereich: Sozialwissenschaftlich */
	WP1SW,

	/** Kursart Wahlpflichtbereich: Wirtschaftlich */
	WP1WW,

	/** Kursart Wahlpflichtfach */
	WPF,

	/** Kursart Wahlpflichtbereich I */
	WPI,

	/** Kursart Wahlpflichtbereich I: 2. Fremdsprache */
	WPIGY,

	/** Kursart Wahlpflichtbereich II - Fächerkombination im math.-naturwiss, gesellschaftswiss. oder künstlerischen Schwerpunkt */
	WPII,

	/** Kursart Wahlpflichtunterricht */
	WPU,

	/** Kursart Zusatzkurs */
	ZK,

	/** Kursart zusätzliche Unterrichtsveranstaltung */
	ZUV;


	/** Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();

	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	private static final @NotNull Map<Integer, Map<Schulform, List<ZulaessigeKursart>>> _mapBySchuljahrAndSchulform = new HashMap<>();

	private static final @NotNull Map<Integer, Map<String, List<ZulaessigeKursart>>> _mapBySchuljahrAndAllgemeinerKursart = new HashMap<>();




	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart> manager) {
		CoreTypeDataManager.putManager(ZulaessigeKursart.class, manager);
		for (final var ct : data().getWerte())
			for (final var e : ct.historie()) {
				final Set<Schulform> tmpSet = new HashSet<>();
				for (final var s : e.zulaessig)
					tmpSet.add(Schulform.data().getWertByBezeichner(s.schulform));
				_mapSchulformenByID.put(e.id, tmpSet);
			}
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart> data() {
		return CoreTypeDataManager.getManager(ZulaessigeKursart.class);
	}


	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public boolean hatSchulform(final int schuljahr, final @NotNull Schulform sf) {
		final ZulaessigeKursartKatalogEintrag ke = this.daten(schuljahr);
		if (ke != null) {
			final Set<Schulform> result = _mapSchulformenByID.get(ke.id);
			if (result == null)
				throw new CoreTypeException(
						"Fehler beim Prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.".formatted(this.getClass().getSimpleName()));
			return result.contains(sf);
		}
		return false;
	}


	/**
	 * Liefert alle zulässigen Kursarten für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Kursarten
	 */
	public static @NotNull List<ZulaessigeKursart> getListBySchuljahrAndSchulform(final int schuljahr, final @NotNull Schulform schulform) {
		final Map<Schulform, List<ZulaessigeKursart>> mapBySchulform =
				_mapBySchuljahrAndSchulform.computeIfAbsent(schuljahr, k -> new HashMap<Schulform, List<ZulaessigeKursart>>());
		if (mapBySchulform == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		List<ZulaessigeKursart> result = mapBySchulform.get(schulform);
		if (result == null) {
			result = new ArrayList<>();
			final List<ZulaessigeKursart> kursarten = ZulaessigeKursart.data().getWerteBySchuljahr(schuljahr);
			for (final @NotNull ZulaessigeKursart kursart : kursarten)
				if (kursart.hatSchulform(schuljahr, schulform))
					result.add(kursart);
			mapBySchulform.put(schulform, result);
		}
		return result;
	}


	/**
	 * Bestimmt die Liste der möglichen speziellen Kursarten für die angegebene allgemeine Kursart
	 *
	 * @param schuljahr     das Schuljahr, in dem die speziellen Kursarten gültig sind
	 * @param allgKursart   die allgemeine Kursart
	 *
	 * @return die Liste der möglichen speziellen Kursarten
	 */
	public static @NotNull List<ZulaessigeKursart> getByAllgemeinerKursart(final int schuljahr, final @NotNull String allgKursart) {
		// TODO Aktueller Fix für E- und G-Kurse an Gesamtschulen
		if ("E".equals(allgKursart) || "G".equals(allgKursart)) {
			final List<ZulaessigeKursart> result = new ArrayList<>();
			result.add("E".equals(allgKursart) ? ZulaessigeKursart.E : ZulaessigeKursart.G);
			return result;
		}
		// Normaler Aufruf
		final Map<String, List<ZulaessigeKursart>> mapByAllgemeinerKursart =
				_mapBySchuljahrAndAllgemeinerKursart.computeIfAbsent(schuljahr, k -> new HashMap<String, List<ZulaessigeKursart>>());
		if (mapByAllgemeinerKursart == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		List<ZulaessigeKursart> result = mapByAllgemeinerKursart.get(allgKursart);
		if (result == null) {
			result = new ArrayList<>();
			final List<ZulaessigeKursart> kursarten = ZulaessigeKursart.data().getWerteBySchuljahr(schuljahr);
			for (final @NotNull ZulaessigeKursart kursart : kursarten) {
				final ZulaessigeKursartKatalogEintrag zkke = kursart.daten(schuljahr);
				if (zkke == null)
					continue;
				if (("".equals(allgKursart) && (zkke.kuerzel == null)) || (allgKursart.equals(zkke.kuerzelAllg))
						|| ((zkke.kuerzelAllg == null) && (allgKursart.equals(zkke.kuerzel))))
					result.add(kursart);
			}
			mapByAllgemeinerKursart.put(allgKursart, result);
		}
		return result;
	}

}
