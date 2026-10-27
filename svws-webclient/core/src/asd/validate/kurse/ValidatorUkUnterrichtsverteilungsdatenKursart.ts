import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorUk00UnterrichtsverteilungsdatenKursart } from '../../../asd/validate/kurse/ValidatorUk00UnterrichtsverteilungsdatenKursart';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUkUnterrichtsverteilungsdatenKursart extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param kursartAllg              Die Kursart
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, kursartAllg: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => kontext.getSchuljahrBySchuljahresabschnitt(this.getNotNullSupplierLong(idSchuljahresabschnitt)) };
		this._validatoren.add(new ValidatorUk00UnterrichtsverteilungsdatenKursart(schuljahr, kursartAllg, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUkUnterrichtsverteilungsdatenKursart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUkUnterrichtsverteilungsdatenKursart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUkUnterrichtsverteilungsdatenKursart>('de.svws_nrw.asd.validate.kurse.ValidatorUkUnterrichtsverteilungsdatenKursart');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUkUnterrichtsverteilungsdatenKursart(obj: unknown): ValidatorUkUnterrichtsverteilungsdatenKursart {
	return obj as ValidatorUkUnterrichtsverteilungsdatenKursart;
}
