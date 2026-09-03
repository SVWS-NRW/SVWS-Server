import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft extends Validator {

	private readonly _idLehrkraft: Supplier<number>;

	private readonly _listLehrer: Supplier<List<LehrerStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrkraft   der Lehrer
	 * @param listLehrer    die Liste aller Lehrer dieser Schule
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idLehrkraft: Supplier<number>, listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._idLehrkraft = idLehrkraft;
		this._listLehrer = listLehrer;
	}

	protected pruefe(): boolean {
		const idLehrkraft: number | null = this._idLehrkraft.get();
		const listlehrer: List<LehrerStatistikGesamt> | null = this._listLehrer.get();
		for (const lehrer of listlehrer) {
			if (idLehrkraft === lehrer.id) {
				return true;
			}
		}
		this.addFehler(0, "Lehrkraft: Das Feld 'Lehrkraft' muss zulässig sein.");
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft>('de.svws_nrw.asd.validate.kurse.ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft(obj: unknown): ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft {
	return obj as ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft;
}
