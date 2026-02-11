import { ValidatorLsv14LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv14LehrerStammdatenVorname';
import { ValidatorLsv12LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv12LehrerStammdatenVorname';
import { ValidatorLsv15LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv15LehrerStammdatenVorname';
import { ValidatorLsv13LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv13LehrerStammdatenVorname';
import { ValidatorLsv11LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv11LehrerStammdatenVorname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorLsv16LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv16LehrerStammdatenVorname';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorLsv17LehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsv17LehrerStammdatenVorname';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv10LehrerStammdatenVorname extends Validator {

	/**
	 * Der Lehrer-Vorname
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Vorname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsv11LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv12LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv13LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv14LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv15LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv16LehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsv17LehrerStammdatenVorname(daten, kontext));
	}

	protected pruefe(): boolean {
		if (JavaString.isBlank(this.daten.get().trim())) {
			this.addFehler(1, "Vorname der Lehrkraft: Der Vorname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv10LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsv10LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv10LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv10LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv10LehrerStammdatenVorname(obj: unknown): ValidatorLsv10LehrerStammdatenVorname {
	return obj as ValidatorLsv10LehrerStammdatenVorname;
}
