import { ValidatorLsv01LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv01LehrerStammdatenVorname';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv00LehrerStammdatenVorname extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: LehrerStammdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerStammdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsv01LehrerStammdatenVorname(daten, kontext));
	}

	protected pruefe(): boolean {
		const vorname: string | null = this.daten.vorname;
		if ((vorname === null) || (vorname.length === 0)) {
			this.addFehler(0, "Vorname der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv00LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv00LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLsv00LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv00LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv00LehrerStammdatenVorname(obj: unknown): ValidatorLsv00LehrerStammdatenVorname {
	return obj as ValidatorLsv00LehrerStammdatenVorname;
}
