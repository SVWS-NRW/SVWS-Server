import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
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
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param idRechtsverhaeltnis      die ID des Rechtsverhältnis
	 * @param geburtsdatum             das Geburtsdatum des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, idStaatsangehoerigkeit: Supplier<number | null>, idRechtsverhaeltnis: Supplier<number>, geburtsdatum: Supplier<DateManager>, kontext: ValidatorKontext) {
		super(kontext);
		this._idRechtsverhaeltnis = idRechtsverhaeltnis;
		this._validatoren.add(new ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, idStaatsangehoerigkeit, idRechtsverhaeltnis, geburtsdatum, kontext));
	}

	public pruefe(): boolean {
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
