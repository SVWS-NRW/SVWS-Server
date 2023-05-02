package de.svws_nrw.db.utils.lupo.mdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

/**
 * Diese Klasse wird für den Import der Tabelle ABP_Lehrer, d.h. für die Benutzer
 * der LuPO-MDB, aus einer LuPO-Datenbank im Access-Format genutzt.
 */
public final class ABPLehrer {

	/** Der Name des Benutzers der LuPO-MDB */
	public String Name = null;

	/** Gibt die Art des Kurses im Klartext an. */
	public String EMail = null;

	/** Das einfach verschlüsselte Kennwort des Benutzers. */
	public String Kennwort = null;

	/** Der Benutzername des SMTP-Benutzer, falls der EMail-Versand über SMTP genutzt wird */
	public String SMTP_User = null;

	/** Der Kennwort des SMTP-Benutzer, falls der EMail-Versand über SMTP genutzt wird */
	public String SMTP_Password = null;


	private static final String fieldName = "Name";
	private static final String fieldEMail = "EMail";
	private static final String fieldKennwort = "Kennwort";
	private static final String fieldSMTP_User = "SMTP_User";
	private static final String fieldSMTP_Password = "SMTP_Password";


	/**
	 * Liest alle Einträge der Tabelle "ABP_Lehrer" aus der LuPO-Datei ein.
	 *
	 * @param db   die Datenbank, aus der die Tabelle gelesen werden soll
	 *
	 * @return die Liste der LuPO-MDB-Benutzer aus der LuPO-Datei
	 */
	public static List<ABPLehrer> read(final Database db) {
		try {
			final List<ABPLehrer> liste = new ArrayList<>();
			final Table table = db.getTable("ABP_Lehrer");
			for (final Row r : table) {
				final ABPLehrer zuordnung = new ABPLehrer();
				zuordnung.Name = r.getString(fieldName);
				zuordnung.EMail = r.getString(fieldEMail);
				zuordnung.Kennwort = r.getString(fieldKennwort);
				zuordnung.SMTP_User = r.getString(fieldSMTP_User);
				zuordnung.SMTP_Password = r.getString(fieldSMTP_Password);
				liste.add(zuordnung);
			}
			return liste;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return Collections.emptyList();
		}
	}


	/**
	 * Schreibt die angegebenen LuPO-MDB-Benutzer in die übergebene Datenbank
	 *
	 * @param db     die zu beschreibende Datenbank
	 * @param list   die Liste der zu schreibenden LuPO-MDB-Benutzer
	 */
	public static void write(final Database db, final List<ABPLehrer> list) {
		try {
			final Table table = new TableBuilder("ABP_Lehrer")
				.addColumn(new ColumnBuilder(fieldName, DataType.TEXT).setLengthInUnits(50))
				.addColumn(new ColumnBuilder(fieldEMail, DataType.TEXT).setLengthInUnits(100))
				.addColumn(new ColumnBuilder(fieldKennwort, DataType.TEXT).setLengthInUnits(20))
				.addColumn(new ColumnBuilder(fieldSMTP_User, DataType.TEXT).setLengthInUnits(255))
				.addColumn(new ColumnBuilder(fieldSMTP_Password, DataType.TEXT).setLengthInUnits(255))
			    .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns(fieldName).setPrimaryKey())
			    .toTable(db);
			for (final ABPLehrer zuordnung: list) {
				table.addRow(
					zuordnung.Name,
					zuordnung.EMail,
					zuordnung.Kennwort,
					zuordnung.SMTP_User,
					zuordnung.SMTP_Password
				);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gibt den Standard-Eintrag für die Tabelle ABPLehrer zurück.
	 *
	 * @return der Standard-Eintrag für die Tabelle ABPLehrer
	 */
	public static List<ABPLehrer> getDefault() {
		return new ArrayList<>();
	}


	@Override
	public String toString() {
		return "ABPLehrer [Name=" + Name + ", EMail=" + EMail + ", Kennwort=" + Kennwort + ", SMTP_User=" + SMTP_User
				+ ", SMTP_Password=" + SMTP_Password + "]";
	}

}
