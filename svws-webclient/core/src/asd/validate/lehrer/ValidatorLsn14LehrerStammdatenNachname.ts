import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { NamensManager } from '../../../asd/validate/NamensManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn14LehrerStammdatenNachname extends Validator {

	/**
	 * Der Lehrer-Nachname
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const nachnameOhneZusatz: string = NamensManager.getOhneZusatz(this.daten.get());
		const fehlertext5: string | null = "Nachname der Lehrkraft: Die zweite Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.";
		if (nachnameOhneZusatz.length > 1 && JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(1))) {
			this.addFehler(5, fehlertext5);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn14LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn14LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn14LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn14LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn14LehrerStammdatenNachname(obj: unknown): ValidatorLsn14LehrerStammdatenNachname {
	return obj as ValidatorLsn14LehrerStammdatenNachname;
}
