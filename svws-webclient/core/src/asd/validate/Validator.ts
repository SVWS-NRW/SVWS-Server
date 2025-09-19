import { JavaObject } from '../../java/lang/JavaObject';
import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { ValidatorManager } from '../../asd/validate/ValidatorManager';
import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorFehler } from '../../asd/validate/ValidatorFehler';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';

export abstract class Validator extends JavaObject {

	/**
	 * Der vom Validator genutzte Kontext
	 */
	private readonly _kontext : ValidatorKontext;

	/**
	 * Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden.
	 */
	protected readonly _validatoren : List<Validator> = new ArrayList<Validator>();

	/**
	 * Eine Liste mit Fehlern bei der Validierung
	 */
	private readonly _fehler : List<ValidatorFehler> = new ArrayList<ValidatorFehler>();

	/**
	 * Die stärkste Fehlerart die bei einem Lauf des Validators vorgekommen ist.
	 */
	private _fehlerart : ValidatorFehlerart = ValidatorFehlerart.UNGENUTZT;


	/**
	 * Erstellt einen neuen Validator in dem übegebenen Kontext
	 *
	 * @param kontext   der Kontext, in dem der Validator ausgeführt wird
	 */
	protected constructor(kontext : ValidatorKontext) {
		super();
		this._kontext = kontext;
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
				this.addFehler(-1, "Unerwarteter Fehler bei der Validierung: " + e.getMessage());
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
	private updateFehlerart(art : ValidatorFehlerart) : void {
		if (this._fehlerart.ordinal() > art.ordinal())
			this._fehlerart = art;
	}

	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param pruefschritt    die Nummer des Prüfschrittes, bei welchem der Fehler aufgetreten ist
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected addFehler(pruefschritt : number, fehlermeldung : string) : void {
		this._fehler.add(new ValidatorFehler(this, pruefschritt, fehlermeldung));
		this.updateFehlerart(this.getValidatorFehlerart(pruefschritt));
	}

	/**
	 * Gibt die Fehler des Validators als unmodifiable List zurück.
	 *
	 * @return die Liste der Fehler als unmodifiable List
	 */
	public getFehler() : List<ValidatorFehler> {
		return new ArrayList<ValidatorFehler>(this._fehler);
	}

	/**
	 * Die Fehlerart, welche diesem speziellen Validator zugeordnet ist.
	 *
	 * @param pruefschritt   der Prüfschritt, bei welchem der Fehler aufgetreten ist
	 *
	 * @return die Fehlerart
	 */
	public getValidatorFehlerart(pruefschritt : number) : ValidatorFehlerart {
		return this._kontext.getValidatorManager().getFehlerartBySchuljahrAndValidatorClassAndPruefschritt(this._kontext.getSchuljahr(), this.getClass(), pruefschritt);
	}

	/**
	 * Gibt das Fehlercode-Präfix zurück, welcher diesem speziellen Validator zugeordnet ist.
	 *
	 * @return das Fehlercode-Präfix
	 */
	public getFehlercodePraefix() : string {
		return this._kontext.getValidatorManager().getFehlercodePraefixBySchuljahrAndValidatorClass(this._kontext.getSchuljahr(), this.getClass());
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

	public static class = new Class<Validator>('de.svws_nrw.asd.validate.Validator');

}

export function cast_de_svws_nrw_asd_validate_Validator(obj : unknown) : Validator {
	return obj as Validator;
}
