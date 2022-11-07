import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BenutzerKompetenzKatalogEintrag, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerKompetenzKatalogEintrag } from '../../../core/data/benutzer/BenutzerKompetenzKatalogEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { BenutzerKompetenzGruppe, cast_de_nrw_schule_svws_core_types_benutzer_BenutzerKompetenzGruppe } from '../../../core/types/benutzer/BenutzerKompetenzGruppe';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class BenutzerKompetenz extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BenutzerKompetenz> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, BenutzerKompetenz> = new Map<String, BenutzerKompetenz>();

	public static readonly KEINE : BenutzerKompetenz = new BenutzerKompetenz("KEINE", 0, new BenutzerKompetenzKatalogEintrag(-2, BenutzerKompetenzGruppe.KEINE, "keine"));

	public static readonly ADMIN : BenutzerKompetenz = new BenutzerKompetenz("ADMIN", 1, new BenutzerKompetenzKatalogEintrag(-1, BenutzerKompetenzGruppe.ADMIN, "admin"));

	public static readonly SCHUELER_INDIVIDUALDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_ANSEHEN", 2, new BenutzerKompetenzKatalogEintrag(11, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ansehen"));

	public static readonly SCHUELER_INDIVIDUALDATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_AENDERN", 3, new BenutzerKompetenzKatalogEintrag(12, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ändern"));

	public static readonly SCHUELER_INDIVIDUALDATEN_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_LOESCHEN", 4, new BenutzerKompetenzKatalogEintrag(13, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Löschen"));

	public static readonly SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN", 5, new BenutzerKompetenzKatalogEintrag(14, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Vermerke ändern"));

	public static readonly SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN", 6, new BenutzerKompetenzKatalogEintrag(15, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "KAoA-Daten ändern"));

	public static readonly SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN", 7, new BenutzerKompetenzKatalogEintrag(16, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Einwilligungen ändern (DSGVO, Lernplattformen)"));

	public static readonly SCHUELER_LEISTUNGSDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_LEISTUNGSDATEN_ANSEHEN", 8, new BenutzerKompetenzKatalogEintrag(21, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Ansehen"));

	public static readonly SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN", 9, new BenutzerKompetenzKatalogEintrag(22, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Funktionsbezogen ändern"));

	public static readonly SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN", 10, new BenutzerKompetenzKatalogEintrag(23, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Alle ändern"));

	public static readonly BERICHTE_ALLE_FORMULARE_DRUCKEN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_ALLE_FORMULARE_DRUCKEN", 11, new BenutzerKompetenzKatalogEintrag(31, BenutzerKompetenzGruppe.BERICHTE, "Alle Formulare drucken"));

	public static readonly BERICHTE_STANDARDFORMULARE_DRUCKEN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_STANDARDFORMULARE_DRUCKEN", 12, new BenutzerKompetenzKatalogEintrag(32, BenutzerKompetenzGruppe.BERICHTE, "Standard-Formulare drucken"));

	public static readonly BERICHTE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_AENDERN", 13, new BenutzerKompetenzKatalogEintrag(33, BenutzerKompetenzGruppe.BERICHTE, "Ändern"));

	public static readonly BERICHTE_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_LOESCHEN", 14, new BenutzerKompetenzKatalogEintrag(34, BenutzerKompetenzGruppe.BERICHTE, "Löschen"));

	public static readonly IMPORT_EXPORT_DATEN_IMPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_DATEN_IMPORTIEREN", 15, new BenutzerKompetenzKatalogEintrag(41, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Daten importieren"));

	public static readonly IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN", 16, new BenutzerKompetenzKatalogEintrag(42, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schülerdaten exportieren"));

	public static readonly IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN", 17, new BenutzerKompetenzKatalogEintrag(43, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Lehrerdaten exportieren"));

	public static readonly IMPORT_EXPORT_SCHNITTSTELLE_SCHILD_NRW : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_SCHNITTSTELLE_SCHILD_NRW", 18, new BenutzerKompetenzKatalogEintrag(44, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schnittstelle SchILD-NRW verwenden"));

	public static readonly IMPORT_EXPORT_ACCESS_DB : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_ACCESS_DB", 19, new BenutzerKompetenzKatalogEintrag(45, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Access-DB-Export durchführen"));

	public static readonly IMPORT_EXPORT_XML : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_XML", 20, new BenutzerKompetenzKatalogEintrag(46, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Export über XML-Schnittstellen"));

	public static readonly SCHULBEZOGENE_DATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHULBEZOGENE_DATEN_ANSEHEN", 21, new BenutzerKompetenzKatalogEintrag(61, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ansehen"));

	public static readonly SCHULBEZOGENE_DATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHULBEZOGENE_DATEN_AENDERN", 22, new BenutzerKompetenzKatalogEintrag(62, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ändern"));

	public static readonly EXTRAS_BACKUP_DURCHFUEHREN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_BACKUP_DURCHFUEHREN", 23, new BenutzerKompetenzKatalogEintrag(71, BenutzerKompetenzGruppe.EXTRAS, "Backup durchführen"));

	public static readonly EXTRAS_GELOESCHTE_DATEN_ZURUECKHOLEN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_GELOESCHTE_DATEN_ZURUECKHOLEN", 24, new BenutzerKompetenzKatalogEintrag(72, BenutzerKompetenzGruppe.EXTRAS, "Gelöschte Daten zurückholen"));

	public static readonly EXTRAS_FARBEN_FUER_FACHGRUPPEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_FARBEN_FUER_FACHGRUPPEN_AENDERN", 25, new BenutzerKompetenzKatalogEintrag(73, BenutzerKompetenzGruppe.EXTRAS, "Farben für Fachgruppen ändern"));

	public static readonly EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN", 26, new BenutzerKompetenzKatalogEintrag(74, BenutzerKompetenzGruppe.EXTRAS, "Daten aus Kurs42 importieren"));

	public static readonly EXTRAS_DATEN_PERSONENGRUPPEN_BEARBEITEN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_DATEN_PERSONENGRUPPEN_BEARBEITEN", 27, new BenutzerKompetenzKatalogEintrag(75, BenutzerKompetenzGruppe.EXTRAS, "Personengruppen bearbeiten"));

	public static readonly KATALOG_EINTRAEGE_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("KATALOG_EINTRAEGE_ANSEHEN", 28, new BenutzerKompetenzKatalogEintrag(81, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ansehen"));

	public static readonly KATALOG_EINTRAEGE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("KATALOG_EINTRAEGE_AENDERN", 29, new BenutzerKompetenzKatalogEintrag(82, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ändern"));

	public static readonly KATALOG_EINTRAEGE_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("KATALOG_EINTRAEGE_LOESCHEN", 30, new BenutzerKompetenzKatalogEintrag(83, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Löschen"));

	public static readonly LEHRERDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_ANSEHEN", 31, new BenutzerKompetenzKatalogEintrag(91, BenutzerKompetenzGruppe.LEHRERDATEN, "Ansehen"));

	public static readonly LEHRERDATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_AENDERN", 32, new BenutzerKompetenzKatalogEintrag(92, BenutzerKompetenzGruppe.LEHRERDATEN, "Ändern"));

	public static readonly LEHRERDATEN_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_LOESCHEN", 33, new BenutzerKompetenzKatalogEintrag(93, BenutzerKompetenzGruppe.LEHRERDATEN, "Löschen"));

	public static readonly LEHRERDATEN_DETAILDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_DETAILDATEN_ANSEHEN", 34, new BenutzerKompetenzKatalogEintrag(94, BenutzerKompetenzGruppe.LEHRERDATEN, "Detaildaten ansehen"));

	public static readonly LEHRERDATEN_DETAILDATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_DETAILDATEN_AENDERN", 35, new BenutzerKompetenzKatalogEintrag(95, BenutzerKompetenzGruppe.LEHRERDATEN, "Detaildaten ändern"));

	public static readonly SCHULPFLICHTVERLETZUNG_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHULPFLICHTVERLETZUNG_ANSEHEN", 36, new BenutzerKompetenzKatalogEintrag(101, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ansehen"));

	public static readonly SCHULPFLICHTVERLETZUNG_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHULPFLICHTVERLETZUNG_AENDERN", 37, new BenutzerKompetenzKatalogEintrag(102, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ändern"));

	public static readonly SCHULPFLICHTVERLETZUNG_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHULPFLICHTVERLETZUNG_LOESCHEN", 38, new BenutzerKompetenzKatalogEintrag(103, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Löschen"));

	public static readonly STUNDENPLAN_ALLGEMEIN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_ALLGEMEIN_ANSEHEN", 39, new BenutzerKompetenzKatalogEintrag(111, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenplan ansehen (allgemein)"));

	public static readonly STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN", 40, new BenutzerKompetenzKatalogEintrag(112, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundneplan ansehen (funktionsbezogen)"));

	public static readonly STUNDENPLAN_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_IMPORT", 41, new BenutzerKompetenzKatalogEintrag(113, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne importieren"));

	public static readonly STUNDENPLAN_EXPORT : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_EXPORT", 42, new BenutzerKompetenzKatalogEintrag(114, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne exportieren"));

	public static readonly STUNDENPLAN_ERSTELLEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_ERSTELLEN", 43, new BenutzerKompetenzKatalogEintrag(115, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne erstellen"));

	public static readonly STUNDENPLAN_AKTIVIEREN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_AKTIVIEREN", 44, new BenutzerKompetenzKatalogEintrag(116, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne aktivieren"));

	public static readonly NOTENMODUL_ADMINISTRATION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_ADMINISTRATION", 45, new BenutzerKompetenzKatalogEintrag(131, BenutzerKompetenzGruppe.NOTENMODUL, "Administration Notenmodul"));

	public static readonly NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN", 46, new BenutzerKompetenzKatalogEintrag(132, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (allgemein)"));

	public static readonly NOTENMODUL_NOTEN_AENDERN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_AENDERN_FUNKTION", 47, new BenutzerKompetenzKatalogEintrag(133, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (funktionsbezogen)"));

	public static readonly NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN", 48, new BenutzerKompetenzKatalogEintrag(134, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (allgemein)"));

	public static readonly NOTENMODUL_NOTEN_ANSEHEN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_ANSEHEN_FUNKTION", 49, new BenutzerKompetenzKatalogEintrag(135, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (funktionsbezogen)"));

	public static readonly DATENBANK_SCHEMA_ERSTELLEN : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SCHEMA_ERSTELLEN", 50, new BenutzerKompetenzKatalogEintrag(141, BenutzerKompetenzGruppe.DATENBANK, "Schema erstellen und migrieren"));

	public static readonly DATENBANK_SQLITE_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SQLITE_IMPORT", 51, new BenutzerKompetenzKatalogEintrag(142, BenutzerKompetenzGruppe.DATENBANK, "SQLite importieren (Backup einspielen)"));

	public static readonly DATENBANK_SQLITE_EXPORT : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SQLITE_EXPORT", 52, new BenutzerKompetenzKatalogEintrag(143, BenutzerKompetenzGruppe.DATENBANK, "SQLite exportieren (Backup erstellen)"));

	public static readonly OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN", 53, new BenutzerKompetenzKatalogEintrag(161, BenutzerKompetenzGruppe.OBERSTUFE_LAUFBAHNPLANUNG, "Laufbahnplanung (allgemein)"));

	public static readonly OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN", 54, new BenutzerKompetenzKatalogEintrag(162, BenutzerKompetenzGruppe.OBERSTUFE_LAUFBAHNPLANUNG, "Laufbahnplanung (funktionsbezogen)"));

	public static readonly OBERSTUFE_LUPO_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LUPO_IMPORT", 55, new BenutzerKompetenzKatalogEintrag(163, BenutzerKompetenzGruppe.OBERSTUFE_LAUFBAHNPLANUNG, "Laufbahnplanung aus LuPO importieren"));

	public static readonly OBERSTUFE_KURSPLANUNG_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_ALLGEMEIN", 56, new BenutzerKompetenzKatalogEintrag(171, BenutzerKompetenzGruppe.OBERSTUFE_KURSPLANUNG, "Kursverwaltung - Blocken (allgemein)"));

	public static readonly OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN", 57, new BenutzerKompetenzKatalogEintrag(172, BenutzerKompetenzGruppe.OBERSTUFE_KURSPLANUNG, "Kursverwaltung - Blocken (funktionsbezogen)"));

	public static readonly OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN", 58, new BenutzerKompetenzKatalogEintrag(173, BenutzerKompetenzGruppe.OBERSTUFE_KURSPLANUNG, "Kursverwaltung - Blockung aktivieren"));

	public static readonly OBERSTUFE_KLAUSURPLANUNG_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_AENDERN", 59, new BenutzerKompetenzKatalogEintrag(181, BenutzerKompetenzGruppe.OBERSTUFE_KLAUSURPLANUNG, "Klausurplanung ändern"));

	public static readonly OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN", 60, new BenutzerKompetenzKatalogEintrag(182, BenutzerKompetenzGruppe.OBERSTUFE_KLAUSURPLANUNG, "Klausurplanung ansehen (allgemein)"));

	public static readonly OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION", 61, new BenutzerKompetenzKatalogEintrag(183, BenutzerKompetenzGruppe.OBERSTUFE_KLAUSURPLANUNG, "Klausurplanung ansehen (funktionsbezogen)"));

	public static readonly ABITUR_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_ANSEHEN_ALLGEMEIN", 62, new BenutzerKompetenzKatalogEintrag(191, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ansehen (allgemein)"));

	public static readonly ABITUR_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_ANSEHEN_FUNKTIONSBEZOGEN", 63, new BenutzerKompetenzKatalogEintrag(192, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ansehen (funktionsbezogen)"));

	public static readonly ABITUR_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_AENDERN_ALLGEMEIN", 64, new BenutzerKompetenzKatalogEintrag(193, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ändern (allgemein)"));

	public static readonly ABITUR_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_AENDERN_FUNKTIONSBEZOGEN", 65, new BenutzerKompetenzKatalogEintrag(194, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ändern (funktionsbezogen)"));

	public static readonly ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN", 66, new BenutzerKompetenzKatalogEintrag(195, BenutzerKompetenzGruppe.ABITUR, "Prüfungsergebnisse eingeben (allgemein)"));

	public static readonly ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 67, new BenutzerKompetenzKatalogEintrag(196, BenutzerKompetenzGruppe.ABITUR, "Prüfungsergebnisse eingeben (funktionsbezogen)"));

	public static readonly ADRESSDATEN_ERZIEHER_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("ADRESSDATEN_ERZIEHER_ANSEHEN", 68, new BenutzerKompetenzKatalogEintrag(201, BenutzerKompetenzGruppe.CARDDAV, "Ansehen"));

	public static readonly ADRESSDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("ADRESSDATEN_ANSEHEN", 69, new BenutzerKompetenzKatalogEintrag(202, BenutzerKompetenzGruppe.CARDDAV, "Ansehen"));

	public static readonly KALENDER_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("KALENDER_ANSEHEN", 70, new BenutzerKompetenzKatalogEintrag(301, BenutzerKompetenzGruppe.CALDAV, "Ansehen"));

	public static readonly EIGENEN_KALENDER_BEARBEITEN : BenutzerKompetenz = new BenutzerKompetenz("EIGENEN_KALENDER_BEARBEITEN", 71, new BenutzerKompetenzKatalogEintrag(302, BenutzerKompetenzGruppe.CALDAV, "Bearbeiten"));

	public static readonly KALENDER_FUNKTIONSBEZOGEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("KALENDER_FUNKTIONSBEZOGEN_ANSEHEN", 72, new BenutzerKompetenzKatalogEintrag(303, BenutzerKompetenzGruppe.CALDAV, "Ansehen"));

	public static readonly ABSCHLUSS_SEKI_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_ANSEHEN_ALLGEMEIN", 73, new BenutzerKompetenzKatalogEintrag(401, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (allgemein)"));

	public static readonly ABSCHLUSS_SEKI_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_ANSEHEN_FUNKTIONSBEZOGEN", 74, new BenutzerKompetenzKatalogEintrag(402, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (funktionsbezogen)"));

	public static readonly ABSCHLUSS_SEKI_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_AENDERN_ALLGEMEIN", 75, new BenutzerKompetenzKatalogEintrag(403, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (allgemein)"));

	public static readonly ABSCHLUSS_SEKI_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_AENDERN_FUNKTIONSBEZOGEN", 76, new BenutzerKompetenzKatalogEintrag(404, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (funktionsbezogen)"));

	public static readonly ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_ALLGEMEIN", 77, new BenutzerKompetenzKatalogEintrag(405, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (allgemein)"));

	public static readonly ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 78, new BenutzerKompetenzKatalogEintrag(406, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (funktionsbezogen)"));

	public static readonly ABSCHLUSS_BK_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_ANSEHEN_ALLGEMEIN", 79, new BenutzerKompetenzKatalogEintrag(501, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (allgemein)"));

	public static readonly ABSCHLUSS_BK_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_ANSEHEN_FUNKTIONSBEZOGEN", 80, new BenutzerKompetenzKatalogEintrag(502, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (funktionsbezogen)"));

	public static readonly ABSCHLUSS_BK_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_AENDERN_ALLGEMEIN", 81, new BenutzerKompetenzKatalogEintrag(503, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (allgemein)"));

	public static readonly ABSCHLUSS_BK_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_AENDERN_FUNKTIONSBEZOGEN", 82, new BenutzerKompetenzKatalogEintrag(504, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (funktionsbezogen)"));

	public static readonly ABSCHLUSS_BK_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_EINGABE_ERGEBNISSE_ALLGEMEIN", 83, new BenutzerKompetenzKatalogEintrag(505, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (allgemein)"));

	public static readonly ABSCHLUSS_BK_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 84, new BenutzerKompetenzKatalogEintrag(506, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (funktionsbezogen)"));

	public static VERSION : number = 1;

	public readonly daten : BenutzerKompetenzKatalogEintrag;

	private static readonly _mapID : HashMap<Number, BenutzerKompetenz> = new HashMap();

	private static readonly _mapGruppenZuordnung : HashMap<BenutzerKompetenzGruppe, List<BenutzerKompetenz>> = new HashMap();

	/**
	 * Erzeugt eine neue Benutzerkompetenz für die Aufzählung.
	 *
	 * @param id                  die ID der Benutzerkompetenz
	 * @param bezeichnungGruppe   die Bezeichnung der Gruppe von Kompetenzen zu dieser diese Benutzerkompetenz gehört
	 * @param bezeichnung         die Bezeichnung der Benutzerkompetenz
	 */
	private constructor(name : string, ordinal : number, daten : BenutzerKompetenzKatalogEintrag) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BenutzerKompetenz.all_values_by_ordinal.push(this);
		BenutzerKompetenz.all_values_by_name.set(name, this);
		this.daten = daten;
	}

	/**
	 * Gibt eine Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 */
	private static getMapID() : HashMap<Number, BenutzerKompetenz> {
		if (BenutzerKompetenz._mapID.size() === 0) 
			for (let p of BenutzerKompetenz.values()) 
				BenutzerKompetenz._mapID.put(p.daten.id, p);
		return BenutzerKompetenz._mapID;
	}

	/**
	 * Gibt eine Map von den Benutzerkompetenzen-Gruppen auf die zugehörigen Benutzerkompetenzen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Benutzerkompetenzen-Gruppen auf die zugehörigen Benutzerkompetenzen
	 */
	private static getMapGruppenZuordnung() : HashMap<BenutzerKompetenzGruppe, List<BenutzerKompetenz>> {
		if (BenutzerKompetenz._mapGruppenZuordnung.size() === 0) {
			for (let g of BenutzerKompetenzGruppe.values()) 
				BenutzerKompetenz._mapGruppenZuordnung.put(g, new Vector());
			for (let p of BenutzerKompetenz.values()) {
				let gruppe : BenutzerKompetenzGruppe | null = BenutzerKompetenzGruppe.getByID(p.daten.gruppe_id);
				if (gruppe === null) 
					gruppe = BenutzerKompetenzGruppe.KEINE;
				let liste : List<BenutzerKompetenz> | null = BenutzerKompetenz._mapGruppenZuordnung.get(gruppe);
				if (liste !== null) 
					liste.add(p);
			}
		}
		return BenutzerKompetenz._mapGruppenZuordnung;
	}

	/**
	 *
	 * Gibt die Benutzerkompetenz anhand der übergebenen ID zurück. 
	 * 
	 * @param id    die ID der Benutzerkompetenz
	 *  
	 * @return die Benutzerkompetenz oder null, falls die ID fehlerhaft ist
	 */
	public static getByID(id : number) : BenutzerKompetenz | null {
		return BenutzerKompetenz.getMapID().get(id);
	}

	/**
	 * Gibt die Liste aller Benutzerkompetenzen zurück, welche der übergebenen Gruppe
	 * zugeordnet sind.
	 *  
	 * @param gruppe   die Benutzerkompetenz-Gruppe
	 * 
	 * @return die Liste der Benutzerkompetenzen
	 */
	public static getKompetenzen(gruppe : BenutzerKompetenzGruppe) : List<BenutzerKompetenz> {
		let liste : List<BenutzerKompetenz> | null = BenutzerKompetenz.getMapGruppenZuordnung().get(gruppe);
		if (liste === null) 
			return new Vector();
		return liste;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : String {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof BenutzerKompetenz))
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
	public compareTo(other : BenutzerKompetenz) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BenutzerKompetenz> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : BenutzerKompetenz | null {
		let tmp : BenutzerKompetenz | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_benutzer_BenutzerKompetenz(obj : unknown) : BenutzerKompetenz {
	return obj as BenutzerKompetenz;
}
