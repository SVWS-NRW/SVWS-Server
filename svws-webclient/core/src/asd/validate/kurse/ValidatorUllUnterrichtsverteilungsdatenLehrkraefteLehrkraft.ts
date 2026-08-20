import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft } from '../../../asd/validate/kurse/ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrkraft   die ID des Lehrer
	 * @param listLehrer    die Liste aller Lehrer dieser Schule
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idLehrkraft: Supplier<number | null>, listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft(idLehrkraft, listLehrer, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft>('de.svws_nrw.asd.validate.kurse.ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft(obj: unknown): ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft {
	return obj as ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft;
}
