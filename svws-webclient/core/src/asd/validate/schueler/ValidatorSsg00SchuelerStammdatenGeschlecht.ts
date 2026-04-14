import { ValidatorSsg01SchuelerStammdatenGeschlecht } from '../../../asd/validate/schueler/ValidatorSsg01SchuelerStammdatenGeschlecht';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsg00SchuelerStammdatenGeschlecht extends Validator {

	/**
	 * Das Geschlecht des Schuelers
	 */
	private readonly _idGeschlecht: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeschlecht     das Geschlecht des Schuelers
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(idGeschlecht: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeschlecht = idGeschlecht;
		this._validatoren.add(new ValidatorSsg01SchuelerStammdatenGeschlecht(this.getNotNullSupplierInteger(idGeschlecht), kontext));
	}

	protected pruefe(): boolean {
		const idGeschlecht: number | null = this._idGeschlecht.get();
		if (idGeschlecht === null) {
			this.addFehler(0, "Das Feld 'Geschlecht' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsg00SchuelerStammdatenGeschlecht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsg00SchuelerStammdatenGeschlecht', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsg00SchuelerStammdatenGeschlecht>('de.svws_nrw.asd.validate.schueler.ValidatorSsg00SchuelerStammdatenGeschlecht');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsg00SchuelerStammdatenGeschlecht(obj: unknown): ValidatorSsg00SchuelerStammdatenGeschlecht {
	return obj as ValidatorSsg00SchuelerStammdatenGeschlecht;
}
