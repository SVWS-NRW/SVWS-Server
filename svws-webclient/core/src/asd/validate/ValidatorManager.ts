import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { HashMap } from '../../java/util/HashMap';
import { Schulform } from '../../asd/types/schule/Schulform';
import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorFehlerartKontext } from '../../asd/validate/ValidatorFehlerartKontext';
import { JavaString } from '../../java/lang/JavaString';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { PairNN } from '../../asd/adt/PairNN';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { CoreTypeData } from '../../asd/data/CoreTypeData';
import type { JavaMap } from '../../java/util/JavaMap';
import { CoreTypeException } from '../../asd/data/CoreTypeException';

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
	 * Eine geschachtelte Map, die einem Schuljar eine Map mit der Zuordnung der Validatoren zu ihrer Fehlerart für die Schulform _schulform
	 */
	private readonly _mapSchuljahrValidatornameToFehlerart : HashMap<number, HashMap<string, ValidatorFehlerart>> = new HashMap<number, HashMap<string, ValidatorFehlerart>>();

	/**
	 * Eine geschachtelte Map, die einem Schuljahr eine Map mit der Zuordnung einer Fehlerart zu jedem Validator für die Schulform _schulform
	 */
	private readonly _mapSchuljahrFehlerartToValidatorname : HashMap<number, HashMap<ValidatorFehlerart, List<string>>> = new HashMap<number, HashMap<ValidatorFehlerart, List<string>>>();


	/**
	 * Erstellt einen neuen Manager für die übergebene Schulform und die Entsprechene Validierungsumgebung
	 * (Zebras oder SVWS)
	 *
	 * @param zebras            die Umgebung, in der gerade validiert wird: true: ZeBrAS  false: SVWS
	 * @param schulform			die Schulform, für die gerade
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
				const zeitraum : PairNN<number, number> = ValidatorManager.createZeitraum(eintrag.gueltigVon, eintrag.gueltigBis);
				ValidatorManager.addZeitraum(mapZeitraeumeBySchulform, zeitraum, eintrag.hart);
				ValidatorManager.addZeitraum(mapZeitraeumeBySchulform, zeitraum, eintrag.muss);
				ValidatorManager.addZeitraum(mapZeitraeumeBySchulform, zeitraum, eintrag.hinweis);
			}
			for (const zeitraeume of mapZeitraeumeBySchulform.entrySet()) {
				const l : List<CoreTypeData> = new ArrayList<CoreTypeData>();
				const sf : Schulform | null = Schulform.valueOf(zeitraeume.getKey());
				if (sf !== null)
					l.addAll(sf.historie());
				if (!ValidatorManager.pruefeAufZeitraumueberdeckung(validatorName, ValidatorManager.createSchulformZeitraumListe(l), zeitraeume.getValue()))
					throw new CoreTypeException(JavaString.format("Fehler beim prüfen der Schulform. Der Validator %s hat ungültige Schulform-Zeitraum-Kombinationen.", validatorName))
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
	private getValidatornameToFehlerartCache(schuljahr : number) : HashMap<string, ValidatorFehlerart> {
		const mapValidatorToFehlerart : HashMap<string, ValidatorFehlerart> = this.computeIfAbsentValidatornameToFehlerart(schuljahr);
		if (mapValidatorToFehlerart.isEmpty())
			this.createCache(schuljahr);
		return mapValidatorToFehlerart;
	}

	/**
	 * Liefert für das angegebene Schuljahr die Map die einer Fehlerart die Liste der Namen der zugehörigen Validatoren liefert.
	 * Ist der Cache für das Schuljahr noch nicht aufgebaut, so wird dieser erstellt.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Map, die für das gegebene Schuljahr pro Fehlerart die Liste der Validatornamen enthält
	 */
	private getFehlerartToValidatornameCache(schuljahr : number) : HashMap<ValidatorFehlerart, List<string>> {
		const mapFehlerartToValidatorname : HashMap<ValidatorFehlerart, List<string>> = this.computeIfAbsentFehlerartToValidatorname(schuljahr);
		if (mapFehlerartToValidatorname.isEmpty())
			this.createCache(schuljahr);
		return mapFehlerartToValidatorname;
	}

	/**
	 * holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param schuljahr - das Schuljahr, für das das Objekt geholt wird
	 * @return das benötigte Objekt
	 */
	private computeIfAbsentValidatornameToFehlerart(schuljahr : number) : HashMap<string, ValidatorFehlerart> {
		let mapValidatorToFehlerart : HashMap<string, ValidatorFehlerart> | null = this._mapSchuljahrValidatornameToFehlerart.get(schuljahr);
		if (mapValidatorToFehlerart === null) {
			mapValidatorToFehlerart = new HashMap();
			this._mapSchuljahrValidatornameToFehlerart.put(schuljahr, mapValidatorToFehlerart);
		}
		return mapValidatorToFehlerart;
	}

	/**
	 * holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param schuljahr - das Schuljahr, für das das Objekt geholt wird
	 * @return das benötigte Objekt
	 */
	private computeIfAbsentFehlerartToValidatorname(schuljahr : number) : HashMap<ValidatorFehlerart, List<string>> {
		let mapFehlerartToValidatorname : HashMap<ValidatorFehlerart, List<string>> | null = this._mapSchuljahrFehlerartToValidatorname.get(schuljahr);
		if (mapFehlerartToValidatorname === null) {
			mapFehlerartToValidatorname = new HashMap();
			this._mapSchuljahrFehlerartToValidatorname.put(schuljahr, mapFehlerartToValidatorname);
		}
		return mapFehlerartToValidatorname;
	}

	/**
	 * holt das Objekt aus der HashMap oder erzeugt es wenn es nicht vorhanden ist.
	 *
	 * @param art - die Fehlerart, für die die Liste ggfs. erzeugt wird
	 * @param map - die HashMap mit den ArrayLists
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
		const mapValidatorToFehlerart : JavaMap<string, ValidatorFehlerart> = this.computeIfAbsentValidatornameToFehlerart(schuljahr);
		mapValidatorToFehlerart.clear();
		const mapFehlerartToValidator : JavaMap<ValidatorFehlerart, List<string>> = this.computeIfAbsentFehlerartToValidatorname(schuljahr);
		mapFehlerartToValidator.clear();
		const hart : List<string> = ValidatorManager.computeIfAbsentFehlerartValidator(ValidatorFehlerart.HART, mapFehlerartToValidator);
		hart.clear();
		const muss : List<string> = ValidatorManager.computeIfAbsentFehlerartValidator(ValidatorFehlerart.MUSS, mapFehlerartToValidator);
		muss.clear();
		const hinweis : List<string> = ValidatorManager.computeIfAbsentFehlerartValidator(ValidatorFehlerart.HINWEIS, mapFehlerartToValidator);
		hinweis.clear();
		const ungenutzt : List<string> = ValidatorManager.computeIfAbsentFehlerartValidator(ValidatorFehlerart.UNGENUTZT, mapFehlerartToValidator);
		ungenutzt.clear();
		for (const entry of ValidatorManager._data.entrySet()) {
			const validatorName : string = entry.getKey();
			const list : List<ValidatorFehlerartKontext> = entry.getValue();
			for (const eintrag of list) {
				const hasHart : boolean = eintrag.hart.contains(this._schulform.name());
				const hasMuss : boolean = eintrag.muss.contains(this._schulform.name());
				const hasHinweis : boolean = eintrag.hinweis.contains(this._schulform.name());
				if ((hasHart && hasMuss) || (hasMuss && hasHinweis) || (hasHart && hasHinweis))
					throw new CoreTypeException("Ein Validator kann bei einer Schulform nicht gleichzeitig bei mehreren Fehlerarten aktiv sein.")
				const validatorAktivInUmgebungUndSchuljahr : boolean = (this._isZebras ? eintrag.zebras : eintrag.svws) && ((eintrag.gueltigVon === null) || (eintrag.gueltigVon <= schuljahr)) && ((eintrag.gueltigBis === null) || (schuljahr <= eintrag.gueltigBis));
				if (validatorAktivInUmgebungUndSchuljahr && hasHart) {
					mapValidatorToFehlerart.put(validatorName, ValidatorFehlerart.HART);
					hart.add(validatorName);
				} else
					if (validatorAktivInUmgebungUndSchuljahr && hasMuss) {
						mapValidatorToFehlerart.put(validatorName, ValidatorFehlerart.MUSS);
						muss.add(validatorName);
					} else
						if (validatorAktivInUmgebungUndSchuljahr && hasHinweis) {
							mapValidatorToFehlerart.put(validatorName, ValidatorFehlerart.HINWEIS);
							hinweis.add(validatorName);
						} else {
							mapValidatorToFehlerart.put(validatorName, ValidatorFehlerart.UNGENUTZT);
							ungenutzt.add(validatorName);
						}
			}
		}
	}

	/**
	 * Gibt die Fehlerart eines Validators für das angegebene Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param validator   der kanonische Name des Validators
	 *
	 * @return die Fehlerart des Validators für das angegebene Schuljahr
	 */
	public getFehlerartBySchuljahr(schuljahr : number, validator : string) : ValidatorFehlerart | null {
		return this.getValidatornameToFehlerartCache(schuljahr).get(validator);
	}

	/**
	 * Setzt die Fehlerart eines Validators für das angegebene Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param validator   der kanonische Name des Validators
	 * @param fehlerart   die Fehlerart des Validators
	 */
	public setFehlerartBySchuljahr(schuljahr : number, validator : string, fehlerart : ValidatorFehlerart) : void {
		const mapFehlerartToValidator : HashMap<ValidatorFehlerart, List<string>> = this.getFehlerartToValidatornameCache(schuljahr);
		const art : ValidatorFehlerart | null = this.getFehlerartBySchuljahr(schuljahr, validator);
		if (art !== null) {
			const list : List<string> | null = mapFehlerartToValidator.get(art);
			if (list !== null)
				list.remove(validator);
		}
		ValidatorManager.computeIfAbsentFehlerartValidator(fehlerart, mapFehlerartToValidator).add(validator);
		const map : HashMap<string, ValidatorFehlerart> = this.getValidatornameToFehlerartCache(schuljahr);
		map.remove(validator);
		map.put(validator, fehlerart);
	}

	/**
	 * Prüft, ob der übergebene Validator in dem angegebenen Schuljar aktiv ist oder nicht.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param validator   der kanonische Name des Validators
	 *
	 * @return true, falls der Validator in dem Schuljahr aktiv ist.
	 */
	public isValidatorActiveInSchuljahr(schuljahr : number, validator : string) : boolean {
		return (this.getValidatornameToFehlerartCache(schuljahr).get(validator) as unknown !== ValidatorFehlerart.UNGENUTZT as unknown);
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
