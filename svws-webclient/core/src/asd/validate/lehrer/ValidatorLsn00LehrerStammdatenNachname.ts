import { ValidatorLsn01LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn01LehrerStammdatenNachname';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn00LehrerStammdatenNachname extends Validator {

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
		this._validatoren.add(new ValidatorLsn01LehrerStammdatenNachname(daten, kontext));
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const nachname: string | null = this.daten.nachname;
		success = this.exec(0, { getAsBoolean: () => (nachname === null) || (nachname.length === 0) }, "Nachname der Lehrkraft: Kein Wert vorhanden.");
		if (!success)
			return false;
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn00LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsn00LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLsn00LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn00LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn00LehrerStammdatenNachname(obj: unknown): ValidatorLsn00LehrerStammdatenNachname {
	return obj as ValidatorLsn00LehrerStammdatenNachname;
}
