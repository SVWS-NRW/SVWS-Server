import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsv10SchuelerStammdatenVorname extends Validator {

	/**
	 * Der Schueler-Vorname
	 */
	private readonly vorname: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param vorname   der Vorname des Schuelers
	 * @param kontext   der Kontext der Schule
	 */
	public constructor(vorname: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.vorname = vorname;
	}

	protected pruefe(): boolean {
		if (JavaString.isBlank(this.vorname.get().trim())) {
			this.addFehler(1, "Rufname des Schülers: Der Rufname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsv10SchuelerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsv10SchuelerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsv10SchuelerStammdatenVorname>('de.svws_nrw.asd.validate.schueler.ValidatorSsv10SchuelerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsv10SchuelerStammdatenVorname(obj: unknown): ValidatorSsv10SchuelerStammdatenVorname {
	return obj as ValidatorSsv10SchuelerStammdatenVorname;
}
