import { ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung } from '../../../asd/validate/schueler/ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung extends Validator {

	/**
	 * Die ID der Schulgliederung des Schülers
	 */
	private readonly _idSchulgliederung: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param schuljahr            Schuljahr
	 * @param idSchulgliederung    die ID der Schulgliederung
	 * @param kontext              der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idSchulgliederung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;
		this._validatoren.add(new ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung(schuljahr, this.getNotNullSupplierLong(idSchulgliederung), kontext));
	}

	protected pruefe(): boolean {
		const idSchulgliederung: number | null = this._idSchulgliederung.get();
		if (idSchulgliederung === null) {
			this.addFehler(0, "Schulgliederung des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung>('de.svws_nrw.asd.validate.schueler.ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung(obj: unknown): ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung {
	return obj as ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung;
}
