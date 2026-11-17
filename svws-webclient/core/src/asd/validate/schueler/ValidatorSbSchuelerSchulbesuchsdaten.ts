import { ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung } from '../../../asd/validate/schueler/ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbSchuelerSchulbesuchsdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKuerzelGrundschuleUebergangsempfehlung    GrundschuleUebergangsempfehlung ID
	 * @param kontext        				              der Kontext des Validators
	 */
	public constructor(idKuerzelGrundschuleUebergangsempfehlung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung(idKuerzelGrundschuleUebergangsempfehlung, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbSchuelerSchulbesuchsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbSchuelerSchulbesuchsdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbSchuelerSchulbesuchsdaten>('de.svws_nrw.asd.validate.schueler.ValidatorSbSchuelerSchulbesuchsdaten');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbSchuelerSchulbesuchsdaten(obj: unknown): ValidatorSbSchuelerSchulbesuchsdaten {
	return obj as ValidatorSbSchuelerSchulbesuchsdaten;
}
