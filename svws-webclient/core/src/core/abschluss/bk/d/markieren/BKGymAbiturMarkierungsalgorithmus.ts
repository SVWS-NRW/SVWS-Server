import { JavaObject } from '../../../../../java/lang/JavaObject';
import { BKGymAbiturMarkierungsalgorithmusErgebnis } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusErgebnis';
import { BKGymAbiturMarkierungsregelFach } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelFach';
import { BKGymAbiturdatenManager } from '../../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { BKGymAbiturMarkierungsVarianten } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVarianten';
import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { BKGymAbiturMarkierungsregelFremdsprachePruefeNeue } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelFremdsprachePruefeNeue';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { BKGymAbiturMarkierungsregelMaxAnzahlkurse } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelMaxAnzahlkurse';
import { BKGymAbiturMarkierungsregelKopie } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelKopie';
import { BKGymAbiturMarkierungsregelMinAnzahlkurse } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelMinAnzahlkurse';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import { BKGymAbiturMarkierungsregelFachgruppe } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelFachgruppe';
import { BKGymAbiturMarkierungsregelFremdspracheErste } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelFremdspracheErste';
import { BKGymAbiturMarkierungsregelFremdspracheZweiteNeu } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelFremdspracheZweiteNeu';
import { BKGymAbiturMarkierungsregelDefizitePruefeLK } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelDefizitePruefeLK';
import { BKGymAbiturMarkierungsregelMinAnzahlpunkte } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelMinAnzahlpunkte';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsregelAufgabenfeld } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelAufgabenfeld';
import { BKGymAbiturMarkierungsregelPruefeVorraussetzung } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelPruefeVorraussetzung';
import { BKGymAbiturMarkierungsregelKursart } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelKursart';
import { BKGymAbiturMarkierungsregelFremdspracheZweiteNur } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelFremdspracheZweiteNur';
import { BKGymAbiturMarkierungsregelDefizite } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregelDefizite';

export class BKGymAbiturMarkierungsalgorithmus extends JavaObject {

	/**
	 * Der Abiturdaten-Manager
	 */
	public readonly manager: BKGymAbiturdatenManager;

	/**
	 * Die verschiedenen Markierungsergebnisse, aus denen das beste Ergebnis gewählt wird.
	 */
	private readonly ergebnisse: BKGymAbiturMarkierungsVarianten;

	/**
	 * der Regelsatz
	 */
	private static readonly regelsatz: List<BKGymAbiturMarkierungsregel> = ArrayList.of(cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelPruefeVorraussetzung("M_MOEGLICH", "Prüfe, ob Voraussetzungen zum Markieren gegeben sind. Alle sechs Halbjahre bewertet,", "dass Kurse auch aus der EF herangezogen werden und die Halbjahre gewertet sein müssen.")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelKursart("LK1", 4, "M_LK1", "Markiere vier Kurse des ersten Leistungskurses,", "APO-BK AnlageD § 15 Abs. 2 Nr. 1 (LK1)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelKursart("LK2", 4, "M_LK2", "Markiere vier Kurse des zweiten Leistungskurses,", "APO-BK AnlageD § 15 Abs. 2 Nr. 1 (LK2)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelKursart("AB3", 4, "M_AB3", "Markiere vier Kurse des dritten Abiturfachs,", "APO-BK AnlageD § 15 Abs. 3 Nr. 1 (AB3)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelKursart("AB4", 4, "M_AB4", "Markiere vier Kurse des vierten Abiturfachs,", "APO-BK AnlageD § 15 Abs. 3 Nr. 1 (AB4)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFach("Deutsch", 4, "M_FA_D", "Markiere vier Kurse im Fach Deutsch,", "APO-BK AnlageD § 15 Abs. 3 Nr. 2 a)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFach("Mathematik", 4, "M_FA_M", "Markiere vier Kurse im Fach Mathematik,", "APO-BK AnlageD § 15 Abs. 3 Nr. 2 c)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFach("Gesellschaftslehre mit Geschichte", 2, "M_FA_GMG", "Markiere zwei Kurse im Fach Gesellschaftslehre mit Geschichte,", "APO-BK AnlageD § 15 Abs. 3 Nr. 2 e) 2. Teil")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelAufgabenfeld("II", 4, "M_AF_II", "Markiere vier Kurse im gesellschaftswissenschaftlichen Aufgabenfeld,", "APO-BK AnlageD § 15 Abs. 3 Nr. 2 e) 1. Teil")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFachgruppe("NW", 4, "EF.1", "M_FG_NW", "Markiere vier Kurse einer ab EF.1 belegten Naturwissenschaft,", "APO-BK AnlageD § 15 Abs. 3 Nr. 2 d)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFremdsprachePruefeNeue(4, "M_FS2NEU", "Prüfe bei nicht vorhandener 2. FS in SEKI, dass alle 4 Kurse mit > 0 Punkten absolviert wurden,", "APO-BK AnlageD § 15 Abs. 2 Nr. 4")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelKopie("2FS", false, "M_KOPIE", "Erstelle Markierungsvariante für Fremdsprachenoptionen,", "APO-BK AnlageD § 15 Abs. 2 Nr. 4 und Abs. 3 Nr. 2 b),f)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFremdspracheErste(4, "Root", "M_FS1", "Markiere Fremdsprachen Variante 1: 4 Kurse der 1. Fremdsprache (2. FS folgt ggfs.),", "APO-BK AnlageD § 15 Abs. 2 Nr. 4 und Abs. 3 Nr. 2 b)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFremdspracheZweiteNeu(2, "Root", "M_FS12", "Markiere Fremdsprachen Variante 1: 2 Kurse der neu einsetzenden 2. Fremdsprache,", "APO-BK AnlageD § 15 Abs. 2 Nr. 4 und Abs. 3 Nr. 2 f)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelFremdspracheZweiteNur(4, "Root#2FS", "M_FS2", "Markiere Fremdsprachen Variante 2: 4 Kurse der zweiten Fremdsprache,", "APO-BK AnlageD § 15 Abs. 2 Nr. 4 und Abs. 3 Nr. 2 b), f)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelMinAnzahlkurse(32, "M_WK_MIN", "Markiere mindestens 32 Kurse,", "APO-BK AnlageD § 15 Abs. 2 Nr. 1a")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelKopie("FA", true, "M_KOPIE", "Erstelle Markierungsvariante für Facharbeit,", "APO-BK Anlage D § 8 Abs. 2")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelMaxAnzahlkurse(40, "Root", "M_WK_MAX", "Markiere weitere Kurse um bestes Ergebnis zu erreichen (1. FS),", "APO-BK AnlageD § 15 Abs. 2 Nr. 1b (V1)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelMaxAnzahlkurse(40, "Root#2FS", "M_WK_MAX_FS", "Markiere weitere Kurse um bestes Ergebnis zu erreichen (2. FS),", "APO-BK AnlageD § 15 Abs. 2 Nr. 1b (V2)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelMaxAnzahlkurse(39, "Root#FA", "M_WK_MAX_FA", "Markiere weitere Kurse um bestes Ergebnis zu erreichen (1. FS + FA),", "APO-BK AnlageD § 15 Abs. 2 Nr. 1b (V3)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelMaxAnzahlkurse(39, "Root#2FS#FA", "M_WK_MAX_FS_FA", "Markiere weitere Kurse um bestes Ergebnis zu erreichen (2. FS + FA),", "APO-BK AnlageD § 15 Abs. 2 Nr. 1b (V4)")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelMinAnzahlpunkte(200, 40, "M_WK_MAX", "Markiere weitere Kurse um mindestens 200 Punkte im Block I zu erreichen,", "APO-BK AnlageD § 15 Abs. 2 Nr. 2")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelDefizite(38, 8, "M_WK_DEF8", "Markiere bis zu 40 Kursen um 8 Defizite tolerieren zu können,", "APO-BK AnlageD § 15 Abs. 2 Nr. 3c")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelDefizite(33, 7, "M_WK_DEF7", "Markiere weitere Kurse, falls mehr als 7 Defizite bei bis zu 37 eingebrachten Kursen vorhanden sind,", "APO-BK AnlageD § 15 Abs. 2 Nr. 3b")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelDefizite(32, 6, "M_WK_DEF6", "Markiere weitere Kurse, falls mehr als 6 Defizite bei 32 eingebrachten Kursen vorhanden sind,", "APO-BK AnlageD § 15 Abs. 2 Nr. 3a")), cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(new BKGymAbiturMarkierungsregelDefizitePruefeLK(3, "M_PR_DEFLK", "Prüfe auf maximal erlaubte Anzahl von drei Defiziten im LK-Bereich,", "APO-BK AnlageD § 15 Abs. 2 Nr. 3 Nachsatz")));


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus und initialisiert ihn
	 *
	 * @param manager   der Abiturdatenmanager des beruflichen Gymnasiums
	 */
	public constructor(manager: BKGymAbiturdatenManager) {
		super();
		this.manager = manager;
		this.ergebnisse = new BKGymAbiturMarkierungsVarianten(manager);
	}

	/**
	 * Führt die optimale Markierung der Kurse für die Zulassung zum Abitur durch.
	 * Voraussetzung ist, dass alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @return das Markierungsergebnis
	 */
	public berechne(): BKGymAbiturMarkierungsalgorithmusErgebnis | null {
		for (const regel of BKGymAbiturMarkierungsalgorithmus.regelsatz) {
			const jetzt: List<BKGymAbiturMarkierungsVariante> | null = new ArrayList<BKGymAbiturMarkierungsVariante>(this.ergebnisse.getErgebnisse());
			for (const variante of jetzt)
				regel.exec(variante);
		}
		return this.ergebnisse.getBestesErgebnis();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsalgorithmus';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsalgorithmus'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsalgorithmus>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsalgorithmus');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsalgorithmus(obj: unknown): BKGymAbiturMarkierungsalgorithmus {
	return obj as BKGymAbiturMarkierungsalgorithmus;
}
