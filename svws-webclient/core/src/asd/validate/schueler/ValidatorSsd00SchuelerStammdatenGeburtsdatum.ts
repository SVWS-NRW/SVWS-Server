import { ValidatorSsd01SchuelerStammdatenGeburtsdatum } from '../../../asd/validate/schueler/ValidatorSsd01SchuelerStammdatenGeburtsdatum';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsd00SchuelerStammdatenGeburtsdatum extends Validator {

	/**
	 * Das Geburtsdatum des Schülers
	 */
	private readonly fieldGeburtsdatum: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsdatum     das Geburtsdatum des Schülers
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(geburtsdatum: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.fieldGeburtsdatum = geburtsdatum;
		this._validatoren.add(new ValidatorSsd01SchuelerStammdatenGeburtsdatum(this.getNotNullSupplier(geburtsdatum), kontext));
	}

	protected pruefe(): boolean {
		const geburtsdatum: string | null = this.fieldGeburtsdatum.get();
		if ((geburtsdatum === null) || (JavaString.isEmpty(geburtsdatum))) {
			this.addFehler(0, "Das Feld 'Geburtsdatum' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsd00SchuelerStammdatenGeburtsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsd00SchuelerStammdatenGeburtsdatum', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsd00SchuelerStammdatenGeburtsdatum>('de.svws_nrw.asd.validate.schueler.ValidatorSsd00SchuelerStammdatenGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsd00SchuelerStammdatenGeburtsdatum(obj: unknown): ValidatorSsd00SchuelerStammdatenGeburtsdatum {
	return obj as ValidatorSsd00SchuelerStammdatenGeburtsdatum;
}
