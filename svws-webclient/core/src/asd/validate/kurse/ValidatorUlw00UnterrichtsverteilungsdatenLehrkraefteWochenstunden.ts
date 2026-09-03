import { ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden } from '../../../asd/validate/kurse/ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden extends Validator {

	private readonly wochenstundenLehrer: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden der Lehrkraft
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(wochenstundenLehrer: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
		this._validatoren.add(new ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden(this.getNotNullSupplierInteger(wochenstundenLehrer), kontext));
	}

	protected pruefe(): boolean {
		const wochenstunden: number | null = this.wochenstundenLehrer.get();
		if ((wochenstunden === null)) {
			this.addFehler(0, "Wochenstunden der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden(obj: unknown): ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden {
	return obj as ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden;
}
