import { JavaObject } from '../../../../java/lang/JavaObject';
import { BKGymAbiturMarkierungsalgorithmusErgebnis } from '../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusErgebnis';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit';
import { BKGymAbiturdaten } from '../../../../core/data/bk/abi/BKGymAbiturdaten';
import { BKGymBelegpruefungErgebnis } from '../../../../core/data/bk/abi/BKGymBelegpruefungErgebnis';
import { BKGymFaecherManager } from '../../../../core/utils/bk/BKGymFaecherManager';
import { BeruflichesGymnasiumPruefungsordnungAnlage } from '../../../../asd/types/schule/BeruflichesGymnasiumPruefungsordnungAnlage';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { Schulgliederung } from '../../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../../java/util/List';
import { BeruflichesGymnasiumStundentafelFach } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';
import { BKGymBelegpruefungErgebnisFehler } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungErgebnisFehler';
import { HashSet } from '../../../../java/util/HashSet';
import { BKGymFach } from '../../../../core/data/bk/abi/BKGymFach';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag } from '../../../../asd/data/schule/BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag';
import { BKGymAbiturFachbelegung } from '../../../../core/data/bk/abi/BKGymAbiturFachbelegung';
import { BKGymAbiturFachbelegungHalbjahr } from '../../../../core/data/bk/abi/BKGymAbiturFachbelegungHalbjahr';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { BKGymAbiturMarkierungsalgorithmus } from '../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsalgorithmus';
import { SprachendatenUtils } from '../../../../core/utils/schueler/SprachendatenUtils';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { Note } from '../../../../asd/types/Note';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';

export class BKGymAbiturdatenManager extends JavaObject {

	/**
	 * Die Zweite Fremdsprache
	 */
	public static readonly ZWEITE_FREMDSPRACHE: string = "Zweite Fremdsprache";

	/**
	 * Die Neueinsetzende Fremdsprache
	 */
	public static readonly NEUE_FREMDSPRACHE: string = "Neue Fremdsprache";

	/**
	 * Das Wahlfach
	 */
	public static readonly WAHLFACH: string = "Wahlfach";

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
	 * FachID der zweiten Fremdsprache
	 */
	private readonly zweiteFremdspracheID: number | null;

	/**
	 * Ob eine zweite Fremdsprache in der SekI vier Jahre lang belegt wurde
	 */
	private readonly zweiteFremdspracheInSekIErfuellt: boolean;

	/**
	 * Ob das Fach der Facharbeit ein LK ist
	 */
	private readonly istFacharbeitLK: boolean;

	/**
	 * Der Belegprüfungsalgorithmus
	 */
	private readonly belegpruefung: BKGymBelegpruefung;

	/**
	 * Der Markierungsalgorithmus
	 */
	private readonly markieren: BKGymAbiturMarkierungsalgorithmus;

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach anhand der Bezeichnung ermöglicht
	 */
	private readonly mapFachbelegungenByFachbezeichnung: JavaMap<string, BKGymAbiturFachbelegung> = new HashMap<string, BKGymAbiturFachbelegung>();

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
	private readonly ergebnisMarkierungsalgorithmus: BKGymAbiturMarkierungsalgorithmusErgebnis = new BKGymAbiturMarkierungsalgorithmusErgebnis();

	/**
	 * Eine Map, welche von der Nummer des Abiturfaches auf die Fachbelegung der Abiturdaten verweist.
	 */
	private readonly mapAbiturfachbelegungen: HashMap<number, BKGymAbiturFachbelegung> = new HashMap<number, BKGymAbiturFachbelegung>();


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
		this.bisHalbjahr = bisHalbjahr;
		this.zweiteFremdspracheID = this.ermittleZweiteFremdspracheID();
		this.zweiteFremdspracheInSekIErfuellt = this.istZweiteFremdspracheInSekIErfuellt();
		this.anlage = this.bestimmeAnlage();
		this.belegpruefung = this.getBelegpruefung();
		this.markieren = new BKGymAbiturMarkierungsalgorithmus(this);
		this.istFacharbeitLK = this.pruefeIstFacharbeitLK();
		this.init();
	}

	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public init(): void {
		this.mapFachbelegungenByFachbezeichnung.clear();
		this.mapAbiturfachbelegungen.clear();
		const fachbelegungen: List<BKGymAbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of fachbelegungen) {
			if (fachbelegung.abiturFach !== null)
				this.mapAbiturfachbelegungen.put(fachbelegung.abiturFach, fachbelegung);
			const fach: BKGymFach | null = this.faecherManager.get(fachbelegung.fachID);
			if ((fach === null) || (fach.bezeichnung === null))
				continue;
			this.mapFachbelegungenByFachbezeichnung.put(fach.bezeichnung, fachbelegung);
		}
	}

	/**
	 * Ermittelt ob die Facharbeit einem LK-Fach zugeordnet ist.
	 * Wird dann auf false gesetzt, wenn eine Facharbeit vorhanden ist und die Fachbezeichnung
	 * für die Facharbeit nicht dem LK1 oder LK2 zugeordnet werden kann.
	 *
	 * @return false wenn Facharbeit vorhanden und nicht einem LK zugeordnet sonst true
	 */
	private pruefeIstFacharbeitLK(): boolean {
		if (this.abidaten.facharbeitFachbezeichnung === null)
			return true;
		const facharbeitFachID: number | null = this.faecherManager.getFachIDByBezeichnung(this.abidaten.facharbeitFachbezeichnung);
		if (facharbeitFachID === null)
			return false;
		const fachIDLK1: number | null = this.getAbiFachID(GostAbiturFach.LK1);
		if (fachIDLK1 !== null && JavaObject.equalsTranspiler(facharbeitFachID, (fachIDLK1)))
			return true;
		const fachIDLK2: number | null = this.getAbiFachID(GostAbiturFach.LK2);
		if (fachIDLK2 === null)
			return false;
		return JavaObject.equalsTranspiler(facharbeitFachID, (fachIDLK2));
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
		if (this.istBewertetQualifikationsPhase()) {
			this.markieren.berechne();
		}
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
	 * Getter für den Zugriff auf die Abiturdaten
	 *
	 * @return die Abiturdaten
	 */
	public getAbidaten(): BKGymAbiturdaten {
		return this.abidaten;
	}

	/**
	 * Getter für den Zugriff auf die Anlage
	 *
	 * @return die Anlage
	 */
	public getAnlage(): BeruflichesGymnasiumPruefungsordnungAnlage {
		return this.anlage;
	}

	/**
	 * Getter für den Zugriff auf das Halbjahr, bis zu welchem geprüft werde soll
	 *
	 * @return das Halbjahr
	 */
	public getBisHalbjahr(): GostHalbjahr {
		return this.bisHalbjahr;
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
	 * Getter für den Zugriff auf die FachID der zweiten Fremdsprache
	 *
	 * @return die FachID
	 */
	public getZweiteFremdspracheID(): number | null {
		return this.zweiteFremdspracheID;
	}

	/**
	 * liefert die Bezeichnung der zweiten Fremdsprache
	 *
	 * @return die Bezeichnung der zweiten Fremdsprache
	 */
	public getZweiteFremdspracheBezeichnung(): string | null {
		return this.zweiteFremdspracheID === null ? null : this.faecherManager.getBezeichnungByFachID(this.zweiteFremdspracheID);
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
	 * Getter für den Zugriff auf istFacharbeitLK
	 *
	 * @return ob ggfs. die Facharbeit einem LK-Fach zugeordnet ist
	 */
	public getIstFacharbeitLK(): boolean {
		return this.istFacharbeitLK;
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
	 * Prüft ob es sich um die Bezeichnung für das symbolische Wahlfach handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public istWahlfach(bezeichnung: string): boolean {
		return JavaObject.equalsTranspiler(bezeichnung, (BKGymAbiturdatenManager.WAHLFACH));
	}

	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Zweite Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für die zweite Fremdsprache ist, sonst false
	 */
	public istZweiteFremdsprache(bezeichnung: string): boolean {
		return JavaObject.equalsTranspiler(bezeichnung, (BKGymAbiturdatenManager.ZWEITE_FREMDSPRACHE));
	}

	/**
	 * Prüft ob es sich um die Bezeichnung für das symbolische Fach Neue Fremdsprache handelt.
	 * @param bezeichnung   eine Fachbezeichnung aus der Stundentafel
	 *
	 * @return true wenn es die Repräsentation für das Wahlfach ist, sonst false
	 */
	public istNeueFremdsprache(bezeichnung: string): boolean {
		return JavaObject.equalsTranspiler(bezeichnung, (BKGymAbiturdatenManager.NEUE_FREMDSPRACHE));
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
		return this.ergebnisMarkierungsalgorithmus;
	}

	/**
	 * Liefert eine Belegung anhand der Fachbezeichnung zurück
	 *
	 * @param bezeichnung   das Fach
	 *
	 * @return die Fachbelegung
	 */
	public getFachbelegungByBezeichnung(bezeichnung: string): BKGymAbiturFachbelegung | null {
		return this.mapFachbelegungenByFachbezeichnung.get(bezeichnung);
	}

	/**
	 * Liefert die FachID anhand der Fachbezeichnung zurück
	 *
	 * @param bezeichnung   das Fach
	 *
	 * @return die FachID oder null, wenn die Bezeichnung nicht existiert.
	 */
	public getFachIDByBezeichnung(bezeichnung: string): number | null {
		const fach: BKGymAbiturFachbelegung | null = this.mapFachbelegungenByFachbezeichnung.get(bezeichnung);
		if (fach === null)
			return null;
		return fach.fachID;
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
	 * Gibt das Abiturfachdaten für das geforderte Abiturfach zurück.
	 *
	 * @param abiFach Das n. Abiturfach, das gewünscht ist
	 *
	 * @return die entsprechende Fachbelegung des Abiturfachs
	 */
	public getAbiFachbelegung(abiFach: GostAbiturFach): BKGymAbiturFachbelegung | null {
		return this.mapAbiturfachbelegungen.get(abiFach.id);
	}

	/**
	 * Gibt die FachID für das geforderte Abiturfach zurück.
	 *
	 * @param abiFach Das n. Abiturfach, das gewünscht ist
	 *
	 * @return die entsprechende FachID des Abiturfachs oder null wenn es nicht gefunden wird.
	 */
	public getAbiFachID(abiFach: GostAbiturFach): number | null {
		const abifach: BKGymAbiturFachbelegung | null = this.getAbiFachbelegung(abiFach);
		if (abifach === null)
			return null;
		return abifach.fachID;
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
	 * Prüft, ob es sich bei der Fachbelegung um eine Belegung einer neu einsetzenden Fremdsprache handelt.
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return true, wenn es sich um ein neu einsetzende Fremdsprachenbelegung handelt, und ansonsten false
	 */
	public istNeueFremdsprachenbelegung(fb: BKGymAbiturFachbelegung): boolean {
		const fbFach: BKGymFach | null = this.faecherManager.get(fb.fachID);
		if ((fbFach === null) || (fbFach.bezeichnung === null))
			return false;
		return fbFach.istFremdSpracheNeuEinsetzend;
	}

	/**
	 * liefert die Fachbezeichnung einer Belegung
	 *
	 * @param fb   die Fachbelegung
	 *
	 * @return die Fachbezeichnung
	 */
	public getFachkuerzelFromFachbelegung(fb: BKGymAbiturFachbelegung): string {
		const fbFach: BKGymFach | null = this.faecherManager.get(fb.fachID);
		if ((fbFach === null) || (fbFach.kuerzelAnzeige === null))
			return "";
		return fbFach.kuerzelAnzeige;
	}

	/**
	 * Prüft, ob die übergebene Fachbelgung als Fach in der Stundentafel vorkommt bzw. vorkommen kann.
	 *
	 * @param tafel   die Stundentafel
	 * @param fb      die Fachbelegung
	 *
	 * @return der Eintrag der Stundentafel, bei welchem die Fachbelegung vorkommt, oder null, wenn keine Zuordnung zur Stundentafel möglich ist
	 */
	public getFachByBelegung(tafel: BeruflichesGymnasiumStundentafel, fb: BKGymAbiturFachbelegung): BeruflichesGymnasiumStundentafelFach | null {
		const fbFach: BKGymFach | null = this.faecherManager.get(fb.fachID);
		if ((fbFach === null) || (fbFach.bezeichnung === null))
			return null;
		for (const tafelFach of tafel.faecher)
			if (JavaObject.equalsTranspiler(tafelFach.fachbezeichnung, (fbFach.bezeichnung)))
				return tafelFach;
		if (fbFach.istFremdsprache)
			for (const tafelFach of tafel.faecher)
				if (JavaObject.equalsTranspiler(tafelFach.fachbezeichnung, (BKGymAbiturdatenManager.ZWEITE_FREMDSPRACHE)))
					return tafelFach;
		for (const tafelFach of tafel.faecher)
			if (JavaObject.equalsTranspiler(tafelFach.fachbezeichnung, (BKGymAbiturdatenManager.WAHLFACH)))
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
	public istGueltigeKursartFachbelegung(tafel: BeruflichesGymnasiumStundentafel, fb: BKGymAbiturFachbelegung, abifach: GostAbiturFach): boolean {
		const tafelFach: BeruflichesGymnasiumStundentafelFach | null = this.getFachByBelegung(tafel, fb);
		if (tafelFach === null)
			return false;
		if (tafelFach.abifach === null)
			return true;
		let _sevar_1247436812 : any;
		const _seexpr_1247436812 = (abifach);
		if (_seexpr_1247436812 === GostAbiturFach.LK1) {
			_sevar_1247436812 = (tafelFach.abifach === 1) && JavaObject.equalsTranspiler(tafelFach.kursart, ("LK"));
		} else if (_seexpr_1247436812 === GostAbiturFach.LK2) {
			_sevar_1247436812 = (tafelFach.abifach === 2) && JavaObject.equalsTranspiler(tafelFach.kursart, ("LK"));
		} else if (_seexpr_1247436812 === GostAbiturFach.AB3) {
			_sevar_1247436812 = BKGymAbiturdatenManager.istNtesAbifach(tafel.wahlmoeglichkeiten, abifach, tafelFach);
		} else if (_seexpr_1247436812 === GostAbiturFach.AB4) {
			_sevar_1247436812 = BKGymAbiturdatenManager.istNtesAbifach(tafel.wahlmoeglichkeiten, abifach, tafelFach);
		} else if (_seexpr_1247436812 === GostAbiturFach.AB5) {
			_sevar_1247436812 = BKGymAbiturdatenManager.istNtesAbifach(tafel.wahlmoeglichkeiten, abifach, tafelFach);
		}
		return _sevar_1247436812;
	}

	/**
	 * Prüft ob das Fach in der Liste der Wahlmöglichkeiten für ein bestimmtes Abiturfach ist
	 *
	 * @param wahlmoeglichkeiten   die Wahlmöglichkeiten aus der Stundentafel
	 * @param abifach              welches Abiturfach
	 * @param tafelFach            das belegte Fach
	 *
	 * @return true, wenn es eine gültige Belegung ist.
	 */
	private static istNtesAbifach(wahlmoeglichkeiten: List<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit>, abifach: GostAbiturFach, tafelFach: BeruflichesGymnasiumStundentafelFach): boolean {
		for (const wm of wahlmoeglichkeiten) {
			let abifaecher: List<string>;
			const _seexpr_496852165 = (abifach);
			if (_seexpr_496852165 === GostAbiturFach.AB3) {
				abifaecher = wm.abifach3;
			} else if (_seexpr_496852165 === GostAbiturFach.AB4) {
				abifaecher = wm.abifach4;
			} else if (_seexpr_496852165 === GostAbiturFach.AB5) {
				abifaecher = wm.abifach5;
			} else {
				abifaecher = new ArrayList();
			}
			;
			for (const bezeichnung of abifaecher)
				if (JavaObject.equalsTranspiler(bezeichnung, (tafelFach.fachbezeichnung)))
					return true;
		}
		return false;
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
	private istGueltigeWahlmoeglichkeit(wm: BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit, ab3: BKGymAbiturFachbelegung, ab4: BKGymAbiturFachbelegung): boolean {
		const ab3Fach: BKGymFach | null = this.faecherManager.get(ab3.fachID);
		const ab4Fach: BKGymFach | null = this.faecherManager.get(ab4.fachID);
		if ((ab3Fach === null) || (ab4Fach === null))
			return false;
		let wm3: string | null = null;
		for (const fachBez3 of wm.abifach3)
			if (JavaObject.equalsTranspiler(fachBez3, (ab3Fach.bezeichnung)) || (JavaObject.equalsTranspiler(BKGymAbiturdatenManager.ZWEITE_FREMDSPRACHE, (fachBez3)) && ab3Fach.istFremdsprache) || JavaObject.equalsTranspiler(BKGymAbiturdatenManager.WAHLFACH, (fachBez3)))
				wm3 = fachBez3;
		if (wm3 === null)
			return false;
		for (const fachBez4 of wm.abifach4)
			if (JavaObject.equalsTranspiler(fachBez4, (ab4Fach.bezeichnung)) || (JavaObject.equalsTranspiler(BKGymAbiturdatenManager.ZWEITE_FREMDSPRACHE, (fachBez4)) && ab4Fach.istFremdsprache) || JavaObject.equalsTranspiler(BKGymAbiturdatenManager.WAHLFACH, (fachBez4)))
				return true;
		return false;
	}

	/**
	 * Prüft ob die Fachkombination für das dritte und vierte Abiturfach gültig ist.
	 *
	 * @param tafel   die zu prüfende Stundentafel mit ihren Wahlmöglichkeiten
	 * @param ab3     die Belegung des dritten Abiturfaches
	 * @param ab4     die Belegung des vierten Abiturfaches
	 *
	 * @return true, wenn die Wahlmöglichkeit besteht, ansonsten false
	 */
	public pruefeAbiGrundkurswahl(tafel: BeruflichesGymnasiumStundentafel, ab3: BKGymAbiturFachbelegung, ab4: BKGymAbiturFachbelegung): boolean {
		for (const wm of tafel.wahlmoeglichkeiten) {
			if (this.istGueltigeWahlmoeglichkeit(wm, ab3, ab4))
				return true;
		}
		return false;
	}

	/**
	 * Liefert eine Map, die zu jedem Index der Fachtafel die zugehörigen Fächer liefert.
	 * Hier sind die Wahlmöglichkeiten enthalten, die eine Stundentafelvariante erlaubt.
	 *
	 * @param tafel   die Stundentafel mit der Liste der Fächer
	 *
	 * @return die Map
	 */
	public getMapFaecherFromTafelByIndex(tafel: BeruflichesGymnasiumStundentafel): JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>> {
		const mapFaecher: JavaMap<number, List<BeruflichesGymnasiumStundentafelFach>> = new HashMap<number, List<BeruflichesGymnasiumStundentafelFach>>();
		for (const fach of tafel.faecher) {
			if ((fach.stundenumfang[0] > 0) || (fach.stundenumfang[1] > 0) || (fach.stundenumfang[2] > 0) || (fach.stundenumfang[3] > 0) || (fach.stundenumfang[4] > 0) || (fach.stundenumfang[5] > 0)) {
				let faecher: List<BeruflichesGymnasiumStundentafelFach> | null = mapFaecher.get(fach.sortierung);
				if (faecher === null) {
					faecher = new ArrayList();
					mapFaecher.put(fach.sortierung, faecher);
				}
				faecher.add(fach);
			}
		}
		return mapFaecher;
	}

	/**
	 * Liefert eine Map, die zu jedem Fach der Stundentafel die zugehörigen Belegungen zuordnet.
	 *
	 * @param tafel   die Stundentafel aus der APO-BK Anlage D
	 *
	 * @return die Map
	 */
	public getMapBelegungenForTafelByFach(tafel: BeruflichesGymnasiumStundentafel): JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> {
		const mapBelegungenByFach: JavaMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>> = new HashMap<BeruflichesGymnasiumStundentafelFach, List<BKGymAbiturFachbelegung>>();
		const zugeordnet: JavaSet<BKGymAbiturFachbelegung> = new HashSet<BKGymAbiturFachbelegung>();
		let zweiteFremdspracheBelegungen: List<BKGymAbiturFachbelegung> = new ArrayList<BKGymAbiturFachbelegung>();
		let neueFremdspracheBelegungen: List<BKGymAbiturFachbelegung> = new ArrayList<BKGymAbiturFachbelegung>();
		let wahlfachBelegungen: List<BKGymAbiturFachbelegung> = new ArrayList<BKGymAbiturFachbelegung>();
		for (const fach of tafel.faecher) {
			let belegungen: List<BKGymAbiturFachbelegung> | null = mapBelegungenByFach.get(fach);
			if (belegungen === null) {
				belegungen = new ArrayList();
				mapBelegungenByFach.put(fach, belegungen);
			}
			const belegung: BKGymAbiturFachbelegung | null = this.getFachbelegungByBezeichnung(fach.fachbezeichnung);
			if (belegung !== null) {
				belegungen.add(belegung);
				zugeordnet.add(belegung);
			} else
				if (this.istZweiteFremdsprache(fach.fachbezeichnung))
					zweiteFremdspracheBelegungen = belegungen;
				else
					if (this.istNeueFremdsprache(fach.fachbezeichnung))
						neueFremdspracheBelegungen = belegungen;
					else
						if (this.istWahlfach(fach.fachbezeichnung))
							wahlfachBelegungen = belegungen;
		}
		const fachbelegungen: List<BKGymAbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of fachbelegungen) {
			if (!zugeordnet.contains(fachbelegung)) {
				if (this.istNeueFremdsprachenbelegung(fachbelegung))
					neueFremdspracheBelegungen.add(fachbelegung);
				else
					if (this.istFremdsprachenbelegung(fachbelegung))
						zweiteFremdspracheBelegungen.add(fachbelegung);
					else
						wahlfachBelegungen.add(fachbelegung);
			}
		}
		return mapBelegungenByFach;
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
	 * Liefert die FachID der zweiten Fremdsprache oder null, falls nicht vorhanden
	 *
	 * @return die ID der zweiten Fremdsprache oder null
	 */
	private ermittleZweiteFremdspracheID(): number | null {
		for (const entry of this.mapAbiturfachbelegungen.entrySet()) {
			const fach: BKGymFach | null = this.faecherManager.get(entry.getValue().fachID);
			if ((fach !== null) && fach.istFremdsprache && !JavaObject.equalsTranspiler(fach.bezeichnung, ("Englisch")))
				return fach.id;
		}
		return null;
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
				let anzHalbjahre: number = (SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang) - SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang) + 1) * 2;
				anzHalbjahre += belegung.belegungBisAbschnitt - belegung.belegungVonAbschnitt - 1;
				if (anzHalbjahre >= 8)
					return true;
			}
		}
		return false;
	}

	/**
	 * Delegation für die doppelten Fächer als List
	 *
	 * @return die Liste der doppelten Fächer
	 */
	public getDoppelteFaecher(): List<string> {
		return this.faecherManager.getDoppelteFaecher();
	}

	/**
	 * Gibt zurück, ob es sich bei der Halbjahresbelegung um eine Belegung handelt, welche mit
	 * null Punkten abgeschlossen wurde und welche daher als nicht belegter Kurs zu werten ist.
	 *
	 * @param halbjahresbelegung   die Halbjahresbelegung eines Kurses
	 *
	 * @return true, fall es sich um einen Null-Punkte-Kurs in der Qualifikationsphase handelt.
	 */
	public static istNullPunkteBelegungInQPhase(halbjahresbelegung: BKGymAbiturFachbelegungHalbjahr): boolean {
		const hj: GostHalbjahr | null = GostHalbjahr.fromKuerzel(halbjahresbelegung.halbjahrKuerzel);
		if ((hj === null) || (hj.istEinfuehrungsphase()))
			return false;
		return Note.fromKuerzel(halbjahresbelegung.notenkuerzel) as unknown === Note.UNGENUEGEND as unknown;
	}

	/**
	 * Prüft, ob das Fach in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public pruefeBelegung(fachbelegung: BKGymAbiturFachbelegung | null, ...halbjahre: Array<GostHalbjahr>): boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr: BKGymAbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (belegungHalbjahr.kursartKuerzel === null))
				return false;
			if (BKGymAbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr))
				return false;
		}
		return true;
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
