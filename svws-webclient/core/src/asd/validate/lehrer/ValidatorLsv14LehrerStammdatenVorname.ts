import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv14LehrerStammdatenVorname extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const vorname: string | null = this.daten.get();
		const fehlertext: string | null = "Vorname der Lehrkraft: Die zweite Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.";
		if (vorname.length > 1 && JavaCharacter.isUpperCase(vorname.charAt(1))) {
			this.addFehler(5, fehlertext);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv14LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv14LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv14LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv14LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv14LehrerStammdatenVorname(obj: unknown): ValidatorLsv14LehrerStammdatenVorname {
	return obj as ValidatorLsv14LehrerStammdatenVorname;
}
