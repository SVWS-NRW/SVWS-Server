package de.svws_nrw.core.types.benutzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.benutzer.BenutzerKompetenzKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Benutzerkompetenzen
 * zur Verfügung.
 */
public enum BenutzerKompetenz {


	/** Es werden keinerlei Kompetenzen benötigt. */
	KEINE(new BenutzerKompetenzKatalogEintrag(-2, BenutzerKompetenzGruppe.KEINE, "keine", null,
			"Es wird keine spezielle Benutzerkompetent benötigt, eine erfolgreiche Anmeldung am Server genügt."
	)),

	/** Es werden Admin-Rechte benötigt. */
	ADMIN(new BenutzerKompetenzKatalogEintrag(-1, BenutzerKompetenzGruppe.ADMIN, "admin", null,
			"Es werden administrative Kompetenzen benötigt."
	)),

	/** Es werden Rechte zum Ansehen der Schüler Individualdaten benötigt. */
	SCHUELER_INDIVIDUALDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			11, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ansehen", null,
			"Ermöglicht das Anzeigen aller gespeicherten Informationen eines Schülers (außer Leistungsdaten)"
	)),

	/** Es werden Rechte zum Ändern der Schüler Individualdaten benötigt. */
	SCHUELER_INDIVIDUALDATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			12, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ändern", null,
			"Erlaubt das Bearbeiten der persönlichen Schülerdaten wie Name, Adresse und Kontaktinformationen."
	)),

	/** Es werden Rechte zum endgültigen Löschen eines Schülers benötigt. */
	SCHUELER_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
			13, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Löschen", null,
			"Ermöglicht das dauerhafte Entfernen aller Schülerdaten aus dem System."
	)),

	/** Es werden Rechte zum Ändern der Schüler Vermerke benötigt. */
	SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN(new BenutzerKompetenzKatalogEintrag(
			14, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Vermerke ändern", null,
			"Gestattet das Hinzufügen oder Bearbeiten von Vermerken (Notizen) im Reiter 'Vermerke'."
	)),

	/** Es werden Rechte zum Ändern der Schüler KAoA-Daten benötigt. */
	SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			15, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "KAoA-Daten ändern", null,
			"Erlaubt das Bearbeiten der Berufsvorbereitungsdaten im Rahmen von KAoA im Reiter 'KAoA'."
	)),

	/** Es werden Rechte zum Ändern der Einwilligungen zu einem Schüler benötigt. */
	SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			16, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Einwilligungen ändern (DSGVO, Lernplattformen)", null,
			"Ermöglicht das Bearbeiten der Einwilligungen für Datenschutz und Lernplattformen."
	)),

	/** Es werden Rechte zum Ansehen der Schüler Leistungsdaten benötigt. */
	SCHUELER_LEISTUNGSDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			21, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Ansehen", null,
			"Ermöglicht das Anzeigen der schulischen Leistungen eines Schülers im Reiter 'Leistungsdaten'."
	)),

	/** Es werden Rechte zum funktionsbezogenen Ändern der Schüler Leistungsdaten benötigt. */
	SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			22, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Funktionsbezogen ändern", null,
			"Erlaubt das Bearbeiten der Leistungsdaten wie Noten für Abteilungs-, Stufen- oder Klassenleitungen."
	)),

	/** Es werden Rechte zum generellen Ändern der Schüler Leistungsdaten benötigt. */
	SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN(new BenutzerKompetenzKatalogEintrag(
			23, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Alle ändern", null,
			"Ermöglicht die vollständige Bearbeitung aller Leistungsdaten eines Schülers."
	)),

	/** Es werden Rechte zum Drucken der Berichte benötigt. */
	BERICHTE_ALLE_FORMULARE_DRUCKEN(new BenutzerKompetenzKatalogEintrag(
			31, BenutzerKompetenzGruppe.BERICHTE, "Alle Formulare drucken", null,
			"Erlaubt das Drucken sämtlicher Berichtsformulare eines Schülers, z. B. Zeugnisse oder Leistungsberichte."
	)),

	/** Es werden Rechte zum Drucken der Standardberichte benötigt. */
	BERICHTE_STANDARDFORMULARE_DRUCKEN(new BenutzerKompetenzKatalogEintrag(
			32, BenutzerKompetenzGruppe.BERICHTE, "Standard-Formulare drucken", null,
			"Bietet die Möglichkeit, vorab definierte Standardformulare wie Schulbescheinigungen oder Stammblätter auszudrucken."
	)),

	/** Es werden Rechte zum Ändern der Berichte benötigt. */
	BERICHTE_AENDERN(new BenutzerKompetenzKatalogEintrag(
			33, BenutzerKompetenzGruppe.BERICHTE, "Ändern", null,
			"Ermöglicht das Bearbeiten von Berichtsformularen, z. B. das Anpassen von Inhalten oder Formulierungen."
	)),

	/** Es werden Rechte zum Löschen der Berichte benötigt. */
	BERICHTE_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
			34, BenutzerKompetenzGruppe.BERICHTE, "Löschen", null,
			"Erlaubt das dauerhafte Entfernen von Berichtsformularen aus dem System."
	)),

	/** Es werden Rechte zum Importieren von Daten benötigt. */
	IMPORT_EXPORT_DATEN_IMPORTIEREN(new BenutzerKompetenzKatalogEintrag(
			41, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Daten importieren", null,
			"Ermöglicht das Einlesen von Schüler- oder Lehrerdaten aus externen Quellen wie CSV-Dateien."
	)),

	/** Es werden Rechte zum Exportieren von Schülerdaten benötigt. */
	IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN(new BenutzerKompetenzKatalogEintrag(
			42, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schülerdaten exportieren", null,
			"Erlaubt den Export von Schülerdaten in verschiedenen Dateiformaten zur weiteren Verarbeitung."
	)),

	/** Es werden Rechte zum Exportieren von Lehrerdaten benötigt. */
	IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN(new BenutzerKompetenzKatalogEintrag(
			43, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Lehrerdaten exportieren", null,
			"Bietet die Möglichkeit, Lehrerdaten für externe Systeme oder administrative Zwecke zu exportieren."
	)),

	/** Es werden Rechte zum Nutzen der SchILD-NRW-Schnittstelle benötigt. */
	IMPORT_EXPORT_SCHNITTSTELLE_SCHILD_NRW(new BenutzerKompetenzKatalogEintrag(
			44, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schnittstelle SchILD-NRW verwenden", null,
			"Ermöglicht die Nutzung der SchILD-NRW-Schnittstelle für den Datenaustausch mit externen Systemen."
	)),

	/** Es werden Rechte zum Ausführen des Access-DB-Exports benötigt. */
	IMPORT_EXPORT_ACCESS_DB(new BenutzerKompetenzKatalogEintrag(
			45, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Access-DB-Export durchführen", null,
			"Erlaubt den Export von Daten in eine Access-Datenbank zur Speicherung oder Weiterverarbeitung."
	)),

	/** Es werden Rechte zum Export über die XML-Schnittstellen benötigt. */
	IMPORT_EXPORT_XML(new BenutzerKompetenzKatalogEintrag(
			46, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Export über XML-Schnittstellen", null,
			"Ermöglicht den Export von Daten über standardisierte XML-Schnittstellen."
	)),

	/** Es werden Rechte zum Import und Export über die Schulbewerbung.de-Schnittstelle benötigt. */
	IMPORT_EXPORT_SCHULBEWERBUNG_DE(new BenutzerKompetenzKatalogEintrag(
			47, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Datenaustausch mit Schulbewerbung.de", null,
			"Bietet die Möglichkeit zum Datenaustausch mit Schulbewerbung.de."
	)),

	/** Es werden Rechte zum Import und Export über die Lernplattform-Schnittstelle benötigt. */
	IMPORT_EXPORT_LERNPLATTFORM(new BenutzerKompetenzKatalogEintrag(
			48, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Datenaustausch mit Lernplattformen", null,
			"Bietet die Möglichkeit zum Datenaustausch mit Lernplattformen."
	)),

	/** Es werden Rechte zum Ansehen der Schulbezogenen Daten benötigt. */
	SCHULBEZOGENE_DATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			61, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ansehen", null,
			"Ermöglicht das Anzeigen der schulbezogenen Daten wie Schuladresse, Telefonnummer und weitere Informationen."
	)),

	/** Es werden Rechte zum Ändern der Schulbezogenen Daten benötigt. */
	SCHULBEZOGENE_DATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			62, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ändern", null,
			"Erlaubt das Bearbeiten der schulbezogenen Daten, z. B. zur Aktualisierung von Kontaktdaten oder Adressen."
	)),

	/** Es werden Rechte benötigt um ein Backup durchzuführen. */
	EXTRAS_BACKUP_DURCHFUEHREN(new BenutzerKompetenzKatalogEintrag(
			71, BenutzerKompetenzGruppe.EXTRAS, "Backup durchführen", null,
			"Erlaubt die Möglichkeit, ein vollständiges Backup der aktuellen Daten zu erstellen."
	)),

	/** Es werden Rechte zum Wiederherstellen von gelöschten Schülerdaten benötigt. */
	EXTRAS_GELOESCHTE_DATEN_ZURUECKHOLEN(new BenutzerKompetenzKatalogEintrag(
			72, BenutzerKompetenzGruppe.EXTRAS, "Gelöschte Daten zurückholen", null,
			"Ermöglicht das Wiederherstellen versehentlich gelöschter Daten."
	)),

	/** Es werden Rechte zum Ändern der Farben für Fachgruppen benötigt. */
	EXTRAS_FARBEN_FUER_FACHGRUPPEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			73, BenutzerKompetenzGruppe.EXTRAS, "Farben für Fachgruppen ändern", null,
			"Erlaubt das Anpassen der Farben für die verschiedenen Fachgruppen in der Benutzeroberfläche."
	)),

	/** Es werden Rechte Import von Daten aus Kurs42 benötigt. */
	EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN(new BenutzerKompetenzKatalogEintrag(
			74, BenutzerKompetenzGruppe.EXTRAS, "Daten aus Kurs42 importieren", getSchulformenGymOb(),
			"Ermöglicht das Importieren von Daten aus dem Kurs42-System in die aktuelle Plattform."
	)),

	/** Es werden Rechte zum Bearbeiten von Personengruppen benötigt. */
	EXTRAS_DATEN_PERSONENGRUPPEN_BEARBEITEN(new BenutzerKompetenzKatalogEintrag(
			75, BenutzerKompetenzGruppe.EXTRAS, "Personengruppen bearbeiten", null,
			"Bietet die Möglichkeit, Personengruppen aus Lehrkräften, Schülern, Erziehungsberechtigten oder außerschulischen Kontaktpersonen zu erstellen."
	)),

	/** Es werden Rechte zum Ansehen von Katalogen benötigt. */
	KATALOG_EINTRAEGE_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			81, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ansehen", null,
			"Ermöglicht das Anzeigen von aktuellen Katalogeinträgen unter dem Menüpunkt 'Schule' an."
	)),

	/** Es werden Rechte zum Ändern von Katalogen benötigt. */
	KATALOG_EINTRAEGE_AENDERN(new BenutzerKompetenzKatalogEintrag(
			82, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ändern", null,
			"Erlaubt das Bearbeiten von Katalogeinträgen zur Aktualisierung der Inhalte unter dem Menüpunkt 'Schule'."
	)),

	/** Es werden Rechte zum Löschen von Katalogen benötigt. */
	KATALOG_EINTRAEGE_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
			83, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Löschen", null,
			"Ermöglicht das dauerhafte Entfernen von Katalogeinträgen, die nicht mehr benötigt werden."
	)),

	/** Es werden Rechte zum Ansehen von Lehrerdaten benötigt. */
	LEHRERDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			91, BenutzerKompetenzGruppe.LEHRERDATEN, "Ansehen", null,
			"Zeigt unter dem Menüpunkt Lehrkräfte den Reiter 'Individualdaten' mit den Informationen wie Name, Geburtsdatum und Kontaktinformationen an."
	)),

	/** Es werden Rechte zum Ändern von Lehrerdaten benötigt. */
	LEHRERDATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			92, BenutzerKompetenzGruppe.LEHRERDATEN, "Ändern", null,
			"Erlaubt das Bearbeiten von Lehrkraftdaten unter dem Menüpunkt 'Lehrkräfte', Reiter 'Individualdaten"
	)),

	/** Es werden Rechte zum Löschen von Lehrerdaten benötigt. */
	LEHRERDATEN_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
			93, BenutzerKompetenzGruppe.LEHRERDATEN, "Löschen", null,
			"Ermöglicht das dauerhafte Entfernen von Lehrkraftdaten aus dem System."
	)),

	/** Es werden Rechte zum Ansehen von Lehrerdetaildaten benötigt. */
	LEHRER_PERSONALDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			94, BenutzerKompetenzGruppe.LEHRERDATEN, "Personaldaten ansehen", null,
			"Ermöglicht das Anzeigen von 'Personaldaten' wie Lehrbefähigung, Beschäftigungsverhältnis und Anrechnungsstunden."
	)),

	/** Es werden Rechte zum Ändern von Lehrerdetaildaten benötigt. */
	LEHRER_PERSONALDATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			95, BenutzerKompetenzGruppe.LEHRERDATEN, "Personaldaten ändern", null,
			"Erlaubt das Bearbeiten der Personaldaten von Lehrkräften."
	)),

	/** Es werden Rechte zum Ansehen von Daten des Verfahrens Schulpflichtverletzung benötigt. */
	SCHULPFLICHTVERLETZUNG_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			101, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ansehen", null,
			"Zeigt die aktuellen Verfahren bei Schulpflichtverletzungen an."
	)),

	/** Es werden Rechte zum Ändern von Daten des Verfahrens Schulpflichtverletzung benötigt. */
	SCHULPFLICHTVERLETZUNG_AENDERN(new BenutzerKompetenzKatalogEintrag(
			102, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ändern", null,
			"Erlaubt das Bearbeiten der Verfahren bei Schulpflichtverletzungen, um aktuelle Entwicklungen zu dokumentieren."
	)),

	/** Es werden Rechte zum Löschen von Daten des Verfahrens Schulpflichtverletzung benötigt. */
	SCHULPFLICHTVERLETZUNG_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
			103, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Löschen", null,
			"Ermöglicht das dauerhafte Entfernen von Verfahrensdokumenten aus dem System."
	)),

	/** Es werden Rechte zum Ansehen der Unterrichtsverteilung bis zum aktuellen Schuljahresabschnitt benötigt. */
	UNTERRICHTSVERTEILUNG_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			105, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Unterrichtsverteilung ansehen", null,
			"Zeigt die aktuelle Unterrichtsverteilung im System an."
	)),

	/** Es werden Rechte zum Ansehen der Unterrichtsverteilung auch nach dem aktuellen Schuljahresabschnitt benötigt. */
	UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			106, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Planung der Unterrichtsverteilung ansehen", null,
			"Ermöglicht das Anzeigen der Planungsdaten zur Unterrichtsverteilung."
	)),

	/** Es werden Rechte zum Ändern der Unterrichtsverteilung (allgemein) benötigt. */
	UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			107, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Unterrichtsverteilung ändern (allgemein)", null,
			"Erlaubt das Bearbeiten der allgemeinen Unterrichtsverteilung für alle Klassen."
	)),

	/** Es werden Rechte zum Ändern der Unterrichtsverteilung (funktionsbezogen) benötigt. */
	UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			108, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Unterrichtsverteilung ändern (funktionsbezogen)", null,
			"Ermöglicht die funktionsbezogene Anpassung der Unterrichtsverteilung für Abteilungs-, Stufen- oder Klassenleitungen."
	)),

	/** Es werden Rechte zum Ansehen von Stundenplänen (allgemein) benötigt. */
	STUNDENPLAN_ALLGEMEIN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			111, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenplan ansehen (allgemein)", null,
			"Zeigt den allgemeinen Stundenplan einer Klasse, einer Stufe oder einer Lehrkraft."
	)),

	/** Es werden Rechte zum Ansehen von Stundenplänen (funktionsbezogen) benötigt. */
	STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			112, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenplan ansehen (funktionsbezogen)", null,
			"Ermöglicht das Anzeigen des Stundenplans nur für Abteilungs-, Stufen- oder Klassenleitungen."
	)),

	/** Es werden Rechte zum Importieren von Stundenplänen benötigt. */
	STUNDENPLAN_IMPORT(new BenutzerKompetenzKatalogEintrag(
			113, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne importieren", null,
			"Bietet die Möglichkeit, Stundenpläne aus externen Quellen zu importieren."
	)),

	/** Es werden Rechte zum Exportieren von Stundenplänen benötigt. */
	STUNDENPLAN_EXPORT(new BenutzerKompetenzKatalogEintrag(
			114, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne exportieren", null,
			"Ermöglicht das Exportieren von Stundenplänen in verschiedene Formate."
	)),

	/** Es werden Rechte zum Erstellen von Stundenplänen benötigt. */
	STUNDENPLAN_AENDERN(new BenutzerKompetenzKatalogEintrag(
			115, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne ändern", null,
			"Erlaubt das Bearbeiten und Anpassen der bestehenden Stundenpläne."
	)),

	/** Es werden Rechte zum Aktivieren von Stundenplänen benötigt. */
	STUNDENPLAN_AKTIVIEREN(new BenutzerKompetenzKatalogEintrag(
			116, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne aktivieren", null,
			"Bietet die Möglichkeit, Stundenpläne zu aktivieren, damit sie in Kraft treten."
	)),

	/** Es werden Rechte zur Administration des Notenmoduls benötigt. */
	NOTENMODUL_ADMINISTRATION(new BenutzerKompetenzKatalogEintrag(
			131, BenutzerKompetenzGruppe.NOTENMODUL, "Administration Notenmodul", null,
			"Ermöglicht die Verwaltung des Notenmoduls für alle Klassen und Fächer."
	)),

	/** Es werden Rechte zur Änderung von Noten im Notenmodul (allgemein) benötigt. */
	NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			132, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (allgemein)", null,
			"Erlaubt das Bearbeiten und Anpassen von allgemeinen Noten der Schüler."
	)),

	/** Es werden Rechte zur Änderung von Noten im Notenmodul (funktionsbezogen) benötigt. */
	NOTENMODUL_NOTEN_AENDERN_FUNKTION(new BenutzerKompetenzKatalogEintrag(
			133, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (funktionsbezogen)", null,
			"Bietet die Möglichkeit, Noten als Abteilungs-, Stufen- oder Klassenleitung zu ändern."
	)),

	/** Es werden Rechte zum Ansehen von Noten im Notenmodul (allgemein) benötigt. */
	NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			134, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (allgemein)", null,
			"Ermöglicht das Anzeigen aller Noten eines Schülers in allen Fächern."
	)),

	/** Es werden Rechte zum Ansehen von Noten im Notenmodul (funktionsbezogen) benötigt. */
	NOTENMODUL_NOTEN_ANSEHEN_FUNKTION(new BenutzerKompetenzKatalogEintrag(
			135, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (funktionsbezogen)", null,
			"Ermöglicht das Anzeigen aller Noten eines Schülers nur für Abteilungs-, Stufen- oder Klassenleitungen."
	)),

	/** Es werden Rechte zur Administration der Datenbank (Schema erstellen/migrieren) benötigt. */
	DATENBANK_SCHEMA_ERSTELLEN(new BenutzerKompetenzKatalogEintrag(
			141, BenutzerKompetenzGruppe.DATENBANK, "Schema erstellen und migrieren", null,
			"Bietet die Möglichkeit, ein neues Datenbankschema zu erstellen oder bestehende zu migrieren."
	)),

	/** Es werden Rechte zum Import von SQLite-Backups benötigt. */
	DATENBANK_SQLITE_IMPORT(new BenutzerKompetenzKatalogEintrag(
			142, BenutzerKompetenzGruppe.DATENBANK, "SQLite importieren (Backup einspielen)", null,
			"Ermöglicht das Importieren eines SQLite-Backups zur Wiederherstellung von Daten."
	)),

	/** Es werden Rechte zum Export von SQLite-Backups benötigt. */
	DATENBANK_SQLITE_EXPORT(new BenutzerKompetenzKatalogEintrag(
			143, BenutzerKompetenzGruppe.DATENBANK, "SQLite exportieren (Backup erstellen)", null,
			"Erlaubt das Erstellen eines Backups im SQLite-Format zur Datensicherung."
	)),

	/** Es werden Rechte zur Durchführung der Laufbahnplanung (allgemein) benötigt. */
	OBERSTUFE_ABITURJAHRGAENGE_VERWALTEN(new BenutzerKompetenzKatalogEintrag(
			160, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturjahrgänge anlegen und löschen", getSchulformenGymOb(),
			"Ermöglicht das Anlegen und Löschen von Abiturjahrgängen unter dem Menüpunkt 'Oberstufe'."
	)),

	/** Es werden Rechte zur Durchführung der Laufbahnplanung (allgemein) benötigt. */
	OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			161, BenutzerKompetenzGruppe.OBERSTUFE, "Laufbahnplanung (allgemein)", getSchulformenGymOb(),
			"Bietet die Möglichkeit, die allgemeine Laufbahnplanung für Schüler in der Oberstufe zu verwalten."
	)),

	/** Es werden Rechte zur Durchführung der Laufbahnplanung (stufenbezogen) benötigt. */
	OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			162, BenutzerKompetenzGruppe.OBERSTUFE, "Laufbahnplanung (funktionsbezogen)", getSchulformenGymOb(),
			"Erlaubt das Bearbeiten der Laufbahnplanung für Schüler nur für Abteilungs- und Stufenleitungen."
	)),

	/** Es werden Rechte zum Import von Laufbahnpdaten aus LuPO benötigt. */
	OBERSTUFE_LUPO_IMPORT(new BenutzerKompetenzKatalogEintrag(
			163, BenutzerKompetenzGruppe.OBERSTUFE, "Laufbahnplanung aus LuPO importieren", getSchulformenGymOb(),
			"Ermöglicht den Import von Laufbahndaten aus dem LuPO-System."
	)),

	/** Es werden Rechte zur Kursverwaltung - Blocken (allgemein) benötigt. */
	OBERSTUFE_KURSPLANUNG_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			171, BenutzerKompetenzGruppe.OBERSTUFE, "Kursplanung - Blocken (allgemein)", getSchulformenGymOb(),
			"Bietet die Möglichkeit, für einen Abiturjahrgang eine Blockung anzulegen oder aus bestehenden Daten wieder herzustellen."
	)),

	/** Es werden Rechte zur Kursverwaltung - Blocken (stufenbezogen) benötigt. */
	OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			172, BenutzerKompetenzGruppe.OBERSTUFE, "Kursplanung - Blocken (funktionsbezogen)", getSchulformenGymOb(),
			"Ermöglicht das Anlegen oder die Wiederherstellung von Blockungen nur für Abteilungs- und Stufenleitungen."
	)),

	/** Es werden Rechte zur Aktivierung einer Blockung benötigt. */
	OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN(new BenutzerKompetenzKatalogEintrag(
			173, BenutzerKompetenzGruppe.OBERSTUFE, "Kursplanung - Blockung aktivieren", getSchulformenGymOb(),
			"Erlaubt das Aktivieren der Kursblockung, um die Planung zu finalisieren und in die Leistungsdaten zu übernehmen."
	)),

	/** Es werden Rechte zur Bearbeitung einer Klausurplanung benötigt. */
	OBERSTUFE_KLAUSURPLANUNG_AENDERN(new BenutzerKompetenzKatalogEintrag(
			181, BenutzerKompetenzGruppe.OBERSTUFE, "Klausurplanung ändern", getSchulformenGymOb(),
			"Bietet die Möglichkeit, eine Klausurplanung im Menübereich Oberstufe, Reiter 'Klausurplanung' zu ändern."
	)),

	/** Es werden Rechte zum Ansehen einer Klausurplanung (allgemein) benötigt. */
	OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			182, BenutzerKompetenzGruppe.OBERSTUFE, "Klausurplanung ansehen (allgemein)", getSchulformenGymOb(),
			"Erlaubt das Anzeigen der Klausurplanung eines Abiturjahrgangs in einem Halbjahr."
	)),

	/** Es werden Rechte zum Ansehen einer Klausurplanung (funktionsbezogen) benötigt. */
	OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION(new BenutzerKompetenzKatalogEintrag(
			183, BenutzerKompetenzGruppe.OBERSTUFE, "Klausurplanung ansehen (funktionsbezogen)", getSchulformenGymOb(),
			"Erlaubt das Anzeigen der Klausurplanung eines Abiturjahrgangs in einem Halbjahr nur für Abteilungs- oder Stufenleitungen."
	)),

	/** Berechtigung zum Ansehen von Abiturdaten der Oberstufe (allgemein). */
	ABITUR_ANSEHEN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			191, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ansehen (allgemein)", getSchulformenGymOb(),
			"Erlaubt das Anzeigen der Abiturdaten eines Jahrgangs."
	)),

	/** Berechtigung zum Ansehen von Abiturdaten der Oberstufe (funktionsbezogen). */
	ABITUR_ANSEHEN_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			192, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ansehen (funktionsbezogen)", getSchulformenGymOb(),
			"Erlaubt das Anzeigen der Abiturdaten eines Jahrgangs nur für Abteilungs- oder Stufenleitungen."
	)),

	/** Berechtigung zum Ändern aller Daten zum Abitur (allgemein). */
	ABITUR_AENDERN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			193, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ändern (allgemein)", getSchulformenGymOb(),
			"Erlaubt das Bearbeiten der Abiturdaten wie Noteneintragungen oder Gesamtberechnungen."
	)),

	/** Berechtigung zum Ändern aller Daten zum Abitur (funktionsbezogen). */
	ABITUR_AENDERN_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			194, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ändern (funktionsbezogen)", getSchulformenGymOb(),
			"Erlaubt das Bearbeiten der Abiturdaten wie Noteneintragungen oder Gesamtberechnungen nur für Abteilungs- oder Stufenleitungen."
	)),

	/** Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein). */
	ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			195, BenutzerKompetenzGruppe.OBERSTUFE, "Prüfungsergebnisse eingeben (allgemein)", getSchulformenGymOb(),
			"Ermöglicht das Eingeben der Prüfungsergebnisse eines Schülers."
	)),

	/** Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen). */
	ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			196, BenutzerKompetenzGruppe.OBERSTUFE, "Prüfungsergebnisse eingeben (funktionsbezogen)", getSchulformenGymOb(),
			"Ermöglicht das Eingeben der Prüfungsergebnisse eines Schülers nur für Abteilungs- oder Stufenleitungen."
	)),

	/** Allgemeine Berechtigung zum Verwenden der CardDav API für Addressdaten */
	CARDDAV_NUTZEN(new BenutzerKompetenzKatalogEintrag(
			201, BenutzerKompetenzGruppe.CARDDAV, "Addressbuch (CardDAV) nutzen", null,
			"Ermöglicht die Nutzung des Adressbuchs über CardDAV zur Verwaltung von Kontakten."
	)),

	/** Es werden Rechte zum Ansehen der Adressdaten eines von Erziehungsberechtigten benötigt */
	CARDDAV_ERZIEHER_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
			202, BenutzerKompetenzGruppe.CARDDAV, "Erzieherdaten im Addressbuch anzeigen", null,
			"Zeigt die Erzieherdaten im Adressbuch an."
	)),

	/** Allgemeine Berechtigung zum Verwenden der CalDav API für Kalenderdaten */
	CALDAV_NUTZEN(new BenutzerKompetenzKatalogEintrag(
			301, BenutzerKompetenzGruppe.CALDAV, "Kalender (CalDAV) nutzen", null,
			"Ermöglicht die Nutzung des Kalenders über CalDAV zur Verwaltung von Terminen."
	)),

	/** Berechtigung für den Besitz und das Bearbeiten eines eigenen Kalenders über die CalDav API. */
	CALDAV_EIGENER_KALENDER(new BenutzerKompetenzKatalogEintrag(
			302, BenutzerKompetenzGruppe.CALDAV, "Eigener Kalender", null,
			"Bietet die Möglichkeit, den persönlichen Kalender zu verwalten und anzupassen."
	)),

	/** Allgemeine Berechtigung zum Ansehen der generierten Kalenderdaten */
	CALDAV_KALENDER_ANSEHEN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			303, BenutzerKompetenzGruppe.CALDAV, "Kalender Ansehen (Allgemein)", null,
			"Erlaubt das Ansehen des Kalender zur Terminübersicht."
	)),

	/** Funktionsbezogene Berechtigung zum Ansehen generierter Kalender */
	CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			304, BenutzerKompetenzGruppe.CALDAV, "Kalender Ansehen (Funktionsbezogen)", null,
			"Ermöglicht das Anzeigen des Kalenders nur für spezifische Nutzergruppen."
	)),

	/** Berechtigung zum Ansehen von Abschlussdaten der Sekundarstufe I (allgemein). */
	ABSCHLUSS_SEKI_ANSEHEN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			401, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (allgemein)", null,
			"Ermöglicht das Anzeigen von Abschlussdaten der Sekundarstufe I."
	)),

	/** Berechtigung zum Ansehen von Abschlussdaten der Sekundarstufe I (funktionsbezogen). */
	ABSCHLUSS_SEKI_ANSEHEN_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			402, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (funktionsbezogen)", null,
			"Ermöglicht das Anzeigen von Abschlussdaten der Sekundarstufe I nur für Abteilungs-,  Stufen- oder Klassenleitungen."
	)),

	/** Berechtigung zum Ändern aller Abschlussdaten der Sekundarstufe I (allgemein). */
	ABSCHLUSS_SEKI_AENDERN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			403, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (allgemein)", null,
			"Erlaubt das Bearbeiten der Abschlussdaten in der Sekundarstufe I."
	)),

	/** Berechtigung zum Ändern aller Abschlussdaten der Sekundarstufe I (funktionsbezogen). */
	ABSCHLUSS_SEKI_AENDERN_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			404, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (funktionsbezogen)", null,
			"Erlaubt die Bearbeitung von Abschlussdaten der Sekundarstufe I nur für Abteilungs-,  Stufen- oder Klassenleitungen."
	)),

	/** Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein). */
	ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			405, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (allgemein)", null,
			"Ermöglicht das Eingeben der Prüfungsergebnisse in der Sekundarstufe I."
	)),

	/** Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen). */
	ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			406, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (funktionsbezogen)", null,
			"Ermöglicht das Eingeben der Prüfungsergebnisse in der Sekundarstufe I nur für Abteilungs-,  Stufen- oder Klassenleitungen."
	)),

	/** Berechtigung zum Ansehen von Abschlussdaten an einer berufsbildenden Schule (allgemein). */
	ABSCHLUSS_BK_ANSEHEN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			501, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB),
			"Erlaubt das Anzeigen der Abschlussdaten an berufsbildenden Schulen."
	)),

	/** Berechtigung zum Ansehen von Abschlussdaten an einer berufsbildenden Schule (funktionsbezogen). */
	ABSCHLUSS_BK_ANSEHEN_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			502, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB),
			"Erlaubt das Anzeigen der Abschlussdaten an berufsbildenden Schulen eines Jahrgangs nur für Abteilungs- oder Stufenleitungen."
	)),

	/** Berechtigung zum Ändern aller Abschlussdaten an einer berufsbildenden Schule (allgemein). */
	ABSCHLUSS_BK_AENDERN_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			503, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB),
			"Erlaubt das Bearbeiten der Abschlussdaten an berufsbildenden Schulen wie Noteneintragungen oder Gesamtberechnungen."
	)),

	/** Berechtigung zum Ändern aller Abschlussdaten an einer berufsbildenden Schule (funktionsbezogen). */
	ABSCHLUSS_BK_AENDERN_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			504, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB),
			"Erlaubt das Bearbeiten der Abschlussdaten an berufsbildenden Schulen wie Noteneintragungen oder Gesamtberechnungen nur für Abteilungs- oder Stufenleitungen."
	)),

	/** Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein). */
	ABSCHLUSS_BK_EINGABE_ERGEBNISSE_ALLGEMEIN(new BenutzerKompetenzKatalogEintrag(
			505, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB),
			"Ermöglicht das Eingeben der Prüfungsergebnisse eines Schülers an berufsbildenden Schulen."
	)),

	/** Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen). */
	ABSCHLUSS_BK_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN(new BenutzerKompetenzKatalogEintrag(
			506, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB),
			"Ermöglicht das Eingeben der Prüfungsergebnisse eines Schülers an berufsbildenden Schulen nur für Abteilungs- oder Stufenleitungen."
	));


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 2;

	/** Die Daten der Benutzerkompetenz */
	public final @NotNull BenutzerKompetenzKatalogEintrag daten;


	/** Eine HashMap zum schnellen Zugriff auf ein Aufzählungsobjekt anhand der ID der Benutzerkompetenz */
	private static final @NotNull HashMap<Long, BenutzerKompetenz> _mapID = new HashMap<>();

	/** Eine ArrayMap zum schnellen Zugriff auf die Benutzer-Kompetenzen anhand der Benutzer-Kompetenz-Gruppe*/
	private static final @NotNull ArrayMap<BenutzerKompetenzGruppe, List<BenutzerKompetenz>> _mapGruppenZuordnung =
			new ArrayMap<>(BenutzerKompetenzGruppe.values());


	/**
	 * Erzeugt eine neue Benutzerkompetenz für die Aufzählung.
	 *
	 * @param daten   die Daten der Benutzerkompetenz
	 */
	BenutzerKompetenz(final @NotNull BenutzerKompetenzKatalogEintrag daten) {
		this.daten = daten;
	}


	/**
	 * Gibt eine Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 */
	private static @NotNull HashMap<Long, BenutzerKompetenz> getMapID() {
		if (_mapID.size() == 0)
			for (final @NotNull BenutzerKompetenz p : BenutzerKompetenz.values())
				_mapID.put(p.daten.id, p);
		return _mapID;
	}

	/**
	 * Gibt eine Map von den Benutzerkompetenzen-Gruppen auf die zugehörigen Benutzerkompetenzen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Benutzerkompetenzen-Gruppen auf die zugehörigen Benutzerkompetenzen
	 */
	private static @NotNull Map<BenutzerKompetenzGruppe, List<BenutzerKompetenz>> getMapGruppenZuordnung() {
		if (_mapGruppenZuordnung.size() == 0) {
			for (final @NotNull BenutzerKompetenzGruppe g : BenutzerKompetenzGruppe.values())
				_mapGruppenZuordnung.put(g, new ArrayList<>());
			for (final @NotNull BenutzerKompetenz p : BenutzerKompetenz.values()) {
				BenutzerKompetenzGruppe gruppe = BenutzerKompetenzGruppe.getByID(p.daten.gruppe_id);
				if (gruppe == null)
					gruppe = BenutzerKompetenzGruppe.KEINE;
				final List<@NotNull BenutzerKompetenz> liste = _mapGruppenZuordnung.get(gruppe);
				if (liste != null)
					liste.add(p);
			}
		}
		return _mapGruppenZuordnung;
	}


	/**
	 * Gibt die Benutzerkompetenz anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID der Benutzerkompetenz
	 *
	 * @return die Benutzerkompetenz oder null, falls die ID fehlerhaft ist
	 */
	public static BenutzerKompetenz getByID(final long id) {
		return getMapID().get(id);
	}

	/**
	 * Überprüft, ob die Kompetenz mit k_id für die Schulform mit s_id zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Abfrage bezieht
	 * @param schulform  Die Schulform
	 *
	 * @return true, wenn die Kompetenz für die Schulform zulässig ist.
	 */
	public boolean hatSchulform(final int schuljahr, final Schulform schulform) {
		if (schulform == null)
			return false;
		if (daten.nurSchulformen != null)
			return daten.nurSchulformen.contains(schulform.name());
		return true;
	}

	/**
	 * Gibt die Liste aller Benutzerkompetenzen zurück, welche der übergebenen Gruppe
	 * zugeordnet sind.
	 *
	 * @param gruppe   die Benutzerkompetenz-Gruppe
	 *
	 * @return die Liste der Benutzerkompetenzen
	 */
	public static @NotNull List<BenutzerKompetenz> getKompetenzen(final @NotNull BenutzerKompetenzGruppe gruppe) {
		final List<BenutzerKompetenz> liste = getMapGruppenZuordnung().get(gruppe);
		if (liste == null)
			return new ArrayList<>();
		return liste;
	}

	/**
	 * Gibt die Liste aller Benutzerkompetenzen zurück, welche der übergebenen Gruppe
	 * zugeordnet sind und die übergebene Schulform besitzen.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Abfrage bezieht
	 * @param gruppe   die Benutzerkompetenz-Gruppe
	 * @param schulform   die Schulform
	 *
	 * @return die Liste der Benutzerkompetenzen
	 */
	public static @NotNull List<BenutzerKompetenz> getKompetenzenMitSchulform(final int schuljahr,
			final @NotNull BenutzerKompetenzGruppe gruppe, final @NotNull Schulform schulform) {
		final List<BenutzerKompetenz> l = new ArrayList<>();
		final List<BenutzerKompetenz> liste = getMapGruppenZuordnung().get(gruppe);
		if (liste == null)
			return l;
		for (final BenutzerKompetenz bk : liste) {
			if (bk.hatSchulform(schuljahr, schulform))
				l.add(bk);
		}
		return l;
	}


	private static @NotNull List<Schulform> getSchulformenGymOb() {
		final @NotNull List<Schulform> result = new ArrayList<>();
		result.add(Schulform.FW);
		result.add(Schulform.GE);
		result.add(Schulform.GY);
		result.add(Schulform.SG);
		result.add(Schulform.WF);
		return result;
	}

}

