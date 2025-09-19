import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

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
	}

	protected pruefe() : boolean {
		const vorname : string | null = this.daten.vorname;
		if ((vorname === null) || (vorname.length === 0)) {
			this.addFehler(0, "Vorname der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		if (JavaString.isBlank(vorname.trim())) {
			this.addFehler(1, "Vorname der Lehrkraft: Der Vorname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		let success : boolean = true;
		if ((this.daten.vorname.length === 1)) {
			this.addFehler(2, "Vorname der Lehrkraft: Der Vorname besteht aus nur einem Zeichen. Bitte überprüfen sie ihre Angaben.");
			success = false;
		}
		if (vorname.startsWith(" ") || vorname.startsWith("\t")) {
			this.addFehler(3, "Vorname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
			success = false;
		}
		if (!JavaCharacter.isUpperCase(vorname.charAt(0))) {
			this.addFehler(4, "Vorname der Lehrkraft: Die erste Stelle des Vornamens muss mit einem Großbuchstaben besetzt sein.");
			success = false;
		}
		if ((vorname.length > 1) && JavaCharacter.isUpperCase(vorname.charAt(1))) {
			this.addFehler(5, "Vorname der Lehrkraft: Die zweite Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if ((vorname.length > 2) && JavaCharacter.isUpperCase(vorname.charAt(2))) {
			this.addFehler(6, "Vorname der Lehrkraft: Die dritte Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if (JavaString.contains(vorname, " -") || JavaString.contains(vorname, "- ")) {
			this.addFehler(7, "Vorname der Lehrkraft: Der Vorname enthält überflüssige Leerzeichen vor und/oder nach dem Bindestrich.");
			success = false;
		}
		const nLower : string | null = vorname.toLowerCase();
		if (nLower.startsWith("frau ") || nLower.startsWith("herr ")) {
			this.addFehler(8, "Vorname der Lehrkraft: Die Anrede (Frau oder Herr) gehört nicht in den Vornamen.");
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
