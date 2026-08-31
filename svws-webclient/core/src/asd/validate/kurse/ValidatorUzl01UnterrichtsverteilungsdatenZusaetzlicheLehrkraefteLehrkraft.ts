import { KursLehrer } from '../../../asd/data/kurse/KursLehrer';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/**
	 * Die Liste weiterer Lehrer.
	 */
	private readonly _listWeitereLehrer: Supplier<List<KursLehrer>>;

	/**
	 * Die Liste der Lehrer.
	 */
	private readonly _listLehrer: Supplier<List<LehrerStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung zusätzlicher Lehrkräfte
	 *
	 * @param listWeitereLehrer   die Liste der Kurslehrer
	 * @param listLehrer          die Liste der Lehrer
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listWeitereLehrer: Supplier<List<KursLehrer>>, listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._listWeitereLehrer = listWeitereLehrer;
		this._listLehrer = listLehrer;
	}

	protected pruefe(): boolean {
		const listWeitereLehrer: List<KursLehrer> | null = this._listWeitereLehrer.get();
		const listLehrer: List<LehrerStatistikGesamt> | null = this._listLehrer.get();
		if (listWeitereLehrer === null || listLehrer === null) {
			return true;
		}
		for (const idKurslehrer of listWeitereLehrer) {
			let gefunden: boolean = false;
			for (const idLehrer of listLehrer) {
				if (idKurslehrer.idLehrer === idLehrer.id) {
					gefunden = true;
				}
			}
			if (!gefunden) {
				this.addFehler(0, "Zusätzliche Lehrkraft: Ungültige ausgewählte Lehrkraft.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft>('de.svws_nrw.asd.validate.kurse.ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(obj: unknown): ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft {
	return obj as ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft;
}
