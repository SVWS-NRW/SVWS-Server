import { ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung } from '../../../asd/validate/schueler/ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {

	/**
	 * Die Übergangsempfehlung-Grundschule-ID des Schülers
	 */
	readonly _idUebergangsempfehlungGrundschule: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public constructor(idUebergangsempfehlungGrundschule: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idUebergangsempfehlungGrundschule = idUebergangsempfehlungGrundschule;
		this._validatoren.add(new ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung(this.getNotNullSupplierLong(idUebergangsempfehlungGrundschule), kontext));
	}

	protected pruefe(): boolean {
		const idUebergangsempfehlungGrundschule: number | null = this._idUebergangsempfehlungGrundschule.get();
		if (idUebergangsempfehlungGrundschule === null) {
			this.addFehler(0, "Übergangsempfehlung des Schülers: Das Feld 'Übergangsempfehlung Jg. 5' darf nicht leer sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung>('de.svws_nrw.asd.validate.schueler.ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung(obj: unknown): ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung {
	return obj as ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung;
}
