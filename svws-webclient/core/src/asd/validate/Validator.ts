import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { ValidatorManager } from '../../asd/validate/ValidatorManager';
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
	 * Die stärkste Fehlerart die bei einem Lauf des Validators vorgekommen ist.
	 */
	private _fehlerart : ValidatorFehlerart = ValidatorFehlerart.UNGENUTZT;

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
	 * Gibt den zugehörigen ValidatorManager zurück.
	 *
	 * @return der ValidatorManager
	 */
	public getValidatorManager() : ValidatorManager {
		return this._kontext.getValidatorManager();
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
				this.updateFehlerart(validator.getFehlerart());
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
	 * Aktualisiert die Fehlerart, die durch den Lauf dieses Validators erzeugt wurde
	 * anhand der übergebenen Fehlerart. Wird null übergeben, so wird die Fehlerart genutzt, die
	 * diesem Validator zugeordnet ist.
	 *
	 * @param art   die Fehlerart, die für die Überprüfung genutzt wird, oder null
	 */
	private updateFehlerart(art : ValidatorFehlerart | null) : void {
		const tmp : ValidatorFehlerart = (art !== null) ? art : this.getValidatorFehlerart();
		if (this._fehlerart.ordinal() > tmp.ordinal())
			this._fehlerart = tmp;
	}

	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected addFehler(fehlermeldung : string) : void {
		this._fehler.add(new ValidatorFehler<any>(this, fehlermeldung));
		this.updateFehlerart(null);
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
	 * Die Fehlerart, welche diesem speziellen Validator zugeordnet ist.
	 *
	 * @return die Fehlerart
	 */
	public getValidatorFehlerart() : ValidatorFehlerart {
		return this._kontext.getValidatorManager().getFehlerartBySchuljahrAndValidatorClass(this._kontext.getSchuljahr(), this.getClass());
	}

	/**
	 * Die Fehlerart, welche dem Validator nach dem Lauf der Validierung zugeordnet ist.
	 * Dabei sind die Ergebnisse von ggf. vorhandene Sub-Validatoren mit einbezogen.
	 * Es wird also die schwerwiegendste Fehlerart zurückgegeben.
	 *
	 * @return die Fehlerart
	 */
	public getFehlerart() : ValidatorFehlerart {
		return this._fehlerart;
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
