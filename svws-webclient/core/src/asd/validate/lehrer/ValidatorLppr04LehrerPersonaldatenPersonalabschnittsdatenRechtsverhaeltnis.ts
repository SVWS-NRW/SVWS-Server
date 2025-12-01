import { JavaObject } from '../../../java/lang/JavaObject';
import { DateManager } from '../../../asd/validate/DateManager';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

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
		if (!JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.L)) && !JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.P)) && !JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.W))) {
			const minJahr: number = schuljahr - 80;
			const maxJahr: number = schuljahr - 18;
			if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
				this.addFehler(4, "Der Wert für das Geburtsjahr sollte bei sonstigen Rechtsverhältnissen zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
