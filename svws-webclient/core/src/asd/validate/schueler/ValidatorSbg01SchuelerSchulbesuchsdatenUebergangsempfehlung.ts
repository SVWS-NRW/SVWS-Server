import { Uebergangsempfehlung } from '../../../asd/types/schueler/Uebergangsempfehlung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung } from '../../../asd/validate/schueler/ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung';

export class ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {

	/**
	 * Die Übergangsempfehlung-Grundschule-ID des Schülers
	 */
	private readonly _idUebergangsempfehlungGrundschule: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public constructor(idUebergangsempfehlungGrundschule: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idUebergangsempfehlungGrundschule = idUebergangsempfehlungGrundschule;
		this._validatoren.add(new ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung(idUebergangsempfehlungGrundschule, kontext));
	}

	protected pruefe(): boolean {
		const uge: Uebergangsempfehlung | null = Uebergangsempfehlung.data().getWertByIDOrNull(this._idUebergangsempfehlungGrundschule.get());
		if (uge === null) {
			this.addFehler(0, "Übergangsempfehlung des Schülers: Das Feld 'Übergangsempfehlung Jg. 5' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung>('de.svws_nrw.asd.validate.schueler.ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung(obj: unknown): ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung {
	return obj as ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung;
}
