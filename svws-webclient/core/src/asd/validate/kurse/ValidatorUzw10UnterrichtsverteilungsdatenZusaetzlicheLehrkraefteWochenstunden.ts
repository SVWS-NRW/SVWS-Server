import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { JavaDouble } from '../../../java/lang/JavaDouble';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden extends Validator {

	private readonly wochenstundenLehrer: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden des Lehrer
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(wochenstundenLehrer: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
	}

	protected pruefe(): boolean {
		const wochenstunden: number | null = this.wochenstundenLehrer.get();
		if (JavaDouble.compare(wochenstunden, 0.0) < 0) {
			this.addFehler(0, "Wochenstunden der zusätzlichen Lehrkraft: Der eingetragene Wert muss mindestens '0' betragen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(obj: unknown): ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden {
	return obj as ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden;
}
