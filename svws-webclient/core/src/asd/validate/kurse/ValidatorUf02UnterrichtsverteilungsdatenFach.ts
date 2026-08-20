import { Fach } from '../../../asd/types/fach/Fach';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUf02UnterrichtsverteilungsdatenFach extends Validator {

	/**
	 * Fach
	 */
	private readonly _idFach: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Fach des Kurses: Der eingetragene Wert für das Feld 'Fach' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public constructor(idFach: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFach = idFach;
	}

	protected pruefe(): boolean {
		if (!Fach.data().isGueltig(this._idFach.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, ValidatorUf02UnterrichtsverteilungsdatenFach.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUf02UnterrichtsverteilungsdatenFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUf02UnterrichtsverteilungsdatenFach', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUf02UnterrichtsverteilungsdatenFach>('de.svws_nrw.asd.validate.kurse.ValidatorUf02UnterrichtsverteilungsdatenFach');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUf02UnterrichtsverteilungsdatenFach(obj: unknown): ValidatorUf02UnterrichtsverteilungsdatenFach {
	return obj as ValidatorUf02UnterrichtsverteilungsdatenFach;
}
