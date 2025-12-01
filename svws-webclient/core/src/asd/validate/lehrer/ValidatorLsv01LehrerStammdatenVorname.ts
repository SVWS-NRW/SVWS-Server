import { ValidatorLsv05LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv05LehrerStammdatenVorname';
import { ValidatorLsv08LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv08LehrerStammdatenVorname';
import { ValidatorLsv04LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv04LehrerStammdatenVorname';
import { ValidatorLsv02LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv02LehrerStammdatenVorname';
import { ValidatorLsv03LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv03LehrerStammdatenVorname';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { ValidatorLsv06LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv06LehrerStammdatenVorname';
import { ValidatorLsv07LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv07LehrerStammdatenVorname';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv01LehrerStammdatenVorname extends Validator {

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
		this._validatoren.add(new ValidatorLsv02LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv03LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv04LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv05LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv06LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv07LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv08LehrerStammdatenVorname(daten, kontext));
	}

	protected pruefe(): boolean {
		if (JavaString.isBlank(this.daten.vorname.trim())) {
			this.addFehler(1, "Vorname der Lehrkraft: Der Vorname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv01LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv01LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv01LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv01LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv01LehrerStammdatenVorname(obj: unknown): ValidatorLsv01LehrerStammdatenVorname {
	return obj as ValidatorLsv01LehrerStammdatenVorname;
}
