import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../core/types/schule/SchulabschlussAllgemeinbildend';
import { JavaBoolean, cast_java_lang_Boolean } from '../../java/lang/JavaBoolean';
import { BKAnlageAFach, cast_de_nrw_schule_svws_core_abschluss_bk_a_BKAnlageAFach } from '../../core/abschluss/bk/a/BKAnlageAFach';
import { AbschlussErgebnisBerufsbildend, cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnisBerufsbildend } from '../../core/data/abschluss/AbschlussErgebnisBerufsbildend';
import { JavaDouble, cast_java_lang_Double } from '../../java/lang/JavaDouble';
import { BKAnlageAFaecher, cast_de_nrw_schule_svws_core_abschluss_bk_a_BKAnlageAFaecher } from '../../core/abschluss/bk/a/BKAnlageAFaecher';

export class AbschlussManagerBerufsbildend extends JavaObject {


	public constructor() {
		super();
	}

	/**
	 * Erzeugt ein Ergebnis der Abschlussberechnung unter Angabe, ob dieser erworben
	 * wurde. Die Liste der Nachprüfungsfächer ist leer und ein Log ist nicht zugeordnet.
	 *
	 * @param hatBSA                      ist der Berufsschulabschluss erreicht
	 * @param note                        Note des Abschlusses
	 * @param hatBA                       ist der Berufsabschluss erreicht
	 * @param abschlussAllgemeinbildend   der allgemeinbildende Abschluss
	 *
	 * @return das Ergebnis der Abschlussberechnung
	 */
	public static getErgebnis(hatBSA : boolean, note : number, hatBA : boolean | null, abschlussAllgemeinbildend : SchulabschlussAllgemeinbildend | null) : AbschlussErgebnisBerufsbildend {
		let ergebnis : AbschlussErgebnisBerufsbildend = new AbschlussErgebnisBerufsbildend();
		ergebnis.hatBSA = hatBSA;
		ergebnis.note = note;
		ergebnis.hatBA = hatBA;
		ergebnis.abschlussAllgemeinbildend = (abschlussAllgemeinbildend === null) ? null : abschlussAllgemeinbildend.toString();
		ergebnis.log = null;
		return ergebnis;
	}

	/**
	 * Berechnet den Notendurchschnitt aller Fächer
	 *
	 * @param abschluss_faecher   die Fächer für die Abschlussberechnung
	 *
	 * @return der Notendurchschnitt oder NaN im Fehlerfall
	 */
	public static getDurchschnitt(abschluss_faecher : BKAnlageAFaecher) : number {
		if ((abschluss_faecher.faecher === null) || (abschluss_faecher.faecher.size() === 0))
			return NaN;
		let sum : number = 0;
		for (let fach of abschluss_faecher.faecher) {
			sum += fach.note;
		}
		return (sum as number) / abschluss_faecher.faecher.size();
	}

	/**
	 * Berechnet die Anzahl der Defizite
	 *
	 * @param abschluss_faecher   die Fächer für die Abschlussberechnung
	 *
	 * @return die Anzahl der Defizite oder -1 im Fehlerfall
	 */
	public static getAnzahlDefizite(abschluss_faecher : BKAnlageAFaecher) : number {
		if ((abschluss_faecher.faecher === null) || (abschluss_faecher.faecher.size() === 0))
			return -1;
		let sum : number = 0;
		for (let fach of abschluss_faecher.faecher) {
			if (fach.note >= 5)
				sum++;
		}
		return sum;
	}

	/**
	 * Berechnet die Anzahl der Note Ungenügend
	 *
	 * @param abschluss_faecher   die Fächer für die Abschlussberechnung
	 *
	 * @return die Anzahl der Note Ungenügend oder -1 im Fehlerfall
	 */
	public static getAnzahlUngenuegend(abschluss_faecher : BKAnlageAFaecher) : number {
		if ((abschluss_faecher.faecher === null) || (abschluss_faecher.faecher.size() === 0))
			return -1;
		let sum : number = 0;
		for (let fach of abschluss_faecher.faecher) {
			if (fach.note >= 6)
				sum++;
		}
		return sum;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.AbschlussManagerBerufsbildend'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_AbschlussManagerBerufsbildend(obj : unknown) : AbschlussManagerBerufsbildend {
	return obj as AbschlussManagerBerufsbildend;
}
