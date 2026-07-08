import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

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
		this._validatoren.add(new ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit(this.getNotNullSupplierLong(idStaatsangehoerigkeit2), kontext));
	}

	protected pruefe(): boolean {
		const idStaatsangehoerigkeit2: number | null = this._idStaatsangehoerigkeit2.get();
		const idStaatsangehoerigkeit: number | null = this._idStaatsangehoerigkeit.get();
		if (idStaatsangehoerigkeit2 === null) {
			return false;
		}
		if (idStaatsangehoerigkeit === null && idStaatsangehoerigkeit2 !== null) {
			this.addFehler(0, "2. Staatsangehörigkeit des Schülers: Das Feld '2. Staatsangehörigkeit' darf nur ausgefüllt sein, wenn das Feld '1. Staatsangehörigkeit' ausgefüllt ist");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit(obj: unknown): ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit {
	return obj as ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit;
}
