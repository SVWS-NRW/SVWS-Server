import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { HashMap } from '../../java/util/HashMap';
import { Schulform } from '../../asd/types/schule/Schulform';
import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorFehlerartKontext } from '../../asd/validate/ValidatorFehlerartKontext';
import { JavaString } from '../../java/lang/JavaString';
import { ValidatorFehlerartKontextPruefschritt } from '../../asd/validate/ValidatorFehlerartKontextPruefschritt';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { ValidatorException } from '../../asd/validate/ValidatorException';
import { PairNN } from '../../asd/adt/PairNN';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { CoreTypeData } from '../../asd/data/CoreTypeData';
import type { JavaMap } from '../../java/util/JavaMap';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';
import { CoreTypeException } from '../../asd/data/CoreTypeException';
import { HashSet } from '../../java/util/HashSet';

export class ValidatorManager extends JavaObject {

	/**
	 * Die Version der Fehlerart-Kontexte
	 */
	private static _version : number = 0;

	/**
	 * Die Fehlerart-Kontexte für jeden Validator als Historienliste
	 */
	private static _data : JavaMap<string, List<ValidatorFehlerartKontext>>;

	/**
	 * Die ValidatorManager pro Schulform für den SVWS-Kontext
	 */
	private static _managerSVWS : JavaMap<Schulform, ValidatorManager> = new HashMap<Schulform, ValidatorManager>();

	/**
	 * Die ValidatorManager pro Schulform für deb Zebras-Kontext
	 */
	private static _managerZebras : JavaMap<Schulform, ValidatorManager> = new HashMap<Schulform, ValidatorManager>();

	/**
	 * Die Schulform, für den der ValidatorManager gilt
	 */
	private readonly _schulform : Schulform;

	/**
	 * Die Umgebung, für den der ValidatorManager erzeugt wurde: true = ZeBrAS ; false = SVWS
	 */
	private readonly _isZebras : boolean;

	/**
	 * Eine geschachtelte Map, die einem Schuljahr eine Map mit der Zuordnung der Validatoren zu den Prüfschritten und deren Fehlerarten für die Schulform _schulform
	 */
	private readonly _mapSchuljahrValidatornameToFehlerart : HashMap<number, HashMap<string, HashMap<number, ValidatorFehlerart>>> = new HashMap<number, HashMap<string, HashMap<number, ValidatorFehlerart>>>();

	/**
	 * Eine geschachtelte Map, die einem Schuljahr eine Map mit der Zuordnung der Validatoren zu den Fehlercode-Präfixen des Validators
	 */
	private readonly _mapSchuljahrValidatornameToFehlercodePraefix : HashMap<number, HashMap<string, string>> = new HashMap<number, HashMap<string, string>>();


	/**
	 * Erstellt einen neuen Manager für die übergebene Schulform und die Entsprechene Validierungsumgebung
	 * (Zebras oder SVWS)
	 *
	 * @param zebras      die Umgebung, in der gerade validiert wird: true: ZeBrAS  false: SVWS
	 * @param schulform   die Schulform, für die gerade
	 */
	private constructor(schulform : Schulform, zebras : boolean) {
		super();
		this._schulform = schulform;
		this._isZebras = zebras;
	}

	/**
	 * Initialisierung des Validators mit den Daten, die aus einem json eingelesen wurden.
	 *
	 * @param version	Die Versionsnummer der Daten zu den Fehlerart-Kontexten.
	 * @param data		Die aus der JSON-Datei eingelesenen Daten.
	 */
	public static init(version : number, data : JavaMap<string, List<ValidatorFehlerartKontext>>) : void {
		ValidatorManager._version = version;
		ValidatorManager._data = data;
		ValidatorManager._managerSVWS = new HashMap();
		ValidatorManager._managerZebras = new HashMap();
		for (const entry of ValidatorManager._data.entrySet()) {
			const validatorName : string = entry.getKey();
			const list : List<ValidatorFehlerartKontext> = entry.getValue();
			const mapZeitraeumeBySchulform : HashMap<string, List<PairNN<number, number>>> = new HashMap<string, List<PairNN<number, number>>>();
			for (const eintrag of list) {
				let prfDefault : ValidatorFehlerartKontextPruefschritt | null = null;
				for (const prf of eintrag.pruefschritte) {
					if (prf.nummer < -1)
						throw new CoreTypeException(JavaString.format("Fehler bei der Definition der Prüfschritte. Der Validator %s hat eine Nummer für einen Prüfschritt angegeben, der kleiner als -1 ist.", validatorName))
					if (prf.nummer === -1) {
						prfDefault = prf;
						const zeitraum : PairNN<number, number> = ValidatorManager.createZeitraum(eintrag.gueltigVon, eintrag.gueltigBis);
						ValidatorManager.addZeitraum(mapZeitraeumeBySchulform, zeitraum, prf.muss);
						ValidatorManager.addZeitraum(mapZeitraeumeBySchulform, zeitraum, prf.kann);
						ValidatorManager.addZeitraum(mapZeitraeumeBySchulform, zeitraum, prf.hinweis);
					}
				}
				if (prfDefault === null)
					throw new CoreTypeException(JavaString.format("Fehler bei der Definition der Prüfschritte. Der Validator %s hat keine Default-Definition für Prüfschritte.", validatorName))
			}
			for (const zeitraeume of mapZeitraeumeBySchulform.entrySet()) {
				const l : List<CoreTypeData> = new ArrayList<CoreTypeData>();
				const sf : Schulform | null = Schulform.valueOf(zeitraeume.getKey());
				if (sf !== null)
					l.addAll(sf.historie());
				if (!ValidatorManager.pruefeAufZeitraumueberdeckung(validatorName, ValidatorManager.createSchulformZeitraumListe(l), zeitraeume.getValue()))
					throw new CoreTypeException(JavaString.format("Fehler beim Prüfen der Schulform. Der Validator %s hat ungültige Schulform-Zeitraum-Kombinationen.", validatorName))
			}
		}
	}

	/**
	 * Gibt den Manager für die Schulform und Umgebung zurück, wobei er erzeugt wird, wenn
	 * er nicht existiert.
	 *
	 * @param schulform  die Schulform, für die der Manager benötigt wird
	 * @param isZebras   die entsprechende Umgebung
	 *
	 * @return der Validator-Manager
	 */
	public static getManager(schulform : Schulform, isZebras : boolean) : ValidatorManager {
		if (isZebras) {
			let vm : ValidatorManager | null = ValidatorManager._managerZebras.get(schulform);
			if (vm === null) {
				vm = new ValidatorManager(schulform, true);
				ValidatorManager._managerZebras.put(schulform, vm);
			}
			return vm;
		}
		let vm : ValidatorManager | null = ValidatorManager._managerSVWS.get(schulform);
		if (vm === null) {
			vm = new ValidatorManager(schulform, false);
			ValidatorManager._managerSVWS.put(schulform, vm);
		}
		return vm;
	}

	/**
	 * Gibt die Version der Fehler-Kontext-Daten zurück.
	 *
	 * @return die Version
	 */
	public static getVersion() : number {
		return ValidatorManager._version;
	}

	/**
	 * Gibt die Liste der Validatorennamen als nicht-leeres Set zurück.
	 *
	 * @return das nicht-leeres Set der Validatoren-Namen
	 */
	public static getValidatornamenAsSet() : JavaSet<string> {
		return ValidatorManager._data.keySet();
	}

	/**
	 * Gibt die Historie der Fehlerart-Kontexte für den angegebenen Validator zurück.
	 *
	 * @param validator   der kanonische Name des Validators
	 *
	 * @return die Historie
	 */
	public static getValidatorHistorie(validator : string) : List<ValidatorFehlerartKontext> {
		const tmp : List<ValidatorFehlerartKontext> | null = ValidatorManager._data.get(validator);
		if (tmp === null)
			throw new CoreTypeException("Der Validator " + validator + " existiert nicht in 'validatoren.json'.")
		return tmp;
	}

	/**
	 * Liefert für das angegebene Schuljahr die Map von dem Validatornamen zu der Fehlerart.
	 * Ist der Cache für das Schuljahr noch nicht aufgebaut, so wird dieser erstellt.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Map, die für das gegebene Schuljahr die Fehlerart pro Validator enthält
	 */
	private getValidatornameToFehlerartCache(schuljahr : number) : HashMap<string, HashMap<number, ValidatorFehlerart>> {
		const mapValidatorToFehlerart : HashMap<string, HashMap<number, ValidatorFehlerart>> = this.computeIfAbsentValidatornameToFehlerart(schuljahr);
		if (mapValidatorToFehlerart.isEmpty())
			this.createCache(schuljahr);
		return mapValidatorToFehlerart;
	}

	/**
	 * Holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param schuljahr  das Schuljahr, für das das Objekt geholt wird
	 *
	 * @return das benötigte Objekt
	 */
	private computeIfAbsentValidatornameToFehlerart(schuljahr : number) : HashMap<string, HashMap<number, ValidatorFehlerart>> {
		let mapValidatorToFehlerart : HashMap<string, HashMap<number, ValidatorFehlerart>> | null = this._mapSchuljahrValidatornameToFehlerart.get(schuljahr);
		if (mapValidatorToFehlerart === null) {
			mapValidatorToFehlerart = new HashMap();
			this._mapSchuljahrValidatornameToFehlerart.put(schuljahr, mapValidatorToFehlerart);
		}
		return mapValidatorToFehlerart;
	}

	/**
	 * Liefert für das angegebene Schuljahr die Map von dem Validatornamen zu dem Fehlercode-Präfix.
	 * Ist der Cache für das Schuljahr noch nicht aufgebaut, so wird dieser erstellt.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Map, die für das gegebene Schuljahr das Fehlercode-Präfix pro Validator enthält
	 */
	private getValidatornameToFehlercodePraefixCache(schuljahr : number) : HashMap<string, string> {
		const mapValidatorToFehlercodePraefix : HashMap<string, string> = this.computeIfAbsentValidatornameToFehlercodePraefix(schuljahr);
		if (mapValidatorToFehlercodePraefix.isEmpty())
			this.createCache(schuljahr);
		return mapValidatorToFehlercodePraefix;
	}

	/**
	 * Holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param schuljahr  das Schuljahr, für das das Objekt geholt wird
	 *
	 * @return das benötigte Objekt
	 */
	private computeIfAbsentValidatornameToFehlercodePraefix(schuljahr : number) : HashMap<string, string> {
		let mapValidatorToFehlercodePraefix : HashMap<string, string> | null = this._mapSchuljahrValidatornameToFehlercodePraefix.get(schuljahr);
		if (mapValidatorToFehlercodePraefix === null) {
			mapValidatorToFehlercodePraefix = new HashMap();
			this._mapSchuljahrValidatornameToFehlercodePraefix.put(schuljahr, mapValidatorToFehlercodePraefix);
		}
		return mapValidatorToFehlercodePraefix;
	}

	/**
	 * Holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param art - die Fehlerart, für die die Liste ggfs. erzeugt wird
	 * @param map - die HashMap mit den ArrayLists
	 *
	 * @return das benötigte Objekt
	 */
	private static computeIfAbsentFehlerartValidator(art : ValidatorFehlerart, map : JavaMap<ValidatorFehlerart, List<string>>) : List<string> {
		let list : List<string> | null = map.get(art);
		if (list === null) {
			list = new ArrayList();
			map.put(art, list);
		}
		return list;
	}

	/**
	 * holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param schulform - die Fehlerart, für die die Liste ggfs. erzeugt wird
	 * @param map - die HashMap mit den ArrayLists
	 * @return das benötigte Objekt
	 */
	private static computeIfAbsentZeitraeumeSchulform(schulform : string, map : HashMap<string, List<PairNN<number, number>>>) : List<PairNN<number, number>> {
		let list : List<PairNN<number, number>> | null = map.get(schulform);
		if (list === null) {
			list = new ArrayList();
			map.put(schulform, list);
		}
		return list;
	}

	/**
	 * Erstellt den Cache für das angegeben Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 */
	private createCache(schuljahr : number) : void {
		const mapValidatorToFehlerart : HashMap<string, HashMap<number, ValidatorFehlerart>> = this.computeIfAbsentValidatornameToFehlerart(schuljahr);
		mapValidatorToFehlerart.clear();
		const mapValidatorToFehlercodePraefix : HashMap<string, string> = this.computeIfAbsentValidatornameToFehlercodePraefix(schuljahr);
		mapValidatorToFehlercodePraefix.clear();
		const praefixe : JavaSet<string> | null = new HashSet<string>();
		for (const entry of ValidatorManager._data.entrySet()) {
			const validatorName : string = entry.getKey();
			const list : List<ValidatorFehlerartKontext> = entry.getValue();
			const mapPruefschrittToFehlerart : HashMap<number, ValidatorFehlerart> = new HashMap<number, ValidatorFehlerart>();
			mapValidatorToFehlerart.put(validatorName, mapPruefschrittToFehlerart);
			for (const eintrag of list) {
				for (const prf of eintrag.pruefschritte) {
					const hasHart : boolean = prf.muss.contains(this._schulform.name());
					const hasMuss : boolean = prf.kann.contains(this._schulform.name());
					const hasHinweis : boolean = prf.hinweis.contains(this._schulform.name());
					if ((hasHart && hasMuss) || (hasMuss && hasHinweis) || (hasHart && hasHinweis))
						throw new CoreTypeException("Ein Validator kann bei einer Schulform nicht bei einem Prüfschritt gleichzeitig bei mehreren Fehlerarten aktiv sein.")
				}
				const validatorAktivInUmgebungUndSchuljahr : boolean = (this._isZebras ? eintrag.zebras : eintrag.svws) && ((eintrag.gueltigVon === null) || (eintrag.gueltigVon <= schuljahr)) && ((eintrag.gueltigBis === null) || (schuljahr <= eintrag.gueltigBis));
				if (validatorAktivInUmgebungUndSchuljahr) {
					if (praefixe.contains(eintrag.praefix))
						throw new CoreTypeException(JavaString.format("Das Fehlercode-Präfix eines Validators muss eindeutig sein. Das Präfix %s wurde mehrfach verwendet.", eintrag.praefix))
					praefixe.add(eintrag.praefix);
					mapValidatorToFehlercodePraefix.put(validatorName, eintrag.praefix);
					for (const prf of eintrag.pruefschritte) {
						const hasHart : boolean = prf.muss.contains(this._schulform.name());
						const hasMuss : boolean = prf.kann.contains(this._schulform.name());
						const hasHinweis : boolean = prf.hinweis.contains(this._schulform.name());
						if (hasHart)
							mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.MUSS);
						else
							if (hasMuss)
								mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.KANN);
							else
								if (hasHinweis)
									mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.HINWEIS);
								else
									mapPruefschrittToFehlerart.put(prf.nummer, ValidatorFehlerart.UNGENUTZT);
					}
				}
			}
		}
	}

	/**
	 * Gibt die Fehlerart eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 *
	 * @return die Fehlerart des Validators für das angegebene Schuljahr
	 */
	public getFehlerartBySchuljahrAndValidatorNameAndPruefschritt(schuljahr : number, validator : string, pruefschritt : number) : ValidatorFehlerart | null {
		if (pruefschritt < -1)
			return null;
		const mapPruefschritt : HashMap<number, ValidatorFehlerart> | null = this.getValidatornameToFehlerartCache(schuljahr).get(validator);
		if (mapPruefschritt === null)
			return null;
		if (pruefschritt >= 0) {
			const art : ValidatorFehlerart | null = mapPruefschritt.get(pruefschritt);
			if (art !== null)
				return art;
		}
		const art : ValidatorFehlerart | null = mapPruefschritt.get(-1);
		return (art === null) ? ValidatorFehlerart.UNGENUTZT : art;
	}

	/**
	 * Gibt die Fehlerart eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param <T>            der Type des Validators
	 * @param schuljahr      das Schuljahr
	 * @param validator      die Klasse des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 *
	 * @return die Fehlerart des Validators für das angegebene Schuljahr
	 */
	public getFehlerartBySchuljahrAndValidatorClassAndPruefschritt<T extends Validator>(schuljahr : number, validator : Class<T>, pruefschritt : number) : ValidatorFehlerart {
		const tmp : ValidatorFehlerart | null = this.getFehlerartBySchuljahrAndValidatorNameAndPruefschritt(schuljahr, validator.getCanonicalName(), pruefschritt);
		return (tmp === null) ? ValidatorFehlerart.UNGENUTZT : tmp;
	}

	/**
	 * Setzt die Fehlerart eines Prüfschrittes eines Validators für das angegebene Schuljahr.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 * @param fehlerart      die Fehlerart des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 */
	public setFehlerartBySchuljahr(schuljahr : number, validator : string, fehlerart : ValidatorFehlerart, pruefschritt : number) : void {
		const mapValidator : HashMap<string, HashMap<number, ValidatorFehlerart>> = this.getValidatornameToFehlerartCache(schuljahr);
		let mapPruefschritt : HashMap<number, ValidatorFehlerart> | null = mapValidator.get(validator);
		if (mapPruefschritt === null) {
			mapPruefschritt = new HashMap();
			mapValidator.put(validator, mapPruefschritt);
		}
		mapPruefschritt.put(pruefschritt, fehlerart);
	}

	/**
	 * Prüft, ob der übergebene Validator in dem angegebenen Schuljahr aktiv ist oder nicht.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 *
	 * @return true, falls der Validator in dem Schuljahr aktiv ist.
	 */
	public isValidatorActiveInSchuljahr(schuljahr : number, validator : string) : boolean {
		const mapValidator : HashMap<string, HashMap<number, ValidatorFehlerart>> = this.getValidatornameToFehlerartCache(schuljahr);
		const mapPruefschritt : HashMap<number, ValidatorFehlerart> | null = mapValidator.get(validator);
		if (mapPruefschritt === null)
			return false;
		const fa : ValidatorFehlerart | null = mapPruefschritt.get(-1);
		return (fa !== null) && (fa as unknown !== ValidatorFehlerart.UNGENUTZT as unknown);
	}

	/**
	 * Prüft, ob der übergebene Prüfschritt des übergebenen Validators in dem angegebenen Schuljahr aktiv ist oder nicht.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 * @param pruefschritt   die Nummer des Prüfschrittes
	 *
	 * @return true, falls der Validator in dem Schuljahr aktiv ist.
	 */
	public isPruefschrittActiveInSchuljahr(schuljahr : number, validator : string, pruefschritt : number) : boolean {
		const mapValidator : HashMap<string, HashMap<number, ValidatorFehlerart>> = this.getValidatornameToFehlerartCache(schuljahr);
		const mapPruefschritt : HashMap<number, ValidatorFehlerart> | null = mapValidator.get(validator);
		if (mapPruefschritt === null)
			return false;
		const fa : ValidatorFehlerart | null = mapPruefschritt.get(pruefschritt);
		return (fa !== null) && (fa as unknown !== ValidatorFehlerart.UNGENUTZT as unknown);
	}

	/**
	 * Gibt das Fehlercode-Präfix eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param schuljahr      das Schuljahr
	 * @param validator      der kanonische Name des Validators
	 *
	 * @return das Fehlercode-Präfix des Validators für das angegebene Schuljahr
	 */
	public getFehlercodePraefixBySchuljahrAndValidatorName(schuljahr : number, validator : string) : string {
		const code : string | null = this.getValidatornameToFehlercodePraefixCache(schuljahr).get(validator);
		if (code === null)
			throw new ValidatorException(JavaString.format("Fehler beim Zugriff auf den Fehlercode-Präfix für den Validator %s im Schuljahr %d.", validator, schuljahr))
		return code;
	}

	/**
	 * Gibt das Fehlercode-Präfix eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param <T>            der Type des Validators
	 * @param schuljahr      das Schuljahr
	 * @param validator      die Klasse des Validators
	 *
	 * @return das Fehlercode-Präfix des Validators für das angegebene Schuljahr
	 */
	public getFehlercodePraefixBySchuljahrAndValidatorClass<T extends Validator>(schuljahr : number, validator : Class<T>) : string {
		return this.getFehlercodePraefixBySchuljahrAndValidatorName(schuljahr, validator.getCanonicalName());
	}

	/**
	 * Trägt aus der Liste von Schulformen den angegebenen Zeitraum in die Liste Zeiträume der jeweiligen Schulform ein.
	 *
	 * @param mapZeitraeumeBySchulform		Die map, die für jede Schulform die Liste der gültigen Zeiträume speichert
	 * @param zeitraum						Ein Zeitraum, in dem die Schulformen in der Liste schulformen gültig sind
	 * @param schulformen					Die Liste der in dem Zeitraum gültigen Schulformen.
	 */
	private static addZeitraum(mapZeitraeumeBySchulform : HashMap<string, List<PairNN<number, number>>>, zeitraum : PairNN<number, number>, schulformen : List<string>) : void {
		for (const schulform of schulformen) {
			const zeitraeumeBySchulform : List<PairNN<number, number>> = ValidatorManager.computeIfAbsentZeitraeumeSchulform(schulform, mapZeitraeumeBySchulform);
			zeitraeumeBySchulform.add(zeitraum);
		}
	}

	/**
	 * Bildet aus der Historie der Schulformen eine Liste der Zeiträume.
	 *
	 * @param historie      die Historie der Schulformen
	 *
	 * @return die Liste der Zeiträume
	 */
	private static createSchulformZeitraumListe(historie : List<CoreTypeData>) : List<PairNN<number, number>> {
		const zeitraeume : List<PairNN<number, number>> = new ArrayList<PairNN<number, number>>();
		for (const eintrag of historie)
			zeitraeume.add(ValidatorManager.createZeitraum(eintrag.gueltigVon, eintrag.gueltigBis));
		return zeitraeume;
	}

	/**
	 * Prüft ob die Zeiträumen der zweiten Liste komplett innerhalb der Zeiträume der ersten Liste liegen. In diesem Zusammenhang wird geprüft,
	 * ob alle Zeiträume, wo ein Validator gültig sein soll auch durch die Gültigkeit bei der entsprechenden Schulform abgedeckt ist. <br>
	 * <br>
	 * Kurzbeschreibung des Algorithmus: <br>
	 *
	 * Beide Zeitstrahlen können als ggfs. unterbrochene Linien aufgefasst werden. Dort wo der Zeitstrahl 'obermenge' unterbrochen ist, muss
	 * der Zeitstrahl 'untermenge' auch unterbrochen sein. Falls nicht wird false zurückgegeben. <br>
	 * <br>
	 * Gültige Beispiele für 'obermenge' enthält 'untermenge': <br>
	 * Zeitstrahl obermenge:  a) -------   b) --------   ----------   c) --------   ---------- <br>
	 * Zeitstrahl untermenge:    -------       -----                       -----    ---------- <br>
	 * scanPoints:               ^     ^      ^^   ^ .   .        .      ^ ^   ^^   ^        ^   // . werden nicht mehr geprüft, da Ergebnis fest steht <br>
	 * <br>
	 * Ungültige Beispiele für 'obermenge' enthält 'untermenge' <br>
	 * Zeitstrahl obermenge:  a)   -----   b) --------   ----------   c) --------   ---------- <br>
	 * Zeitstrahl untermenge:    -------       --------                   -----    ---------- <br>
	 * scanPoints:               ^ .   .      ^^     ^^  .        .      ^^   ^ ^  ^.       ..   // . werden nicht mehr geprüft, da Ergebnis fest steht <br>
	 * <br>
	 * Der Position des Scanpoints wird für das Verfahren nicht benötigt (Es ist immer der kleinere der beiden Punkte die mit iObermenge und iUntermenge
	 * referenziert werden.) <br>
	 *
	 * @param validatorName   der Name des Validators
	 * @param obermenge       die Liste der Zeiträume, die die Zeiträume der Untermenge beinhaltet
	 * @param untermenge      die Liste der Zeiträume, die überprüft wird, ob sie in der Liste der Obermenge beinhaltet ist.
	 *
	 * @return true, falls untermenge wirklich eine Untermenge von Obermenge ist und ansonsten false
	 */
	private static pruefeAufZeitraumueberdeckung(validatorName : string, obermenge : List<PairNN<number, number>>, untermenge : List<PairNN<number, number>>) : boolean {
		if (obermenge.isEmpty())
			return untermenge.isEmpty();
		const listObermenge : List<number> | null = ValidatorManager.getZeitraumListe(validatorName, obermenge);
		const listUntermenge : List<number> | null = ValidatorManager.getZeitraumListe(validatorName, untermenge);
		let iObermenge : number = 0;
		let iUntermenge : number = 0;
		do {
			if (iUntermenge >= listUntermenge.size())
				return true;
			if (iObermenge >= listObermenge.size())
				return false;
			if (listObermenge.get(iObermenge) === listUntermenge.get(iUntermenge)) {
				iObermenge++;
				iUntermenge++;
			} else {
				if (listObermenge.get(iObermenge) < listUntermenge.get(iUntermenge))
					iObermenge++;
				else
					iUntermenge++;
			}
		} while ((iObermenge % 2 === 1) || (iUntermenge % 2 === 0));
		return false;
	}

	/**
	 * Erstellt ein Liste mit den Jahreszahlen, welche immer eine gerade Anzahl von Einträgen hat. Ein Paar besteht aus Zeitraum-Werten von
	 * und bis. Das nächste Paar wird nur eingetragen, wenn eine Lücke vorhanden ist, so dass ggf. Zeiträume zusammengefasst werden.
	 * Die Zeiträume sind werden hier in der Form [von,bis[ erwartet: Schuljahr 'von' ist Teil des Zeitraums und Schuljahr 'bis' nicht,
	 * so dass kontinuierliche Intervalle entstehen. <br>
	 * <br>
	 * Beispiel: 2021-2022, 2023-null => 2021-(2022+1), 2023-null (in aufrufenden Klassen so umgesetzt)
	 *
	 * @param validatorName   der Name des Validators
	 * @param vbs              die Liste mit den Zeitraum-Paaren
	 *
	 * @return Liste mit den Jahreszahlen, welche die Paare von gültigen Zeiträumen für den Algorithmus aufbereitet enthält.
	 */
	private static getZeitraumListe(validatorName : string, vbs : List<PairNN<number, number>>) : List<number> {
		const list : List<number> | null = new ArrayList<number>();
		let i : number = 0;
		list.add(vbs.get(0).a);
		while (i + 1 < vbs.size()) {
			if (vbs.get(i).b > vbs.get(i + 1).a)
				throw new CoreTypeException(JavaString.format("Fehler beim prüfen der Zeiträume bei dem Validator '%s'. Die Zeiträume von %s sind überlappend definiert.", validatorName, vbs.get(0).getClass().getSimpleName()))
			if (vbs.get(i).b < vbs.get(i + 1).a) {
				list.add(vbs.get(i).b);
				list.add(vbs.get(i + 1).a);
			}
			i++;
		}
		list.add(vbs.get(i).b);
		return list;
	}

	/**
	 * Erzeugt ein PairNN, dass den Anfangszeitpunkt und den Endzeitpunkt enthält. Die Zeiträume sind werden
	 * hier in der Form [von,bis[ erwartet, Schuljahr 'von' ist Teil des Zeitraums Schuljahr 'bis' nicht,
	 * Die null-Werte aus gueltigVon und gueltigBis werden in 0 bzw. MAX_VALUE übersetzt, sowie der
	 * gueltigBis-Wert um 1 erhöht, damit kontinuierliche  Zeiträume entstehen können.
	 *
	 * @param von  Beginn des Zeitraums
	 * @param bis  Ende des Zeitraums
	 *
	 * @return Das Zeitraum-Paar mit übersetzten Null-Werten.
	 */
	private static createZeitraum(von : number | null, bis : number | null) : PairNN<number, number> {
		const v : number = (von === null ? JavaInteger.MIN_VALUE : von);
		const b : number = (bis === null ? JavaInteger.MAX_VALUE : bis + 1);
		return new PairNN<number, number>(v, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorManager'].includes(name);
	}

	public static class = new Class<ValidatorManager>('de.svws_nrw.asd.validate.ValidatorManager');

}

export function cast_de_svws_nrw_asd_validate_ValidatorManager(obj : unknown) : ValidatorManager {
	return obj as ValidatorManager;
}
