package de.nrw.schule.svws.db.utils.schema;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.data.fach.FachKatalogEintrag;
import de.nrw.schule.svws.core.data.jahrgang.JahrgangsKatalogEintrag;
import de.nrw.schule.svws.core.data.jahrgang.JahrgangsKatalogEintragBezeichnung;
import de.nrw.schule.svws.core.data.klassen.KlassenartKatalogEintrag;
import de.nrw.schule.svws.core.data.kurse.KursartKatalogEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogAbgangsgrundEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogAnrechnungsgrundEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogBeschaeftigungsartEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogEinsatzstatusEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogFachrichtungAnerkennungEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogFachrichtungEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogLehramtAnerkennungEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogLehramtEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogLehrbefaehigungAnerkennungEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogLehrbefaehigungEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogMehrleistungsartEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogMinderleistungsartEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogRechtsverhaeltnisEintrag;
import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogZugangsgrundEintrag;
import de.nrw.schule.svws.core.data.schule.AllgemeineMerkmaleKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.BerufskollegAnlageKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalogDaten;
import de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalogIndex;
import de.nrw.schule.svws.core.data.schule.EinschulungsartKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.FoerderschwerpunktKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.HerkunftKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.HerkunftsartKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.HerkunftsartKatalogEintragBezeichnung;
import de.nrw.schule.svws.core.data.schule.NationalitaetenKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.OrganisationsformKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.ReligionKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulformKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulformSchulgliederung;
import de.nrw.schule.svws.core.data.schule.SchulgliederungKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.VerkehrsspracheKatalogEintrag;
import de.nrw.schule.svws.core.types.KursFortschreibungsart;
import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.core.types.PersonalTyp;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenzGruppe;
import de.nrw.schule.svws.core.types.lehrer.LehrerLeitungsfunktion;
import de.nrw.schule.svws.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.nrw.schule.svws.core.types.schule.BerufskollegAnlage;
import de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene1;
import de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene2;
import de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene3;
import de.nrw.schule.svws.core.types.schule.BerufskollegOrganisationsformen;
import de.nrw.schule.svws.core.types.schule.Nationalitaeten;
import de.nrw.schule.svws.core.types.schule.SchulabschlussAllgemeinbildend;
import de.nrw.schule.svws.core.types.schule.SchulabschlussBerufsbildend;
import de.nrw.schule.svws.core.types.schule.Verkehrssprache;
import de.nrw.schule.svws.core.types.schule.WeiterbildungskollegOrganisationsformen;
import de.nrw.schule.svws.core.types.statkue.AllgemeineMerkmale;
import de.nrw.schule.svws.core.types.statkue.Einschulungsart;
import de.nrw.schule.svws.core.types.statkue.Fachgruppe;
import de.nrw.schule.svws.core.types.statkue.Foerderschwerpunkt;
import de.nrw.schule.svws.core.types.statkue.Herkunft;
import de.nrw.schule.svws.core.types.statkue.Herkunftsarten;
import de.nrw.schule.svws.core.types.statkue.Jahrgaenge;
import de.nrw.schule.svws.core.types.statkue.Klassenart;
import de.nrw.schule.svws.core.types.statkue.LehrerAbgangsgrund;
import de.nrw.schule.svws.core.types.statkue.LehrerAnrechnungsgrund;
import de.nrw.schule.svws.core.types.statkue.LehrerBeschaeftigungsart;
import de.nrw.schule.svws.core.types.statkue.LehrerEinsatzstatus;
import de.nrw.schule.svws.core.types.statkue.LehrerFachrichtung;
import de.nrw.schule.svws.core.types.statkue.LehrerFachrichtungAnerkennung;
import de.nrw.schule.svws.core.types.statkue.LehrerLehramt;
import de.nrw.schule.svws.core.types.statkue.LehrerLehramtAnerkennung;
import de.nrw.schule.svws.core.types.statkue.LehrerLehrbefaehigung;
import de.nrw.schule.svws.core.types.statkue.LehrerLehrbefaehigungAnerkennung;
import de.nrw.schule.svws.core.types.statkue.LehrerMehrleistungArt;
import de.nrw.schule.svws.core.types.statkue.LehrerMinderleistungArt;
import de.nrw.schule.svws.core.types.statkue.LehrerRechtsverhaeltnis;
import de.nrw.schule.svws.core.types.statkue.LehrerZugangsgrund;
import de.nrw.schule.svws.core.types.statkue.Religion;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.types.statkue.Schulgliederung;
import de.nrw.schule.svws.core.types.statkue.ZulaessigeKursart;
import de.nrw.schule.svws.core.types.statkue.ZulaessigesFach;
import de.nrw.schule.svws.core.utils.schule.BerufskollegFachklassenManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.DBException;
import de.nrw.schule.svws.db.dto.current.svws.db.DTOCoreTypeVersion;
import de.nrw.schule.svws.json.JsonDaten;
import de.nrw.schule.svws.logger.Logger;

/**
 * Diese Klasse dient der Aktualisierung von Core-Type-Daten in der
 * Datenbank. Diese Updates werden von Prozessen, wie z.B. dem Erstellen,
 * Migrieren, Aktualisieren, Importieren und Exportieren von DB-Schemata
 * durchgeführt. Eine Prüfung beim Serverstart im Rahmen des dortigen
 * Update-Prozesses ist wünschenswert, da die Core-Typs nicht zwingend 
 * an neue Datenbank-Revisionen geknüpft sind.
 */
public class DBCoreTypeUpdater {
	
	/** Der Schema-Manager, welcher für die Updates verwendet wird */
	private final DBSchemaManager schemaManager;
	
	/** Ein Logger, um die Abläufe bei dem Update-Prozess zu loggen */ 
	private final Logger logger;	

	/** Der Status des Datenbank-Schema */
	private final DBSchemaStatus status;
	
	/** Ein Record mit dem Tabellen-Namen, der Version des Core-Types und einem Lambda für die Aktualisierung der Tabelle mit den Core-Type-Daten */
	private record CoreTypeTable(String name, long version, Consumer<Logger> updater) {}

	/** Eine Liste von Records mit den zu aktualisierenden Tabellen - siehe auch {@link CoreTypeTable} */
	private final Vector<CoreTypeTable> tables = new Vector<>();
	
	
	/**
	 * Erzeugt einen neuen {@link DBCoreTypeUpdater}.
	 * 
	 * @param schemaManager   der Schema-Manager, welcher verwendet wird
	 * @param returnOnError   gibt an, ob Operatioen bei Einzelfehlern abgebrochen werden sollen
	 */
	DBCoreTypeUpdater(DBSchemaManager schemaManager, boolean returnOnError) {
		this.schemaManager = schemaManager;
		this.logger = schemaManager.getLogger();
		this.status = schemaManager.getSchemaStatus();
		tables.add(new CoreTypeTable("Kompetenzgruppen", BenutzerKompetenzGruppe.VERSION, updateBenutzerKompetenzGruppen));
		tables.add(new CoreTypeTable("Kompetenzen", BenutzerKompetenz.VERSION, updateBenutzerKompetenzen));
		tables.add(new CoreTypeTable("Berufskolleg_Anlagen", BerufskollegAnlage.VERSION, updateBerufskollegAnlagen));
		tables.add(new CoreTypeTable("Berufskolleg_Berufsebenen1", BerufskollegBerufsebene1.VERSION, updateBerufskollegBerufsebene1));
		tables.add(new CoreTypeTable("Berufskolleg_Berufsebenen2", BerufskollegBerufsebene2.VERSION, updateBerufskollegBerufsebene2));
		tables.add(new CoreTypeTable("Berufskolleg_Berufsebenen3", BerufskollegBerufsebene3.VERSION, updateBerufskollegBerufsebene3));
		tables.add(new CoreTypeTable("Berufskolleg_Fachklassen", JsonDaten.fachklassenManager.getVersion(), updateBerufskollegFachklassen));
		tables.add(new CoreTypeTable("Berufskolleg_Fachklassen_Keys", JsonDaten.fachklassenManager.getVersion(), updateBerufskollegFachklassenKeys));
		tables.add(new CoreTypeTable("Fachgruppen", Fachgruppe.VERSION, updateFachgruppen));
		tables.add(new CoreTypeTable("KursFortschreibungsarten", KursFortschreibungsart.VERSION, updateKursFortschreibungsarten));
		tables.add(new CoreTypeTable("Schulabschluesse_Allgemeinbildend", SchulabschlussAllgemeinbildend.VERSION, updateSchulabschluesseAllgemeinbildend));
		tables.add(new CoreTypeTable("Schulabschluesse_Berufsbildend", SchulabschlussBerufsbildend.VERSION, updateSchulabschluesseBerufsbildend));
		tables.add(new CoreTypeTable("Schulformen", Schulform.VERSION, updateSchulformen));
		tables.add(new CoreTypeTable("Schulgliederungen", Schulgliederung.VERSION, updateSchulgliederungen));
		tables.add(new CoreTypeTable("Schulgliederungen_Schulformen", Schulgliederung.VERSION, updateSchulgliederungenSchulformen));
		tables.add(new CoreTypeTable("Schulgliederungen_Abschluesse_Allgemeinbildend", Schulgliederung.VERSION, updateSchulgliederungenAbschluesseAllg));
		tables.add(new CoreTypeTable("Schulgliederungen_Abschluesse_Berufsbildend", Schulgliederung.VERSION, updateSchulgliederungenAbschluesseBeruf));
		tables.add(new CoreTypeTable("Foerderschwerpunkte", Foerderschwerpunkt.VERSION, updateFoerderschwerpunkte));
		tables.add(new CoreTypeTable("Foerderschwerpunkte_Schulformen", Foerderschwerpunkt.VERSION, updateFoerderschwerpunkteSchulformen));
		tables.add(new CoreTypeTable("Statkue_LehrerAbgang", LehrerAbgangsgrund.VERSION, updateLehrerAbgangsgruende));
		tables.add(new CoreTypeTable("Statkue_LehrerAnrechnung", LehrerAnrechnungsgrund.VERSION, updateLehrerAnrechnungsgruende));
		tables.add(new CoreTypeTable("Statkue_LehrerBeschaeftigungsart", LehrerBeschaeftigungsart.VERSION, updateLehrerBeschaeftigungsarten));
		tables.add(new CoreTypeTable("Statkue_LehrerEinsatzstatus", LehrerEinsatzstatus.VERSION, updateLehrerEinsatzstatus));
		tables.add(new CoreTypeTable("Statkue_LehrerFachrAnerkennung", LehrerFachrichtungAnerkennung.VERSION, updateLehrerFachrichtungAnerkennungen));
		tables.add(new CoreTypeTable("Statkue_LehrerFachrichtung", LehrerFachrichtung.VERSION, updateLehrerFachrichtungen));
		tables.add(new CoreTypeTable("Statkue_LehrerLehramt", LehrerLehramt.VERSION, updateLehreraemter));
		tables.add(new CoreTypeTable("Statkue_LehrerLehramtAnerkennung", LehrerLehramtAnerkennung.VERSION, updateLehreramtAnerkennungen)); 
		tables.add(new CoreTypeTable("Statkue_LehrerLehrbefaehigung", LehrerLehrbefaehigung.VERSION, updateLehrbefaehigungen));
		tables.add(new CoreTypeTable("Statkue_LehrerLehrbefAnerkennung", LehrerLehrbefaehigungAnerkennung.VERSION, updateLehrbefaehigungAnerkennungen));
		tables.add(new CoreTypeTable("Statkue_LehrerMehrleistung", LehrerMehrleistungArt.VERSION, updateLehrerMehrleisungsarten));
		tables.add(new CoreTypeTable("Statkue_LehrerMinderleistung", LehrerMinderleistungArt.VERSION, updateLehrerMinderleisungsarten));
		tables.add(new CoreTypeTable("Statkue_LehrerRechtsverhaeltnis", LehrerRechtsverhaeltnis.VERSION, updateLehrerRechtsverhaeltnisse));
		tables.add(new CoreTypeTable("Statkue_LehrerZugang", LehrerZugangsgrund.VERSION, updateLehrerZugangsgruende));
		tables.add(new CoreTypeTable("PersonalTypen", PersonalTyp.VERSION, updatePersonalTypen));
		tables.add(new CoreTypeTable("Nationalitaeten", Nationalitaeten.VERSION, updateNationalitaeten));
		tables.add(new CoreTypeTable("Nationalitaeten_Keys", Nationalitaeten.VERSION, updateNationalitaeten_Keys));
		tables.add(new CoreTypeTable("Jahrgaenge", Jahrgaenge.VERSION, updateJahrgaenge));
		tables.add(new CoreTypeTable("Jahrgaenge_Bezeichnungen", Jahrgaenge.VERSION, updateJahrgaengeBezeichnungen));
		tables.add(new CoreTypeTable("Jahrgaenge_Keys", Jahrgaenge.VERSION, updateJahrgaengeKeys));
		tables.add(new CoreTypeTable("Noten", Note.VERSION, updateNoten));
		tables.add(new CoreTypeTable("SchuelerStatus", SchuelerStatus.VERSION, updateSchuelerStatus));
		tables.add(new CoreTypeTable("Verkehrssprachen", Verkehrssprache.VERSION, updateVerkehrssprachen));
		tables.add(new CoreTypeTable("Herkunft", Herkunft.VERSION, updateHerkuenfte));
		tables.add(new CoreTypeTable("Herkunft_Keys", Herkunft.VERSION, updateHerkuenfteKeys));
		tables.add(new CoreTypeTable("Herkunft_Schulformen", Herkunft.VERSION, updateHerkuenfteSchulformen));
		tables.add(new CoreTypeTable("Herkunftsart", Herkunftsarten.VERSION, updateHerkunftsarten));
		tables.add(new CoreTypeTable("Herkunftsart_Keys", Herkunftsarten.VERSION, updateHerkunftsartenKeys));
		tables.add(new CoreTypeTable("Herkunftsart_Schulformen", Herkunftsarten.VERSION, updateHerkunftsartenSchulformen));
		tables.add(new CoreTypeTable("KlassenartenKatalog", Klassenart.VERSION, updateKlassenarten));
		tables.add(new CoreTypeTable("KlassenartenKatalog_Keys", Klassenart.VERSION, updateKlassenartenKeys));
		tables.add(new CoreTypeTable("KlassenartenKatalog_Schulformen", Klassenart.VERSION, updateKlassenartenSchulformen));
		tables.add(new CoreTypeTable("KursartenKatalog", ZulaessigeKursart.VERSION, updateKursarten));
		tables.add(new CoreTypeTable("KursartenKatalog_Keys", ZulaessigeKursart.VERSION, updateKursartenKeys));
		tables.add(new CoreTypeTable("KursartenKatalog_Schulformen", ZulaessigeKursart.VERSION, updateKursartenSchulformen));
		tables.add(new CoreTypeTable("FachKatalog", ZulaessigesFach.VERSION, updateZulaessigeFaecher));
		tables.add(new CoreTypeTable("FachKatalog_Keys", ZulaessigesFach.VERSION, updateZulaessigeFaecherKeys));
		tables.add(new CoreTypeTable("FachKatalog_Schulformen", ZulaessigesFach.VERSION, updateZulaessigeFaecherSchulformen));
		tables.add(new CoreTypeTable("EinschulungsartKatalog", Einschulungsart.VERSION, updateEinschulungsarten));
		tables.add(new CoreTypeTable("EinschulungsartKatalog_Keys", Einschulungsart.VERSION, updateEinschulungsartenKeys));
		tables.add(new CoreTypeTable("Religionen", Religion.VERSION, updateReligionen));
		tables.add(new CoreTypeTable("Religionen_Keys", Religion.VERSION, updateReligionenKeys));
		tables.add(new CoreTypeTable("AllgemeineMerkmaleKatalog", AllgemeineMerkmale.VERSION, updateAllgemeineMerkmale));
		tables.add(new CoreTypeTable("AllgemeineMerkmaleKatalog_Keys", AllgemeineMerkmale.VERSION, updateAllgemeineMerkmaleKeys));
		tables.add(new CoreTypeTable("AllgemeineMerkmaleKatalog_Schulformen", AllgemeineMerkmale.VERSION, updateAllgemeineMerkmaleSchulformen));
		tables.add(new CoreTypeTable("OrganisationsformenKatalog", BerufskollegOrganisationsformen.VERSION + WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION, updateOrganisationsformen));
		tables.add(new CoreTypeTable("OrganisationsformenKatalog_Keys", BerufskollegOrganisationsformen.VERSION + WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION, updateOrganisationsformenKeys));
		tables.add(new CoreTypeTable("OrganisationsformenKatalog_Schulformen", BerufskollegOrganisationsformen.VERSION + WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION, updateOrganisationsformenSchulformen));
        tables.add(new CoreTypeTable("LehrerLeitungsfunktion_Keys", LehrerLeitungsfunktion.VERSION, updateLehrerLeitungsfunktionenKeys));
	}
	

	/**
	 * Prüft, ob die Core-Types aktuell sind, d.h. die Version in den Core-Types mit der 
	 * Version in der Datenbank übereinstimmt.
	 * 
	 * @return true, falls die Core-Types in der DB aktuell sind, sonst false
	 */
	public boolean isUptodate() {
		status.update();
		for (CoreTypeTable entry : tables)
			if ((entry.name == null) || (!pruefeVersion(entry.name, entry.version))) 
				return false;			
		return true;
	}
	
	
	
	/**
	 * Prüft, ob eine Aktualisierung der Core-Types möglich ist,
	 * d.h. die Java-Core-Types gleich alt oder neuer sind als die in
	 * der Datenbank vorhandenen Core-Types. Ist dies nicht der
	 * Fall, so wird i.A. eine veraltete Server-Version mit einem neueren Schema
	 * verwendet.
	 * 
	 * @return true, falls eine Aktualisierung möglich ist, sonst false
	 */
	public boolean isUpdatable() {
		// Prüfe zunächst, ob ein Update möglich ist
		status.update();
		for (CoreTypeTable entry : tables) {
			DTOCoreTypeVersion v = status.getCoreTypeVersion(entry.name);
			if ((v == null) || (v.Version == null))
				continue; // Bisher keine Version gespeichert - Update also möglich
			if  (Long.compare(entry.version, v.Version) < 0)
				return false;  // Die Version des Core-Types ist kleiner als die Version in der DB
		}
		return true;
	}


	/**
	 * Aktualisiert die Core-Types im Schema schrittweise auf die angegebene Revision
	 * 
	 * @param lockSchema          gibt an, on das Schema für den Update-Prozess gesperrt werden soll. Dies ist z.B. nicht 
	 *                            notwendig, wenn der Update-Prozess im Rahmen einer Migration gestartet wird.  
	 * @param revision            die Datenbank-Revision auf welche aktualisiert wird
	 * 
	 * @return true im Erfolgsfall, sonst false
	 */
	public boolean update(boolean lockSchema, long revision) {
		// Sperre ggf. das Datenbankschema
		if ((lockSchema) && (!SVWSKonfiguration.get().lockSchema(schemaManager.getSchemaStatus().schemaName))) {
			logger.logLn("-> Update fehlgeschlagen! (Schema ist aktuell gesperrt und kann daher nicht aktualisiert werden)");
			return false;
		}
		
		try {
			// Prüfe zunächst, ob ein Update möglich ist
			if (!isUpdatable())
				throw new DBException("Core-Types können nicht aktualisiert werden, da die Core-Type-Version in der Datenbank neuer sind als die des Servers.");
			// Bestimme ggf. die aktuelle Datenbank-Revision
			if (revision < 0)
				revision = status.getVersion().getRevisionOrDefault(-1);
			if (revision < 0)
				throw new DBException("Core-Types können nicht aktualisiert werden, da die Revision der Datenbank nicht bestimmt werden kann.");
			// Aktualisiere ggf. die Daten der einzelnen Core-Types
			for (CoreTypeTable entry : tables)
				if (!pruefeVersion(entry.name, entry.version))
					entry.updater.accept(logger);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			// Entsperre ggf. das Datenbankschema
			if ((lockSchema) && (!SVWSKonfiguration.get().unlockSchema(schemaManager.getSchemaStatus().schemaName))) {
				logger.logLn("-> Update evtl. fehlgeschlagen! (Fehler beim Freigeben des Datenbank-Schemas. Schema ist nicht gesperrt - dies wird an dieser Stelle nicht erwartet!)");
				return false;
			}
		}
	}
	
	/**
	 * Aktualisiert die Datenbank in Bezug auf den Core-Type in der angegebenen
	 * Tabelle mithilfe des übergebenen SQL-Befehls.
	 * Dabei wird in einer Datenbank-Transaktion zunächst eine 
	 * Lösch-Operation für alle Daten der Tabelle durchgeführt und 
	 * anschließend die Insert-Operation, bevor die Version in der Versions-Tabelle
	 * für die Core-Types aktualisiert wird.
	 * Die Operationen müssen sicherstellen, dass die referentielle
	 * Integrität nicht zerstört wird. Ist dies nicht der Fall, so wird
	 * die Transaktion nicht ausgeführt.
	 * 
	 * @param tabname     der Name der Tabelle
	 * @param typename    der Simple-Name des Core-Types
	 * @param version     die neu zu setzende Version des Core-Type
	 * @param sqlInsert   der Befehl zum Einfügen der Core-type-Daten
	 */
	private void updateCoreTypeTabelle(String tabname, String typename, long version, String sqlInsert) {
		DBEntityManager conn = schemaManager.getEntityManager();
		try {
			conn.transactionBegin();
			// Lösche alle Daten
			conn.transactionNativeDelete("DELETE FROM " + tabname);
			// Füge die aktuellen Daten des Core-Types ein
			conn.transactionNativeUpdate(sqlInsert);
			// Aktualsiere die Core-Type-Version in der entsprechenden Tabelle
			DTOCoreTypeVersion v = status.getCoreTypeVersion(tabname);
			if (v == null) {
				v = new DTOCoreTypeVersion(tabname, typename, version);
			} else {
				v.Version = version;
			}
			conn.transactionPersist(v);
			conn.transactionCommit();
		} catch (Exception e) {
			conn.transactionRollback();
			throw e;
		}
	}
	

	/**
	 * Aktualisiert die Tabelle für den Core-Type BenutzerKompetenz 
	 */
	private Consumer<Logger> updateBenutzerKompetenzen = (Logger logger) -> {
		String tabname = "Kompetenzen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(KO_ID, KO_Gruppe, KO_Bezeichnung) ");
		BenutzerKompetenz[] values = BenutzerKompetenz.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append(values[i].daten.id).append(",");
			sql.append(values[i].daten.gruppe_id).append(",");
			sql.append("'").append(values[i].daten.bezeichnung).append("'").append(")");
		}
		updateCoreTypeTabelle(tabname, BenutzerKompetenz.class.getCanonicalName(), BenutzerKompetenz.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type BenutzerKompetenz 
	 */
	private Consumer<Logger> updateBenutzerKompetenzGruppen = (Logger logger) -> {
		String tabname = "Kompetenzgruppen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(KG_ID, KG_Bezeichnung, KG_Spalte, KG_Zeile) ");
		BenutzerKompetenzGruppe[] values = BenutzerKompetenzGruppe.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append(values[i].daten.id).append(",");
			sql.append("'").append(values[i].daten.bezeichnung).append("'").append(",");
			sql.append(values[i].daten.spalte).append(",");
			sql.append(values[i].daten.zeile).append(")");
		}
		updateCoreTypeTabelle(tabname, BenutzerKompetenzGruppe.class.getCanonicalName(), BenutzerKompetenzGruppe.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type BerufskollegAnlagen 
	 */
	private Consumer<Logger> updateBerufskollegAnlagen = (Logger logger) -> {
		String tabname = "Berufskolleg_Anlagen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		BerufskollegAnlage[] values = BerufskollegAnlage.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			BerufskollegAnlage bkanlage = values[i];
			for (BerufskollegAnlageKatalogEintrag anlage : bkanlage.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(anlage.id).append(",");
				sql.append("'").append(anlage.kuerzel).append("'").append(",");
				sql.append("'").append(anlage.bezeichnung).append("'").append(",");
				sql.append(anlage.gueltigVon).append(",");
				sql.append(anlage.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, BerufskollegAnlage.class.getCanonicalName(), BerufskollegAnlage.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type BerufskollegBerufsebene1 
	 */
	private Consumer<Logger> updateBerufskollegBerufsebene1 = (Logger logger) -> {
		String tabname = "Berufskolleg_Berufsebenen1";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		BerufskollegBerufsebene1[] values = BerufskollegBerufsebene1.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			BerufskollegBerufsebene1 bkebene = values[i];
			for (BerufskollegBerufsebeneKatalogEintrag ebene : bkebene.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(ebene.id).append(",");
				sql.append("'").append(ebene.kuerzel).append("'").append(",");
				sql.append("'").append(ebene.bezeichnung).append("'").append(",");
				sql.append(ebene.gueltigVon).append(",");
				sql.append(ebene.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, BerufskollegBerufsebene1.class.getCanonicalName(), BerufskollegBerufsebene1.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type BerufskollegBerufsebene2 
	 */
	private Consumer<Logger> updateBerufskollegBerufsebene2 = (Logger logger) -> {
		String tabname = "Berufskolleg_Berufsebenen2";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		BerufskollegBerufsebene2[] values = BerufskollegBerufsebene2.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			BerufskollegBerufsebene2 bkebene = values[i];
			for (BerufskollegBerufsebeneKatalogEintrag ebene : bkebene.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(ebene.id).append(",");
				sql.append("'").append(ebene.kuerzel).append("'").append(",");
				sql.append("'").append(ebene.bezeichnung).append("'").append(",");
				sql.append(ebene.gueltigVon).append(",");
				sql.append(ebene.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, BerufskollegBerufsebene2.class.getCanonicalName(), BerufskollegBerufsebene2.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type BerufskollegBerufsebene3 
	 */
	private Consumer<Logger> updateBerufskollegBerufsebene3 = (Logger logger) -> {
		String tabname = "Berufskolleg_Berufsebenen3";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		BerufskollegBerufsebene3[] values = BerufskollegBerufsebene3.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			BerufskollegBerufsebene3 bkebene = values[i];
			for (BerufskollegBerufsebeneKatalogEintrag ebene : bkebene.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(ebene.id).append(",");
				sql.append("'").append(ebene.kuerzel).append("'").append(",");
				sql.append("'").append(ebene.bezeichnung).append("'").append(",");
				sql.append(ebene.gueltigVon).append(",");
				sql.append(ebene.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, BerufskollegBerufsebene3.class.getCanonicalName(), BerufskollegBerufsebene3.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type BerufskollegFachklassenManager
	 */
	private Consumer<Logger> updateBerufskollegFachklassen = (Logger logger) -> {
		String tabname = "Berufskolleg_Fachklassen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, FachklassenIndex, Schluessel, Schluessel2, IstAusgelaufen, BerufsfeldGruppe, Berufsfeld, Berufsebene1, Berufsebene2, Berufsebene3, Bezeichnung, BezeichnungM, BezeichnungW, gueltigVon, gueltigBis) ");
		BerufskollegFachklassenManager manager = JsonDaten.fachklassenManager;
		boolean isFirst = true;
		for (BerufskollegFachklassenKatalogIndex katIndex : manager.getKatalog().indizes) {
			for (BerufskollegFachklassenKatalogEintrag eintrag : katIndex.fachklassen) {
				for (BerufskollegFachklassenKatalogDaten daten : eintrag.historie) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(daten.id).append(",");
					sql.append(katIndex.index).append(",");
					sql.append("'").append(eintrag.schluessel).append("'").append(",");
					sql.append("'").append(eintrag.schluessel2).append("'").append(",");
					sql.append(daten.istAusgelaufen ? 1 : 0).append(",");
					sql.append("'").append(daten.berufsfeldGruppe).append("'").append(",");
					sql.append("'").append(daten.berufsfeld).append("'").append(",");
					sql.append("'").append(daten.ebene1).append("'").append(",");
					sql.append("'").append(daten.ebene2).append("'").append(",");
					sql.append("'").append(daten.ebene3).append("'").append(",");
					sql.append("'").append(daten.bezeichnung).append("'").append(",");
					sql.append("'").append(daten.bezeichnungM).append("'").append(",");
					sql.append("'").append(daten.bezeichnungW).append("'").append(",");
					sql.append(daten.gueltigVon).append(",");
					sql.append(daten.gueltigBis).append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, BerufskollegFachklassenManager.class.getCanonicalName(), JsonDaten.fachklassenManager.getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type BerufskollegFachklassen 
	 */
	private Consumer<Logger> updateBerufskollegFachklassenKeys = (Logger logger) -> {
		String tabname = "Berufskolleg_Fachklassen_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(FachklassenIndex, Schluessel, Schluessel2) ");
		BerufskollegFachklassenManager manager = JsonDaten.fachklassenManager;
		boolean isFirst = true;
		for (BerufskollegFachklassenKatalogIndex katIndex : manager.getKatalog().indizes) {
			for (BerufskollegFachklassenKatalogEintrag eintrag : katIndex.fachklassen) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(katIndex.index).append(",");
				sql.append("'").append(eintrag.schluessel).append("'").append(",");
				sql.append("'").append(eintrag.schluessel2).append("'").append(")");
			}
		}
		updateCoreTypeTabelle(tabname, BerufskollegFachklassenManager.class.getCanonicalName(), JsonDaten.fachklassenManager.getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Fachgruppe 
	 */
	private Consumer<Logger> updateFachgruppen = (Logger logger) -> {
		String tabname = "Fachgruppen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Fachbereich, SchildFgID, FG_Bezeichnung, FG_Kuerzel, Schulformen, FarbeR, FarbeG, FarbeB, Sortierung, FuerZeugnis, gueltigVon, gueltigBis) ");
		Fachgruppe[] values = Fachgruppe.values();
		for (int i = 0; i < values.length; i++) {
			Fachgruppe f = values[i];
			sql.append(i == 0 ? "VALUES (" : ", (");
			sql.append(f.id).append(",");
			sql.append(f.fachbereich).append(",");
			sql.append(f.idSchild).append(",");
			sql.append("'").append(f.bezeichnung).append("'").append(",");
			sql.append("'").append(f.kuerzel).append("'").append(",");
			sql.append("'").append(f.getSchulformen().stream().map(s -> s.daten.kuerzel).collect(Collectors.joining(","))).append("'").append(",");
			sql.append(f.farbe.getRed()).append(",");
			sql.append(f.farbe.getGreen()).append(",");
			sql.append(f.farbe.getBlue()).append(",");
			sql.append(f.sortierung).append(",");
			sql.append(f.fuerZeugnis ? 1 : 0).append(",");
			sql.append(f.gueltigVon).append(",");
			sql.append(f.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(tabname, KursFortschreibungsart.class.getCanonicalName(), KursFortschreibungsart.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Foerderschwerpunkt 
	 */
	private Consumer<Logger> updateFoerderschwerpunkte = (Logger logger) -> {
		String tabname = "Foerderschwerpunkte";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Beschreibung, gueltigVon, gueltigBis) ");
		Foerderschwerpunkt[] values = Foerderschwerpunkt.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Foerderschwerpunkt foerderschwerpunkt = values[i];
			for (FoerderschwerpunktKatalogEintrag fs : foerderschwerpunkt.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(fs.id).append(",");
				sql.append("'").append(fs.kuerzel).append("'").append(",");
				sql.append("'").append(fs.beschreibung).append("'").append(",");
				sql.append(fs.gueltigVon).append(",");
				sql.append(fs.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Foerderschwerpunkt.class.getCanonicalName(), Foerderschwerpunkt.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Foerderschwerpunkt 
	 */
	private Consumer<Logger> updateFoerderschwerpunkteSchulformen = (Logger logger) -> {
		String tabname = "Foerderschwerpunkte_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Foerderschwerpunkt_ID, Schulform_Kuerzel) ");
		Foerderschwerpunkt[] values = Foerderschwerpunkt.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Foerderschwerpunkt foerderschwerpunkt = values[i];
			for (FoerderschwerpunktKatalogEintrag fs : foerderschwerpunkt.historie) {
				List<Schulform> schulformen = fs.schulformen.stream().map(s -> Schulform.getByKuerzel(s)).collect(Collectors.toList());
				for (Schulform sf : schulformen) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(fs.id).append(",");
					sql.append("'").append(sf.daten.kuerzel).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Foerderschwerpunkt.class.getCanonicalName(), Foerderschwerpunkt.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Jahrgaenge 
	 */
	private Consumer<Logger> updateJahrgaenge = (Logger logger) -> {
		String tabname = "Jahrgaenge";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, gueltigVon, gueltigBis) ");
		Jahrgaenge[] values = Jahrgaenge.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Jahrgaenge jahrgaenge = values[i];
			for (JahrgangsKatalogEintrag jg : jahrgaenge.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(jg.id).append(",");
				sql.append("'").append(jg.kuerzel).append("'").append(",");
				sql.append(jg.gueltigVon).append(",");
				sql.append(jg.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Jahrgaenge.class.getCanonicalName(), Jahrgaenge.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Jahrgaenge 
	 */
	private Consumer<Logger> updateJahrgaengeBezeichnungen = (Logger logger) -> {
		String tabname = "Jahrgaenge_Bezeichnungen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Jahrgang_ID, Schulform_Kuerzel, Bezeichnung) ");
		Jahrgaenge[] values = Jahrgaenge.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Jahrgaenge jahrgaenge = values[i];
			for (JahrgangsKatalogEintrag jg : jahrgaenge.historie) {
				for (JahrgangsKatalogEintragBezeichnung jgb : jg.bezeichnungen) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(jg.id).append(",");
					sql.append("'").append(jgb.schulform).append("'").append(",");
					sql.append("'").append(jgb.bezeichnung).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Jahrgaenge.class.getCanonicalName(), Jahrgaenge.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Jahrgaenge 
	 */
	private Consumer<Logger> updateJahrgaengeKeys = (Logger logger) -> {
		String tabname = "Jahrgaenge_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		Jahrgaenge[] values = Jahrgaenge.values();
		String[] jg_kuerzel = Arrays.stream(values).flatMap(jg -> Arrays.stream(jg.historie)).map(jg -> jg.kuerzel).collect(Collectors.toSet()).toArray(new String[0]);
		boolean isFirst = true;
		for (int i = 0; i < jg_kuerzel.length; i++) {
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(jg_kuerzel[i]).append("'").append(")");
		}
		updateCoreTypeTabelle(tabname, Jahrgaenge.class.getCanonicalName(), Jahrgaenge.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type KursFortschreibungsart 
	 */
	private Consumer<Logger> updateKursFortschreibungsarten = (Logger logger) -> {
		String tabname = "KursFortschreibungsarten";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		KursFortschreibungsart[] values = KursFortschreibungsart.values();
		for (int i = 0; i < values.length; i++) {
			KursFortschreibungsart p = values[i];
			sql.append(i == 0 ? "VALUES (" : ", (");
			sql.append(p.id).append(",");
			sql.append("'").append(p.kuerzel).append("'").append(",");
			sql.append("'").append(p.beschreibung).append("'").append(",");
			sql.append(p.gueltigVon).append(",");
			sql.append(p.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(tabname, KursFortschreibungsart.class.getCanonicalName(), KursFortschreibungsart.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerAbgangsgrund 
	 */
	private Consumer<Logger> updateLehrerAbgangsgruende = (Logger logger) -> {
		String tabname = "Statkue_LehrerAbgang";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, ASDSchluessel, gueltigVon, gueltigBis) ");
		LehrerAbgangsgrund[] values = LehrerAbgangsgrund.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerAbgangsgrund grund = values[i];
			for (LehrerKatalogAbgangsgrundEintrag l : grund.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id * 100).append(",");
				sql.append("'").append(l.schluessel).append("'").append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerAbgangsgrund.class.getCanonicalName(), LehrerAbgangsgrund.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerAnrechnungsgrund 
	 */
	private Consumer<Logger> updateLehrerAnrechnungsgruende = (Logger logger) -> {
		String tabname = "Statkue_LehrerAnrechnung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerAnrechnungsgrund[] values = LehrerAnrechnungsgrund.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerAnrechnungsgrund grund = values[i];
			for (LehrerKatalogAnrechnungsgrundEintrag l : grund.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerAnrechnungsgrund.class.getCanonicalName(), LehrerAnrechnungsgrund.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerBeschaeftigungsart 
	 */
	private Consumer<Logger> updateLehrerBeschaeftigungsarten = (Logger logger) -> {
		String tabname = "Statkue_LehrerBeschaeftigungsart";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerBeschaeftigungsart[] values = LehrerBeschaeftigungsart.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerBeschaeftigungsart art = values[i];
			for (LehrerKatalogBeschaeftigungsartEintrag l : art.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerBeschaeftigungsart.class.getCanonicalName(), LehrerBeschaeftigungsart.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerEinsatzstatus 
	 */
	private Consumer<Logger> updateLehrerEinsatzstatus = (Logger logger) -> {
		String tabname = "Statkue_LehrerEinsatzstatus";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerEinsatzstatus[] values = LehrerEinsatzstatus.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerEinsatzstatus status = values[i];
			for (LehrerKatalogEinsatzstatusEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerEinsatzstatus.class.getCanonicalName(), LehrerEinsatzstatus.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerFachrichtungAnerkennung 
	 */
	private Consumer<Logger> updateLehrerFachrichtungAnerkennungen = (Logger logger) -> {
		String tabname = "Statkue_LehrerFachrAnerkennung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerFachrichtungAnerkennung[] values = LehrerFachrichtungAnerkennung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerFachrichtungAnerkennung status = values[i];
			for (LehrerKatalogFachrichtungAnerkennungEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerFachrichtungAnerkennung.class.getCanonicalName(), LehrerFachrichtungAnerkennung.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerFachrichtung 
	 */
	private Consumer<Logger> updateLehrerFachrichtungen = (Logger logger) -> {
		String tabname = "Statkue_LehrerFachrichtung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerFachrichtung[] values = LehrerFachrichtung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerFachrichtung status = values[i];
			for (LehrerKatalogFachrichtungEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerFachrichtung.class.getCanonicalName(), LehrerFachrichtung.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerLehramt 
	 */
	private Consumer<Logger> updateLehreraemter = (Logger logger) -> {
		String tabname = "Statkue_LehrerLehramt";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerLehramt[] values = LehrerLehramt.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerLehramt status = values[i];
			for (LehrerKatalogLehramtEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerLehramt.class.getCanonicalName(), LehrerLehramt.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerLehramtAnerkennung 
	 */
	private Consumer<Logger> updateLehreramtAnerkennungen = (Logger logger) -> {
		String tabname = "Statkue_LehrerLehramtAnerkennung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerLehramtAnerkennung[] values = LehrerLehramtAnerkennung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerLehramtAnerkennung status = values[i];
			for (LehrerKatalogLehramtAnerkennungEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerLehramtAnerkennung.class.getCanonicalName(), LehrerLehramtAnerkennung.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerLehrbefaehigung 
	 */
	private Consumer<Logger> updateLehrbefaehigungen = (Logger logger) -> {
		String tabname = "Statkue_LehrerLehrbefaehigung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerLehrbefaehigung[] values = LehrerLehrbefaehigung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerLehrbefaehigung status = values[i];
			for (LehrerKatalogLehrbefaehigungEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerLehrbefaehigung.class.getCanonicalName(), LehrerLehrbefaehigung.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerLehrbefaehigungAnerkennung 
	 */
	private Consumer<Logger> updateLehrbefaehigungAnerkennungen = (Logger logger) -> {
		String tabname = "Statkue_LehrerLehrbefAnerkennung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerLehrbefaehigungAnerkennung[] values = LehrerLehrbefaehigungAnerkennung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerLehrbefaehigungAnerkennung status = values[i];
			for (LehrerKatalogLehrbefaehigungAnerkennungEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerLehrbefaehigungAnerkennung.class.getCanonicalName(), LehrerLehrbefaehigungAnerkennung.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerMehrleistungArt 
	 */
	private Consumer<Logger> updateLehrerMehrleisungsarten = (Logger logger) -> {
		String tabname = "Statkue_LehrerMehrleistung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerMehrleistungArt[] values = LehrerMehrleistungArt.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerMehrleistungArt status = values[i];
			for (LehrerKatalogMehrleistungsartEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerMehrleistungArt.class.getCanonicalName(), LehrerMehrleistungArt.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerMinderleistungArt 
	 */
	private Consumer<Logger> updateLehrerMinderleisungsarten = (Logger logger) -> {
		String tabname = "Statkue_LehrerMinderleistung";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerMinderleistungArt[] values = LehrerMinderleistungArt.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerMinderleistungArt status = values[i];
			for (LehrerKatalogMinderleistungsartEintrag l : status.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerMinderleistungArt.class.getCanonicalName(), LehrerMinderleistungArt.VERSION, sql.toString());
	};
	

	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerRechtsverhaeltnis 
	 */
	private Consumer<Logger> updateLehrerRechtsverhaeltnisse = (Logger logger) -> {
		String tabname = "Statkue_LehrerRechtsverhaeltnis";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, gueltigVon, gueltigBis) ");
		LehrerRechtsverhaeltnis[] values = LehrerRechtsverhaeltnis.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerRechtsverhaeltnis art = values[i];
			for (LehrerKatalogRechtsverhaeltnisEintrag l : art.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id*100).append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerRechtsverhaeltnis.class.getCanonicalName(), LehrerRechtsverhaeltnis.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerZugangsgrund 
	 */
	private Consumer<Logger> updateLehrerZugangsgruende = (Logger logger) -> {
		String tabname = "Statkue_LehrerZugang";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kurztext, Langtext, Beginn, Ende, Sort, ASDSchluessel, gueltigVon, gueltigBis) ");
		LehrerZugangsgrund[] values = LehrerZugangsgrund.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			LehrerZugangsgrund grund = values[i];
			for (LehrerKatalogZugangsgrundEintrag l : grund.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(l.id).append(",");
				sql.append("'").append(l.kuerzel).append("'").append(",");
				sql.append("'").append(l.text).append("'").append(",");
				sql.append("null,");
				sql.append("null,");
				sql.append(l.id * 100).append(",");
				sql.append("'").append(l.schluessel).append("'").append(",");
				sql.append(l.gueltigVon).append(",");
				sql.append(l.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, LehrerZugangsgrund.class.getCanonicalName(), LehrerZugangsgrund.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Nationalitaeten 
	 */
	private Consumer<Logger> updateNationalitaeten = (Logger logger) -> {
		String tabname = "Nationalitaeten";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Iso3Code, Iso2Code, Iso3Numerisch, DEStatisCode, BezeichnungSuche, Bezeichnung, BezeichnungLang, Staatsangehoerigkeit, gueltigVon, gueltigBis) ");
		Nationalitaeten[] values = Nationalitaeten.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Nationalitaeten nationalitaeten = values[i];
			NationalitaetenKatalogEintrag nat = nationalitaeten.daten;
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append(nat.id).append(",");
			sql.append("'").append(nat.iso3).append("'").append(",");
			sql.append("'").append(nat.iso2).append("'").append(",");
			if (nat.isoNumerisch == null)
				sql.append("null,");
			else
				sql.append("'").append(nat.isoNumerisch).append("'").append(",");
			sql.append("'").append(nat.codeDEStatis).append("'").append(",");
			sql.append("'").append(nat.bezeichnungSuche).append("'").append(",");
			sql.append("'").append(nat.bezeichnung).append("'").append(",");
			sql.append("'").append(nat.bezeichnungLang).append("'").append(",");
			sql.append("'").append(nat.staatsangehoerigkeit).append("'").append(",");
			sql.append(nat.gueltigVon).append(",");
			sql.append(nat.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(tabname, Nationalitaeten.class.getCanonicalName(), Nationalitaeten.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Nationalitaeten 
	 */
	private Consumer<Logger> updateNationalitaeten_Keys = (Logger logger) -> {
		String tabname = "Nationalitaeten_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(DEStatisCode) ");
		List<String> codes = Arrays.stream(Nationalitaeten.values()).map(nat -> nat.daten.codeDEStatis).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(code).append("')");
		}
		updateCoreTypeTabelle(tabname, Nationalitaeten.class.getCanonicalName(), Nationalitaeten.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Note 
	 */
	private Consumer<Logger> updateNoten = (Logger logger) -> {
		String tabname = "Noten";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, IstTendenznote, Text, AufZeugnis, Notenpunkte, TextLaufbahnSII, AufLaufbahnSII, Sortierung, gueltigVon, gueltigBis) ");
		Note[] values = Note.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Note n = values[i];
			if (n == Note.KEINE)
				continue;
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append(n.id).append(",");
			sql.append("'").append(n.kuerzel).append("'").append(",");
			sql.append(n.hatTendenz()).append(",");
			sql.append("'").append(n.text).append("'").append(",");
			sql.append("--------------------".equals(n.textZeugnis)).append(",");
			sql.append(n.notenpunkte).append(",");
			if (n.notenpunkte != null)
				sql.append("'").append(String.format("%02d", n.notenpunkte)).append("'").append(",");
			else if ((n == Note.E3_TEILGENOMMEN) || (n == Note.E2_MIT_ERFOLG_TEILGENOMMEN) || n == (Note.E3_TEILGENOMMEN) || (n == Note.ATTEST))
				sql.append("'").append(n.kuerzel).append("'").append(",");
			else
				sql.append("null,");
			sql.append((n.notenpunkte != null) || 
					   (n == Note.E3_TEILGENOMMEN) || 
					   (n == Note.E2_MIT_ERFOLG_TEILGENOMMEN) || 
					   (n == (Note.E3_TEILGENOMMEN)) || 
					   (n == Note.ATTEST)).append(",");
			sql.append(n.sortierung).append(",");
			sql.append(n.gueltigVon).append(",");
			sql.append(n.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(tabname, Note.class.getCanonicalName(), Note.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type PersonalTyp 
	 */
	private Consumer<Logger> updatePersonalTypen = (Logger logger) -> {
		String tabname = "PersonalTypen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		PersonalTyp[] values = PersonalTyp.values();
		for (int i = 0; i < values.length; i++) {
			PersonalTyp p = values[i];
			sql.append(i == 0 ? "VALUES (" : ", (");
			sql.append(p.id).append(",");
			sql.append("'").append(p.kuerzel).append("'").append(",");
			sql.append("'").append(p.bezeichnung).append("'").append(",");
			sql.append(p.gueltigVon).append(",");
			sql.append(p.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(tabname, PersonalTyp.class.getCanonicalName(), PersonalTyp.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type SchuelerStatus 
	 */
	private Consumer<Logger> updateSchuelerStatus = (Logger logger) -> {
		String tabname = "SchuelerStatus";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Bezeichnung, gueltigVon, gueltigBis) ");
		SchuelerStatus[] values = SchuelerStatus.values();
		for (int i = 0; i < values.length; i++) {
			SchuelerStatus p = values[i];
			sql.append(i == 0 ? "VALUES (" : ", (");
			sql.append(p.id).append(",");
			sql.append("'").append(p.bezeichnung).append("'").append(",");
			sql.append(p.gueltigVon).append(",");
			sql.append(p.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(tabname, SchuelerStatus.class.getCanonicalName(), SchuelerStatus.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type SchulabschlussAllgemeinbildend 
	 */
	private Consumer<Logger> updateSchulabschluesseAllgemeinbildend = (Logger logger) -> {
		String tabname = "Schulabschluesse_Allgemeinbildend";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, Kuerzel_Statistik, gueltigVon, gueltigBis) ");
		SchulabschlussAllgemeinbildend[] values = SchulabschlussAllgemeinbildend.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			SchulabschlussAllgemeinbildend abschlussart = values[i];
			for (SchulabschlussAllgemeinbildendKatalogEintrag sf : abschlussart.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(sf.id).append(",");
				sql.append("'").append(sf.kuerzel).append("'").append(",");
				sql.append("'").append(sf.bezeichnung).append("'").append(",");
				sql.append("'").append(sf.kuerzelStatistik).append("'").append(",");
				sql.append(sf.gueltigVon).append(",");
				sql.append(sf.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, SchulabschlussAllgemeinbildend.class.getCanonicalName(), SchulabschlussAllgemeinbildend.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type SchulabschlussBerufsbildend 
	 */
	private Consumer<Logger> updateSchulabschluesseBerufsbildend = (Logger logger) -> {
		String tabname = "Schulabschluesse_Berufsbildend";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, Kuerzel_Statistik, gueltigVon, gueltigBis) ");
		SchulabschlussBerufsbildend[] values = SchulabschlussBerufsbildend.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			SchulabschlussBerufsbildend abschlussart = values[i];
			for (SchulabschlussBerufsbildendKatalogEintrag sf : abschlussart.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(sf.id).append(",");
				sql.append("'").append(sf.kuerzel).append("'").append(",");
				sql.append("'").append(sf.bezeichnung).append("'").append(",");
				sql.append("'").append(sf.kuerzelStatistik).append("'").append(",");
				sql.append(sf.gueltigVon).append(",");
				sql.append(sf.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, SchulabschlussBerufsbildend.class.getCanonicalName(), SchulabschlussBerufsbildend.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Schulform 
	 */
	private Consumer<Logger> updateSchulformen = (Logger logger) -> {
		String tabname = "Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Nummer, Bezeichnung, HatGymOb, gueltigVon, gueltigBis) ");
		Schulform[] values = Schulform.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Schulform schulform = values[i];
			for (SchulformKatalogEintrag sf : schulform.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(sf.id).append(",");
				sql.append("'").append(sf.kuerzel).append("'").append(",");
				if (sf.nummer == null)
					sql.append("null,");
				else
					sql.append("'").append(sf.nummer).append("'").append(",");
				sql.append("'").append(sf.bezeichnung).append("'").append(",");
				sql.append(sf.hatGymOb).append(",");
				sql.append(sf.gueltigVon).append(",");
				sql.append(sf.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Schulform.class.getCanonicalName(), Schulform.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Schulform 
	 */
	private Consumer<Logger> updateSchulgliederungen = (Logger logger) -> {
		String tabname = "Schulgliederungen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, IstBKBildungsgang, IstAuslaufend, IstAusgelaufen, Bezeichnung, BKAnlage, BKTyp, BKIndex, IstVZ, gueltigVon, gueltigBis) ");
		Schulgliederung[] values = Schulgliederung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Schulgliederung schulgliederung = values[i];
			for (SchulgliederungKatalogEintrag sg : schulgliederung.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(sg.id).append(",");
				sql.append("'").append(sg.kuerzel).append("'").append(",");
				sql.append(sg.istBK).append(",");
				sql.append(sg.istAuslaufend).append(",");
				sql.append(sg.istAusgelaufen).append(",");
				sql.append("'").append(sg.beschreibung).append("'").append(",");
				if (sg.bkAnlage == null)
					sql.append("null,");
				else
					sql.append("'").append(sg.bkAnlage).append("'").append(",");
				if (sg.bkTyp == null)
					sql.append("null,");
				else
					sql.append("'").append(sg.bkTyp).append("'").append(",");
				if (sg.bkIndex == null)
					sql.append("null,");
				else
					sql.append("'").append(sg.bkIndex).append("'").append(",");
				sql.append(sg.istVZ).append(",");
				sql.append(sg.gueltigVon).append(",");
				sql.append(sg.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Schulgliederung.class.getCanonicalName(), Schulgliederung.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Schulgliederung 
	 */
	private Consumer<Logger> updateSchulgliederungenSchulformen = (Logger logger) -> {
		String tabname = "Schulgliederungen_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Schulgliederung_ID, Schulform_Kuerzel) ");
		Schulgliederung[] values = Schulgliederung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Schulgliederung schulgliederung = values[i];
			for (SchulgliederungKatalogEintrag sg : schulgliederung.historie) {
				List<Schulform> schulformen = sg.schulformen.stream().map(s -> Schulform.getByKuerzel(s)).collect(Collectors.toList());
				for (Schulform sf : schulformen) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(sg.id).append(",");
					sql.append("'").append(sf.daten.kuerzel).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Schulgliederung.class.getCanonicalName(), Schulgliederung.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Schulgliederung 
	 */
	private Consumer<Logger> updateSchulgliederungenAbschluesseAllg = (Logger logger) -> {
		String tabname = "Schulgliederungen_Abschluesse_Allgemeinbildend";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Schulgliederung_ID, Abschluss_Kuerzel) ");
		Schulgliederung[] values = Schulgliederung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Schulgliederung schulgliederung = values[i];
			for (SchulgliederungKatalogEintrag sg : schulgliederung.historie) {
				List<SchulabschlussAllgemeinbildend> abschlussarten = sg.bkAbschlussAllgemeinbildend.stream().map(s -> SchulabschlussAllgemeinbildend.getByKuerzel(s)).collect(Collectors.toList());
				for (SchulabschlussAllgemeinbildend aa : abschlussarten) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(sg.id).append(",");
					sql.append("'").append(aa.daten.kuerzel).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Schulgliederung.class.getCanonicalName(), Schulgliederung.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Schulgliederung 
	 */
	private Consumer<Logger> updateSchulgliederungenAbschluesseBeruf = (Logger logger) -> {
		String tabname = "Schulgliederungen_Abschluesse_Berufsbildend";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Schulgliederung_ID, Abschluss_Kuerzel) ");
		Schulgliederung[] values = Schulgliederung.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Schulgliederung schulgliederung = values[i];
			for (SchulgliederungKatalogEintrag sg : schulgliederung.historie) {
				List<SchulabschlussBerufsbildend> abschlussarten = sg.bkAbschlussBerufsbildend.stream().map(s -> SchulabschlussBerufsbildend.getByKuerzel(s)).collect(Collectors.toList());
				for (SchulabschlussBerufsbildend aa : abschlussarten) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(sg.id).append(",");
					sql.append("'").append(aa.daten.kuerzel).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Schulgliederung.class.getCanonicalName(), Schulgliederung.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Verkehrssprache 
	 */
	private Consumer<Logger> updateVerkehrssprachen = (Logger logger) -> {
		String tabname = "Verkehrssprachen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Iso3, Iso2, Bezeichnung) ");
		Verkehrssprache[] values = Verkehrssprache.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Verkehrssprache sprache = values[i];
			VerkehrsspracheKatalogEintrag spr = sprache.daten;
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append(spr.id).append(",");
			sql.append("'").append(spr.kuerzel).append("'").append(",");
			if (spr.iso2 == null)
				sql.append("'").append(Verkehrssprache.UND.daten.iso2).append("'").append(",");
			else
				sql.append("'").append(spr.iso2).append("'").append(",");
			sql.append("'").append(spr.bezeichnung.replace("'", "''")).append("'").append(")");
		}
		updateCoreTypeTabelle(tabname, Verkehrssprache.class.getCanonicalName(), Verkehrssprache.VERSION, sql.toString());
	};

	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunft 
	 */
	private Consumer<Logger> updateHerkuenfte = (Logger logger) -> {
		String tabname = "Herkunft";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Beschreibung, gueltigVon, gueltigBis) ");
		Herkunft[] values = Herkunft.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Herkunft herkunft = values[i];
			for (HerkunftKatalogEintrag h : herkunft.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(h.id).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append("'").append(h.beschreibung.replace("'", "''")).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Herkunft.class.getCanonicalName(), Herkunft.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunft 
	 */
	private Consumer<Logger> updateHerkuenfteKeys = (Logger logger) -> {
		String tabname = "Herkunft_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(Herkunft.values()).map(h -> h.daten.kuerzel).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, Herkunft.class.getCanonicalName(), Herkunft.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunft 
	 */
	private Consumer<Logger> updateHerkuenfteSchulformen = (Logger logger) -> {
		String tabname = "Herkunft_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Herkunft_ID, Schulform_Kuerzel) ");
		Herkunft[] values = Herkunft.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Herkunft herkunft = values[i];
			for (HerkunftKatalogEintrag h : herkunft.historie) {
				List<Schulform> schulformen = h.schulformen.stream().map(s -> Schulform.getByKuerzel(s)).collect(Collectors.toList());
				for (Schulform sf : schulformen) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(h.id).append(",");
					sql.append("'").append(sf.daten.kuerzel).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Herkunft.class.getCanonicalName(), Herkunft.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunftsarten
	 */
	private Consumer<Logger> updateHerkunftsarten = (Logger logger) -> {
		String tabname = "Herkunftsart";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, gueltigVon, gueltigBis) ");
		Herkunftsarten[] values = Herkunftsarten.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Herkunftsarten herkunft = values[i];
			for (HerkunftsartKatalogEintrag h : herkunft.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(h.id).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Herkunftsarten.class.getCanonicalName(), Herkunftsarten.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunftsarten 
	 */
	private Consumer<Logger> updateHerkunftsartenKeys = (Logger logger) -> {
		String tabname = "Herkunftsart_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(Herkunftsarten.values()).map(h -> h.daten.kuerzel).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, Herkunftsarten.class.getCanonicalName(), Herkunftsarten.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunftsarten
	 */
	private Consumer<Logger> updateHerkunftsartenSchulformen = (Logger logger) -> {
		String tabname = "Herkunftsart_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Herkunftsart_ID, Schulform_Kuerzel, KurzBezeichnung, Bezeichnung) ");
		Herkunftsarten[] values = Herkunftsarten.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Herkunftsarten herkunftsart = values[i];
			for (HerkunftsartKatalogEintrag h : herkunftsart.historie) {
				for (HerkunftsartKatalogEintragBezeichnung hb : h.bezeichnungen) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(h.id).append(",");
					sql.append("'").append(Schulform.getByKuerzel(hb.schulform).daten.kuerzel).append("'").append(",");
					sql.append("'").append(hb.kurzBezeichnung.replace("'", "''")).append("'").append(",");
					sql.append("'").append(hb.bezeichnung.replace("'", "''")).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Herkunftsarten.class.getCanonicalName(), Herkunftsarten.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Klassenart
	 */
	private Consumer<Logger> updateKlassenarten = (Logger logger) -> {
		String tabname = "KlassenartenKatalog";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		Klassenart[] values = Klassenart.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Klassenart klassenart = values[i];
			for (KlassenartKatalogEintrag k : klassenart.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(k.id).append(",");
				sql.append("'").append(k.kuerzel).append("'").append(",");
				sql.append("'").append(k.bezeichnung).append("'").append(",");
				sql.append(k.gueltigVon).append(",");
				sql.append(k.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Klassenart.class.getCanonicalName(), Klassenart.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Klassenart 
	 */
	private Consumer<Logger> updateKlassenartenKeys = (Logger logger) -> {
		String tabname = "KlassenartenKatalog_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(Klassenart.values()).map(h -> h.daten.kuerzel).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, Klassenart.class.getCanonicalName(), Klassenart.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Klassenart
	 */
	private Consumer<Logger> updateKlassenartenSchulformen = (Logger logger) -> {
		String tabname = "KlassenartenKatalog_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Klassenart_ID, Schulform_Kuerzel, Schulgliederung_Kuerzel) ");
		Klassenart[] values = Klassenart.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Klassenart klassenart = values[i];
			for (KlassenartKatalogEintrag k : klassenart.historie) {
				for (SchulformSchulgliederung sfsg : k.zulaessig) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(k.id).append(",");
					if (sfsg.schulform == null)
						sql.append("null,");
					else
						sql.append("'").append(Schulform.getByKuerzel(sfsg.schulform).daten.kuerzel).append("'").append(",");
					if (sfsg.gliederung == null)
						sql.append("'')");
					else
						sql.append("'").append(Schulgliederung.getByKuerzel(sfsg.gliederung)).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, Klassenart.class.getCanonicalName(), Klassenart.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigeKursart
	 */
	private Consumer<Logger> updateKursarten = (Logger logger) -> {
		String tabname = "KursartenKatalog";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Nummer, Bezeichnung, Bemerkungen, KuerzelAllg, BezeichnungAllg, ErlaubtGOSt, gueltigVon, gueltigBis) ");
		ZulaessigeKursart[] values = ZulaessigeKursart.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			ZulaessigeKursart kursart = values[i];
			for (KursartKatalogEintrag k : kursart.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(k.id).append(",");
				sql.append("'").append(k.kuerzel).append("'").append(",");
				sql.append("'").append(k.nummer).append("'").append(",");
				sql.append("'").append(k.bezeichnung).append("'").append(",");
				if (k.bemerkungen == null)
					sql.append("null,");
				else
					sql.append("'").append(k.bemerkungen).append("'").append(",");
				if (k.kuerzelAllg == null)
					sql.append("null,");
				else
					sql.append("'").append(k.kuerzelAllg).append("'").append(",");
				if (k.bezeichnungAllg == null)
					sql.append("null,");
				else
					sql.append("'").append(k.bezeichnungAllg).append("'").append(",");
				sql.append(k.erlaubtGOSt ? 1 : 0).append(",");
				sql.append(k.gueltigVon).append(",");
				sql.append(k.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, ZulaessigeKursart.class.getCanonicalName(), ZulaessigeKursart.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigeKursart 
	 */
	private Consumer<Logger> updateKursartenKeys = (Logger logger) -> {
		String tabname = "KursartenKatalog_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(ZulaessigeKursart.values()).map(h -> h.daten.kuerzel).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, ZulaessigeKursart.class.getCanonicalName(), ZulaessigeKursart.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigeKursart
	 */
	private Consumer<Logger> updateKursartenSchulformen = (Logger logger) -> {
		String tabname = "KursartenKatalog_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kursart_ID, Schulform_Kuerzel, Schulgliederung_Kuerzel) ");
		ZulaessigeKursart[] values = ZulaessigeKursart.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			ZulaessigeKursart kursart = values[i];
			for (KursartKatalogEintrag k : kursart.historie) {
				for (SchulformSchulgliederung sfsg : k.zulaessig) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(k.id).append(",");
					if (sfsg.schulform == null)
						sql.append("null,");
					else
						sql.append("'").append(Schulform.getByKuerzel(sfsg.schulform).daten.kuerzel).append("'").append(",");
					if (sfsg.gliederung == null)
						sql.append("'')");
					else
						sql.append("'").append(Schulgliederung.getByKuerzel(sfsg.gliederung)).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, ZulaessigeKursart.class.getCanonicalName(), ZulaessigeKursart.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigesFach
	 */
	private Consumer<Logger> updateZulaessigeFaecher = (Logger logger) -> {
		String tabname = "FachKatalog";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, KuerzelASD, Bezeichnung, Kuerzel, Aufgabenfeld, Fachgruppe, JahrgangAb, IstFremdsprache, IstHKFS, IstAusRegUFach, IstErsatzPflichtFS, IstKonfKoop, NurSII, ExportASD, gueltigVon, gueltigBis) ");
		ZulaessigesFach[] values = ZulaessigesFach.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			ZulaessigesFach fach = values[i];
			for (FachKatalogEintrag f : fach.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(f.id).append(",");
				sql.append("'").append(f.kuerzelASD).append("'").append(",");
				sql.append("'").append(f.bezeichnung.replace("'", "''")).append("'").append(",");
				sql.append("'").append(f.kuerzel).append("'").append(",");
				if (f.aufgabenfeld == null)
					sql.append("null,");
				else
					sql.append(f.aufgabenfeld).append(",");
				if (f.fachgruppe == null)
					sql.append("null,");
				else
					sql.append("'").append(f.fachgruppe).append("'").append(",");
				if (f.abJahrgang == null)
					sql.append("null,");
				else
					sql.append("'").append(f.abJahrgang).append("'").append(",");
				sql.append(f.istFremdsprache ? 1 : 0).append(",");
				sql.append(f.istHKFS ? 1 : 0).append(",");
				sql.append(f.istAusRegUFach ? 1 : 0).append(",");
				sql.append(f.istErsatzPflichtFS ? 1 : 0).append(",");
				sql.append(f.istKonfKoop ? 1 : 0).append(",");
				sql.append(f.nurSII ? 1 : 0).append(",");
				sql.append(f.exportASD ? 1 : 0).append(",");
				sql.append(f.gueltigVon).append(",");
				sql.append(f.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, ZulaessigesFach.class.getCanonicalName(), ZulaessigesFach.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigesFach 
	 */
	private Consumer<Logger> updateZulaessigeFaecherKeys = (Logger logger) -> {
		String tabname = "FachKatalog_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(ZulaessigesFach.values()).map(h -> h.daten.kuerzelASD).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, ZulaessigesFach.class.getCanonicalName(), ZulaessigesFach.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigesFach
	 */
	private Consumer<Logger> updateZulaessigeFaecherSchulformen = (Logger logger) -> {
		String tabname = "FachKatalog_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Fach_ID, Schulform_Kuerzel, Schulgliederung_Kuerzel) ");
		ZulaessigesFach[] values = ZulaessigesFach.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			ZulaessigesFach fach = values[i];
			for (FachKatalogEintrag f : fach.historie) {
				for (SchulformSchulgliederung sfsg : f.zulaessig) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(f.id).append(",");
					if (sfsg.schulform == null)
						sql.append("null,");
					else
						sql.append("'").append(Schulform.getByKuerzel(sfsg.schulform).daten.kuerzel).append("'").append(",");
					if (sfsg.gliederung == null)
						sql.append("'')");
					else
						sql.append("'").append(Schulgliederung.getByKuerzel(sfsg.gliederung)).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, ZulaessigesFach.class.getCanonicalName(), ZulaessigesFach.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Einschulungsart
	 */
	private Consumer<Logger> updateEinschulungsarten = (Logger logger) -> {
		String tabname = "EinschulungsartKatalog";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, Beschreibung, gueltigVon, gueltigBis) ");
		Einschulungsart[] values = Einschulungsart.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Einschulungsart art = values[i];
			for (EinschulungsartKatalogEintrag a : art.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(a.id).append(",");
				sql.append("'").append(a.kuerzel).append("'").append(",");
				sql.append("'").append(a.bezeichnung.replace("'", "''")).append("'").append(",");
				sql.append("'").append(a.beschreibung.replace("'", "''")).append("'").append(",");
				sql.append(a.gueltigVon).append(",");
				sql.append(a.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Einschulungsart.class.getCanonicalName(), Einschulungsart.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Einschulungsart 
	 */
	private Consumer<Logger> updateEinschulungsartenKeys = (Logger logger) -> {
		String tabname = "EinschulungsartKatalog_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(Einschulungsart.values()).map(h -> h.daten.kuerzel).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, Einschulungsart.class.getCanonicalName(), Einschulungsart.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Religion
	 */
	private Consumer<Logger> updateReligionen = (Logger logger) -> {
		String tabname = "Religionen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		Religion[] values = Religion.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			Religion religion = values[i];
			for (ReligionKatalogEintrag r : religion.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(r.id).append(",");
				sql.append("'").append(r.kuerzel).append("'").append(",");
				sql.append("'").append(r.bezeichnung.replace("'", "''")).append("'").append(",");
				sql.append(r.gueltigVon).append(",");
				sql.append(r.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, Religion.class.getCanonicalName(), Religion.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type Religion 
	 */
	private Consumer<Logger> updateReligionenKeys = (Logger logger) -> {
		String tabname = "Religionen_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(Religion.values()).map(h -> h.daten.kuerzel).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, Religion.class.getCanonicalName(), Religion.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type AllgemeineMerkmale
	 */
	private Consumer<Logger> updateAllgemeineMerkmale = (Logger logger) -> {
		String tabname = "AllgemeineMerkmaleKatalog";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Bezeichnung, beiSchule, beiSchueler, KuerzelASD, gueltigVon, gueltigBis) ");
		AllgemeineMerkmale[] values = AllgemeineMerkmale.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			AllgemeineMerkmale merkmal = values[i];
			for (AllgemeineMerkmaleKatalogEintrag m : merkmal.historie) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(m.id).append(",");
				sql.append("'").append(m.kuerzel).append("'").append(",");
				sql.append("'").append(m.bezeichnung).append("'").append(",");
				sql.append(m.beiSchule).append(",");
				sql.append(m.beiSchueler).append(",");
				if (m.kuerzelASD == null)
					sql.append("null,");
				else
					sql.append("'").append(m.kuerzelASD).append("'").append(",");
				sql.append(m.gueltigVon).append(",");
				sql.append(m.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(tabname, AllgemeineMerkmale.class.getCanonicalName(), AllgemeineMerkmale.VERSION, sql.toString());
	};

	
	/**
	 * Aktualisiert die Tabelle für den Core-Type AllgemeineMerkmale 
	 */
	private Consumer<Logger> updateAllgemeineMerkmaleKeys = (Logger logger) -> {
		String tabname = "AllgemeineMerkmaleKatalog_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Arrays.stream(AllgemeineMerkmale.values()).map(h -> h.daten.kuerzel).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, AllgemeineMerkmale.class.getCanonicalName(), AllgemeineMerkmale.VERSION, sql.toString());
	};
	
	
	/**
	 * Aktualisiert die Tabelle für den Core-Type AllgemeineMerkmale
	 */
	private Consumer<Logger> updateAllgemeineMerkmaleSchulformen = (Logger logger) -> {
		String tabname = "AllgemeineMerkmaleKatalog_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Merkmal_ID, Schulform_Kuerzel) ");
		AllgemeineMerkmale[] values = AllgemeineMerkmale.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			AllgemeineMerkmale merkmal = values[i];
			for (AllgemeineMerkmaleKatalogEintrag am : merkmal.historie) {
				for (String schulform : am.schulformen) {
					sql.append(isFirst ? "VALUES (" : ", (");
					isFirst = false;
					sql.append(am.id).append(",");
					sql.append("'").append(Schulform.getByKuerzel(schulform).daten.kuerzel).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(tabname, AllgemeineMerkmale.class.getCanonicalName(), AllgemeineMerkmale.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für die Core-Types BerufskollegOrganisationsformen,
	 * WeiterbildungskollegOrganisationsformen und AllgemeinbildendOrganisationsformen
	 */
	private Consumer<Logger> updateOrganisationsformen = (Logger logger) -> {
		String tabname = "OrganisationsformenKatalog";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(ID, Kuerzel, Beschreibung, gueltigVon, gueltigBis) ");
		boolean isFirst = true;
		List<OrganisationsformKatalogEintrag> values = Stream.of(
			Stream.of(BerufskollegOrganisationsformen.values()).flatMap(o -> Stream.of(o.historie)), 
			Stream.of(WeiterbildungskollegOrganisationsformen.values()).flatMap(o -> Stream.of(o.historie)), 
			Stream.of(AllgemeinbildendOrganisationsformen.values()).flatMap(o -> Stream.of(o.historie))
		).flatMap(o -> o).toList();
		for (OrganisationsformKatalogEintrag f : values) {
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append(f.id).append(",");
			sql.append("'").append(f.kuerzel).append("'").append(",");
			sql.append("'").append(f.beschreibung.replace("'", "''")).append("'").append(",");
			sql.append(f.gueltigVon).append(",");
			sql.append(f.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(tabname, 
			BerufskollegOrganisationsformen.class.getCanonicalName() + ", " + WeiterbildungskollegOrganisationsformen.class.getCanonicalName() + ", " + AllgemeinbildendOrganisationsformen.class.getCanonicalName(), 
			BerufskollegOrganisationsformen.VERSION + WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION, 
			sql.toString()
		);
	};

	
	/**
	 * Aktualisiert die Tabelle für die Core-Types BerufskollegOrganisationsformen,
	 * WeiterbildungskollegOrganisationsformen und AllgemeinbildendOrganisationsformen
	 */
	private Consumer<Logger> updateOrganisationsformenKeys = (Logger logger) -> {
		String tabname = "OrganisationsformenKatalog_Keys";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Kuerzel) ");
		List<String> kuerzel = Stream.of( 
				Stream.of(BerufskollegOrganisationsformen.values()).map(h -> h.daten.kuerzel),
				Stream.of(WeiterbildungskollegOrganisationsformen.values()).map(h -> h.daten.kuerzel),
				Stream.of(AllgemeinbildendOrganisationsformen.values()).map(h -> h.daten.kuerzel)
			).flatMap(o -> o).distinct().collect(Collectors.toList());
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			String k = kuerzel.get(i);
			sql.append(isFirst ? "VALUES (" : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(tabname, 
			BerufskollegOrganisationsformen.class.getCanonicalName() + ", " + WeiterbildungskollegOrganisationsformen.class.getCanonicalName() + ", " + AllgemeinbildendOrganisationsformen.class.getCanonicalName(), 
			BerufskollegOrganisationsformen.VERSION + WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION, 
			sql.toString()
		);
	};
	
	
	/**
	 * Aktualisiert die Tabelle für die Core-Types BerufskollegOrganisationsformen,
	 * WeiterbildungskollegOrganisationsformen und AllgemeinbildendOrganisationsformen
	 */
	private Consumer<Logger> updateOrganisationsformenSchulformen = (Logger logger) -> {
		String tabname = "OrganisationsformenKatalog_Schulformen";
		logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tabname); 
		sql.append("(Organisationsform_ID, Schulform_Kuerzel) ");
		boolean isFirst = true;
		List<OrganisationsformKatalogEintrag> values = Stream.of(
				Stream.of(BerufskollegOrganisationsformen.values()).flatMap(o -> Stream.of(o.historie)), 
				Stream.of(WeiterbildungskollegOrganisationsformen.values()).flatMap(o -> Stream.of(o.historie)), 
				Stream.of(AllgemeinbildendOrganisationsformen.values()).flatMap(o -> Stream.of(o.historie))
			).flatMap(o -> o).toList();
		for (OrganisationsformKatalogEintrag f : values) {
			for (String sf : f.schulformen) {
				sql.append(isFirst ? "VALUES (" : ", (");
				isFirst = false;
				sql.append(f.id).append(",");
				if (sf == null)
					sql.append("null)");
				else
					sql.append("'").append(Schulform.getByKuerzel(sf).daten.kuerzel).append("'").append(")");
			}
		}
		updateCoreTypeTabelle(tabname, 
			BerufskollegOrganisationsformen.class.getCanonicalName() + ", " + WeiterbildungskollegOrganisationsformen.class.getCanonicalName() + ", " + AllgemeinbildendOrganisationsformen.class.getCanonicalName(), 
			BerufskollegOrganisationsformen.VERSION + WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION, 
			sql.toString()
		);
	};


    /**
     * Aktualisiert die Tabelle für den Core-Type LehrerLeitungsfunktion 
     */
    private Consumer<Logger> updateLehrerLeitungsfunktionenKeys = (Logger logger) -> {
        String tabname = "LehrerLeitungsfunktion_Keys";
        logger.logLn("Aktualisiere Core-Type in Tabelle " + tabname);
        updateCoreTypeTabelle(tabname, LehrerLeitungsfunktion.class.getCanonicalName(), LehrerLeitungsfunktion.VERSION, 
            Arrays.stream(LehrerLeitungsfunktion.values()).map(h -> "" + h.daten.id).distinct()
                .collect(Collectors.joining("), (", "INSERT INTO " + tabname + "(ID) VALUES (", ")"))
        );
    };


	/**
	 * Prüft, ob die übergebene Version mit der Version des in der 
	 * Datenbank gespeicherten Core-Types übereinstimmt oder nicht.
	 * 
	 * @param tabname   der Name der Tabelle, wo der Core-Type gespechert ist
	 * @param version   die Version, auf welche geprüft wird 
	 * 
	 * @return true, falls die Versionen übereinstimmen, und ansonsten false 
	 */
	private boolean pruefeVersion(String tabname, long version) {
		DTOCoreTypeVersion v = status.getCoreTypeVersion(tabname);
		return (v == null) || (v.Version == null) ? false : v.Version == version;
	}

}
