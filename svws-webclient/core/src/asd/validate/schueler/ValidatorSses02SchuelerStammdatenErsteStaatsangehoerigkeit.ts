import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

	/**
	 * Die Staatsangehoerigkeit des Schuelers
	 */
	private readonly _idStaatsangehoerigkeit: Supplier<number>;

	private static readonly FEHLERTEXT: string = "1. Staatsangehörigkeit des Schülers: Der eingetragene Wert für das Feld '1. Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param idStaatsangehoerigkeit   StaatsangehörigkeitID
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
	}

	protected pruefe(): boolean {
		if (!Nationalitaeten.data().isGueltig(this._idStaatsangehoerigkeit.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit(obj: unknown): ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit {
	return obj as ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit;
}
