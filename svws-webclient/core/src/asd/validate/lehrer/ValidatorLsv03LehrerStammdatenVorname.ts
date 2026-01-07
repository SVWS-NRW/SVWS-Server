import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv03LehrerStammdatenVorname extends Validator {

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
		if (vorname.startsWith(" ") || vorname.startsWith("\t")) {
			this.addFehler(3, "Vorname der Lehrkraft: Die Eintragung des Vornamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv03LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsv03LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv03LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv03LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv03LehrerStammdatenVorname(obj: unknown): ValidatorLsv03LehrerStammdatenVorname {
	return obj as ValidatorLsv03LehrerStammdatenVorname;
}
