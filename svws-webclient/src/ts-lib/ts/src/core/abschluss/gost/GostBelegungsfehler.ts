import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBelegpruefungsArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt } from '../../../core/abschluss/gost/GostBelegpruefungsArt';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostBelegungsfehlerArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehlerArt } from '../../../core/abschluss/gost/GostBelegungsfehlerArt';

export class GostBelegungsfehler extends JavaObject {

	public static readonly ABI_10 : GostBelegungsfehler = new GostBelegungsfehler("ABI_10", GostBelegungsfehlerArt.BELEGUNG, "Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein.", null);

	public static readonly ABI_11 : GostBelegungsfehler = new GostBelegungsfehler("ABI_11", GostBelegungsfehlerArt.BELEGUNG, "Religionslehre und Sport dürfen nicht gleichzeitig Abiturfächer sein.", null);

	public static readonly ABI_12 : GostBelegungsfehler = new GostBelegungsfehler("ABI_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 3. Abiturfach schriftlich belegt sein.", null);

	public static readonly ABI_13 : GostBelegungsfehler = new GostBelegungsfehler("ABI_13", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 4. Abiturfach mündlich belegt sein.", null);

	public static readonly ABI_15 : GostBelegungsfehler = new GostBelegungsfehler("ABI_15", GostBelegungsfehlerArt.BELEGUNG, "Sport kann nur als 2. oder als 4. Abiturfach gewählt werden.", null);

	public static readonly ABI_16 : GostBelegungsfehler = new GostBelegungsfehler("ABI_16", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Fächer, die keine Abiturfächer sind, müssen in Q2.2 mündlich belegt werden.", null);

	public static readonly ABI_17 : GostBelegungsfehler = new GostBelegungsfehler("ABI_17", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Das 3. Abiturfach muss von Q1.1 bis Q2.2 schriftlich belegt sein.", null);

	public static readonly ABI_18 : GostBelegungsfehler = new GostBelegungsfehler("ABI_18", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Das 4. Abiturfach muss von Q1.1 bis Q2.1 schriftlich belegt sein.", null);

	public static readonly ABI_19 : GostBelegungsfehler = new GostBelegungsfehler("ABI_19", GostBelegungsfehlerArt.BELEGUNG, "Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein. Dies kann nicht durch zwei Fremdsprachen erfüllt werden.", null);

	public static readonly ABI_21 : GostBelegungsfehler = new GostBelegungsfehler("ABI_21", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 1. Abiturfach sein.", null);

	public static readonly ABI_22 : GostBelegungsfehler = new GostBelegungsfehler("ABI_22", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 2. Abiturfach sein.", null);

	public static readonly ABI_23 : GostBelegungsfehler = new GostBelegungsfehler("ABI_23", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 3. Abiturfach sein.", null);

	public static readonly ABI_24 : GostBelegungsfehler = new GostBelegungsfehler("ABI_24", GostBelegungsfehlerArt.BELEGUNG, "Es kann nur ein Fach 4. Abiturfach sein.", null);

	public static readonly ANZ_10 : GostBelegungsfehler = new GostBelegungsfehler("ANZ_10", GostBelegungsfehlerArt.BELEGUNG, "In der Einführungsphase müssen in jedem Halbjahr mindestens 10 Fächer belegt werden. Vertiefungskurse werden bei der Zählung nicht berücksichtigt.", "In EF.1 müssen mindestens 10 Kurse belegt werden. Bei der Kurszählung werden Vertiefungskurse nicht mitgezählt.");

	public static readonly ANZ_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("ANZ_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Die Stundenbandbreite sollte pro Halbjahr 32 bis 36 Stunden betragen, um eine gleichmäßige Stundenbelastung zu gewährleisten.", "Die Gesamtstundenzahl sollte 32 bis 36 Stunden betragen, um eine gleichmäßige Stundenbelastung in der Oberstufe zu gewährleisten.");

	public static readonly ANZ_12 : GostBelegungsfehler = new GostBelegungsfehler("ANZ_12", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase müssen mindestens 38 anrechenbare Kurse belegt werden.", null);

	public static readonly ANZ_13 : GostBelegungsfehler = new GostBelegungsfehler("ANZ_13", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase müssen mindestens 32 anrechenbare Kurse belegt werden.", null);

	public static readonly BIL_4_INFO : GostBelegungsfehler = new GostBelegungsfehler("BIL_4_INFO", GostBelegungsfehlerArt.HINWEIS, "Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!", "Soll ein bilinguales Sachfach zum Sprachenschwerpunkt beitragen, so muss es schriftlich belegt werden.");

	public static readonly BIL_10 : GostBelegungsfehler = new GostBelegungsfehler("BIL_10", GostBelegungsfehlerArt.BELEGUNG, "Im bilingualen Bildungsgang muss die bilinguale Fremdsprache in EF.1 und EF.2 schriftlich und in Q1.1 bis Q2.2 als Leistungskurs belegt werden.", "Im bilingualen Bildungsgang muss die bilinguale Fremdsprache schriftlich belegt werden.");

	public static readonly BIL_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("BIL_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Im bilingualen Bildungsgang werden in EF.1 und EF.2 in der Regel zwei bilinguale Sachfächer belegt.", "Im bilingualen Bildungsgang sollten zwei bilinguale Sachfächer belegt werden.");

	public static readonly BIL_12 : GostBelegungsfehler = new GostBelegungsfehler("BIL_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Im bilingualen Bildungsgang muss ein bilinguales Sachfach durchgehend von Q1.1 bis Q2.1 schriftlich belegt werden.", null);

	public static readonly BIL_13 : GostBelegungsfehler = new GostBelegungsfehler("BIL_13", GostBelegungsfehlerArt.BELEGUNG, "Ein bilinguales Sachfach muss unter den Abiturfächern sein.", null);

	public static readonly BIL_14 : GostBelegungsfehler = new GostBelegungsfehler("BIL_14", GostBelegungsfehlerArt.BELEGUNG, "Es können nur bilinguale Sachfächer belegt werden, deren Sprache in der Sekundarstufe erlernt wurde.", "Es können nur bilinguale Sachfächer belegt werden, deren Sprache in der Sekundarstufe erlernt wurde.");

	public static readonly BIL_15 : GostBelegungsfehler = new GostBelegungsfehler("BIL_15", GostBelegungsfehlerArt.BELEGUNG, "Im bilingualen Bildungsgang muss in EF.1 und EF.2 mindestens ein bilinguales Sachfach belegt werden.", "Im bilingualen Bildungsgang muss mindestens ein bilinguales Sachfach belegt werden.");

	public static readonly D_10 : GostBelegungsfehler = new GostBelegungsfehler("D_10", GostBelegungsfehlerArt.BELEGUNG, "Deutsch muss von EF.1 bis Q2.2 belegt werden.", "Deutsch muss in EF.1 schriftlich belegt werden.");

	public static readonly D_11 : GostBelegungsfehler = new GostBelegungsfehler("D_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Deutsch muss von EF.1 bis wenigstens Q2.1 schriftlich belegt werden.", "Deutsch muss in EF.1 schriftlich belegt werden.");

	public static readonly E1BEL_10 : GostBelegungsfehler = new GostBelegungsfehler("E1BEL_10", GostBelegungsfehlerArt.BELEGUNG, "Bis auf Literatur, vokal- und instrumentalpraktische Kurse, Zusatzkurse, Vertiefungsfächer und Projektkurse können keine Fächer hinzugewählt werden, die nicht schon ab EF.1 belegt wurden.", null);

	public static readonly FS_10 : GostBelegungsfehler = new GostBelegungsfehler("FS_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine Fremdsprache muss von EF.1 bis Q2.2 durchgehend belegt werden. Handelt es sich hierbei um eine neu einsetzende Fremdsprache, so muss zusätzlich mindestens eine aus der SI fortgeführte Fremdsprache von EF.1 bis EF.2 belegt werden.", "Da die gewählte Fremdsprache in der Oberstufe nicht durchgehend angeboten wird, muss entweder zusätzliche eine neu einsetzende Fremdsprache, oder eine andere in der Sekundarstufe I begonnene Fremdsprache belegt werden.");

	public static readonly FS_11 : GostBelegungsfehler = new GostBelegungsfehler("FS_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mindestens eine durchgehend belegte Fremdsprache muss von EF.1 bis Q2.1 schriftlich belegt sein.", "Mindestens eine Fremdsprache muss in EF.1 schriftlich belegt werden. Die zu wählende Fremdsprache muss durchgehend angeboten werden.");

	public static readonly FS_12 : GostBelegungsfehler = new GostBelegungsfehler("FS_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In EF.1 und EF.2 müssen alle gewählten Fremdsprachenfächer schriftlich belegt werden.", "In EF.1 müssen alle belegten Fremdsprachen schriftlich belegt werden.");

	public static readonly FS_13 : GostBelegungsfehler = new GostBelegungsfehler("FS_13", GostBelegungsfehlerArt.BELEGUNG, "Bei unvollendeter 2. Fremdsprache, muss die in der Sekundarstufe 1 begonnene 2. Fremdsprache in EF schriftlich oder eine neu einsetzende Fremdsprache durchgehend schriftlich belegt werden.", "Wurde die 2. Fremdsprache erst ab Klasse 8 erlernt, muss die in der Sekundarstufe 1 begonnene 2. Fremdsprache oder eine neu einsetzende Fremdsprache schriftlich in EF.1 belegt werden.");

	public static readonly FS_14 : GostBelegungsfehler = new GostBelegungsfehler("FS_14", GostBelegungsfehlerArt.BELEGUNG, "Bei fehlender 2. Fremdsprache muss eine neu einsetzende Fremdsprache durchgehend schriftlich belegt werden.", "Wurde bisher keine 2. Fremdsprache erlernt, muss eine neu einsetzende Fremdsprache in EF.1 schriftlich belegt werden.");

	public static readonly FS_15 : GostBelegungsfehler = new GostBelegungsfehler("FS_15", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Kurse in neu einsetzenden Fremdsprachen müssen schriftlich belegt werden.", null);

	public static readonly FS_16 : GostBelegungsfehler = new GostBelegungsfehler("FS_16", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mindestens eine fortgeführte Fremdsprache muss in EF.1 und EF.2 schriftlich belegt werden.", "Mindestens eine in der Sekundarstufe I begonnene Fremdsprache muss in EF.1 schriftlich belegt werden.");

	public static readonly FS_17 : GostBelegungsfehler = new GostBelegungsfehler("FS_17", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Neu einsetzende Fremdsprachen dürfen nicht als Leistungskurs belegt werden.", null);

	public static readonly FS_18 : GostBelegungsfehler = new GostBelegungsfehler("FS_18", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine Fremdsprache muss von EF.1 bis Q2.2 belegt werden.", "Mindestens eine in der Sekundarstufe I begonnene Fremdsprache muss in EF.1 schriftlich belegt werden.");

	public static readonly FS_19_INFO : GostBelegungsfehler = new GostBelegungsfehler("FS_19_INFO", GostBelegungsfehlerArt.HINWEIS, "Eine erfolgreiche Feststellungsprüfung in der Muttersprache am Ende der Sekundarstufe I und am Ende von EF.2 ist notwendig, um die Fremdsprachenbedingungen zu erfüllen.", "Eine erfolgreiche Feststellungsprüfung in der Muttersprache am Ende der Sekundarstufe I und am Ende von EF.2 ist notwendig, um die Fremdsprachenbedingungen zu erfüllen.");

	public static readonly FS_20 : GostBelegungsfehler = new GostBelegungsfehler("FS_20", GostBelegungsfehlerArt.BELEGUNG, "Eine neu einsetzende Fremdsprache kann nur gewählt werden, wenn diese nicht zuvor in der Sekundarstaufe I belegt wurde. Die Sprachenfolge ist fehlerhaft.", "Eine neu einsetzende Fremdsprache kann nur gewählt werden, wenn diese nicht zuvor in der Sekundarstaufe I belegt wurde. Die Sprachenfolge ist fehlerhaft.");

	public static readonly FS_21 : GostBelegungsfehler = new GostBelegungsfehler("FS_21", GostBelegungsfehlerArt.BELEGUNG, "Eine fortgeführte Fremdsprache wurde in der Sprachenfolge als neu einsetzend eingetragen. Die Sprachenfolge ist fehlerhaft.", "Eine fortgeführte Fremdsprache wurde in der Sprachenfolge als neu einsetzend eingetragen. Die Sprachenfolge ist fehlerhaft.");

	public static readonly FS_22_INFO : GostBelegungsfehler = new GostBelegungsfehler("FS_22_INFO", GostBelegungsfehlerArt.HINWEIS, "Englisch wurde nicht in der Sprachenfolge eingetragen. Bitte Prüfen Sie die Sprachenfolge auf Korrektheit.", "Englisch wurde nicht in der Sprachenfolge eingetragen. Bitte Prüfen Sie die Sprachenfolge auf Korrektheit.");

	public static readonly FS_23 : GostBelegungsfehler = new GostBelegungsfehler("FS_23", GostBelegungsfehlerArt.BELEGUNG, "Es wird das Fach einer fortgeführten Fremdsprache belegt, zu dem es keine entsprechende Eintragung einer Belegung oder Sprachprüfung in der Sekundarstufe I gibt. Bitte die Sprachenfolge und Sprachprüfungen prüfen.", "Es wird das Fach einer fortgeführten Fremdsprache belegt, zu dem es keine entsprechende Eintragung einer Belegung oder Sprachprüfung in der Sekundarstufe I gibt. Bitte die Sprachenfolge und Sprachprüfungen prüfen.");

	public static readonly FS_24 : GostBelegungsfehler = new GostBelegungsfehler("FS_24", GostBelegungsfehlerArt.BELEGUNG, "Ist die Bedingungen für die Belegung einer zweiten Fremdsprache in der Sekundarstufe I noch nicht erfüllt worden, so müssen in der Einführungsphase zwei Fremdsprachen belegt werden.", "Ist die Bedingungen für die Belegung einer zweiten Fremdsprache in der Sekundarstufe I noch nicht erfüllt worden, so müssen in der Einführungsphase zwei Fremdsprachen belegt werden.");

	public static readonly FS_25 : GostBelegungsfehler = new GostBelegungsfehler("FS_25", GostBelegungsfehlerArt.BELEGUNG, "Es wurde keine fortführbare Fremdsprache in der Sprachbelegung gefunden. Bitte prüfen Sie die Sprachenfolge.", "Es wurde keine fortführbare Fremdsprache in der Sprachbelegung gefunden. Bitte prüfen Sie die Sprachenfolge.");

	public static readonly GE_1_INFO : GostBelegungsfehler = new GostBelegungsfehler("GE_1_INFO", GostBelegungsfehlerArt.HINWEIS, "Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!", "Wird Geschichte nicht in EF.1 belegt, so muss Geschichte in der Qualifikationsphase als Zusatzkurs gewählt werden.");

	public static readonly GE_10 : GostBelegungsfehler = new GostBelegungsfehler("GE_10", GostBelegungsfehlerArt.BELEGUNG, "Geschichte muss von EF.1 bis wenigstens Q1.2 oder als Zusatzkurs (in der Regel von Q2.1 bis Q2.2) belegt werden.", null);

	public static readonly GKS_10 : GostBelegungsfehler = new GostBelegungsfehler("GKS_10", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase sind pro Halbjahr mindestens 7 Fächer in Grundkursen zu wählen.", null);

	public static readonly GKS_11 : GostBelegungsfehler = new GostBelegungsfehler("GKS_11", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase sind pro Halbjahr mindestens 6 Fächer in Grundkursen zu wählen.", null);

	public static readonly GW_10 : GostBelegungsfehler = new GostBelegungsfehler("GW_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine Gesellschaftswissenschaft muss von Q1.1 bis Q2.2 durchgehend belegt werden.", "Mindestens eine Gesellschaftswissenschaft muss in EF.1 belegt werden und durchgängig belegbar sein.");

	public static readonly GW_11 : GostBelegungsfehler = new GostBelegungsfehler("GW_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In EF.1 und EF.2 muss mindestens eine Gesellschaftswissenschaft schriftlich belegt sein.", "Mindestens eine Gesellschaftswissenschaft muss in EF.1 schriftlich belegt werden.");

	public static readonly GW_12 : GostBelegungsfehler = new GostBelegungsfehler("GW_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mindestens eine Gesellschaftswissenschaft oder Religionslehre muss von Q1.1 bis wenigstens Q2.1 schriftlich belegt werden.", null);

	public static readonly IGF_10 : GostBelegungsfehler = new GostBelegungsfehler("IGF_10", GostBelegungsfehlerArt.BELEGUNG, "Inhaltsgleiche Fächer und Projektkurse dürfen in jedem Halbjahr nur einmal belegt werden.", "Inhaltsgleiche Fächer dürfen nur einmal belegt werden.");

	public static readonly KU_MU_10 : GostBelegungsfehler = new GostBelegungsfehler("KU_MU_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eines der Fächer Kunst oder Musik muss von EF.1 bis wenigstens Q1.2 durchgehend belegt werden. In der Qualifikationsphase kann auch alternativ Literatur, ein vokalpraktisches oder ein instrumentalpraktisches Fach mit zwei Kursen belegt werden.", "Mindestens eines der Fächer Kunst oder Musik muss in EF.1 belegt werden");

	public static readonly L_10_INFO : GostBelegungsfehler = new GostBelegungsfehler("L_10_INFO", GostBelegungsfehlerArt.HINWEIS, "Um das Latinum zu erlangen muss Latein in EF.1 und EF.2 belegt werden.", "Um das Latinum zu erlangen muss Latein in EF.1 schriftlich fortgeführt werden.");

	public static readonly L_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("L_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Um das Latinum zu erlangen muss Latein mindestens bis Q1.2, je nach Stundenvolumen sogar bis Q2.2 belegt werden.", null);

	public static readonly L_12_INFO : GostBelegungsfehler = new GostBelegungsfehler("L_12_INFO", GostBelegungsfehlerArt.HINWEIS, "Um das Latinum zu erlangen muss Latein mindestens bis EF.2, je nach Beginn und Stundenvolumen sogar bis Q2.2 belegt werden.", null);

	public static readonly LI_IV_10 : GostBelegungsfehler = new GostBelegungsfehler("LI_IV_10", GostBelegungsfehlerArt.BELEGUNG, "Die Fächer Literatur, instrumentalpraktischer bzw. vokalpraktischer Grundkurs dürfen maximal in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	public static readonly LI_IV_11 : GostBelegungsfehler = new GostBelegungsfehler("LI_IV_11", GostBelegungsfehlerArt.BELEGUNG, "Es darf nur eins der Fächer Literatur, IP oder VP belegt werden. (Schulen mit genehmigtem musisch-künstlerischem Schwerpunkt müssen betroffene Schüler in SchILD-NRW manuell zulassen).", null);

	public static readonly LK_10 : GostBelegungsfehler = new GostBelegungsfehler("LK_10", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase müssen zwei Fächer durchgehend in Leistungskursen belegt werden.", null);

	public static readonly LK_11 : GostBelegungsfehler = new GostBelegungsfehler("LK_11", GostBelegungsfehlerArt.BELEGUNG, "Es dürfen nicht mehr als zwei Fächer als Leistungskurse belegt werden.", null);

	public static readonly LK1_10 : GostBelegungsfehler = new GostBelegungsfehler("LK1_10", GostBelegungsfehlerArt.BELEGUNG, "Erstes Abiturfach nicht eindeutig identifizierbar.", null);

	public static readonly LK1_11 : GostBelegungsfehler = new GostBelegungsfehler("LK1_11", GostBelegungsfehlerArt.BELEGUNG, "Das erste Leistungskursfach muss eine fortgeführte Fremdsprache, Mathematik, eine klassische Naturwissenschaft oder Deutsch sein.", null);

	public static readonly LK1_12 : GostBelegungsfehler = new GostBelegungsfehler("LK1_12", GostBelegungsfehlerArt.BELEGUNG, "Ist Deutsch erstes Leistungskursfach, muss Mathematik oder eine Fremdsprache unter den vier Abiturfächern sein.", null);

	public static readonly LK1_13 : GostBelegungsfehler = new GostBelegungsfehler("LK1_13", GostBelegungsfehlerArt.BELEGUNG, "Die Abiturfächer müssen alle drei Aufgabenfelder abdecken. Insgesamt sind vier Abiturfächer zu belegen.", null);

	public static readonly M_10 : GostBelegungsfehler = new GostBelegungsfehler("M_10", GostBelegungsfehlerArt.BELEGUNG, "Mathematik muss von EF.1 bis Q2.2 belegt werden.", "Mathematik muss in EF.1 schriftlich belegt werden.");

	public static readonly M_11 : GostBelegungsfehler = new GostBelegungsfehler("M_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Mathematik muss von EF.1 bis wenigstens Q2.1 schriftlich belegt werden.", "Mathematik muss in EF.1 schriftlich belegt werden.");

	public static readonly NW_10 : GostBelegungsfehler = new GostBelegungsfehler("NW_10", GostBelegungsfehlerArt.BELEGUNG, "Mindestens eine klassische Naturwissenschaft (Physik, Biologie, Chemie) muss durchgehend von Q1.1 bis Q2.2 belegt werden.", "Mindestens eines der Fächer Physik, Chemie oder Biologie muss in EF.1 belegt werden und durchgängig belegbar sein.");

	public static readonly NW_11 : GostBelegungsfehler = new GostBelegungsfehler("NW_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "In EF.1 und EF.2 muss mindestens eine klassische Naturwissenschaft schriftlich belegt sein.", "Mindestens eines der Fächer Physik, Chemie oder Biologie muss in EF.1 schriftlich belegt werden.");

	public static readonly NW_FS_10 : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_10", GostBelegungsfehlerArt.BELEGUNG, "Von EF.1 bis Q2.2 müssen entweder zwei Naturwissenschaften oder zwei Fremdsprachen durchgehend gewählt werden. Hierbei ist eine Naturwissenschaft oder sind zwei Fremdsprachen schriftlich zu belegen. Zu den Fremdsprachen zählen auch in einer weiteren Fremdsprache unterrichtete Sachfächer.", "In EF.1 müssen entweder zwei Naturwissenschaften oder zwei Fremdsprachen belegt werden. Hierbei ist eine Naturwissenschaft oder sind zwei Fremdsprachen schriftlich zu belegen. Zu den Fremdsprachen zählen auch in einer weiteren Fremdsprache unterrichtete Sachfächer.");

	public static readonly NW_FS_11 : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT, "Zwei Fremdsprachen oder eines von mindestens zwei naturwissenschaftlichen Fächern muss von Q1.1 bis wenigstens Q2.1 schriftlich belegt werden.", null);

	public static readonly NW_FS_12_INFO : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_12_INFO", GostBelegungsfehlerArt.HINWEIS, "Da von EF.1 bis Q2.2 weniger als zwei naturwissenschaftliche Fächer durchgehend belegt wurden, oder kein naturwissenschaftliches Fach schriftlich belegt wurde, liegt ausschließlich ein Sprachenschwerpunkt vor.", "Da in EF.1 weniger als zwei Naturwissenschaften belegt, oder keine schriftlich belegt wurden, müssen zwei Fremdsprachen bis Q2.2 durchgehend schriftlich belegt werden.");

	public static readonly NW_FS_13_INFO : GostBelegungsfehler = new GostBelegungsfehler("NW_FS_13_INFO", GostBelegungsfehlerArt.HINWEIS, "Da von EF.1 bis Q2.2 weniger als zwei Fremdsprachen schriftlich belegt wurden, liegt ausschließlich ein naturwissenschaftlicher Schwerpunkt vor.", "Da in EF.1 weniger als zwei Fremdsprachen schriftlich belegt wurden, müssen zwei Naturwissenschaften bis Q2.2 belegt, davon mindestens eine schriftlich belegt werden.");

	public static readonly PF_10 : GostBelegungsfehler = new GostBelegungsfehler("PF_10", GostBelegungsfehlerArt.BELEGUNG, "In der Einführungsphase können keine Projektkurse belegt werden.", "In EF.1 können keine Projektkurse belegt werden.");

	public static readonly PF_11 : GostBelegungsfehler = new GostBelegungsfehler("PF_11", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase kann maximal ein Projektkurs belegt werden.", null);

	public static readonly PF_12 : GostBelegungsfehler = new GostBelegungsfehler("PF_12", GostBelegungsfehlerArt.BELEGUNG, "Projektkurse können nur dann belegt werden, wenn gleichzeitig oder maximal zwei Halbjahre vorher auch das Leitfach im gleichem Umfang belegt wurde.", null);

	public static readonly PF_13 : GostBelegungsfehler = new GostBelegungsfehler("PF_13", GostBelegungsfehlerArt.BELEGUNG, "Ein Projektkurs kann nur belegt werden, wenn in der Qualifikationsphase zeitgleich oder vorab auch sein Leitfach zwei Halbjahre lang belegt wurde.", null);

	public static readonly PF_14 : GostBelegungsfehler = new GostBelegungsfehler("PF_14", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase kann maximal ein Projektkurs in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	public static readonly PF_15 : GostBelegungsfehler = new GostBelegungsfehler("PF_15", GostBelegungsfehlerArt.BELEGUNG, "Es existiert kein Projektkurs, der als besondere Lernleistung eingebracht werden kann.", null);

	public static readonly PF_16_INFO : GostBelegungsfehler = new GostBelegungsfehler("PF_16_INFO", GostBelegungsfehlerArt.HINWEIS, "Wird der Projektkurs als besondere Lernleistung in das Abitur eingebracht, so zählt er nicht mehr zu den einbringungsfähigen Kursen in Block I.", null);

	public static readonly PF_17_INFO : GostBelegungsfehler = new GostBelegungsfehler("PF_17_INFO", GostBelegungsfehlerArt.HINWEIS, "Ein Projektkurs soll nur im Ausnahmefall abgewählt werden. Bei einem abgewählten Projektkurs werden lediglich die Wochenstunden auf die Laufbahn angerechnet.", null);

	public static readonly PF_18 : GostBelegungsfehler = new GostBelegungsfehler("PF_18", GostBelegungsfehlerArt.BELEGUNG, "Ein Projektkurs kann nicht in der Q2.2 beginnen.", null);

	public static readonly RE_10 : GostBelegungsfehler = new GostBelegungsfehler("RE_10", GostBelegungsfehlerArt.BELEGUNG, "Religionslehre muss wenigstens von EF.1-Q1.2 durchgehend belegt werden. Als Ersatz kann Philosophie dienen, sofern Philosophie nicht die einzige von EF.1 bis Q2.2 durchgehend belegte Gesellschaftswissenschaft ist. In diesen Fällen muss ein weiteres Fach der Gesellschaftswissenschaften als Religionsersatz dienen.", "Ein Religionskurs muss in EF.1 belegt werden. Als Ersatz kann Philosophie belegt werden, sofern eine weitere Gesellschaftswissenschaft bis zum Abitur belegt wird.");

	public static readonly RE_11 : GostBelegungsfehler = new GostBelegungsfehler("RE_11", GostBelegungsfehlerArt.BELEGUNG, "Philosophie darf nicht die einzige durchgehend von Q1.1 bis Q2.2 belegte Geisteswissenschaft sein.", null);

	public static readonly SP_10 : GostBelegungsfehler = new GostBelegungsfehler("SP_10", GostBelegungsfehlerArt.BELEGUNG, "Sport muss von EF.1 bis Q2.2 belegt werden.", "Sport muss in EF.1 belegt werden.");

	public static readonly STD_10 : GostBelegungsfehler = new GostBelegungsfehler("STD_10", GostBelegungsfehlerArt.BELEGUNG, "Der Pflichtunterricht darf 102 Stunden nicht unterschreiten.", null);

	public static readonly STD_11_INFO : GostBelegungsfehler = new GostBelegungsfehler("STD_11_INFO", GostBelegungsfehlerArt.HINWEIS, "Der Pflichtunterricht darf nur in begründeten Ausnahmefällen 102 Stunden unterschreiten.", null);

	public static readonly SW_1_INFO : GostBelegungsfehler = new GostBelegungsfehler("SW_1_INFO", GostBelegungsfehlerArt.HINWEIS, "Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!", "Wird Sozialwissenschaften nicht in EF.1 belegt, so muss Sozialwissenschaften in der Qualifikationsphase als Zusatzkurs gewählt werden.");

	public static readonly SW_10 : GostBelegungsfehler = new GostBelegungsfehler("SW_10", GostBelegungsfehlerArt.BELEGUNG, "Sozialwissenschaften muss von EF.1 bis wenigstens Q1.2 oder als Zusatzkurs (in der Regel von Q2.1 bis Q2.2) belegt werden.", null);

	public static readonly VF_10 : GostBelegungsfehler = new GostBelegungsfehler("VF_10", GostBelegungsfehlerArt.BELEGUNG, "In der Einführungsphase können maximal vier Vertiefungskurse belegt werden.", null);

	public static readonly VF_11 : GostBelegungsfehler = new GostBelegungsfehler("VF_11", GostBelegungsfehlerArt.BELEGUNG, "In der Qualifikationsphase können maximal zwei Vertiefungskurse belegt werden.", null);

	public static readonly WST_10 : GostBelegungsfehler = new GostBelegungsfehler("WST_10", GostBelegungsfehlerArt.BELEGUNG, "Die Summe der durchschnittlichen Jahreskursstunden von EF.1 bis Q2.2 darf 100 nicht unterschreiten.", null);

	public static readonly WST_20 : GostBelegungsfehler = new GostBelegungsfehler("WST_20", GostBelegungsfehlerArt.BELEGUNG, "Die durchschnittliche Wochenstundenzahl muss in der Einführungsphase mindestens 34 Stunden betragen.", null);

	public static readonly WST_21 : GostBelegungsfehler = new GostBelegungsfehler("WST_21", GostBelegungsfehlerArt.BELEGUNG, "Die durchschnittliche Wochenstundenzahl muss in der Qualifikationsphase mindestens 34 Stunden betragen.", null);

	public static readonly ZK_10 : GostBelegungsfehler = new GostBelegungsfehler("ZK_10", GostBelegungsfehlerArt.BELEGUNG, "Ein Zusatzkurs in Geschichte oder Sozialwissenschaften kann nur angewählt werden, wenn das Fach im vorangegangenen Halbjahr nicht belegt wurde.", null);

	public static readonly ZK_11 : GostBelegungsfehler = new GostBelegungsfehler("ZK_11", GostBelegungsfehlerArt.BELEGUNG, "Ein Zusatzkurs in Geschichte oder Sozialwissenschaften muss in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	public static readonly ZK_12 : GostBelegungsfehler = new GostBelegungsfehler("ZK_12", GostBelegungsfehlerArt.BELEGUNG, "Zusatzkurse dürfen maximal in zwei aufeinanderfolgenden Halbjahren belegt werden.", null);

	public static readonly ZK_13 : GostBelegungsfehler = new GostBelegungsfehler("ZK_13", GostBelegungsfehlerArt.BELEGUNG, "Zusatzkurse dürfen nur einmal pro Fach belegt werden und können nicht in einem bilingualen Fach gewählt werden.", null);

	public readonly code : String;

	public readonly art : GostBelegungsfehlerArt;

	public readonly textGESAMT : String;

	public readonly textEF1 : String;


	/**
	 * Erstellt einen neuen Belegungsfehler für die Aufzählung (s.o.). Dabei wird ein
	 * Text für die Gesamtprüfung und die EF.1-Prüfung angegeben.
	 *
	 * @param code         der eindeutige Code des Belegungsfehlers
	 * @param art          die Fehlerart (Belegungsfehler, Schriftlichkeit oder Information)
	 * @param textGESAMT   der zugeordnete Text für die Gesamtbelegprüfung oder null
	 * @param textEF1      der zugeordnete Text für die EF.1-Prüfung oder null
	 */
	private constructor(code : String, art : GostBelegungsfehlerArt, textGESAMT : String | null, textEF1 : String | null) {
		super();
		this.code = code;
		this.art = art;
		this.textGESAMT = textGESAMT !== null ? textGESAMT : "Programmfehler: Diese Belegungsfehlerart ist für eine Gesamt-Prüfung nicht vorgesehen!";
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
	 * @param pruef_art   die Belegprüfungsart
	 *
	 * @return der zugehörige Text des Belegungsfehlers
	 */
	public getText(pruef_art : GostBelegpruefungsArt) : String {
		if (pruef_art as unknown === GostBelegpruefungsArt.EF1 as unknown) 
			return this.textEF1;
		if (pruef_art as unknown === GostBelegpruefungsArt.GESAMT as unknown) 
			return this.textGESAMT;
		return this.textGESAMT;
	}

	public toString() : String {
		return this.code;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehler'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehler(obj : unknown) : GostBelegungsfehler {
	return obj as GostBelegungsfehler;
}
