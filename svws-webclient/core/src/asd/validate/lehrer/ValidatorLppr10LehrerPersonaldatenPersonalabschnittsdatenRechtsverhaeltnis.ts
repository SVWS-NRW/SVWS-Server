import { JavaObject } from '../../../java/lang/JavaObject';
import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly _geburtsdatum: Supplier<DateManager>;

	/**
	 * Die ID des Schuljahresabschnittes
	 */
	private readonly _idSchuljahresabschnitt: Supplier<number>;

	/**
	 * Das Rechtsverhältnis
	 */
	private readonly _rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param rechtsverhaeltnisNotNull        das Rechtsverhältnis
	 * @param geburtsdatum             das Geburtsdatum des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, rechtsverhaeltnisNotNull: Supplier<LehrerRechtsverhaeltnis>, geburtsdatum: Supplier<DateManager>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		this._rechtsverhaeltnis = rechtsverhaeltnisNotNull;
		this._geburtsdatum = geburtsdatum;
	}

	protected pruefe(): boolean {
		const schuljahresabschnitt: Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this._idSchuljahresabschnitt.get());
		if (schuljahresabschnitt === null) {
			return false;
		}
		const schuljahr: number = schuljahresabschnitt.schuljahr;
		if (JavaObject.equalsTranspiler(this._rechtsverhaeltnis.get(), (LehrerRechtsverhaeltnis.L))) {
			const minJahr: number = schuljahr - ((schuljahr <= 2023) ? 65 : ((schuljahr <= 2030) ? 66 : 67));
			const maxJahr: number = schuljahr - 27;
			if (!this._geburtsdatum.get().istInJahren(minJahr, maxJahr)) {
				this.addFehler(1, "Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Lebenszeit (Rechtsverhältnis = L) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
