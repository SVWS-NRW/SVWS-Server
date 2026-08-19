import { ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden } from '../../../asd/validate/kurse/ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden des Lehrer
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(wochenstundenLehrer: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(wochenstundenLehrer, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(obj: unknown): ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden {
	return obj as ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden;
}
