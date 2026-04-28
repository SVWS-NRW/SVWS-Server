import { ValidatorSsgSchuelerStammdatenGeschlecht } from '../../../asd/validate/schueler/ValidatorSsgSchuelerStammdatenGeschlecht';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSsdSchuelerStammdatenGeburtsdatum } from '../../../asd/validate/schueler/ValidatorSsdSchuelerStammdatenGeburtsdatum';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsSchuelerStammdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geschlecht    das geschlecht des Schuelers
	 * @param geburtsdatum  das geburtsdatum des Schuelers
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(geschlecht: Supplier<number | null>, geburtsdatum: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsgSchuelerStammdatenGeschlecht(geschlecht, kontext));
		this._validatoren.add(new ValidatorSsdSchuelerStammdatenGeburtsdatum(geburtsdatum, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsSchuelerStammdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsSchuelerStammdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsSchuelerStammdaten>('de.svws_nrw.asd.validate.schueler.ValidatorSsSchuelerStammdaten');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsSchuelerStammdaten(obj: unknown): ValidatorSsSchuelerStammdaten {
	return obj as ValidatorSsSchuelerStammdaten;
}
