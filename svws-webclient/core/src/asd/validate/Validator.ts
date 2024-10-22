import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorFehler } from '../../asd/validate/ValidatorFehler';
import type { List } from '../../java/util/List';
import { Class, cast_java_lang_Class } from '../../java/lang/Class';
import { JavaObject } from '../../java/lang/JavaObject';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';

export abstract class Validator<T extends JavaObject> extends JavaObject {

	/**
	 * Die zu validierenden Daten
	 */
	private readonly _daten : T;

	/**
	 * Der vom Validator genutzte Kontext
	 */
	private readonly _kontext : ValidatorKontext;

	/**
	 * Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden.
	 */
	protected readonly _validatoren : List<Validator<any>> = new ArrayList<Validator<any>>();

	/**
	 * Eine Liste mit Fehlern bei der Validierung
	 */
	private readonly _fehler : List<ValidatorFehler<any>> = new ArrayList<ValidatorFehler<any>>();

	/**
	 * Die DTO-Klasse, welche von dem Validator geprüft wurde
	 */
	private readonly _dtoClass : Class<T>;


	/**
	 * Erstellt einen neuen Validator in dem übegebenen Kontext
	 *
	 * @param daten     die zu validierenden Daten
	 * @param kontext   der Kontext, in dem der Validator ausgeführt wird
	 */
	protected constructor(daten : T, kontext : ValidatorKontext) {
		super();
		this._daten = daten;
		this._kontext = kontext;
		this._dtoClass = cast_java_lang_Class(daten.getClass());
	}

	/**
	 * Gibt den Kontext des Validators zurück.
	 *
	 * @return der Kontext des Validators
	 */
	public kontext() : ValidatorKontext {
		return this._kontext;
	}

	/**
	 * Gibt die zu validierenden Daten zurück.
	 *
	 * @return die zu validierenden Daten
	 */
	public daten() : T {
		return this._daten;
	}

	/**
	 * Gibt die Klasse der zu validierenden Daten zurück.
	 *
	 * @return die Klasse der zu validierenden Daten
	 */
	public getDTOClass() : Class<T> {
		return this._dtoClass;
	}

	/**
	 * Führt die Prüfungen des Validators aus. Dabei wird zunächst die Fehlerliste
	 * geleert und durch die ausführenden Prüfroutinen befüllt.
	 *
	 * @return true, falls alle Prüfroutinen erfolgreich waren, und ansonsten false
	 */
	public run() : boolean {
		let success : boolean = true;
		this._fehler.clear();
		if (this._kontext.getValidatorManager().isValidatorActiveInSchuljahr(this._kontext.getSchuljahr(), this.getClass().getCanonicalName())) {
			for (const validator of this._validatoren) {
				if (!validator.run())
					success = false;
				this._fehler.addAll(validator._fehler);
			}
			try {
				if (!this.pruefe())
					success = false;
			} catch(e : any) {
				this.addFehler("Unerwarteter Fehler bei der Validierung: " + e.getMessage());
			}
		}
		return success;
	}

	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected addFehler(fehlermeldung : string) : void {
		this._fehler.add(new ValidatorFehler<any>(this, fehlermeldung));
	}

	/**
	 * Gibt die Fehler des Validators als unmodifiable List zurück.
	 *
	 * @return die Liste der Fehler als unmodifiable List
	 */
	public getFehler() : List<ValidatorFehler<any>> {
		return new ArrayList<ValidatorFehler<any>>(this._fehler);
	}

	/**
	 * Führt die Prüfung der Daten aus. Befüllt ggf. die Fehlerliste, falls
	 * es zu Fehlern kommt.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	protected abstract pruefe() : boolean;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.Validator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<Validator<any>>('de.svws_nrw.asd.validate.Validator');

}

export function cast_de_svws_nrw_asd_validate_Validator<T extends JavaObject>(obj : unknown) : Validator<T> {
	return obj as Validator<T>;
}
