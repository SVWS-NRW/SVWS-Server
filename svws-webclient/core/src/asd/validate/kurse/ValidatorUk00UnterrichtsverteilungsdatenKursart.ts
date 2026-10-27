import { ValidatorUk01UnterrichtsverteilungsdatenKursart } from '../../../asd/validate/kurse/ValidatorUk01UnterrichtsverteilungsdatenKursart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUk00UnterrichtsverteilungsdatenKursart extends Validator {

	private readonly _kursartAllg: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Kursart.
	 *
	 * @param schuljahr     Schuljahr
	 * @param kursartAllg   die allgemeine Kursart
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, kursartAllg: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._kursartAllg = kursartAllg;
		this._validatoren.add(new ValidatorUk01UnterrichtsverteilungsdatenKursart(schuljahr, this.getNotNullSupplier(kursartAllg), kontext));
	}

	protected pruefe(): boolean {
		const kursartAllg: string | null = this._kursartAllg.get();
		if (kursartAllg === null || JavaString.isEmpty(kursartAllg)) {
			this.addFehler(0, "Kursart der Unterrichtsverteilungsdaten: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUk00UnterrichtsverteilungsdatenKursart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUk00UnterrichtsverteilungsdatenKursart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUk00UnterrichtsverteilungsdatenKursart>('de.svws_nrw.asd.validate.kurse.ValidatorUk00UnterrichtsverteilungsdatenKursart');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUk00UnterrichtsverteilungsdatenKursart(obj: unknown): ValidatorUk00UnterrichtsverteilungsdatenKursart {
	return obj as ValidatorUk00UnterrichtsverteilungsdatenKursart;
}
