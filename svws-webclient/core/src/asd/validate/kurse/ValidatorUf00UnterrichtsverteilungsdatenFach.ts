import { ValidatorUf01UnterrichtsverteilungsdatenFach } from '../../../asd/validate/kurse/ValidatorUf01UnterrichtsverteilungsdatenFach';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUf00UnterrichtsverteilungsdatenFach extends Validator {

	private readonly _idFach: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Fach des Kurses: Kein Wert vorhanden.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public constructor(idFach: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFach = idFach;
		this._validatoren.add(new ValidatorUf01UnterrichtsverteilungsdatenFach(this.getNotNullSupplierLong(idFach), kontext));
	}

	protected pruefe(): boolean {
		const idFach: number | null = this._idFach.get();
		if (idFach === null) {
			this.addFehler(0, ValidatorUf00UnterrichtsverteilungsdatenFach.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUf00UnterrichtsverteilungsdatenFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUf00UnterrichtsverteilungsdatenFach', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUf00UnterrichtsverteilungsdatenFach>('de.svws_nrw.asd.validate.kurse.ValidatorUf00UnterrichtsverteilungsdatenFach');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUf00UnterrichtsverteilungsdatenFach(obj: unknown): ValidatorUf00UnterrichtsverteilungsdatenFach {
	return obj as ValidatorUf00UnterrichtsverteilungsdatenFach;
}
