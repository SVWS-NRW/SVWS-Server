import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdatenVornameAnredeFehlerhaft extends Validator {

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
		const vorname : string | null = this.daten.vorname;
		let success : boolean = true;
		if ((vorname === null) || JavaString.isBlank(vorname))
			return false;
		const vLower : string | null = vorname.toLowerCase();
		const hatFrau : boolean = JavaString.contains(vLower, "frau ") && !JavaObject.equalsTranspiler(vLower, ("frauke"));
		const hatHerr : boolean = JavaString.contains(vLower, "herr ") && !JavaObject.equalsTranspiler(vLower, ("herr"));
		if (hatFrau || hatHerr) {
			this.addFehler("Bitte entfernen Sie die Anrede (Frau oder Herr) im Feld Vorname. (AA8)");
			success = false;
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenVornameAnredeFehlerhaft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenVornameAnredeFehlerhaft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenVornameAnredeFehlerhaft>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenVornameAnredeFehlerhaft');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenVornameAnredeFehlerhaft(obj : unknown) : ValidatorLehrerStammdatenVornameAnredeFehlerhaft {
	return obj as ValidatorLehrerStammdatenVornameAnredeFehlerhaft;
}
