import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv06LehrerStammdatenVorname extends Validator {

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
		const vorname: string | null = this.daten.vorname;
		const fehlertext: string | null = "Vorname der Lehrkraft: Die dritte Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.";
		if (vorname.length > 2 && JavaCharacter.isUpperCase(vorname.charAt(2))) {
			this.addFehler(6, fehlertext);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv06LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv06LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv06LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv06LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv06LehrerStammdatenVorname(obj: unknown): ValidatorLsv06LehrerStammdatenVorname {
	return obj as ValidatorLsv06LehrerStammdatenVorname;
}
