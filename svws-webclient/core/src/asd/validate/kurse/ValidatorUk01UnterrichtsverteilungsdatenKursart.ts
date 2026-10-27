import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ZulaessigeKursart } from '../../../asd/types/kurse/ZulaessigeKursart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorUk02UnterrichtsverteilungsdatenKursart } from '../../../asd/validate/kurse/ValidatorUk02UnterrichtsverteilungsdatenKursart';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUk01UnterrichtsverteilungsdatenKursart extends Validator {

	private readonly _kursartAllg: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param schuljahr     Schuljahr
	 * @param kursartAllg   die allgemeine Kursart
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, kursartAllg: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this._kursartAllg = kursartAllg;
		this._validatoren.add(new ValidatorUk02UnterrichtsverteilungsdatenKursart(schuljahr, kursartAllg, kontext));
	}

	protected pruefe(): boolean {
		const kursartAllg: string = this._kursartAllg.get();
		if (!ZulaessigeKursart.isKursartAllgVorhanden(kursartAllg)) {
			this.addFehler(0, "Kursart der Unterrichtsverteilungsdaten: Das Feld 'Kursart' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUk01UnterrichtsverteilungsdatenKursart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUk01UnterrichtsverteilungsdatenKursart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUk01UnterrichtsverteilungsdatenKursart>('de.svws_nrw.asd.validate.kurse.ValidatorUk01UnterrichtsverteilungsdatenKursart');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUk01UnterrichtsverteilungsdatenKursart(obj: unknown): ValidatorUk01UnterrichtsverteilungsdatenKursart {
	return obj as ValidatorUk01UnterrichtsverteilungsdatenKursart;
}
