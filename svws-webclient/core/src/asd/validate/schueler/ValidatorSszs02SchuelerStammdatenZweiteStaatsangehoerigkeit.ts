import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

	/**
	 * Die Staatsangehoerigkeit des Schuelers
	 */
	private readonly _idStaatsangehoerigkeit2: Supplier<number>;

	private static readonly FEHLERTEXT: string = "2. Staatsangehörigkeit des Schülers: Der eingetragene Wert für das Feld '2. Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param idStaatsangehoerigkeit2   Staatsangehörigkeit2ID
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit2: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit2 = idStaatsangehoerigkeit2;
	}

	protected pruefe(): boolean {
		if (!Nationalitaeten.data().isGueltig(this._idStaatsangehoerigkeit2.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit(obj: unknown): ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit {
	return obj as ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit;
}
