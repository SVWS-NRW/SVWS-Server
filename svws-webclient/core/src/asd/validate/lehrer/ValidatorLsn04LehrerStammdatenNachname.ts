import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { NamensManager } from '../../../asd/validate/NamensManager';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn04LehrerStammdatenNachname extends Validator {

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
		if (!JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(0))) {
			this.addFehler(4, "Nachname der Lehrkraft: Die erste Stelle des Nachnamens muss - ggf. im Anschluss an einen Namenszusatz, wie z.B. \"von\" -  mit einem Großbuchstaben besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn04LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsn04LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn04LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn04LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn04LehrerStammdatenNachname(obj: unknown): ValidatorLsn04LehrerStammdatenNachname {
	return obj as ValidatorLsn04LehrerStammdatenNachname;
}
