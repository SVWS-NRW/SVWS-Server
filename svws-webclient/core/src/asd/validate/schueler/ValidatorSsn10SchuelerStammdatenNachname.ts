import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsn10SchuelerStammdatenNachname extends Validator {

	private readonly _nachname: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator für den Nachnamen.
	 *
	 * @param nachname der Supplier für den Nachnamen
	 * @param kontext  der Validierungskontext
	 */
	public constructor(nachname: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this._nachname = nachname;
	}

	protected pruefe(): boolean {
		const nachname: string | null = this._nachname.get();
		if (!JavaString.isEmpty(nachname) && JavaString.isEmpty(nachname.trim())) {
			this.addFehler(0, "Nachname des Schülers: Der Nachname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsn10SchuelerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsn10SchuelerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsn10SchuelerStammdatenNachname>('de.svws_nrw.asd.validate.schueler.ValidatorSsn10SchuelerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsn10SchuelerStammdatenNachname(obj: unknown): ValidatorSsn10SchuelerStammdatenNachname {
	return obj as ValidatorSsn10SchuelerStammdatenNachname;
}
