import { ValidatorSlb01SchuelerLernabschnittsdatenFachklassen } from '../../../asd/validate/schueler/ValidatorSlb01SchuelerLernabschnittsdatenFachklassen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlb00SchuelerLernabschnittsdatenFachklassen extends Validator {

	/**
	 * Die Fachklassen-ID des Schülers
	 */
	private readonly _fachklasseID: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param schuljahr       Schuljahr
	 * @param fachklasseID    FachklassenID
	 * @param kontext         der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, fachklasseID: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._fachklasseID = fachklasseID;
		this._validatoren.add(new ValidatorSlb01SchuelerLernabschnittsdatenFachklassen(schuljahr, this.getNotNullSupplierLong(fachklasseID), kontext));
	}

	protected pruefe(): boolean {
		const fachklasseID: number | null = this._fachklasseID.get();
		if (fachklasseID === null) {
			this.addFehler(0, "Fachklasse des Schülers: Das Feld  'Fachklasse' darf nicht leer sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlb00SchuelerLernabschnittsdatenFachklassen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlb00SchuelerLernabschnittsdatenFachklassen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlb00SchuelerLernabschnittsdatenFachklassen>('de.svws_nrw.asd.validate.schueler.ValidatorSlb00SchuelerLernabschnittsdatenFachklassen');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlb00SchuelerLernabschnittsdatenFachklassen(obj: unknown): ValidatorSlb00SchuelerLernabschnittsdatenFachklassen {
	return obj as ValidatorSlb00SchuelerLernabschnittsdatenFachklassen;
}
