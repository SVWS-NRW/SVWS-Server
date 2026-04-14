import { ValidatorSsg00SchuelerStammdatenGeschlecht } from '../../../asd/validate/schueler/ValidatorSsg00SchuelerStammdatenGeschlecht';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsgSchuelerStammdatenGeschlecht extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeschlecht     das Geschlecht des Schuelers
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(idGeschlecht: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsg00SchuelerStammdatenGeschlecht(idGeschlecht, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsgSchuelerStammdatenGeschlecht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsgSchuelerStammdatenGeschlecht', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsgSchuelerStammdatenGeschlecht>('de.svws_nrw.asd.validate.schueler.ValidatorSsgSchuelerStammdatenGeschlecht');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsgSchuelerStammdatenGeschlecht(obj: unknown): ValidatorSsgSchuelerStammdatenGeschlecht {
	return obj as ValidatorSsgSchuelerStammdatenGeschlecht;
}
