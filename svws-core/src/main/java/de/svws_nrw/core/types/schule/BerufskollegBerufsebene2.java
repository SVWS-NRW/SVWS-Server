package de.svws_nrw.core.types.schule;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Berufsebenen der Ebene 2 am Berufskolleg.
 */
public enum BerufskollegBerufsebene2 {

	/** Berufsebene 2 : Assistent/-in für Ernährung und Versorgung */
	EV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200001000, 2, "EV", "Assistent/-in für Ernährung und Versorgung", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Augenoptik */
	AA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200002000, 2, "AA", "Aufbaubildungsgang Augenoptik", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Betriebswirtschaft */
	AB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200003000, 2, "AB", "Aufbaubildungsgang Betriebswirtschaft", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Bewegung und Gesundheit */
	BG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200004000, 2, "BG", "Aufbaubildungsgang Bewegung und Gesundheit", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Bildung und Schulvorbereitung in Tageseinrichtungen f. Kinder */
	BS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200005000, 2, "BS", "Aufbaubildungsgang Bildung und Schulvorbereitung in Tageseinrichtungen f. Kinder", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Bildung, Erziehung u. Betreuung von Kindern unter drei Jahren */
	BE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200006000, 2, "BE", "Aufbaubildungsgang Bildung, Erziehung u. Betreuung von Kindern unter drei Jahren", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Controlling */
	CO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200007000, 2, "CO", "Aufbaubildungsgang Controlling", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Existenzgründung */
	XT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200008000, 2, "XT", "Aufbaubildungsgang Existenzgründung", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Existenzgründung (Schulversuch) */
	XS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200009000, 2, "XS", "Aufbaubildungsgang Existenzgründung (Schulversuch)", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Fachkraft für Beratung und Anleitung in der Pflege */
	BA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200010000, 2, "BA", "Aufbaubildungsgang Fachkraft für Beratung und Anleitung in der Pflege", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Fachkraft für heilpädagogische Förderung mit dem Pferd */
	HF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200011000, 2, "HF", "Aufbaubildungsgang Fachkraft für heilpädagogische Förderung mit dem Pferd", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Inklusive Pädagogik */
	IP(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200012000, 2, "IP", "Aufbaubildungsgang Inklusive Pädagogik", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Medienkompetenz in der Kinder- und Jugendhilfe */
	MK(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200013000, 2, "MK", "Aufbaubildungsgang Medienkompetenz in der Kinder- und Jugendhilfe", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Musikalische Förderung im sozialpädagogischen Arbeitsfeld */
	MF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200014000, 2, "MF", "Aufbaubildungsgang Musikalische Förderung im sozialpädagogischen Arbeitsfeld", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Naturwissenschaftlich-technische Früherziehung */
	NF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200015000, 2, "NF", "Aufbaubildungsgang Naturwissenschaftlich-technische Früherziehung", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Offene Ganztagsschule */
	OG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200016000, 2, "OG", "Aufbaubildungsgang Offene Ganztagsschule", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Praxisanleitung */
	PA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200017000, 2, "PA", "Aufbaubildungsgang Praxisanleitung", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Produktionslogistik (Schulversuch) */
	PL(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200018000, 2, "PL", "Aufbaubildungsgang Produktionslogistik (Schulversuch)", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Sozialmanagement */
	SM(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200019000, 2, "SM", "Aufbaubildungsgang Sozialmanagement", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Sprachförderung */
	SF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200020000, 2, "SF", "Aufbaubildungsgang Sprachförderung", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Technischer Umweltschutz */
	TU(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200021000, 2, "TU", "Aufbaubildungsgang Technischer Umweltschutz", null, null)
	}),


	/** Berufsebene 2 : Aufbaubildungsgang Unternehmensmanagement */
	UM(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200022000, 2, "UM", "Aufbaubildungsgang Unternehmensmanagement", null, null)
	}),


	/** Berufsebene 2 : Augenoptik */
	AU(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200023000, 2, "AU", "Augenoptik", null, null)
	}),


	/** Berufsebene 2 : Bau und Holztechnik */
	BH(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200024000, 2, "BH", "Bau und Holztechnik", null, null)
	}),


	/** Berufsebene 2 : Baudenkmalpflege u. Altbau. */
	BP(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200025000, 2, "BP", "Baudenkmalpflege u. Altbau.", null, null)
	}),


	/** Berufsebene 2 : Bautechnik */
	BT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200026000, 2, "BT", "Bautechnik", null, null)
	}),


	/** Berufsebene 2 : Bekleidungstechnik */
	BK(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200027000, 2, "BK", "Bekleidungstechnik", null, null)
	}),


	/** Berufsebene 2 : Bergbautechnik */
	BB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200028000, 2, "BB", "Bergbautechnik", null, null)
	}),


	/** Berufsebene 2 : Betriebswirtschaft */
	BW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200029000, 2, "BW", "Betriebswirtschaft", null, null)
	}),


	/** Berufsebene 2 : Biogentechnik */
	BO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200030000, 2, "BO", "Biogentechnik", null, null)
	}),


	/** Berufsebene 2 : Biologietechnik */
	BI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200031000, 2, "BI", "Biologietechnik", null, null)
	}),


	/** Berufsebene 2 : Chemietechnik */
	CT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200032000, 2, "CT", "Chemietechnik", null, null)
	}),


	/** Berufsebene 2 : Druck- und Medientechnik */
	DM(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200033000, 2, "DM", "Druck- und Medientechnik", null, null)
	}),


	/** Berufsebene 2 : Drucktechnik */
	DT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200034000, 2, "DT", "Drucktechnik", null, null)
	}),


	/** Berufsebene 2 : Edelmetallgestaltung */
	EM(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200035000, 2, "EM", "Edelmetallgestaltung", null, null)
	}),


	/** Berufsebene 2 : Elektrotechnik */
	ET(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200036000, 2, "ET", "Elektrotechnik", null, null)
	}),


	/** Berufsebene 2 : Fahrzeugtechnik */
	FT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200037000, 2, "FT", "Fahrzeugtechnik", null, null)
	}),


	/** Berufsebene 2 : Farb- und Lacktechnik */
	FL(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200038000, 2, "FL", "Farb- und Lacktechnik", null, null)
	}),


	/** Berufsebene 2 : Farbe, Gestaltung, Werbung */
	FG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200039000, 2, "FG", "Farbe, Gestaltung, Werbung", null, null)
	}),


	/** Berufsebene 2 : Farbtechnik und Raumgestaltung */
	FR(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200040000, 2, "FR", "Farbtechnik und Raumgestaltung", null, null)
	}),


	/** Berufsebene 2 : Galvanotechnik */
	GT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200041000, 2, "GT", "Galvanotechnik", null, null)
	}),


	/** Berufsebene 2 : Gartenbau */
	GB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200042000, 2, "GB", "Gartenbau", null, null)
	}),


	/** Berufsebene 2 : Gebäudesystemtechnik */
	GD(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200043000, 2, "GD", "Gebäudesystemtechnik", null, null)
	}),


	/** Berufsebene 2 : Gesundheit */
	GS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200044000, 2, "GS", "Gesundheit", null, null)
	}),


	/** Berufsebene 2 : Gesundheitswesen */
	GW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200045000, 2, "GW", "Gesundheitswesen", null, null)
	}),


	/** Berufsebene 2 : Gießereitechnik */
	GR(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200046000, 2, "GR", "Gießereitechnik", null, null)
	}),


	/** Berufsebene 2 : Glastechnik */
	GL(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200047000, 2, "GL", "Glastechnik", null, null)
	}),


	/** Berufsebene 2 : Großhaushalt */
	GO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200048000, 2, "GO", "Großhaushalt", null, null)
	}),


	/** Berufsebene 2 : Heilerziehungspflege */
	HP(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200049000, 2, "HP", "Heilerziehungspflege", null, null)
	}),


	/** Berufsebene 2 : Heilpädagogik */
	HD(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200050000, 2, "HD", "Heilpädagogik", null, null)
	}),


	/** Berufsebene 2 : Heizungs-, Lüftungs-, Klimatechnik */
	HL(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200051000, 2, "HL", "Heizungs-, Lüftungs-, Klimatechnik", null, null)
	}),


	/** Berufsebene 2 : Holztechnik */
	HT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200052000, 2, "HT", "Holztechnik", null, null)
	}),


	/** Berufsebene 2 : Hotel und Gaststätten */
	HG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200053000, 2, "HG", "Hotel und Gaststätten", null, null)
	}),


	/** Berufsebene 2 : Hotel- und Gaststättengewerbe */
	HB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200054000, 2, "HB", "Hotel- und Gaststättengewerbe", null, null)
	}),


	/** Berufsebene 2 : Informatik */
	IF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200055000, 2, "IF", "Informatik", null, null)
	}),


	/** Berufsebene 2 : Informations- und Telekommunikationstechnik */
	IT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200056000, 2, "IT", "Informations- und Telekommunikationstechnik", null, null)
	}),


	/** Berufsebene 2 : Kältetechnik */
	KT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200057000, 2, "KT", "Kältetechnik", null, null)
	}),


	/** Berufsebene 2 : Karosserie- und Fahrzeugbautechnik */
	KF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200058000, 2, "KF", "Karosserie- und Fahrzeugbautechnik", null, null)
	}),


	/** Berufsebene 2 : Kinderpfleger/-in */
	KI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200059000, 2, "KI", "Kinderpfleger/-in", null, null)
	}),


	/** Berufsebene 2 : Körperpflege */
	KP(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200060000, 2, "KP", "Körperpflege", null, null)
	}),


	/** Berufsebene 2 : Korrosionsschutztechnik */
	KO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200061000, 2, "KO", "Korrosionsschutztechnik", null, null)
	}),


	/** Berufsebene 2 : Kunststoff- und Kautschuktechnik */
	KK(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200062000, 2, "KK", "Kunststoff- und Kautschuktechnik", null, null)
	}),


	/** Berufsebene 2 : Labor- und Verfahrentechnik */
	LV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200063000, 2, "LV", "Labor- und Verfahrentechnik", null, null)
	}),


	/** Berufsebene 2 : Landwirtschaft */
	LW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200064000, 2, "LW", "Landwirtschaft", null, null)
	}),


	/** Berufsebene 2 : Lebensmitteltechnik */
	LT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200065000, 2, "LT", "Lebensmitteltechnik", null, null)
	}),


	/** Berufsebene 2 : Luftfahrttechnik */
	LF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200066000, 2, "LF", "Luftfahrttechnik", null, null)
	}),


	/** Berufsebene 2 : Marketing */
	MG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200067000, 2, "MG", "Marketing", null, null)
	}),


	/** Berufsebene 2 : Maschinenbautechnik */
	MB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200068000, 2, "MB", "Maschinenbautechnik", null, null)
	}),


	/** Berufsebene 2 : Mechatronik */
	MT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200069000, 2, "MT", "Mechatronik", null, null)
	}),


	/** Berufsebene 2 : Medien */
	MN(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200070000, 2, "MN", "Medien", null, null)
	}),


	/** Berufsebene 2 : Medien/Medientechnologie */
	MM(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200071000, 2, "MM", "Medien/Medientechnologie", null, null)
	}),


	/** Berufsebene 2 : Medizintechnik */
	MD(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200072000, 2, "MD", "Medizintechnik", null, null)
	}),


	/** Berufsebene 2 : Metallbautechnik */
	ML(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200073000, 2, "ML", "Metallbautechnik", null, null)
	}),


	/** Berufsebene 2 : Metalltechnik */
	ME(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200074000, 2, "ME", "Metalltechnik", null, null)
	}),


	/** Berufsebene 2 : Möbelhandel */
	MH(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200075000, 2, "MH", "Möbelhandel", null, null)
	}),


	/** Berufsebene 2 : Mode */
	MO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200076000, 2, "MO", "Mode", null, null)
	}),


	/** Berufsebene 2 : Motopädie */
	MI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200077000, 2, "MI", "Motopädie", null, null)
	}),


	/** Berufsebene 2 : Pädagogik */
	PG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200078000, 2, "PG", "Pädagogik", null, null)
	}),


	/** Berufsebene 2 : Physik, Chemie und Biologie */
	PU(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200079000, 2, "PU", "Physik, Chemie und Biologie", null, null)
	}),


	/** Berufsebene 2 : Physik, Chemie, Biologie */
	PB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200080000, 2, "PB", "Physik, Chemie, Biologie", null, null)
	}),


	/** Berufsebene 2 : Physik/Chemie/Biologie */
	PC(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200081000, 2, "PC", "Physik/Chemie/Biologie", null, null)
	}),


	/** Berufsebene 2 : Physiktechnik */
	PT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200082000, 2, "PT", "Physiktechnik", null, null)
	}),


	/** Berufsebene 2 : Sozialassistent/-in */
	SA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200083000, 2, "SA", "Sozialassistent/-in", null, null)
	}),


	/** Berufsebene 2 : Sozialassistent/-in - Heilerziehung */
	SH(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200084000, 2, "SH", "Sozialassistent/-in - Heilerziehung", null, null)
	}),


	/** Berufsebene 2 : Sozialpädagogik */
	SP(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200085000, 2, "SP", "Sozialpädagogik", null, null)
	}),


	/** Berufsebene 2 : Sozialwesen */
	SW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200086000, 2, "SW", "Sozialwesen", null, null)
	}),


	/** Berufsebene 2 : Spreng- und Sicherheitstechnik */
	SI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200087000, 2, "SI", "Spreng- und Sicherheitstechnik", null, null)
	}),


	/** Berufsebene 2 : Technische Informatik */
	TI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200088000, 2, "TI", "Technische Informatik", null, null)
	}),


	/** Berufsebene 2 : Textiltechnik */
	TT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200089000, 2, "TT", "Textiltechnik", null, null)
	}),


	/** Berufsebene 2 : Textiltechnik und Bekleidung */
	TB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200090000, 2, "TB", "Textiltechnik und Bekleidung", null, null)
	}),


	/** Berufsebene 2 : Tourismus */
	TO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200091000, 2, "TO", "Tourismus", null, null)
	}),


	/** Berufsebene 2 : Umweltschutztechnik */
	US(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200092000, 2, "US", "Umweltschutztechnik", null, null)
	}),


	/** Berufsebene 2 : Umwelttechnik */
	UT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200093000, 2, "UT", "Umwelttechnik", null, null)
	}),


	/** Berufsebene 2 : Vermessungstechnik */
	VT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200094000, 2, "VT", "Vermessungstechnik", null, null)
	}),


	/** Berufsebene 2 : Werkstofftechnik */
	WT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200095000, 2, "WT", "Werkstofftechnik", null, null)
	}),


	/** Berufsebene 2 : Wirtschaftsinformatik */
	WI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200096000, 2, "WI", "Wirtschaftsinformatik", null, null)
	}),


	/** Berufsebene 2 : Wohnungswirtschaft und Realkredit */
	WR(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(200097000, 2, "WR", "Wohnungswirtschaft und Realkredit", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Berufsebene, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull BerufskollegBerufsebeneKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Berufsebene */
	public final @NotNull BerufskollegBerufsebeneKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Berufsebenen der Ebene 2, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull BerufskollegBerufsebene2> _ebenen = new HashMap<>();
	

	/**
	 * Erzeugt eine neue Berufsebene in der Aufzählung.
	 * 
	 * @param historie   die Historie der Berufsebene, welches ein Array von {@link BerufskollegBerufsebeneKatalogEintrag} ist  
	 */
	private BerufskollegBerufsebene2(final @NotNull BerufskollegBerufsebeneKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}
	
	
	/**
	 * Gibt eine Map von den Kürzels der Berufsebenen auf die zugehörigen Berufsebenen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Berufsebene auf die zugehörigen Berufsebene
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull BerufskollegBerufsebene2> getMapBerufsebenenByKuerzel() {
		if (_ebenen.size() == 0) {
			for (final BerufskollegBerufsebene2 s : BerufskollegBerufsebene2.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt die Berufsebene für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Berufsebene
	 * 
	 * @return die Berufsebene oder null, falls das Kürzel ungültig ist
	 */
	public static BerufskollegBerufsebene2 getByKuerzel(final String kuerzel) {
		return getMapBerufsebenenByKuerzel().get(kuerzel);
	}

}
