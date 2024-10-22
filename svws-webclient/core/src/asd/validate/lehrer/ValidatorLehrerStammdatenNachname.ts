import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdatenNachname extends Validator<LehrerStammdaten> {

	private readonly zusaetze : JavaSet<string> = java_util_Set_of("de", "te", "zu", "da", "von", "van", "vom", "thor");

	private readonly zusaetzeZweiteilig : JavaSet<string> = java_util_Set_of("de la");


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(daten, kontext);
	}

	/**
	 * Entfernt ggf. die in "zusaetze" oder "zusaetzeZweiteilig" aufgeführten Zusätze, welche dem Nachnamen
	 * vorangestellt sein können. Diese Methode wird zur Prüfung des Anfangsbuchstabens des Nachnamens
	 * verwendet.
	 *
	 * @param nachname   der Nachname
	 *
	 * @return der Nachname mit ggf. entferntem Vornamen
	 */
	private getOhneZusatz(nachname : string) : string {
		const teile : Array<string> = nachname.split(" ", 3);
		if ((teile.length === 3) && (this.zusaetzeZweiteilig.contains(teile[0] + " " + teile[1])))
			return teile[2];
		if ((teile.length === 3) && (this.zusaetze.contains(teile[0])))
			return teile[1] + " " + teile[2];
		if ((teile.length === 2) && (this.zusaetze.contains(teile[0])))
			return teile[1];
		return nachname;
	}

	protected pruefe() : boolean {
		const nachname : string | null = this.daten().nachname;
		if ((nachname === null) || (nachname.length === 0)) {
			this.addFehler("Kein Wert im Feld 'nachname'.");
			return false;
		}
		const success : boolean = true;
		if (nachname.startsWith("") || nachname.startsWith("\t"))
			this.addFehler("Nachname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
		const nachnameOhneZusatz : string = this.getOhneZusatz(nachname);
		if (!JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(0)))
			this.addFehler("Nachname der Lehrkraft: Die erste Stelle des Nachnamens muss - ggf. im Anschluss an einen Namenszusatz, wie z.B. \"von\" -  mit einem Großbuchstaben besetzt sein.");
		if ((nachnameOhneZusatz.length > 1) && JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(1)))
			this.addFehler("Die zweite Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
		if ((nachnameOhneZusatz.length > 2) && JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(2)) && !java_util_Set_of("A'", "D'", "M'", "O'", "Mc").contains(nachnameOhneZusatz.substring(0, 2)))
			this.addFehler("Nachname der Lehrkraft: Die dritte Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenNachname(obj : unknown) : ValidatorLehrerStammdatenNachname {
	return obj as ValidatorLehrerStammdatenNachname;
}
