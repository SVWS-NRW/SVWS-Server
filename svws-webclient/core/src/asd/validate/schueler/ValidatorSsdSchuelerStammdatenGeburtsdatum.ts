import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorSsd00SchuelerStammdatenGeburtsdatum } from '../../../asd/validate/schueler/ValidatorSsd00SchuelerStammdatenGeburtsdatum';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsdSchuelerStammdatenGeburtsdatum extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsdatum     das Geburtsadtum des Schülers
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(geburtsdatum: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsd00SchuelerStammdatenGeburtsdatum(geburtsdatum, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsdSchuelerStammdatenGeburtsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsdSchuelerStammdatenGeburtsdatum', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsdSchuelerStammdatenGeburtsdatum>('de.svws_nrw.asd.validate.schueler.ValidatorSsdSchuelerStammdatenGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsdSchuelerStammdatenGeburtsdatum(obj: unknown): ValidatorSsdSchuelerStammdatenGeburtsdatum {
	return obj as ValidatorSsdSchuelerStammdatenGeburtsdatum;
}
