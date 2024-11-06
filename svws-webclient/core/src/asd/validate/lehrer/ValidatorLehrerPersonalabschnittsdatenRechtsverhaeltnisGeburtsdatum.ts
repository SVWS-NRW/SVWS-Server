import { DateManager } from '../../../asd/validate/DateManager';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum extends Validator<LehrerPersonalabschnittsdaten> {

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
		super(daten, kontext);
		this.geburtsdatum = geburtsdatum;
	}

	protected pruefe() : boolean {
		const schuljahresabschnitt : Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.daten().idSchuljahresabschnitt);
		if (schuljahresabschnitt === null)
			return false;
		const schuljahr : number = schuljahresabschnitt.schuljahr;
		const rv : LehrerRechtsverhaeltnis | null = LehrerRechtsverhaeltnis.getBySchluessel(this.daten().rechtsverhaeltnis);
		if (rv === null) {
			this.addFehler("Kein Wert im Feld 'rechtsverhaeltnis'.");
			return false;
		}
		let success : boolean = true;
		switch (rv) {
			case LehrerRechtsverhaeltnis.L: {
				const minJahr : number = schuljahr - ((schuljahr <= 2023) ? 65 : ((schuljahr <= 2030) ? 66 : 67));
				const maxJahr : number = schuljahr - 27;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler("Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Lebenszeit (Rechtsverhältnis = L) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
			case LehrerRechtsverhaeltnis.P: {
				const minJahr : number = schuljahr - 55;
				const maxJahr : number = schuljahr - 20;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler("Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Probe (Rechtsverhältnis = P) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
			case LehrerRechtsverhaeltnis.W: {
				const minJahr : number = schuljahr - 50;
				const maxJahr : number = schuljahr - 18;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler("Der Wert für das Geburtsjahr sollte bei Lehramtsanwärtern/-innen (Rechtsverhältnis = W) zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
			default: {
				const minJahr : number = schuljahr - 80;
				const maxJahr : number = schuljahr - 18;
				if (!this.geburtsdatum.istInJahren(minJahr, maxJahr)) {
					this.addFehler("Der Wert für das Geburtsjahr sollte bei sonstigen Rechtsverhältnissen zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
				break;
			}
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum(obj : unknown) : ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum {
	return obj as ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum;
}
