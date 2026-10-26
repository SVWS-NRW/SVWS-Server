import { ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung } from '../../../asd/validate/lehrer/ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplfLehrerPersonaldatenLehramtFachrichtung extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idFachrichtung           die Katalog-ID der Fachrichtung
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, idFachrichtung: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => kontext.getSchuljahrBySchuljahresabschnitt(this.getNotNullSupplierLong(idSchuljahresabschnitt)) };
		this._validatoren.add(new ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung(schuljahr, idFachrichtung, kontext));
	}

	protected pruefe(): boolean {
		const sja: Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.getNotNullSupplierLong(this._idSchuljahresabschnitt).get());
		if (sja === null) {
			this.addFehler(0, "Dem Lehrer ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplfLehrerPersonaldatenLehramtFachrichtung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplfLehrerPersonaldatenLehramtFachrichtung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplfLehrerPersonaldatenLehramtFachrichtung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplfLehrerPersonaldatenLehramtFachrichtung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplfLehrerPersonaldatenLehramtFachrichtung(obj: unknown): ValidatorLplfLehrerPersonaldatenLehramtFachrichtung {
	return obj as ValidatorLplfLehrerPersonaldatenLehramtFachrichtung;
}
