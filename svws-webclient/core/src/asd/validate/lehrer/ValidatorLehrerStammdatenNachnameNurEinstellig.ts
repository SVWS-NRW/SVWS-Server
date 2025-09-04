import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdatenNachnameNurEinstellig extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten : LehrerStammdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe() : boolean {
		if ((this.daten.nachname === null) || (this.daten.nachname.length === 0) || JavaString.isBlank(this.daten.nachname)) {
			return true;
		}
		let success : boolean = true;
		if (this.daten.nachname.length === 1) {
			this.addFehler("Der Nachname besteht aus nur einem Zeichen. Bitte überprüfen sie ihre Angaben.");
			success = false;
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameNurEinstellig';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameNurEinstellig', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenNachnameNurEinstellig>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameNurEinstellig');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenNachnameNurEinstellig(obj : unknown) : ValidatorLehrerStammdatenNachnameNurEinstellig {
	return obj as ValidatorLehrerStammdatenNachnameNurEinstellig;
}
