import { ValidatorSsv00SchuelerStammdatenVorname } from '../../../asd/validate/schueler/ValidatorSsv00SchuelerStammdatenVorname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsvSchuelerStammdatenVorname extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param vorname   der Vorname des Schuelers
	 * @param kontext   der Kontext der Schule
	 */
	public constructor(vorname: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsv00SchuelerStammdatenVorname(vorname, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsvSchuelerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsvSchuelerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsvSchuelerStammdatenVorname>('de.svws_nrw.asd.validate.schueler.ValidatorSsvSchuelerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsvSchuelerStammdatenVorname(obj: unknown): ValidatorSsvSchuelerStammdatenVorname {
	return obj as ValidatorSsvSchuelerStammdatenVorname;
}
