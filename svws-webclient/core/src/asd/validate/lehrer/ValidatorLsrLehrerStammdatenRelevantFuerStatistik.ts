import { ValidatorLsr10LehrerStammdatenRelevantFuerStatistik } from '../../../asd/validate/lehrer/ValidatorLsr10LehrerStammdatenRelevantFuerStatistik';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsrLehrerStammdatenRelevantFuerStatistik extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param istRelevantFuerStatistik     ist relevant für Statistik
	 * @param abgangsdatum                 das Abgangsdatum des Lehrers
	 * @param kontext                      der Kontext des Validators
	 */
	public constructor(istRelevantFuerStatistik: Supplier<boolean | null>, abgangsdatum: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLsr10LehrerStammdatenRelevantFuerStatistik(istRelevantFuerStatistik, abgangsdatum, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsrLehrerStammdatenRelevantFuerStatistik';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsrLehrerStammdatenRelevantFuerStatistik', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsrLehrerStammdatenRelevantFuerStatistik>('de.svws_nrw.asd.validate.lehrer.ValidatorLsrLehrerStammdatenRelevantFuerStatistik');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsrLehrerStammdatenRelevantFuerStatistik(obj: unknown): ValidatorLsrLehrerStammdatenRelevantFuerStatistik {
	return obj as ValidatorLsrLehrerStammdatenRelevantFuerStatistik;
}
