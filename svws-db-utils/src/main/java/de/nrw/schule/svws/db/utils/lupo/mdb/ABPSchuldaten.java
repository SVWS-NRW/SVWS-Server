package de.nrw.schule.svws.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.db.utils.data.Schule;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Schuldaten aus einer LuPO-Datenbank 
 * im Access-Format genutzt. 
 */
public class ABPSchuldaten {

	/** Die sechsstellige Nummer der Schule. */
	public String Schulnr = null;

	/** Das Kürzel der Schulform der Schule. */
	public String SchulformKrz = null;
	
	/** Die Bezeichnung der Schulform der Schule. */
	public String SchulformBez = null;
	
	/** Der erste Teil der Bezeichnung der Schule. */
	public String Bezeichnung1 = null;
	
	/** Der zweite Teil der Bezeichnung der Schule. */
	public String Bezeichnung2 = null;
	
	/** Der dritte Teil der Bezeichnung der Schule. */
	public String Bezeichnung3 = null;
	
	/** TODO ??? */
	public String Kennwort = null;

	/** Die Prüfungsordung für die Oberstufe der Schule. */
	public String PruefOrdnung = null;
	
	/** TODO ??? */
	public String PruefPhase = null;
	
	/** Die bilinguale Sprache, falls es an der Schule bilingualen Sachunterricht gibt. */
	public String BilingualSprachen = null;

	/** TODO ??? */
	public String Beratungslehrer = null;

	/** TODO ??? */
	public String BeratungslehrerEMail = null;

	/** Legt fest, ob eine Schule Zusatzkurse im Fach Geschichte anbietet */
	public boolean ZusatzkursGeschichteVorhanden = true;

	/** Der Jahrgang in dem die Zusatzkurse in Geschichte beginnen (normalerweise Q2.1) */
	public GostHalbjahr ZusatzkursGeschichteBeginn = GostHalbjahr.Q21;

	/** Legt fest, ob eine Schule Zusatzkurse im Fach Sozialwissenschaften anbietet */
	public boolean ZusatzkursSoWiVorhanden = true;

	/** Der Jahrgang in dem die Zusatzkurse in Sozialwissenschaften beginnen (normalerweise Q2.1) */
	public GostHalbjahr ZusatzkursSoWiBeginn = GostHalbjahr.Q21;
	
	/** TODO ??? */
	boolean AusdruckAlleFaecher = true;
	
	/** Das Halbjahr für das die Schüler aktuell beraten werden. */
	public String Beratungshalbjahr = null;
	
	/** Dieser Text erscheint auf dem Beratungsbogen eines Schülers. */
	public String BeratungsText = null;
	
	/** Der Text der mitgeschickt wird, wenn eine Email von Lupo generiert wird. */
	public String MailText = null;
	
	/** Der Text der auf den LuPO-Bögen gesetzt wird, wenn eine Email von Lupo generiert wird. */
	public String MailTextBoegen = null;
	
	/** TODO ??? */
	public String FS_NurMitSF = null;
	
	/** TODO ??? */
	public Boolean Komprimieren = false;
	
	/** Gibt an, ob Änderungen an den Datensätzen erlaubt sind. */
	public String AenderungenErlauben = null;
	
	/** Gibt an, ob die Laufbahn bei der Eingabe automatisch geprüft werden soll.  */
	public boolean AutoPruefModus = true;
	
	/** Gibt an, welche Dauer eine Unterrichtseinheit in Minuten hat */
	public int DauerUnterrichtseinheit = 45;
	
	/** Für den automatischen Emailversand über Lupo: Der SMTP-Benutzername. */
	public String SMTP_User = null;
	
	/** Für den automatischen Emailversand über Lupo: Das SMTP-Passwort des Benutzers. */
	public String SMTP_Password = null;
	
	/** Für den automatischen Emailversand über Lupo: Der SMTP-Serverhostname. */
	public String SMTP_Server = null;
	
	/** Für den automatischen Emailversand über Lupo: Der Port des SMTP Servers. */
	public String SMTP_Port = null;
	
	/** Für den automatischen Emailversand über Lupo: Der SMTP-Benutzername. */
	public String SMTP_SSL = null;
	
	/** Für den automatischen Emailversand über Lupo: Der SMTP-Benutzername. */
	public String SMTP_StartTLS = null;
	


	/**
	 * Liest alle Einträge der Tabelle "ABP_Schuldaten" aus der LuPO-Datei ein.
	 * 
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 * 
	 * @return die Liste der Kursarten aus der LuPO-Datei
	 */
	public static List<ABPSchuldaten> read(Database db) {
		try {
			String zkGeBeginn;
			String zkSoWiBeginn;
			List<ABPSchuldaten> liste = new Vector<>();
			Table table = db.getTable("ABP_Schuldaten");
			for (Row r : table) {
				ABPSchuldaten zuordnung = new ABPSchuldaten();
				zuordnung.Schulnr = r.getString("Schulnr");
				zuordnung.SchulformKrz = r.getString("SchulformKrz");
				zuordnung.SchulformBez = r.getString("SchulformBez");
				zuordnung.Bezeichnung1 = r.getString("Bezeichnung1");
				zuordnung.Bezeichnung2 = r.getString("Bezeichnung2");
				zuordnung.Bezeichnung3 = r.getString("Bezeichnung3");
				zuordnung.Kennwort = r.getString("Kennwort");
				zuordnung.PruefOrdnung = r.getString("PruefOrdnung");
				zuordnung.PruefPhase = r.getString("PruefPhase");
				zuordnung.BilingualSprachen = r.getString("BilingualSprachen");
				zuordnung.Beratungslehrer = r.getString("Beratungslehrer");
				zuordnung.BeratungslehrerEMail = r.getString("BeratungslehrerEMail");
				zkGeBeginn = r.getString("ZK_Beginn_GE");
				if (GostHalbjahr.Q11.kuerzelAlt.equals(zkGeBeginn) || GostHalbjahr.Q12.kuerzelAlt.equals(zkGeBeginn) || GostHalbjahr.Q21.kuerzelAlt.equals(zkGeBeginn) || GostHalbjahr.Q22.kuerzelAlt.equals(zkGeBeginn) ) {
					zuordnung.ZusatzkursGeschichteVorhanden = true;
					zuordnung.ZusatzkursGeschichteBeginn = GostHalbjahr.fromKuerzelAlt(zkGeBeginn);
				}
				else {
					zuordnung.ZusatzkursGeschichteVorhanden = false;
					zuordnung.ZusatzkursGeschichteBeginn = GostHalbjahr.Q21;
				}
				zkSoWiBeginn = r.getString("ZK_Beginn_SW");
				if (GostHalbjahr.Q11.kuerzelAlt.equals(zkSoWiBeginn) || GostHalbjahr.Q12.kuerzelAlt.equals(zkSoWiBeginn) || GostHalbjahr.Q21.kuerzelAlt.equals(zkSoWiBeginn) || GostHalbjahr.Q22.kuerzelAlt.equals(zkSoWiBeginn) ) {
					zuordnung.ZusatzkursSoWiVorhanden = true;
					zuordnung.ZusatzkursSoWiBeginn = GostHalbjahr.fromKuerzelAlt(zkSoWiBeginn);
				}
				else {
					zuordnung.ZusatzkursSoWiVorhanden = false;
					zuordnung.ZusatzkursSoWiBeginn = GostHalbjahr.Q21;
				}
				zuordnung.AusdruckAlleFaecher = "J".equals(r.getString("AusdruckAlleFaecher"));
				zuordnung.Beratungshalbjahr = r.getString("Beratungshalbjahr");
				zuordnung.BeratungsText = r.getString("BeratungsText");
				zuordnung.MailText = r.getString("MailText");
				zuordnung.MailTextBoegen = r.getString("MailTextBoegen");
				zuordnung.FS_NurMitSF = r.getString("FS_NurMitSF");
				zuordnung.Komprimieren = (r.getString("Komprimieren") == null) ? null : "N".equals(r.getString("Komprimieren"));
				zuordnung.AenderungenErlauben = r.getString("AenderungenErlauben");
				zuordnung.AutoPruefModus = "J".equals(r.getString("AutoPruefModus"));
				zuordnung.DauerUnterrichtseinheit = (r.getInt("DauerUnterrichtseinheit") == null) ? 45 : r.getInt("DauerUnterrichtseinheit");
				zuordnung.SMTP_User = r.getString("SMTP_User");
				zuordnung.SMTP_Password = r.getString("SMTP_Password");
				zuordnung.SMTP_Server = r.getString("SMTP_Server");
				zuordnung.SMTP_Port = r.getString("SMTP_Port");
				zuordnung.SMTP_SSL = r.getString("SMTP_SSL");
				zuordnung.SMTP_StartTLS = r.getString("SMTP_StartTLS");
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen Kursarten in die übergebene Datenbank
	 * 
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Kursarten
	 */
	public static void write(Database db, List<ABPSchuldaten> list) {
		try {
			Table table = new TableBuilder("ABP_Schuldaten")
				.addColumn(new ColumnBuilder("Schulnr", DataType.TEXT).setLengthInUnits(6))
				.addColumn(new ColumnBuilder("SchulformKrz", DataType.TEXT).setLengthInUnits(3))
				.addColumn(new ColumnBuilder("SchulformBez", DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder("Bezeichnung1", DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder("Bezeichnung2", DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder("Bezeichnung3", DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder("Kennwort", DataType.TEXT).setLengthInUnits(20))
				.addColumn(new ColumnBuilder("PruefOrdnung", DataType.TEXT).setLengthInUnits(20))
				.addColumn(new ColumnBuilder("PruefPhase", DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder("BilingualSprachen", DataType.TEXT).setLengthInUnits(10))
				.addColumn(new ColumnBuilder("Beratungslehrer", DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder("BeratungslehrerEMail", DataType.TEXT).setLengthInUnits(100))
				.addColumn(new ColumnBuilder("ZK_Beginn_GE", DataType.TEXT).setLengthInUnits(2).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'Q3'"))
				.addColumn(new ColumnBuilder("ZK_Beginn_SW", DataType.TEXT).setLengthInUnits(2).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'Q3'"))
				.addColumn(new ColumnBuilder("AusdruckAlleFaecher", DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder("Beratungshalbjahr", DataType.TEXT).setLengthInUnits(5).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'EF.1'"))
				.addColumn(new ColumnBuilder("BeratungsText", DataType.MEMO).setLengthInUnits(16777216))
				.addColumn(new ColumnBuilder("MailText", DataType.MEMO).setLengthInUnits(16777216))
				.addColumn(new ColumnBuilder("MailTextBoegen", DataType.MEMO).setLengthInUnits(16777216))
				.addColumn(new ColumnBuilder("FS_NurMitSF", DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder("Komprimieren", DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder("AenderungenErlauben", DataType.TEXT).setLengthInUnits(6))
				.addColumn(new ColumnBuilder("AutoPruefModus", DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder("DauerUnterrichtseinheit", DataType.LONG))
				.addColumn(new ColumnBuilder("SMTP_User", DataType.TEXT).setLengthInUnits(255))
				.addColumn(new ColumnBuilder("SMTP_Password", DataType.TEXT).setLengthInUnits(255))
				.addColumn(new ColumnBuilder("SMTP_Server", DataType.TEXT).setLengthInUnits(255))
				.addColumn(new ColumnBuilder("SMTP_Port", DataType.LONG))
				.addColumn(new ColumnBuilder("SMTP_SSL", DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder("SMTP_StartTLS", DataType.TEXT).setLengthInUnits(1))
			    .toTable(db);

			for (ABPSchuldaten zuordnung: list) {
				table.addRow(
					zuordnung.Schulnr,
					zuordnung.SchulformKrz,
					zuordnung.SchulformBez,
					zuordnung.Bezeichnung1,
					zuordnung.Bezeichnung2,
					zuordnung.Bezeichnung3,
					zuordnung.Kennwort,
					zuordnung.PruefOrdnung,
					zuordnung.PruefPhase,
					zuordnung.BilingualSprachen,
					zuordnung.Beratungslehrer,
					zuordnung.BeratungslehrerEMail,
					(!zuordnung.ZusatzkursGeschichteVorhanden) ? "N" : zuordnung.ZusatzkursGeschichteBeginn.kuerzelAlt,
					(!zuordnung.ZusatzkursSoWiVorhanden) ? "N" : zuordnung.ZusatzkursSoWiBeginn.kuerzelAlt,
					zuordnung.AusdruckAlleFaecher ? "J" : "N",
					zuordnung.Beratungshalbjahr,
					zuordnung.BeratungsText,
					zuordnung.MailText,
					zuordnung.MailTextBoegen,
					zuordnung.FS_NurMitSF,
					zuordnung.Komprimieren == null ? null : (zuordnung.Komprimieren ? "J" : "N"),
					zuordnung.AenderungenErlauben,
					zuordnung.AutoPruefModus ? "J" : "N",
					zuordnung.DauerUnterrichtseinheit,
					zuordnung.SMTP_User,
					zuordnung.SMTP_Password,
					zuordnung.SMTP_Server,
					zuordnung.SMTP_Port,
					zuordnung.SMTP_SSL,
					zuordnung.SMTP_StartTLS
				);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchuldaten zurück.
	 * 
	 * @return der Standard-Eintrag für die Tabelle ABPSchuldaten
	 */
	public static List<ABPSchuldaten> getDefault() {
		List<ABPSchuldaten> schuldaten = new Vector<>();
		return schuldaten;
	}


	/**
	 * Erstellt den Eintrag für die Tabelle ABPSchuldaten aus dem DTO
	 * der SVWS-Server-Datenbank.
	 * 
	 * @param schule     das SVWS-Server-DTO für die Schuldaten
	 * @param jahrgang   der Jahrgang, für den die Lupo-MDB erstellt wird
	 * 
	 * @return der Eintrag für die Tabelle ABPSchuldaten
	 */
	public static List<ABPSchuldaten> get(Schule schule, String jahrgang) {
		List<ABPSchuldaten> lupoSchuldaten = new Vector<>();
		if (schule == null)
			return lupoSchuldaten;
		var sf = schule.getSchulform(); 
		ABPSchuldaten schuldaten = new ABPSchuldaten();
		schuldaten.Schulnr = "" + schule.dto.SchulNr;
		schuldaten.SchulformKrz = ((sf == null) || (sf.daten == null)) ? null : sf.daten.kuerzel;
		schuldaten.SchulformBez = ((sf == null) || (sf.daten == null)) ? null : sf.daten.bezeichnung;
		schuldaten.Bezeichnung1 = schule.dto.Bezeichnung1;
		schuldaten.Bezeichnung2 = schule.dto.Bezeichnung2;
		schuldaten.Bezeichnung3 = schule.dto.Bezeichnung3;
		schuldaten.Kennwort = null;
		schuldaten.PruefOrdnung = "APO-GOSt(B)10/G8"; // TODO Prüfungsordnung demnächst automatisch setzen anhand des Abiturjahrganges.
		schuldaten.PruefPhase = null;
		schuldaten.BilingualSprachen = null;     // TODO Biligualen Bildungsgang aus der SVWS-DB bestimmen
		schuldaten.Beratungslehrer = null;
		schuldaten.BeratungslehrerEMail = null;
		schuldaten.ZusatzkursGeschichteVorhanden = true;
		schuldaten.ZusatzkursGeschichteBeginn = GostHalbjahr.Q21;
		schuldaten.ZusatzkursSoWiVorhanden = true;
		schuldaten.ZusatzkursSoWiBeginn = GostHalbjahr.Q21;
		schuldaten.AusdruckAlleFaecher = true;
		schuldaten.Beratungshalbjahr = jahrgang + "." + schule.getHalbjahr();  
		schuldaten.BeratungsText = null;
		schuldaten.MailText = null;
		schuldaten.MailTextBoegen = null;
		schuldaten.FS_NurMitSF = null;
		schuldaten.Komprimieren = null;
		schuldaten.AenderungenErlauben = null;
		schuldaten.AutoPruefModus = true;
		schuldaten.DauerUnterrichtseinheit = schule.dto.DauerUnterrichtseinheit; 
		schuldaten.SMTP_User = null;
		schuldaten.SMTP_Password = null;
		schuldaten.SMTP_Server = null;
		schuldaten.SMTP_Port = null;
		schuldaten.SMTP_SSL = null;
		schuldaten.SMTP_StartTLS = null;		
		lupoSchuldaten.add(schuldaten);
		return lupoSchuldaten;
	}


	@Override
	public String toString() {
		return "ABPSchuldaten [Schulnr=" + Schulnr + ", SchulformKrz=" + SchulformKrz + ", SchulformBez=" + SchulformBez
				+ ", Bezeichnung1=" + Bezeichnung1 + ", Bezeichnung2=" + Bezeichnung2 + ", Bezeichnung3=" + Bezeichnung3
				+ ", Kennwort=" + Kennwort + ", PruefOrdnung=" + PruefOrdnung + ", PruefPhase=" + PruefPhase
				+ ", BilingualSprachen=" + BilingualSprachen + ", Beratungslehrer=" + Beratungslehrer
				+ ", BeratungslehrerEMail=" + BeratungslehrerEMail + ", ZK_Angebot_GE=" + ZusatzkursGeschichteVorhanden + ", ZK_Beginn_GE=" + ZusatzkursGeschichteBeginn
				+ ", ZK_Angebot_SW=" + ZusatzkursSoWiVorhanden + ", ZK_Beginn_SW=" + ZusatzkursSoWiBeginn + ", AusdruckAlleFaecher=" + AusdruckAlleFaecher
				+ ", Beratungshalbjahr=" + Beratungshalbjahr + ", BeratungsText=" + BeratungsText + ", MailText="
				+ MailText + ", MailTextBoegen=" + MailTextBoegen + ", FS_NurMitSF=" + FS_NurMitSF + ", Komprimieren=" + Komprimieren
				+ ", AenderungenErlauben=" + AenderungenErlauben + ", AutoPruefModus=" + AutoPruefModus
				+ ", DauerUnterrichtseinheit=" + DauerUnterrichtseinheit + ", SMTP_User=" + SMTP_User
				+ ", SMTP_Password=" + SMTP_Password + ", SMTP_Server=" + SMTP_Server + ", SMTP_Port=" + SMTP_Port
				+ ", SMTP_SSL=" + SMTP_SSL + ", SMTP_StartTLS=" + SMTP_StartTLS + "]";
	}


}
