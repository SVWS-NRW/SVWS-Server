import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft } from '../../../asd/validate/kurse/ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft extends Validator {

	private readonly _idLehrkraft: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrkraft   die Wochenstunden des Lehrer
	 * @param listLehrer    die Liste aller Lehrer dieser Schule
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idLehrkraft: Supplier<number | null>, listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._idLehrkraft = idLehrkraft;
		this._validatoren.add(new ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft(this.getNotNullSupplierLong(idLehrkraft), listLehrer, kontext));
	}

	protected pruefe(): boolean {
		const idLehrkraft: number | null = this._idLehrkraft.get();
		if ((idLehrkraft === null)) {
			this.addFehler(0, "Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft>('de.svws_nrw.asd.validate.kurse.ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft(obj: unknown): ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft {
	return obj as ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft;
}
