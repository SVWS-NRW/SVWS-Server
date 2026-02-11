import { JavaObject } from '../../../java/lang/JavaObject';
import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly geburtsdatum: Supplier<DateManager>;

	/**
	 * Die ID des Schuljahresabschnittes
	 */
	private readonly idSchuljahresabschnitt: Supplier<number>;

	/**
	 * Das Rechtsverhältnis
	 */
	private readonly rechtsverhaeltnis: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param rechtsverhaeltnis       das Rechtsverhältnis
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, rechtsverhaeltnis: Supplier<string>, geburtsdatum: Supplier<DateManager>, kontext: ValidatorKontext) {
		super(kontext);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.rechtsverhaeltnis = rechtsverhaeltnis;
		this.geburtsdatum = geburtsdatum;
	}

	protected pruefe(): boolean {
		const schuljahresabschnitt: Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.idSchuljahresabschnitt.get());
		if (schuljahresabschnitt === null)
			return false;
		const schuljahr: number = schuljahresabschnitt.schuljahr;
		const rv: LehrerRechtsverhaeltnis | null = LehrerRechtsverhaeltnis.getBySchluessel(this.rechtsverhaeltnis.get());
		if (JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.W))) {
			const minJahr: number = schuljahr - 50;
			const maxJahr: number = schuljahr - 18;
			if (!this.geburtsdatum.get().istInJahren(minJahr, maxJahr)) {
				this.addFehler(3, "Der Wert für das Geburtsjahr sollte bei Lehramtsanwärtern/-innen (Rechtsverhältnis = W) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
