import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLsn10LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn10LehrerStammdatenNachname';

export class ValidatorLsn00LehrerStammdatenNachname extends Validator {

	/**
	 * Der Lehrer-Nachname
	 */
	private readonly daten: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsn10LehrerStammdatenNachname(this.getNotNullSupplier(daten), kontext));
	}

	protected pruefe(): boolean {
		const nachname: string | null = this.daten.get();
		if ((nachname === null) || (JavaString.isEmpty(nachname))) {
			this.addFehler(0, "Nachname der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn00LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsn00LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn00LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn00LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn00LehrerStammdatenNachname(obj: unknown): ValidatorLsn00LehrerStammdatenNachname {
	return obj as ValidatorLsn00LehrerStammdatenNachname;
}
