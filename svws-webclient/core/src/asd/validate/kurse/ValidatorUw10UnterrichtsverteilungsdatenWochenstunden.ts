import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { JavaDouble } from '../../../java/lang/JavaDouble';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUw10UnterrichtsverteilungsdatenWochenstunden extends Validator {

	private readonly wochenstundenKurs: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenKurs     die Wochenstunden des Kurses
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(wochenstundenKurs: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.wochenstundenKurs = wochenstundenKurs;
	}

	protected pruefe(): boolean {
		const wochenstunden: number | null = this.wochenstundenKurs.get();
		if (JavaDouble.compare(wochenstunden, 0.0) < 0) {
			this.addFehler(0, "Wochenstunden des Kurses: Der eingetragene Wert muss mindestens '0' betragen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUw10UnterrichtsverteilungsdatenWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUw10UnterrichtsverteilungsdatenWochenstunden', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUw10UnterrichtsverteilungsdatenWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUw10UnterrichtsverteilungsdatenWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUw10UnterrichtsverteilungsdatenWochenstunden(obj: unknown): ValidatorUw10UnterrichtsverteilungsdatenWochenstunden {
	return obj as ValidatorUw10UnterrichtsverteilungsdatenWochenstunden;
}
