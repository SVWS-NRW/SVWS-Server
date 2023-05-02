package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Schuldaten aus einer LuPO-Datenbank
 * im Access-Format genutzt.
 */
public final class ABPSchuldaten {

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

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht verwendet */
	public String Kennwort = null;

	/** Die Prüfungsordung für die Oberstufe der Schule. */
	public String PruefOrdnung = null;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht verwendet */
	public String PruefPhase = null;

	/** Die bilinguale Sprache, falls es an der Schule bilingualen Sachunterricht gibt. */
	public String BilingualSprachen = null;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht verwendet */
	public String Beratungslehrer = null;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht verwendet */
	public String BeratungslehrerEMail = null;

	/** Legt fest, ob eine Schule Zusatzkurse im Fach Geschichte anbietet */
	public boolean ZusatzkursGeschichteVorhanden = true;

	/** Der Jahrgang in dem die Zusatzkurse in Geschichte beginnen (normalerweise Q2.1) */
	public GostHalbjahr ZusatzkursGeschichteBeginn = GostHalbjahr.Q21;

	/** Legt fest, ob eine Schule Zusatzkurse im Fach Sozialwissenschaften anbietet */
	public boolean ZusatzkursSoWiVorhanden = true;

	/** Der Jahrgang in dem die Zusatzkurse in Sozialwissenschaften beginnen (normalerweise Q2.1) */
	public GostHalbjahr ZusatzkursSoWiBeginn = GostHalbjahr.Q21;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht verwendet */
	boolean AusdruckAlleFaecher = true;

	/** Das Halbjahr für das die Schüler aktuell beraten werden. */
	public String Beratungshalbjahr = null;

	/** Dieser Text erscheint auf dem Beratungsbogen eines Schülers. */
	public String BeratungsText = null;

	/** Der Text der mitgeschickt wird, wenn eine Email von Lupo generiert wird. */
	public String MailText = null;

	/** Der Text der auf den LuPO-Bögen gesetzt wird, wenn eine Email von Lupo generiert wird. */
	public String MailTextBoegen = null;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht verwendet */
	public String FS_NurMitSF = null;

	/** Deprecated: Wird in der aktuellen Laufbahnplanung nicht verwendet */
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


	private static final String fieldSchulnr = "Schulnr";
	private static final String fieldSchulformKrz = "SchulformKrz";
	private static final String fieldSchulformBez = "SchulformBez";
	private static final String fieldBezeichnung1 = "Bezeichnung1";
	private static final String fieldBezeichnung2 = "Bezeichnung2";
	private static final String fieldBezeichnung3 = "Bezeichnung3";
	private static final String fieldKennwort = "Kennwort";
	private static final String fieldPruefOrdnung = "PruefOrdnung";
	private static final String fieldPruefPhase = "PruefPhase";
	private static final String fieldBilingualSprachen = "BilingualSprachen";
	private static final String fieldBeratungslehrer = "Beratungslehrer";
	private static final String fieldBeratungslehrerEMail = "BeratungslehrerEMail";
	private static final String fieldZK_Beginn_GE = "ZK_Beginn_GE";
	private static final String fieldZK_Beginn_SW = "ZK_Beginn_SW";
	private static final String fieldAusdruckAlleFaecher = "AusdruckAlleFaecher";
	private static final String fieldBeratungshalbjahr = "Beratungshalbjahr";
	private static final String fieldBeratungsText = "BeratungsText";
	private static final String fieldMailText = "MailText";
	private static final String fieldMailTextBoegen = "MailTextBoegen";
	private static final String fieldFS_NurMitSF = "FS_NurMitSF";
	private static final String fieldKomprimieren = "Komprimieren";
	private static final String fieldAenderungenErlauben = "AenderungenErlauben";
	private static final String fieldAutoPruefModus = "AutoPruefModus";
	private static final String fieldDauerUnterrichtseinheit = "DauerUnterrichtseinheit";
	private static final String fieldSMTP_User = "SMTP_User";
	private static final String fieldSMTP_Password = "SMTP_Password";
	private static final String fieldSMTP_Server = "SMTP_Server";
	private static final String fieldSMTP_Port = "SMTP_Port";
	private static final String fieldSMTP_SSL = "SMTP_SSL";
	private static final String fieldSMTP_StartTLS = "SMTP_StartTLS";



	/**
	 * Liest alle Einträge der Tabelle "ABP_Schuldaten" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der Kursarten aus der LuPO-Datei
	 */
	public static List<ABPSchuldaten> read(final Database db) {
		try {
			String zkGeBeginn;
			String zkSoWiBeginn;
			final List<ABPSchuldaten> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_Schuldaten");
			for (final Row r : table) {
				final ABPSchuldaten zuordnung = new ABPSchuldaten();
				zuordnung.Schulnr = r.getString(fieldSchulnr);
				zuordnung.SchulformKrz = r.getString(fieldSchulformKrz);
				zuordnung.SchulformBez = r.getString(fieldSchulformBez);
				zuordnung.Bezeichnung1 = r.getString(fieldBezeichnung1);
				zuordnung.Bezeichnung2 = r.getString(fieldBezeichnung2);
				zuordnung.Bezeichnung3 = r.getString(fieldBezeichnung3);
				zuordnung.Kennwort = r.getString(fieldKennwort);
				zuordnung.PruefOrdnung = r.getString(fieldPruefOrdnung);
				zuordnung.PruefPhase = r.getString(fieldPruefPhase);
				zuordnung.BilingualSprachen = r.getString(fieldBilingualSprachen);
				zuordnung.Beratungslehrer = r.getString(fieldBeratungslehrer);
				zuordnung.BeratungslehrerEMail = r.getString(fieldBeratungslehrerEMail);
				zkGeBeginn = r.getString(fieldZK_Beginn_GE);
				if (GostHalbjahr.Q11.kuerzelAlt.equals(zkGeBeginn) || GostHalbjahr.Q12.kuerzelAlt.equals(zkGeBeginn) || GostHalbjahr.Q21.kuerzelAlt.equals(zkGeBeginn) || GostHalbjahr.Q22.kuerzelAlt.equals(zkGeBeginn)) {
					zuordnung.ZusatzkursGeschichteVorhanden = true;
					zuordnung.ZusatzkursGeschichteBeginn = GostHalbjahr.fromKuerzelAlt(zkGeBeginn);
				} else {
					zuordnung.ZusatzkursGeschichteVorhanden = false;
					zuordnung.ZusatzkursGeschichteBeginn = GostHalbjahr.Q21;
				}
				zkSoWiBeginn = r.getString(fieldZK_Beginn_SW);
				if (GostHalbjahr.Q11.kuerzelAlt.equals(zkSoWiBeginn) || GostHalbjahr.Q12.kuerzelAlt.equals(zkSoWiBeginn) || GostHalbjahr.Q21.kuerzelAlt.equals(zkSoWiBeginn) || GostHalbjahr.Q22.kuerzelAlt.equals(zkSoWiBeginn)) {
					zuordnung.ZusatzkursSoWiVorhanden = true;
					zuordnung.ZusatzkursSoWiBeginn = GostHalbjahr.fromKuerzelAlt(zkSoWiBeginn);
				} else {
					zuordnung.ZusatzkursSoWiVorhanden = false;
					zuordnung.ZusatzkursSoWiBeginn = GostHalbjahr.Q21;
				}
				zuordnung.AusdruckAlleFaecher = "J".equals(r.getString(fieldAusdruckAlleFaecher));
				zuordnung.Beratungshalbjahr = r.getString(fieldBeratungshalbjahr);
				zuordnung.BeratungsText = r.getString(fieldBeratungsText);
				zuordnung.MailText = r.getString(fieldMailText);
				zuordnung.MailTextBoegen = r.getString(fieldMailTextBoegen);
				zuordnung.FS_NurMitSF = r.getString(fieldFS_NurMitSF);
				zuordnung.Komprimieren = (r.getString(fieldKomprimieren) == null) ? null : "N".equals(r.getString(fieldKomprimieren));
				zuordnung.AenderungenErlauben = r.getString(fieldAenderungenErlauben);
				zuordnung.AutoPruefModus = "J".equals(r.getString(fieldAutoPruefModus));
				zuordnung.DauerUnterrichtseinheit = (r.getInt(fieldDauerUnterrichtseinheit) == null) ? 45 : r.getInt(fieldDauerUnterrichtseinheit);
				zuordnung.SMTP_User = r.getString(fieldSMTP_User);
				zuordnung.SMTP_Password = r.getString(fieldSMTP_Password);
				zuordnung.SMTP_Server = r.getString(fieldSMTP_Server);
				zuordnung.SMTP_Port = r.getString(fieldSMTP_Port);
				zuordnung.SMTP_SSL = r.getString(fieldSMTP_SSL);
				zuordnung.SMTP_StartTLS = r.getString(fieldSMTP_StartTLS);
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}

	private static String toStringJN(final boolean value) {
		return value ? "J" : "N";
	}

	/**
	 * Schreibt die angegebenen Kursarten in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden Kursarten
	 */
	public static void write(final Database db, final List<ABPSchuldaten> list) {
		try {
			final Table table = new TableBuilder("ABP_Schuldaten")
				.addColumn(new ColumnBuilder(fieldSchulnr, DataType.TEXT).setLengthInUnits(6))
				.addColumn(new ColumnBuilder(fieldSchulformKrz, DataType.TEXT).setLengthInUnits(3))
				.addColumn(new ColumnBuilder(fieldSchulformBez, DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder(fieldBezeichnung1, DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder(fieldBezeichnung2, DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder(fieldBezeichnung3, DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder(fieldKennwort, DataType.TEXT).setLengthInUnits(20))
				.addColumn(new ColumnBuilder(fieldPruefOrdnung, DataType.TEXT).setLengthInUnits(20))
				.addColumn(new ColumnBuilder(fieldPruefPhase, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldBilingualSprachen, DataType.TEXT).setLengthInUnits(10))
				.addColumn(new ColumnBuilder(fieldBeratungslehrer, DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder(fieldBeratungslehrerEMail, DataType.TEXT).setLengthInUnits(100))
				.addColumn(new ColumnBuilder(fieldZK_Beginn_GE, DataType.TEXT).setLengthInUnits(2).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'Q3'"))
				.addColumn(new ColumnBuilder(fieldZK_Beginn_SW, DataType.TEXT).setLengthInUnits(2).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'Q3'"))
				.addColumn(new ColumnBuilder(fieldAusdruckAlleFaecher, DataType.TEXT).setLengthInUnits(1).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'J'"))
				.addColumn(new ColumnBuilder(fieldBeratungshalbjahr, DataType.TEXT).setLengthInUnits(5).putProperty(PropertyMap.DEFAULT_VALUE_PROP, DataType.TEXT, "'EF.1'"))
				.addColumn(new ColumnBuilder(fieldBeratungsText, DataType.MEMO).setLengthInUnits(16777216))
				.addColumn(new ColumnBuilder(fieldMailText, DataType.MEMO).setLengthInUnits(16777216))
				.addColumn(new ColumnBuilder(fieldMailTextBoegen, DataType.MEMO).setLengthInUnits(16777216))
				.addColumn(new ColumnBuilder(fieldFS_NurMitSF, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldKomprimieren, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldAenderungenErlauben, DataType.TEXT).setLengthInUnits(6))
				.addColumn(new ColumnBuilder(fieldAutoPruefModus, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldDauerUnterrichtseinheit, DataType.LONG))
				.addColumn(new ColumnBuilder(fieldSMTP_User, DataType.TEXT).setLengthInUnits(255))
				.addColumn(new ColumnBuilder(fieldSMTP_Password, DataType.TEXT).setLengthInUnits(255))
				.addColumn(new ColumnBuilder(fieldSMTP_Server, DataType.TEXT).setLengthInUnits(255))
				.addColumn(new ColumnBuilder(fieldSMTP_Port, DataType.LONG))
				.addColumn(new ColumnBuilder(fieldSMTP_SSL, DataType.TEXT).setLengthInUnits(1))
				.addColumn(new ColumnBuilder(fieldSMTP_StartTLS, DataType.TEXT).setLengthInUnits(1))
			    .toTable(db);

			for (final ABPSchuldaten zuordnung: list) {
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
					toStringJN(zuordnung.AusdruckAlleFaecher),
					zuordnung.Beratungshalbjahr,
					zuordnung.BeratungsText,
					zuordnung.MailText,
					zuordnung.MailTextBoegen,
					zuordnung.FS_NurMitSF,
					zuordnung.Komprimieren == null ? null : toStringJN(zuordnung.Komprimieren),
					zuordnung.AenderungenErlauben,
					toStringJN(zuordnung.AutoPruefModus),
					zuordnung.DauerUnterrichtseinheit,
					zuordnung.SMTP_User,
					zuordnung.SMTP_Password,
					zuordnung.SMTP_Server,
					zuordnung.SMTP_Port,
					zuordnung.SMTP_SSL,
					zuordnung.SMTP_StartTLS
				);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPSchuldaten zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPSchuldaten
	 */
	public static List<ABPSchuldaten> getDefault() {
		return new ArrayList<>();
	}


	/**
	 * Erstellt den Eintrag für die Tabelle ABPSchuldaten aus dem DTO
	 * der SVWS-Server-Datenbank.
	 *
	 * @param schule     das SVWS-Server-DTO für die Schuldaten
	 * @param jahrgang   der Jahrgang, für den die Lupo-MDB erstellt wird
	 * @param halbjahr   das Halbjahr, für den die Lupo-MDB erstellt wird
	 *
	 * @return der Eintrag für die Tabelle ABPSchuldaten
	 */
	public static List<ABPSchuldaten> get(final DTOEigeneSchule schule, final String jahrgang, final int halbjahr) {
		final List<ABPSchuldaten> lupoSchuldaten = new ArrayList<>();
		if (schule == null)
			return lupoSchuldaten;
		final ABPSchuldaten schuldaten = new ABPSchuldaten();
		schuldaten.Schulnr = "" + schule.SchulNr;
		schuldaten.SchulformKrz = ((schule.Schulform == null) || (schule.Schulform.daten == null)) ? null : schule.Schulform.daten.kuerzel;
		schuldaten.SchulformBez = ((schule.Schulform == null) || (schule.Schulform.daten == null)) ? null : schule.Schulform.daten.bezeichnung;
		schuldaten.Bezeichnung1 = schule.Bezeichnung1;
		schuldaten.Bezeichnung2 = schule.Bezeichnung2;
		schuldaten.Bezeichnung3 = schule.Bezeichnung3;
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
		schuldaten.Beratungshalbjahr = jahrgang + "." + halbjahr;
		schuldaten.BeratungsText = null;
		schuldaten.MailText = null;
		schuldaten.MailTextBoegen = null;
		schuldaten.FS_NurMitSF = null;
		schuldaten.Komprimieren = null;
		schuldaten.AenderungenErlauben = null;
		schuldaten.AutoPruefModus = true;
		schuldaten.DauerUnterrichtseinheit = schule.DauerUnterrichtseinheit;
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
