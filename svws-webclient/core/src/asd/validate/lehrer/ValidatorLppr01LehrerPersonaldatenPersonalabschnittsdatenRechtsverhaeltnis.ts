import { ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Das Rechtsverhältnis
	 */
	private readonly _idRechtsverhaeltnis: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param idRechtsverhaeltnis      die ID des Rechtsverhältnis
	 * @param geburtsdatum             das Geburtsdatum des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, idRechtsverhaeltnis: Supplier<number | null>, geburtsdatum: Supplier<DateManager>, kontext: ValidatorKontext) {
		super(kontext);
		this._idRechtsverhaeltnis = idRechtsverhaeltnis;
		const rechtsverhaeltnisNotNull: Supplier<LehrerRechtsverhaeltnis> = { get: () => LehrerRechtsverhaeltnis.data().getWertByID(this.getNotNullSupplierLong(idRechtsverhaeltnis).get()) };
		this._validatoren.add(new ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
	}

	protected pruefe(): boolean {
		const idRechtsverhaeltnis: number | null = this._idRechtsverhaeltnis.get();
		const rv: LehrerRechtsverhaeltnis | null = (idRechtsverhaeltnis === null) ? null : LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis);
		if (rv === null) {
			this.addFehler(0, "Kein gültiger Wert im Feld 'rechtsverhaeltnis'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
