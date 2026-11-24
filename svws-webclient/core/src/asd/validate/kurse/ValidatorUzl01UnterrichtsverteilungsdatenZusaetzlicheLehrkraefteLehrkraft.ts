import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/**
	 * Die Liste der IDs weiterer Lehrer.
	 */
	private readonly _idsWeitereLehrer: Supplier<List<number> | null>;

	/**
	 * Die Liste der IDs aller Lehrer.
	 */
	private readonly _idsAlleLehrer: Supplier<List<number> | null>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung zusätzlicher Lehrkräfte
	 *
	 * @param idsWeitereLehrer    die Liste der IDs der weiteren LehrerKursleherer
	 * @param idsAlleLehrer       die Liste der IDs aller Lehrer dieser Schule
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idsWeitereLehrer: Supplier<List<number> | null>, idsAlleLehrer: Supplier<List<number> | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idsWeitereLehrer = idsWeitereLehrer;
		this._idsAlleLehrer = idsAlleLehrer;
	}

	protected pruefe(): boolean {
		const idsWeitereLehrer: List<number> | null = this._idsWeitereLehrer.get();
		const idsAlleLehrer: List<number> | null = this._idsAlleLehrer.get();
		if (idsWeitereLehrer === null || idsAlleLehrer === null) {
			return true;
		}
		for (const idKurslehrer of idsWeitereLehrer) {
			let gefunden: boolean = false;
			for (const idLehrer of idsAlleLehrer) {
				if (idKurslehrer as unknown === idLehrer as unknown) {
					gefunden = true;
					break;
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
