import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { ZulaessigeKursartKatalogEintrag } from '../../../asd/data/kurse/ZulaessigeKursartKatalogEintrag';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashSet } from '../../../java/util/HashSet';
import { CoreTypeException } from '../../../asd/data/CoreTypeException';

export class ZulaessigeKursart extends JavaEnum<ZulaessigeKursart> implements CoreType<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<ZulaessigeKursart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, ZulaessigeKursart> = new Map<string, ZulaessigeKursart>();

	/**
	 * Kursart 3. Abiturfach
	 */
	public static readonly AB3 : ZulaessigeKursart = new ZulaessigeKursart("AB3", 0, );

	/**
	 * Kursart 4. Abiturfach
	 */
	public static readonly AB4 : ZulaessigeKursart = new ZulaessigeKursart("AB4", 1, );

	/**
	 * Kursart Arbeitsgemeinschaft gemäß APO SI
	 */
	public static readonly AG : ZulaessigeKursart = new ZulaessigeKursart("AG", 2, );

	/**
	 * Kursart Arbeitsgemeinschaft im Ganztagsbereich
	 */
	public static readonly AGGT : ZulaessigeKursart = new ZulaessigeKursart("AGGT", 3, );

	/**
	 * Kursart Stütz- und Angleichungskurs / Förderunterricht
	 */
	public static readonly AGKWB : ZulaessigeKursart = new ZulaessigeKursart("AGKWB", 4, );

	/**
	 * Kursart Angleichungskurs
	 */
	public static readonly AGK : ZulaessigeKursart = new ZulaessigeKursart("AGK", 5, );

	/**
	 * Kursart Arbeits- bzw. Übungsstunde
	 */
	public static readonly AST : ZulaessigeKursart = new ZulaessigeKursart("AST", 6, );

	/**
	 * Kursart Begegnung mit Sprachen in der Primarstufe
	 */
	public static readonly BSP : ZulaessigeKursart = new ZulaessigeKursart("BSP", 7, );

	/**
	 * Kursart Unterricht im Rahmen von KAOA einschl. Schule trifft Arbeitswelt
	 */
	public static readonly BUS : ZulaessigeKursart = new ZulaessigeKursart("BUS", 8, );

	/**
	 * Kursart Erweiterungsebene/-kurs
	 */
	public static readonly E : ZulaessigeKursart = new ZulaessigeKursart("E", 9, );

	/**
	 * Kursart Erweiterungskurs – Bildungsgang Hauptschule
	 */
	public static readonly E_H : ZulaessigeKursart = new ZulaessigeKursart("E_H", 10, );

	/**
	 * Kursart Erweitertes Bildungsangebot
	 */
	public static readonly EBA : ZulaessigeKursart = new ZulaessigeKursart("EBA", 11, );

	/**
	 * Kursart Einführung in die 2. Fremdsprache oder Ersatzfach
	 */
	public static readonly EF2 : ZulaessigeKursart = new ZulaessigeKursart("EF2", 12, );

	/**
	 * Kursart Ersatzfach für Sport als 4. Abiturfach
	 */
	public static readonly EFSP : ZulaessigeKursart = new ZulaessigeKursart("EFSP", 13, );

	/**
	 * Kursart Ergänzungsstunden
	 */
	public static readonly EGS1 : ZulaessigeKursart = new ZulaessigeKursart("EGS1", 14, );

	/**
	 * Kursart Ergänzungsstunden mit Benotung
	 */
	public static readonly EGSN : ZulaessigeKursart = new ZulaessigeKursart("EGSN", 15, );

	/**
	 * Kursart Ergänzungs- oder Vertiefungskurs
	 */
	public static readonly EV : ZulaessigeKursart = new ZulaessigeKursart("EV", 16, );

	/**
	 * Kursart Erweiterungsunterricht Wahlbereich - berufsbezogen, fachübergreifend
	 */
	public static readonly EWBF : ZulaessigeKursart = new ZulaessigeKursart("EWBF", 17, );

	/**
	 * Kursart Erweiterungsunterricht Wahlbereich - fach-, jedoch nicht abschlussbezogen
	 */
	public static readonly EWF : ZulaessigeKursart = new ZulaessigeKursart("EWF", 18, );

	/**
	 * Kursart Erweiterungsunterricht Wahlbereich - fach- und abschlussbezogen
	 */
	public static readonly EWFA : ZulaessigeKursart = new ZulaessigeKursart("EWFA", 19, );

	/**
	 * Kursart Wahlplflichtunterricht: Einzelfach im math.-naturw., techn., gesellschaftsw., künstlerischen Schwerpunkt
	 */
	public static readonly F3 : ZulaessigeKursart = new ZulaessigeKursart("F3", 20, );

	/**
	 * Kursart freiwillige Arbeitsgemeinschaft
	 */
	public static readonly FAG : ZulaessigeKursart = new ZulaessigeKursart("FAG", 21, );

	/**
	 * Kursart Fachbezogener Förderunterricht
	 */
	public static readonly FFU : ZulaessigeKursart = new ZulaessigeKursart("FFU", 22, );

	/**
	 * Kursart Förderangebot Ganztagsschule
	 */
	public static readonly FOGT : ZulaessigeKursart = new ZulaessigeKursart("FOGT", 23, );

	/**
	 * Kursart Förderung neu zugewanderter Schüler in Deutschfördergruppen (teilweise äußere und innere Differenzierung)
	 */
	public static readonly DFG : ZulaessigeKursart = new ZulaessigeKursart("DFG", 24, );

	/**
	 * Kursart Wahlpflichtbereich II (Kl. 09 und 10, bei G8: 08 und 09): 3. Fremdsprache
	 */
	public static readonly FS3 : ZulaessigeKursart = new ZulaessigeKursart("FS3", 25, );

	/**
	 * Kursart Förderung neu zugewanderter Schüler in Deutschförderklassen (vollständige äußere Differenzierung)
	 */
	public static readonly DFK : ZulaessigeKursart = new ZulaessigeKursart("DFK", 26, );

	/**
	 * Kursart Förderunterricht
	 */
	public static readonly FU : ZulaessigeKursart = new ZulaessigeKursart("FU", 27, );

	/**
	 * Kursart Stützunterricht Wahlbereich - Förderunterricht für ausländische u. ausgesiedelte Schüler
	 */
	public static readonly FUAUS : ZulaessigeKursart = new ZulaessigeKursart("FUAUS", 28, );

	/**
	 * Kursart Fachunabhängiger Förderunterricht
	 */
	public static readonly FUF : ZulaessigeKursart = new ZulaessigeKursart("FUF", 29, );

	/**
	 * Kursart Förderunterricht im Klassenverband
	 */
	public static readonly FUK : ZulaessigeKursart = new ZulaessigeKursart("FUK", 30, );

	/**
	 * Kursart Förderunterricht für Teile von Klassen
	 */
	public static readonly FUT : ZulaessigeKursart = new ZulaessigeKursart("FUT", 31, );

	/**
	 * Kursart fächerübergreifender Kurs
	 */
	public static readonly FUEK : ZulaessigeKursart = new ZulaessigeKursart("FUEK", 32, );

	/**
	 * Kursart Grundebene/-kurs
	 */
	public static readonly G : ZulaessigeKursart = new ZulaessigeKursart("G", 33, );

	/**
	 * Kursart Grundkurs – Bildungsgang Hauptschule
	 */
	public static readonly G_H : ZulaessigeKursart = new ZulaessigeKursart("G_H", 34, );

	/**
	 * Kursart Grundkurs mündlich
	 */
	public static readonly GKM : ZulaessigeKursart = new ZulaessigeKursart("GKM", 35, );

	/**
	 * Kursart Grundkurs schriftlich
	 */
	public static readonly GKS : ZulaessigeKursart = new ZulaessigeKursart("GKS", 36, );

	/**
	 * Kursart Hausunterricht
	 */
	public static readonly HU : ZulaessigeKursart = new ZulaessigeKursart("HU", 37, );

	/**
	 * Kursart Zusätzlicher Förderunterricht im Rahmen der Initiative Komm mit
	 */
	public static readonly KMFOE : ZulaessigeKursart = new ZulaessigeKursart("KMFOE", 38, );

	/**
	 * Kursart Leistungskurs I
	 */
	public static readonly LK1 : ZulaessigeKursart = new ZulaessigeKursart("LK1", 39, );

	/**
	 * Kursart Leistungskurs II
	 */
	public static readonly LK2 : ZulaessigeKursart = new ZulaessigeKursart("LK2", 40, );

	/**
	 * Kursart Schülerinnen und Schüler mit besonderen Schwierigkeiten im Erlernen des Lesens und Rechtschreibens (LRS)
	 */
	public static readonly LRS : ZulaessigeKursart = new ZulaessigeKursart("LRS", 41, );

	/**
	 * Kursart Unterricht in der Herkunftssprache (Muttersprachlicher Unterricht)
	 */
	public static readonly MEU : ZulaessigeKursart = new ZulaessigeKursart("MEU", 42, );

	/**
	 * Kursart Förderung in der deutschen Sprache außerhalb von Sprachfördermaßnahmen
	 */
	public static readonly FDS : ZulaessigeKursart = new ZulaessigeKursart("FDS", 43, );

	/**
	 * Kursart Neigungs- und Projektgruppe
	 */
	public static readonly NPG : ZulaessigeKursart = new ZulaessigeKursart("NPG", 44, );

	/**
	 * Kursart Projektkurs
	 */
	public static readonly PJK : ZulaessigeKursart = new ZulaessigeKursart("PJK", 45, );

	/**
	 * Kursart Profilklasse
	 */
	public static readonly PROJ : ZulaessigeKursart = new ZulaessigeKursart("PROJ", 46, );

	/**
	 * Kursart Unterricht im Klassenverband
	 */
	public static readonly PUK : ZulaessigeKursart = new ZulaessigeKursart("PUK", 47, );

	/**
	 * Kursart Pflichtunterricht für Teile von Klassen
	 */
	public static readonly PUT : ZulaessigeKursart = new ZulaessigeKursart("PUT", 48, );

	/**
	 * Kursart Stütz- oder Förderkurs
	 */
	public static readonly SF : ZulaessigeKursart = new ZulaessigeKursart("SF", 49, );

	/**
	 * Kursart Selbstlernphase
	 */
	public static readonly SLP : ZulaessigeKursart = new ZulaessigeKursart("SLP", 50, );

	/**
	 * Kursart Wahlpflichtunterricht: Schwerpunktübergreifende Angebote
	 */
	public static readonly SPA : ZulaessigeKursart = new ZulaessigeKursart("SPA", 51, );

	/**
	 * Kursart Sportförderunterricht
	 */
	public static readonly SPFU : ZulaessigeKursart = new ZulaessigeKursart("SPFU", 52, );

	/**
	 * Kursart sonderpädagogische Förderung
	 */
	public static readonly SPF : ZulaessigeKursart = new ZulaessigeKursart("SPF", 53, );

	/**
	 * Kursart Stützunterricht Wahlbereich - fachbezogen
	 */
	public static readonly SWFB : ZulaessigeKursart = new ZulaessigeKursart("SWFB", 54, );

	/**
	 * Kursart Stützunterricht Wahlbereich - fachübergreifend
	 */
	public static readonly SWFW : ZulaessigeKursart = new ZulaessigeKursart("SWFW", 55, );

	/**
	 * Kursart Unterricht im Rahmen des Schulversuchs Talentschule
	 */
	public static readonly TAL : ZulaessigeKursart = new ZulaessigeKursart("TAL", 56, );

	/**
	 * Kursart Unterricht in der Herkunftssprache anstelle einer Pflichtfremdsprache oder eines Wahlpflichtfaches
	 */
	public static readonly UMPF : ZulaessigeKursart = new ZulaessigeKursart("UMPF", 57, );

	/**
	 * Kursart Förderunterricht
	 */
	public static readonly VSU : ZulaessigeKursart = new ZulaessigeKursart("VSU", 58, );

	/**
	 * Kursart Vertiefungsfach
	 */
	public static readonly VTF : ZulaessigeKursart = new ZulaessigeKursart("VTF", 59, );

	/**
	 * Kursart Vertiefungsunterricht Wahlbereich - berufsfeld- / berufsbezogener fachpraxisorientierter Kurs
	 */
	public static readonly VUW : ZulaessigeKursart = new ZulaessigeKursart("VUW", 60, );

	/**
	 * Kursart Wahlpflichtbereich
	 */
	public static readonly WP : ZulaessigeKursart = new ZulaessigeKursart("WP", 61, );

	/**
	 * Kursart Wahlpflichtbereich: Fremdsprachlich
	 */
	public static readonly WP1FS : ZulaessigeKursart = new ZulaessigeKursart("WP1FS", 62, );

	/**
	 * Kursart Wahlpflichtbereich: Musisch-künstlerisch
	 */
	public static readonly WP1MU : ZulaessigeKursart = new ZulaessigeKursart("WP1MU", 63, );

	/**
	 * Kursart Wahlpflichtbereich: Naturwissenschaftlich - technisch
	 */
	public static readonly WP1NT : ZulaessigeKursart = new ZulaessigeKursart("WP1NT", 64, );

	/**
	 * Kursart Wahlpflichtbereich: Sozialwissenschaftlich
	 */
	public static readonly WP1SW : ZulaessigeKursart = new ZulaessigeKursart("WP1SW", 65, );

	/**
	 * Kursart Wahlpflichtbereich: Wirtschaftlich
	 */
	public static readonly WP1WW : ZulaessigeKursart = new ZulaessigeKursart("WP1WW", 66, );

	/**
	 * Kursart Wahlpflichtfach
	 */
	public static readonly WPF : ZulaessigeKursart = new ZulaessigeKursart("WPF", 67, );

	/**
	 * Kursart Wahlpflichtbereich I
	 */
	public static readonly WPI : ZulaessigeKursart = new ZulaessigeKursart("WPI", 68, );

	/**
	 * Kursart Wahlpflichtbereich I: 2. Fremdsprache
	 */
	public static readonly WPIGY : ZulaessigeKursart = new ZulaessigeKursart("WPIGY", 69, );

	/**
	 * Kursart Wahlpflichtbereich II - Fächerkombination im math.-naturwiss, gesellschaftswiss. oder künstlerischen Schwerpunkt
	 */
	public static readonly WPII : ZulaessigeKursart = new ZulaessigeKursart("WPII", 70, );

	/**
	 * Kursart Wahlpflichtunterricht
	 */
	public static readonly WPU : ZulaessigeKursart = new ZulaessigeKursart("WPU", 71, );

	/**
	 * Kursart Zusatzkurs
	 */
	public static readonly ZK : ZulaessigeKursart = new ZulaessigeKursart("ZK", 72, );

	/**
	 * Kursart zusätzliche Unterrichtsveranstaltung
	 */
	public static readonly ZUV : ZulaessigeKursart = new ZulaessigeKursart("ZUV", 73, );

	/**
	 * Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer.
	 */
	private static readonly _mapSchulformenByID : HashMap<number, JavaSet<Schulform>> = new HashMap<number, JavaSet<Schulform>>();

	private static readonly _mapBySchuljahrAndSchulform : JavaMap<number, JavaMap<Schulform, List<ZulaessigeKursart>>> = new HashMap<number, JavaMap<Schulform, List<ZulaessigeKursart>>>();

	private static readonly _mapBySchuljahrAndAllgemeinerKursart : JavaMap<number, JavaMap<string, List<ZulaessigeKursart>>> = new HashMap<number, JavaMap<string, List<ZulaessigeKursart>>>();

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		ZulaessigeKursart.all_values_by_ordinal.push(this);
		ZulaessigeKursart.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart>) : void {
		CoreTypeDataManager.putManager(ZulaessigeKursart.class, manager);
		ZulaessigeKursart._mapSchulformenByID.clear();
		ZulaessigeKursart._mapBySchuljahrAndSchulform.clear();
		ZulaessigeKursart._mapBySchuljahrAndAllgemeinerKursart.clear();
		for (const ct of ZulaessigeKursart.data().getWerte())
			for (const e of ct.historie()) {
				const tmpSet : JavaSet<Schulform> | null = new HashSet<Schulform>();
				for (const s of e.zulaessig)
					tmpSet.add(Schulform.data().getWertByBezeichner(s.schulform));
				ZulaessigeKursart._mapSchulformenByID.put(e.id, tmpSet);
			}
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart> {
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
	public hatSchulform(schuljahr : number, sf : Schulform) : boolean {
		const ke : ZulaessigeKursartKatalogEintrag | null = this.daten(schuljahr);
		if (ke !== null) {
			const result : JavaSet<Schulform> | null = ZulaessigeKursart._mapSchulformenByID.get(ke.id);
			if (result === null)
				throw new CoreTypeException(JavaString.format("Fehler beim Prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.", this.getClass().getSimpleName()))
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
	public static getListBySchuljahrAndSchulform(schuljahr : number, schulform : Schulform) : List<ZulaessigeKursart> {
		const mapBySchulform : JavaMap<Schulform, List<ZulaessigeKursart>> | null = ZulaessigeKursart._mapBySchuljahrAndSchulform.computeIfAbsent(schuljahr, { apply : (k: number | null) => new HashMap<Schulform, List<ZulaessigeKursart>>() });
		if (mapBySchulform === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let result : List<ZulaessigeKursart> | null = mapBySchulform.get(schulform);
		if (result === null) {
			result = new ArrayList();
			const kursarten : List<ZulaessigeKursart> | null = ZulaessigeKursart.data().getWerteBySchuljahr(schuljahr);
			for (const kursart of kursarten)
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
	public static getByAllgemeinerKursart(schuljahr : number, allgKursart : string) : List<ZulaessigeKursart> {
		if (JavaObject.equalsTranspiler("E", (allgKursart)) || JavaObject.equalsTranspiler("G", (allgKursart))) {
			const result : List<ZulaessigeKursart> | null = new ArrayList<ZulaessigeKursart>();
			result.add(JavaObject.equalsTranspiler("E", (allgKursart)) ? ZulaessigeKursart.E : ZulaessigeKursart.G);
			return result;
		}
		const mapByAllgemeinerKursart : JavaMap<string, List<ZulaessigeKursart>> | null = ZulaessigeKursart._mapBySchuljahrAndAllgemeinerKursart.computeIfAbsent(schuljahr, { apply : (k: number | null) => new HashMap<string, List<ZulaessigeKursart>>() });
		if (mapByAllgemeinerKursart === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let result : List<ZulaessigeKursart> | null = mapByAllgemeinerKursart.get(allgKursart);
		if (result === null) {
			result = new ArrayList();
			const kursarten : List<ZulaessigeKursart> | null = ZulaessigeKursart.data().getWerteBySchuljahr(schuljahr);
			for (const kursart of kursarten) {
				const zkke : ZulaessigeKursartKatalogEintrag | null = kursart.daten(schuljahr);
				if (zkke === null)
					continue;
				if ((JavaObject.equalsTranspiler("", (allgKursart)) && (zkke.kuerzel === null)) || (JavaObject.equalsTranspiler(allgKursart, (zkke.kuerzelAllg))) || ((zkke.kuerzelAllg === null) && (JavaObject.equalsTranspiler(allgKursart, (zkke.kuerzel)))))
					result.add(kursart);
			}
			mapByAllgemeinerKursart.put(allgKursart, result);
		}
		return result;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ZulaessigeKursart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ZulaessigeKursart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : ZulaessigeKursartKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<ZulaessigeKursartKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kurse.ZulaessigeKursart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.kurse.ZulaessigeKursart', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<ZulaessigeKursart>('de.svws_nrw.asd.types.kurse.ZulaessigeKursart');

}

export function cast_de_svws_nrw_asd_types_kurse_ZulaessigeKursart(obj : unknown) : ZulaessigeKursart {
	return obj as ZulaessigeKursart;
}
