import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit';
import { BKGymAbiturdaten, cast_de_svws_nrw_core_abschluss_bk_d_BKGymAbiturdaten } from '../../../../core/abschluss/bk/d/BKGymAbiturdaten';
import { BKGymBelegpruefungErgebnis } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungErgebnis';
import { BKGymFaecherManager } from '../../../../core/utils/bk/BKGymFaecherManager';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { BeruflichesGymnasiumPruefungsordnungAnlage } from '../../../../asd/types/schule/BeruflichesGymnasiumPruefungsordnungAnlage';
import { Schulgliederung } from '../../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../../java/util/List';
import { BKGymBelegpruefungD3 } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungD3';
import { BeruflichesGymnasiumStundentafelFach } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';
import { BKGymBelegpruefungErgebnisFehler } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungErgebnisFehler';
import { BKGymFach } from '../../../../core/data/bk/abi/BKGymFach';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag } from '../../../../asd/data/schule/BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag';
import { BKGymAbiturFachbelegung } from '../../../../core/abschluss/bk/d/BKGymAbiturFachbelegung';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';

export class BKGymAbiturdatenManager extends JavaObject {

	/**
	 * Die Abiturdaten des Schülers
	 */
	private readonly abidaten : BKGymAbiturdaten;

	/**
	 * Die Schulgliederung des Bildungsgangs des Schülers
	 */
	private readonly gliederung : Schulgliederung;

	/**
	 * Der Fachklassen-Schlüssel des Bildungsgangs des Schülers
	 */
	private readonly fks : string;

	/**
	 * Der Manager für die Fächer des beruflichen Gymnasiums
	 */
	private readonly faecherManager : BKGymFaecherManager;

	/**
	 * Das Halbjahr, bis zu welchem die Belegprüfung durchgeführt werden soll
	 */
	private readonly bisHalbjahr : GostHalbjahr;

	/**
	 * Der Belegprüfungsalgorithmus
	 */
	private readonly belegpruefung : BKGymBelegpruefung;

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach anhand der Bezeichnung ermöglicht
	 */
	private readonly mapFachbelegungenByFachbezeichnung : JavaMap<string, BKGymAbiturFachbelegung> = new HashMap<string, BKGymAbiturFachbelegung>();

	/**
	 * Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind.
	 */
	private belegpruefungsfehler : List<BKGymBelegungsfehler> = new ArrayList<BKGymBelegungsfehler>();

	/**
	 * Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht.
	 */
	private belegpruefungErfolgreich : boolean = false;

	/**
	 * Eine Map, welche von der Nummer des Abiturfaches auf die Fachbelegung der Abiturdaten verweist.
	 */
	private readonly mapAbiturfachbelegungen : HashMap<number, BKGymAbiturFachbelegung> = new HashMap<number, BKGymAbiturFachbelegung>();


	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param abidaten         die Abiturdaten des Schülers
	 * @param gliederung       die Schulgliederung des Bildungsgangs des Schülers
	 * @param fks              der fünfstellige Fachklassenschlüssel des Bildungsgangs des Schülers
	 * @param faecherManager   der Manager für die Fächer
	 * @param bisHalbjahr      die Art der Belegpruefung - bis zu welchem Halbjahr geprüft werden soll
	 */
	public constructor(abidaten : BKGymAbiturdaten, gliederung : Schulgliederung, fks : string, faecherManager : BKGymFaecherManager, bisHalbjahr : GostHalbjahr) {
		super();
		this.abidaten = abidaten;
		this.gliederung = gliederung;
		this.fks = fks;
		this.faecherManager = faecherManager;
		this.bisHalbjahr = bisHalbjahr;
		this.belegpruefung = this.getBelegpruefung();
		this.init();
		this.belegpruefung.pruefe();
		this.belegpruefungsfehler = this.belegpruefung.getBelegungsfehler();
		this.belegpruefungErfolgreich = this.belegpruefung.istErfolgreich();
	}

	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public init() : void {
		this.mapFachbelegungenByFachbezeichnung.clear();
		this.mapAbiturfachbelegungen.clear();
		const fachbelegungen : List<BKGymAbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of fachbelegungen) {
			if (fachbelegung.abiturFach !== null)
				this.mapAbiturfachbelegungen.put(fachbelegung.abiturFach, fachbelegung);
			const fach : BKGymFach | null = this.faecherManager.get(fachbelegung.fachID);
			if ((fach === null) || (fach.bezeichnung === null))
				continue;
			this.mapFachbelegungenByFachbezeichnung.put(fach.bezeichnung, fachbelegung);
		}
	}

	/**
	 * Erstellt eine Belegprüfung zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private createBelegpruefungD01() : BKGymBelegpruefung {
		let _sevar_1941673097 : any;
		const _seexpr_1941673097 = (this.fks);
		if (_seexpr_1941673097 === "10600") {
			_sevar_1941673097 = new BKGymBelegpruefungD3(this);
		} else {
			throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + this.gliederung.name() + " und den Fachklassenschlüssel " + this.fks + " wird noch nicht unterstützt.");
		}
		return _sevar_1941673097;
	}

	/**
	 * Erstellt die zugehörige Belegprüfung mit den Abiturdaten anhand des übergebenen Bildungsganges.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private getBelegpruefung() : BKGymBelegpruefung {
		let pruefung : BKGymBelegpruefung;
		const _seexpr_7773353 = (this.gliederung);
		if (_seexpr_7773353 === Schulgliederung.D01) {
			pruefung = this.createBelegpruefungD01();
		} else {
			throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + this.gliederung.name() + " wird noch nicht unterstützt.");
		}
		;
		return pruefung;
	}

	/**
	 * Getter für den Zugriff auf die Abiturdaten
	 *
	 * @return die Abiturdaten
	 */
	public getAbidaten() : BKGymAbiturdaten {
		return this.abidaten;
	}

	/**
	 * Getter für den Zugriff auf das Halbjahr, bis zu welchem geprüft werde soll
	 *
	 * @return das Halbjahr
	 */
	public getBisHalbjahr() : GostHalbjahr {
		return this.bisHalbjahr;
	}

	/**
	 * Getter für den Zugriff auf die Schulgliederung des Bildungsganges
	 *
	 * @return die Schulgliederung des Bildungsganges
	 */
	public getGliederung() : Schulgliederung {
		return this.gliederung;
	}

	/**
	 * Getter für den Zugriff auf den Fachklassenschlüssel des Bildungsganges
	 *
	 * @return der Fachklassenschlüssel des Bildungsganges
	 */
	public getFachklassenschluessel() : string {
		return this.fks;
	}

	/**
	 * Getter für den Zugriff auf das Schuljahr in dem das Abitur stattfindet
	 *
	 * @return das Schuljahr des Abiturs
	 */
	public getSchuljahrAbitur() : number {
		return this.abidaten.schuljahrAbitur;
	}

	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public getBelegpruefungErgebnis() : BKGymBelegpruefungErgebnis {
		const ergebnis : BKGymBelegpruefungErgebnis = new BKGymBelegpruefungErgebnis();
		ergebnis.erfolgreich = this.belegpruefungErfolgreich;
		for (let i : number = 0; i < this.belegpruefungsfehler.size(); i++) {
			const fehler : BKGymBelegungsfehler = this.belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new BKGymBelegpruefungErgebnisFehler(fehler));
		}
		return ergebnis;
	}

	/**
	 * Gibt das Abiturfachdaten für das geforderte Abiturfach zurück.
	 *
	 * @param abiFach Das n. Abiturfach, das gewünscht ist
	 *
	 * @return die entsprechende Fachbelegung des Abiturfachs
	 */
	public getAbiFachbelegung(abiFach : GostAbiturFach) : BKGymAbiturFachbelegung | null {
		return this.mapAbiturfachbelegungen.get(abiFach.id);
	}

	/**
	 * Prüft, ob die übergebene Fachbelgung als Fach in der Stundentafel vorkommt bzw. vorkommen kann.
	 *
	 * @param tafel   die Stundentafel
	 * @param fb      die Fachbelegung
	 *
	 * @return der Eintrag der Stundentafel, bei welchem die Fachbelegung vorkommt, oder null, wenn keine Zuordnung zur Stundentafel möglich ist
	 */
	private getFachByBelegung(tafel : BeruflichesGymnasiumStundentafel, fb : BKGymAbiturFachbelegung) : BeruflichesGymnasiumStundentafelFach | null {
		const fbFach : BKGymFach | null = this.faecherManager.get(fb.fachID);
		if ((fbFach === null) || (fbFach.bezeichnung === null))
			return null;
		for (const tafelFach of tafel.faecher)
			if (JavaObject.equalsTranspiler(tafelFach.fachbezeichnung, (fbFach.bezeichnung)))
				return tafelFach;
		if (fbFach.istFremdsprache)
			for (const tafelFach of tafel.faecher)
				if (JavaObject.equalsTranspiler(tafelFach.fachbezeichnung, ("Zweite Fremdsprache")))
					return tafelFach;
		for (const tafelFach of tafel.faecher)
			if (JavaObject.equalsTranspiler(tafelFach.fachbezeichnung, ("Wahlfach")))
				return tafelFach;
		return null;
	}

	/**
	 * Gibt TRUE zurück falls die übergebene Fachbelegung in der Stundentafel in der durch Abifach gegebenen Rolle gültig ist.
	 *
	 * @param tafel     die Stundentafel
	 * @param fb        die zu prüfende Fachbelegung
	 * @param abifach   die Rolle als Abiturfach oder null als Nicht-Abiturfach
	 *
	 * @return true, wenn die Belegung mit der angegebenen Kursart gültig ist und ansonsten FALSE
	 */
	private isValidKursartFachbelegung(tafel : BeruflichesGymnasiumStundentafel, fb : BKGymAbiturFachbelegung, abifach : GostAbiturFach) : boolean {
		const tafelFach : BeruflichesGymnasiumStundentafelFach | null = this.getFachByBelegung(tafel, fb);
		if (tafelFach === null)
			return false;
		if ((abifach as unknown === GostAbiturFach.LK1 as unknown) || (abifach as unknown === GostAbiturFach.LK2 as unknown))
			return JavaObject.equalsTranspiler(tafelFach.kursart, (abifach.kuerzel));
		if ((abifach as unknown === GostAbiturFach.AB3 as unknown) || (abifach as unknown === GostAbiturFach.AB4 as unknown) || (abifach as unknown === GostAbiturFach.AB5 as unknown)) {
			for (const wm of tafel.wahlmoeglichkeiten) {
				const abifaecher : List<string> = (abifach as unknown === GostAbiturFach.AB3 as unknown) ? wm.abifach3 : ((abifach as unknown === GostAbiturFach.AB4 as unknown) ? wm.abifach4 : wm.abifach5);
				for (const bezeichnung of abifaecher)
					if (JavaObject.equalsTranspiler(bezeichnung, (tafelFach.fachbezeichnung)))
						return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Bestimmt zu der übergebenen Anlage der Prüfungsordnung die möglichen Stundentafel-Variante anhand der Leistungskurs-Kombinationen.
	 *
	 * @param anlage   die Anlage der Prüfungsordnung, deren Stundentafel bestimmt werden soll
	 *
	 * @return die Liste der möglichen Variante anhand der belegten Leistungskurse
	 */
	public getStundentafelByLeistungskurse(anlage : BeruflichesGymnasiumPruefungsordnungAnlage) : List<BeruflichesGymnasiumStundentafel> {
		const result : List<BeruflichesGymnasiumStundentafel> = new ArrayList<BeruflichesGymnasiumStundentafel>();
		const schuljahr : number = this.getSchuljahrAbitur();
		const poke : BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag | null = anlage.daten(schuljahr);
		if (poke === null)
			return result;
		const lk1 : BKGymAbiturFachbelegung | null = this.getAbiFachbelegung(GostAbiturFach.LK1);
		const lk2 : BKGymAbiturFachbelegung | null = this.getAbiFachbelegung(GostAbiturFach.LK2);
		if ((lk1 === null) || (lk2 === null))
			return result;
		for (const tafel of poke.stundentafeln)
			if (this.isValidKursartFachbelegung(tafel, lk1, GostAbiturFach.LK1) && this.isValidKursartFachbelegung(tafel, lk2, GostAbiturFach.LK2))
				result.add(tafel);
		return result;
	}

	/**
	 * Prüft, ob die übergebene Kombination aus drittem und viertem Abiturfach gültig ist.
	 * Dabei werden die Spezialfälle für eine zweite Fremdsprache und ein mögliches Wahlfach (Zukunftstauglichkeit)
	 * berücksichtigt
	 *
	 * @param wm    die Wahlmöglichkeit
	 * @param ab3   die Belegung des dritten Abiturfaches
	 * @param ab4   die Belegung des vierten Abiturfaches
	 *
	 * @return true, wenn sie gültig ist, und ansonsten false
	 */
	private isValidWahlmoeglichkeit(wm : BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit, ab3 : BKGymAbiturFachbelegung, ab4 : BKGymAbiturFachbelegung) : boolean {
		const ab3Fach : BKGymFach | null = this.faecherManager.get(ab3.fachID);
		const ab4Fach : BKGymFach | null = this.faecherManager.get(ab4.fachID);
		if ((ab3Fach === null) || (ab4Fach === null))
			return false;
		let wm3 : string | null = null;
		for (const fachBez3 of wm.abifach3)
			if (JavaObject.equalsTranspiler(fachBez3, (ab3Fach.bezeichnung)) || (JavaObject.equalsTranspiler("Zweite Fremdsprache", (fachBez3)) && ab3Fach.istFremdsprache) || JavaObject.equalsTranspiler("Wahlfach", (fachBez3)))
				wm3 = fachBez3;
		if (wm3 === null)
			return false;
		for (const fachBez4 of wm.abifach4)
			if (JavaObject.equalsTranspiler(fachBez4, (ab4Fach.bezeichnung)) || (JavaObject.equalsTranspiler("Zweite Fremdsprache", (fachBez4)) && ab4Fach.istFremdsprache) || JavaObject.equalsTranspiler("Wahlfach", (fachBez4)))
				return true;
		return false;
	}

	/**
	 * Bestimmt die Wahlmöglichkeit, welche zu der Belelgung des dritten und vierten Abiturfaches passt.
	 *
	 * @param tafel   die Stundentafel
	 *
	 * @return die Wahlmöglichkeit oder null, wenn keine passt
	 */
	public getWahlmoeglichkeitAusStundentafel(tafel : BeruflichesGymnasiumStundentafel) : BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit | null {
		const ab3 : BKGymAbiturFachbelegung | null = this.getAbiFachbelegung(GostAbiturFach.AB3);
		const ab4 : BKGymAbiturFachbelegung | null = this.getAbiFachbelegung(GostAbiturFach.AB4);
		if ((ab3 === null) || (ab4 === null))
			return null;
		let found : BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit | null = null;
		for (const wm of tafel.wahlmoeglichkeiten) {
			if (!this.isValidWahlmoeglichkeit(wm, ab3, ab4))
				continue;
			if (found !== null)
				throw new DeveloperNotificationException("In der Definition der Prüfungsordnung ist ein Fehler aufgetreten: Eine Abiturfachkombination darf in der Definition zur Prüfungsordnung der Anlage bei einer Variante nicht mehrfach auftreten.")
			found = wm;
		}
		return found;
	}

	/**
	 * Liefert eine Zuordnung der gefundenen Wahlmöglichkeiten zu deren Stundentafeln als Map.
	 *
	 * @param tafeln   die zu prüfenden Stundetafeln mit ihren Wahlmöglichkeiten
	 *
	 * @return die Map
	 */
	public getWahlmoeglichekiten(tafeln : List<BeruflichesGymnasiumStundentafel>) : JavaMap<BeruflichesGymnasiumStundentafel, BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> {
		const result : JavaMap<BeruflichesGymnasiumStundentafel, BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> = new HashMap<BeruflichesGymnasiumStundentafel, BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit>();
		for (const tafel of tafeln) {
			const wm : BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit | null = this.getWahlmoeglichkeitAusStundentafel(tafel);
			if (wm !== null)
				result.put(tafel, wm);
		}
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymAbiturdatenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymAbiturdatenManager'].includes(name);
	}

	public static class = new Class<BKGymAbiturdatenManager>('de.svws_nrw.core.abschluss.bk.d.BKGymAbiturdatenManager');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymAbiturdatenManager(obj : unknown) : BKGymAbiturdatenManager {
	return obj as BKGymAbiturdatenManager;
}
