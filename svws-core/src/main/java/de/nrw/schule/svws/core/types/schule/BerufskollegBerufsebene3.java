package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Berufsebenen der Ebene 3 am Berufskolleg.
 */
public enum BerufskollegBerufsebene3 {

	/** Berufsebene 3 : Absatzwirtschaft */
	AB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300001000, 3, "AB", "Absatzwirtschaft", null, null)
	}),


	/** Berufsebene 3 : Agrarservice */
	AS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300002000, 3, "AS", "Agrarservice", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Bautechnik */
	BH(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300003000, 3, "BH", "Allgemeine Hochschulreife / Bautechnik", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Betriebswirtschaftslehre */
	BW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300004000, 3, "BW", "Allgemeine Hochschulreife / Betriebswirtschaftslehre", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Biologie, Chemie */
	BC(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300005000, 3, "BC", "Allgemeine Hochschulreife / Biologie, Chemie", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Chemie, Chemietechnik */
	CC(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300006000, 3, "CC", "Allgemeine Hochschulreife / Chemie, Chemietechnik", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Deutsch, Englisch */
	DE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300007000, 3, "DE", "Allgemeine Hochschulreife / Deutsch, Englisch", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Elektrotechnik */
	ET(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300008000, 3, "ET", "Allgemeine Hochschulreife / Elektrotechnik", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Ernährung */
	ER(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300009000, 3, "ER", "Allgemeine Hochschulreife / Ernährung", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Erziehungswissenschaften */
	EW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300010000, 3, "EW", "Allgemeine Hochschulreife / Erziehungswissenschaften", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Freizeitsportleiter (Sport/Gesundheitsförderung/Biologie) */
	FL(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300011000, 3, "FL", "Allgemeine Hochschulreife / Freizeitsportleiter (Sport/Gesundheitsförderung/Biologie)", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Fremdsprachenkorrespondent */
	FK(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300012000, 3, "FK", "Allgemeine Hochschulreife / Fremdsprachenkorrespondent", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Gesundheit */
	GE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300013000, 3, "GE", "Allgemeine Hochschulreife / Gesundheit", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Ingenieurwissenschaften */
	IW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300014000, 3, "IW", "Allgemeine Hochschulreife / Ingenieurwissenschaften", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Kunst/Englisch */
	KE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300015000, 3, "KE", "Allgemeine Hochschulreife / Kunst/Englisch", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Maschinenbautechnik */
	MT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300016000, 3, "MT", "Allgemeine Hochschulreife / Maschinenbautechnik", null, null)
	}),


	/** Berufsebene 3 : Allgemeine Hochschulreife / Mathematik, Informatik */
	MI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300017000, 3, "MI", "Allgemeine Hochschulreife / Mathematik, Informatik", null, null)
	}),


	/** Berufsebene 3 : angewandte Baudenkmalpflege */
	BP(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300018000, 3, "BP", "angewandte Baudenkmalpflege", null, null)
	}),


	/** Berufsebene 3 : Assistent(in) für Konstruktions- und Fertigungstechnik / AHR */
	AK(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300019000, 3, "AK", "Assistent(in) für Konstruktions- und Fertigungstechnik / AHR", null, null)
	}),


	/** Berufsebene 3 : Ausbau */
	AU(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300020000, 3, "AU", "Ausbau", null, null)
	}),


	/** Berufsebene 3 : Außenhandel */
	AH(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300021000, 3, "AH", "Außenhandel", null, null)
	}),


	/** Berufsebene 3 : Avionik */
	AI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300022000, 3, "AI", "Avionik", null, null)
	}),


	/** Berufsebene 3 : Bautechnischer Assistent / AHR */
	BA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300023000, 3, "BA", "Bautechnischer Assistent / AHR", null, null)
	}),


	/** Berufsebene 3 : Betriebstechnik */
	BT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300024000, 3, "BT", "Betriebstechnik", null, null)
	}),


	/** Berufsebene 3 : Biologisch-technische(r) Assistent(in) / AHR */
	BS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300025000, 3, "BS", "Biologisch-technische(r) Assistent(in) / AHR", null, null)
	}),


	/** Berufsebene 3 : Chemisch-technische(r) Assistent(in) / AHR */
	CA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300026000, 3, "CA", "Chemisch-technische(r) Assistent(in) / AHR", null, null)
	}),


	/** Berufsebene 3 : CNC-Systemtechnik */
	CS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300027000, 3, "CS", "CNC-Systemtechnik", null, null)
	}),


	/** Berufsebene 3 : Computer- und Kommunikationst. */
	CK(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300028000, 3, "CK", "Computer- und Kommunikationst.", null, null)
	}),


	/** Berufsebene 3 : Dienstleistungsgartenbau */
	DG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300029000, 3, "DG", "Dienstleistungsgartenbau", null, null)
	}),


	/** Berufsebene 3 : Elektrotechnische(r) Assistent(in) / AHR */
	EA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300030000, 3, "EA", "Elektrotechnische(r) Assistent(in) / AHR", null, null)
	}),


	/** Berufsebene 3 : energieeffiziente-ökologische Altbauerneuerung */
	AE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300031000, 3, "AE", "energieeffiziente-ökologische Altbauerneuerung", null, null)
	}),


	/** Berufsebene 3 : Erzieher/Erzieherin / AHR */
	EZ(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300032000, 3, "EZ", "Erzieher/Erzieherin / AHR", null, null)
	}),


	/** Berufsebene 3 : Finanzdienstleistungen */
	FD(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300033000, 3, "FD", "Finanzdienstleistungen", null, null)
	}),


	/** Berufsebene 3 : Finanzwirtschaft */
	FW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300034000, 3, "FW", "Finanzwirtschaft", null, null)
	}),


	/** Berufsebene 3 : Flugwerk/Triebwerk */
	FT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300035000, 3, "FT", "Flugwerk/Triebwerk", null, null)
	}),


	/** Berufsebene 3 : Fremdsprachen */
	FS(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300036000, 3, "FS", "Fremdsprachen", null, null)
	}),


	/** Berufsebene 3 : Gestaltungstechnische(r) Assistent(in)/AHR */
	GA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300037000, 3, "GA", "Gestaltungstechnische(r) Assistent(in)/AHR", null, null)
	}),


	/** Berufsebene 3 : Gesundheitsökonomie und -management */
	GM(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300038000, 3, "GM", "Gesundheitsökonomie und -management", null, null)
	}),


	/** Berufsebene 3 : Grafik-Design */
	GD(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300039000, 3, "GD", "Grafik-Design", null, null)
	}),


	/** Berufsebene 3 : Handelsmanagement (Schulversuch) */
	HM(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300040000, 3, "HM", "Handelsmanagement (Schulversuch)", null, null)
	}),


	/** Berufsebene 3 : Heilerziehung */
	HE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300041000, 3, "HE", "Heilerziehung", null, null)
	}),


	/** Berufsebene 3 : Hochbau */
	HB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300042000, 3, "HB", "Hochbau", null, null)
	}),


	/** Berufsebene 3 : Informationstechnische(r) Assistent(in) / AHR */
	IA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300043000, 3, "IA", "Informationstechnische(r) Assistent(in) / AHR", null, null)
	}),


	/** Berufsebene 3 : Kaufmännische(r) Assistent(in) / AHR */
	KA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300044000, 3, "KA", "Kaufmännische(r) Assistent(in) / AHR", null, null)
	}),


	/** Berufsebene 3 : Kokerei/Aufbereitungstechnik */
	KO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300045000, 3, "KO", "Kokerei/Aufbereitungstechnik", null, null)
	}),


	/** Berufsebene 3 : Labortechnik */
	LT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300046000, 3, "LT", "Labortechnik", null, null)
	}),


	/** Berufsebene 3 : Logistik */
	LO(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300047000, 3, "LO", "Logistik", null, null)
	}),


	/** Berufsebene 3 : Marketing-Kommunikation */
	MK(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300048000, 3, "MK", "Marketing-Kommunikation", null, null)
	}),


	/** Berufsebene 3 : Medien- und Eventmanagement */
	ME(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300049000, 3, "ME", "Medien- und Eventmanagement", null, null)
	}),


	/** Berufsebene 3 : Medizinische Verwaltung */
	MV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300050000, 3, "MV", "Medizinische Verwaltung", null, null)
	}),


	/** Berufsebene 3 : Ökologischer Landbau */
	OL(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300051000, 3, "OL", "Ökologischer Landbau", null, null)
	}),


	/** Berufsebene 3 : Personalwirtschaft */
	PE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300052000, 3, "PE", "Personalwirtschaft", null, null)
	}),


	/** Berufsebene 3 : Physikalisch-technische(r) Assistent(in) / AHR */
	PT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300053000, 3, "PT", "Physikalisch-technische(r) Assistent(in) / AHR", null, null)
	}),


	/** Berufsebene 3 : Praktikanten mit Förderverträgen */
	PF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300054000, 3, "PF", "Praktikanten mit Förderverträgen", null, null)
	}),


	/** Berufsebene 3 : Produktion u. Vermarktung */
	PV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300055000, 3, "PV", "Produktion u. Vermarktung", null, null)
	}),


	/** Berufsebene 3 : Produktionswirtschaft */
	PW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300056000, 3, "PW", "Produktionswirtschaft", null, null)
	}),


	/** Berufsebene 3 : Rechnungswesen */
	RW(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300057000, 3, "RW", "Rechnungswesen", null, null)
	}),


	/** Berufsebene 3 : Recht */
	RE(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300058000, 3, "RE", "Recht", null, null)
	}),


	/** Berufsebene 3 : Reiseverkehr/Touristik */
	RT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300059000, 3, "RT", "Reiseverkehr/Touristik", null, null)
	}),


	/** Berufsebene 3 : Schüler/-innen in berufsvorb. Maßn. der AV u. fr. Träger */
	AV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300060000, 3, "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. fr. Träger", null, null)
	}),


	/** Berufsebene 3 : Schüler/-innen m. Arbeitsverh. u. Praktikant. */
	AP(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300061000, 3, "AP", "Schüler/-innen m. Arbeitsverh. u. Praktikant.", null, null)
	}),


	/** Berufsebene 3 : Schüler/-innen ohne Arbeitsverhältnis */
	OA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300062000, 3, "OA", "Schüler/-innen ohne Arbeitsverhältnis", null, null)
	}),


	/** Berufsebene 3 : Service */
	SV(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300063000, 3, "SV", "Service", null, null)
	}),


	/** Berufsebene 3 : Sport und Freizeit (Schulversuch) */
	SF(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300064000, 3, "SF", "Sport und Freizeit (Schulversuch)", null, null)
	}),


	/** Berufsebene 3 : Steuern */
	ST(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300065000, 3, "ST", "Steuern", null, null)
	}),


	/** Berufsebene 3 : Tagebautechnik */
	TG(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300066000, 3, "TG", "Tagebautechnik", null, null)
	}),


	/** Berufsebene 3 : Technische(r) Assistent(in) für Betriebsinformatik / AHR */
	TA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300067000, 3, "TA", "Technische(r) Assistent(in) für Betriebsinformatik / AHR", null, null)
	}),


	/** Berufsebene 3 : Tiefbau */
	TB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300068000, 3, "TB", "Tiefbau", null, null)
	}),


	/** Berufsebene 3 : Tiefbautechnik */
	TT(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300069000, 3, "TT", "Tiefbautechnik", null, null)
	}),


	/** Berufsebene 3 : Umwelttechnische(r) Assistent(in) / AHR */
	UA(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300070000, 3, "UA", "Umwelttechnische(r) Assistent(in) / AHR", null, null)
	}),


	/** Berufsebene 3 : Werkstattjahr */
	WJ(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300071000, 3, "WJ", "Werkstattjahr", null, null)
	}),


	/** Berufsebene 3 : Wirtschaftsinformatik */
	WI(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300072000, 3, "WI", "Wirtschaftsinformatik", null, null)
	}),


	/** Berufsebene 3 : Anerkannter Ausbildungsberuf (2-jährig) */
	A2(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300073000, 3, "A2", "Anerkannter Ausbildungsberuf (2-jährig)", null, null)
	}),


	/** Berufsebene 3 : Anerkannter Ausbildungsberuf (3-jährig) */
	A3(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300074000, 3, "A3", "Anerkannter Ausbildungsberuf (3-jährig)", null, null)
	}),


	/** Berufsebene 3 : Anerkannter Ausbildungsberuf (3 1/2-jährig) */
	A4(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300075000, 3, "A4", "Anerkannter Ausbildungsberuf (3 1/2-jährig)", null, null)
	}),


	/** Berufsebene 3 : Ausbildungsberuf für Menschen mit Behinderungen */
	MB(new BerufskollegBerufsebeneKatalogEintrag[] {
		new BerufskollegBerufsebeneKatalogEintrag(300076000, 3, "MB", "Ausbildungsberuf für Menschen mit Behinderungen", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Berufsebene, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull BerufskollegBerufsebeneKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Berufsebene */
	public final @NotNull BerufskollegBerufsebeneKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Berufsebenen der Ebene 3, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull BerufskollegBerufsebene3> _ebenen = new HashMap<>();
	

	/**
	 * Erzeugt eine neue Berufsebene in der Aufzählung.
	 * 
	 * @param historie   die Historie der Berufsebene, welches ein Array von {@link BerufskollegBerufsebeneKatalogEintrag} ist  
	 */
	private BerufskollegBerufsebene3(@NotNull BerufskollegBerufsebeneKatalogEintrag@NotNull[] historie) {
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
	private static @NotNull HashMap<@NotNull String, @NotNull BerufskollegBerufsebene3> getMapBerufsebenenByKuerzel() {
		if (_ebenen.size() == 0) {
			for (BerufskollegBerufsebene3 s : BerufskollegBerufsebene3.values()) {
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
	public static BerufskollegBerufsebene3 getByKuerzel(String kuerzel) {
		return getMapBerufsebenenByKuerzel().get(kuerzel);
	}

}
