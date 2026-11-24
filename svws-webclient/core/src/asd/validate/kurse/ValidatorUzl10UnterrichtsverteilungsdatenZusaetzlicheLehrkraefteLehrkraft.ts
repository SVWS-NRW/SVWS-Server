import { JavaObject } from '../../../java/lang/JavaObject';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/**
	 * Die Liste der IDs weiterer Lehrer.
	 */
	private readonly _idsWeitereLehrer: Supplier<List<number> | null>;

	/**
	 * Die ID des Kurslehrers
	 */
	private readonly _idLehrer: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung zusätzlicher Lehrkräfte
	 *
	 * @param idsWeitereLehrer    die Liste der IDs der weiteren LehrerKursleherer
	 * @param idLehrer            die ID des Kurslehrers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idsWeitereLehrer: Supplier<List<number> | null>, idLehrer: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idsWeitereLehrer = idsWeitereLehrer;
		this._idLehrer = idLehrer;
	}

	protected pruefe(): boolean {
		const idsWeitereLehrer: List<number> | null = this._idsWeitereLehrer.get();
		const idLehrer: number | null = this._idLehrer.get();
		if (idsWeitereLehrer === null || idLehrer === null) {
			return true;
		}
		for (const idKurslehrer of idsWeitereLehrer) {
			if (idKurslehrer !== null && JavaObject.equalsTranspiler(idLehrer, (idKurslehrer))) {
				this.addFehler(0, "Zusätzliche Lehrkraft: Die ausgewählte zusätzliche Lehrkraft ist bereits oben im Feld 'Lehrkraft' angegeben. Bitte prüfen.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft>('de.svws_nrw.asd.validate.kurse.ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(obj: unknown): ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft {
	return obj as ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft;
}
