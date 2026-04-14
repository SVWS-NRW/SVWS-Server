import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { Geschlecht } from '../../../asd/types/Geschlecht';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsg01SchuelerStammdatenGeschlecht extends Validator {

	/**
	 * Die Schueler-Stammdaten
	 */
	private readonly _idGeschlecht: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeschlecht       das Geschlecht des Schuelers
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(idGeschlecht: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeschlecht = idGeschlecht;
	}

	protected pruefe(): boolean {
		const geschlecht: Geschlecht | null = Geschlecht.fromValue(this._idGeschlecht.get());
		if (geschlecht === null) {
			this.addFehler(0, "Unzulässiger Schlüssel '" + this._idGeschlecht.get() + "' im Feld 'Geschlecht'. Die gültigen Schlüssel entnehmen Sie bitte dem Pulldownmenü.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsg01SchuelerStammdatenGeschlecht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsg01SchuelerStammdatenGeschlecht', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsg01SchuelerStammdatenGeschlecht>('de.svws_nrw.asd.validate.schueler.ValidatorSsg01SchuelerStammdatenGeschlecht');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsg01SchuelerStammdatenGeschlecht(obj: unknown): ValidatorSsg01SchuelerStammdatenGeschlecht {
	return obj as ValidatorSsg01SchuelerStammdatenGeschlecht;
}
