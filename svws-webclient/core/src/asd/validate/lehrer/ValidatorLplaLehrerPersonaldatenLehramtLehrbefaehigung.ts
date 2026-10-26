import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idLehrbefaehigung        eine idLehrbefaehigung des Lehrers
	 * @param lehrerLehramt            das Lehramt des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, idLehrbefaehigung: Supplier<number | null>, lehrerLehramt: Supplier<LehrerLehramt | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => kontext.getSchuljahrBySchuljahresabschnitt(this.getNotNullSupplierLong(idSchuljahresabschnitt)) };
		this._validatoren.add(new ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung(schuljahr, idLehrbefaehigung, lehrerLehramt, kontext));
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
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung;
}
