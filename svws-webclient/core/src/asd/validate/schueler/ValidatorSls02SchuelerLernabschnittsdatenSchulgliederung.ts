import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung extends Validator {

	/**
	 * Das aktuelle Schuljahr des Schülers
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Die ID der Schulgliederung des Schuelers
	 */
	private readonly _idSchulgliederung: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Schulgliederung des Schülers: Der eingetragene Wert für das Feld 'Schulgliederung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param schuljahr            Schuljahr
	 * @param idSchulgliederung    die ID der Schulgliederung
	 * @param kontext              der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idSchulgliederung: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idSchulgliederung = idSchulgliederung;
	}

	protected pruefe(): boolean {
		if (!Schulgliederung.data().isGueltig(this._idSchulgliederung.get(), this._schuljahr.get())) {
			this.addFehler(0, ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung>('de.svws_nrw.asd.validate.schueler.ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung(obj: unknown): ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung {
	return obj as ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung;
}
