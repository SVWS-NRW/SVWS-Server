import { ValidatorManager } from '../../asd/validate/ValidatorManager';
import { BasicValidator } from '../../asd/validate/BasicValidator';
import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorFehler } from '../../asd/validate/ValidatorFehler';
import { Exception } from '../../java/lang/Exception';
import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { DateManager } from '../../asd/validate/DateManager';
import { NullPointerException } from '../../java/lang/NullPointerException';
import type { List } from '../../java/util/List';
import type { Supplier } from '../../java/util/function/Supplier';
import { Class } from '../../java/lang/Class';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';

export abstract class Validator extends BasicValidator {

	/**
	 * Der vom Validator genutzte Kontext
	 */
	private readonly _kontext: ValidatorKontext;

	/**
	 * Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden.
	 */
	protected readonly _validatoren: List<Validator> = new ArrayList<Validator>();


	/**
	 * Erstellt einen neuen Validator in dem übegebenen Kontext
	 *
	 * @param kontext   der Kontext, in dem der Validator ausgeführt wird
	 */
	protected constructor(kontext: ValidatorKontext) {
		super(ValidatorFehlerart.UNGENUTZT);
		this._kontext = kontext;
		this._defaultValidatorFehlerart = this.getValidatorFehlerart();
	}

	/**
	 * Wandelt einen Supplier für Strings in einen Supplier für Strings zurück, welcher keine null-Werte liefert,
	 * sondern nur leere Strings.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Strings liefern kann
	 * @param <T>        der Datentyp, der vom Supplier geliefert wird
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	protected getNotNullObjectSupplier<T>(supplier: Supplier<T | null>): Supplier<T> {
		return { get: () => {
			const value: T | null = supplier.get();
			if (value === null)
				throw new NullPointerException()
			return value;
		} };
	}

	/**
	 * Wandelt einen Supplier für Strings in einen Supplier für Strings zurück, welcher keine null-Werte liefert,
	 * sondern nur leere Strings.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Strings liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	protected getNotNullSupplier(supplier: Supplier<string | null>): Supplier<string> {
		return { get: () => {
			const value: string | null = supplier.get();
			return (value === null) ? "" : value;
		} };
	}

	/**
	 * Wandelt einen Supplier für Strings in einen Supplier für Strings zurück, welcher keine null-Werte liefert,
	 * sondern nur leere Strings.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Strings liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	protected getDateManagerSupplier(supplier: Supplier<string | null>): Supplier<DateManager | null> {
		return { get: () => {
			const value: string | null = supplier.get();
			if (value === null)
				return null;
			try {
				return DateManager.from(value);
			} catch(e : any) {
				return null;
			}
		} };
	}

	/**
	 * Gibt den Kontext des Validators zurück.
	 *
	 * @return der Kontext des Validators
	 */
	public kontext(): ValidatorKontext {
		return this._kontext;
	}

	/**
	 * Gibt den zugehörigen ValidatorManager zurück.
	 *
	 * @return der ValidatorManager
	 */
	public getValidatorManager(): ValidatorManager {
		return this._kontext.getValidatorManager();
	}

	/**
	 * Führt die Prüfungen des Validators aus. Dabei wird zunächst die Fehlerliste
	 * geleert und durch die ausführenden Prüfroutinen befüllt.
	 *
	 * @return true, falls alle Prüfroutinen erfolgreich waren, und ansonsten false
	 */
	public run(): boolean {
		let success: boolean = true;
		this._fehler.clear();
		if (this._kontext.getValidatorManager().isValidatorActiveInSchuljahr(this._kontext.getSchuljahr(), this.getClass().getCanonicalName())) {
			try {
				if (!this.pruefe())
					return false;
			} catch(e : any) {
				this.addFehler(-1, "Unerwarteter Fehler bei der Validierung: " + e.getMessage());
				return false;
			}
			for (const validator of this._validatoren) {
				if (!validator.run())
					success = false;
				this._fehler.addAll(validator._fehler);
				this.updateFehlerart(validator.getFehlerart());
			}
			try {
				if (!this.pruefeAbschluss())
					success = false;
			} catch(e : any) {
				this.addFehler(-1, "Unerwarteter Fehler bei der Validierung: " + e.getMessage());
			}
		}
		return success;
	}

	/**
	 * Führt ggf. eine Prüfung der Daten nach der Überprüfung der Subvalidatoren als Abschluss
	 * der Prüfung aus. Dabei wird die Fehlerliste, falls es zu Fehlern kommt.
	 * Diese Methode ist bei Bedarf in dem konkreten Fall zu überschreiben.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	protected pruefeAbschluss(): boolean {
		return true;
	}

	/**
	 * Gibt die Fehler des Validators als unmodifiable List zurück.
	 *
	 * @return die Liste der Fehler als unmodifiable List
	 */
	public getFehler(): List<ValidatorFehler> {
		return new ArrayList<ValidatorFehler>(this._fehler);
	}

	/**
	 * Die Fehlerart, welche diesem speziellen Validator zugeordnet ist.
	 *
	 * @return die Fehlerart
	 */
	public getValidatorFehlerart(): ValidatorFehlerart {
		return this._kontext.getValidatorManager().getFehlerartBySchuljahrAndValidatorClass(this._kontext.getSchuljahr(), this.getClass());
	}

	/**
	 * Gibt das Fehlercode-Präfix zurück, welcher diesem speziellen Validator zugeordnet ist.
	 *
	 * @return das Fehlercode-Präfix
	 */
	public getFehlercodePraefix(): string {
		return this._kontext.getValidatorManager().getFehlercodePraefixBySchuljahrAndValidatorClass(this._kontext.getSchuljahr(), this.getClass());
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.Validator';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<Validator>('de.svws_nrw.asd.validate.Validator');

}

export function cast_de_svws_nrw_asd_validate_Validator(obj: unknown): Validator {
	return obj as Validator;
}
