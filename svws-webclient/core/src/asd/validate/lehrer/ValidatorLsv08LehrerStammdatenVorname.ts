import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv08LehrerStammdatenVorname extends Validator {

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
		const nLower: string | null = this.daten.vorname.toLowerCase();
		if (nLower.startsWith("frau ") || nLower.startsWith("herr ")) {
			this.addFehler(8, "Vorname der Lehrkraft: Die Anrede (Frau oder Herr) gehört nicht in den Vornamen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv08LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv08LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv08LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv08LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv08LehrerStammdatenVorname(obj: unknown): ValidatorLsv08LehrerStammdatenVorname {
	return obj as ValidatorLsv08LehrerStammdatenVorname;
}
