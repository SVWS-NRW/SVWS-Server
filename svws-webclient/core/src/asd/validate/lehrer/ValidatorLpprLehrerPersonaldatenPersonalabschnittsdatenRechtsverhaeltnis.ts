import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param idRechtsverhaeltnis      das Rechtsverhältnis
	 * @param geburtsdatum             das Geburtsdatum des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, idStaatsangehoerigkeit: Supplier<number | null>, idRechtsverhaeltnis: Supplier<number | null>, geburtsdatum: Supplier<DateManager>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => kontext.getSchuljahrBySchuljahresabschnitt(this.getNotNullSupplierLong(idSchuljahresabschnitt)) };
		this._validatoren.add(new ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(schuljahr, idStaatsangehoerigkeit, idRechtsverhaeltnis, geburtsdatum, kontext));
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
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
