import { JavaInteger } from '../../../java/lang/JavaInteger';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden extends Validator {

	private readonly wochenstundenLehrer: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden der Lehrkraft
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(wochenstundenLehrer: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
	}

	protected pruefe(): boolean {
		const wochenstunden: number | null = this.wochenstundenLehrer.get();
		if (JavaInteger.compare(wochenstunden, 0) < 0) {
			this.addFehler(0, "Wochenstunden der Lehrkraft: Es sind nur Werte >= 0 erlaubt");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden(obj: unknown): ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden {
	return obj as ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden;
}
