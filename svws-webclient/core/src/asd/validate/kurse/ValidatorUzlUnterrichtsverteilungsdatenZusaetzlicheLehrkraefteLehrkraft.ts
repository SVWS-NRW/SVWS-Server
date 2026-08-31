import { KursLehrer } from '../../../asd/data/kurse/KursLehrer';
import { ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft } from '../../../asd/validate/kurse/ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listWeitereLehrer  die Liste der Kursleherer
	 * @param listLehrer         die Liste aller Lehrer dieser Schule
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(listWeitereLehrer: Supplier<List<KursLehrer>>, listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(listWeitereLehrer, listLehrer, kontext));
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
