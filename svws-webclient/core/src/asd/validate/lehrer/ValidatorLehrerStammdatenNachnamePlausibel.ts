import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdatenNachnamePlausibel extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten : LehrerStammdaten;

	/**
	 *   Namenszusätze in Europa:
	 *   Ist noch mit IT.NRW abzustimmen, was davon umgesetzt werden soll.
	 *   Deutschsprachiger Raum : von, zu, vom, vonder, zum, zur
	 *   Niederlande : van, van de, van der, van den, de, ten
	 *   Belgien : de, de la, de l’, van, van der,
	 *   Frankreich : de, du, des, de la, le, la
	 *   Spanien : de, del, de la, los, las, y
	 *   Italien : di, della, del, dei, da
	 *   Portugal : de, da, do, dos, das
	 *   Großbritannien : of, ap (walisisch), fitz (anglo-normannisch),
	 *   Skandinavien : af, von, son, dotter
	 *   Polen : z, de
	 *   Ungarn : de, von, fi
	 *   Russland und Osteuropa : von, de
	 *
	 *   Gesamt:
	 *   1-teilig: af, ap, da, das, de, dei, del, della, des, di, do, dos, dotter, du, fi, fitz, la, las, le, los, of, son, ten, van, vom, von, vonder, y, z, zu, zum, zur
	 *   2-teilig: de la, de l’, van de, van den, van der
	 */
	private readonly zusaetze : JavaSet<string> = java_util_Set_of("de", "te", "zu", "da", "von", "van", "vom", "thor");

	private readonly zusaetzeZweiteilig : JavaSet<string> = java_util_Set_of("de la");


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
		const nachname : string | null = this.daten.nachname;
		if ((nachname === null) || (nachname.length === 0) || (JavaString.isBlank(nachname)))
			return true;
		let success : boolean = true;
		if (nachname.startsWith(" ") || nachname.startsWith("\t")) {
			this.addFehler("Nachname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
			success = false;
		}
		const nachnameOhneZusatz : string = this.getOhneZusatz(nachname);
		if (!JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(0))) {
			this.addFehler("Nachname der Lehrkraft: Die erste Stelle des Nachnamens muss - ggf. im Anschluss an einen Namenszusatz, wie z.B. \"von\" -  mit einem Großbuchstaben besetzt sein.");
			success = false;
		}
		if ((nachnameOhneZusatz.length > 1) && JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(1))) {
			this.addFehler("Die zweite Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if ((nachnameOhneZusatz.length > 2) && JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(2)) && !java_util_Set_of("A'", "D'", "M'", "O'", "Mc").contains(nachnameOhneZusatz.substring(0, 2))) {
			this.addFehler("Nachname der Lehrkraft: Die dritte Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnamePlausibel';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnamePlausibel', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenNachnamePlausibel>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnamePlausibel');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenNachnamePlausibel(obj : unknown) : ValidatorLehrerStammdatenNachnamePlausibel {
	return obj as ValidatorLehrerStammdatenNachnamePlausibel;
}
