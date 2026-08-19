import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden } from '../../../asd/validate/kurse/ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden extends Validator {

	private readonly wochenstundenLehrer: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden des Lehrer
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(wochenstundenLehrer: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
		this._validatoren.add(new ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(this.getNotNullSupplierDouble(wochenstundenLehrer), kontext));
	}

	protected pruefe(): boolean {
		const wochenstunden: number | null = this.wochenstundenLehrer.get();
		if ((wochenstunden === null)) {
			this.addFehler(0, "Wochenstunden der zusätzlichen Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(obj: unknown): ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden {
	return obj as ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden;
}
