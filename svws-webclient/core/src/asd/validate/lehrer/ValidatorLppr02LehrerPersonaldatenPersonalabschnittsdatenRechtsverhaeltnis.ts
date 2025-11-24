import { JavaObject } from '../../../java/lang/JavaObject';
import { DateManager } from '../../../asd/validate/DateManager';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Die Lehrer-Personalabschnittdaten
	 */
	private readonly daten: LehrerPersonalabschnittsdaten;

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly geburtsdatum: DateManager;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten          die Personalabschnittsdaten für den Validator
	 * @param geburtsdatum   das Geburtsdatum des Lehrers
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, geburtsdatum: DateManager, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this.geburtsdatum = geburtsdatum;
	}

	protected pruefe(): boolean {
		const schuljahresabschnitt: Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.daten.idSchuljahresabschnitt);
		if (schuljahresabschnitt === null)
			return false;
		const schuljahr: number = schuljahresabschnitt.schuljahr;
		const rv: LehrerRechtsverhaeltnis | null = LehrerRechtsverhaeltnis.getBySchluessel(this.daten.rechtsverhaeltnis);
		if (JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.P))) {
			const minJahr: number = schuljahr - 55;
			const maxJahr: number = schuljahr - 20;
			if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
				this.addFehler(2, "Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Probe (Rechtsverhältnis = P) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
