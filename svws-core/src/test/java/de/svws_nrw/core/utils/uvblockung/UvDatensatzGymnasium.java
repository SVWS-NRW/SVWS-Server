package de.svws_nrw.core.utils.uvblockung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenSchiene;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.utils.uv.UvManager;

/**
 * Diese Klasse stellt einen großen, realitätsnahen Testdatensatz für die Unterrichtsverteilung bereit.
 * Der Datensatz basiert auf einem anonymisierten Kurs42-Export eines Gymnasiums und enthält:
 * - 9 Jahrgänge (05-10, EF, Q1, Q2),
 * - 22 Klassen (05a-10c) mit Schülergruppen und Schülern,
 * - 35 Fächer,
 * - 68 anonymisierte Lehrkräfte mit Lehrbefähigungen und Pflichtstundensoll,
 * - Kurse mit eigenen Schülergruppen (Kursunterricht, Kopplungen, Oberstufe),
 * - Lerngruppen (Klassen- und Kursunterricht) inkl. Schienen,
 * - ein Zeitraster (5 Tage x 9 Stunden).
 *
 * @author Benjamin A. Bartsch
 */
class UvDatensatzGymnasium {

	/** Die ID des Planungsabschnitts. */
	public static final long PLANUNGSABSCHNITT = 1L;

	/** Der Gültigkeitsbeginn des Planungsabschnitts. */
	public static final String GUELTIG_VON = "2026-08-01";

	/** Die ID der fixierten Lehrkraft (L044 unterrichtet BI in der Lerngruppe 37). */
	public static final long LEHRKRAFT_FIXIERT = 1044L;

	/** Die ID der verbotenen Lehrkraft (L051 unterrichtet D, nicht BI). */
	public static final long LEHRKRAFT_VERBOTEN = 1051L;

	/** Die ID der Lerngruppe für Fixierung und Verbot (37 = BI in der Klasse 05a). */
	public static final long LERNGRUPPE = 37L;

	/** Fächer: Kürzel|Bezeichnung */
	private static final String[] FAECHER = {
					"56FörSp|Fördersport (Stufen 5/6)",
					"BI|BI",
					"CH|CH",
					"D|D",
					"E|E",
					"EK|EK",
					"ER|ER",
					"F7|Französisch (ab 7)",
					"GE|GE",
					"GeBi|GeBi",
					"GEn|GEn",
					"GeWi|GeWi",
					"IF|IF",
					"Kl|Klassenlehrerstunde",
					"KR|KR",
					"KU|KU",
					"L0|L0",
					"L7|Latein (ab 7)",
					"LI|LI",
					"M|M",
					"MI|MI",
					"MU|MU",
					"NW|NW",
					"PA|PA",
					"PH|PH",
					"PL|PL",
					"PPL|Praktische Philosophie",
					"RE|RE",
					"S0|S0",
					"Schwi|Sport (Schwimmhalle)",
					"SP|SP",
					"SW|SW",
					"SWn|SWn",
					"Vert M|Vert M",
					"WP|Wirtschaft/Politik",
	};

	/** Jahrgänge: Kürzel|Kurzbezeichnung|Bezeichnung */
	private static final String[] JAHRGAENGE = {
					"05|5|Jahrgang 5",
					"06|6|Jahrgang 6",
					"07|7|Jahrgang 7",
					"08|8|Jahrgang 8",
					"09|9|Jahrgang 9",
					"10|10|Jahrgang 10",
					"EF|EF|Einführungsphase",
					"Q1|Q1|Qualifikationsphase 1",
					"Q2|Q2|Qualifikationsphase 2",
	};

	/** Klassen: Kürzel|Schülerzahl */
	private static final String[] KLASSEN = {
					"05a|32",
					"05b|32",
					"05c|32",
					"06a|31",
					"06b|31",
					"06c|31",
					"06d|31",
					"07a|30",
					"07b|30",
					"07c|30",
					"07d|30",
					"08a|29",
					"08b|29",
					"08c|29",
					"08d|29",
					"09a|28",
					"09b|28",
					"09c|28",
					"09d|28",
					"10a|27",
					"10b|27",
					"10c|27",
	};

	/** Oberstufen-Jahrgänge: Kürzel|Schülerzahl */
	private static final String[] SEK2_JAHRGAENGE = {
					"EF|100",
					"Q1|75",
					"Q2|90",
	};

	/** Schienen: Bezeichnung|Jahrgänge */
	private static final String[] SCHIENEN = {
					"10FL|10",
					"10RE|10",
					"10WPL|10",
					"5RE|05",
					"6RE|06",
					"7FL|07",
					"7RE|07",
					"8FL|08",
					"8RE|08",
					"9FL|09",
					"9RE|09",
					"9WPL|09",
					"EFB01|EF",
					"EFB02|EF",
					"EFB03|EF",
					"EFB04|EF",
					"EFB05|EF",
					"EFB06|EF",
					"EFB07|EF",
					"EFB08|EF",
					"EFB09|EF",
					"EFB10|EF",
					"EFB11|EF",
					"EFB12|EF",
					"EFB13|EF",
					"Q1B03|Q1",
					"Q1B04|Q1",
					"Q1B05|Q1",
					"Q1B06|Q1",
					"Q1B07|Q1",
					"Q1B08|Q1",
					"Q1B09|Q1",
					"Q1B10|Q1",
					"Q1B11|Q1",
					"Q1B12|Q1",
					"Q1LK1|Q1",
					"Q1LK2|Q1",
					"Q2B03|Q2",
					"Q2B04|Q2",
					"Q2B05|Q2",
					"Q2B06|Q2",
					"Q2B07|Q2",
					"Q2B08|Q2",
					"Q2B09|Q2",
					"Q2B10|Q2",
					"Q2B11|Q2",
					"Q2LK1|Q2",
					"Q2LK2|Q2",
	};

	/** Lehrkräfte (anonymisiert) */
	private static final String[] LEHRKRAEFTE = {
					"L001",
					"L002",
					"L003",
					"L004",
					"L005",
					"L006",
					"L007",
					"L008",
					"L009",
					"L010",
					"L011",
					"L012",
					"L013",
					"L014",
					"L015",
					"L016",
					"L017",
					"L018",
					"L019",
					"L020",
					"L021",
					"L022",
					"L023",
					"L024",
					"L025",
					"L026",
					"L027",
					"L028",
					"L029",
					"L030",
					"L031",
					"L032",
					"L033",
					"L034",
					"L035",
					"L036",
					"L037",
					"L038",
					"L039",
					"L040",
					"L041",
					"L042",
					"L043",
					"L044",
					"L045",
					"L046",
					"L047",
					"L048",
					"L049",
					"L050",
					"L051",
					"L052",
					"L053",
					"L054",
					"L055",
					"L056",
					"L057",
					"L058",
					"L059",
					"L060",
					"L061",
					"L062",
					"L063",
					"L064",
					"L065",
					"L066",
					"L067",
					"L068",
	};

	/** Kurse: Id|Std|Lehrkraft|Fach|Klassen|Schiene */
	private static final String[] KURSE = {
					"36|2|L054|56FörSp|05a,05b,05c,06a,06b,06c,06d|",
					"37|4|L044|BI|05a|",
					"38|8|L051|D|05a|",
					"39|10|L006|E|05a|",
					"40|4|L060|EK|05a|",
					"41|2|L006|Kl|05a|",
					"42|4|L051|KU|05a|",
					"43|10|L032|M|05a|",
					"44|2|L029|MU|05a|",
					"45|4|L012|PPL|05a,05b,05c|5RE",
					"46|4|L059|PPL|05a,05b,05c|5RE",
					"47|4|L033|RE|05a,05b,05c|5RE",
					"48|4|L037|RE|05a,05b,05c|5RE",
					"49|4|L041|RE|05a,05b,05c|5RE",
					"50|4|L037|Schwi|05a|",
					"51|4|L037|SP|05a|",
					"52|4|L048|WP|05a|",
					"53|4|L024|BI|05b|",
					"54|8|L012|D|05b|",
					"55|10|L014|E|05b|",
					"56|4|L014|EK|05b|",
					"57|2|L014|Kl|05b|",
					"58|4|L051|KU|05b|",
					"59|10|L009|M|05b|",
					"60|2|L029|MU|05b|",
					"61|0|L039|Schwi|05b|",
					"62|8|L039|SP|05b|",
					"63|4|L048|WP|05b|",
					"64|4|L024|BI|05c|",
					"65|8|L048|D|05c|",
					"66|10|L064|E|05c|",
					"67|4|L060|EK|05c|",
					"68|2|L048|Kl|05c|",
					"69|4|L051|KU|05c|",
					"70|10|L024|M|05c|",
					"71|2|L029|MU|05c|",
					"72|4|L024|Schwi|05c|",
					"73|4|L024|SP|05c|",
					"74|4|L048|WP|05c|",
					"75|4|L062|BI|06a|",
					"76|10|L023|D|06a|",
					"77|8|L042|E|06a|",
					"78|4|L066|GE|06a|",
					"79|4|L013|IF|06a|",
					"80|4|L035|KU|06a|",
					"81|8|L034|M|06a|",
					"82|4|L007|MU|06a|",
					"83|4|L031|PH|06a|",
					"84|4|L012|PPL|06a,06b,06c,06d|6RE",
					"85|4|L059|PPL|06a,06b,06c,06d|6RE",
					"86|4|L007|RE|06a,06b,06c,06d|6RE",
					"87|4|L033|RE|06a,06b,06c,06d|6RE",
					"88|4|L037|RE|06a,06b,06c,06d|6RE",
					"89|4|L037|Schwi|06a|",
					"90|2|L037|SP|06a|",
					"91|4|L062|BI|06b|",
					"92|10|L012|D|06b|",
					"93|8|L053|E|06b|",
					"94|4|L011|GE|06b|",
					"95|4|L055|IF|06b|",
					"96|4|L043|KU|06b|",
					"97|8|L028|M|06b|",
					"98|4|L029|MU|06b|",
					"99|4|L016|PH|06b|",
					"100|4|L045|Schwi|06b|",
					"101|2|L045|SP|06b|",
					"102|4|L044|BI|06c|",
					"103|10|L028|D|06c|",
					"104|8|L064|E|06c|",
					"105|4|L027|GE|06c|",
					"106|4|L055|IF|06c|",
					"107|4|L043|KU|06c|",
					"108|8|L032|M|06c|",
					"109|4|L007|MU|06c|",
					"110|4|L016|PH|06c|",
					"111|0|L039|Schwi|06c|",
					"112|6|L039|SP|06c|",
					"113|4|L024|BI|06d|",
					"114|10|L059|D|06d|",
					"115|8|L027|E|06d|",
					"116|4|L027|GE|06d|",
					"117|4|L011|IF|06d|",
					"118|4|L043|KU|06d|",
					"119|8|L011|M|06d|",
					"120|4|L007|MU|06d|",
					"121|4|L031|PH|06d|",
					"122|4|L024|Schwi|06d|",
					"123|2|L024|SP|06d|",
					"124|4|L021|CH|07a|",
					"125|6|L067|D|07a|",
					"126|6|L019|E|07a|",
					"127|4|L060|EK|07a|",
					"128|4|L020|ER|07a,07b,07c,07d|7RE",
					"129|4|L033|ER|07a,07b,07c,07d|7RE",
					"130|8|L039|F7|07a,07b,07c,07d|7FL",
					"131|8|L049|F7|07a,07b,07c,07d|7FL",
					"132|8|L050|F7|07a,07b,07c,07d|7FL",
					"133|4|L018|GE|07a|",
					"134|4|L026|KR|07a,07b,07c,07d|7RE",
					"135|4|L043|KU|07a|",
					"136|8|L042|L7|07a,07b,07c,07d|7FL",
					"137|8|L058|L7|07a,07b,07c,07d|7FL",
					"138|6|L028|M|07a|",
					"139|4|L007|MU|07a|",
					"140|4|L012|PPL|07a,07b,07c,07d|7RE",
					"141|4|L066|PPL|07a,07b,07c,07d|7RE",
					"142|6|L045|SP|07a|",
					"143|4|L063|WP|07a|",
					"144|4|L007|CH|07b|",
					"145|6|L020|D|07b|",
					"146|6|L042|E|07b|",
					"147|4|L060|EK|07b|",
					"148|4|L018|GE|07b|",
					"149|4|L051|KU|07b|",
					"150|6|L032|M|07b|",
					"151|4|L007|MU|07b|",
					"152|6|L037|SP|07b|",
					"153|4|L063|WP|07b|",
					"154|4|L021|CH|07c|",
					"155|6|L015|D|07c|",
					"156|6|L010|E|07c|",
					"157|4|L013|EK|07c|",
					"158|4|L010|GE|07c|",
					"159|4|L051|KU|07c|",
					"160|6|L068|M|07c|",
					"161|4|L007|MU|07c|",
					"162|6|L037|SP|07c|",
					"163|4|L059|WP|07c|",
					"164|4|L007|CH|07d|",
					"165|6|L030|D|07d|",
					"166|6|L033|E|07d|",
					"167|4|L060|EK|07d|",
					"168|4|L044|GE|07d|",
					"169|4|L043|KU|07d|",
					"170|6|L065|M|07d|",
					"171|4|L007|MU|07d|",
					"172|6|L045|SP|07d|",
					"173|4|L059|WP|07d|",
					"174|4|L025|BI|08a|",
					"175|4|L052|CH|08a|",
					"176|8|L048|D|08a|",
					"177|8|L053|E|08a|",
					"178|4|L005|ER|08a,08b,08c,08d|8RE",
					"179|8|L039|F7|08a,08b,08c,08d|8FL",
					"180|8|L041|F7|08a,08b,08c,08d|8FL",
					"181|8|L049|F7|08a,08b,08c,08d|8FL",
					"182|4|L017|KR|08a,08b,08c,08d|8RE",
					"183|4|L026|KR|08a,08b,08c,08d|8RE",
					"184|8|L046|L7|08a,08b,08c,08d|8FL",
					"185|8|L057|L7|08a,08b,08c,08d|8FL",
					"186|8|L055|M|08a|",
					"187|4|L029|MU|08a|",
					"188|4|L031|PH|08a|",
					"189|4|L057|PPL|08a,08b,08c,08d|8RE",
					"190|4|L059|PPL|08a,08b,08c,08d|8RE",
					"191|0|L037|Schwi|08a|",
					"192|4|L037|SP|08a|",
					"193|4|L063|WP|08a|",
					"194|4|L021|BI|08b|",
					"195|4|L021|CH|08b|",
					"196|8|L035|D|08b|",
					"197|8|L006|E|08b|",
					"198|8|L034|M|08b|",
					"199|4|L029|MU|08b|",
					"200|4|L016|PH|08b|",
					"201|0|L037|Schwi|08b|",
					"202|4|L037|SP|08b|",
					"203|4|L063|WP|08b|",
					"204|4|L025|BI|08c|",
					"205|4|L031|CH|08c|",
					"206|8|L025|D|08c|",
					"207|8|L064|E|08c|",
					"208|8|L045|M|08c|",
					"209|4|L029|MU|08c|",
					"210|4|L031|PH|08c|",
					"211|0|L036|Schwi|08c|",
					"212|4|L036|SP|08c|",
					"213|4|L063|WP|08c|",
					"214|4|L052|BI|08d|",
					"215|4|L052|CH|08d|",
					"216|8|L047|D|08d|",
					"217|8|L047|E|08d|",
					"218|8|L045|M|08d|",
					"219|4|L029|MU|08d|",
					"220|4|L056|PH|08d|",
					"221|0|L045|Schwi|08d|",
					"222|4|L045|SP|08d|",
					"223|4|L059|WP|08d|",
					"224|4|L021|CH|09a|",
					"225|6|L025|D|09a|",
					"226|6|L049|E|09a|",
					"227|4|L013|EK|09a|",
					"228|4|L005|ER|09a,09b,09c,09d|9RE",
					"229|4|L033|ER|09a,09b,09c,09d|9RE",
					"230|8|L041|F7|09a,09b,09c,09d|9FL",
					"231|8|L049|F7|09a,09b,09c,09d|9FL",
					"232|8|L050|F7|09a,09b,09c,09d|9FL",
					"233|4|L058|GE|09a|",
					"234|6|L038|GeBi|09a,09b,09c,09d|9WPL",
					"235|6|L063|GeWi|09a,09b,09c,09d|9WPL",
					"236|4|L017|KR|09a,09b,09c,09d|9RE",
					"237|4|L051|KU|09a|",
					"238|8|L058|L7|09a,09b,09c,09d|9FL",
					"239|6|L065|M|09a|",
					"240|6|L011|MI|09a,09b,09c,09d|9WPL",
					"241|6|L021|NW|09a,09b,09c,09d|9WPL",
					"242|4|L056|PH|09a|",
					"243|4|L012|PPL|09a,09b,09c,09d|9RE",
					"244|4|L066|PPL|09a,09b,09c,09d|9RE",
					"245|4|L065|SP|09a|",
					"246|4|L040|CH|09b|",
					"247|6|L064|D|09b|",
					"248|6|L064|E|09b|",
					"249|4|L060|EK|09b|",
					"250|4|L058|GE|09b|",
					"251|4|L051|KU|09b|",
					"252|6|L061|M|09b|",
					"253|4|L016|PH|09b|",
					"254|4|L045|SP|09b|",
					"255|4|L040|CH|09c|",
					"256|6|L008|D|09c|",
					"257|6|L053|E|09c|",
					"258|4|L013|EK|09c|",
					"259|4|L063|GE|09c|",
					"260|4|L043|KU|09c|",
					"261|6|L034|M|09c|",
					"262|4|L016|PH|09c|",
					"263|4|L050|SP|09c|",
					"264|4|L040|CH|09d|",
					"265|6|L008|D|09d|",
					"266|6|L010|E|09d|",
					"267|4|L060|EK|09d|",
					"268|4|L018|GE|09d|",
					"269|4|L051|KU|09d|",
					"270|6|L036|M|09d|",
					"271|4|L036|PH|09d|",
					"272|4|L036|SP|09d|",
					"273|4|L016|BI|10a|",
					"274|0|L021|CH|10a|",
					"275|6|L017|D|10a|",
					"276|6|L019|E|10a|",
					"277|4|L060|EK|10a|",
					"278|4|L005|ER|10a,10b,10c|10RE",
					"279|6|L039|F7|10a,10b,10c|10FL",
					"280|6|L049|F7|10a,10b,10c|10FL",
					"281|4|L017|GE|10a|",
					"282|6|L018|GeBi|10a,10b,10c|10WPL",
					"283|6|L013|GeWi|10a,10b,10c|10WPL",
					"284|4|L026|KR|10a,10b,10c|10RE",
					"285|0|L035|KU|10a|",
					"286|6|L046|L7|10a,10b,10c|10FL",
					"287|6|L061|M|10a|",
					"288|6|L011|MI|10a,10b,10c|10WPL",
					"289|4|L029|MU|10a|",
					"290|6|L044|NW|10a,10b,10c|10WPL",
					"291|4|L016|PH|10a|",
					"292|4|L012|PPL|10a,10b,10c|10RE",
					"293|4|L059|PPL|10a,10b,10c|10RE",
					"294|4|L037|SP|10a|",
					"295|4|L063|WP|10a|",
					"296|4|L025|BI|10b|",
					"297|4|L052|CH|10b|",
					"298|6|L023|D|10b|",
					"299|6|L067|E|10b|",
					"300|0|L013|EK|10b|",
					"301|4|L018|GE|10b|",
					"302|4|L035|KU|10b|",
					"303|6|L034|M|10b|",
					"304|0|L007|MU|10b|",
					"305|4|L056|PH|10b|",
					"306|4|L034|SP|10b|",
					"307|4|L048|WP|10b|",
					"308|4|L062|BI|10c|",
					"309|4|L021|CH|10c|",
					"310|6|L062|D|10c|",
					"311|6|L068|E|10c|",
					"312|0|L060|EK|10c|",
					"313|4|L065|GE|10c|",
					"314|0|L051|KU|10c|",
					"315|6|L065|M|10c|",
					"316|4|L007|MU|10c|",
					"317|4|L016|PH|10c|",
					"318|4|L065|SP|10c|",
					"319|4|L063|WP|10c|",
					"320|6|L021|BI|EF|EFB11",
					"321|6|L062|BI|EF|EFB09",
					"322|6|L062|BI|EF|EFB05",
					"323|6|L021|CH|EF|EFB08",
					"324|6|L031|CH|EF|EFB06",
					"325|6|L012|D|EF|EFB03",
					"326|6|L035|D|EF|EFB02",
					"327|6|L046|D|EF|EFB01",
					"328|6|L062|D|EF|EFB01",
					"329|6|L010|E|EF|EFB03",
					"330|6|L053|E|EF|EFB01",
					"331|6|L067|E|EF|EFB02",
					"332|6|L068|E|EF|EFB02",
					"333|6|L013|EK|EF|EFB10",
					"334|6|L014|EK|EF|EFB07",
					"335|6|L033|ER|EF|EFB04",
					"336|6|L050|F7|EF|EFB10",
					"337|6|L005|GE|EF|EFB06",
					"338|6|L010|GE|EF|EFB09",
					"339|6|L066|GE|EF|EFB08",
					"340|6|L009|IF|EF|EFB11",
					"341|6|L007|KR|EF|EFB04",
					"342|6|L017|KR|EF|EFB04",
					"343|6|L043|KU|EF|EFB06",
					"344|6|L043|KU|EF|EFB09",
					"345|6|L043|KU|EF|EFB07",
					"346|8|L001|L0|EF|EFB05",
					"347|6|L057|L7|EF|EFB10",
					"348|6|L058|L7|EF|EFB10",
					"349|6|L009|M|EF|EFB03",
					"350|6|L011|M|EF|EFB03",
					"351|6|L022|M|EF|EFB02",
					"352|6|L028|M|EF|EFB01",
					"353|6|L029|MU|EF|EFB09",
					"354|6|L030|PA|EF|EFB11",
					"355|6|L030|PA|EF|EFB08",
					"356|6|L016|PH|EF|EFB06",
					"357|6|L031|PH|EF|EFB05",
					"358|6|L057|PL|EF|EFB04",
					"359|8|L006|S0|EF|EFB05",
					"360|8|L027|S0|EF|EFB05",
					"361|6|L034|SP|EF|EFB12",
					"362|6|L045|SP|EF|EFB12",
					"363|6|L050|SP|EF|EFB12",
					"364|6|L008|SW|EF|EFB05",
					"365|6|L063|SW|EF|EFB07",
					"366|4|L028|Vert M|EF|EFB13",
					"367|4|L032|Vert M|EF|EFB13",
					"368|4|L055|Vert M|EF|EFB13",
					"369|10|L002|BI|Q1|Q1LK1",
					"370|6|L025|BI|Q1|Q1B07",
					"371|6|L044|BI|Q1|Q1B12",
					"372|10|L044|BI|Q1|Q1LK2",
					"373|6|L031|CH|Q1|Q1B07",
					"374|6|L012|D|Q1|Q1B08",
					"375|10|L020|D|Q1|Q1LK1",
					"376|6|L028|D|Q1|Q1B10",
					"377|6|L018|E|Q1|Q1B04",
					"378|10|L032|E|Q1|Q1LK1",
					"379|10|L042|E|Q1|Q1LK2",
					"380|6|L047|E|Q1|Q1B10",
					"381|10|L003|EK|Q1|Q1LK2",
					"382|6|L013|EK|Q1|Q1B09",
					"383|6|L005|ER|Q1|Q1B03",
					"384|6|L041|F7|Q1|Q1B06",
					"385|6|L005|GE|Q1|Q1B10",
					"386|10|L017|GE|Q1|Q1LK2",
					"387|6|L009|IF|Q1|Q1B04",
					"388|6|L017|KR|Q1|Q1B03",
					"389|6|L035|KU|Q1|Q1B05",
					"390|6|L058|L7|Q1|Q1B06",
					"391|6|L015|LI|Q1|Q1B05",
					"392|6|L030|LI|Q1|Q1B05",
					"393|6|L022|M|Q1|Q1B08",
					"394|6|L055|M|Q1|Q1B12",
					"395|6|L056|M|Q1|Q1B06",
					"396|10|L061|M|Q1|Q1LK1",
					"397|6|L030|PA|Q1|Q1B09",
					"398|6|L036|PH|Q1|Q1B12",
					"399|6|L044|PL|Q1|Q1B03",
					"400|8|L053|S0|Q1|Q1B04",
					"401|10|L004|SP|Q1|Q1LK2",
					"402|6|L024|SP|Q1|Q1B11",
					"403|6|L039|SP|Q1|Q1B11",
					"404|6|L065|SP|Q1|Q1B11",
					"405|6|L063|SW|Q1|Q1B08",
					"406|10|L001|BI|Q2|Q2LK1",
					"407|6|L024|BI|Q2|Q2B07",
					"408|6|L025|BI|Q2|Q2B08",
					"409|6|L044|BI|Q2|Q2B06",
					"410|10|L062|BI|Q2|Q2LK2",
					"411|6|L021|CH|Q2|Q2B10",
					"412|6|L031|CH|Q2|Q2B07",
					"413|6|L008|D|Q2|Q2B05",
					"414|6|L020|D|Q2|Q2B06",
					"415|6|L023|D|Q2|Q2B09",
					"416|10|L046|D|Q2|Q2LK1",
					"417|10|L006|E|Q2|Q2LK1",
					"418|6|L049|E|Q2|Q2B08",
					"419|10|L053|E|Q2|Q2LK2",
					"420|6|L064|E|Q2|Q2B10",
					"421|10|L013|EK|Q2|Q2LK2",
					"422|6|L013|EK|Q2|Q2B05",
					"423|10|L014|EK|Q2|Q2LK1",
					"424|6|L020|ER|Q2|Q2B04",
					"425|6|L050|F7|Q2|Q2B09",
					"426|12|L005|GE|Q2|Q2LK2",
					"427|6|L005|GE|Q2|Q2B06",
					"428|6|L010|GE|Q2|Q2B08",
					"429|4|L058|GEn|Q2|Q2B03",
					"430|6|L009|IF|Q2|Q2B10",
					"431|6|L017|KR|Q2|Q2B04",
					"432|4|L035|KU|Q2|Q2B03",
					"433|6|L057|L7|Q2|Q2B09",
					"434|10|L011|M|Q2|Q2LK1",
					"435|6|L028|M|Q2|Q2B09",
					"436|6|L056|M|Q2|Q2B05",
					"437|6|L061|M|Q2|Q2B10",
					"438|6|L030|PA|Q2|Q2B06",
					"439|10|L016|PH|Q2|Q2LK2",
					"440|6|L022|PH|Q2|Q2B08",
					"441|6|L057|PL|Q2|Q2B04",
					"442|8|L027|S0|Q2|Q2B09",
					"443|10|L004|SP|Q2|Q2LK2",
					"444|6|L037|SP|Q2|Q2B11",
					"445|6|L039|SP|Q2|Q2B11",
					"446|6|L050|SP|Q2|Q2B11",
					"447|6|L065|SP|Q2|Q2B11",
					"448|6|L008|SW|Q2|Q2B07",
					"449|4|L008|SWn|Q2|Q2B04",
					"450|4|L048|SWn|Q2|Q2B03",
					"451|4|L063|SWn|Q2|Q2B03",
	};

	/**
	 * Erzeugt einen {@link UvManager} mit dem beschriebenen Gymnasium-Testdatensatz.
	 *
	 * @return ein {@link UvManager}-Objekt mit dem beschriebenen Zustand.
	 */
	public static UvManager erzeugeUvManager() {
		final long pl = PLANUNGSABSCHNITT;
		final Random rnd = new Random(20261015L);

		// Fächer
		final List<FachDaten> listFachDaten = new ArrayList<>();
		final List<UvFach> listUvFach = new ArrayList<>();
		final Map<String, Long> mapFachId = new HashMap<>();
		final Map<String, Long> mapFachDatenId = new HashMap<>();
		long idFachDaten = 201;
		long idUvFach = 301;
		for (final String eintrag : FAECHER) {
			final String[] t = eintrag.split("\\|");
			listFachDaten.add(UvDatensatz.erzeugeFachDaten(idFachDaten, t[0], t[0], t[1], true, (int) (idFachDaten - 200)));
			listUvFach.add(UvDatensatz.erzeugeUvFach(idUvFach, idFachDaten, GUELTIG_VON, null));
			mapFachId.put(t[0], idUvFach);
			mapFachDatenId.put(t[0], idFachDaten);
			idFachDaten++;
			idUvFach++;
		}

		// Jahrgänge
		final List<JahrgangsDaten> listJahrgaenge = new ArrayList<>();
		final Map<String, Long> mapJahrgangId = new HashMap<>();
		long idJahrgang = 101;
		for (final String eintrag : JAHRGAENGE) {
			final String[] t = eintrag.split("\\|");
			listJahrgaenge.add(UvDatensatz.erzeugeJahrgang(idJahrgang, t[0], t[1], t[2], (int) (idJahrgang - 100), true));
			mapJahrgangId.put(t[0], idJahrgang);
			idJahrgang++;
		}

		// Klassen, Klassen-Schülergruppen und Schüler der Sekundarstufe I
		final List<UvKlasse> listKlassen = new ArrayList<>();
		final List<UvSchuelergruppe> listSchuelergruppen = new ArrayList<>();
		final List<UvPlanungsabschnittSchueler> listSchueler = new ArrayList<>();
		final List<UvSchuelergruppeSchueler> listGruppeSchueler = new ArrayList<>();
		final Map<String, Long> mapKlassenId = new HashMap<>();
		final Map<String, List<Long>> mapKlassenSchueler = new HashMap<>();
		long idKlasse = 401;
		long idKlassenGruppe = 501;
		long idSchueler = 10001;
		for (final String eintrag : KLASSEN) {
			final String[] t = eintrag.split("\\|");
			final String klKuerzel = t[0];
			final int groesse = Integer.parseInt(t[1]);
			final long jgId = mapJahrgangId.get(klKuerzel.substring(0, 2));
			final long idGruppe = idKlassenGruppe++;
			listSchuelergruppen.add(UvDatensatz.erzeugeSchuelergruppe(idGruppe, pl, klKuerzel, jgId));
			listKlassen.add(UvDatensatz.erzeugeKlasse(idKlasse, pl, klKuerzel, idGruppe));
			mapKlassenId.put(klKuerzel, idKlasse);
			final List<Long> schuelerIds = new ArrayList<>();
			for (int s = 0; s < groesse; s++) {
				final long idS = idSchueler++;
				listSchueler.add(UvDatensatz.erzeugePlanungsabschnittSchueler(pl, idS, jgId, idKlasse));
				listGruppeSchueler.add(UvDatensatz.erzeugeSchuelergruppeSchueler(pl, idGruppe, idS));
				schuelerIds.add(idS);
			}
			mapKlassenSchueler.put(klKuerzel, schuelerIds);
			idKlasse++;
		}

		// Schüler der Sekundarstufe II (ohne Klassen-Zuordnung)
		final Map<String, List<Long>> mapJahrgangSchueler = new HashMap<>();
		for (final String eintrag : SEK2_JAHRGAENGE) {
			final String[] t = eintrag.split("\\|");
			final long jgId = mapJahrgangId.get(t[0]);
			final List<Long> schuelerIds = new ArrayList<>();
			for (int s = 0; s < Integer.parseInt(t[1]); s++) {
				final UvPlanungsabschnittSchueler ps = new UvPlanungsabschnittSchueler();
				ps.idPlanungsabschnitt = pl;
				ps.idSchueler = idSchueler++;
				ps.idJahrgang = jgId;
				ps.idKlasse = null;
				listSchueler.add(ps);
				schuelerIds.add(ps.idSchueler);
			}
			mapJahrgangSchueler.put(t[0], schuelerIds);
		}

		// Lehrkräfte, Pflichtstundensoll, Lehrbefähigungen (aus den Kursen abgeleitet)
		final Map<String, Double> mapLehrerSoll = new HashMap<>();
		final Map<String, Set<String>> mapLehrerFaecher = new HashMap<>();
		final Map<String, Set<String>> mapLehrerFaecherSek1 = new HashMap<>();
		final Map<String, Set<String>> mapLehrerFaecherSek2 = new HashMap<>();
		for (final String kurs : KURSE) {
			final String[] t = kurs.split("\\|", -1);
			final String lehrer = t[2];
			final String fach = t[3];
			final boolean istSek2 = t[4].contains("EF") || t[4].contains("Q1") || t[4].contains("Q2");
			mapLehrerSoll.merge(lehrer, Double.parseDouble(t[1]) / 2.0, Double::sum);
			mapLehrerFaecher.computeIfAbsent(lehrer, k -> new TreeSet<>()).add(fach);
			if (istSek2) {
				mapLehrerFaecherSek2.computeIfAbsent(lehrer, k -> new TreeSet<>()).add(fach);
			} else {
				mapLehrerFaecherSek1.computeIfAbsent(lehrer, k -> new TreeSet<>()).add(fach);
			}
		}
		final List<UvLehrer> listLehrer = new ArrayList<>();
		final List<UvLehrerPflichtstundensoll> listSoll = new ArrayList<>();
		final List<UvPlanungsabschnittLehrer> listPlanungsabschnittLehrer = new ArrayList<>();
		final List<LehrerUnterrichtsfach> listUnterrichtsfach = new ArrayList<>();
		final Map<String, Long> mapLehrerId = new HashMap<>();
		long idLehrer = 1001;
		long idSoll = 3001;
		long idUnterrichtsfach = 4001;
		for (final String kuerzel : LEHRKRAEFTE) {
			final long lId = idLehrer++;
			mapLehrerId.put(kuerzel, lId);
			listLehrer.add(UvDatensatz.erzeugeLehrer(lId, kuerzel, "Lehrkraft", kuerzel, "2020-08-01", null));
			listSoll.add(UvDatensatz.erzeugePflichtstundensoll(idSoll++, lId, GUELTIG_VON, null, mapLehrerSoll.getOrDefault(kuerzel, 0.0)));
			listPlanungsabschnittLehrer.add(UvDatensatz.erzeugePlanungsabschnittLehrer(pl, lId));
			for (final String fach : mapLehrerFaecher.getOrDefault(kuerzel, new TreeSet<>())) {
				final boolean sek1 = mapLehrerFaecherSek1.getOrDefault(kuerzel, new TreeSet<>()).contains(fach);
				final boolean sek2 = mapLehrerFaecherSek2.getOrDefault(kuerzel, new TreeSet<>()).contains(fach);
				listUnterrichtsfach.add(UvDatensatz.erzeugeLehrerUnterrichtsfach(idUnterrichtsfach++, lId, mapFachDatenId.get(fach), sek1, sek2, false));
			}
		}

		// Schienen
		final List<UvSchiene> listSchienen = new ArrayList<>();
		final Map<String, Long> mapSchieneId = new HashMap<>();
		long idSchiene = 5001;
		int nummer = 1;
		for (final String eintrag : SCHIENEN) {
			final String[] t = eintrag.split("\\|");
			final UvSchiene schiene = new UvSchiene();
			schiene.id = idSchiene;
			schiene.idPlanungsabschnitt = pl;
			schiene.nummer = nummer++;
			schiene.bezeichnung = t[0];
			for (final String jg : t[1].split(",")) {
				schiene.idsJahrgaengeErlaubt.add(mapJahrgangId.get(jg));
			}
			listSchienen.add(schiene);
			mapSchieneId.put(t[0], idSchiene);
			idSchiene++;
		}

		// Kurse (Kursunterricht: Schiene vorhanden oder mehrere Klassen) mit eigenen Schülergruppen
		final List<UvKurs> listKurse = new ArrayList<>();
		final List<UvLerngruppenSchiene> listLerngruppenSchienen = new ArrayList<>();
		final Map<Long, Long> mapKursIdSchuelergruppe = new HashMap<>();
		long idKursGruppe = 2001;
		for (final String kurs : KURSE) {
			final String[] t = kurs.split("\\|", -1);
			final long kursId = Long.parseLong(t[0]);
			final String[] klassen = t[4].split(",");
			final boolean istKursunterricht = (!t[5].isEmpty()) || (klassen.length > 1);
			if (!istKursunterricht) {
				continue;
			}
			final long idGruppe = idKursGruppe++;
			final String bezeichnung = t[t[5].isEmpty() ? 3 : 5];
			final TreeSet<Long> jgIds = new TreeSet<>();
			final List<Long> auswahl = new ArrayList<>();
			final List<Long> rest = new ArrayList<>();
			for (final String kl : klassen) {
				final boolean istKlasse = mapKlassenSchueler.containsKey(kl);
				final List<Long> pool = (istKlasse ? mapKlassenSchueler : mapJahrgangSchueler).get(kl);
				jgIds.add(mapJahrgangId.get(istKlasse ? kl.substring(0, 2) : kl));
				final List<Long> poolKopie = new ArrayList<>(pool);
				Collections.shuffle(poolKopie, rnd);
				auswahl.add(poolKopie.get(0));
				for (int i = 1; i < poolKopie.size(); i++) {
					rest.add(poolKopie.get(i));
				}
			}
			Collections.shuffle(rest, rnd);
			final int ziel = Math.min(15 + rnd.nextInt(11), auswahl.size() + rest.size());
			for (int i = 0; (i < rest.size()) && (auswahl.size() < ziel); i++) {
				auswahl.add(rest.get(i));
			}
			final UvSchuelergruppe gruppe = UvDatensatz.erzeugeSchuelergruppe(idGruppe, pl, bezeichnung, jgIds.toArray(new Long[0]));
			listSchuelergruppen.add(gruppe);
			for (final Long idS : auswahl) {
				listGruppeSchueler.add(UvDatensatz.erzeugeSchuelergruppeSchueler(pl, idGruppe, idS));
			}
			mapKursIdSchuelergruppe.put(kursId, idGruppe);
			final UvKurs k = new UvKurs();
			k.id = kursId;
			k.idPlanungsabschnitt = pl;
			k.idSchuljahresabschnitt = 1;
			k.idFach = mapFachId.get(t[3]);
			k.kursart = t[5].contains("LK") ? "LK" : (klassen[0].matches("EF|Q1|Q2") ? "GK" : "");
			k.kursnummer = (int) kursId;
			k.idSchuelergruppe = idGruppe;
			listKurse.add(k);
		}

		// Lerngruppen (Klassen- und Kursunterricht) und Schienen-Zuordnungen
		final List<UvLerngruppe> listLerngruppen = new ArrayList<>();
		for (final String kurs : KURSE) {
			final String[] t = kurs.split("\\|", -1);
			final long kursId = Long.parseLong(t[0]);
			final String[] klassen = t[4].split(",");
			final boolean istKursunterricht = (!t[5].isEmpty()) || (klassen.length > 1);
			final UvLerngruppe lg = new UvLerngruppe();
			lg.id = kursId;
			lg.idPlanungsabschnitt = pl;
			lg.wochenstunden = Double.parseDouble(t[1]) / 2.0;
			if (istKursunterricht) {
				lg.idKurs = kursId;
				lg.idKlasse = null;
				lg.idFach = null;
			} else {
				lg.idKurs = null;
				lg.idKlasse = mapKlassenId.get(klassen[0]);
				lg.idFach = mapFachId.get(t[3]);
			}
			listLerngruppen.add(lg);
			if (!t[5].isEmpty()) {
				final UvLerngruppenSchiene ls = new UvLerngruppenSchiene();
				ls.idPlanungsabschnitt = pl;
				ls.idLerngruppe = kursId;
				ls.idSchiene = mapSchieneId.get(t[5]);
				listLerngruppenSchienen.add(ls);
			}
		}

		// Zeitraster (5 Tage x 9 Stunden)
		final UvZeitraster zeitraster = new UvZeitraster();
		zeitraster.id = 6001;
		zeitraster.gueltigVon = GUELTIG_VON;
		zeitraster.bezeichnung = "Standard";
		final List<UvZeitrasterEintrag> listZeitrasterEintraege = new ArrayList<>();
		long idEintrag = 6101;
		for (int tag = 1; tag <= 5; tag++) {
			for (int stunde = 1; stunde <= 9; stunde++) {
				final UvZeitrasterEintrag eintrag = new UvZeitrasterEintrag();
				eintrag.id = idEintrag++;
				eintrag.idZeitraster = zeitraster.id;
				eintrag.wochentag = tag;
				eintrag.stunde = stunde;
				eintrag.beginn = (stunde - 1) * 45;
				eintrag.ende = eintrag.beginn + 45;
				listZeitrasterEintraege.add(eintrag);
			}
		}
		final UvPlanungsabschnittZeitraster planungsabschnittZeitraster = new UvPlanungsabschnittZeitraster();
		planungsabschnittZeitraster.idPlanungsabschnitt = pl;
		planungsabschnittZeitraster.idZeitraster = zeitraster.id;
		for (final String eintrag : JAHRGAENGE) {
			planungsabschnittZeitraster.idsJahrgaenge.add(mapJahrgangId.get(eintrag.split("\\|")[0]));
		}

		// Manager erzeugen und alle Objekte hinzufügen
		final UvManager man = UvDatensatz.erzeugeUvManager(listJahrgaenge, listFachDaten);
		man.planungsabschnittAdd(UvDatensatz.erzeugePlanungsabschnitt(pl, GUELTIG_VON, null));
		for (final UvFach fach : listUvFach) {
			man.fachAdd(fach);
		}
		for (final UvKlasse klasse : listKlassen) {
			man.klasseAdd(klasse);
		}
		for (final UvLehrer lehrer : listLehrer) {
			man.lehrerAdd(lehrer);
		}
		for (final UvLehrerPflichtstundensoll soll : listSoll) {
			man.lehrerPflichtstundensollAdd(soll);
		}
		for (final UvPlanungsabschnittLehrer pal : listPlanungsabschnittLehrer) {
			man.planungsabschnittLehrerAdd(pal);
		}
		for (final LehrerUnterrichtsfach uf : listUnterrichtsfach) {
			man.lehrerUnterrichtsfachAdd(uf);
		}
		for (final UvSchuelergruppe gruppe : listSchuelergruppen) {
			man.schuelergruppeAdd(gruppe);
		}
		for (final UvPlanungsabschnittSchueler schueler : listSchueler) {
			man.planungsabschnittSchuelerAdd(schueler);
		}
		for (final UvSchuelergruppeSchueler zuordnung : listGruppeSchueler) {
			man.schuelergruppeSchuelerAdd(zuordnung);
		}
		for (final UvKurs kurs : listKurse) {
			man.kursAdd(kurs);
		}
		for (final UvLerngruppe lg : listLerngruppen) {
			man.lerngruppeAdd(lg);
		}
		for (final UvSchiene schiene : listSchienen) {
			man.schieneAdd(schiene);
		}
		for (final UvLerngruppenSchiene zuordnung : listLerngruppenSchienen) {
			man.lerngruppenSchieneAdd(zuordnung);
		}
		man.zeitrasterAdd(zeitraster);
		for (final UvZeitrasterEintrag eintrag : listZeitrasterEintraege) {
			man.zeitrasterEintragAdd(eintrag);
		}
		man.planungsabschnittZeitrasterAdd(planungsabschnittZeitraster);
		return man;
	}


	/**
	 * Erzeugt die Regeln für den Benchmark: eine Fixierung und ein Verbot (Lehrkraft-Lerngruppe).
	 *
	 * @return die Liste der Regeln.
	 */
	public static List<UvBlockungRegel> erzeugeRegeln() {
		final UvBlockungRegel regelFixierung = new UvBlockungRegel();
		regelFixierung.typ = UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr;
		regelFixierung.prioritaet = UvBlockungRegelPrioritaet.HOCH.nr;
		regelFixierung.parameter.add(LEHRKRAFT_FIXIERT);
		regelFixierung.parameter.add(LERNGRUPPE);

		final UvBlockungRegel regelVerbot = new UvBlockungRegel();
		regelVerbot.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr;
		regelVerbot.prioritaet = UvBlockungRegelPrioritaet.HOCH.nr;
		regelVerbot.parameter.add(LEHRKRAFT_VERBOTEN);
		regelVerbot.parameter.add(LERNGRUPPE);

		return List.of(regelFixierung, regelVerbot);
	}

}
