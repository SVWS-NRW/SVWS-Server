package de.svws_nrw.core.types.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationGost;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationKlassen;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationKurse;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationLehrer;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationSchueler;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationStundenplanung;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageSchulformen;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Eine ENUM der integrierten Report-Vorlagen des SVWS-Servers.
 * Im Rahmen des Reportings werden auf Basis dieses Core-Typs Template-Definitionen vorgenommen.
 * Die Angaben jedes Eintrags stehen in einer {@link ReportingReportvorlageDefinition}.
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
	GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN(new ReportingReportvorlageDefinition("GostKlausurplanung-KlausurtermineMitKursen")
			.setUiTitel("Klausurplan der Kurse")
			.setUiBeschreibung("Einen Plan mit den Klausurterminen der Kurse erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE)
			.setPfadHtmlTemplate("gost/klausurplanung/GostKlausurplanungKlausurtermineMitKursen.html")
			.setDateiname("GOSt-Klausurplanung-Klausurtermine-Kurse")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
					BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVKlausurtermineMitKursen())),

	/** Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren */
	GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN(new ReportingReportvorlageDefinition("GostKlausurplanung-SchuelerMitKlausuren")
			.setUiTitel("Klausurplan der Schülerinnen und Schüler")
			.setUiBeschreibung("Einen Plan mit den Klausurterminen der Schülerinnen und Schüler erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER)
			.setPfadHtmlTemplate("gost/klausurplanung/GostKlausurplanungSchuelerMitKlausuren.html")
			.setDateiname("GOSt-Klausurplanung-Schueler-Klausuren")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
					BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKlausurplanungVSchuelerMitKlausuren())),

	/** Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler */
	GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN(new ReportingReportvorlageDefinition("GostKursplanung-KursMitKursschuelern")
			.setUiTitel("Kursliste")
			.setUiBeschreibung("Eine Liste mit den Schülerinnen und Schülern der Kurse aus der GOSt-Kursplaung erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE)
			.setPfadHtmlTemplate("gost/kursplanung/GostKursplanungKursMitKursschuelern.html")
			.setDateiname("GOSt-Blockungsergebnis-Kurs-Schueler")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVKursMitKursschuelern())),

	/** Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte */
	GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN(new ReportingReportvorlageDefinition("GostKursplanung-KurseMitStatistikwerten")
			.setUiTitel("Kursstatistik")
			.setUiBeschreibung("Eine Liste mit den Kursen aus der GOSt-Kursplanung und ihren Statistikwerten erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE)
			.setPfadHtmlTemplate("gost/kursplanung/GostKursplanungKurseMitStatistikwerten.html")
			.setDateiname("GOSt-Blockungsergebnis-Kurse-Statistikwerte")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVKurseMitStatistikwerten())),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN(new ReportingReportvorlageDefinition("GostKursplanung-SchuelerMitKursen")
			.setUiTitel("Kurszuordnungen der Schülerinnen und Schüler")
			.setUiBeschreibung("Eien Übersicht mit den einzelnen Kurszuorndungen der Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER)
			.setPfadHtmlTemplate("gost/kursplanung/GostKursplanungSchuelerMitKursen.html")
			.setDateiname("GOSt-Blockungsergebnis-Schueler-Kurse")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitKursen())),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN(new ReportingReportvorlageDefinition("GostKursplanung-SchuelerMitSchienenKursen")
			.setUiTitel("Kurs-Schienen-Zuordnungen der Schülerinnen und Schüler")
			.setUiBeschreibung("Eine Übersicht mit den einzelnen Kurszuordnungen und deeren Schienen für die Schülerinnen und Schüler aus der GOSt-Kursplanung erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER)
			.setPfadHtmlTemplate("gost/kursplanung/GostKursplanungSchuelerMitSchienenKursen.html")
			.setDateiname("GOSt-Blockungsergebnis-Schueler-Schienen-Kurse")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostKursplanungVSchuelerMitSchienenKursen())),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN(new ReportingReportvorlageDefinition("GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken")
			.setUiTitel("Fachwahlstatistiken")
			.setUiBeschreibung("Eine statische Übersicht der Fachwahlen eines Abiturjahrgangs aus der GOSt-Laufbahnplanung erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG)
			.setPfadHtmlTemplate("gost/laufbahnplanung/GostLaufbahnplanungAbiturjahrgangFachwahlstatistiken.html")
			.setDateiname("GOSt-Laufbahnplanung-Abiturjahrgang-Fachwahlstatistiken")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationGost.getGostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken())),

	/** Report-Vorlage: Klasse - Liste - Schüler - Fotos - Namen */
	KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN(new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Fotos-Namen")
			.setUiTitel("Fotoübersicht klassenweise")
			.setUiBeschreibung("Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Klassen erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KLASSEN))
			.setDatenContext(ReportingReportvorlageDatenContext.KLASSEN)
			.setPfadHtmlTemplate("klassen/KlasseListeSchuelerFotosNamen.html")
			.setDateiname("Klasse-Liste-Schueler-Fotos-Namen")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerFotosNamen())),

	/** Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher */
	KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER(new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Kontaktdaten-Erzieher")
			.setUiTitel("Klassenliste mit Kontaktdaten")
			.setUiBeschreibung("Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Klassen erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KLASSEN))
			.setDatenContext(ReportingReportvorlageDatenContext.KLASSEN)
			.setPfadHtmlTemplate("klassen/KlasseListeSchuelerKontaktdatenErzieher.html")
			.setDateiname("Klasse-Liste-Schueler-Kontaktdaten-Erzieher")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerKontaktdatenerzieher())),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN(new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Leistungsdaten")
			.setUiTitel("Leistungsübersicht klassenweise")
			.setUiBeschreibung("Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KLASSEN))
			.setDatenContext(ReportingReportvorlageDatenContext.KLASSEN)
			.setPfadHtmlTemplate("klassen/leistungsdaten/KlasseListeSchuelerLeistungsdaten.html")
			.setDateiname("Klassen-Liste-Schueler-Leistungsdaten")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN))
			.setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdaten())),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten - Detailliert */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT(new ReportingReportvorlageDefinition("Klasse-Liste-Schueler-Leistungsdaten-Detailliert")
			.setUiTitel("Leistungsübersicht klassenweise (detailliert)")
			.setUiBeschreibung("Eine detaillierte Übersicht der Leistungsdaten der Schülerinnen und Schüler der Klassen erzeugen.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KLASSEN))
			.setDatenContext(ReportingReportvorlageDatenContext.KLASSEN)
			.setPfadHtmlTemplate("klassen/leistungsdaten/KlasseListeSchuelerLeistungsdatenDetailliert.html")
			.setDateiname("Klassen-Liste-Schueler-Leistungsdaten-Detailliert")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN))
			.setReportingParameter(ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerLeistungsdatenDetailliert())),

	/** Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher */
	KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER(new ReportingReportvorlageDefinition("Kurs-Liste-Schueler-Kontaktdaten-Erzieher")
			.setUiTitel("Kursliste mit Kontaktdaten")
			.setUiBeschreibung("Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler der Kurse erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KURSE))
			.setDatenContext(ReportingReportvorlageDatenContext.KURSE)
			.setPfadHtmlTemplate("kurse/KursListeSchuelerKontaktdatenErzieher.html")
			.setDateiname("Kurs-Liste-Schueler-Kontaktdaten-Erzieher")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerKontaktdatenerzieher())),

	/** Report-Vorlage: Kurs - Liste - Schüler - Fotos - Namen */
	KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN(new ReportingReportvorlageDefinition("Kurs-Liste-Schueler-Fotos-Namen")
			.setUiTitel("Fotoübersicht kursweise")
			.setUiBeschreibung("Eine Übersicht mit den Fotos der Schülerinnen und Schüler der Kurse erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KURSE))
			.setDatenContext(ReportingReportvorlageDatenContext.KURSE)
			.setPfadHtmlTemplate("kurse/KursListeSchuelerFotosNamen.html")
			.setDateiname("Kurs-Liste-Schueler-Fotos-Namen")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerFotosNamen())),

	/** Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten */
	KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN(new ReportingReportvorlageDefinition("Kurs-Liste-Schueler-Leistungsdaten")
			.setUiTitel("Leistungsübersicht kursweise")
			.setUiBeschreibung("Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der Kurse erzeugen.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KURSE))
			.setDatenContext(ReportingReportvorlageDatenContext.KURSE)
			.setPfadHtmlTemplate("kurse/leistungsdaten/KursListeSchuelerLeistungsdaten.html")
			.setDateiname("Kurs-Liste-Schueler-Leistungsdaten")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN))
			.setReportingParameter(ReportingReportvorlageKonfigurationKurse.getKurseVListeSchuelerLeistungsdaten())),

	/** Report-Vorlage: Lehrer - Liste - Kontaktdaten */
	LEHRER_V_LISTE_KONTAKTDATEN(new ReportingReportvorlageDefinition("Lehrer-Liste-Kontaktdaten")
			.setUiTitel("Lehrerliste mit Kontaktdaten")
			.setUiBeschreibung("Eine Liste mit den Kontaktdaten der ausgewählten Lehrkräfte erzeugen.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.LEHRER))
			.setDatenContext(ReportingReportvorlageDatenContext.LEHRER)
			.setPfadHtmlTemplate("lehrer/listen/LehrerListeKontaktdaten.html")
			.setDateiname("Lehrer-Liste-Kontaktdaten")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVListeKontaktdaten())),

	/** Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten */
	LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN(new ReportingReportvorlageDefinition("Lehrer-Liste-Schueler-Leistungsdaten")
			.setUiTitel("Leistungsdaten der Lerngruppen")
			.setUiBeschreibung("Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Lehrkräfte nach Lerngruppen erzeugen")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.LEHRER))
			.setDatenContext(ReportingReportvorlageDatenContext.LEHRER)
			.setPfadHtmlTemplate("lehrer/leistungsdaten/LehrerListeSchuelerLeistungsdaten.html")
			.setDateiname("Lehrer-Liste-Schueler-Leistungsdaten")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN))
			.setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVListeSchuelerLeistungsdaten())),

	/** Report-Vorlage: Lehrer - Zugangsdaten für das externe Notenmodul */
	LEHRER_V_NOTENMODUL_ANSCHREIBEN_ZUGANGSDATEN(new ReportingReportvorlageDefinition("Lehrer-Notenmodul-Anschreiben-Zugangsdaten")
			.setUiTitel("Zugangsdaten für den WebNotenManager")
			.setUiBeschreibung("Ein Anschreiben mit dem Initialkennwort für den WebNotenManager je Lehrkraft erzeugen.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.LEHRER))
			.setDatenContext(ReportingReportvorlageDatenContext.LEHRER)
			.setPfadHtmlTemplate("lehrer/notenmodul/LehrerNotenmodulAnschreibenZugangsdaten.html")
			.setDateiname("Lehrer-Notenmodul-Anschreiben-Zugangsdaten")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.NOTENMODUL_ADMINISTRATION))
			.setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVNotenmodulAnschreibenZugangsdaten())),

	/** Report-Vorlage: Lehrer - Stammdaten - Liste */
	LEHRER_V_STAMMDATENLISTE(new ReportingReportvorlageDefinition("Lehrer-Stammdatenliste")
			.setUiTitel("Stammdatenliste der Lehrkräfte")
			.setUiBeschreibung("Stammdatenliste der Lehrkräfte erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.LEHRER)
			.setPfadHtmlTemplate("lehrer/stammdaten/LehrerStammdatenliste.html")
			.setDateiname("Lehrer-Stammdatenliste")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationLehrer.getLehrerVStammdatenliste())),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A4 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4(new ReportingReportvorlageDefinition("Schueler-GostAbiturApoAnlage12-A4")
			.setUiTitel("APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A4)")
			.setUiBeschreibung("Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12")
			.setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR)
			.setPfadHtmlTemplate("schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A4.html")
			.setDateiname("APO-GOSt-Anlage12")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A4())),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A3 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3(new ReportingReportvorlageDefinition("Schueler-GostAbiturApoAnlage12-A3")
			.setUiTitel("APO-GOSt - Anlage 12 - Abiturzeugnis (DIN-A3)")
			.setUiBeschreibung("Erzeugt das Abiturzeugnis des Schülerinnen und Schüler gemäß APO-GOSt Anlage 12")
			.setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR)
			.setPfadHtmlTemplate("schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A3.html")
			.setDateiname("APO-GOSt-Anlage12")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostAbiturApoAnlage12A3())),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT(new ReportingReportvorlageDefinition("Schueler-GostLaufbahnplanungErgebnisuebersicht")
			.setUiTitel("Ergebnisübersicht der GOSt-Laufbahnplanung")
			.setUiBeschreibung("Ergebnisübersicht der GOSt-Laufbahnplanung nach Schülerinnen und Schüler für Beratungslehrkräfte erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG)
			.setPfadHtmlTemplate("schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungErgebnisuebersicht.html")
			.setDateiname("GOSt-Laufbahnplanung-Pruefungsergebnisse")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungErgebnisuebersicht())),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN(new ReportingReportvorlageDefinition("Schueler-GostLaufbahnplanungWahlbogen")
			.setUiTitel("GOST-Laufbahnwahlbogen")
			.setUiBeschreibung("Die GOST-Laufbahnwahlbögen für Schülerinnen und Schüler erzeugen oder versenden.")
			.setDatenContext(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG)
			.setPfadHtmlTemplate("schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungWahlbogen.html")
			.setDateiname("GOSt-Laufbahnplanung-Wahlboegen")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GOST)
			.setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGostLaufbahnplanungWahlbogen())),

	/** Report-Vorlage: Schüler - Grundschulzeugnis - Klasse 4 */
	SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_4(new ReportingReportvorlageDefinition("Schueler-GrundschulzeugnisKlasse4")
			.setUiTitel("Grundschulzeugnis Klasse 4")
			.setUiBeschreibung("Ein Zeugnis der Grundschule für die Klasse 4 erstellen.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.SCHUELER, ReportingReportvorlageUiNutzung.KLASSEN))
			.setDatenContext(ReportingReportvorlageDatenContext.SCHUELER)
			.setPfadHtmlTemplate("schueler/aogs/SchuelerGrundschulzeugnisKlasse4.html")
			.setDateiname("Grundschulzeugnis-Klasse4")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			.setSchulformen(ReportingReportvorlageSchulformen.GRUNDSCHULE)
			.setErforderlicherServerMode(ServerMode.ALPHA)
			.setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVGrundschulzeugnisKlasse4())),

	/** Report-Vorlage: Schüler - Schulbescheinigung */
	SCHUELER_V_SCHULBESCHEINIGUNG(new ReportingReportvorlageDefinition("Schueler-Schulbescheinigung")
			.setUiTitel("Schulbescheinigung")
			.setUiBeschreibung("Eine Schulbescheinigung für Schülerinnen und Schüler oder deren Erziehungsberechtigte erzeugen.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.SCHUELER))
			.setDatenContext(ReportingReportvorlageDatenContext.SCHUELER)
			.setPfadHtmlTemplate("schueler/anschreiben/SchuelerSchulbescheinigung.html")
			.setDateiname("Schueler-Schulbescheinigung")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVSchulbescheinigung())),

	/** Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher */
	SCHUELER_V_LISTE_KONTAKTDATENERZIEHER(new ReportingReportvorlageDefinition("Schueler-Liste-Kontaktdaten-Erzieher")
			.setUiTitel("Schülerliste mit Kontaktdaten")
			.setUiBeschreibung("Eine Liste mit den Kontaktdaten der Schülerinnen und Schüler erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.SCHUELER))
			.setDatenContext(ReportingReportvorlageDatenContext.SCHUELER)
			.setPfadHtmlTemplate("schueler/listen/SchuelerListeKontaktdatenErzieher.html")
			.setDateiname("Schueler-Liste-Kontaktdaten-Erzieher")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationSchueler.getSchuelerVListeKontaktdatenerzieher())),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_FACH_STUNDENPLAN(new ReportingReportvorlageDefinition("Stundenplanung-FachStundenplan")
			.setUiTitel("Fach-Stundenplan")
			.setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Fächer erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.FAECHER))
			.setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH)
			.setPfadHtmlTemplate("stundenplanung/StundenplanungFachStundenplan.html")
			.setDateiname("Fach-Stundenplan")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVFachStundenplan())),

	/** Report-Vorlage: Stundenplanung - Klasse - Stundenplan */
	STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN(new ReportingReportvorlageDefinition("Stundenplanung-KlassenStundenplan")
			.setUiTitel("Klassen-Stundenplan")
			.setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Klassen erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.KLASSEN))
			.setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN)
			.setPfadHtmlTemplate("stundenplanung/StundenplanungKlassenStundenplan.html")
			.setDateiname("Klassen-Stundenplan")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVKlassenStundenplan())),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN(new ReportingReportvorlageDefinition("Stundenplanung-LehrerStundenplan")
			.setUiTitel("Lehrer-Stundenplan")
			.setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.LEHRER))
			.setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER)
			.setPfadHtmlTemplate("stundenplanung/StundenplanungLehrerStundenplan.html")
			.setDateiname("Lehrer-Stundenplan")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplan())),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT(new ReportingReportvorlageDefinition("Stundenplanung-LehrerStundenplanKombiniert")
			.setUiTitel("Lehrer-Stundenplan kombiniert")
			.setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Lehrkräfte in einer kombinierten Ansicht erzeugen")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.LEHRER))
			.setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER)
			.setPfadHtmlTemplate("stundenplanung/StundenplanungLehrerStundenplanKombiniert.html")
			.setDateiname("Lehrer-Stundenplan-Kombiniert")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVLehrerStundenplanKombiniert())),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_RAUM_STUNDENPLAN(new ReportingReportvorlageDefinition("Stundenplanung-RaumStundenplan")
			.setUiTitel("Raum-Stundenplan")
			.setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Räume erzeugen.")
			.setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM)
			.setPfadHtmlTemplate("stundenplanung/StundenplanungRaumStundenplan.html")
			.setDateiname("Raum-Stundenplan")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVRaumStundenplan())),

	/** Report-Vorlage: Stundenplanung - Schüler - Stundenplan */
	STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN(new ReportingReportvorlageDefinition("Stundenplanung-SchuelerStundenplan")
			.setUiTitel("Schüler-Stundenplan")
			.setUiBeschreibung("Den ausgewählten Stundenplan für die ausgewählten Schülerinnen und Schüler erzeugen oder versenden.")
			.setUiNutzungen(List.of(ReportingReportvorlageUiNutzung.SCHUELER))
			.setDatenContext(ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER)
			.setPfadHtmlTemplate("stundenplanung/StundenplanungSchuelerStundenplan.html")
			.setDateiname("Schueler-Stundenplan")
			.setBenutzerKompetenzen(List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN))
			.setReportingParameter(ReportingReportvorlageKonfigurationStundenplanung.getStundenplanungVSchuelerStundenplan()));


	/** Die Bezeichnung der Report-Vorlage */
	private final @NotNull String bezeichnung;

	/** Der Titel, der in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Titel. */
	private final @NotNull String uiTitel;

	/** Die Beschreibung, die in der UI zu diesem Report-Vorlagen-Objekt angezeigt wird, z. B. als Card- oder Gruppen-Beschreibung. */
	private final @NotNull String uiBeschreibung;

	/** Die Bereiche im Client, in denen die Report-Vorlage angeboten wird. */
	private final @NotNull List<ReportingReportvorlageUiNutzung> uiNutzungen;

	/** Der Daten-Context, der für die HTML-Template-Datei verwendet wird. */
	private final @NotNull ReportingReportvorlageDatenContext datenContext;

	/** Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad. */
	private final @NotNull String pfadHtmlTemplate;

	/** Der statische Dateiname ohne Dateiendung, der bei der Ausgabe als ZIP-Datei verwendet wird. */
	private final @NotNull String dateiname;

	/** Die Liste mit Benutzerkompetenzen (als OR-Verknüpfung) gemäß {@link BenutzerKompetenz}, die zur Nutzung des Templates erforderlich sind. */
	private final @NotNull List<BenutzerKompetenz> benutzerKompetenzen;

	/** Die Schulformen, an denen die Report-Vorlage genutzt werden darf. Eine leere Liste gilt für alle Schulformen. */
	private final @NotNull List<Schulform> schulformen;

	/** Der mindestens erforderliche ServerMode, damit die Report-Vorlage genutzt werden darf. STABLE lässt sie in allen Modi zu. */
	private final @NotNull ServerMode erforderlicherServerMode;

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
	 * Übernimmt die Angaben aus der Definition. Fehlt eine Pflichtangabe, schlägt schon das Laden des Enums fehl.
	 *
	 * @param definition   die Definition mit den Angaben zur Report-Vorlage
	 */
	ReportingReportvorlage(final @NotNull ReportingReportvorlageDefinition definition) {
		this.bezeichnung = definition.getBezeichnung();
		this.uiTitel = definition.getUiTitel();
		this.uiBeschreibung = definition.getUiBeschreibung();
		this.uiNutzungen = definition.getUiNutzungen();
		this.datenContext = definition.getDatenContext();
		this.pfadHtmlTemplate = definition.getPfadHtmlTemplate();
		this.dateiname = definition.getDateiname();
		this.benutzerKompetenzen = definition.getBenutzerKompetenzen();
		this.schulformen = definition.getSchulformen();
		this.erforderlicherServerMode = definition.getErforderlicherServerMode();
		this.reportingParameter = definition.getReportingParameter();
		this.reportingParameter.reportvorlage = this.bezeichnung;
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
	 * Gibt die Bereiche im Client zurück, in denen diese Report-Vorlage angeboten wird.
	 *
	 * @return die Bereiche im Client, ggf. leer
	 */
	public @NotNull List<ReportingReportvorlageUiNutzung> getUiNutzungen() {
		return this.uiNutzungen;
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
	 * Gibt die Schulformen zurück, an denen diese Report-Vorlage genutzt werden darf.
	 * Ist die Liste leer, so gilt die Vorlage für alle Schulformen.
	 *
	 * @return Die Liste der Schulformen
	 */
	public @NotNull List<Schulform> getSchulformen() {
		return this.schulformen;
	}

	/**
	 * Gibt an, ob diese Report-Vorlage an der übergebenen Schulform genutzt werden darf. Eine leere Liste an der Vorlage lässt jede Schulform zu.
	 * Nennt die Vorlage Schulformen, muss die übergebene darunter sein; eine nicht übergebene Schulform gilt dann als unzulässig.
	 *
	 * @param schulform Die zu prüfende Schulform. {@code null} ist zulässig.
	 *
	 * @return true, wenn die Vorlage an dieser Schulform genutzt werden darf; sonst false.
	 */
	public boolean giltFuerSchulform(final Schulform schulform) {
		return this.schulformen.isEmpty() || ((schulform != null) && this.schulformen.contains(schulform));
	}

	/**
	 * Gibt den mindestens erforderlichen ServerMode zurück, damit diese Report-Vorlage genutzt werden darf.
	 *
	 * @return Der erforderliche ServerMode. STABLE lässt die Vorlage in allen Modi zu.
	 */
	public @NotNull ServerMode getErforderlicherServerMode() {
		return this.erforderlicherServerMode;
	}

	/**
	 * Gibt an, ob diese Report-Vorlage in dem übergebenen ServerMode genutzt werden darf. Der Client blendet die Vorlage sonst aus, der Server lehnt sie ab.
	 *
	 * @param serverMode Der ServerMode, in dem der Server betrieben wird.
	 *
	 * @return true, wenn der ServerMode für diese Vorlage ausreicht; sonst false.
	 */
	public boolean giltFuerServerMode(final @NotNull ServerMode serverMode) {
		return this.erforderlicherServerMode.checkServerMode(serverMode);
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
	 * Ermittelt alle Report-Vorlagen, die der Client in dem übergebenen Bereich anbietet. Die Liste ist nach dem Titel sortiert, weil Anwender eine Card
	 * über ihren Titel suchen.
	 *
	 * @param uiNutzung   der Bereich im Client
	 *
	 * @return die Report-Vorlagen mit dieser Nutzung, ggf. leer
	 */
	public static @NotNull List<ReportingReportvorlage> getByUiNutzung(final @NotNull ReportingReportvorlageUiNutzung uiNutzung) {
		final @NotNull List<ReportingReportvorlage> ergebnis = new ArrayList<>();
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values()) {
			if (rv.uiNutzungen.contains(uiNutzung)) {
				ergebnis.add(rv);
			}
		}
		ergebnis.sort((final @NotNull ReportingReportvorlage a, final @NotNull ReportingReportvorlage b) -> a.uiTitel.compareToIgnoreCase(b.uiTitel));
		return ergebnis;
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
