import { Fachklasse } from '../../../asd/types/schule/Fachklasse';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlb02SchuelerLernabschnittsdatenFachklassen extends Validator {

	/**
	 * Das aktuelle Schuljahr des Schülers
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Die FachklassenID des Schuelers
	 */
	private readonly _fachklasseID: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Fachklasse des Schülers: Der eingetragene Wert für das Feld 'Fachklasse' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Fachklasse.
	 *
	 * @param schuljahr       Schuljahr
	 * @param fachklasseID    FachklassenID
	 * @param kontext         der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, fachklasseID: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._fachklasseID = fachklasseID;
	}

	protected pruefe(): boolean {
		if (!Fachklasse.data().isGueltig(this._fachklasseID.get(), this._schuljahr.get())) {
			this.addFehler(0, ValidatorSlb02SchuelerLernabschnittsdatenFachklassen.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlb02SchuelerLernabschnittsdatenFachklassen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlb02SchuelerLernabschnittsdatenFachklassen', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlb02SchuelerLernabschnittsdatenFachklassen>('de.svws_nrw.asd.validate.schueler.ValidatorSlb02SchuelerLernabschnittsdatenFachklassen');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlb02SchuelerLernabschnittsdatenFachklassen(obj: unknown): ValidatorSlb02SchuelerLernabschnittsdatenFachklassen {
	return obj as ValidatorSlb02SchuelerLernabschnittsdatenFachklassen;
}
