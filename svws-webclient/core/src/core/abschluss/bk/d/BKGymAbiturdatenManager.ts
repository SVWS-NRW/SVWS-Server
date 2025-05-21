import { JavaObject } from '../../../../java/lang/JavaObject';
import { BKGymFach } from '../../../../core/data/bk/abi/BKGymFach';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { BKGymAbiturFachbelegung } from '../../../../core/abschluss/bk/d/BKGymAbiturFachbelegung';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { BKGymAbiturdaten, cast_de_svws_nrw_core_abschluss_bk_d_BKGymAbiturdaten } from '../../../../core/abschluss/bk/d/BKGymAbiturdaten';
import { BKGymBelegpruefungErgebnis } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungErgebnis';
import { BKGymFaecherManager } from '../../../../core/utils/bk/BKGymFaecherManager';
import { BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { Schulgliederung } from '../../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { BKGymBelegpruefungErgebnisFehler } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungErgebnisFehler';

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
		this.belegpruefung = BKGymBelegpruefung.getPruefung(this);
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
		const fachbelegungen : List<BKGymAbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of fachbelegungen) {
			const fach : BKGymFach | null = this.faecherManager.get(fachbelegung.fachID);
			if ((fach === null) || (fach.bezeichnung === null))
				continue;
			this.mapFachbelegungenByFachbezeichnung.put(fach.bezeichnung, fachbelegung);
		}
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
