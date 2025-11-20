import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv03LehrerStammdatenVorname extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: LehrerStammdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerStammdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const vorname: string | null = this.daten.vorname;
		if (!this.exec(3, { getAsBoolean: () => vorname.startsWith(" ") || vorname.startsWith("\t") }, "Vorname der Lehrkraft: Die Eintragung des Vornamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs)."))
			success = false;
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv03LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsv03LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLsv03LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv03LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv03LehrerStammdatenVorname(obj: unknown): ValidatorLsv03LehrerStammdatenVorname {
	return obj as ValidatorLsv03LehrerStammdatenVorname;
}
