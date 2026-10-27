import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ZulaessigeKursart } from '../../../asd/types/kurse/ZulaessigeKursart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUk02UnterrichtsverteilungsdatenKursart extends Validator {

	/**
	 * Das aktuelle Schuljahr
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Fach
	 */
	private readonly _kursartAllg: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param schuljahr     Schuljahr
	 * @param kursartAllg   FachID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, kursartAllg: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this._kursartAllg = kursartAllg;
		this._schuljahr = schuljahr;
	}

	protected pruefe(): boolean {
		if (ZulaessigeKursart.getByAllgemeinerKursart(this._schuljahr.get(), this._kursartAllg.get()).isEmpty()) {
			this.addFehler(0, "Kursart der Unterrichtsverteilungsdaten: Der eingetragene Wert für das Feld 'Kursart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUk02UnterrichtsverteilungsdatenKursart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUk02UnterrichtsverteilungsdatenKursart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUk02UnterrichtsverteilungsdatenKursart>('de.svws_nrw.asd.validate.kurse.ValidatorUk02UnterrichtsverteilungsdatenKursart');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUk02UnterrichtsverteilungsdatenKursart(obj: unknown): ValidatorUk02UnterrichtsverteilungsdatenKursart {
	return obj as ValidatorUk02UnterrichtsverteilungsdatenKursart;
}
