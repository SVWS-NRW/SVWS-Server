import { ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

	/**
	 * Die Staatsangehoerigkeit des Schuelers
	 */
	private readonly _idStaatsangehoerigkeit: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit    StaatsangehörigkeitID
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		this._validatoren.add(new ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit(this.getNotNullSupplierLong(idStaatsangehoerigkeit), kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit(obj: unknown): ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit {
	return obj as ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit;
}
