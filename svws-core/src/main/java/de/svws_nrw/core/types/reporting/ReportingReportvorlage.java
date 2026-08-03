package de.svws_nrw.core.types.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationGost;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationKlassen;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationKurse;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationLehrer;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationSchueler;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationStundenplanung;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Eine ENUM der integrierten Report-Vorlagen des SVWS-Servers.
 * Im Rahmen des Reportings werden auf Basis dieses Core-Typs Template-Definitionen vorgenommen.
 * Die Benennung der Vorlagen erfolgt nach dem Schema <code>Hauptdaten_v_Detaildaten</code>.
 * Bei der Report-Generierung erfolgt in Teilen ein entsprechendes Füllen der Datenkontexte anhand der Benennung.
 * Daher gilt es folgende Punkte zu beachten:
 * <ul>
 *   <li>Die Bezeichnung der Vorlagen muss eindeutig sein und darf keine Leerzeichen enthalten. Zulässig sind nur Buchstaben, Ziffern, Bindestriche und
 *   Unterstriche.</li>
 *   <li>Die Parametergruppen sowie die Parameter müssen je ENUM-Eintrag eindeutig sein und dürfen ebenfalls keine Leerzeichen enthalten. Auch hier sind nur
 *   Buchstaben, Ziffern, Bindestriche und Unterstriche zulässig.</li>
 * </ul>
 * Die zu jeder Vorlage zugeordneten Parametergruppen und Parameter werden in der Konfiguration der Vorlage definiert. Zu den jeweiligen Hauptdaten gibt es
 * jeweils eine eigene ReportingReportvorlageKonfiguration-Datei (z.B. ReportingReportvorlageKonfigurationGost.java). Diese Dateien werden in der
 * ReportvorlageKonfiguration-Package gespeichert.
 */

// SONARQUBE WARNUNG: Es sollen Konstanten für wiederkehrende Strings definiert werden. Das ist in einer ENUM nicht ohne Komplexitätserhöhung möglich.
@SuppressWarnings("java:S1192")
public enum ReportingReportvorlage {

	/** Report-Vorlage: GOSt - Klausurplanung - Klausurtermine-Kurse */
	GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN("GostKlausurplanung-KlausurtermineMitKursen",
			"Klausurplan der Kurse",
			"Einen Plan mit den Klausurterminen der Kurse erzeugen.",
			ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE,
			"gost/klausurplanung/GostKlausurplanungKlausurtermineMitKursen.html",
			"GOSt-Klausurplanung-Klausurtermine-Kurse",
			List.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
					BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION),
			ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVKlausurtermineMitKursen()
	),

	/** Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren */
	GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN("GostKlausurplanung-SchuelerMitKlausuren",
			"Klausurplan der Schülerinnen und Schüler",
			"Einen Plan mit den Klausurterminen der Schülerinnen und Schüler erzeugen.",
			ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER,
			"gost/klausurplanung/GostKlausurplanungSchuelerMitKlausuren.html",
			"GOSt-Klausurplanung-Schueler-Klausuren",
			List.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
					BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION),
			ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVSchuelerMitKlausuren()
	),

	/** Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler */
	GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN("GostKursplanung-KursMitKursschuelern",
			"Kursliste",
			"Eine Liste mit den Schülerinnen und Schülern der Kurse aus der GOSt-Kursplaung erzeugen.",
			ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE,
			"gost/kursplanung/GostKursplanungKursMitKursschuelern.html",
			"GOSt-Blockungsergebnis-Kurs-Schueler",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationGost.getGostKursplanungVKursMitKursschuelern()
	),

	/** Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte */
	GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN("GostKursplanung-KurseMitStatistikwerten",
			"Kursstatistik",
			"Eine Liste mit den Kursen aus der GOSt-Kursplanung und ihren Statistikwerten erzeugen.",
			ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE,
			"gost/kursplanung/GostKursplanungKurseMitStatistikwerten.html",
			"GOSt-Blockungsergebnis-Kurse-Statistikwerte",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationGost.getGostKursplanungVKurseMitStatistikwerten()
	),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN("GostKursplanung-SchuelerMitKursen",
			"Kurszuordnungen der Schülerinnen und Schüler",
			"Eien Übersicht mit den einzelnen Kurszuorndungen der Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.",
			ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER,
			"gost/kursplanung/GostKursplanungSchuelerMitKursen.html",
			"GOSt-Blockungsergebnis-Schueler-Kurse",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitKursen()
	),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN("GostKursplanung-SchuelerMitSchienenKursen",
			"Kurs-Schienen-Zuordnungen der Schülerinnen und Schüler",
			"Eine Übersicht mit den einzelnen Kurszuordnungen und deeren Schienen für die Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.",
			ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER,
			"gost/kursplanung/GostKursplanungSchuelerMitSchienenKursen.html",
			"GOSt-Blockungsergebnis-Schueler-Schienen-Kurse",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitSchienenKursen()
	),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN("GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken",
			"Fachwahlstatistiken",
			"Eine statische Übersicht der Fachwahlen eines Abiturjahrgangs aus der GOSt-Laufbahnplanung erzeugen.",
			ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG,
			"gost/laufbahnplanung/GostLaufbahnplanungAbiturjahrgangFachwahlstatistiken.html",
			"GOSt-Laufbahnplanung-Abiturjahrgang-Fachwahlstatistiken",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationGost.getGostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken()
	),

	/** Report-Vorlage: Klasse - Liste - Schüler - Fotos - Namen */
	KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN("Klasse-Liste-Schueler-Fotos-Namen",
			"Fotoübersicht klassenweise",
			"Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Klassen erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.KLASSEN,
			"klassen/KlasseListeSchuelerFotosNamen.html",
			"Klasse-Liste-Schueler-Fotos-Namen",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN),
			ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerFotosNamen()
	),

	/** Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher */
	KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Klasse-Liste-Schueler-Kontaktdaten-Erzieher",
			"Klassenliste mit Kontaktdaten",
			"Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Klassen erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.KLASSEN,
			"klassen/KlasseListeSchuelerKontaktdatenErzieher.html",
			"Klasse-Liste-Schueler-Kontaktdaten-Erzieher",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN),
			ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerKontaktdatenerzieher()
	),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN("Klasse-Liste-Schueler-Leistungsdaten",
			"Leistungsübersicht klassenweise",
			"Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.",
			ReportingReportvorlageDatenContext.KLASSEN,
			"klassen/leistungsdaten/KlasseListeSchuelerLeistungsdaten.html",
			"Klassen-Liste-Schueler-Leistungsdaten",
			List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN),
			ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdaten()
	),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten - Detailliert */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT("Klasse-Liste-Schueler-Leistungsdaten-Detailliert",
			"Leistungsübersicht klassenweise (detailliert)",
			"Eine detaillierte Übersicht der Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.",
			ReportingReportvorlageDatenContext.KLASSEN,
			"klassen/leistungsdaten/KlasseListeSchuelerLeistungsdatenDetailliert.html",
			"Klassen-Liste-Schueler-Leistungsdaten-Detailliert",
			List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN),
			ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdatenDetailliert()
	),

	/** Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher */
	KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Kurs-Liste-Schueler-Kontaktdaten-Erzieher",
			"Kursliste mit Kontaktdaten",
			"Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Kurse erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.KURSE,
			"kurse/KursListeSchuelerKontaktdatenErzieher.html",
			"Kurs-Liste-Schueler-Kontaktdaten-Erzieher",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN),
			ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerKontaktdatenerzieher()
	),

	/** Report-Vorlage: Kurs - Liste - Schüler - Fotos - Namen */
	KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN("Kurs-Liste-Schueler-Fotos-Namen",
			"Fotoübersicht kursweise",
			"Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Kurse erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.KURSE,
			"kurse/KursListeSchuelerFotosNamen.html",
			"Kurs-Liste-Schueler-Fotos-Namen",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN),
			ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerFotosNamen()
	),

	/** Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten */
	KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN("Kurs-Liste-Schueler-Leistungsdaten",
			"Leistungsübersicht kursweise",
			"Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Kurse erzeugen.",
			ReportingReportvorlageDatenContext.KURSE,
			"kurse/leistungsdaten/KursListeSchuelerLeistungsdaten.html",
			"Kurs-Liste-Schueler-Leistungsdaten",
			List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN),
			ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerLeistungsdaten()
	),

	/** Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten */
	LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN("Lehrer-Liste-Schueler-Leistungsdaten",
			"Leistungsdaten der Lerngruppen",
			"Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Lehrkräfte nach Lerngruppen erzeugen",
			ReportingReportvorlageDatenContext.LEHRER,
			"lehrer/leistungsdaten/LehrerListeSchuelerLeistungsdaten.html",
			"Lehrer-Liste-Schueler-Leistungsdaten",
			List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN),
			ReportingReportvorlageKonfigurationLehrer.getLehrerVListeSchuelerLeistungsdaten()
	),

	/** Report-Vorlage: Lehrer - Stammdaten - Liste */
	LEHRER_V_STAMMDATENLISTE("Lehrer-Stammdatenliste",
			"Stammdatenliste der Lehrkräfte",
			"Stammdatenliste der Lehrkräfte erzeugen.",
			ReportingReportvorlageDatenContext.LEHRER,
			"lehrer/stammdaten/LehrerStammdatenliste.html",
			"Lehrer-Stammdatenliste",
			List.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN),
			ReportingReportvorlageKonfigurationLehrer.getLehrerVStammdatenliste()
	),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A4 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4("Schueler-GostAbiturApoAnlage12-A4",
			"APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A4)", "Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12",
			ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR,
			"schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A4.html",
			"APO-GOSt-Anlage12",
			List.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A4()
	),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A3 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3("Schueler-GostAbiturApoAnlage12-A3",
			"APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A3)", "Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12",
			ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR,
			"schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A3.html",
			"APO-GOSt-Anlage12",
			List.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A3()
	),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT("Schueler-GostLaufbahnplanungErgebnisuebersicht",
			"Ergebnisübersicht der GOSt-Laufbahnplanung",
			"Ergebnisübersicht der GOSt-Laufbahnplanung nach Schülerinnen und Schüler für Beratungslehrkräfte erzeugen.",
			ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG,
			"schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungErgebnisuebersicht.html",
			"GOSt-Laufbahnplanung-Pruefungsergebnisse",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungErgebnisuebersicht()
	),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN("Schueler-GostLaufbahnplanungWahlbogen",
			"GOST-Laufbahnwahlbogen",
			"Die GOST-Laufbahnwahlbögen für Schülerinnen und Schüler erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG,
			"schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungWahlbogen.html",
			"GOSt-Laufbahnplanung-Wahlboegen",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN),
			ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungWahlbogen()
	),

	/** Report-Vorlage: Schüler - Schulbescheinigung */
	SCHUELER_V_SCHULBESCHEINIGUNG("Schueler-Schulbescheinigung",
			"Schulbescheinigung",
			"Eine Schulbescheinigung für Schülerinnen und Schüler oder deren Erziehungsberechtigte erzeugen.",
			ReportingReportvorlageDatenContext.SCHUELER,
			"schueler/anschreiben/SchuelerSchulbescheinigung.html",
			"Schueler-Schulbescheinigung",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN),
			ReportingReportvorlageKonfigurationSchueler.getSchuelerVSchulbescheinigung()
	),

	/** Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher */
	SCHUELER_V_LISTE_KONTAKTDATENERZIEHER("Schueler-Liste-Kontaktdaten-Erzieher",
			"Schülerliste mit Kontaktdaten",
			"Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.SCHUELER,
			"schueler/listen/SchuelerListeKontaktdatenErzieher.html",
			"Schueler-Liste-Kontaktdaten-Erzieher",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN),
			ReportingReportvorlageKonfigurationSchueler.getSchuelerVListeKontaktdatenerzieher()
	),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_FACH_STUNDENPLAN("Stundenplanung-FachStundenplan",
			"Fach-Stundenplan",
			"Den ausgewählten Stundenplan für die ausgewählten Fächer erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH,
			"stundenplanung/StundenplanungFachStundenplan.html",
			"Fach-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN),
			ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVFachStundenplan()
	),

	/** Report-Vorlage: Stundenplanung - Klasse - Stundenplan */
	STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN("Stundenplanung-KlassenStundenplan",
			"Klassen-Stundenplan",
			"Den ausgewählten Stundenplan für die ausgewählten Klassen erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN,
			"stundenplanung/StundenplanungKlassenStundenplan.html",
			"Klassen-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN),
			ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVKlassenStundenplan()
	),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN("Stundenplanung-LehrerStundenplan",
			"Lehrer-Stundenplan",
			"Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER,
			"stundenplanung/StundenplanungLehrerStundenplan.html",
			"Lehrer-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN),
			ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplan()
	),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT("Stundenplanung-LehrerStundenplanKombiniert",
			"Lehrer-Stundenplan kombiniert",
			"Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte in einer kombinierten Ansicht erzeugen",
			ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER,
			"stundenplanung/StundenplanungLehrerStundenplanKombiniert.html",
			"Lehrer-Stundenplan-Kombiniert",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN),
			ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplanKombiniert()
	),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_RAUM_STUNDENPLAN("Stundenplanung-RaumStundenplan",
			"Raum-Stundenplan",
			"Den ausgewählten Stundenplan für die ausgewählten Räume erzeugen.",
			ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM,
			"stundenplanung/StundenplanungRaumStundenplan.html",
			"Raum-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN),
			ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVRaumStundenplan()
	),

	/** Report-Vorlage: Stundenplanung - Schüler - Stundenplan */
	STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN("Stundenplanung-SchuelerStundenplan",
			"Schüler-Stundenplan",
			"Den ausgewählten Stundenplan für die ausgewählten Schülerinnen und Schüler erzeugen oder versenden.",
			ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER,
			"stundenplanung/StundenplanungSchuelerStundenplan.html",
			"Schueler-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN),
			ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVSchuelerStundenplan()
	);


	/** Die Bezeichnung der Report-Vorlage */
	private final @NotNull String bezeichnung;

	/** Der Titel, der in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Titel. */
	private final @NotNull String uiTitel;

	/** Die Beschreibung, die in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Beschreibung. */
	private final @NotNull String uiBeschreibung;

	/** Der Daten-Context, der für die HTML-Template-Datei verwendet wird. */
	private final @NotNull ReportingReportvorlageDatenContext datenContext;

	/** Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad. */
	private final @NotNull String pfadHtmlTemplate;

	/** Der statische Dateiname ohne Dateiendung, der bei der Ausgabe als ZIP-Datei verwendet wird. */
	private final @NotNull String dateiname;

	/** Die Liste mit Benutzerkompetenzen (als OR-Verknüpfung) gemäß {@link BenutzerKompetenz}, die zur Nutzung des Templates erforderlich sind. */
	private final @NotNull List<BenutzerKompetenz> benutzerKompetenzen;


	/** Reporting-Parameter inkl. der gültigen Vorlage-Parametergruppen für diese Report-Vorlage. */
	private final @NotNull ReportingParameter reportingParameter;

	/** Interne Map für direkten Zugriff auf Parameter: Reportvorlage > Parametername > Parameter */
	private static final @NotNull Map<String, Map<String, ReportingReportvorlageParameter>> MAP_PARAMETER = new HashMap<>();

	/** Interne Map für direkten Zugriff auf Parametergruppen: Reportvorlage > Parametergruppe > Parameterliste */
	private static final @NotNull Map<String, Map<String, ReportingReportvorlageParameterGruppe>> MAP_PARAMETERGRUPPEN = new HashMap<>();

	/** Interner Index, der Namensbestandteile der Enum mit den passenden Reportvorlagen verwaltet. */
	private static final @NotNull Map<String, List<ReportingReportvorlage>> MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN = new HashMap<>();

	/**
	 * Der Präfix des Config-Schlüssels, unter dem die benutzerspezifischen Einstellungen einer Report-Vorlage in der Client-Konfiguration gespeichert werden.
	 * Vervollständigt wird der Schlüssel je Report-Vorlage um deren {@link #bezeichnung} (siehe {@link #getConfigKeyBenutzerVorlage()}).
	 */
	private static final @NotNull String CONFIG_KEY_PREFIX_BENUTZER_VORLAGE = "reporting.einstellungen.benutzer.vorlage.";

	/** Gibt an, ob die internen Indexstrukturen bereits initialisiert wurden. */
	private static boolean mapsInitialisiert = false;

	/**
	 * Konstruktor für eine Reporting-Reportvorlage.
	 *
	 * @param bezeichnung         Die Bezeichnung der Reportvorlage.
	 * @param uiTitel             Der Titel, der in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Titel.
	 * @param uiBeschreibung      Die Beschreibung, die in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Beschreibung.
	 * @param datenContext        Der Hauptdaten-Context, der für die HTML-Template-Datei verwendet wird.
	 * @param pfadHtmlTemplate    Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad.
	 * @param dateiname           Der statische Dateiname ohne Dateiendung.
	 * @param benutzerKompetenzen Die Liste mit Benutzerkompetenzen.
	 * @param reportingParameter  Eine Liste mit den Vorlage-Parametern, basierend auf der jeweiligen Definition.
	 */
	// SONARQUBE WARNUNG: Es sollen max. 7 Paramater an den Konstruktor übergeben werden. Hier sind es nun mal acht, aber dennoch ist alles gut nachvollziehbar.
	@SuppressWarnings("java:S107")
	ReportingReportvorlage(final @NotNull String bezeichnung, final @NotNull String uiTitel,
			final @NotNull String uiBeschreibung, final @NotNull ReportingReportvorlageDatenContext datenContext,
			final @NotNull String pfadHtmlTemplate, final @NotNull String dateiname, final @NotNull List<BenutzerKompetenz> benutzerKompetenzen,
			final @NotNull ReportingParameter reportingParameter) {
		this.bezeichnung = bezeichnung;
		this.uiTitel = uiTitel;
		this.uiBeschreibung = uiBeschreibung;
		reportingParameter.reportvorlage = bezeichnung;
		this.datenContext = datenContext;
		this.pfadHtmlTemplate = pfadHtmlTemplate;
		this.dateiname = dateiname;
		this.benutzerKompetenzen = benutzerKompetenzen;
		this.reportingParameter = reportingParameter;
	}


	/**
	 * Gibt die Bezeichnung dieser Report-Vorlage zurück
	 *
	 * @return Die Bezeichnung dieser Report-Vorlage
	 */
	public @NotNull String getBezeichnung() {
		return (this.bezeichnung != null) ? this.bezeichnung : "";
	}

	/**
	 * Gibt den Schlüssel zurück, unter dem die benutzerspezifischen Einstellungen dieser Report-Vorlage in der Client-Konfiguration gespeichert werden
	 * (Schema {@code reporting.einstellungen.benutzer.vorlage.<bezeichnung>}). Die Client-Anwendung, unter der dieser Schlüssel abgelegt wird,
	 * ist stets {@link de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationBenutzerweit#CONFIG_APP_NAME}.
	 *
	 * @return der Config-Schlüssel für die benutzerspezifischen Einstellungen dieser Report-Vorlage
	 */
	public @NotNull String getConfigKeyBenutzerVorlage() {
		return CONFIG_KEY_PREFIX_BENUTZER_VORLAGE + this.bezeichnung;
	}

	/**
	 * Gibt den Titel für die Benutzeroberfläche zurück.
	 *
	 * @return Der Titel für die Benutzeroberfläche. Falls kein Titel gesetzt wurde, wird ein leerer String zurückgegeben.
	 */
	public @NotNull String getUiTitel() {
		return (this.uiTitel != null) ? this.uiTitel : "";
	}

	/**
	 * Gibt die Beschreibung für die Benutzeroberfläche zurück.
	 *
	 * @return Die Beschreibung für die  Benutzeroberfläche. Falls keine Beschreibung gesetzt wurde, wird ein leerer String zurückgegeben.
	 */
	public @NotNull String getUiBeschreibung() {
		return (this.uiBeschreibung != null) ? this.uiBeschreibung : "";
	}

	/**
	 * Liefert den Daten-Context der aktuellen HTML-Template-Definition.
	 *
	 * @return Der Daten-Context
	 */
	public @NotNull ReportingReportvorlageDatenContext getReportingReportvorlageDatenContext() {
		return this.datenContext;
	}

	/**
	 * Gibt den statischen Root-Pfad zurück.
	 *
	 * @return der Root-Pfad für alle Reporting-Templates
	 */
	public static @NotNull String getRootPfad() {
		return "de/svws_nrw/module/reporting/";
	}

	/**
	 * Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad.
	 *
	 * @return Der Dateipfad zur HTML-Template-Datei
	 */
	public @NotNull String getPfadHtmlTemplate() {
		return this.pfadHtmlTemplate;
	}

	/**
	 * Pfad zur HTML-Template-Datei, inklusive des Root-Pfads.
	 *
	 * @return Der Root-Dateipfad zur HTML-Template-Datei
	 */
	public @NotNull String getRootPfadHtmlTemplate() {
		return getRootPfad() + this.pfadHtmlTemplate;
	}

	/**
	 * Gibt den statischen Dateinamen ohne Dateiendung zurück.
	 *
	 * @return Der statische Dateiname
	 */
	public @NotNull String getDateiname() {
		return this.dateiname;
	}

	/**
	 * Gibt den relativen Pfad zur Dateinamensvorlage zurück, indem im Pfad der HTML-Vorlage die Endung ".html" durch ".name.tpl" ersetzt wird.
	 *
	 * @return Der Pfad zur Dateinamensvorlage.
	 */
	public @NotNull String getPfadDateinamensvorlage() {
		return this.pfadHtmlTemplate.replace(".html", ".name.tpl");
	}

	/**
	 * Gibt die Benutzer-Kompetenzen für diese Template-Definition zurück
	 *
	 * @return Die Liste der Benutzerkompetenzen
	 */
	public @NotNull List<BenutzerKompetenz> getBenutzerKompetenzen() {
		return this.benutzerKompetenzen;
	}

	/**
	 * Gibt eine Kopie der ReportingParamater für diese Report-Vorlage zurück. So wird verhindert, dass die Werte der ReportingParameter in der ENUM
	 * verändert werden können, was im Client Server weit greifen würde.
	 *
	 * @return Die Kopie der ReportingParameter für diese Report-Vorlage.
	 */
	public @NotNull ReportingParameter getReportingParameter() {
		return ReportingReportvorlageUtils.cloneReportingParameter(this.reportingParameter);
	}


	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 *
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static ReportingReportvorlage getByBezeichnung(final @NotNull String bezeichnung) {
		if (bezeichnung.isEmpty()) {
			return null;
		}
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values()) {
			if (rv.bezeichnung.equals(bezeichnung)) {
				return rv;
			}
		}
		return null;
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand des übergebenen Namens. Der Vergleich ignoriert Groß- und Kleinschreibung.
	 *
	 * @param name Der Name der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static ReportingReportvorlage getByName(final @NotNull String name) {
		if (name.isEmpty()) {
			return null;
		}
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values()) {
			if (rv.name().equalsIgnoreCase(name)) {
				return rv;
			}
		}
		return null;
	}

	/**
	 * Ermittelt alle Report-Vorlagen, deren Enum-Name alle übergebenen Namensbestandteile enthält. Dabei werden im Enum-Namen nur die durch '_' getrennten Teile
	 * berücksichtigt.
	 * Der Vergleich erfolgt ohne Beachtung der Groß-/Kleinschreibung für Namensbestandteile mit einer Länge größer oder gleich 3. Suchbegriffe, die diese
	 * Bedingungen nicht erfüllen, werden ignoriert.
	 *
	 * @param namensbestandteile  die Liste der Namensbestandteile, die in den Namensbestandteilen enthalten sein müssen
	 *
	 * @return eine Liste aller passenden Report-Vorlagen
	 */
	public static @NotNull List<ReportingReportvorlage> getByNamensbestandteilen(final @NotNull List<String> namensbestandteile) {
		mapsInitialisieren();

		final List<String> namensbestandteileNormalisiert = new ArrayList<>();
		for (final String bestandteil : namensbestandteile) {
			if ((bestandteil != null) && !bestandteil.isBlank() && (bestandteil.trim().length() >= 3)) {
				namensbestandteileNormalisiert.add(bestandteil.trim().toLowerCase());
			}
		}

		Set<ReportingReportvorlage> gueltigeVorlagen = null;
		for (final String suchbegriff : namensbestandteileNormalisiert) {
			final List<ReportingReportvorlage> treffer = MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(suchbegriff);
			if ((treffer == null) || treffer.isEmpty()) {
				return new ArrayList<>();
			}

			if (gueltigeVorlagen == null) {
				gueltigeVorlagen = new HashSet<>(treffer);
			} else {
				gueltigeVorlagen.retainAll(treffer);
				if (gueltigeVorlagen.isEmpty()) {
					return new ArrayList<>();
				}
			}
		}

		return (gueltigeVorlagen == null) ? new ArrayList<>() : new ArrayList<>(gueltigeVorlagen);
	}

	/**
	 * Liefert alle Default-Vorlageparameter der Reportvorlage über deren Bezeichnung als Liste ohne Gruppenzuordnung.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @return Liste der Default-Vorlageparameter der Reportvorlage (ggf. leer)
	 */
	public @NotNull List<ReportingReportvorlageParameter> getDefaultVorlageparameterByVorlage() {
		mapsInitialisieren();

		final String key1 = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		final Map<String, ReportingReportvorlageParameter> mapParam = MAP_PARAMETER.get(key1);
		return (mapParam == null) ? new ArrayList<>() : new ArrayList<>(mapParam.values());
	}

	/**
	 * Liefert alle Default-Vorlageparameter einer Parametergruppe aus der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parametergruppeName der Name der Vorlageparametergruppe
	 *
	 * @return Liste der Default-Vorlageparameter der Gruppe (ggf. leer)
	 */
	public @NotNull List<ReportingReportvorlageParameter> getDefaultVorlageparameterByGruppe(final @NotNull String parametergruppeName) {
		mapsInitialisieren();

		final String key1 = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		final String key2 = ReportingReportvorlageUtils.normalizeKeyInput(parametergruppeName);

		final Map<String, ReportingReportvorlageParameterGruppe> mapGruppen = MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen != null) {
			final ReportingReportvorlageParameterGruppe gruppe = mapGruppen.get(key2);
			if ((gruppe != null) && (gruppe.reportvorlageParameter != null)) {
				return gruppe.reportvorlageParameter;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Liefert genau den gewünschten Default-Vorlageparameter der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parameterName der Name des Vorlageparameters
	 *
	 * @return der Default-Vorlageparameter oder null, falls nicht vorhanden.
	 */
	public ReportingReportvorlageParameter getDefaultVorlageparameter(final @NotNull String parameterName) {
		mapsInitialisieren();

		final String key1 = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		final String key3 = ReportingReportvorlageUtils.normalizeKeyInput(parameterName);

		final Map<String, ReportingReportvorlageParameter> mapParam = MAP_PARAMETER.get(key1);
		return (mapParam != null) ? mapParam.get(key3) : null;
	}

	/**
	 * Setzt den Wert eines Vorlageparameters innerhalb der ReportingParameter, die übergeben werden. Die Default-Werte der ENUM werden dadurch nicht angepasst.
	 *
	 * @param reportingParameter   das zu verändernde ReportingParameter-Objekt.
	 * @param vorlageparameterName der Name des zu suchenden Vorlageparameters
	 * @param wert                 der zu setzende Wert als String
	 */
	public void setReportingParameterVorlageparameter(final @NotNull ReportingParameter reportingParameter, final @NotNull String vorlageparameterName,
			final @NotNull String wert) {
		for (final ReportingReportvorlageParameterGruppe gruppe : reportingParameter.reportvorlageParameterGruppen) {
			if (gruppe.reportvorlageParameter == null) {
				continue;
			}
			for (final ReportingReportvorlageParameter param : gruppe.reportvorlageParameter) {
				if ((param.name != null) && param.name.equals(vorlageparameterName)) {
					param.wert = wert;
					return;
				}
			}
		}
	}

	/**
	 * Liefert die Default-Vorlageparametergruppe der Reportvorlage über deren Namen.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param gruppenName der Name der Vorlageparametergruppe
	 *
	 * @return die Default-Vorlageparametergruppe oder null, falls nicht vorhanden.
	 */
	public ReportingReportvorlageParameterGruppe getDefaultVorlageparametergruppeByName(final @NotNull String gruppenName) {
		mapsInitialisieren();

		final String key1 = ReportingReportvorlageUtils.normalizeKeyInput(this.bezeichnung);
		final String key2 = ReportingReportvorlageUtils.normalizeKeyInput(gruppenName);

		final Map<String, ReportingReportvorlageParameterGruppe> mapGruppen = MAP_PARAMETERGRUPPEN.get(key1);
		return (mapGruppen != null) ? mapGruppen.get(key2) : null;
	}


	// ##### Hilfsmethoden #####

	/** Initialisiert einmalig interne ListMaps/Indizes für schnelleren Zugriff. */
	private static synchronized void mapsInitialisieren() {
		if (mapsInitialisiert) {
			return;
		}

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			initialisiereParameterMaps(reportvorlage);
			initialisiereNamensbestandteileIndex(reportvorlage);
		}

		mapsInitialisiert = true;
	}

	/**
	 * Initialisiert die Maps für die Parameter und Parametergruppen einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static void initialisiereParameterMaps(final @NotNull ReportingReportvorlage reportvorlage) {
		final String key1 = ReportingReportvorlageUtils.normalizeKeyInput(reportvorlage.bezeichnung);
		Map<String, ReportingReportvorlageParameter> mapParam = MAP_PARAMETER.get(key1);
		if (mapParam == null) {
			mapParam = new HashMap<>();
			MAP_PARAMETER.put(key1, mapParam);
		}
		Map<String, ReportingReportvorlageParameterGruppe> mapGruppen = MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen == null) {
			mapGruppen = new HashMap<>();
			MAP_PARAMETERGRUPPEN.put(key1, mapGruppen);
		}

		for (final ReportingReportvorlageParameterGruppe gruppe : reportvorlage.reportingParameter.reportvorlageParameterGruppen) {
			if ((gruppe == null) || (gruppe.name == null) || (gruppe.reportvorlageParameter == null)) {
				continue;
			}

			final String key2 = ReportingReportvorlageUtils.normalizeKeyInput(gruppe.name);
			mapGruppen.put(key2, gruppe);

			for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
				if ((parameter == null) || (parameter.name == null)) {
					continue;
				}

				final String key3 = ReportingReportvorlageUtils.normalizeKeyInput(parameter.name);
				mapParam.put(key3, parameter);
			}
		}
	}

	/**
	 * Initialisiert den Suchindex für die Bestandteile des ENUM-Namens einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static void initialisiereNamensbestandteileIndex(final @NotNull ReportingReportvorlage reportvorlage) {
		final String[] enumNameBestandteile = reportvorlage.name().toLowerCase().split("_");
		for (final String bestandteil : enumNameBestandteile) {
			if ((bestandteil == null) || bestandteil.isBlank()) {
				continue;
			}
			// Da der Transpiler Probleme mit FomputeIfAbsend hat, wird hier auf diese Funktion verzichtet.
			@SuppressWarnings("java:S3824") List<ReportingReportvorlage> vorlagen = MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(bestandteil);
			if (vorlagen == null) {
				vorlagen = new ArrayList<>();
				MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.put(bestandteil, vorlagen);
			}
			vorlagen.add(reportvorlage);
		}
	}
}
