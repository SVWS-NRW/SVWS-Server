import { JavaObject } from '../../java/lang/JavaObject';
import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { ValidatorFehler } from '../../asd/validate/ValidatorFehler';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';

export abstract class BasicValidator extends JavaObject {

	/**
	 * Eine Liste mit Fehlern bei der Validierung
	 */
	protected readonly _fehler: List<ValidatorFehler>;

	/**
	 * Die stärkste Fehlerart die bei einem Lauf des Validators vorgekommen ist.
	 */
	protected _fehlerart: ValidatorFehlerart = ValidatorFehlerart.UNGENUTZT;

	/**
	 * Die Default-Fehlerart für diesen Validator
	 */
	protected _defaultValidatorFehlerart: ValidatorFehlerart;


	/**
	 * Erstellt einen einfach Validator mit einer leeren Fehlerliste
	 *
	 * @param defaultValidatorFehlerart   die Default-Fehlerart für diesen Validator
	 */
	protected constructor(defaultValidatorFehlerart: ValidatorFehlerart) {
		super();
		this._fehler = new ArrayList();
		this._defaultValidatorFehlerart = defaultValidatorFehlerart;
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
		this._fehlerart = ValidatorFehlerart.UNGENUTZT;
		try {
			if (!this.pruefe())
				success = false;
		} catch(e : any) {
			this.addFehler(-1, "Unerwarteter Fehler bei der Validierung: " + e.getMessage());
			success = false;
		}
		return success;
	}

	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param pruefschritt    die Nummer des Prüfschrittes, bei welchem der Fehler aufgetreten ist
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected addFehler(pruefschritt: number, fehlermeldung: string): void {
		this._fehler.add(new ValidatorFehler(this, pruefschritt, fehlermeldung));
		this.updateFehlerart(this.getValidatorFehlerart());
	}

	/**
	 * Aktualisiert die Fehlerart, die durch den Lauf dieses Validators erzeugt wurde
	 * anhand der übergebenen Fehlerart. Wird null übergeben, so wird die Fehlerart genutzt, die
	 * diesem Validator zugeordnet ist.
	 *
	 * @param art   die Fehlerart, die für die Überprüfung genutzt wird, oder null
	 */
	protected updateFehlerart(art: ValidatorFehlerart): void {
		if (this._fehlerart.ordinal() > art.ordinal())
			this._fehlerart = art;
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
		return this._defaultValidatorFehlerart;
	}

	/**
	 * Gibt das Fehlercode-Präfix zurück, welcher diesem speziellen Validator zugeordnet ist.
	 *
	 * @return das Fehlercode-Präfix
	 */
	public getFehlercodePraefix(): string {
		return "";
	}

	/**
	 * Die Fehlerart, welche dem Validator nach dem Lauf der Validierung zugeordnet ist.
	 * Dabei sind die Ergebnisse von ggf. vorhandene Sub-Validatoren mit einbezogen.
	 * Es wird also die schwerwiegendste Fehlerart zurückgegeben.
	 *
	 * @return die Fehlerart
	 */
	public getFehlerart(): ValidatorFehlerart {
		return this._fehlerart;
	}

	/**
	 * Führt die Prüfung der Daten aus. Befüllt ggf. die Fehlerliste, falls
	 * es zu Fehlern kommt.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	protected abstract pruefe(): boolean;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.BasicValidator';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator'].includes(name);
	}

	public static readonly class = new Class<BasicValidator>('de.svws_nrw.asd.validate.BasicValidator');

}

export function cast_de_svws_nrw_asd_validate_BasicValidator(obj: unknown): BasicValidator {
	return obj as BasicValidator;
}
