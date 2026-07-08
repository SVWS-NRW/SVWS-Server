import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import { ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

	/**
	 * Die Staatsangehoerigkeit des Schuelers
	 */
	private readonly _idStaatsangehoerigkeit2: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit2   Staatsangehoerigkeit2ID
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit2: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit2 = idStaatsangehoerigkeit2;
		this._validatoren.add(new ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit(idStaatsangehoerigkeit2, kontext));
	}

	protected pruefe(): boolean {
		const sa: Nationalitaeten | null = Nationalitaeten.data().getWertByIDOrNull(this._idStaatsangehoerigkeit2.get());
		if (sa === null) {
			this.addFehler(0, "2. Staatsangehörigkeit des Schülers: Das Feld '2. Staatsangehörigkeit' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit(obj: unknown): ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit {
	return obj as ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit;
}
