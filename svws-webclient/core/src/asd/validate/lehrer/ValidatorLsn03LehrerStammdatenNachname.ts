import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn03LehrerStammdatenNachname extends Validator {

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
		if (nachname.startsWith(" ") || nachname.startsWith("\t")) {
			this.addFehler(3, "Nachname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn03LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn03LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn03LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn03LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn03LehrerStammdatenNachname(obj: unknown): ValidatorLsn03LehrerStammdatenNachname {
	return obj as ValidatorLsn03LehrerStammdatenNachname;
}
