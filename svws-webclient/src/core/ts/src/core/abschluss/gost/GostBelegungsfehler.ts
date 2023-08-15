import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBelegpruefungsArt } from '../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegungsfehlerArt } from '../../../core/abschluss/gost/GostBelegungsfehlerArt';

export class GostBelegungsfehler extends JavaObject implements JavaEnum<GostBelegungsfehler> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostBelegungsfehler> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostBelegungsfehler> = new Map<string, GostBelegungsfehler>();

	/**
	 * BelegungsfehlerArt ABI_10: Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein.
	 */
	public static readonly ABI_10 : GostBelegungsfehler = new GostBelegungsfehler("ABI_10", 0, "ABI_10", GostBelegungsfehlerArt.BELEGUNG, "Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein.", null);

	/**
	 * BelegungsfehlerArt ABI_11
	 */
	public static readonly ABI_11 : GostBelegungsfehler = new GostBelegungsfehler("ABI_11", 1, "ABI_11", GostBelegungsfehlerArt.BELEGUNG, "Religionslehre und Sport dürfen nicht gleichzeitig Abiturfächer sein.", null);

	/**
	 * BelegungsfehlerArt ABI_12
	 */
	public static readonly ABI_12 : GostBelegungsfehler = new GostBelegungsfehler("ABI_12", 2, "ABI_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 3. Abiturfach schriftlich belegt sein.", null);

	/**
	 * BelegungsfehlerArt 	ABI_13
	 */
	public static readonly ABI_13 : GostBelegungsfehler = new GostBelegungsfehler("ABI_13", 3, "ABI_13", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 4. Abiturfach mündlich belegt sein.", null);

	/**
	 * BelegungsfehlerArt ABI_15
	 */
	public static readonly ABI_15 : GostBelegungsfehler = new GostBelegungsfehler("ABI_15", 4, "ABI_15", GostBelegungsfehlerArt.BELEGUNG, "Sport kann nur als 2. oder als 4. Abiturfach gewählt werden.", null);

	/**
	 * BelegungsfehlerArt ABI_16
	 */
	public static readonly ABI_16 : GostBelegungsfehler = new GostBelegungsfehler("ABI_16", 5, "ABI_16", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Fächer, die keine Abiturfächer sind, müssen in Q2.2 mündlich belegt werden.", null);

	/**
	 * BelegungsfehlerArt ABI_17
	 */
	public static readonly ABI_17 : GostBelegungsfehler = new GostBelegungsfehler("ABI_17", 6, "ABI_17", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Das 3. Abiturfach muss von Q1.1 bis Q2.2 schriftlich belegt sein.", null);

	/**
	 * BelegungsfehlerArt
	 */
	public static readonly ABI_18 : GostBelegungsfehler = new GostBelegungsfehler("ABI_18", 7, "ABI_18", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Das 4. Abiturfach muss von Q1.1 bis Q2.1 schriftlich belegt sein.", null);

	/**
	 * BelegungsfehlerArt ABI_19: Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein. Dies kann nicht durch zwei Fremdsprachen erfüllt werden.
	 */
	public static readonly ABI_19 : GostBelegungsfehler = new GostBelegungsfehler("ABI_19", 8, "ABI_19", GostBelegungsfehlerArt.BELEGUNG, "Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein. Dies kann nicht durch zwei Fremdsprachen erfüllt werden.", null);

	/**
	 * BelegungsfehlerArt ABI_21: Es kann nur ein Fach 1. Abiturfach sein.
	 */
	public static readonly ABI_21 : GostBelegungsfehler = new GostBelegungsfehler("ABI_21", 9, "ABI_21", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 1. Abiturfach sein.", null);

	/**
	 * BelegungsfehlerArt ABI_22: Es kann nur ein Fach 2. Abiturfach sein.
	 */
	public static readonly ABI_22 : GostBelegungsfehler = new GostBelegungsfehler("ABI_22", 10, "ABI_22", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 2. Abiturfach sein.", null);

	/**
	 * BelegungsfehlerArt ABI_23: Es kann nur ein Fach 3. Abiturfach sein.
	 */
	public static readonly ABI_23 : GostBelegungsfehler = new GostBelegungsfehler("ABI_23", 11, "ABI_23", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 3. Abiturfach sein.", null);

	/**
	 * BelegungsfehlerArt ABI_24: Es kann nur ein Fach 4. Abiturfach sein.
	 */
	public static readonly ABI_24 : GostBelegungsfehler = new GostBelegungsfehler("ABI_24", 12, "ABI_24", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 4. Abiturfach sein.", null);

	/**
	 * BelegungsfehlerArt ABI_18: In der Einführungsphase müssen in jedem Halbjahr mindestens 10 Fächer belegt werden. Vertiefungskurse werden bei der Zählung nicht berücksichtigt.
	 *   <br>In EF.1 müssen mindestens 10 Kurse belegt werden. Bei der Kurszählung werden Vertiefungskurse nicht mitgezählt.
	 */
	public static readonly ANZ_10 : GostBelegungsfehler = new GostBelegungsfehler("ANZ_10", 13, "ANZ_10", GostBelegungsfehlerArt.BELEGUNG, "In der Einführungsphase müssen in jedem Halbjahr mindestens 10 Fächer belegt werden. Vertiefungskurse werden bei der Zählung nicht berücksichtigt.", "In EF.1 müssen mindestens 10 Kurse belegt werden. Bei der Kurszählung werden Vertiefungskurse nicht mitgezählt.");

	/**
	 * BelegungsfehlerArt ANZ_11_INFO
	 */
	public static readonly ANZ_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("ANZ_11_INFO", 14, "ANZ_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Die Stundenbandbreite sollte pro Halbjahr 32 bis 36 Stunden betragen, um eine gleichmäßige Stundenbelastung zu gewährleisten.", "Die Gesamtstundenzahl sollte 32 bis 36 Stunden betragen, um eine gleichmäßige Stundenbelastung in der Oberstufe zu gewährleisten.");

	/**
	 * BelegungsfehlerArt ANZ_12
	 */
	public static readonly ANZ_12 : GostBelegungsfehler = new GostBelegungsfehler("ANZ_12", 15, "ANZ_12", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase müssen mindestens 38 anrechenbare Kurse belegt werden.", null);

	/**
	 * BelegungsfehlerArt ANZ_13
	 */
	public static readonly ANZ_13 : GostBelegungsfehler = new GostBelegungsfehler("ANZ_13", 16, "ANZ_13", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase müssen mindestens 32 anrechenbare Kurse belegt werden.", null);

	/**
	 * BelegungsfehlerArt ANZ_20_INFO
	 */
	public static readonly ANZ_20_INFO : GostBelegungsfehler = new GostBelegungsfehler("ANZ_20_INFO", 17, "ANZ_20_INFO", GostBelegungsfehlerArt.HINWEIS, "In der Qualifikationsphase können sind nur maximal zwei im literarisch-künstlerischen Fachbereich anrechenbar.", null);

	/**
	 * BelegungsfehlerArt ANZ_21_INFO
	 */
	public static readonly ANZ_21_INFO : GostBelegungsfehler = new GostBelegungsfehler("ANZ_21_INFO", 18, "ANZ_21_INFO", GostBelegungsfehlerArt.HINWEIS, "Bei der Wahl eines Musik-Leistungskurse sind vokal- und instrumentalpraktische Kurse nicht in der Qualifikationsphase nicht anrechenbar.", null);

	/**
	 * BelegungsfehlerArt ANZ_22_INFO
	 */
	public static readonly ANZ_22_INFO : GostBelegungsfehler = new GostBelegungsfehler("ANZ_22_INFO", 19, "ANZ_22_INFO", GostBelegungsfehlerArt.HINWEIS, "Bei der Wahl eines Musik-Grundkurses als Abiturfach sind maximal 6 Kurse Musik-Kurse (Musik, vokal- oder instrumentalpraktischer Grundkurs) in der Qualifikationsphase anrechenbar.", null);

	/**
	 * BelegungsfehlerArt ANZ_23_INFO
	 */
	public static readonly ANZ_23_INFO : GostBelegungsfehler = new GostBelegungsfehler("ANZ_23_INFO", 20, "ANZ_23_INFO", GostBelegungsfehlerArt.HINWEIS, "In der Qualifikationsphase sind, wenn Musik nicht als Abiturfach gewählt wird, maximal 5 Kurse Musik-Kurse (Musik, vokal- oder instrumentalpraktischer Grundkurs) anrechenbar.", null);

	/**
	 * BelegungsfehlerArt BIL_4_INFO
	 */
	public static readonly BIL_4_INFO : GostBelegungsfehler = new GostBelegungsfehler("BIL_4_INFO", 21, "BIL_4_INFO", GostBelegungsfehlerArt.HINWEIS, "Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!", "Soll ein bilinguales Sachfach zum Sprachenschwerpunkt beitragen, so muss es schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt BIL_10
	 */
	public static readonly BIL_10 : GostBelegungsfehler = new GostBelegungsfehler("BIL_10", 22, "BIL_10", GostBelegungsfehlerArt.BELEGUNG, "Im bilingualen Bildungsgang muss die bilinguale Fremdsprache in EF.1 und EF.2 schriftlich und in Q1.1 bis Q2.2 als Leistungskurs belegt werden.", "Im bilingualen Bildungsgang muss die bilinguale Fremdsprache schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt BIL_11_INFO
	 */
	public static readonly BIL_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("BIL_11_INFO", 23, "BIL_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Im bilingualen Bildungsgang werden in EF.1 und EF.2 in der Regel zwei bilinguale Sachfächer belegt.", "Im bilingualen Bildungsgang sollten zwei bilinguale Sachfächer belegt werden.");

	/**
	 * BelegungsfehlerArt BIL_12
	 */
	public static readonly BIL_12 : GostBelegungsfehler = new GostBelegungsfehler("BIL_12", 24, "BIL_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Im bilingualen Bildungsgang muss ein bilinguales Sachfach durchgehend von Q1.1 bis Q2.1 schriftlich belegt werden.", null);

	/**
	 * BelegungsfehlerArt BIL_13
	 */
	public static readonly BIL_13 : GostBelegungsfehler = new GostBelegungsfehler("BIL_13", 25, "BIL_13", GostBelegungsfehlerArt.BELEGUNG, "Ein bilinguales Sachfach muss unter den Abiturfächern sein.", null);

	/**
	 * BelegungsfehlerArt BIL_14
	 */
	public static readonly BIL_14 : GostBelegungsfehler = new GostBelegungsfehler("BIL_14", 26, "BIL_14", GostBelegungsfehlerArt.BELEGUNG, "Es können nur bilinguale Sachfächer belegt werden, deren Sprache in der Sekundarstufe erlernt wurde.", "Es können nur bilinguale Sachfächer belegt werden, deren Sprache in der Sekundarstufe erlernt wurde.");

	/**
	 * BelegungsfehlerArt BIL_15
	 */
	public static readonly BIL_15 : GostBelegungsfehler = new GostBelegungsfehler("BIL_15", 27, "BIL_15", GostBelegungsfehlerArt.BELEGUNG, "Im bilingualen Bildungsgang muss in EF.1 und EF.2 mindestens ein bilinguales Sachfach belegt werden.", "Im bilingualen Bildungsgang muss mindestens ein bilinguales Sachfach belegt werden.");

	/**
	 * BelegungsfehlerArt D_10
	 */
	public static readonly D_10 : GostBelegungsfehler = new GostBelegungsfehler("D_10", 28, "D_10", GostBelegungsfehlerArt.BELEGUNG, "Deutsch muss von EF.1 bis Q2.2 belegt werden.", "Deutsch muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt D_11
	 */
	public static readonly D_11 : GostBelegungsfehler = new GostBelegungsfehler("D_11", 29, "D_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Deutsch muss von EF.1 bis wenigstens Q2.1 schriftlich belegt werden.", "Deutsch muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt E1BEL_10
	 */
	public static readonly E1BEL_10 : GostBelegungsfehler = new GostBelegungsfehler("E1BEL_10", 30, "E1BEL_10", GostBelegungsfehlerArt.BELEGUNG, "Bis auf Literatur, vokal- und instrumentalpraktische Kurse, Zusatzkurse, Vertiefungsfächer und Projektkurse können keine Fächer hinzugewählt werden, die nicht schon ab EF.1 belegt wurden.", null);

	/**
	 * BelegungsfehlerArt FS_10
	 */
	public static readonly FS_10 : GostBelegungsfehler = new GostBelegungsfehler("FS_10", 31, "FS_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine Fremdsprache muss von EF.1 bis Q2.2 durchgehend belegt werden. Handelt es sich hierbei um eine neu einsetzende Fremdsprache, so muss zusätzlich mindestens eine aus der SI fortgeführte Fremdsprache von EF.1 bis EF.2 belegt werden.", "Da die gewählte Fremdsprache in der Oberstufe nicht durchgehend angeboten wird, muss entweder zusätzliche eine neu einsetzende Fremdsprache, oder eine andere in der Sekundarstufe I begonnene Fremdsprache belegt werden.");

	/**
	 * BelegungsfehlerArt FS_11
	 */
	public static readonly FS_11 : GostBelegungsfehler = new GostBelegungsfehler("FS_11", 32, "FS_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mindestens eine durchgehend belegte Fremdsprache muss von EF.1 bis Q2.1 schriftlich belegt sein.", "Mindestens eine Fremdsprache muss in EF.1 schriftlich belegt werden. Die zu wählende Fremdsprache muss durchgehend angeboten werden.");

	/**
	 * BelegungsfehlerArt FS_12
	 */
	public static readonly FS_12 : GostBelegungsfehler = new GostBelegungsfehler("FS_12", 33, "FS_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In EF.1 und EF.2 müssen alle gewählten Fremdsprachenfächer schriftlich belegt werden.", "In EF.1 müssen alle belegten Fremdsprachen schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt FS_13
	 */
	public static readonly FS_13 : GostBelegungsfehler = new GostBelegungsfehler("FS_13", 34, "FS_13", GostBelegungsfehlerArt.BELEGUNG, "Bei unvollendeter 2. Fremdsprache, muss die in der Sekundarstufe 1 begonnene 2. Fremdsprache in EF schriftlich oder eine neu einsetzende Fremdsprache durchgehend schriftlich belegt werden.", "Wurde die 2. Fremdsprache erst ab Klasse 8 erlernt, muss die in der Sekundarstufe 1 begonnene 2. Fremdsprache oder eine neu einsetzende Fremdsprache schriftlich in EF.1 belegt werden.");

	/**
	 * BelegungsfehlerArt FS_14
	 */
	public static readonly FS_14 : GostBelegungsfehler = new GostBelegungsfehler("FS_14", 35, "FS_14", GostBelegungsfehlerArt.BELEGUNG, "Bei fehlender 2. Fremdsprache muss eine neu einsetzende Fremdsprache durchgehend schriftlich belegt werden.", "Wurde bisher keine 2. Fremdsprache erlernt, muss eine neu einsetzende Fremdsprache in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt FS_15
	 */
	public static readonly FS_15 : GostBelegungsfehler = new GostBelegungsfehler("FS_15", 36, "FS_15", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Kurse in neu einsetzenden Fremdsprachen müssen schriftlich belegt werden.", null);

	/**
	 * BelegungsfehlerArt FS_16
	 */
	public static readonly FS_16 : GostBelegungsfehler = new GostBelegungsfehler("FS_16", 37, "FS_16", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mindestens eine fortgeführte Fremdsprache muss in EF.1 und EF.2 schriftlich belegt werden.", "Mindestens eine in der Sekundarstufe I begonnene Fremdsprache muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt FS_17
	 */
	public static readonly FS_17 : GostBelegungsfehler = new GostBelegungsfehler("FS_17", 38, "FS_17", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Neu einsetzende Fremdsprachen dürfen nicht als Leistungskurs belegt werden.", null);

	/**
	 * BelegungsfehlerArt FS_18
	 */
	public static readonly FS_18 : GostBelegungsfehler = new GostBelegungsfehler("FS_18", 39, "FS_18", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine Fremdsprache muss von EF.1 bis Q2.2 belegt werden.", "Mindestens eine in der Sekundarstufe I begonnene Fremdsprache muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt FS_19_INFO
	 */
	public static readonly FS_19_INFO : GostBelegungsfehler = new GostBelegungsfehler("FS_19_INFO", 40, "FS_19_INFO", GostBelegungsfehlerArt.HINWEIS, "Eine erfolgreiche Feststellungsprüfung in der Muttersprache am Ende der Sekundarstufe I und am Ende von EF.2 ist notwendig, um die Fremdsprachenbedingungen zu erfüllen.", "Eine erfolgreiche Feststellungsprüfung in der Muttersprache am Ende der Sekundarstufe I und am Ende von EF.2 ist notwendig, um die Fremdsprachenbedingungen zu erfüllen.");

	/**
	 * BelegungsfehlerArt FS_20
	 */
	public static readonly FS_20 : GostBelegungsfehler = new GostBelegungsfehler("FS_20", 41, "FS_20", GostBelegungsfehlerArt.BELEGUNG, "Eine neu einsetzende Fremdsprache kann nur gewählt werden, wenn diese nicht zuvor in der Sekundarstaufe I belegt wurde. Die Sprachenfolge ist fehlerhaft.", "Eine neu einsetzende Fremdsprache kann nur gewählt werden, wenn diese nicht zuvor in der Sekundarstaufe I belegt wurde. Die Sprachenfolge ist fehlerhaft.");

	/**
	 * BelegungsfehlerArt FS_21
	 */
	public static readonly FS_21 : GostBelegungsfehler = new GostBelegungsfehler("FS_21", 42, "FS_21", GostBelegungsfehlerArt.BELEGUNG, "Eine fortgeführte Fremdsprache wurde in der Sprachenfolge als neu einsetzend eingetragen. Die Sprachenfolge ist fehlerhaft.", "Eine fortgeführte Fremdsprache wurde in der Sprachenfolge als neu einsetzend eingetragen. Die Sprachenfolge ist fehlerhaft.");

	/**
	 * BelegungsfehlerArt FS_22_INFO
	 */
	public static readonly FS_22_INFO : GostBelegungsfehler = new GostBelegungsfehler("FS_22_INFO", 43, "FS_22_INFO", GostBelegungsfehlerArt.HINWEIS, "Englisch wurde nicht in der Sprachenfolge eingetragen. Bitte Prüfen Sie die Sprachenfolge auf Korrektheit.", "Englisch wurde nicht in der Sprachenfolge eingetragen. Bitte Prüfen Sie die Sprachenfolge auf Korrektheit.");

	/**
	 * BelegungsfehlerArt FS_23
	 */
	public static readonly FS_23 : GostBelegungsfehler = new GostBelegungsfehler("FS_23", 44, "FS_23", GostBelegungsfehlerArt.BELEGUNG, "Es wird das Fach einer fortgeführten Fremdsprache belegt, zu dem es keine entsprechende Eintragung einer Belegung oder Sprachprüfung in der Sekundarstufe I gibt. Bitte die Sprachenfolge und Sprachprüfungen prüfen.", "Es wird das Fach einer fortgeführten Fremdsprache belegt, zu dem es keine entsprechende Eintragung einer Belegung oder Sprachprüfung in der Sekundarstufe I gibt. Bitte die Sprachenfolge und Sprachprüfungen prüfen.");

	/**
	 * BelegungsfehlerArt FS_24
	 */
	public static readonly FS_24 : GostBelegungsfehler = new GostBelegungsfehler("FS_24", 45, "FS_24", GostBelegungsfehlerArt.BELEGUNG, "Ist die Bedingungen für die Belegung einer zweiten Fremdsprache in der Sekundarstufe I noch nicht erfüllt worden, so müssen in der Einführungsphase zwei Fremdsprachen belegt werden.", "Ist die Bedingungen für die Belegung einer zweiten Fremdsprache in der Sekundarstufe I noch nicht erfüllt worden, so müssen in der Einführungsphase zwei Fremdsprachen belegt werden.");

	/**
	 * BelegungsfehlerArt FS_25
	 */
	public static readonly FS_25 : GostBelegungsfehler = new GostBelegungsfehler("FS_25", 46, "FS_25", GostBelegungsfehlerArt.BELEGUNG, "Es wurde keine fortführbare Fremdsprache in der Sprachbelegung gefunden. Bitte prüfen Sie die Sprachenfolge.", "Es wurde keine fortführbare Fremdsprache in der Sprachbelegung gefunden. Bitte prüfen Sie die Sprachenfolge.");

	/**
	 * BelegungsfehlerArt
	 */
	public static readonly GE_1_INFO : GostBelegungsfehler = new GostBelegungsfehler("GE_1_INFO", 47, "GE_1_INFO", GostBelegungsfehlerArt.HINWEIS, "Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!", "Wird Geschichte nicht in EF.1 belegt, so muss Geschichte in der Qualifikationsphase als Zusatzkurs gewählt werden.");

	/**
	 * BelegungsfehlerArt GE_1_INFO
	 */
	public static readonly GE_10 : GostBelegungsfehler = new GostBelegungsfehler("GE_10", 48, "GE_10", GostBelegungsfehlerArt.BELEGUNG, "Geschichte muss von EF.1 bis wenigstens Q1.2 oder als Zusatzkurs (in der Regel von Q2.1 bis Q2.2) belegt werden.", null);

	/**
	 * BelegungsfehlerArt GKS_10
	 */
	public static readonly GKS_10 : GostBelegungsfehler = new GostBelegungsfehler("GKS_10", 49, "GKS_10", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase sind pro Halbjahr mindestens 7 Fächer in Grundkursen zu wählen.", null);

	/**
	 * BelegungsfehlerArt GKS_11
	 */
	public static readonly GKS_11 : GostBelegungsfehler = new GostBelegungsfehler("GKS_11", 50, "GKS_11", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase sind pro Halbjahr mindestens 6 Fächer in Grundkursen zu wählen.", null);

	/**
	 * BelegungsfehlerArt GW_10
	 */
	public static readonly GW_10 : GostBelegungsfehler = new GostBelegungsfehler("GW_10", 51, "GW_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine Gesellschaftswissenschaft muss von Q1.1 bis Q2.2 durchgehend belegt werden.", "Mindestens eine Gesellschaftswissenschaft muss in EF.1 belegt werden und durchgängig belegbar sein.");

	/**
	 * BelegungsfehlerArt GW_11
	 */
	public static readonly GW_11 : GostBelegungsfehler = new GostBelegungsfehler("GW_11", 52, "GW_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In EF.1 und EF.2 muss mindestens eine Gesellschaftswissenschaft schriftlich belegt sein.", "Mindestens eine Gesellschaftswissenschaft muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt GW_12
	 */
	public static readonly GW_12 : GostBelegungsfehler = new GostBelegungsfehler("GW_12", 53, "GW_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mindestens eine Gesellschaftswissenschaft oder Religionslehre muss von Q1.1 bis wenigstens Q2.1 schriftlich belegt werden.", null);

	/**
	 * BelegungsfehlerArt IGF_10
	 */
	public static readonly IGF_10 : GostBelegungsfehler = new GostBelegungsfehler("IGF_10", 54, "IGF_10", GostBelegungsfehlerArt.BELEGUNG, "Inhaltsgleiche Fächer und Projektkurse dürfen in jedem Halbjahr nur einmal belegt werden.", "Inhaltsgleiche Fächer dürfen nur einmal belegt werden.");

	/**
	 * BelegungsfehlerArt KU_MU_10
	 */
	public static readonly KU_MU_10 : GostBelegungsfehler = new GostBelegungsfehler("KU_MU_10", 55, "KU_MU_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eines der Fächer Kunst oder Musik muss von EF.1 bis wenigstens Q1.2 durchgehend belegt werden. In der Qualifikationsphase kann auch alternativ Literatur, ein vokalpraktisches oder ein instrumentalpraktisches Fach mit zwei Kursen belegt werden.", "Mindestens eines der Fächer Kunst oder Musik muss in EF.1 belegt werden");

	/**
	 * BelegungsfehlerArt L_10_INFO
	 */
	public static readonly L_10_INFO : GostBelegungsfehler = new GostBelegungsfehler("L_10_INFO", 56, "L_10_INFO", GostBelegungsfehlerArt.HINWEIS, "Um das Latinum zu erlangen muss Latein in EF.1 und EF.2 belegt werden.", "Um das Latinum zu erlangen muss Latein in EF.1 schriftlich fortgeführt werden.");

	/**
	 * BelegungsfehlerArt L_11_INFO
	 */
	public static readonly L_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("L_11_INFO", 57, "L_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Um das Latinum zu erlangen muss Latein mindestens bis Q1.2, je nach Stundenvolumen sogar bis Q2.2 belegt werden.", null);

	/**
	 * BelegungsfehlerArt L_12_INFO
	 */
	public static readonly L_12_INFO : GostBelegungsfehler = new GostBelegungsfehler("L_12_INFO", 58, "L_12_INFO", GostBelegungsfehlerArt.HINWEIS, "Um das Latinum zu erlangen muss Latein mindestens bis EF.2, je nach Beginn und Stundenvolumen sogar bis Q2.2 belegt werden.", null);

	/**
	 * BelegungsfehlerArt LI_IV_10
	 */
	public static readonly LI_IV_10 : GostBelegungsfehler = new GostBelegungsfehler("LI_IV_10", 59, "LI_IV_10", GostBelegungsfehlerArt.BELEGUNG, "Die Fächer Literatur, instrumentalpraktischer bzw. vokalpraktischer Grundkurs dürfen maximal in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	/**
	 * BelegungsfehlerArt LI_IV_11
	 */
	public static readonly LI_IV_11 : GostBelegungsfehler = new GostBelegungsfehler("LI_IV_11", 60, "LI_IV_11", GostBelegungsfehlerArt.BELEGUNG, "Es darf nur eins der Fächer Literatur, IP oder VP belegt werden. (Schulen mit genehmigtem musisch-künstlerischem Schwerpunkt müssen betroffene Schüler in SchILD-NRW manuell zulassen).", null);

	/**
	 * BelegungsfehlerArt LK_10
	 */
	public static readonly LK_10 : GostBelegungsfehler = new GostBelegungsfehler("LK_10", 61, "LK_10", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase müssen zwei Fächer durchgehend in Leistungskursen belegt werden.", null);

	/**
	 * BelegungsfehlerArt LK_11
	 */
	public static readonly LK_11 : GostBelegungsfehler = new GostBelegungsfehler("LK_11", 62, "LK_11", GostBelegungsfehlerArt.BELEGUNG, "Es dürfen nicht mehr als zwei Fächer als Leistungskurse belegt werden.", null);

	/**
	 * BelegungsfehlerArt LK1_10
	 */
	public static readonly LK1_10 : GostBelegungsfehler = new GostBelegungsfehler("LK1_10", 63, "LK1_10", GostBelegungsfehlerArt.BELEGUNG, "Erstes Abiturfach nicht eindeutig identifizierbar.", null);

	/**
	 * BelegungsfehlerArt LK1_11
	 */
	public static readonly LK1_11 : GostBelegungsfehler = new GostBelegungsfehler("LK1_11", 64, "LK1_11", GostBelegungsfehlerArt.BELEGUNG, "Das erste Leistungskursfach muss eine fortgeführte Fremdsprache, Mathematik, eine klassische Naturwissenschaft oder Deutsch sein.", null);

	/**
	 * BelegungsfehlerArt LK1_12
	 */
	public static readonly LK1_12 : GostBelegungsfehler = new GostBelegungsfehler("LK1_12", 65, "LK1_12", GostBelegungsfehlerArt.BELEGUNG, "Ist Deutsch erstes Leistungskursfach, muss Mathematik oder eine Fremdsprache unter den vier Abiturfächern sein.", null);

	/**
	 * BelegungsfehlerArt LK1_13
	 */
	public static readonly LK1_13 : GostBelegungsfehler = new GostBelegungsfehler("LK1_13", 66, "LK1_13", GostBelegungsfehlerArt.BELEGUNG, "Die Abiturfächer müssen alle drei Aufgabenfelder abdecken. Insgesamt sind vier Abiturfächer zu belegen.", null);

	/**
	 * BelegungsfehlerArt M_10
	 */
	public static readonly M_10 : GostBelegungsfehler = new GostBelegungsfehler("M_10", 67, "M_10", GostBelegungsfehlerArt.BELEGUNG, "Mathematik muss von EF.1 bis Q2.2 belegt werden.", "Mathematik muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt M_11
	 */
	public static readonly M_11 : GostBelegungsfehler = new GostBelegungsfehler("M_11", 68, "M_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mathematik muss von EF.1 bis wenigstens Q2.1 schriftlich belegt werden.", "Mathematik muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt NW_10
	 */
	public static readonly NW_10 : GostBelegungsfehler = new GostBelegungsfehler("NW_10", 69, "NW_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine klassische Naturwissenschaft (Physik, Biologie, Chemie) muss durchgehend von Q1.1 bis Q2.2 belegt werden.", "Mindestens eines der Fächer Physik, Chemie oder Biologie muss in EF.1 belegt werden und durchgängig belegbar sein.");

	/**
	 * BelegungsfehlerArt NW_11
	 */
	public static readonly NW_11 : GostBelegungsfehler = new GostBelegungsfehler("NW_11", 70, "NW_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In EF.1 und EF.2 muss mindestens eine klassische Naturwissenschaft schriftlich belegt sein.", "Mindestens eines der Fächer Physik, Chemie oder Biologie muss in EF.1 schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt NW_FS_10
	 */
	public static readonly NW_FS_10 : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_10", 71, "NW_FS_10", GostBelegungsfehlerArt.BELEGUNG, "Von EF.1 bis Q2.2 müssen entweder zwei Naturwissenschaften oder zwei Fremdsprachen durchgehend gewählt werden. Hierbei ist eine Naturwissenschaft oder sind zwei Fremdsprachen schriftlich zu belegen. Zu den Fremdsprachen zählen auch in einer weiteren Fremdsprache unterrichtete Sachfächer.", "In EF.1 müssen entweder zwei Naturwissenschaften oder zwei Fremdsprachen belegt werden. Hierbei ist eine Naturwissenschaft oder sind zwei Fremdsprachen schriftlich zu belegen. Zu den Fremdsprachen zählen auch in einer weiteren Fremdsprache unterrichtete Sachfächer.");

	/**
	 * BelegungsfehlerArt NW_FS_11
	 */
	public static readonly NW_FS_11 : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_11", 72, "NW_FS_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Zwei Fremdsprachen oder eines von mindestens zwei naturwissenschaftlichen Fächern muss von Q1.1 bis wenigstens Q2.1 schriftlich belegt werden.", null);

	/**
	 * BelegungsfehlerArt NW_FS_12_INFO
	 */
	public static readonly NW_FS_12_INFO : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_12_INFO", 73, "NW_FS_12_INFO", GostBelegungsfehlerArt.HINWEIS, "Da von EF.1 bis Q2.2 weniger als zwei naturwissenschaftliche Fächer durchgehend belegt wurden, oder kein naturwissenschaftliches Fach schriftlich belegt wurde, liegt ausschließlich ein Sprachenschwerpunkt vor.", "Da in EF.1 weniger als zwei Naturwissenschaften belegt, oder keine schriftlich belegt wurden, müssen zwei Fremdsprachen bis Q2.2 durchgehend schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt NW_FS_13_INFO
	 */
	public static readonly NW_FS_13_INFO : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_13_INFO", 74, "NW_FS_13_INFO", GostBelegungsfehlerArt.HINWEIS, "Da von EF.1 bis Q2.2 weniger als zwei Fremdsprachen schriftlich belegt wurden, liegt ausschließlich ein naturwissenschaftlicher Schwerpunkt vor.", "Da in EF.1 weniger als zwei Fremdsprachen schriftlich belegt wurden, müssen zwei Naturwissenschaften bis Q2.2 belegt, davon mindestens eine schriftlich belegt werden.");

	/**
	 * BelegungsfehlerArt PF_10
	 */
	public static readonly PF_10 : GostBelegungsfehler = new GostBelegungsfehler("PF_10", 75, "PF_10", GostBelegungsfehlerArt.BELEGUNG, "In der Einführungsphase können keine Projektkurse belegt werden.", "In EF.1 können keine Projektkurse belegt werden.");

	/**
	 * BelegungsfehlerArt PF_11
	 */
	public static readonly PF_11 : GostBelegungsfehler = new GostBelegungsfehler("PF_11", 76, "PF_11", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase kann maximal ein Projektkurs belegt werden.", null);

	/**
	 * BelegungsfehlerArt PF_12
	 */
	public static readonly PF_12 : GostBelegungsfehler = new GostBelegungsfehler("PF_12", 77, "PF_12", GostBelegungsfehlerArt.BELEGUNG, "Projektkurse können nur dann belegt werden, wenn gleichzeitig oder maximal zwei Halbjahre vorher auch das Leitfach im gleichem Umfang belegt wurde.", null);

	/**
	 * BelegungsfehlerArt PF_13
	 */
	public static readonly PF_13 : GostBelegungsfehler = new GostBelegungsfehler("PF_13", 78, "PF_13", GostBelegungsfehlerArt.BELEGUNG, "Ein Projektkurs kann nur belegt werden, wenn in der Qualifikationsphase zeitgleich oder vorab auch sein Leitfach zwei Halbjahre lang belegt wurde.", null);

	/**
	 * BelegungsfehlerArt PF_14
	 */
	public static readonly PF_14 : GostBelegungsfehler = new GostBelegungsfehler("PF_14", 79, "PF_14", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase kann maximal ein Projektkurs in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	/**
	 * BelegungsfehlerArt PF_15
	 */
	public static readonly PF_15 : GostBelegungsfehler = new GostBelegungsfehler("PF_15", 80, "PF_15", GostBelegungsfehlerArt.BELEGUNG, "Es existiert kein Projektkurs, der als besondere Lernleistung eingebracht werden kann.", null);

	/**
	 * BelegungsfehlerArt PF_16_INFO
	 */
	public static readonly PF_16_INFO : GostBelegungsfehler = new GostBelegungsfehler("PF_16_INFO", 81, "PF_16_INFO", GostBelegungsfehlerArt.HINWEIS, "Wird der Projektkurs als besondere Lernleistung in das Abitur eingebracht, so zählt er nicht mehr zu den einbringungsfähigen Kursen in Block I.", null);

	/**
	 * BelegungsfehlerArt PF_17_INFO
	 */
	public static readonly PF_17_INFO : GostBelegungsfehler = new GostBelegungsfehler("PF_17_INFO", 82, "PF_17_INFO", GostBelegungsfehlerArt.HINWEIS, "Ein Projektkurs soll nur im Ausnahmefall abgewählt werden. Bei einem abgewählten Projektkurs werden lediglich die Wochenstunden auf die Laufbahn angerechnet.", null);

	/**
	 * BelegungsfehlerArt PF_18
	 */
	public static readonly PF_18 : GostBelegungsfehler = new GostBelegungsfehler("PF_18", 83, "PF_18", GostBelegungsfehlerArt.BELEGUNG, "Ein Projektkurs kann nicht in der Q2.2 beginnen.", null);

	/**
	 * BelegungsfehlerArt PF_19
	 */
	public static readonly PF_19 : GostBelegungsfehler = new GostBelegungsfehler("PF_19", 84, "PF_19", GostBelegungsfehlerArt.BELEGUNG, "Ein Projektkurs kann als Leitfach kein anderes Projektkursfach, ein Vertiefungskursfach oder ein Ersatzfach aus dem literarisch künstlerischen Bereich haben.", null);

	/**
	 * BelegungsfehlerArt RE_10
	 */
	public static readonly RE_10 : GostBelegungsfehler = new GostBelegungsfehler("RE_10", 85, "RE_10", GostBelegungsfehlerArt.BELEGUNG, "Religionslehre muss wenigstens von EF.1-Q1.2 durchgehend belegt werden. Als Ersatz kann Philosophie dienen, sofern Philosophie nicht die einzige von EF.1 bis Q2.2 durchgehend belegte Gesellschaftswissenschaft ist. In diesen Fällen muss ein weiteres Fach der Gesellschaftswissenschaften als Religionsersatz dienen.", "Ein Religionskurs muss in EF.1 belegt werden. Als Ersatz kann Philosophie belegt werden, sofern eine weitere Gesellschaftswissenschaft bis zum Abitur belegt wird.");

	/**
	 * BelegungsfehlerArt RE_11
	 */
	public static readonly RE_11 : GostBelegungsfehler = new GostBelegungsfehler("RE_11", 86, "RE_11", GostBelegungsfehlerArt.BELEGUNG, "Philosophie darf nicht die einzige durchgehend von Q1.1 bis Q2.2 belegte Geisteswissenschaft sein.", null);

	/**
	 * BelegungsfehlerArt SP_10
	 */
	public static readonly SP_10 : GostBelegungsfehler = new GostBelegungsfehler("SP_10", 87, "SP_10", GostBelegungsfehlerArt.BELEGUNG, "Sport muss von EF.1 bis Q2.2 belegt werden.", "Sport muss in EF.1 belegt werden.");

	/**
	 * BelegungsfehlerArt STD_10
	 */
	public static readonly STD_10 : GostBelegungsfehler = new GostBelegungsfehler("STD_10", 88, "STD_10", GostBelegungsfehlerArt.BELEGUNG, "Der Pflichtunterricht darf 102 Stunden nicht unterschreiten.", null);

	/**
	 * BelegungsfehlerArt STD_11_INFO
	 */
	public static readonly STD_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("STD_11_INFO", 89, "STD_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Der Pflichtunterricht darf nur in begründeten Ausnahmefällen 102 Stunden unterschreiten.", null);

	/**
	 * BelegungsfehlerArt SW_1_INFO
	 */
	public static readonly SW_1_INFO : GostBelegungsfehler = new GostBelegungsfehler("SW_1_INFO", 90, "SW_1_INFO", GostBelegungsfehlerArt.HINWEIS, "Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!", "Wird Sozialwissenschaften nicht in EF.1 belegt, so muss Sozialwissenschaften in der Qualifikationsphase als Zusatzkurs gewählt werden.");

	/**
	 * BelegungsfehlerArt SW_10
	 */
	public static readonly SW_10 : GostBelegungsfehler = new GostBelegungsfehler("SW_10", 91, "SW_10", GostBelegungsfehlerArt.BELEGUNG, "Sozialwissenschaften muss von EF.1 bis wenigstens Q1.2 oder als Zusatzkurs (in der Regel von Q2.1 bis Q2.2) belegt werden.", null);

	/**
	 * BelegungsfehlerArt VF_10
	 */
	public static readonly VF_10 : GostBelegungsfehler = new GostBelegungsfehler("VF_10", 92, "VF_10", GostBelegungsfehlerArt.BELEGUNG, "In der Einführungsphase können maximal vier Vertiefungskurse belegt werden.", null);

	/**
	 * BelegungsfehlerArt VF_11
	 */
	public static readonly VF_11 : GostBelegungsfehler = new GostBelegungsfehler("VF_11", 93, "VF_11", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase können maximal zwei Vertiefungskurse belegt werden.", null);

	/**
	 * BelegungsfehlerArt WST_10
	 */
	public static readonly WST_10 : GostBelegungsfehler = new GostBelegungsfehler("WST_10", 94, "WST_10", GostBelegungsfehlerArt.BELEGUNG, "Die Summe der durchschnittlichen Jahreskursstunden von EF.1 bis Q2.2 darf 100 nicht unterschreiten.", null);

	/**
	 * BelegungsfehlerArt WST_20
	 */
	public static readonly WST_20 : GostBelegungsfehler = new GostBelegungsfehler("WST_20", 95, "WST_20", GostBelegungsfehlerArt.BELEGUNG, "Die durchschnittliche Wochenstundenzahl muss in der Einführungsphase mindestens 34 Stunden betragen.", null);

	/**
	 * BelegungsfehlerArt WST_21
	 */
	public static readonly WST_21 : GostBelegungsfehler = new GostBelegungsfehler("WST_21", 96, "WST_21", GostBelegungsfehlerArt.BELEGUNG, "Die durchschnittliche Wochenstundenzahl muss in der Qualifikationsphase mindestens 34 Stunden betragen.", null);

	/**
	 * BelegungsfehlerArt ZK_10
	 */
	public static readonly ZK_10 : GostBelegungsfehler = new GostBelegungsfehler("ZK_10", 97, "ZK_10", GostBelegungsfehlerArt.BELEGUNG, "Ein Zusatzkurs in Geschichte oder Sozialwissenschaften kann nur angewählt werden, wenn das Fach im vorangegangenen Halbjahr nicht belegt wurde.", null);

	/**
	 * BelegungsfehlerArt ZK_11
	 */
	public static readonly ZK_11 : GostBelegungsfehler = new GostBelegungsfehler("ZK_11", 98, "ZK_11", GostBelegungsfehlerArt.BELEGUNG, "Ein Zusatzkurs in Geschichte oder Sozialwissenschaften muss in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	/**
	 * BelegungsfehlerArt ZK_12
	 */
	public static readonly ZK_12 : GostBelegungsfehler = new GostBelegungsfehler("ZK_12", 99, "ZK_12", GostBelegungsfehlerArt.BELEGUNG, "Zusatzkurse dürfen maximal in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	/**
	 * BelegungsfehlerArt ZK_13
	 */
	public static readonly ZK_13 : GostBelegungsfehler = new GostBelegungsfehler("ZK_13", 100, "ZK_13", GostBelegungsfehlerArt.BELEGUNG, "Zusatzkurse dürfen nur einmal pro Fach belegt werden und können nicht in einem bilingualen Fach gewählt werden.", null);

	/**
	 * BelegungsfehlerArt ZK_14
	 */
	public static readonly ZK_14 : GostBelegungsfehler = new GostBelegungsfehler("ZK_14", 101, "ZK_14", GostBelegungsfehlerArt.SCHULSPEZIFISCH, "Zusatzkurse in Geschichte werden in diesem Jahrgang nicht angeboten.", null);

	/**
	 * BelegungsfehlerArt ZK_15
	 */
	public static readonly ZK_15 : GostBelegungsfehler = new GostBelegungsfehler("ZK_15", 102, "ZK_15", GostBelegungsfehlerArt.SCHULSPEZIFISCH, "Zusatzkurse in Sozialwissenschaften werden in diesem Jahrgang nicht angeboten.", null);

	/**
	 * BelegungsfehlerArt ZK_16
	 */
	public static readonly ZK_16 : GostBelegungsfehler = new GostBelegungsfehler("ZK_16", 103, "ZK_16", GostBelegungsfehlerArt.SCHULSPEZIFISCH, "Der Beginn des Zusatzkurses in Geschichte entspricht nicht der Einstellung in diesem Jahrgang.", null);

	/**
	 * BelegungsfehlerArt ZK_17
	 */
	public static readonly ZK_17 : GostBelegungsfehler = new GostBelegungsfehler("ZK_17", 104, "ZK_17", GostBelegungsfehlerArt.SCHULSPEZIFISCH, "Der Beginn des Zusatzkurses in Sozialwissenschaften entspricht nicht der Einstellung in diesem Jahrgang.", null);

	/**
	 * BelegungsfehlerArt ZK_18
	 */
	public static readonly ZK_18 : GostBelegungsfehler = new GostBelegungsfehler("ZK_18", 105, "ZK_18", GostBelegungsfehlerArt.BELEGUNG, "Im Anschluss an zwei Zusatzkursbelegungen darf das Fach nicht weiter belegt werden.", null);

	/**
	 * BelegungsfehlerArt KOMBI_1
	 */
	public static readonly KOMBI_1 : GostBelegungsfehler = new GostBelegungsfehler("KOMBI_1", 106, "KOMBI_1", GostBelegungsfehlerArt.SCHULSPEZIFISCH, "Es wurde eine in diesem Jahrgang nicht erlaubte Fächerkombination gewählt.", "Es wurde eine in diesem Jahrgang nicht erlaubte Fächerkombination gewählt.");

	/**
	 * BelegungsfehlerArt KOMBI_2
	 */
	public static readonly KOMBI_2 : GostBelegungsfehler = new GostBelegungsfehler("KOMBI_2", 107, "KOMBI_2", GostBelegungsfehlerArt.SCHULSPEZIFISCH, "Es wurde eine in diesem Jahrgang geforderte Fächerkombination nicht erfüllt.", "Es wurde eine in diesem Jahrgang geforderte Fächerkombination nicht erfüllt.");

	/**
	 * Der eindeutige Code des Belegungsfehlers
	 */
	public readonly code : string;

	/**
	 * Die Art des Fehlers
	 */
	public readonly art : GostBelegungsfehlerArt;

	/**
	 * Der Text des Fehlers, der bei einer Gesamtprüfung ausgegeben wird
	 */
	public readonly textGESAMT : string;

	/**
	 * Der alternative Text, der bei der eingeschränkten Prüfung für die EF.1 ausgegeben wird
	 */
	public readonly textEF1 : string;

	/**
	 * Erstellt einen neuen Belegungsfehler für die Aufzählung (s.o.). Dabei wird ein
	 * Text für die Gesamtprüfung und die EF.1-Prüfung angegeben.
	 *
	 * @param code         der eindeutige Code des Belegungsfehlers
	 * @param art          die Fehlerart (Belegungsfehler, Schriftlichkeit oder Information)
	 * @param textGESAMT   der zugeordnete Text für die Gesamtbelegprüfung oder null
	 * @param textEF1      der zugeordnete Text für die EF.1-Prüfung oder null
	 */
	private constructor(name : string, ordinal : number, code : string, art : GostBelegungsfehlerArt, textGESAMT : string | null, textEF1 : string | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostBelegungsfehler.all_values_by_ordinal.push(this);
		GostBelegungsfehler.all_values_by_name.set(name, this);
		this.code = code;
		this.art = art;
		this.textGESAMT = (textGESAMT !== null) ? textGESAMT : "Programmfehler: Diese Belegungsfehlerart ist für eine Gesamt-Prüfung nicht vorgesehen!";
		this.textEF1 = (textEF1 !== null) ? textEF1 : "Programmfehler: Diese Belegungsfehlerart ist für eine Prüfung eingeschränkt auf die EF.1 nicht vorgesehen!";
	}

	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler nur um eine Information
	 * und nicht um einen "echten" Fehler handelt.
	 *
	 * @return true, falls es sich nur um eine Information handelt, sonst false
	 */
	public istInfo() : boolean {
		return (this.art as unknown === GostBelegungsfehlerArt.HINWEIS as unknown);
	}

	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler um einen "echten" Fehler handelt
	 * und nicht nur um eine Information.
	 *
	 * @return true, falls es sich um einen "echten" Fehler handelt, sonst false
	 */
	public istFehler() : boolean {
		return (this.art as unknown !== GostBelegungsfehlerArt.HINWEIS as unknown);
	}

	/**
	 * Gibt die Art des Belegungsfehlers zurück.
	 *
	 * @return die Art des Belegungsfehlers
	 */
	public getArt() : GostBelegungsfehlerArt {
		return this.art;
	}

	/**
	 * Gibt je nach angegebenener Belegprüfungsart den zugehörigen Text für den Belegungsfehler
	 * zurück.
	 *
	 * @param pruefArt   die Belegprüfungsart
	 *
	 * @return der zugehörige Text des Belegungsfehlers
	 */
	public getText(pruefArt : GostBelegpruefungsArt) : string {
		if (JavaObject.equalsTranspiler(pruefArt, (GostBelegpruefungsArt.EF1)))
			return this.textEF1;
		if (JavaObject.equalsTranspiler(pruefArt, (GostBelegpruefungsArt.GESAMT)))
			return this.textGESAMT;
		return this.textGESAMT;
	}

	public toString() : string {
		return this.code;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof GostBelegungsfehler))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : GostBelegungsfehler) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostBelegungsfehler> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostBelegungsfehler | null {
		const tmp : GostBelegungsfehler | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegungsfehler', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_GostBelegungsfehler(obj : unknown) : GostBelegungsfehler {
	return obj as GostBelegungsfehler;
}
