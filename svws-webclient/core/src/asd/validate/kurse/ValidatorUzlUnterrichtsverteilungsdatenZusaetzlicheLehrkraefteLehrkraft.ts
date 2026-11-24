import { ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft } from '../../../asd/validate/kurse/ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft } from '../../../asd/validate/kurse/ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idsWeitereLehrer    die Liste der IDs der weiteren LehrerKursleherer
	 * @param idsAlleLehrer       die Liste der IDs aller Lehrer dieser Schule
	 * @param idLehrer            die ID einer ausgewählten, weiteren Lehrkraft
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idsWeitereLehrer: Supplier<List<number> | null>, idsAlleLehrer: Supplier<List<number> | null>, idLehrer: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(idsWeitereLehrer, idsAlleLehrer, kontext));
		this._validatoren.add(new ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(idsWeitereLehrer, idLehrer, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft>('de.svws_nrw.asd.validate.kurse.ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(obj: unknown): ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft {
	return obj as ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft;
}
