import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit';

export class ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

	/**
	 * Die Staatsangehoerigkeit des Schuelers
	 */
	private readonly _idStaatsangehoerigkeit: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   StaatsangehoerigkeitID
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		this._validatoren.add(new ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit(idStaatsangehoerigkeit, kontext));
	}

	protected pruefe(): boolean {
		const sa: Nationalitaeten | null = Nationalitaeten.data().getWertByIDOrNull(this._idStaatsangehoerigkeit.get());
		if (sa === null) {
			this.addFehler(0, "1. Staatsangehörigkeit des Schülers: Das Feld '1. Staatsangehörigkeit' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit(obj: unknown): ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit {
	return obj as ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit;
}
