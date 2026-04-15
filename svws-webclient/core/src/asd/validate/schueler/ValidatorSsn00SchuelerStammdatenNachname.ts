import { ValidatorSsn10SchuelerStammdatenNachname } from '../../../asd/validate/schueler/ValidatorSsn10SchuelerStammdatenNachname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsn00SchuelerStammdatenNachname extends Validator {

	private readonly _nachname: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator für den Nachnamen.
	 *
	 * @param nachname der Supplier für den Nachnamen
	 * @param kontext  der Validierungskontext
	 */
	public constructor(nachname: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._nachname = nachname;
		this._validatoren.add(new ValidatorSsn10SchuelerStammdatenNachname(nachname, kontext));
	}

	protected pruefe(): boolean {
		const nachname: string | null = this._nachname.get();
		if ((nachname === null) || JavaString.isBlank(nachname)) {
			this.addFehler(0, "Nachname des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsn00SchuelerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsn00SchuelerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsn00SchuelerStammdatenNachname>('de.svws_nrw.asd.validate.schueler.ValidatorSsn00SchuelerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsn00SchuelerStammdatenNachname(obj: unknown): ValidatorSsn00SchuelerStammdatenNachname {
	return obj as ValidatorSsn00SchuelerStammdatenNachname;
}
