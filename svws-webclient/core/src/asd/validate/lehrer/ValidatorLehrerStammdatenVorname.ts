import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { ValidatorLehrerStammdatenVornameAnredeFehlerhaft } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenVornameAnredeFehlerhaft';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLehrerStammdatenVornameNurEinstellig } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenVornameNurEinstellig';

export class ValidatorLehrerStammdatenVorname extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten : LehrerStammdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLehrerStammdatenVornameAnredeFehlerhaft(daten, kontext));
		this._validatoren.add(new ValidatorLehrerStammdatenVornameNurEinstellig(daten, kontext));
	}

	protected pruefe() : boolean {
		const vorname : string | null = this.daten.vorname;
		if ((vorname === null) || (vorname.length === 0)) {
			this.addFehler("Kein Wert im Feld 'vorname'.");
			return false;
		}
		let success : boolean = true;
		if (vorname.startsWith(" ") || vorname.startsWith("\t")) {
			this.addFehler("Vorname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
			success = false;
		}
		if (!JavaCharacter.isUpperCase(vorname.charAt(0))) {
			this.addFehler("Vorname der Lehrkraft: Die erste Stelle des Vornamens muss mit einem Großbuchstaben besetzt sein.");
			success = false;
		}
		if ((vorname.length > 1) && JavaCharacter.isUpperCase(vorname.charAt(1))) {
			this.addFehler("Vorname der Lehrkraft: Die zweite Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if ((vorname.length > 2) && JavaCharacter.isUpperCase(vorname.charAt(2))) {
			this.addFehler("Vorname der Lehrkraft: Die dritte Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenVorname(obj : unknown) : ValidatorLehrerStammdatenVorname {
	return obj as ValidatorLehrerStammdatenVorname;
}
