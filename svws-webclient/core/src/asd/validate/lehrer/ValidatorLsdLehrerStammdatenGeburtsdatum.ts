import { ValidatorLsd01LehrerStammdatenGeburtsdatum } from '../../../asd/validate/lehrer/ValidatorLsd01LehrerStammdatenGeburtsdatum';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsdLehrerStammdatenGeburtsdatum extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLsd01LehrerStammdatenGeburtsdatum(daten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsdLehrerStammdatenGeburtsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsdLehrerStammdatenGeburtsdatum', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsdLehrerStammdatenGeburtsdatum>('de.svws_nrw.asd.validate.lehrer.ValidatorLsdLehrerStammdatenGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsdLehrerStammdatenGeburtsdatum(obj: unknown): ValidatorLsdLehrerStammdatenGeburtsdatum {
	return obj as ValidatorLsdLehrerStammdatenGeburtsdatum;
}
