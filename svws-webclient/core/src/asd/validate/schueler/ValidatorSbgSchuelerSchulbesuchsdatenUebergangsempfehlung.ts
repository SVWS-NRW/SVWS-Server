import { ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung } from '../../../asd/validate/schueler/ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public constructor(idUebergangsempfehlungGrundschule: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung(idUebergangsempfehlungGrundschule, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung>('de.svws_nrw.asd.validate.schueler.ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung(obj: unknown): ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung {
	return obj as ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung;
}
