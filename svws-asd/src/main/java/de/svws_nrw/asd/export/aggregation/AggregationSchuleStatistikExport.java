package de.svws_nrw.asd.export.aggregation;

import java.util.LinkedList;

import de.svws_nrw.asd.data.statistik.SchuleAdressenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.SchuleAdressenStatistikExport;
import de.svws_nrw.asd.export.data.SchuleStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.schule.FormOffenerGanztag;
import de.svws_nrw.asd.types.schule.Reformpaedagogik;

/*
 * AggregationStatistikExport.java
 *
 * Copyright (c) 2026 Projekt SVWS-Server - Schulverwaltungsserver
 *
 * Landesbetrieb Information und Technik Nordrhein-Westfalen (IT.NRW)
 * Alle Rechte vorbehalten.
 *
 * Versionshistorie
 * @version 1.00 - 11.03.2026 - Daniel Knittel (knitt01) - erste Version
 * @version 1.00 - 11.03.2026 - Mahmoud Guedda (guedd01) - erste Version
 */

/**
 * Die Klasse AggregationStatistikExport ist eine Klasse im Paket de.svws_nrw.asd.export.aggregation des Projekts SVWS-Server.
 *
 * @since 2026
 * @version 1.00 - 11.03.2026
 * @author Daniel Knittel (knitt01)
 * @author Mahmoud Guedda (guedd01)
 *
 */
public class AggregationSchuleStatistikExport {


	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final LinkedList<String> fehlermeldungen;

	/**
	 * Die für den Export vorgesehenen Statistikdaten mit den Aggregaten.
	 */
	private final StatistikExport statistikExport;

	/**
	 * Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
	 */
	private final StatistikGesamt statistikGesamt;



	/**
	 * Konstruktor
	 * @param statistikGesamt
	 * @param statistikExport
	 * @param fehlermeldungen
	 */
	public AggregationSchuleStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final LinkedList<String> fehlermeldungen) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.fehlermeldungen = fehlermeldungen;
	}


	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der Schule in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		// B01 und B02
		erstellenSchuleStatistikExport();

		return true;
	}


	/**
	 * Erstellen der 'Adressen einer Schule (B02)'. <br>
	 * Die Adresse wird der Liste {@link SchuleStatistikExport#adressenStatistikExport} im Objekt {@link #statistikExport} hinzugefügt.
	 *
	 * @param adresse - Eine Adresse der Schule
	 */
	private void erstellenSchuleAdressenStatistikExport(final SchuleAdressenStatistikGesamt adresse) {
		final SchuleAdressenStatistikExport adresseExport = new SchuleAdressenStatistikExport();
		adresseExport.id = adresse.id;
		adresseExport.adresskennzeichen = adresse.adresskennzeichen;

		// TODO: Soll noch von SVWS-Team aus Schuldateidaten gefüllt werden
		//	adresseExport.regionalschluesselLaenderkuerzel = adresse.regionalschluesselLaenderkuerzel;
		//	adresseExport.regionalschluesselGemeindekennzahl = adresse.regionalschluesselGemeindekennzahl;

		adresseExport.strassenname = adresse.strassenname;
		adresseExport.hausnummer = adresse.hausnummer;
		adresseExport.plz = adresse.plz;
		adresseExport.ort = adresse.ort;
		adresseExport.istHauptsitz = adresse.istHauptsitz;
		adresseExport.standortkennzeichen = adresse.standortkennzeichen;
		adresseExport.istAktiv = adresse.istAktiv;
		adresseExport.idArt = adresse.idArt;

		// TODO: Soll noch von SVWS-Team aus Schuldateidaten gefüllt werden
		//	adresseExport.verortungQualitaet = adresse.verortungQualitaet;
		//  adresseExport.koordinateRechtswert = adresse.koordinateRechtswert;
		//  adresseExport.koordinateHochwert = adresse.koordinateHochwert;

		//  adresseExport.istAdressvorgabedatensatz = adresse.istAdressvorgabedatensatz; // Kann evtl. entfallen
		//  adresseExport.datumStempelVorgabedaten = adresse.datumStempelVorgabedaten; // Kann evtl. entfallen

		// TODO: Summe bilden oder Kontrollzahl übernehmen?
		//  adresseExport.schuleAdressenSchuelerStatistikExport.insgesamtZusammen = adresse.;

		statistikExport.schuleStatistikExport.adressenStatistikExport.add(adresseExport);
	}

	/**
	 * Erstellen der 'Daten der Schule (B01)' und der 'Adressen einer Schule (B02)'.
	 */
	private void erstellenSchuleStatistikExport() {
		statistikExport.schuleStatistikExport.schulNr = statistikGesamt.schule.schulNr;
		statistikExport.schuleStatistikExport.schulform = statistikGesamt.schule.schulform;
		statistikExport.schuleStatistikExport.bezeichnung1 = statistikGesamt.schule.bezeichnung1;
		statistikExport.schuleStatistikExport.bezeichnung2 = statistikGesamt.schule.bezeichnung2;
		statistikExport.schuleStatistikExport.bezeichnung3 = statistikGesamt.schule.bezeichnung3;
		final StringBuilder strassenname = new StringBuilder();
		strassenname.append(statistikGesamt.schule.strassenname == null ? "" : statistikGesamt.schule.strassenname.strip());
		strassenname.append(" ");
		strassenname.append(statistikGesamt.schule.hausnummer == null ? "" : statistikGesamt.schule.hausnummer.strip());
		strassenname.append(" ");
		strassenname.append(statistikGesamt.schule.hausnummerZusatz == null ? "" : statistikGesamt.schule.hausnummerZusatz.strip());
		statistikExport.schuleStatistikExport.strassenname = strassenname.toString();
		statistikExport.schuleStatistikExport.plz = statistikGesamt.schule.plz;
		statistikExport.schuleStatistikExport.ort = statistikGesamt.schule.ort;
		statistikExport.schuleStatistikExport.telefon = statistikGesamt.schule.telefon;
		statistikExport.schuleStatistikExport.fax = statistikGesamt.schule.fax;
		statistikExport.schuleStatistikExport.email = statistikGesamt.schule.email;
		statistikExport.schuleStatistikExport.webAdresse = statistikGesamt.schule.webAdresse;
		statistikExport.schuleStatistikExport.zeitmodel = statistikGesamt.schule.dauerUnterrichtseinheit != 45 ? 1 : 45;
		//TODO: Wo ist der zugehörige Katalog? Einbinden.
		statistikExport.schuleStatistikExport.gebundenerGanztag = statistikGesamt.schule.idGebundenerGanztag; // ?Katalog
		statistikExport.schuleStatistikExport.istOffenerGanztag = statistikGesamt.schule.istOffenerGanztag;
		statistikExport.schuleStatistikExport.formOffenerGanztag =
				FormOffenerGanztag.data().getNameByIDOrNull(statistikGesamt.schule.idFormOffenerGanztag);
		statistikExport.schuleStatistikExport.istJva = statistikGesamt.schule.istJva;
		//TODO: Wo ist der zugehörige Katalog? Einbinden.
//		statistikExport.schuleStatistikExport.bilingualerUnterricht = statistikGesamt.schule.idBilingualerUnterricht; // ?Katalog
		statistikExport.schuleStatistikExport.hatRealschuleHauptschulbildungsgang = statistikGesamt.schule.hatRealschuleHauptschulbildungsgang;
		statistikExport.schuleStatistikExport.hatInternationaleKontakte = statistikGesamt.schule.hatInternationaleKontakte;
		statistikExport.schuleStatistikExport.hatKonfessionelleKooperation = statistikGesamt.schule.hatKonfessionelleKooperation;
		//TODO: 2027 Ausgelaufen-noch erhalten?
		statistikExport.schuleStatistikExport.talentschule = (int) statistikGesamt.schule.idTalentschule; // ?2027 Ausgelaufen-noch erhalten?
		statistikExport.schuleStatistikExport.reformpaedagogik = Reformpaedagogik.data().getNameByIDOrNull(statistikGesamt.schule.idReformpaedagogik);

		statistikGesamt.schule.adressen.stream().forEach(this::erstellenSchuleAdressenStatistikExport);
	}


}
