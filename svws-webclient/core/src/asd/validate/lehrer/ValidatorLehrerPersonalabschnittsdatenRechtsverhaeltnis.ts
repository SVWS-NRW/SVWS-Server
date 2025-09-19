import { DateManager } from '../../../asd/validate/DateManager';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Die Lehrer-Personalabschnittdaten
	 */
	private readonly daten : LehrerPersonalabschnittsdaten;

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly geburtsdatum : DateManager;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten          die Personalabschnittsdaten für den Validator
	 * @param geburtsdatum   das Geburtsdatum des Lehrers
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(daten : LehrerPersonalabschnittsdaten, geburtsdatum : DateManager, kontext : ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this.geburtsdatum = geburtsdatum;
	}

	/**
	 * Prüfe das Feld Rechtsverhältnis in Bezug auf das Geburtsdatum des Lehrers.
	 *
	 * @param rv          das Rechtsverhältnis
	 * @param schuljahr   das Schuljahr der Prüfung
	 *
	 * @return true, wenn die Prüfung erfolgreich war, und ansonsten false
	 */
	private pruefeGeburtsdatum(rv : LehrerRechtsverhaeltnis | null, schuljahr : number) : boolean {
		let success : boolean = true;
		switch (rv) {
			case LehrerRechtsverhaeltnis.L: {
				const minJahr : number = schuljahr - ((schuljahr <= 2023) ? 65 : ((schuljahr <= 2030) ? 66 : 67));
				const maxJahr : number = schuljahr - 27;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler(1, "Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Lebenszeit (Rechtsverhältnis = L) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
			case LehrerRechtsverhaeltnis.P: {
				const minJahr : number = schuljahr - 55;
				const maxJahr : number = schuljahr - 20;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler(2, "Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Probe (Rechtsverhältnis = P) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
			case LehrerRechtsverhaeltnis.W: {
				const minJahr : number = schuljahr - 50;
				const maxJahr : number = schuljahr - 18;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler(3, "Der Wert für das Geburtsjahr sollte bei Lehramtsanwärtern/-innen (Rechtsverhältnis = W) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
			default: {
				const minJahr : number = schuljahr - 80;
				const maxJahr : number = schuljahr - 18;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler(4, "Der Wert für das Geburtsjahr sollte bei sonstigen Rechtsverhältnissen zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
		}
		return success;
	}

	protected pruefe() : boolean {
		const schuljahresabschnitt : Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.daten.idSchuljahresabschnitt);
		if (schuljahresabschnitt === null)
			return false;
		const schuljahr : number = schuljahresabschnitt.schuljahr;
		const rv : LehrerRechtsverhaeltnis | null = LehrerRechtsverhaeltnis.getBySchluessel(this.daten.rechtsverhaeltnis);
		if (rv === null) {
			this.addFehler(0, "Kein Wert im Feld 'rechtsverhaeltnis'.");
			return false;
		}
		return this.pruefeGeburtsdatum(rv, schuljahr);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis(obj : unknown) : ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis;
}
