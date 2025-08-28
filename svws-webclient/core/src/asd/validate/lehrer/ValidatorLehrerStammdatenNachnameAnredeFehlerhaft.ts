import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdatenNachnameAnredeFehlerhaft extends Validator {

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
		const nachname : string | null = this.daten.nachname;
		let success : boolean = true;
		if ((nachname === null) || JavaString.isBlank(nachname))
			return false;
		const nLower : string | null = nachname.toLowerCase();
		const hatFrau : boolean = JavaString.contains(nLower, "frau ");
		const hatHerr : boolean = JavaString.contains(nLower, "herr ") && !JavaObject.equalsTranspiler(nLower, ("herr"));
		if (hatFrau || hatHerr) {
			this.addFehler("Bitte entfernen Sie die Anrede (Frau oder Herr) im Feld Nachname. (AA8)");
			success = false;
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameAnredeFehlerhaft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameAnredeFehlerhaft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenNachnameAnredeFehlerhaft>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameAnredeFehlerhaft');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenNachnameAnredeFehlerhaft(obj : unknown) : ValidatorLehrerStammdatenNachnameAnredeFehlerhaft {
	return obj as ValidatorLehrerStammdatenNachnameAnredeFehlerhaft;
}
