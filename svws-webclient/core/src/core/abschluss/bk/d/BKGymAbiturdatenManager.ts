import { JavaObject } from '../../../../java/lang/JavaObject';
import { BKGymStundentafelManager } from '../../../../core/abschluss/bk/d/BKGymStundentafelManager';
import { BKGymAbiturMarkierungsalgorithmusErgebnis } from '../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusErgebnis';
import { ArrayList } from '../../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { BKGymAbiturdaten } from '../../../../core/data/bk/abi/BKGymAbiturdaten';
import { BKGymBelegpruefungErgebnis } from '../../../../core/data/bk/abi/BKGymBelegpruefungErgebnis';
import { BKGymFaecherManager } from '../../../../core/utils/bk/BKGymFaecherManager';
import { BeruflichesGymnasiumPruefungsordnungAnlage } from '../../../../asd/types/schule/BeruflichesGymnasiumPruefungsordnungAnlage';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { Schulgliederung } from '../../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../../java/util/List';
import { BKGymBelegpruefungErgebnisFehler } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungErgebnisFehler';
import { BKGymFach } from '../../../../core/data/bk/abi/BKGymFach';
import { BKGymFachbelegungManager } from '../../../../core/abschluss/bk/d/BKGymFachbelegungManager';
import { BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag } from '../../../../asd/data/schule/BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag';
import { BKGymAbiturFachbelegung } from '../../../../core/data/bk/abi/BKGymAbiturFachbelegung';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { BKGymAbiturMarkierungsalgorithmus } from '../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsalgorithmus';
import { SprachendatenUtils } from '../../../../core/utils/schueler/SprachendatenUtils';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { Class } from '../../../../java/lang/Class';

export class BKGymAbiturdatenManager extends JavaObject {

	/**
	 * Der Stundentafel-Manager
	 */
	private readonly stundentafelManager: BKGymStundentafelManager;

	/**
	 * Der Fachbelegung-Manager
	 */
	private readonly fachbelegungManager: BKGymFachbelegungManager;

	/**
	 * Die Abiturdaten des Schülers
	 */
	private readonly abidaten: BKGymAbiturdaten;

	/**
	 * Die Schulgliederung des Bildungsgangs des Schülers
	 */
	private readonly gliederung: Schulgliederung;

	/**
	 * Der Fachklassen-Schlüssel des Bildungsgangs des Schülers
	 */
	private readonly fks: string;

	/**
	 * Die Anlage, die zur Schulgliederung und Fachklasse gehört
	 */
	private readonly anlage: BeruflichesGymnasiumPruefungsordnungAnlage;

	/**
	 * Der Manager für die Fächer des beruflichen Gymnasiums
	 */
	private readonly faecherManager: BKGymFaecherManager;

	/**
	 * Das Halbjahr, bis zu welchem die Belegprüfung durchgeführt werden soll
	 */
	private readonly bisHalbjahr: GostHalbjahr;

	/**
	 * Ob eine zweite Fremdsprache in der SekI vier Jahre lang belegt wurde
	 */
	private readonly zweiteFremdspracheInSekIErfuellt: boolean;

	/**
	 * Der Belegprüfungsalgorithmus
	 */
	private readonly belegpruefung: BKGymBelegpruefung;

	/**
	 * Der Markierungsalgorithmus
	 */
	private readonly markieren: BKGymAbiturMarkierungsalgorithmus;

	/**
	 * Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind.
	 */
	private belegpruefungsfehler: List<BKGymBelegungsfehler> = new ArrayList<BKGymBelegungsfehler>();

	/**
	 * Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht.
	 */
	private belegpruefungErfolgreich: boolean = false;

	/**
	 * Das Ergebnis des Markierungsalgorithmus
	 */
	private ergebnisMarkierungsalgorithmus: BKGymAbiturMarkierungsalgorithmusErgebnis | null = null;


	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param abidaten         die Abiturdaten des Schülers
	 * @param gliederung       die Schulgliederung des Bildungsgangs des Schülers
	 * @param fks              der fünfstellige Fachklassenschlüssel des Bildungsgangs des Schülers
	 * @param faecherManager   der Manager für die Fächer
	 * @param bisHalbjahr      die Art der Belegprüfung - bis zu welchem Halbjahr geprüft werden soll
	 */
	public constructor(abidaten: BKGymAbiturdaten, gliederung: Schulgliederung, fks: string, faecherManager: BKGymFaecherManager, bisHalbjahr: GostHalbjahr) {
		super();
		this.abidaten = abidaten;
		this.gliederung = gliederung;
		this.fks = fks;
		this.faecherManager = faecherManager;
		this.fachbelegungManager = new BKGymFachbelegungManager(this);
		this.bisHalbjahr = bisHalbjahr;
		this.zweiteFremdspracheInSekIErfuellt = this.istZweiteFremdspracheInSekIErfuellt();
		this.anlage = this.bestimmeAnlage();
		const tafeln: List<BeruflichesGymnasiumStundentafel> = this.getStundentafeln();
		this.stundentafelManager = new BKGymStundentafelManager(this.fachbelegungManager, tafeln);
		this.belegpruefung = this.getBelegpruefung();
		this.markieren = new BKGymAbiturMarkierungsalgorithmus(this);
	}

	/**
	 * Führte die Schritte zur Belegprüfung aus
	 */
	private belegPruefung(): void {
		this.belegpruefung.pruefe();
		this.belegpruefungsfehler = this.belegpruefung.getBelegungsfehler();
		this.belegpruefungErfolgreich = this.belegpruefung.istErfolgreich();
	}

	/**
	 * Markiert zuerst die Kurse und führt dann eine Prüfung der Zulassung durch
	 */
	private zulassungsPruefung(): void {
		this.ergebnisMarkierungsalgorithmus = this.markieren.berechne();
	}

	/**
	 * Ermittelt die Anlage zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @return die Anlage
	 */
	private bestimmeAnlage(): BeruflichesGymnasiumPruefungsordnungAnlage {
		let _sevar_1540609204 : any;
		const _seexpr_1540609204 = (this.gliederung);
		if (_seexpr_1540609204 === Schulgliederung.D01) {
			_sevar_1540609204 = this.getAnlageD01();
		} else if (_seexpr_1540609204 === Schulgliederung.D02) {
			_sevar_1540609204 = this.getAnlageD02();
		} else {
			throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + this.gliederung.name() + " wird noch nicht unterstützt.");
		}
		return _sevar_1540609204;
	}

	private getAnlageD01(): BeruflichesGymnasiumPruefungsordnungAnlage {
		let _sevar_2040674675 : any;
		const _seexpr_2040674675 = (this.fks);
		if (_seexpr_2040674675 === "10100") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D6;
		} else if (_seexpr_2040674675 === "10200") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D1;
		} else if (_seexpr_2040674675 === "10300") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D7;
		} else if (_seexpr_2040674675 === "10400") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D8;
		} else if (_seexpr_2040674675 === "10500") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D2;
		} else if (_seexpr_2040674675 === "10600") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D3;
		} else if (_seexpr_2040674675 === "10700") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D4;
		} else if (_seexpr_2040674675 === "10900") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D12;
		} else if (_seexpr_2040674675 === "11100") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D9;
		} else if (_seexpr_2040674675 === "11200") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D13;
		} else if (_seexpr_2040674675 === "11400") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D10;
		} else if (_seexpr_2040674675 === "11500") {
			_sevar_2040674675 = BeruflichesGymnasiumPruefungsordnungAnlage.D3a;
		} else {
			throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + this.gliederung.name() + " und den Fachklassenschlüssel " + this.fks + " wird noch nicht unterstützt.");
		}
		return _sevar_2040674675;
	}

	private getAnlageD02(): BeruflichesGymnasiumPruefungsordnungAnlage {
		let _sevar_999310226 : any;
		const _seexpr_999310226 = (this.fks);
		if (_seexpr_999310226 === "10100") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D14;
		} else if (_seexpr_999310226 === "10200") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D27;
		} else if (_seexpr_999310226 === "10300") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D22;
		} else if (_seexpr_999310226 === "10400") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D23;
		} else if (_seexpr_999310226 === "10600") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D25;
		} else if (_seexpr_999310226 === "10700") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D15;
		} else if (_seexpr_999310226 === "10900") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D19;
		} else if (_seexpr_999310226 === "11000") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D16;
		} else if (_seexpr_999310226 === "11100") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D17;
		} else if (_seexpr_999310226 === "11300") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D18;
		} else if (_seexpr_999310226 === "11400") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D20;
		} else if (_seexpr_999310226 === "11500") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D21;
		} else if (_seexpr_999310226 === "12000") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D17a;
		} else if (_seexpr_999310226 === "12100") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D15a;
		} else if (_seexpr_999310226 === "12200") {
			_sevar_999310226 = BeruflichesGymnasiumPruefungsordnungAnlage.D28;
		} else {
			throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + this.gliederung.name() + " und den Fachklassenschlüssel " + this.fks + " wird noch nicht unterstützt.");
		}
		return _sevar_999310226;
	}

	/**
	 * Erstellt die zugehörige Belegprüfung mit den Abiturdaten anhand des übergebenen Bildungsganges.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private getBelegpruefung(): BKGymBelegpruefung {
		return new BKGymBelegpruefung(this);
	}

	/**
	 * Getter für den Zugriff auf den Manager der Fachbelegung
	 *
	 * @return den fachbelegungManager
	 */
	public getFachbelegungManager(): BKGymFachbelegungManager {
		return this.fachbelegungManager;
	}

	/**
	 * Getter für den Zugriff auf den Manager der Stundentafeln
	 *
	 * @return den stundentafelManager
	 */
	public getStundentafelManager(): BKGymStundentafelManager {
		return this.stundentafelManager;
	}

	/**
	 * Getter für den Zugriff auf den Fächer-Manager
	 *
	 * @return den faecherManager
	 */
	public getFaecherManager(): BKGymFaecherManager {
		return this.faecherManager;
	}

	/**
	 * Getter für den Zugriff auf die Abiturdaten
	 *
	 * @return die Abiturdaten
	 */
	public getAbidaten(): BKGymAbiturdaten {
		return this.abidaten;
	}

	/**
	 * Getter für den Zugriff auf die Schulgliederung des Bildungsganges
	 *
	 * @return die Schulgliederung des Bildungsganges
	 */
	public getGliederung(): Schulgliederung {
		return this.gliederung;
	}

	/**
	 * Getter für den Zugriff auf den Fachklassenschlüssel des Bildungsganges
	 *
	 * @return der Fachklassenschlüssel des Bildungsganges
	 */
	public getFachklassenschluessel(): string {
		return this.fks;
	}

	/**
	 * Getter für den Zugriff auf den Status der zweiten Fremdsprache
	 *
	 * @return ob die zweite Fremdsprache in der SI ausreichend belegt war.
	 */
	public getZweiteFremdspracheInSekIErfuellt(): boolean {
		return this.zweiteFremdspracheInSekIErfuellt;
	}

	/**
	 * Prüft ob eine Facharbeit vorhanden ist
	 * Das Fach wird hier nicht einbezogen, sondern beim Markieren geprüft
	 *
	 * @return true, wenn Facharbeit vorhanden ist, sonst false
	 */
	public istFacharbeitVorhanden(): boolean {
		const notenpunkte: number | null = this.getAbidaten().facharbeitNotenpunkte;
		return (notenpunkte !== null) && (notenpunkte > 0);
	}

	/**
	 * Getter für den Zugriff auf das Schuljahr in dem das Abitur stattfindet
	 *
	 * @return das Schuljahr des Abiturs
	 */
	public getSchuljahrAbitur(): number {
		return this.abidaten.schuljahrAbitur;
	}

	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public getBelegpruefungErgebnis(): BKGymBelegpruefungErgebnis {
		this.belegPruefung();
		const ergebnis: BKGymBelegpruefungErgebnis = new BKGymBelegpruefungErgebnis();
		ergebnis.erfolgreich = this.belegpruefungErfolgreich;
		for (let i: number = 0; i < this.belegpruefungsfehler.size(); i++) {
			const fehler: BKGymBelegungsfehler = this.belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new BKGymBelegpruefungErgebnisFehler(fehler));
		}
		return ergebnis;
	}

	/**
	 * Gibt das Ergebnis des Markierungsalgorithmus zurück. Dieses enthält, ob der Algorithmus erfolgreich gewesen ist
	 * und im Fehlerfall den Log des Ergebnisses.
	 *
	 * @return das Ergebnis der Markierungsalgorithmus
	 */
	public getErgebnisMarkierungsalgorithmus(): BKGymAbiturMarkierungsalgorithmusErgebnis {
		this.zulassungsPruefung();
		if (this.ergebnisMarkierungsalgorithmus === null)
			return new BKGymAbiturMarkierungsalgorithmusErgebnis();
		return this.ergebnisMarkierungsalgorithmus;
	}

	/**
	 * liefert zu einer fachID die Fachbezeichnung
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die Fachbezeichnung
	 */
	public getBezeichnungByFachID(id: number): string {
		return this.faecherManager.getBezeichnungByFachID(id);
	}

	/**
	 * Prüft, ob es sich bei der Fachbelegung um eine Belegung einer Fremdsprache handelt.
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return true, wenn es sich um eine Fremdsprachenbelegung handelt, und ansonsten false
	 */
	public istFremdsprachenbelegung(fb: BKGymAbiturFachbelegung): boolean {
		const fbFach: BKGymFach | null = this.faecherManager.get(fb.fachID);
		if ((fbFach === null) || (fbFach.bezeichnung === null))
			return false;
		return fbFach.istFremdsprache;
	}

	/**
	 * Liefert die Stundentafeln, die zur APO-BK-Anlage dieses Managers gehören
	 *
	 * @return die Liste der Stundentafeln
	 */
	public getStundentafeln(): List<BeruflichesGymnasiumStundentafel> {
		const result: List<BeruflichesGymnasiumStundentafel> = new ArrayList<BeruflichesGymnasiumStundentafel>();
		const schuljahr: number = this.getSchuljahrAbitur();
		const poke: BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag | null = this.anlage.daten(schuljahr);
		if (poke === null)
			return result;
		return poke.stundentafeln;
	}

	/**
	 * Gibt zurück, ob das angegebene Halbjahr bereits bewertet ist oder nicht.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return true, falls es bereits bewertet ist
	 */
	public istBewertet(halbjahr: GostHalbjahr): boolean {
		return this.abidaten.bewertetesHalbjahr[halbjahr.id];
	}

	/**
	 * Gibt zurück, ob alle Halbjahr der Qualifikationsphase bewertet sind oder nicht.
	 *
	 * @return true, falls alle Halbjahre bewertet sind, und ansonsten false
	 */
	public istBewertetQualifikationsPhase(): boolean {
		for (const hj of GostHalbjahr.getQualifikationsphase())
			if (!this.istBewertet(hj))
				return false;
		return true;
	}

	/**
	 * Ermittelt, ob in der SekI eine zweite Fremdsprache über vier Jahre belegt wurde anhand der Sprachdaten in
	 * den AbiDaten.
	 *
	 * @return true, wenn die Belegung einer zweiten Fremdsprache nicht ununterbrochen über vier Jahre belegt war.
	 */
	private istZweiteFremdspracheInSekIErfuellt(): boolean {
		for (const belegung of this.abidaten.sprachendaten.belegungen) {
			if ((belegung.reihenfolge === null) || (belegung.belegungVonJahrgang === null) || (belegung.belegungBisJahrgang === null) || (belegung.belegungVonAbschnitt === null) || (belegung.belegungBisAbschnitt === null))
				continue;
			if (!JavaObject.equalsTranspiler(belegung.sprache, ("E"))) {
				let anzHalbjahre: number = (SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang) - SprachendatenUtils.getJahrgangNumerisch(belegung.belegungVonJahrgang) + 1) * 2;
				anzHalbjahre += belegung.belegungBisAbschnitt - belegung.belegungVonAbschnitt - 1;
				if (anzHalbjahre >= 8)
					return true;
			}
		}
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymAbiturdatenManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymAbiturdatenManager'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturdatenManager>('de.svws_nrw.core.abschluss.bk.d.BKGymAbiturdatenManager');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymAbiturdatenManager(obj: unknown): BKGymAbiturdatenManager {
	return obj as BKGymAbiturdatenManager;
}
