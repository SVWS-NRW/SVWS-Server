import { Fach } from '../../../asd/types/fach/Fach';
import { ValidatorUf02UnterrichtsverteilungsdatenFach } from '../../../asd/validate/kurse/ValidatorUf02UnterrichtsverteilungsdatenFach';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUf01UnterrichtsverteilungsdatenFach extends Validator {

	private readonly _idFach: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Fach des Kurses: Das Feld 'Fach' muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public constructor(idFach: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFach = idFach;
		this._validatoren.add(new ValidatorUf02UnterrichtsverteilungsdatenFach(idFach, kontext));
	}

	protected pruefe(): boolean {
		const idFach: number = this._idFach.get();
		const fach: Fach | null = Fach.data().getWertByIDOrNull(idFach);
		if (fach === null) {
			this.addFehler(0, ValidatorUf01UnterrichtsverteilungsdatenFach.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUf01UnterrichtsverteilungsdatenFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUf01UnterrichtsverteilungsdatenFach', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUf01UnterrichtsverteilungsdatenFach>('de.svws_nrw.asd.validate.kurse.ValidatorUf01UnterrichtsverteilungsdatenFach');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUf01UnterrichtsverteilungsdatenFach(obj: unknown): ValidatorUf01UnterrichtsverteilungsdatenFach {
	return obj as ValidatorUf01UnterrichtsverteilungsdatenFach;
}
