import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn16LehrerStammdatenNachname extends Validator {

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
		if (JavaString.contains(nachname, " -") || JavaString.contains(nachname, "- ")) {
			this.addFehler(7, "Nachname der Lehrkraft: Der Nachname enthält überflüssige Leerzeichen vor und/oder nach dem Bindestrich.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn16LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn16LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn16LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn16LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn16LehrerStammdatenNachname(obj: unknown): ValidatorLsn16LehrerStammdatenNachname {
	return obj as ValidatorLsn16LehrerStammdatenNachname;
}
