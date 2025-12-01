import { java_util_Set_of } from '../../../java/util/JavaSet';
import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { NamensManager } from '../../../asd/validate/NamensManager';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn06LehrerStammdatenNachname extends Validator {

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
		const nachname: string | null = this.daten.nachname;
		const nachnameOhneZusatz: string = NamensManager.getOhneZusatz(nachname);
		const fehlertext6: string | null = "Nachname der Lehrkraft: Die dritte Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.";
		if ((nachnameOhneZusatz.length > 2) && JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(2)) && !java_util_Set_of("A'", "D'", "M'", "O'", "Mc").contains(nachnameOhneZusatz.substring(0, 2))) {
			this.addFehler(6, fehlertext6);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn06LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsn06LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn06LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn06LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn06LehrerStammdatenNachname(obj: unknown): ValidatorLsn06LehrerStammdatenNachname {
	return obj as ValidatorLsn06LehrerStammdatenNachname;
}
