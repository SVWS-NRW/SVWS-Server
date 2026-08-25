import { ValidatorUw10UnterrichtsverteilungsdatenWochenstunden } from '../../../asd/validate/kurse/ValidatorUw10UnterrichtsverteilungsdatenWochenstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUw00UnterrichtsverteilungsdatenWochenstunden extends Validator {

	private readonly wochenstundenKurs: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenKurs     die Wochenstunden des Kurses
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(wochenstundenKurs: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.wochenstundenKurs = wochenstundenKurs;
		this._validatoren.add(new ValidatorUw10UnterrichtsverteilungsdatenWochenstunden(this.getNotNullSupplierDouble(wochenstundenKurs), kontext));
	}

	protected pruefe(): boolean {
		const wochenstunden: number | null = this.wochenstundenKurs.get();
		if ((wochenstunden === null)) {
			this.addFehler(0, "Wochenstunden des Kurses: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUw00UnterrichtsverteilungsdatenWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUw00UnterrichtsverteilungsdatenWochenstunden', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUw00UnterrichtsverteilungsdatenWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUw00UnterrichtsverteilungsdatenWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUw00UnterrichtsverteilungsdatenWochenstunden(obj: unknown): ValidatorUw00UnterrichtsverteilungsdatenWochenstunden {
	return obj as ValidatorUw00UnterrichtsverteilungsdatenWochenstunden;
}
