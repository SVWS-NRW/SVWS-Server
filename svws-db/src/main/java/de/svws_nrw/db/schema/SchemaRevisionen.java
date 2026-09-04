package de.svws_nrw.db.schema;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.schema.revisionen.Revision11Updates;
import de.svws_nrw.db.schema.revisionen.Revision13Updates;
import de.svws_nrw.db.schema.revisionen.Revision14Updates;
import de.svws_nrw.db.schema.revisionen.Revision15Updates;
import de.svws_nrw.db.schema.revisionen.Revision17Updates;
import de.svws_nrw.db.schema.revisionen.Revision18Updates;
import de.svws_nrw.db.schema.revisionen.Revision1Updates;
import de.svws_nrw.db.schema.revisionen.Revision20Updates;
import de.svws_nrw.db.schema.revisionen.Revision21Updates;
import de.svws_nrw.db.schema.revisionen.Revision22Updates;
import de.svws_nrw.db.schema.revisionen.Revision23Updates;
import de.svws_nrw.db.schema.revisionen.Revision24Updates;
import de.svws_nrw.db.schema.revisionen.Revision25Updates;
import de.svws_nrw.db.schema.revisionen.Revision26Updates;
import de.svws_nrw.db.schema.revisionen.Revision27Updates;
import de.svws_nrw.db.schema.revisionen.Revision28Updates;
import de.svws_nrw.db.schema.revisionen.Revision29Updates;
import de.svws_nrw.db.schema.revisionen.Revision2Updates;
import de.svws_nrw.db.schema.revisionen.Revision30Updates;
import de.svws_nrw.db.schema.revisionen.Revision31Updates;
import de.svws_nrw.db.schema.revisionen.Revision33Updates;
import de.svws_nrw.db.schema.revisionen.Revision34Updates;
import de.svws_nrw.db.schema.revisionen.Revision35Updates;
import de.svws_nrw.db.schema.revisionen.Revision36Updates;
import de.svws_nrw.db.schema.revisionen.Revision37Updates;
import de.svws_nrw.db.schema.revisionen.Revision38Updates;
import de.svws_nrw.db.schema.revisionen.Revision3Updates;
import de.svws_nrw.db.schema.revisionen.Revision40Updates;
import de.svws_nrw.db.schema.revisionen.Revision41Updates;
import de.svws_nrw.db.schema.revisionen.Revision42Updates;
import de.svws_nrw.db.schema.revisionen.Revision43Updates;
import de.svws_nrw.db.schema.revisionen.Revision45Updates;
import de.svws_nrw.db.schema.revisionen.Revision46Updates;
import de.svws_nrw.db.schema.revisionen.Revision4Updates;
import de.svws_nrw.db.schema.revisionen.Revision51Updates;
import de.svws_nrw.db.schema.revisionen.Revision52Updates;
import de.svws_nrw.db.schema.revisionen.Revision53Updates;
import de.svws_nrw.db.schema.revisionen.Revision55Updates;
import de.svws_nrw.db.schema.revisionen.Revision56Updates;
import de.svws_nrw.db.schema.revisionen.Revision58Updates;
import de.svws_nrw.db.schema.revisionen.Revision60Updates;
import de.svws_nrw.db.schema.revisionen.Revision61Updates;
import de.svws_nrw.db.schema.revisionen.Revision62Updates;
import de.svws_nrw.db.schema.revisionen.Revision67Updates;
import de.svws_nrw.db.schema.revisionen.Revision69Updates;
import de.svws_nrw.db.schema.revisionen.Revision6Updates;
import de.svws_nrw.db.schema.revisionen.Revision71Updates;
import de.svws_nrw.db.schema.revisionen.Revision72Updates;
import de.svws_nrw.db.schema.revisionen.Revision74Updates;
import de.svws_nrw.db.schema.revisionen.RevisionNoUpdates;

/**
 * Diese Klasse enthält eine Aufzählung zu den unterschiedlichen Revisionen,
 * in welchem sich ein SVWS-Datenbankschema befinden kann.
 */
public enum SchemaRevisionen {

	/**
	 * Dummy Revision, welche anzeigt dass keine Revision definiert ist (z.B. zum kennzeichnen,
	 * dass ein Datensatz noch nicht veraltet ist)
	 */
	UNDEFINED(-1, "2022-09-29"),

	/**
	 * Erste Version der SVWS-Datenbank. Das Schema wurde von der letzten Schild-NRW Version 2.x übernommen
	 * und um neue Tabellen angereichert.
	 */
	REV_0(0, "2022-09-29"),

	/**
	 * Korrekturen an aus Schild2 importierten Daten, bevor weitere Fremdschlüssel mit Revision 2 ergänzt werden.
	 * Außerdem:
	 * - Hinzufügen der Tabelle SchildKursSchueler (Erstellen der Tabelle) für den schnellen Zugriff auf die
	 * Schüler-Zuordnung zu Kursen.
	 * - Hizufügen von Tabellen für die Kommunikation mit Schulbewerbung.de
	 * - Anpassungen an den Lehrer-Tabellen
	 */
	REV_1(1, "2022-09-29"),

	/**
	 * Hinzufügen weiterer Fremdschlüssel, um die referentielle Integrität in zukünftigen Revisionen zu verbessern.
	 * Erstellen der Trigger zur automatischen Aktualisierung der Tabelle SchildKursSchueler bei Änderung
	 * der Leistungsdaten eines Schülers.
	 * Außerdem wird die Tabelle mit den Daten aus den Leistungsdaten eines Schülers initial befüllt.
	 * Es werden AES- und RSA-Credentials für die eigene Schule erzeugt.
	 */
	REV_2(2, "2022-09-29"),

	/**
	 * Befüllen der Foreign-Keys auf die Tabelle K_Ort in den Tabellen K_AllgAdresse, K_Lehrer, Schueler, SchuelerErzAdr.
	 * <p>
	 * Außerdem wird ein ggf. konfigurierte Quartalsmodus entfernt. Dabei gehen die "Zwischenstände" der vergangenen
	 * Quartale verloren. Die abschließenden Quartale des Halbjahres werden übernommen. Sollte das aktuelle Quartal
	 * einer Schule im Quartalsmodus das 1. Quartal eines Halbjahres sein, so wird dieses als Grundlage für das Halbjahr
	 * verwendet.
	 * <p>
	 * Abschließend werden doppelte Einträge zur gleichen Sprache in der Schülersprachenfolge ggf. zusammengefasst
	 */
	REV_3(3, "2022-09-29"),

	/**
	 * Tabellen für die Laufbahnplanung in der gymnasialen Oberstufe hinzugefügt.
	 */
	REV_4(4, "2022-09-29"),

	/**
	 * Tabellen für Stundenpläne mit Unterrichts- und Pausenzeiten
	 */
	REV_5(5, "2022-09-29"),

	/**
	 * Erstellen von allgemein nutzbaren Views und Tabellen für das ENM
	 */
	REV_6(6, "2022-09-29"),

	/**
	 * Tabellen für die Kursblockung in der gymnasialen Oberstufe hinzugefügt
	 */
	REV_7(7, "2022-09-29"),

	/**
	 * Tabellen für DavRessourcen und Sammlungen
	 */
	REV_8(8, "2022-09-29"),

	/**
	 * Tabellen für Klausurplanung
	 */
	REV_9(9, "2022-12-30"),

	/**
	 * Spalte IstPruefungsordnungsRelevant in der Tabelle EigeneSchule_Faecher ergänzt
	 */
	REV_10(10, "2024-01-26"),

	/**
	 * Hinzufügen der Tabelle EigeneSchule_Email für die Verwaltung der schulweiten Email-Konfiguration
	 */
	REV_11(11, "2024-03-02"),

	/**
	 * Ergänzen weitere Indizes zur Optimierung von Datenbankzugriffen
	 */
	REV_12(12, "2024-03-09"),

	/**
	 * Ergänzen eines Index und ggf. Entfernen einer fehlerhaften Unique-Constraints
	 */
	REV_13(13, "2024-04-12"),

	/**
	 * Ergänzungen für die Schnittstelle zu einem externen Notenmodul
	 */
	REV_14(14, "2024-05-02"),

	/**
	 * Bemerkungen bei Schülern als Vermerke speichern
	 */
	REV_15(15, "2024-05-22"),

	/**
	 * Anpassungen bei Stundenplan-Pausenaufsichten bezüglich der Wochentyp-basierten Zuordnung
	 * zu Aufsichtsbereichen (Teil 1 - Anlegen zweier Indizes)
	 */
	REV_16(16, "2024-06-04"),

	/**
	 * Anpassungen bei Stundenplan-Pausenaufsichten bezüglich der Wochentyp-basierten Zuordnung
	 * zu Aufsichtsbereichen (Teil 2 - Korrektur einer Unique-Constraint und Verschiben der
	 * Wochentyp-Information)
	 */
	REV_17(17, "2024-06-04"),

	/**
	 * Ergänzen der Tabelle LehrerNotenmodulCredentials und übetragen der Informationen
	 * KennwortTools und KennwortToolsAktuell aus der Tabelle K_Lehrer
	 */
	REV_18(18, "2024-06-13"),

	/**
	 * Entfernen der nicht benötigten Tabelle Gost_Klausuren_Termine_Jahrgaenge und
	 * der nicht benötigten Spalte ErsetzteSprache aus der Tabelle SchuelerSprachpruefungen
	 */
	REV_19(19, "2024-07-03"),

	/** Korrigiert ggf. den Primärschlüssel auf der Tabelle Fach_Gliederungen. */
	REV_20(20, "2024-08-06"),

	/** Entfernt die Tabelle SchuelerWiedervorlage und überträgt die Daten in die neue Tabelle Wiedervorlage */
	REV_21(21, "2024-08-26"),

	/** Automatisches Ergänzen der neuen UVD-Benutzerkompetenzen, wenn Benutzerkompetenzen auf Kataloge vorhanden sind. */
	REV_22(22, "2024-08-29"),

	/** Korrektur der Tabelle Schuljahresabschnitte im Nachgang zu dem Auflösen des Quartalsmodus in Revision 3. */
	REV_23(23, "2024-09-10"),

	/** Korrektur der Tabelle SchuelerLernabschnittsdaten, falls dort die ASD-Schulgliederung nicht gesetzt ist */
	REV_24(24, "2024-10-07"),

	/** Umbenennung der Tabellen für die Zeitstempel-Informationen und Hinzufügen der Zeitstempel-Information für die Tabelle SchuelerAnkreuzfloskeln */
	REV_25(25, "2024-10-09"),

	/** Hinzufügen der Zeitstempel-Information für die Tabelle LehrerNotenmodulCredentials */
	REV_26(26, "2024-10-10"),

	/** Umstellung der Statistik-Kürzel für die Semester von 01-06 auf S1-S6 */
	REV_27(27, "2024-10-13"),

	/** Korrektur: Entfernen von Triggern, die in Revision 25 hätten entfernt werden müssen */
	REV_28(28, "2024-10-15"),

	/** Ergänzen weitere Zeitstempel-Informationen für die Tabelle SchuelerLD_PSFachBem */
	REV_29(29, "2024-10-17"),

	/** Aufräumen der Benutzertabellen zur Vermeidung von doppelten Benutzernamen in Credentials */
	REV_30(30, "2024-12-05"),

	/** Ergänzen der Klassen-Tabellen um die Schulgliederung und die Organisationsform, sofern diese über Fachklasse, Vorgänger- oder Nachfolgeklasse ermittelt werden können. */
	REV_31(31, "2025-01-23"),

	/** Erweitern der OAuth2-Client-Tabelle um die Informationen zum Bearer-Token dem TLS-Zertifikat des Servers. */
	REV_32(32, "2025-02-21"),

	/** Ergänzen von evtl. fehlenden Auto-Inkrement-Trigger auf der Tabelle SchuelerMerkmale */
	REV_33(33, "2025-02-24"),

	/** Anpassen der Tabelle Kurs_Schueler: Speichere auch die Leistungs-ID, in der der Kurs zugeordnet wird */
	REV_34(34, "2025-03-02"),

	/** Anpassungen der ASD-Jahrgangsangaben für das WBK */
	REV_35(35, "2025-03-03"),

	/** Anpassungen der ASD-Jahrgangsangaben für die GS */
	REV_36(36, "2025-03-12"),

	/** Korrektur fehlerhafter Datenbank-Trigger */
	REV_37(37, "2025-04-11"),

	/** Aktiv-Flag für Stundenpläne */
	REV_38(38, "2025-04-24"),

	/**
	 * Entfernen des Flags istSichtbar bei den Klassen,
	 * Anpassungen bei den Lehrerpersonaldaten - nicht korrekte Eintragungen bei den Anrechungsstunden, Minder- und Mehrleistungen
	 */
	REV_39(39, "2025-05-19"),

	/** Klausurplan resistent gegen Stundenplan-Änderungen */
	REV_40(40, "2025-05-20"),

	/** Ergänzen des Fremdschlüssels bei der Spalte EinschulungsartASD der Tabelle Schüler auf die Core-Type-Werte */
	REV_41(41, "2025-05-27"),

	/** Korrektur eines Triggers bei der Tabelle Kurs_Schueler */
	REV_42(42, "2025-06-04"),

	/** ggf. Ergänzen der Spalte LehramtKrz in den Tabellen LehrerLehramtFachr und LehrerLehramtLehrbef */
	REV_43(43, "2025-07-02"),

	/** Schulbesuchsjahre aus DB entfernt, PrognoseAbschluss und PrognoseLog in Lernabschnitte, Geburtsort und Geburtsname bei Lehrerdaten */
	REV_44(44, "2025-07-03"),

	/** Überarbeitung der Tabellen zu den Lehrämter und den dort zugeordneten Fachrichtungen und Lehrbefähigungen */
	REV_45(45, "2025-07-18"),

	/** Korrektur von Einträgen in der Klassentabelle. Undefiniert/Default auf Regelklasse setzen. */
	REV_46(46, "2025-08-11"),

	/** Tabelle Schuljahresabschnitte: Einführen einer Unique-Constraint auf Jahr und Abschnitt */
	REV_47(47, "2025-08-21"),

	/** Anlegen der Tabellen zur Unterrichtsverteilung */
	REV_48(48, "2025-09-01"),

	/** Anpassung der Tabellen zur Unterrichtsverteilung (Primär- und Fremdschlüssel) Teil 1 DROP von 2 Tabellen */
	REV_49(49, "2025-11-06"),

	/** Anpassung der Tabellen zur Unterrichtsverteilung (Primär- und Fremdschlüssel) Teil 2 CREATE von 2 Tabellen */
	REV_50(50, "2025-11-06"),

	/** Tabelle K_Sportbefreiung überführt in SchuelerVermerke */
	REV_51(51, "2025-11-06"),

	/** Tabellen für Floskeln und Floskelgruppen überarbeitet */
	REV_52(52, "2025-10-14"),

	/** Notenmodul: Anlegen von Tabellen zur Verwaltung der Konfiguration und von Verbindungen zu externen Servern */
	REV_53(53, "2025-10-29"),

	/** Sprachenfolge bzw. -prüfungen: Ergänzen einer Feldes für die Zeugnisbezeichnungen und eines Sprachnachweises am Weiterbildungskolleg */
	REV_54(54, "2026-02-05"),

	/** Neue Tabelle Zuordnung Ankreuzkompetenz Jahrgang */
	REV_55(55, "2026-02-06"),

	/** Fach_ID der Tabelle Katalog_Floskeln als Fremdschlüssel der Tabelle EigeneSchule_Faecher definieren */
	REV_56(56, "2026-02-10"),

	/** Neue Spalte Aktiv in der Tabelle Gost_Klausuren_Schuelerklausuren */
	REV_57(57, "2026-02-23"),

	/** Aktivierung der Fremdschlüsselbeziehung zwischen der Tabelle Klassen und EigeneSchule_Teilstandorte über das AdrMerkmal */
	REV_58(58, "2026-03-25"),

	/** Neue Tabelle LehrerUnterrichtsfaecher für die Zuordnung von Unterrichtsfächern zu Lehrkräften */
	REV_59(59, "2026-03-26"),

	/** Erweiterung an den Tabellen zu den Notenmodul-Credentials */
	REV_60(60, "2026-04-01"),

	/** Erstellen eines Triggers zum Verhindern fehlerhafter Klassen-Zuordnung bei Lernabschnitten (unterschiedliche Schuljahresabschnitte)
	 * und Ergänzen fehlender Einträge in TimestampsNotenmodulCredentials */
	REV_61(61, "2026-04-16"),

	/** Anlegen der Tabellen zur Logoverwaltung */
	REV_62(62, "2026-04-17"),

	/** Korrektur eines Triggers in TimestampsNotenmodulCredentials */
	REV_63(63, "2026-04-17"),

	/** Erweiterung Tabelle EigeneSchule_Fachklassen um Feld Kuerzel */
	REV_64(64, "2026-05-19"),

	/** Anpassung der Tabellen zur Unterrichtsverteilung (Primär- und Fremdschlüssel) Teil 1 DROP von Spalten, FKs, UCs */
	REV_65(65, "2026-05-28"),

	/** Anpassung der Tabellen zur Unterrichtsverteilung (Primär- und Fremdschlüssel) Teil 2 CREATE von 2 Tabellen LehrerUnterrichtsfaecher, Raumgruppen und
	 * KlassenLehrern, Spalten und FKs */
	REV_66(66, "2026-05-28"),

	/** Verwende in der Datenbank grundsätzlich UTC-Zeitstempel in Triggern. Die Handhabung der Zeitzone erfolgt ggf. im SVWS-Server */
	REV_67(67, "2026-06-08"),

	/** Anpassung der Tabellen zu Klausurvorgaben und Hinzufügen einer neuen Tabelle für die Definition von GKLs bei Schülern */
	REV_68(68, "2026-06-11"),

	/** Tabelle: Schueler -> Spalte LSSchulform Werte FE und SK auf S umschlüsseln (Anderungen in der HerkunftSonstige.json durch IT-NRW Issue #3578) */
	REV_69(69, "2026-06-18"),

	/** Verwende in Triggern beim Abgleich von Zeitstempeln den Spaceship-Operator*/
	REV_70(70, "2026-06-24"),

	/** Ergänzen des Fremdschlüssels auf K_Lehrer in der Tabelle Schueler_AllgAdr für das Feld Betreuungslehrer_ID */
	REV_71(71, "2026-06-24"),

	/** Herkunftbildungsgang ersetzt HerkunftbildungsgangTyp - daher: Umschlüsseln der WBK-Schlüssel AG, AR, KL aus HerkunftbildungsgangTyp auf G02, R02, K02
	 * in Herkunftbildungsgang und nullen der BK-Schlüssel BF, BS, BY, F0, FS */
	REV_72(72, "2026-08-04"),

	/** Tabelle: SchuleOAuthSecrets um Spalte Domaine ergänzt */
	REV_73(73, "2026-08-06"),

	/** Tabelle: SchuleOAuthSecrets um Trigger ergänzt */
	REV_74(74, "2026-08-18"),

	/** Erweiterung Tabelle Schueler um Feld idHochschulabschluss */
	REV_75(75, "2026-09-01"),

	/** Entfernt überflüssige Spalten aus der Tabelle Logo */
	REV_76(76, "2026-09-01");

	/**
	 * Gibt die größte Revisionsnummer an, die in dieser Enumeration definiert wurde und
	 * bis zu welcher alle Schema-Revision als stabil gelten und ab Version 1.0 des SVWS-Servers
	 * nicht mehr verändert werden.
	 */
	public static final SchemaRevisionen maxRevision = REV_76;

	/**
	 * Gibt die größte Revisions-Nummer an, welche in diese Enumeration definiert wurde.
	 * Dies dient dazu Revisionen als Entwickler-Revisionen zu kennzeichnen, die noch nicht
	 * stabil sind. Dieser Wert ist also größer oder gleich {@link SchemaRevisionen#maxRevision}.
	 */
	public static final SchemaRevisionen maxDeveloperRevision = REV_76;


	/** Eine Map, welche von der Revisionsnummer auf das Objekt der Aufzählung abbildet. */
	private static Map<Long, SchemaRevisionen> _mapByNumber = null;


	/** Die Nummer der Schema-Revision */
	public final long revision;

	/** Das Datum, wann diese Revision eingeführt wurde */
	public final LocalDate date;

	/** Das Objekt mit den Update-Befehlen für diese Revision */
	private SchemaRevisionUpdateSQL updater;


	/**
	 * Erstellt eine neue Revision mit der angegebenen Nummer und dem
	 * Datum der Definition für diese Revision.
	 *
	 * @param revision   die Revisionsnummer
	 * @param date       das Datum, wann diese Revision eingeführt wurde
	 */
	SchemaRevisionen(final long revision, final String date) {
		this.revision = revision;
		this.date = LocalDate.parse(date);
		this.updater = null;
	}


	/**
	 * Erstellt die Map für die Abbildung der Revisionsnummer auf das Objekt der Aufzählung.
	 *
	 * @return die Map für die Abbildung
	 */
	private static Map<Long, SchemaRevisionen> getMapByNumber() {
		if (_mapByNumber == null) {
			_mapByNumber = Arrays.stream(SchemaRevisionen.values()).collect(Collectors.toMap(r -> r.revision, r -> r));
		}
		return _mapByNumber;
	}

	/**
	 * Bestimmt das zu der übergebenen Revisionsnummer gehörende Objekt der Aufzählung.
	 *
	 * @param revision   die Revisionsnummer
	 *
	 * @return das Objekt der Aufzählung
	 */
	public static SchemaRevisionen get(final long revision) {
		return getMapByNumber().get(revision);
	}


	/**
	 * Gibt ein Objekt mit den Update-Befehlen für diese Revision zurück.
	 *
	 * @return das Objekt für die Updates zu dieser Revision
	 */
	public final SchemaRevisionUpdateSQL getUpdater() {
		if (updater == null) {
			updater = switch (this) {
				case REV_1 -> new Revision1Updates();
				case REV_2 -> new Revision2Updates();
				case REV_3 -> new Revision3Updates();
				case REV_4 -> new Revision4Updates();
				case REV_6 -> new Revision6Updates();
				case REV_11 -> new Revision11Updates();
				case REV_13 -> new Revision13Updates();
				case REV_14 -> new Revision14Updates();
				case REV_15 -> new Revision15Updates();
				case REV_17 -> new Revision17Updates();
				case REV_18 -> new Revision18Updates();
				case REV_20 -> new Revision20Updates();
				case REV_21 -> new Revision21Updates();
				case REV_22 -> new Revision22Updates();
				case REV_23 -> new Revision23Updates();
				case REV_24 -> new Revision24Updates();
				case REV_25 -> new Revision25Updates();
				case REV_26 -> new Revision26Updates();
				case REV_27 -> new Revision27Updates();
				case REV_28 -> new Revision28Updates();
				case REV_29 -> new Revision29Updates();
				case REV_30 -> new Revision30Updates();
				case REV_31 -> new Revision31Updates();
				case REV_33 -> new Revision33Updates();
				case REV_34 -> new Revision34Updates();
				case REV_35 -> new Revision35Updates();
				case REV_36 -> new Revision36Updates();
				case REV_37 -> new Revision37Updates();
				case REV_38 -> new Revision38Updates();
				case REV_40 -> new Revision40Updates();
				case REV_41 -> new Revision41Updates();
				case REV_42 -> new Revision42Updates();
				case REV_43 -> new Revision43Updates();
				case REV_45 -> new Revision45Updates();
				case REV_46 -> new Revision46Updates();
				case REV_51 -> new Revision51Updates();
				case REV_52 -> new Revision52Updates();
				case REV_53 -> new Revision53Updates();
				case REV_55 -> new Revision55Updates();
				case REV_56 -> new Revision56Updates();
				case REV_58 -> new Revision58Updates();
				case REV_60 -> new Revision60Updates();
				case REV_61 -> new Revision61Updates();
				case REV_62 -> new Revision62Updates();
				case REV_67 -> new Revision67Updates();
				case REV_69 -> new Revision69Updates();
				case REV_71 -> new Revision71Updates();
				case REV_72 -> new Revision72Updates();
				case REV_74 -> new Revision74Updates();
				default -> new RevisionNoUpdates(this);
			};
		}
		return updater;
	}

}
