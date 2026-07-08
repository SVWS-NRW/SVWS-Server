import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

	/**
	 * Die Staatsangehoerigkeit2 des Schuelers
	 */
	private readonly _idStaatsangehoerigkeit2: Supplier<number | null>;

	/**
	 * Die Staatsangehoerigkeit des Schuelers
	 */
	private readonly _idStaatsangehoerigkeit: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit2   Staatsangehörigkeit2ID
	 * @param idStaatsangehoerigkeit    StaatsangehörigkeitID
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit2: Supplier<number | null>, idStaatsangehoerigkeit: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit2 = idStaatsangehoerigkeit2;
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		this._validatoren.add(new ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit(idStaatsangehoerigkeit2, idStaatsangehoerigkeit, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit(obj: unknown): ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit {
	return obj as ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit;
}
