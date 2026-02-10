import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv11LehrerStammdatenVorname extends Validator {

	/**
	 * Der Vorname des Lehrers
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Vorname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const vorname: string | null = this.daten.get();
		if (vorname.length === 1) {
			this.addFehler(2, "Vorname der Lehrkraft: Der Vorname besteht aus nur einem Zeichen. Bitte überprüfen sie ihre Angaben.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv11LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv11LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv11LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv11LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv11LehrerStammdatenVorname(obj: unknown): ValidatorLsv11LehrerStammdatenVorname {
	return obj as ValidatorLsv11LehrerStammdatenVorname;
}
