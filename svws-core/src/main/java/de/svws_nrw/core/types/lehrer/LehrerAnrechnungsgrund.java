package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogAnrechnungsgrundEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Anrechnungsgründe für
 * Lehrerstunden bei der Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerAnrechnungsgrund {

	/** Anrechnungsgrund 'Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für Universitätsabsolventen OBAS' für Stunden bei einem Lehrer */
	ID_310(new LehrerKatalogAnrechnungsgrundEintrag[]{
		new LehrerKatalogAnrechnungsgrundEintrag(310000, "310", "Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für Universitätsabsolventen OBAS", 2010, null)
	}),

	/** Anrechnungsgrund 'Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für FH - Absolventen' für Stunden bei einem Lehrer */
	ID_315(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(315000, "315", "Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für FH - Absolventen", 2010, null)
	}),

	/** Anrechnungsgrund 'Seiteneinsteiger/-in: Entlastung für Pädagogische Einführung in den Schuldienst' für Stunden bei einem Lehrer */
	ID_320(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(320000, "320", "Seiteneinsteiger/-in: Entlastung für Pädagogische Einführung in den Schuldienst", 2010, null)
	}),

	/** Anrechnungsgrund 'Teilnahme an Qualifizierungsmaßnahme: Einführung in die Grundschuldidaktik' für Stunden bei einem Lehrer */
	ID_325(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(325000, "325", "Teilnahme an Qualifizierungsmaßnahme: Einführung in die Grundschuldidaktik", 2019, null)
	}),

	/** Anrechnungsgrund 'Tätigkeit als schulische/-r Ausbilder/-in OBAS (§ 11 Abs. 5 i. V. mit § 9 Abs. 2)' für Stunden bei einem Lehrer */
	ID_330(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(330000, "330", "Tätigkeit als schulische/-r Ausbilder/-in OBAS (§ 11 Abs. 5 i. V. mit § 9 Abs. 2)", 2012, null)
	}),

	/** Anrechnungsgrund 'Erfahrene Lehrkraft: Entlastung für Pädagogische Einführung in den Schuldienst' für Stunden bei einem Lehrer */
	ID_340(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(340000, "340", "Erfahrene Lehrkraft: Entlastung für Pädagogische Einführung in den Schuldienst", 2012, null)
	}),

	/** Anrechnungsgrund 'Wahrnehmung besonderer schulischer Aufgaben / Ausgleich besonderer unterrichtlicher Belastungen' für Stunden bei einem Lehrer */
	ID_500(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(500000, "500", "Wahrnehmung besonderer schulischer Aufgaben / Ausgleich besonderer unterrichtlicher Belastungen", null, null)
	}),

	/** Anrechnungsgrund 'Schulleitungspauschale' für Stunden bei einem Lehrer */
	ID_510(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(510000, "510", "Schulleitungspauschale", null, null)
	}),

	/** Anrechnungsgrund 'Schulübergreifende Aufgaben kleineren Umfangs' für Stunden bei einem Lehrer */
	ID_520(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(520000, "520", "Schulübergreifende Aufgaben kleineren Umfangs", null, null)
	}),

	/** Anrechnungsgrund 'SV-Verbindungslehrer, Beratungslehrer' für Stunden bei einem Lehrer */
	ID_530(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(530000, "530", "SV-Verbindungslehrer, Beratungslehrer", null, null)
	}),

	/** Anrechnungsgrund 'Beratungsaufgaben in der Sek. I' für Stunden bei einem Lehrer */
	ID_540(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(540000, "540", "Beratungsaufgaben in der Sek. I", null, null)
	}),

	/** Anrechnungsgrund 'Laufbahnberatung und -kontrolle in der gymnasialen Oberstufe' für Stunden bei einem Lehrer */
	ID_550(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(550000, "550", "Laufbahnberatung und -kontrolle in der gymnasialen Oberstufe", null, null)
	}),

	/** Anrechnungsgrund 'Koordinations- und Beratungsaufgaben im Landesvorhaben KAoA' für Stunden bei einem Lehrer */
	ID_590(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(590000, "590", "Koordinations- und Beratungsaufgaben im Landesvorhaben KAoA", 2013, null)
	}),

	/** Anrechnungsgrund 'Teamabsprachen, Unterrichtsvorbereitung für Gemeinsames Lernen' für Stunden bei einem Lehrer */
	ID_600(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(600000, "600", "Teamabsprachen, Unterrichtsvorbereitung für Gemeinsames Lernen", null, 2021),
		new LehrerKatalogAnrechnungsgrundEintrag(600001, "600", "Beratung, Teamabsprachen, Unterrichtsvorbereitung für Gemeinsames Lernen", 2022, null)
	}),

	/** Anrechnungsgrund 'Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Vorbereitungsdienst, OBAS, VOBASOF, Pädagogische Einführung in den Schuldienst)' für Stunden bei einem Lehrer */
	ID_605(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(605000, "605", "Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Vorbereitungsdienst, OBAS, VOBASOF, Pädagogische Einführung in den Schuldienst)", null, null)
	}),

	/** Anrechnungsgrund 'Personalratstätigkeit' für Stunden bei einem Lehrer */
	ID_610(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(610000, "610", "Personalratstätigkeit", null, null)
	}),

	/** Anrechnungsgrund 'Schwerbehindertenvertretung' für Stunden bei einem Lehrer */
	ID_615(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(615000, "615", "Schwerbehindertenvertretung", null, null)
	}),

	/** Anrechnungsgrund 'Tätigkeit als schulische/-r Ausbilder/-in VOBASOF (qualifizierte Fachkraft, § 11 VOBASOF)' für Stunden bei einem Lehrer */
	ID_620(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(620000, "620", "Tätigkeit als schulische/-r Ausbilder/-in VOBASOF (qualifizierte Fachkraft, § 11 VOBASOF)", 2013, null)
	}),

	/** Anrechnungsgründe 
	 * 'Fortbildungslehrgänge für technische Lehrer aus Entwicklungsländern (LIB)' bzw.
	 * 'Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Eignungs- und Orientierungspraktikum)' 
	 * für Stunden bei einem Lehrer */
	ID_625(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(625000, "625", "Fortbildungslehrgänge für technische Lehrer aus Entwicklungsländern (LIB)", null, 2007),
		new LehrerKatalogAnrechnungsgrundEintrag(625001, "625", "Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Eignungs- und Orientierungspraktikum)", 2013, null)
	}),

	/** Anrechnungsgrund 'Lehrkraft in Ausbildung: Schulpraktische Ausbildung am Zentrum für schulpraktische Ausbildung (§ 7 VOBASOF)' für Stunden bei einem Lehrer */
	ID_630(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(630000, "630", "Lehrkraft in Ausbildung: Schulpraktische Ausbildung am Zentrum für schulpraktische Ausbildung (§ 7 VOBASOF)", 2013, null)
	}),

	/** Anrechnungsgrund 'Fortbildung und Qualifikation, Medien und Datenschutz' für Stunden bei einem Lehrer */
	ID_635(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(635000, "635", "Fortbildung und Qualifikation, Medien und Datenschutz", null, null)
	}),

	/** Anrechnungsgrund 'Fachberater Schulaufsicht' für Stunden bei einem Lehrer */
	ID_640(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(640000, "640", "Fachberater Schulaufsicht", null, null)
	}),

	/** Anrechnungsgrund 'Sport sowie für Beratung und Koordination im Verbundsystem Schule und Leistungssport' für Stunden bei einem Lehrer */
	ID_645(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(645000, "645", "Sport sowie für Beratung und Koordination im Verbundsystem Schule und Leistungssport", null, null)
	}),

	/** Anrechnungsgrund 'Mitarbeit in Kommunalen Integrationszentren' für Stunden bei einem Lehrer */
	ID_650(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(650000, "650", "Mitarbeit in Kommunalen Integrationszentren", null, null)
	}),

	/** Anrechnungsgrund 'Auslandstätigkeit (Landeslehrerentsendeprogramm)' für Stunden bei einem Lehrer */
	ID_655(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(655000, "655", "Auslandstätigkeit (Landeslehrerentsendeprogramm)", null, null)
	}),

	/** Anrechnungsgründe 
	 * 'Laborschule Bielefeld / Bildungspol. Sonderaufgaben' bzw.
	 * 'Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Praxissemester)'
	 * für Stunden bei einem Lehrer */
	ID_665(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(665000, "665", "Laborschule Bielefeld / Bildungspol. Sonderaufgaben", null, 2011),
		new LehrerKatalogAnrechnungsgrundEintrag(665001, "665", "Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Praxissemester)", 2013, null)
	}),

	/** Anrechnungsgrund 'Richtlinien und Lehrpläne / learn:line' für Stunden bei einem Lehrer */
	ID_670(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(670000, "670", "Richtlinien und Lehrpläne / learn:line", null, 2011)
	}),

	/** Anrechnungsgrund 'GÖS (GanzTag und Öffnung von Schule)' für Stunden bei einem Lehrer */
	ID_675(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(675000, "675", "GÖS (GanzTag und Öffnung von Schule)", null, 2011)
	}),

	/** Anrechnungsgrund 'Archivpädagogen Schülertheater Pusteblume / Koordination GU' für Stunden bei einem Lehrer */
	ID_680(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(680000, "680", "Archivpädagogen Schülertheater Pusteblume / Koordination GU", null, 2011)
	}),

	/** Anrechnungsgrund 'Modellversuche / BLK' für Stunden bei einem Lehrer */
	ID_685(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(685000, "685", "Modellversuche / BLK", null, 2011)
	}),

	/** Anrechnungsgrund 'Musik / Tanz / Theater' für Stunden bei einem Lehrer */
	ID_690(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(690000, "690", "Musik / Tanz / Theater", null, 2011)
	}),

	/** Anrechnungsgrund 'Andere Lernorte' für Stunden bei einem Lehrer */
	ID_695(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(695000, "695", "Andere Lernorte", null, 2011)
	}),

	/** Anrechnungsgrund 'Schaustellerkinder' für Stunden bei einem Lehrer */
	ID_700(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(700000, "700", "Schaustellerkinder", null, 2011)
	}),

	/** Anrechnungsgrund 'Gesundheitserziehung' für Stunden bei einem Lehrer */
	ID_705(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(705000, "705", "Gesundheitserziehung", null, 2011)
	}),

	/** Anrechnungsgrund 'Landerschülervertretungsverbindungslehrer / Go to School' für Stunden bei einem Lehrer */
	ID_710(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(710000, "710", "Landerschülervertretungsverbindungslehrer / Go to School", null, 2011)
	}),

	/** Anrechnungsgründe 
	 * 'Stiftung (Wirtschaft und Schule / Pra WiS, Selbstständige Schule)' bzw.
	 * 'Eignungspraktikum Mentor Schule' 
	 * für Stunden bei einem Lehrer */
	ID_715(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(715000, "715", "Stiftung (Wirtschaft und Schule / Pra WiS, Selbstständige Schule)", null, 2011),
		new LehrerKatalogAnrechnungsgrundEintrag(715001, "715", "Eignungspraktikum Mentor Schule", 2013, 2017)
	}),

	/** Anrechnungsgrund 'Pädaudiologische Zentren und Frühförderzentren für Sehgeschädigte' für Stunden bei einem Lehrer */
	ID_720(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(720000, "720", "Pädaudiologische Zentren und Frühförderzentren für Sehgeschädigte", null, null)
	}),

	/** Anrechnungsgrund 'Tätigkeit bei EU-Geschäftsstellen' für Stunden bei einem Lehrer */
	ID_725(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(725000, "725", "Tätigkeit bei EU-Geschäftsstellen", null, null)
	}),

	/** Anrechnungsgrund 'Curriculumentwicklung / Zentrale Prüfungen' für Stunden bei einem Lehrer */
	ID_730(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(730000, "730", "Curriculumentwicklung / Zentrale Prüfungen", null, null)
	}),

	/** Anrechnungsgrund 'Förderung des Theatertreffens für behinderte Kinder und Jugendliche' für Stunden bei einem Lehrer */
	ID_735(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(735000, "735", "Förderung des Theatertreffens für behinderte Kinder und Jugendliche", null, null)
	}),

	/** Anrechnungsgrund 'Archivpädagogik' für Stunden bei einem Lehrer */
	ID_740(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(740000, "740", "Archivpädagogik", null, null)
	}),

	/** Anrechnungsgrund 'Bildungspolitische Sonderaufgaben' für Stunden bei einem Lehrer */
	ID_745(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(745000, "745", "Bildungspolitische Sonderaufgaben", null, null)
	}),

	/** Anrechnungsgrund 'Sonstiger Ausgleichsbedarf, der aus wechselnden Ausgleichs- und Mehrbedarfen finanziert ist.' für Stunden bei einem Lehrer */
	ID_750(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(750000, "750", "Sonstiger Ausgleichsbedarf, der aus wechselnden Ausgleichs- und Mehrbedarfen finanziert ist.", null, null)
	}),

	/** Anrechnungsgrund 'Tätigkeit als schulische/-r Mentor/-in: Praxissemester' für Stunden bei einem Lehrer */
	ID_765(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(765000, "765", "Tätigkeit als schulische/-r Mentor/-in: Praxissemester", 2013, null)
	}),

	/** Anrechnungsgrund 'Individuelle Beratung der Schülerinnen und Schüler im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_770(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(770000, "770", "Individuelle Beratung der Schülerinnen und Schüler im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Innerschulischer Transfer der Maßnahmen im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_771(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(771000, "771", "Innerschulischer Transfer der Maßnahmen im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Durchführung soziales Training im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_772(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(772000, "772", "Durchführung soziales Training im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Berufsfeld- oder fachbereichsübergreifende Berufsorientierung im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_773(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(773000, "773", "Berufsfeld- oder fachbereichsübergreifende Berufsorientierung im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Erweiterte Blockpraktika/Praxismodul „Berufliche Orientierung“ im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_774(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(774000, "774", "Erweiterte Blockpraktika/Praxismodul „Berufliche Orientierung“ im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Sprachförderung im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_775(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(775000, "775", "Sprachförderung im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Übergangssicherung/Datenmonitoring im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_776(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(776000, "776", "Übergangssicherung/Datenmonitoring im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Kooperation mit externen Partnern im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_777(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(777000, "777", "Kooperation mit externen Partnern im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Verwaltungstätigkeiten zur Unterstützung der Schulleitung bei den Landesaufgaben im Schulversuch Talentschule' für Stunden bei einem Lehrer */
	ID_778(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(778000, "778", "Verwaltungstätigkeiten zur Unterstützung der Schulleitung bei den Landesaufgaben im Schulversuch Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Sonstige Tätigkeiten im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer */
	ID_779(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(779000, "779", "Sonstige Tätigkeiten im Rahmen des Schulversuchs Talentschule", 2020, null)
	}),

	/** Anrechnungsgrund 'Zeitbudget, Steigerung der Berufsfähigkeit (Förderschulen)' für Stunden bei einem Lehrer */
	ID_800(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(800000, "800", "Zeitbudget, Steigerung der Berufsfähigkeit (Förderschulen)", null, 2011)
	}),

	/** Anrechnungsgrund 'Zeitbudget, Modellvorhaben "Selbstständige Schule"' für Stunden bei einem Lehrer */
	ID_805(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(805000, "805", "Zeitbudget, Modellvorhaben \"Selbstständige Schule\"", null, 2007)
	}),

	/** Anrechnungsgrund 'Projekt: Abitur-Online' für Stunden bei einem Lehrer */
	ID_815(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(815000, "815", "Projekt: Abitur-Online", null, null)
	}),

	/** Anrechnungsgrund 'Sonstiger Ausgleichsbedarf, der aus Zeitbudget finanziert ist.' für Stunden bei einem Lehrer */
	ID_820(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(820000, "820", "Sonstiger Ausgleichsbedarf, der aus Zeitbudget finanziert ist.", null, null)
	}),

	/** Anrechnungsgrund 'Förderung lernschwacher und begabter Schülerinnen und Schüler' für Stunden bei einem Lehrer */
	ID_850(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(850000, "850", "Förderung lernschwacher und begabter Schülerinnen und Schüler", null, null)
	}),

	/** Anrechnungsgrund 'sonderpädagogischer Förderbedarf, z. B. Lese- und Rechtschreibschwächen, Lernbehinderungen' für Stunden bei einem Lehrer */
	ID_855(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(855000, "855", "sonderpädagogischer Förderbedarf, z. B. Lese- und Rechtschreibschwächen, Lernbehinderungen", null, null)
	}),

	/** Anrechnungsgrund 'Einstiegshilfen in Beruf/Ausbildung' für Stunden bei einem Lehrer */
	ID_860(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(860000, "860", "Einstiegshilfen in Beruf/Ausbildung", null, null)
	}),

	/** Anrechnungsgrund 'Landes- und Bundeswettbewerbe, Landessschülertheater ' für Stunden bei einem Lehrer */
	ID_875(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(875000, "875", "Landes- und Bundeswettbewerbe, Landessschülertheater", null, null)
	}),

	/** Anrechnungsgrund 'Nichtschüler-, Änderungs- und Feststellungsprüfungen' für Stunden bei einem Lehrer */
	ID_880(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(880000, "880", "Nichtschüler-, Änderungs- und Feststellungsprüfungen", null, null)
	}),

	/** Anrechnungsgrund 'Sonstiger Ausgleichsbedarf, der aus Rundungsgewinnen finanziert ist.' für Stunden bei einem Lehrer */
	ID_885(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(885000, "885", "Sonstiger Ausgleichsbedarf, der aus Rundungsgewinnen finanziert ist.", null, null)
	}),

	/** Anrechnungsgrund 'Tätigkeit als schulische/-r Ausbildungsbeauftragte/-r OVP' für Stunden bei einem Lehrer */
	ID_900(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(900000, "900", "Tätigkeit als schulische/-r Ausbildungsbeauftragte/-r OVP", null, null)
	}),

	/** Anrechnungsgrund 'Außerunterrichtliche Tätigkeit in der offenen Ganztagsschule' für Stunden bei einem Lehrer */
	ID_910(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(910000, "910", "Außerunterrichtliche Tätigkeit in der offenen Ganztagsschule", null, 2011)
	}),

	/** Anrechnungsgrund 'Unterrichtsausfallstatistik' für Stunden bei einem Lehrer */
	ID_915(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(915000, "915", "Unterrichtsausfallstatistik", null, null)
	}),

	/** Anrechnungsgrund 'Einsatz als sozialpädagogische Fachkraft in der Schuleingangsphase' für Stunden bei einem Lehrer */
	ID_920(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(920000, "920", "Einsatz als sozialpädagogische Fachkraft in der Schuleingangsphase", null, 2019)
	}),

	/** Anrechnungsgrund 'Einsatz als sozialpädagogische Fachkraft' für Stunden bei einem Lehrer */
	ID_930(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(930000, "930", "Einsatz als sozialpädagogische Fachkraft", null, null)
	}),

	/** Anrechnungsgrund 'Tätigkeit als Verwaltungsassistent/ Verwaltungsassistentin' für Stunden bei einem Lehrer */
	ID_935(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(935000, "935", "Tätigkeit als Verwaltungsassistent/ Verwaltungsassistentin", 2008, null)
	}),

	/** Anrechnungsgrund 'LOGINEO: Anrechnungsstunde zur Betreuung und Pflege' für Stunden bei einem Lehrer */
	ID_936(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(936000, "936", "LOGINEO: Anrechnungsstunde zur Betreuung und Pflege", 2020, null)
	}),

	/** Anrechnungsgrund 'Betreung und Aufsicht im Ganztagsbereich der Ganztagsschulen' für Stunden bei einem Lehrer */
	ID_940(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(940000, "940", "Betreung und Aufsicht im Ganztagsbereich der Ganztagsschulen", null, 2011)
	}),

	/** Anrechnungsgrund 'Lehrerwochenstunden, die nicht verplant sind' für Stunden bei einem Lehrer */
	ID_945(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(945000, "945", "Lehrerwochenstunden, die nicht verplant sind", 2008, null)
	}),

	/** Anrechnungsgrund 'Ausgleichsbedarf, der aus Flexiblen Mitteln finanziert wird' für Stunden bei einem Lehrer */
	ID_950(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(950000, "950", "Ausgleichsbedarf, der aus Flexiblen Mitteln finanziert wird", null, null)
	}),

	/** Anrechnungsgrund 'Schulpsychologischer Dienst' für Stunden bei einem Lehrer */
	ID_955(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
			new LehrerKatalogAnrechnungsgrundEintrag(955000, "955", "Schulpsychologischer Dienst", 2010, null)
	}),

	/** Anrechnungsgrund 'Pädagogische Tätigkeiten im Bereich Ganztag und Übermittagsbetreuung' für Stunden bei einem Lehrer */
	ID_960(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(960000, "960", "Pädagogische Tätigkeiten im Bereich Ganztag und Übermittagsbetreuung", 2011, null)
	}),

	/** Anrechnungsgrund 'Aufsicht im Bereich Ganztag und Übermittagsbetreuung (halbe Anrechnung)' für Stunden bei einem Lehrer */
	ID_965(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
			new LehrerKatalogAnrechnungsgrundEintrag(965000, "965", "Aufsicht im Bereich Ganztag und Übermittagsbetreuung (halbe Anrechnung)", 2011, null)
	}),

	/** Anrechnungsgrund 'sonstige nichtunterrichtliche Tätigkeiten (Nur mit Genehmigung durch die Schulaufsicht.; auch Beratungstätigkeiten für sonderpäd. Förderung)' für Stunden bei einem Lehrer */
	ID_970(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(970000, "970", "sonstige nichtunterrichtliche Tätigkeiten (Nur mit Genehmigung durch die Schulaufsicht.; auch Beratungstätigkeiten für sonderpäd. Förderung)", null, null)
	}),

	/** Anrechnungsgrund 'Begleitung Schulversuch Gemeinschaftsschule' für Stunden bei einem Lehrer */
	ID_975(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
		new LehrerKatalogAnrechnungsgrundEintrag(975000, "975", "Begleitung Schulversuch Gemeinschaftsschule", 2011, null)
	}),

	/** Anrechnungsgrund 'Übergangsbegleitung im Rahmen des Langzeitpraktikums im Ausbildungskonsens KAoA' für Stunden bei einem Lehrer */
	ID_985(new LehrerKatalogAnrechnungsgrundEintrag[]{ 
			new LehrerKatalogAnrechnungsgrundEintrag(985000, "985", "Übergangsbegleitung im Rahmen des Langzeitpraktikums im Ausbildungskonsens KAoA", 2015, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 2;	
	
	/** Der aktuellen Daten des Abgangsgrundes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogAnrechnungsgrundEintrag daten;
	
	/** Die Historie mit den Einträgen des Abgangsgrundes */
	public final @NotNull LehrerKatalogAnrechnungsgrundEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Anrechnungsgründen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerAnrechnungsgrund> _gruendeByID = new HashMap<>();

	/** Eine Hashmap mit allen Anrechnungsgründen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerAnrechnungsgrund> _gruendeByKuerzel = new HashMap<>();


	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 * 
	 * @param historie   die Historie des Anrechnungsgrundes, welches ein Array von {@link LehrerKatalogAnrechnungsgrundEintrag} ist  
	 */
	private LehrerAnrechnungsgrund(final @NotNull LehrerKatalogAnrechnungsgrundEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerAnrechnungsgrund> getMapGruendeByID() {
		if (_gruendeByID.size() == 0)
			for (final LehrerAnrechnungsgrund g : LehrerAnrechnungsgrund.values())
				_gruendeByID.put(g.daten.id, g);				
		return _gruendeByID;
	}


	/**
	 * Gibt eine Map von den Kürzeln der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 */
	private static @NotNull HashMap<@NotNull String, LehrerAnrechnungsgrund> getMapGruendeByKuerzel() {
		if (_gruendeByKuerzel.size() == 0)
			for (final LehrerAnrechnungsgrund g : LehrerAnrechnungsgrund.values())
				_gruendeByKuerzel.put(g.daten.kuerzel, g);				
		return _gruendeByKuerzel;
	}


	/**
	 * Gibt den Anrechnungsgrund anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Anrechnungsgrundes
	 * 
	 * @return der Anrechnungsgrund oder null, falls die ID ungültig ist. 
	 */
	public static LehrerAnrechnungsgrund getByID(final long id) {
		return getMapGruendeByID().get(id);
	}


	/**
	 * Gibt den Anrechnungsgrund anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Anrechnungsgrundes
	 * 
	 * @return der Anrechnungsgrund oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerAnrechnungsgrund getByKuerzel(final String kuerzel) {
		return getMapGruendeByKuerzel().get(kuerzel);
	}

}
