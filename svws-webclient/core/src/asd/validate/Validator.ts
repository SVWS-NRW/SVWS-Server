import { ValidatorManager } from '../../asd/validate/ValidatorManager';
import { BasicValidator } from '../../asd/validate/BasicValidator';
import { ValidatorFehler } from '../../asd/validate/ValidatorFehler';
import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { DateManager } from '../../asd/validate/DateManager';
import { NullPointerException } from '../../java/lang/NullPointerException';
import type { Supplier } from '../../java/util/function/Supplier';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';

export abstract class Validator extends BasicValidator {

	/**
	 * Der vom Validator genutzte Kontext
	 */
	private readonly _kontext: ValidatorKontext;


	/**
	 * Erstellt einen neuen Validator in dem übergebenen Kontext
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
			if (value === null) {
				throw new NullPointerException()
			}
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
	 * Wandelt einen Supplier für Integer in einen Supplier für Integer zurück, welcher keine null-Werte liefert,
	 * sondern -1 falls der Integer-Wert null ist.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Integer liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	protected getNotNullSupplierInteger(supplier: Supplier<number | null>): Supplier<number> {
		return { get: () => {
			const value: number | null = supplier.get();
			return (value === null) ? -1 : value;
		} };
	}

	/**
	 * Wandelt einen Supplier für Long in einen Supplier für Long zurück, welcher keine null-Werte liefert,
	 * sondern -1 falls der Long-Wert null ist.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Long liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	protected getNotNullSupplierLong(supplier: Supplier<number | null>): Supplier<number> {
		return { get: () => {
			const value: number | null = supplier.get();
			return (value === null) ? -1 : value;
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
			if (value === null) {
				return null;
			}
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
	 * Prüft, ob der Validator aktiv ist.
	 *
	 * @return true, falls der Validator aktiv ist
	 */
	protected isActive(): boolean {
		return this._kontext.getValidatorManager().isValidatorActiveInSchuljahr(this._kontext.getSchuljahr(), this.getClass().getCanonicalName());
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
