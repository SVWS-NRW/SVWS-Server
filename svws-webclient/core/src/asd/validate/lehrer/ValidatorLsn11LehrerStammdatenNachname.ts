import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn11LehrerStammdatenNachname extends Validator {

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
		const nachname: string | null = this.daten.get();
		if (nachname.length === 1) {
			this.addFehler(1, "Nachname der Lehrkraft: Der Nachname besteht aus nur einem Zeichen. Bitte überprüfen sie Ihre Angaben.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn11LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn11LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn11LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn11LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn11LehrerStammdatenNachname(obj: unknown): ValidatorLsn11LehrerStammdatenNachname {
	return obj as ValidatorLsn11LehrerStammdatenNachname;
}
