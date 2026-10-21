import { ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung } from '../../../asd/validate/schueler/ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung extends Validator {

	/**
	 * Die ID der Schulgliederung des Schuelers
	 */
	private readonly _idSchulgliederung: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param schuljahr            Schuljahr
	 * @param idSchulgliederung    die ID der Schulgliederung
	 * @param kontext              der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idSchulgliederung: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;
		this._validatoren.add(new ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung(schuljahr, idSchulgliederung, kontext));
	}

	protected pruefe(): boolean {
		const idSchulgliederung: Schulgliederung | null = Schulgliederung.data().getWertByIDOrNull(this._idSchulgliederung.get());
		if (idSchulgliederung === null) {
			this.addFehler(0, "Schulgliederung des Schülers: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung>('de.svws_nrw.asd.validate.schueler.ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung(obj: unknown): ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung {
	return obj as ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung;
}
