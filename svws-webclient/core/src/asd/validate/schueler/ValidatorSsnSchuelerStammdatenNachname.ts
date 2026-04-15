import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorSsn00SchuelerStammdatenNachname } from '../../../asd/validate/schueler/ValidatorSsn00SchuelerStammdatenNachname';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsnSchuelerStammdatenNachname extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param nachname         der Nachname des Schülers
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(nachname: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsn00SchuelerStammdatenNachname(nachname, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsnSchuelerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsnSchuelerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsnSchuelerStammdatenNachname>('de.svws_nrw.asd.validate.schueler.ValidatorSsnSchuelerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsnSchuelerStammdatenNachname(obj: unknown): ValidatorSsnSchuelerStammdatenNachname {
	return obj as ValidatorSsnSchuelerStammdatenNachname;
}
