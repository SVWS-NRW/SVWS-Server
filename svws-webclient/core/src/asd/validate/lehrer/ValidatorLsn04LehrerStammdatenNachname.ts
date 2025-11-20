import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { JavaCharacter } from '../../../java/lang/JavaCharacter';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn04LehrerStammdatenNachname extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: LehrerStammdaten;

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
	private readonly zusaetze: JavaSet<string> = java_util_Set_of("de", "te", "zu", "da", "von", "van", "vom", "thor");

	private readonly zusaetzeZweiteilig: JavaSet<string> = java_util_Set_of("de la");


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerStammdaten, kontext: ValidatorKontext) {
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
	private getOhneZusatz(nachname: string): string {
		const teile: Array<string> = nachname.split(" ", 3);
		if ((teile.length === 3) && (this.zusaetzeZweiteilig.contains(teile[0] + " " + teile[1])))
			return teile[2];
		if ((teile.length === 3) && (this.zusaetze.contains(teile[0])))
			return teile[1] + " " + teile[2];
		if ((teile.length === 2) && (this.zusaetze.contains(teile[0])))
			return teile[1];
		return nachname;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const nachname: string | null = this.daten.nachname;
		const nachnameOhneZusatz: string = this.getOhneZusatz(nachname);
		if (!this.exec(4, { getAsBoolean: () => !JavaCharacter.isUpperCase(nachnameOhneZusatz.charAt(0)) }, "Nachname der Lehrkraft: Die erste Stelle des Nachnamens muss - ggf. im Anschluss an einen Namenszusatz, wie z.B. \"von\" -  mit einem Großbuchstaben besetzt sein."))
			success = false;
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn04LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsn04LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLsn04LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn04LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn04LehrerStammdatenNachname(obj: unknown): ValidatorLsn04LehrerStammdatenNachname {
	return obj as ValidatorLsn04LehrerStammdatenNachname;
}
