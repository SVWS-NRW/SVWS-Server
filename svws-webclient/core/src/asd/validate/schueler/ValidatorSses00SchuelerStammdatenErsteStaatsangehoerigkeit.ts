import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

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
		this._validatoren.add(new ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit(this.getNotNullSupplierLong(idStaatsangehoerigkeit), kontext));
	}

	protected pruefe(): boolean {
		const idStaatsangehoerigkeit: number | null = this._idStaatsangehoerigkeit.get();
		if (idStaatsangehoerigkeit === null) {
			this.addFehler(0, "1. Staatsangehörigkeit des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit(obj: unknown): ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit {
	return obj as ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit;
}
