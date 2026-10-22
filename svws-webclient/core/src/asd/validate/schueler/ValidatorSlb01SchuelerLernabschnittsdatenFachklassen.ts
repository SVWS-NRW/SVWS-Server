import { Fachklasse } from '../../../asd/types/schule/Fachklasse';
import { ValidatorSlb02SchuelerLernabschnittsdatenFachklassen } from '../../../asd/validate/schueler/ValidatorSlb02SchuelerLernabschnittsdatenFachklassen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlb01SchuelerLernabschnittsdatenFachklassen extends Validator {

	/**
	 * Die FachklassenID des Schuelers
	 */
	private readonly _fachklasseID: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Fachklasse.
	 *
	 * @param schuljahr       Schuljahr
	 * @param fachklasseID    FachklassenID
	 * @param kontext         der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, fachklasseID: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._fachklasseID = fachklasseID;
		this._validatoren.add(new ValidatorSlb02SchuelerLernabschnittsdatenFachklassen(schuljahr, fachklasseID, kontext));
	}

	protected pruefe(): boolean {
		const fk: Fachklasse | null = Fachklasse.data().getWertByIDOrNull(this._fachklasseID.get());
		if (fk === null) {
			this.addFehler(0, "Fachklasse des Schülers: Das Feld 'Fachklasse' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlb01SchuelerLernabschnittsdatenFachklassen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlb01SchuelerLernabschnittsdatenFachklassen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlb01SchuelerLernabschnittsdatenFachklassen>('de.svws_nrw.asd.validate.schueler.ValidatorSlb01SchuelerLernabschnittsdatenFachklassen');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlb01SchuelerLernabschnittsdatenFachklassen(obj: unknown): ValidatorSlb01SchuelerLernabschnittsdatenFachklassen {
	return obj as ValidatorSlb01SchuelerLernabschnittsdatenFachklassen;
}
