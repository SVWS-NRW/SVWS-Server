package de.svws_nrw.db.schema.revisionen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 1.
 */
public final class Revision1Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 1.
	 */
	public Revision1Updates() {
		super(SchemaRevisionen.REV_1);
		pruefeKatalogReligion();
		pruefeKatalogErzieherArt();
		pruefeKatalogOrtsteile();
		pruefeKatalogOrte();
		passeVerkehrsspracheAn();
		pruefeFremdschluessel();
		pruefeFremdschluessel2();
		pruefeLeistungsdatenUndFachbemerkungen();
		passeSprachenfolgeAn();
		pruefeLeistungsdatenUndLernabschnittsdaten();
		pruefeWeitereDaten();
		pruefeAbiturtabellen();
		erstelleNeueKlassenTabelle();
		passeLehrerTabelleAn();
		pruefeWeitereDaten2();
		passeBenutzerTabellenAn();
	}


	private void pruefeKatalogReligion() {
		add("Korrigiere Religion_ID, falls ein Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			UPDATE Schueler
			    JOIN (
			        SELECT
			            SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG,
			            SUM(CASE WHEN SchulnrEigner = 0 THEN ID ELSE null END) AS ID_REPLACE
			        FROM K_Religion
			        GROUP BY Bezeichnung
			        HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			    ) mapping ON Schueler.Religion_ID = mapping.ID_ORIG
			SET Schueler.Religion_ID = mapping.ID_REPLACE
			""",
			Schema.tab_K_Religion, Schema.tab_Schueler
		);
		add("Entferne ID, falls ein Religions-Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			DELETE FROM K_Religion
			WHERE ID IN (
			    SELECT
			        SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG
			    FROM K_Religion
			    GROUP BY Bezeichnung
			    HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			)
			""",
			Schema.tab_K_Religion
		);
	}

	private void pruefeKatalogErzieherArt() {
		add("Korrigiere ErzieherArt_ID, falls ein Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			UPDATE SchuelerErzAdr
			    JOIN (
			        SELECT
			            SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG,
			            SUM(CASE WHEN SchulnrEigner = 0 THEN ID ELSE null END) AS ID_REPLACE
			        FROM K_ErzieherArt
			        GROUP BY Bezeichnung
			        HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			    ) mapping ON SchuelerErzAdr.ErzieherArt_ID = mapping.ID_ORIG
			SET SchuelerErzAdr.ErzieherArt_ID = mapping.ID_REPLACE
			""",
			Schema.tab_K_ErzieherArt, Schema.tab_SchuelerErzAdr
		);
		add("Entferne ID, falls ein Erzieher-Art-Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			DELETE FROM K_ErzieherArt
			WHERE ID IN (
			    SELECT
			        SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG
			    FROM K_ErzieherArt
			    GROUP BY Bezeichnung
			    HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			)
			""",
			Schema.tab_K_ErzieherArt
		);
	}

	private void pruefeKatalogOrtsteile() {
		add("Korrigiere Ortsteil_ID, falls ein Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			UPDATE Schueler
			    JOIN (
			        SELECT
			            SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG,
			            SUM(CASE WHEN SchulnrEigner = 0 THEN ID ELSE null END) AS ID_REPLACE
			        FROM K_Ortsteil
			        GROUP BY Bezeichnung
			        HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			    ) mapping ON Schueler.Ortsteil_ID = mapping.ID_ORIG
			SET Schueler.Ortsteil_ID = mapping.ID_REPLACE
			""",
			Schema.tab_K_Ortsteil, Schema.tab_Schueler
		);
		add("Korrigiere ErzOrtsteil_ID, falls ein Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			UPDATE SchuelerErzAdr
			    JOIN (
			        SELECT
			            SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG,
			            SUM(CASE WHEN SchulnrEigner = 0 THEN ID ELSE null END) AS ID_REPLACE
			        FROM K_Ortsteil
			        GROUP BY Bezeichnung
			        HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			    ) mapping ON SchuelerErzAdr.ErzOrtsteil_ID = mapping.ID_ORIG
			SET SchuelerErzAdr.ErzOrtsteil_ID = mapping.ID_REPLACE
			""",
			Schema.tab_K_Ortsteil, Schema.tab_SchuelerErzAdr
		);
		add("Entferne ID, falls ein Ortsteil-Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			DELETE FROM K_Ortsteil
			WHERE ID IN (
			    SELECT
			        SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG
			    FROM K_Ortsteil
			    GROUP BY Bezeichnung
			    HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			)
			""",
			Schema.tab_K_Ortsteil
		);
	}


	private void pruefeKatalogOrte() {
		add("Entferne ID, falls ein Katalog-Eintrag doppelt vorkommt (z.B. bei SchildZentral-Dbs)",
			"""
			DELETE FROM K_Ort
			WHERE ID IN (
			    SELECT
			        SUM(CASE WHEN SchulnrEigner <> 0 THEN ID ELSE null END) AS ID_ORIG
			    FROM K_Ort
			    GROUP BY PLZ, Bezeichnung
			    HAVING count(ID) = 2 AND SUM(CASE WHEN SchulnrEigner = 0 THEN 1 ELSE 2 END) = 3
			)
			""",
			Schema.tab_K_Ort
		);
	}

	private void passeVerkehrsspracheAn() {
		add("Setze Verkehrssprache auf xx, wenn der bisherige Pseudo-Wert zz verwendet wurde",
			"""
			UPDATE Schueler
			SET VerkehrsspracheFamilie = 'xx'
			WHERE VerkehrsspracheFamilie = 'zz'
			""",
			Schema.tab_Schueler
		);
	}

	private void pruefeFremdschluessel() {
		add("Korrektur für den Fremdschluessel auf die Spalte AbteilungsLeiter der Tabelle EigeneSchule_Abteilungen",
			"""
			UPDATE EigeneSchule_Abteilungen
			SET AbteilungsLeiter = NULL
			WHERE AbteilungsLeiter NOT IN (
			    SELECT Kuerzel
			    FROM K_Lehrer
			)
			""",
			Schema.tab_EigeneSchule_Abteilungen, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte FloskelGruppe der Tabelle Floskeln",
			"""
			UPDATE Floskeln
			SET FloskelGruppe = NULL
			WHERE FloskelGruppe NOT IN (
			    SELECT Kuerzel
			    FROM Floskelgruppen
			)
			""",
			Schema.tab_Floskeln, Schema.tab_Floskelgruppen
		);
		add("Korrektur für den Fremdschluessel auf die Spalte EigeneSchule_Jahrgaenge der Tabelle Floskeln",
			"""
			UPDATE Floskeln
			SET FloskelJahrgang = NULL
			WHERE FloskelJahrgang NOT IN (
			    SELECT ASDJahrgang
			    FROM EigeneSchule_Jahrgaenge
			)
			""",
		    Schema.tab_Floskeln, Schema.tab_EigeneSchule_Jahrgaenge
		);
		add("Korrektur für den Fremdschluessel auf die Spalte AllgAdrAdressArt der Tabelle K_AllgAdresse",
			"""
			UPDATE K_AllgAdresse
			SET AllgAdrAdressArt = NULL
			WHERE AllgAdrAdressArt NOT IN (
			    SELECT Bezeichnung
			    FROM K_Adressart
			)
			""",
			Schema.tab_K_Adressart, Schema.tab_K_AllgAdresse
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fach_ID der Tabelle K_Ankreuzfloskeln – Spalte IstASV nutzen",
			"""
			UPDATE K_Ankreuzfloskeln
			SET IstASV = 1
			WHERE Fach_ID = -1
			""",
			Schema.tab_K_Ankreuzfloskeln
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fach_ID der Tabelle K_Ankreuzfloskeln – Setze Fach-ID auf null bei ungültigen Werten",
			"""
			UPDATE K_Ankreuzfloskeln
			SET Fach_ID = null
			WHERE Fach_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Faecher
			)
			""",
			Schema.tab_K_Ankreuzfloskeln, Schema.tab_EigeneSchule_Faecher
		);
		add("Korrektur für den Fremdschluessel auf die Spalte StaatKrz der Tabelle K_Lehrer",
			"""
			UPDATE K_Lehrer
			SET StaatKrz = NULL
			WHERE StaatKrz NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_K_Lehrer, Schema.tab_Nationalitaeten_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Jahrgang_ID der Tabelle Kurse",
			"""
			UPDATE Kurse
			SET Jahrgang_ID = NULL
			WHERE Jahrgang_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Jahrgaenge
			)
			""",
			Schema.tab_Kurse, Schema.tab_EigeneSchule_Jahrgaenge
		);
		add("Entferne Datensätze aus der Tabelle Kurse, bei denen keine gültige Fach_ID gesetzt ist",
			"""
			DELETE FROM Kurse
			WHERE Fach_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Faecher
			)
			""",
			Schema.tab_Kurse, Schema.tab_EigeneSchule_Faecher
		);
		add("Korrektur für den Fremdschluessel auf die Spalte DatumLoeschfristHinweisDeaktiviertUserID der Tabelle Schild_Verwaltung",
			"""
			UPDATE Schild_Verwaltung
			SET DatumLoeschfristHinweisDeaktiviertUserID = NULL
			WHERE DatumLoeschfristHinweisDeaktiviertUserID NOT IN (
			    SELECT ID FROM Users
			)
			""",
			Schema.tab_Schild_Verwaltung, Schema.tab_Users
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Einschulungsart_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Einschulungsart_ID = null
			WHERE Einschulungsart_ID NOT IN (
			    SELECT ID
			    FROM K_EinschulungsArt
			)
			""",
			Schema.tab_K_EinschulungsArt, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Entlassjahrgang_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Entlassjahrgang_ID = null
			WHERE Entlassjahrgang_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Jahrgaenge
			)
			""",
			Schema.tab_EigeneSchule_Jahrgaenge, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte FachklasseNSJ_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET FachklasseNSJ_ID = null
			WHERE FachklasseNSJ_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Fachklassen
			)
			""",
			Schema.tab_EigeneSchule_Fachklassen, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fahrschueler_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Fahrschueler_ID = null
			WHERE Fahrschueler_ID NOT IN (
			    SELECT ID
			    FROM K_FahrschuelerArt
			)
			""",
			Schema.tab_K_FahrschuelerArt, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Haltestelle_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Haltestelle_ID = null
			WHERE Haltestelle_ID NOT IN (
			    SELECT ID
			    FROM K_Haltestelle
			)
			""",
			Schema.tab_K_Haltestelle, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Jahrgang_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Jahrgang_ID = null
			WHERE Jahrgang_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Jahrgaenge
			)
			""",
			Schema.tab_EigeneSchule_Jahrgaenge, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Kindergarten_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Kindergarten_ID = null
			WHERE Kindergarten_ID NOT IN (
			    SELECT ID
			    FROM K_Kindergarten
			)
			""",
			Schema.tab_K_Kindergarten, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Klasse der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Klasse = null
			WHERE Klasse NOT IN (
			    SELECT Klasse
			    FROM Versetzung
			)
			""",
			Schema.tab_Versetzung, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Ortsteil_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Ortsteil_ID = NULL
			WHERE Ortsteil_ID NOT IN (
			    SELECT ID
			    FROM K_Ortsteil
			)
			""",
			Schema.tab_K_Ortsteil, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Religion_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Religion_ID = NULL
			WHERE Religion_ID NOT IN (
			    SELECT ID FROM K_Religion
			)
			""",
			Schema.tab_K_Religion, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte StaatKrz der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET StaatKrz = NULL
			WHERE StaatKrz NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_Nationalitaeten_Keys, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte StaatKrz2 der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET StaatKrz2 = NULL
			WHERE StaatKrz2 NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_Nationalitaeten_Keys, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte GeburtslandSchueler der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET GeburtslandSchueler = NULL
			WHERE GeburtslandSchueler NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_Nationalitaeten_Keys, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte GeburtslandVater der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET GeburtslandVater = NULL
			WHERE GeburtslandVater NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_Nationalitaeten_Keys, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte GeburtslandMutter der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET GeburtslandMutter = NULL
			WHERE GeburtslandMutter NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_Nationalitaeten_Keys, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Schwerpunkt_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Schwerpunkt_ID = NULL
			WHERE Schwerpunkt_ID NOT IN (
			    SELECT ID
			    FROM K_Schwerpunkt
			)
			""",
			Schema.tab_K_Schwerpunkt, Schema.tab_Schueler
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Sportbefreiung_ID der Tabelle Schueler",
			"""
			UPDATE Schueler
			SET Sportbefreiung_ID = NULL
			WHERE Sportbefreiung_ID NOT IN (
			    SELECT ID
			    FROM K_Sportbefreiung
			)
			""",
			Schema.tab_K_Schwerpunkt, Schema.tab_Schueler
		);
	}

	private void pruefeFremdschluessel2() {
		add("Korrektur für den Fremdschluessel auf die Spalte Ansprechpartner_ID der Tabelle Schueler_AllgAdr",
			"""
			UPDATE Schueler_AllgAdr
			SET Ansprechpartner_ID = NULL
			WHERE Ansprechpartner_ID NOT IN (
			    SELECT ID
			    FROM AllgAdrAnsprechpartner
			)
			""",
			Schema.tab_AllgAdrAnsprechpartner, Schema.tab_Schueler_AllgAdr
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Vertragsart_ID der Tabelle Schueler_AllgAdr",
			"""
			UPDATE Schueler_AllgAdr
			SET Vertragsart_ID = NULL
			WHERE Vertragsart_ID NOT IN (
			    SELECT ID
			    FROM K_BeschaeftigungsArt
			)
			""",
			Schema.tab_K_BeschaeftigungsArt, Schema.tab_Schueler_AllgAdr
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Kurs_ID der Tabelle SchuelerAbiFaecher",
			"""
			UPDATE SchuelerAbiFaecher
			SET Kurs_ID = NULL
			WHERE Kurs_ID NOT IN (
			    SELECT ID
			    FROM Kurse
			)
			""",
			Schema.tab_SchuelerAbiFaecher, Schema.tab_Kurse
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Lehrer_ID der Tabelle SchuelerEinzelleistungen",
			"""
			UPDATE SchuelerEinzelleistungen
			SET Lehrer_ID = NULL WHERE
			Lehrer_ID NOT IN (
			    SELECT ID
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerEinzelleistungen, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte ErzieherArt_ID der Tabelle SchuelerErzAdr",
			"""
			UPDATE SchuelerErzAdr
			SET ErzieherArt_ID = NULL
			WHERE ErzieherArt_ID NOT IN (
			    SELECT ID
			    FROM K_ErzieherArt
			)
			""",
			Schema.tab_SchuelerErzAdr, Schema.tab_K_ErzieherArt
		);
		add("Korrektur für den Fremdschluessel auf die Spalte ErzOrtsteil_ID der Tabelle SchuelerErzAdr",
			"""
			UPDATE SchuelerErzAdr
			SET ErzOrtsteil_ID = NULL
			WHERE ErzOrtsteil_ID NOT IN (
			    SELECT ID
			    FROM K_Ortsteil
			)
			""",
			Schema.tab_SchuelerErzAdr, Schema.tab_K_Ortsteil
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Erz1StaatKrz der Tabelle SchuelerErzAdr",
			"""
			UPDATE SchuelerErzAdr
			SET Erz1StaatKrz = NULL
			WHERE Erz1StaatKrz NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_SchuelerErzAdr, Schema.tab_Nationalitaeten_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Erz2StaatKrz der Tabelle SchuelerErzAdr",
			"""
			UPDATE SchuelerErzAdr
			SET Erz2StaatKrz = NULL
			WHERE Erz2StaatKrz NOT IN (
			    SELECT DEStatisCode
			    FROM Nationalitaeten_Keys
			)
			""",
			Schema.tab_SchuelerErzAdr, Schema.tab_Nationalitaeten_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fach_ID der Tabelle SchuelerFehlstunden",
			"""
			UPDATE SchuelerFehlstunden
			SET Fach_ID = null
			WHERE Fach_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Faecher
			)
			""",
			Schema.tab_SchuelerFehlstunden, Schema.tab_EigeneSchule_Faecher
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Lehrer_ID der Tabelle SchuelerFehlstunden",
			"""
			UPDATE SchuelerFehlstunden
			SET Lehrer_ID = NULL
			WHERE Lehrer_ID NOT IN (
			    SELECT ID
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerFehlstunden, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Lehrer_ID der Tabelle SchuelerFoerderempfehlungen",
			"""
			UPDATE SchuelerFoerderempfehlungen
			SET Lehrer_ID = NULL
			WHERE Lehrer_ID NOT IN (
			    SELECT ID
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerFoerderempfehlungen, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte KategorieID der Tabelle SchuelerKAoADaten",
			"""
			UPDATE SchuelerKAoADaten
			SET KategorieID = NULL
			WHERE KategorieID NOT IN (
			    SELECT ID
			    FROM KAoA_Kategorie_Keys
			)
			""",
			Schema.tab_SchuelerKAoADaten, Schema.tab_KAoA_Kategorie_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte MerkmalID der Tabelle SchuelerKAoADaten",
			"""
			UPDATE SchuelerKAoADaten
			SET MerkmalID = NULL
			WHERE MerkmalID NOT IN (
			    SELECT ID
			    FROM KAoA_Merkmal_Keys
			)
			""",
			Schema.tab_SchuelerKAoADaten, Schema.tab_KAoA_Merkmal_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte ZusatzmerkmalID der Tabelle SchuelerKAoADaten",
			"""
			UPDATE SchuelerKAoADaten
			SET ZusatzmerkmalID = NULL
			WHERE ZusatzmerkmalID NOT IN (
			    SELECT ID
			    FROM KAoA_Zusatzmerkmal_Keys
			)
			""",
			Schema.tab_SchuelerKAoADaten, Schema.tab_KAoA_Zusatzmerkmal_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte AnschlussoptionID der Tabelle SchuelerKAoADaten",
			"""
			UPDATE SchuelerKAoADaten
			SET AnschlussoptionID = NULL
			WHERE AnschlussoptionID NOT IN (
			    SELECT ID
			    FROM KAoA_Anschlussoption_Keys
			)
			""",
			Schema.tab_SchuelerKAoADaten, Schema.tab_KAoA_Anschlussoption_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte BerufsfeldID der Tabelle SchuelerKAoADaten",
			"""
			UPDATE SchuelerKAoADaten
			SET BerufsfeldID = NULL
			WHERE BerufsfeldID NOT IN (
			    SELECT ID
			    FROM KAoA_Berufsfeld_Keys
			)
			""",
			Schema.tab_SchuelerKAoADaten, Schema.tab_KAoA_Berufsfeld_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte SBO_Ebene4ID der Tabelle SchuelerKAoADaten",
			"""
			UPDATE SchuelerKAoADaten
			SET SBO_Ebene4ID = NULL
			WHERE SBO_Ebene4ID NOT IN (
			    SELECT ID
			    FROM KAoA_SBO_Ebene4_Keys
			)
			""",
			Schema.tab_SchuelerKAoADaten, Schema.tab_KAoA_SBO_Ebene4_Keys
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fachlehrer der Tabelle SchuelerLeistungsdaten",
			"""
			UPDATE SchuelerLeistungsdaten
			SET Fachlehrer = NULL
			WHERE Fachlehrer NOT IN (
			    SELECT Kuerzel
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerLeistungsdaten, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Zusatzkraft der Tabelle SchuelerLeistungsdaten",
			"""
			UPDATE SchuelerLeistungsdaten
			SET Zusatzkraft = NULL
			WHERE Zusatzkraft NOT IN (
			    SELECT Kuerzel
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerLeistungsdaten, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Kurs_ID der Tabelle SchuelerLeistungsdaten",
			"""
			UPDATE SchuelerLeistungsdaten
			SET Kurs_ID = NULL
			WHERE Kurs_ID NOT IN (
			    SELECT ID
			    FROM Kurse
			)
			""",
			Schema.tab_SchuelerLeistungsdaten, Schema.tab_Kurse
		);
		add("Setzen des ASDJahrgang, falls diese NULL ist auf den Eintrag des Jahrgangs bei Jahrgang_ID in der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten JOIN EigeneSchule_Jahrgaenge
			    ON SchuelerLernabschnittsdaten.Jahrgang_ID = EigeneSchule_Jahrgaenge.ID
			SET SchuelerLernabschnittsdaten.ASDJahrgang = EigeneSchule_Jahrgaenge.ASDJahrgang
			WHERE SchuelerLernabschnittsdaten.ASDJahrgang IS NULL AND SchuelerLernabschnittsdaten.Jahrgang_ID IS NOT NULL;
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Jahrgaenge
		);
		add("Korrektur für den Fremdschluessel auf die Spalte NPV_Fach_ID der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET NPV_Fach_ID = null
			WHERE NPV_Fach_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Faecher
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Faecher
		);
		add("Korrektur für den Fremdschluessel auf die Spalte NPAA_Fach_ID der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET NPAA_Fach_ID = null
			WHERE NPAA_Fach_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Faecher
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Faecher
		);
		add("Korrektur für den Fremdschluessel auf die Spalte NPBQ_Fach_ID der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET NPBQ_Fach_ID = null
			WHERE NPBQ_Fach_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Faecher
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Faecher
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fachklasse_ID der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Fachklasse_ID = NULL
			WHERE Fachklasse_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Fachklassen
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Fachklassen
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Foerderschwerpunkt_ID der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Foerderschwerpunkt_ID = NULL
			WHERE Foerderschwerpunkt_ID NOT IN (
			    SELECT ID
			    FROM K_Foerderschwerpunkt
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_K_Foerderschwerpunkt
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Foerderschwerpunkt2_ID der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Foerderschwerpunkt2_ID = NULL
			WHERE Foerderschwerpunkt2_ID NOT IN (
			    SELECT ID
			    FROM K_Foerderschwerpunkt
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_K_Foerderschwerpunkt
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Jahrgang_ID der Tabelle Lernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Jahrgang_ID = NULL
			WHERE Jahrgang_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Jahrgaenge
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Jahrgaenge
		);
		add("Korrektur für den Fremdschluessel auf die Spalte KlassenLehrer der Tabelle SchuelerLernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET KlassenLehrer = NULL
			WHERE KlassenLehrer NOT IN (
			    SELECT Kuerzel
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte StvKlassenlehrer_ID der Tabelle SchuelerLernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET StvKlassenlehrer_ID = NULL
			WHERE StvKlassenlehrer_ID NOT IN (
			    SELECT ID
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Schwerpunkt_ID der Tabelle SchuelerLernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Schwerpunkt_ID = NULL
			WHERE Schwerpunkt_ID NOT IN (
			    SELECT ID
			    FROM K_Schwerpunkt
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_K_Schwerpunkt
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Sonderpaedagoge_ID der Tabelle SchuelerLernabschnittsdaten",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Sonderpaedagoge_ID = NULL
			WHERE Sonderpaedagoge_ID NOT IN (
			    SELECT ID
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte TelefonArt_ID der Tabelle SchuelerTelefone",
			"""
			UPDATE SchuelerTelefone
			SET TelefonArt_ID = NULL
			WHERE TelefonArt_ID NOT IN (
			    SELECT ID
			    FROM K_TelefonArt
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_K_TelefonArt
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fachklasse_ID der Tabelle Versetzung",
			"""
			UPDATE Versetzung
			SET Fachklasse_ID = NULL
			WHERE Fachklasse_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Fachklassen
			)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Fachklassen
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Jahrgang_ID der Tabelle Versetzung",
			"""
			UPDATE Versetzung
			SET Jahrgang_ID = NULL
			WHERE Jahrgang_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Jahrgaenge
			)
			""",
			Schema.tab_Versetzung, Schema.tab_EigeneSchule_Jahrgaenge
		);
		add("Korrektur für den Fremdschluessel auf die Spalte KlassenlehrerKrz der Tabelle Versetzung",
			"""
			UPDATE Versetzung
			SET KlassenlehrerKrz = NULL
			WHERE KlassenlehrerKrz NOT IN (
			    SELECT Kuerzel
			    FROM K_Lehrer
			)
			""",
			Schema.tab_Versetzung, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte StvKlassenlehrerKrz der Tabelle Versetzung",
			"""
			UPDATE Versetzung
			SET StvKlassenlehrerKrz = NULL
			WHERE StvKlassenlehrerKrz NOT IN (
			    SELECT Kuerzel
			    FROM K_Lehrer
			)
			""",
			Schema.tab_Versetzung, Schema.tab_K_Lehrer
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Fachklasse_ID der ZuordnungReportvorlagen",
			"""
			UPDATE ZuordnungReportvorlagen
			SET Fachklasse_ID = NULL
			WHERE Fachklasse_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Fachklassen
			)
			""",
			Schema.tab_ZuordnungReportvorlagen, Schema.tab_EigeneSchule_Fachklassen
		);
		add("Korrektur für den Fremdschluessel auf die Spalte Jahrgang_ID der ZuordnungReportvorlagen",
			"""
			UPDATE ZuordnungReportvorlagen
			SET Jahrgang_ID = NULL
			WHERE Jahrgang_ID NOT IN (
			    SELECT ID
			    FROM EigeneSchule_Jahrgaenge
			)
			""",
			Schema.tab_ZuordnungReportvorlagen, Schema.tab_EigeneSchule_Jahrgaenge
		);
	}


	private void pruefeLeistungsdatenUndFachbemerkungen() {
		add("Korrigiere case bei Pseudonoten",
			"""
			UPDATE SchuelerLeistungsdaten
			SET NotenKrz = upper(NotenKrz)
			""",
			Schema.tab_SchuelerLeistungsdaten
		);
		add("Fehlerbehebung bei einer fehlenden Kursart, Kursart raten und setzen auf PUK, falls die Kurs_ID nicht gesetzt ist",
			"""
			UPDATE SchuelerLeistungsdaten
			SET
			    Kursart = 'PUK',
			    KursartAllg = 'PUK'
			WHERE ((Kursart IS NULL) OR (Kursart = ''))
			    AND (Kurs_ID IS NULL)
			""",
			Schema.tab_SchuelerLeistungsdaten
		);
		add("Fehlerbehebung bei einer fehlenden Kursart, Kursart raten und setzen auf PUT, falls die Kurs_ID gesetzt ist",
			"""
			UPDATE SchuelerLeistungsdaten
			SET
			    Kursart = 'PUT',
			    KursartAllg = 'PUT'
			WHERE ((Kursart IS NULL) OR (Kursart = ''))
			    AND (Kurs_ID IS NOT NULL)
			""",
			Schema.tab_SchuelerLeistungsdaten
		);
		add("Kopieren der Daten im Feld LELS in das Feld AUE, falls die Schulform keine Grundschule ist",
			"""
			UPDATE SchuelerLD_PSFachBem
			SET AUE = LELS
			WHERE ((
			        SELECT count(*)
			        FROM EigeneSchule
			        WHERE SchulformKrz = 'G'
			    ) = 0)
			    AND LELS IS NOT NULL
			""",
			Schema.tab_SchuelerLD_PSFachBem, Schema.tab_EigeneSchule
		);
		add("Löschen der Daten im Feld LELS, falls die Schulform keine Grundschule ist",
			"""
			UPDATE SchuelerLD_PSFachBem
			SET LELS = NULL
			WHERE ((
			        SELECT count(*)
			        FROM EigeneSchule
			        WHERE SchulformKrz = 'G'
			    ) = 0)
			    AND LELS IS NOT NULL
			""",
			Schema.tab_SchuelerLD_PSFachBem, Schema.tab_EigeneSchule
		);
	}


	private void passeSprachenfolgeAn() {
		add("Eintragung des atomaren Sprachenkürzels und der vorhandenen Jahrgänge in die ASD-Jahrgänge",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN EigeneSchule_Faecher esf ON ssf.Fach_ID = esf.ID
			    INNER JOIN FachKatalog sszf ON esf.StatistikKrz = sszf.KuerzelASD
			SET
			    ssf.Sprache = sszf.Kuerzel,
			    ssf.ASDJahrgangVon = CASE
			        WHEN ((ssf.JahrgangVon IS NOT NULL)
			            AND (
			                (
			                    FIND_IN_SET(
			                        RIGHT(CONCAT('00', CONVERT(ssf.JahrgangVon, CHAR)), 2),
			                        (SELECT GROUP_CONCAT(Kuerzel) FROM Jahrgaenge_Keys)
			                    ) > 0
			                ) OR (
			                    FIND_IN_SET(
			                        RIGHT(CONCAT('00', CONVERT(ssf.JahrgangVon, CHAR)), 2),
			                        (SELECT DISTINCT GROUP_CONCAT(ASDJahrgang) FROM SchuelerLernabschnittsdaten WHERE ASDJahrgang IS NOT NULL)
			                    ) > 0
			                )
			            )
			        ) THEN
			            RIGHT(CONCAT('00', CONVERT(ssf.JahrgangVon, CHAR)), 2)
			        ELSE
			            NULL
			        END,
			    ssf.ASDJahrgangBis = CASE
			        WHEN ((ssf.JahrgangBis IS NOT NULL)
			            AND (
			                (
			                    FIND_IN_SET(
			                        RIGHT(CONCAT('00', CONVERT(ssf.JahrgangBis, CHAR)), 2),
			                        (SELECT GROUP_CONCAT(Kuerzel) FROM Jahrgaenge_Keys)
			                    ) > 0
			                ) OR (
			                    FIND_IN_SET(
			                        RIGHT(CONCAT('00', CONVERT(ssf.JahrgangBis, CHAR)), 2),
			                        (SELECT DISTINCT GROUP_CONCAT(ASDJahrgang) FROM SchuelerLernabschnittsdaten WHERE ASDJahrgang IS NOT NULL)
			                    ) > 0
			                )
			            )
			        ) THEN
			            RIGHT(CONCAT('00', CONVERT(ssf.JahrgangBis, CHAR)), 2)
			        ELSE
			            NULL
			        END
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_SchuelerLernabschnittsdaten,
			Schema.tab_EigeneSchule_Faecher, Schema.tab_FachKatalog, Schema.tab_Jahrgaenge_Keys
		);
		add("Das Feld Reihenfolge in ReihenfolgeNr übertragen. Nicht nummerische Werte (N, P, x) werden zu NULL",
			"""
			UPDATE SchuelerSprachenfolge
			SET ReihenfolgeNr = CAST(Reihenfolge AS UNSIGNED)
			WHERE Reihenfolge REGEXP '^[1-9]$'
			""",
			Schema.tab_SchuelerSprachenfolge
		);
		add("Anpassung ASDJahrgänge auf alpha-nummerische Werte: Ein Sprachbeginn unter APO-GOSt(B)10/G8 in 10/11 wird zu EF",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN Schueler s ON ssf.Schueler_ID = s.ID
			    INNER JOIN SchuelerLernabschnittsdaten sla ON s.ID = sla.Schueler_ID
			SET ssf.ASDJahrgangVon = 'EF'
			WHERE sla.PruefOrdnung = 'APO-GOSt(B)10/G8'
			    AND ((
			        ssf.ASDJahrgangVon = '10' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'GY'
			    ) OR (
			        ssf.ASDJahrgangVon = '11' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) <> 'GY'
			    ))
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule
		);
		add("Anpassung ASDJahrgänge auf alpha-nummerische Werte: Ein Sprachbeginn unter APO-GOSt(B)10/G8 in 10/11 wird zu EF. Ein späterer Sprachbeginn zu NULL, da unzulässig",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN Schueler s ON ssf.Schueler_ID = s.ID
			    INNER JOIN SchuelerLernabschnittsdaten sla ON s.ID = sla.Schueler_ID
			SET ssf.ASDJahrgangVon = null
			WHERE sla.PruefOrdnung = 'APO-GOSt(B)10/G8'
			    AND ((
			        ssf.ASDJahrgangVon > '10' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'GY'
			    ) OR (
			        ssf.ASDJahrgangVon > '11' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) <> 'GY'
			    ))
			    AND ASDJahrgangVon <> 'EF'
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge auf alpha-nummerische Werte: Bei Sprachbeginn in EF unter APO-GOSt(B)10/G8 ist ein Sprachende in 10/11 die EF",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN Schueler s ON ssf.Schueler_ID = s.ID
			    INNER JOIN SchuelerLernabschnittsdaten sla ON s.ID = sla.Schueler_ID
			SET ssf.ASDJahrgangBis = 'EF'
			WHERE sla.PruefOrdnung = 'APO-GOSt(B)10/G8'
			    AND ((
			        ssf.ASDJahrgangBis = '10' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'GY'
			    ) OR (
			        ssf.ASDJahrgangBis = '11' AND (select SchulformKrz FROM EigeneSchule LIMIT 1) <> 'GY'
			    ))
			    AND ASDJahrgangVon = 'EF'
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge auf alpha-nummerische Werte: Ein Sprachende unter APO-GOSt(B)10/G8 in 10 am GY kann eine abgeschlossene Sprache der Sek-I sein (z. B. bei Wechsel von R, GE zum GY). Prüfe daher, ob Sprache in Leistungsdaten der EF.1 vorhanden",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN Schueler s ON ssf.Schueler_ID = s.ID
			    INNER JOIN SchuelerLernabschnittsdaten sla ON s.id = sla.Schueler_ID
			    INNER JOIN SchuelerLeistungsdaten sld ON sla.ID = sld.Abschnitt_ID
			    INNER JOIN EigeneSchule_Faecher esf ON sld.Fach_ID = esf.ID
			    INNER JOIN FachKatalog sszf ON (sszf.KuerzelASD = esf.StatistikKrz AND ssf.Sprache = sszf.Kuerzel)
			SET ssf.ASDJahrgangBis = 'EF'
			WHERE sla.PruefOrdnung = 'APO-GOSt(B)10/G8'
			    AND sla.ASDJahrgang  = 'EF'
			    AND sla.Abschnitt = 1
			    AND (ssf.ASDJahrgangBis = '10' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'GY')
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule,
			Schema.tab_SchuelerLeistungsdaten, Schema.tab_EigeneSchule_Faecher, Schema.tab_FachKatalog
		);

		add("Anpassung ASDJahrgänge auf alpha-nummerische Werte: Ein Sprachende unter APO-GOSt(B)10/G8 in 11/12 wird zu Q1",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN Schueler s ON ssf.Schueler_ID = s.ID
			    INNER JOIN SchuelerLernabschnittsdaten sla ON s.ID = sla.Schueler_ID
			SET ssf.ASDJahrgangBis = 'Q1'
			WHERE sla.PruefOrdnung = 'APO-GOSt(B)10/G8'
			    AND ((
			        ssf.ASDJahrgangBis = '11' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'GY'
			    ) OR (
			        ssf.ASDJahrgangBis = '12' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) <> 'GY'
			    ))
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge auf alpha-nummerische Werte: Ein Sprachende unter APO-GOSt(B)10/G8 in 12/13 wird zu Q2",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN Schueler s ON ssf.Schueler_ID = s.ID
			    INNER JOIN SchuelerLernabschnittsdaten sla ON s.ID = sla.Schueler_ID
			SET ssf.ASDJahrgangBis = 'Q2'
			WHERE sla.PruefOrdnung = 'APO-GOSt(B)10/G8'
			    AND ((
			        ssf.ASDJahrgangBis = '12' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'GY'
			    ) OR (
			        ssf.ASDJahrgangBis = '13' AND (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) <> 'GY'
			    ))
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge Sprachbeginn am BK 11-12-13 > 01-02-03 (1)",
			"""
			UPDATE SchuelerSprachenfolge ssf
			SET ssf.ASDJahrgangVon = '01'
			WHERE (
			    ssf.JahrgangVon = '11' AND (((SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'BK'
			) OR (
			    (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'SB'))
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge Sprachbeginn am BK 11-12-13 > 01-02-03 (2)",
			"""
			UPDATE SchuelerSprachenfolge ssf
			SET ssf.ASDJahrgangVon = '02'
			WHERE (
			    ssf.JahrgangVon = '12' AND (((SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'BK'
			) OR (
			    (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'SB'))
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge Sprachbeginn am BK 11-12-13 > 01-02-03 (3)",
			"""
			UPDATE SchuelerSprachenfolge ssf
			SET ssf.ASDJahrgangVon = '03'
			WHERE (
			    ssf.JahrgangVon = '13' AND (((SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'BK'
			) OR (
			    (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'SB'))
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge Sprachende am BK 11-12-13 > 01-02-03 (1)",
			"""
			UPDATE SchuelerSprachenfolge ssf
			SET ssf.ASDJahrgangBis = '01'
			WHERE (
			    ssf.JahrgangBis = '11' AND (((SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'BK'
			) OR (
			    (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'SB'))
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge Sprachende am BK 11-12-13 > 01-02-03 (2)",
			"""
			UPDATE SchuelerSprachenfolge ssf
			SET ssf.ASDJahrgangBis = '02'
			WHERE (
			    ssf.JahrgangBis = '12' AND (((SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'BK'
			) OR (
			    (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'SB'))
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_EigeneSchule
		);

		add("Anpassung ASDJahrgänge Sprachende am BK 11-12-13 > 01-02-03 (3)",
			"""
			UPDATE SchuelerSprachenfolge ssf
			SET ssf.ASDJahrgangBis = '03'
			WHERE (
			    ssf.JahrgangBis = '13' AND (((SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'BK'
			) OR (
			    (SELECT SchulformKrz FROM EigeneSchule LIMIT 1) = 'SB'))
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_EigeneSchule
		);

		add("Sprachprüfungen und -nachweis in eine separate Tabelle SchuelerSprachpruefungen auslagern",
			"""
			INSERT INTO SchuelerSprachpruefungen (Schueler_ID, Sprache, ASDJahrgang, IstHSUPruefung, IstFeststellungspruefung, Referenzniveau)
			SELECT
			    s.ID,
			    ssf.Sprache,
			    COALESCE(COALESCE(ASDJahrgangBis, ASDJahrgangVon),
			    COALESCE(ASDJahrgangVon, ASDJahrgangBis)),
			    CASE WHEN ssf.Reihenfolge = 'P' THEN 1 ELSE 0 END,
			    CASE WHEN ssf.Reihenfolge = 'N' THEN 1 ELSE 0 END,
			    ssf.Referenzniveau
			FROM Schueler s
			    INNER JOIN SchuelerSprachenfolge ssf ON ssf.Schueler_ID = s.ID
			WHERE ssf.Reihenfolge = 'P' OR ssf.Reihenfolge = 'N'
			""",
			Schema.tab_SchuelerSprachpruefungen, Schema.tab_SchuelerSprachenfolge, Schema.tab_Schueler
		);

		add("Sprachprüfungen und -nachweis nach dem Auslagern in der alten Sprachenfolgetabelle löschen",
			"""
			DELETE FROM SchuelerSprachenfolge
			WHERE Reihenfolge = 'P' OR Reihenfolge = 'N'
			""",
			Schema.tab_SchuelerSprachenfolge
		);

		add("Ergänze das Fach Latein in der Sprachenfolge, wenn Latinum vorhanden, aber kein Latein",
			"""
			INSERT INTO SchuelerSprachenfolge (Schueler_ID, Sprache)
			(
			    SELECT sabi.Schueler_ID, 'L'
			    FROM SchuelerAbitur sabi
			    WHERE (sabi.Latinum = '+' OR sabi.KlLatinum = '+')
			        AND NOT EXISTS
			        (
			            SELECT ssf.Schueler_ID
			            FROM SchuelerSprachenfolge ssf
			            WHERE ssf.Sprache = 'L' AND sabi.Schueler_ID = ssf.Schueler_ID
			        )
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_SchuelerAbitur
		);

		add("Ergänze das Fach Griechisch in der Sprachenfolge, wenn Graecum vorhanden, aber kein Griechisch",
			"""
			INSERT INTO SchuelerSprachenfolge (Schueler_ID, Sprache)
			(
			    SELECT sabi.Schueler_ID, 'G'
			    FROM SchuelerAbitur sabi
			    WHERE (sabi.Graecum = '+')
			    AND NOT EXISTS
			    (
			        SELECT ssf.Schueler_ID
			        FROM SchuelerSprachenfolge ssf
			        WHERE ssf.Sprache = 'G' AND sabi.Schueler_ID = ssf.Schueler_ID
			    )
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_SchuelerAbitur
		);

		add("Ergänze das Fach Haebräisch in der Sprachenfolge, wenn Hebraicum vorhanden, aber kein Hebräisch",
			"""
			INSERT INTO SchuelerSprachenfolge (Schueler_ID, Sprache)
			(
			    SELECT sabi.Schueler_ID, 'H'
			    FROM SchuelerAbitur sabi
			    WHERE (sabi.Hebraicum = '+')
			    AND NOT EXISTS
			    (
			        SELECT ssf.Schueler_ID
			        FROM SchuelerSprachenfolge ssf
			        WHERE ssf.Sprache = 'H' AND sabi.Schueler_ID = ssf.Schueler_ID
			    )
			)
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_SchuelerAbitur
		);

		add("Übertrage die Angaben (kleines) Latinum von der Abitur in die Sprachenfolge-Tabelle",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN SchuelerAbitur sabi ON ssf.Schueler_ID = sabi.Schueler_ID
			SET
			    ssf.KleinesLatinumErreicht = CASE WHEN sabi.KlLatinum = '+' THEN 1 ELSE 0 END,
			    ssf.LatinumErreicht = CASE WHEN sabi.Latinum = '+' THEN 1 ELSE 0 END
			WHERE ssf.Sprache = 'L'
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_SchuelerAbitur
		);

		add("Übertrage die Angabe Graecum von der Abitur in die Sprachenfolge-Tabelle",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN SchuelerAbitur sabi ON ssf.Schueler_ID = sabi.Schueler_ID
			SET
			    ssf.GraecumErreicht = CASE WHEN sabi.Graecum = '+' THEN 1 ELSE 0 END
			WHERE
			    ssf.Sprache = 'G'
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_SchuelerAbitur
		);

		add("Übertrage die Angabe Hebraicum von der Abitur in die Sprachenfolge-Tabelle",
			"""
			UPDATE SchuelerSprachenfolge ssf
			    INNER JOIN SchuelerAbitur sabi ON ssf.Schueler_ID = sabi.Schueler_ID
			SET
			    ssf.HebraicumErreicht = CASE WHEN sabi.Hebraicum = '+' THEN 1 ELSE 0 END
			WHERE
			    ssf.Sprache = 'H'
			""",
			Schema.tab_SchuelerSprachenfolge, Schema.tab_SchuelerAbitur
		);
	}


	private void pruefeLeistungsdatenUndLernabschnittsdaten() {
		add("Entferne fehlerhafte doppelte Datensätze in Bezug auf die Unique-Constraint SchuelerLeistungsdaten_UC1 aus den Leistungsdaten (1)",
			"""
			CREATE TEMPORARY TABLE TMP_SchuelerLeistungsdaten_Duplikate_MaxID AS
			SELECT max(ID) AS maxID, Abschnitt_ID, Fach_ID, Fachlehrer, Kurs_ID, Kursart
			FROM SchuelerLeistungsdaten
			GROUP BY Abschnitt_ID, Fach_ID, Fachlehrer, Kurs_ID, Kursart
			HAVING count(*) > 1
			""",
			Schema.tab_SchuelerLeistungsdaten
		);
		add("Entferne fehlerhafte doppelte Datensätze in Bezug auf die Unique-Constraint SchuelerLeistungsdaten_UC1 aus den Leistungsdaten (2)",
			"""
			CREATE TEMPORARY TABLE TMP_SchuelerLeistungsdaten_Duplikate_DeleteIDs AS
			SELECT a.ID
			FROM SchuelerLeistungsdaten a
			    JOIN TMP_SchuelerLeistungsdaten_Duplikate_MaxID b
			        ON a.Abschnitt_ID = b.Abschnitt_ID
			        AND (a.Fach_ID = b.Fach_ID)
			        AND ((a.Fachlehrer = b.Fachlehrer) OR (a.Fachlehrer IS NULL AND b.Fachlehrer IS NULL))
			        AND ((a.Kurs_ID = b.Kurs_ID) OR (a.Kurs_ID IS NULL AND b.Kurs_ID IS NULL))
			        AND ((a.Kursart = b.Kursart) OR (a.Kursart IS NULL AND b.Kursart IS NULL))
			        AND a.ID <> b.maxID
			""",
			Schema.tab_SchuelerLeistungsdaten
		);
		add("Entferne fehlerhafte doppelte Datensätze in Bezug auf die Unique-Constraint SchuelerLeistungsdaten_UC1 aus den Leistungsdaten (3)",
			"""
			DELETE FROM SchuelerLeistungsdaten
			WHERE ID IN (
			    SELECT ID
			    FROM TMP_SchuelerLeistungsdaten_Duplikate_DeleteIDs
			)
			""",
			Schema.tab_SchuelerLeistungsdaten
		);
		add("Entferne fehlerhafte doppelte Datensätze in Bezug auf die Unique-Constraint SchuelerLeistungsdaten_UC1 aus den Leistungsdaten (4)",
			"""
			DROP TEMPORARY TABLE TMP_SchuelerLeistungsdaten_Duplikate_DeleteIDs
			"""
		);
		add("Entferne fehlerhafte doppelte Datensätze in Bezug auf die Unique-Constraint SchuelerLeistungsdaten_UC1 aus den Leistungsdaten (5)",
			"""
			DROP TEMPORARY TABLE TMP_SchuelerLeistungsdaten_Duplikate_MaxID
			"""
		);
		add("Überprüfung des Noten-Eintrages für NotenKrz;",
			"""
			UPDATE SchuelerLeistungsdaten
			SET NotenKrz = ''
			WHERE NotenKrz IS NOT NULL
			    AND NotenKrz NOT IN ('6','5-','5','5+','4-','4','4+','3-','3','3+','2-','2','2+','1-','1','1+','E1','E2','E3','AT','AM','NB','NT','NE','LM')
			""",
			Schema.tab_SchuelerLeistungsdaten
		);
		add("Überprüfung des Noten-Eintrages für die Gesamtnote GS oder AL",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Gesamtnote_GS = null
			WHERE Gesamtnote_GS IS NOT NULL AND (Gesamtnote_GS < 1 OR Gesamtnote_GS > 6)
			""",
			Schema.tab_SchuelerLernabschnittsdaten
		);
		add("Überprüfung des Noten-Eintrages für die Gesamtnote NW",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Gesamtnote_NW = null
			WHERE Gesamtnote_NW IS NOT NULL AND (Gesamtnote_NW < 1 OR Gesamtnote_NW > 6)
			""",
			Schema.tab_SchuelerLernabschnittsdaten
		);
	}

	private void pruefeWeitereDaten() {
		add("Kopiere den Eintrag zum Schulleiter und dessen Stellvertreter aus der Tabelle EigeneSchule",
			"""
			INSERT INTO Schulleitung (LeitungsfunktionID, Funktionstext, LehrerID)
			SELECT 1, 'Schulleitung', ID
			FROM K_Lehrer
			WHERE (Nachname, Vorname) IN (SELECT Schulleitername, SchulleiterVorname FROM EigeneSchule)
			UNION
			SELECT 2, 'Stellvertretende Schulleitung', ID
			FROM K_Lehrer
			WHERE (Nachname, Vorname) IN (SELECT StvSchulleiterName, StvSchulleiterVorname FROM EigeneSchule)
			""",
			Schema.tab_Schulleitung, Schema.tab_K_Lehrer, Schema.tab_EigeneSchule
		);
		add("Überprüfung des Eintrages des Geschlechtes",
			"""
			UPDATE Schueler SET Geschlecht = 6 WHERE Geschlecht NOT IN (3,4,5,6)
			""",
			Schema.tab_Schueler
		);
		add("Überprüfung des Eintrages des Geschlechtes",
			"""
			UPDATE K_Lehrer SET Geschlecht = '6' WHERE Geschlecht NOT IN ('3','4','5','6')
			""",
			Schema.tab_K_Lehrer
		);
		add("Überprüfung des Personal-Typs (1)",
			"""
			UPDATE K_Lehrer SET PersonTyp = 'LEHRKRAFT' WHERE PersonTyp IS NULL
			""",
			Schema.tab_K_Lehrer
		);
		add("Überprüfung des Personal-Typs (2)",
			"""
			UPDATE K_Lehrer SET PersonTyp = 'SONSTIGE' WHERE PersonTyp NOT IN ('LEHRKRAFT','SEKRETARIAT','PERSONAL','EXTERN','SONSTIGE')
			""",
			Schema.tab_K_Lehrer
		);
		add("Überprüfung des Schüler-Status",
			"""
			UPDATE Schueler SET Status = 2 WHERE Status NOT IN (0,1,2,3,6,8,9,10)
			""",
			Schema.tab_Schueler
		);
		add("Überprüfung des Eintrages zur Fortschreibungsart",
			"""
			UPDATE Kurse SET Fortschreibungsart = 'K' WHERE Fortschreibungsart NOT IN ('N','D','B','K')
			""",
			Schema.tab_Kurse
		);
		// TODO Prüfe
		final String alleSchulgliederungen = Arrays.stream(Schulgliederung.values())
		        .map(sgl -> Arrays.stream(sgl.historie).toList()).flatMap(List::stream)
		        .map(h -> h.kuerzel).distinct().collect(Collectors.joining("','", "('", "')"));
		add("Überprüfung der Schulgliederung",
			"UPDATE EigeneSchule_Jahrgaenge SET SGL = '***' WHERE SGL NOT IN " + alleSchulgliederungen,
			Schema.tab_EigeneSchule_Jahrgaenge
		);
		// TODO Prüfe
		add("Überprüfung der Schulgliederung",
			"UPDATE Schueler SET ASDSchulform = '***' WHERE ASDSchulform NOT IN " + alleSchulgliederungen,
			Schema.tab_Schueler
		);
		add("Anpassen des Fremdschlüssels für den Fremdschluessel von K_AllgAdresse auf die ID von K_AdressArt statt der Bezeichnung",
			"""
			UPDATE K_AllgAdresse
			SET AdressArt_ID = (
			    SELECT K_Adressart.ID
			    FROM K_Adressart
			    WHERE K_Adressart.Bezeichnung = K_AllgAdresse.AllgAdrAdressArt LIMIT 1
			)
			""",
			Schema.tab_K_AllgAdresse, Schema.tab_K_Adressart
		);
		add("Entferne Einzelleistung, wenn diese nicht auf einen Leistungsdatensatz verweist",
			"""
			DELETE FROM SchuelerEinzelleistungen
			WHERE Leistung_ID NOT IN (
			    SELECT ID
			    FROM SchuelerLeistungsdaten
			)
			""",
			Schema.tab_SchuelerEinzelleistungen, Schema.tab_SchuelerLeistungsdaten
		);
		add("Setze die Art der Einzelleistung auf NULL, wenn diese nicht auf einen Datensatz der Tabelle K_Einzelleistungen verweist",
			"""
			DELETE FROM SchuelerEinzelleistungen
			WHERE Art_ID NOT IN (
			    SELECT ID
			    FROM K_Einzelleistungen
			)
			""",
			Schema.tab_SchuelerEinzelleistungen, Schema.tab_K_Einzelleistungen
		);
		add("Setze den Lehrer, welcher der Einzelleistung zugeordnet ist auf NULL, wenn dieser nicht in der Tabelle K_Lehrer vorkommt",
			"""
			DELETE FROM SchuelerEinzelleistungen
			WHERE Lehrer_ID NOT IN (
			    SELECT ID
			    FROM K_Lehrer
			)
			""",
			Schema.tab_SchuelerEinzelleistungen, Schema.tab_K_Lehrer
		);
		add("Ändere den Eintrag WechselNr von 999 auf NULL für den aktuellen Abschnitt (ggf. nach dem letzten Wechsel)",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET WechselNr = NULL
			WHERE WechselNr=999
			""",
			Schema.tab_SchuelerLernabschnittsdaten
		);
	}

	private void pruefeAbiturtabellen() {
		add("Überprüfung des Noten-Eintrages P11_1",
			"""
			UPDATE SchuelerAbiFaecher
			SET P11_1 = null
			WHERE P11_1 IS NOT NULL
			    AND P11_1 NOT IN ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','AT')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages P11_2",
			"""
			UPDATE SchuelerAbiFaecher
			SET P11_2 = null
			WHERE P11_2 IS NOT NULL
			    AND P11_2 NOT IN ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','AT')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages P12_1",
			"""
			UPDATE SchuelerAbiFaecher
			SET P12_1 = null
			WHERE P12_1 IS NOT NULL
			    AND P12_1 NOT IN ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','AT')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages P12_2",
			"""
			UPDATE SchuelerAbiFaecher
			SET P12_2 = null
			WHERE P12_2 IS NOT NULL
			    AND P12_2 NOT IN ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','AT')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages P13_1",
			"""
			UPDATE SchuelerAbiFaecher
			SET P13_1 = null
			WHERE P13_1 IS NOT NULL
			    AND P13_1 NOT IN ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','AT')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages P13_2",
			"""
			UPDATE SchuelerAbiFaecher
			SET P13_2 = null
			WHERE P13_2 IS NOT NULL
			    AND P13_2 NOT IN ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','AT')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages P_FA",
			"""
			UPDATE SchuelerAbiFaecher
			SET P_FA = null
			WHERE P_FA IS NOT NULL
			    AND P_FA NOT IN ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages AbiPruefErgebnis",
			"""
			UPDATE SchuelerAbiFaecher
			SET AbiPruefErgebnis = null
			WHERE AbiPruefErgebnis IS NOT NULL
			    AND (AbiPruefErgebnis < 0 OR AbiPruefErgebnis > 15)
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages MdlPruefErgebnis",
			"""
			UPDATE SchuelerAbiFaecher
			SET MdlPruefErgebnis = null
			WHERE MdlPruefErgebnis IS NOT NULL
			    AND (MdlPruefErgebnis < 0 OR MdlPruefErgebnis > 15)
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Noten-Eintrages BLL_Punkte",
			"""
			UPDATE SchuelerAbitur
			SET BLL_Punkte = null
			WHERE BLL_Punkte IS NOT NULL
			    AND (BLL_Punkte < 0 OR BLL_Punkte > 15)
			""",
			Schema.tab_SchuelerAbitur
		);
		add("Tabelle SchuelerAbiFaecher - Q1.1: Umwandeln von L in S bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S12_1='S' WHERE S12_1='L'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Tabelle SchuelerAbiFaecher - Q1.2: Umwandeln von L in S bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S12_2='S' WHERE S12_2='L'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Tabelle SchuelerAbiFaecher - Q2.1: Umwandeln von L in S bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S13_1='S' WHERE S13_1='L'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Tabelle SchuelerAbiFaecher - Q2.2: Umwandeln von L in S bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S13_2='S' WHERE S13_2='L'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Tabelle SchuelerAbiFaecher - Q1.1: Umwandeln von Z in M bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S12_1='M' WHERE S12_1='Z'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Tabelle SchuelerAbiFaecher - Q1.2: Umwandeln von Z in M bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S12_2='M' WHERE S12_2='Z'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Tabelle SchuelerAbiFaecher - Q2.1: Umwandeln von Z in M bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S13_1='M' WHERE S13_1='Z'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Tabelle SchuelerAbiFaecher - Q2.2: Umwandeln von Z in M bei der Schriftlichkeit",
			"""
			UPDATE SchuelerAbiFaecher SET S13_2='M' WHERE S13_2='Z'
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Belegungsart S11_1",
			"""
			UPDATE SchuelerAbiFaecher SET S11_1 = '-' WHERE S11_1 IS NULL OR S11_1 NOT IN ('-','M','S')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Belegungsart S11_2",
			"""
			UPDATE SchuelerAbiFaecher SET S11_2 = '-' WHERE S11_2 IS NULL OR S11_2 NOT IN ('-','M','S')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Belegungsart S12_1",
			"""
			UPDATE SchuelerAbiFaecher SET S12_1 = '-' WHERE S12_1 IS NULL OR S12_1 NOT IN ('-','M','S')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Belegungsart S12_2",
			"""
			UPDATE SchuelerAbiFaecher SET S12_2 = '-' WHERE S12_2 IS NULL OR S12_2 NOT IN ('-','M','S')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Belegungsart S13_1",
			"""
			UPDATE SchuelerAbiFaecher SET S13_1 = '-' WHERE S13_1 IS NULL OR S13_1 NOT IN ('-','M','S')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Belegungsart S13_2",
			"""
			UPDATE SchuelerAbiFaecher SET S13_2 = '-' WHERE S13_2 IS NULL OR S13_2 NOT IN ('-','M','S')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Kurs-Markierung R12_1",
			"""
			UPDATE SchuelerAbiFaecher SET R12_1 = null WHERE R12_1 IS NOT NULL AND R12_1 NOT IN ('-','+','/')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Kurs-Markierung R12_2",
			"""
			UPDATE SchuelerAbiFaecher SET R12_2 = null WHERE R12_2 IS NOT NULL AND R12_2 NOT IN ('-','+','/')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Kurs-Markierung R13_1",
			"""
			UPDATE SchuelerAbiFaecher SET R13_1 = null WHERE R13_1 IS NOT NULL AND R13_1 NOT IN ('-','+','/')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Kurs-Markierung R13_2",
			"""
			UPDATE SchuelerAbiFaecher SET R13_2 = null WHERE R13_2 IS NOT NULL AND R13_2 NOT IN ('-','+','/')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung des Abiturfaches",
			"""
			UPDATE SchuelerAbiFaecher SET AbiFach = null WHERE AbiFach IS NOT NULL AND AbiFach NOT IN ('1','2','3','4')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
		add("Überprüfung der Art von Besonderen Lernleistungen",
			"""
			UPDATE SchuelerAbitur SET BLL_Art = 'K' WHERE BLL_Art IS NOT NULL AND BLL_Art NOT IN ('K','P','E')
			""",
			Schema.tab_SchuelerAbitur
		);
		add("Überprüfung der allgemeinen Kurs-Art der Abiturfächer",
			"""
			UPDATE SchuelerAbiFaecher SET KursartAllg = null WHERE KursartAllg IS NOT NULL AND KursartAllg NOT IN ('LK','GK','ZK','PJK','VTF')
			""",
			Schema.tab_SchuelerAbiFaecher
		);
	}


	private void erstelleNeueKlassenTabelle() {
		add("Tabelle Klassen: Erstellung der aktuellen Einträge aus der Versetzungstabelle",
			"""
			INSERT INTO Klassen(ID, Schuljahresabschnitts_ID, Bezeichnung, ASDKlasse, Klasse, Jahrgang_ID, FKlasse, VKlasse, OrgFormKrz, ASDSchulformNr,
			    Fachklasse_ID, PruefOrdnung, Sichtbar, Sortierung, Klassenart, SommerSem, NotenGesperrt, AdrMerkmal, KoopKlasse, Ankreuzzeugnisse)
			SELECT
			    Versetzung.ID,
			    EigeneSchule.Schuljahresabschnitts_ID,
			    Versetzung.Bezeichnung,
			    Versetzung.ASDKlasse,
			    Versetzung.Klasse,
			    Versetzung.Jahrgang_ID,
			    Versetzung.FKlasse,
			    Versetzung.VKlasse,
			    Versetzung.OrgFormKrz,
			    Versetzung.ASDSchulformNr,
			    Versetzung.Fachklasse_ID,
			    Versetzung.PruefOrdnung,
			    Versetzung.Sichtbar,
			    Versetzung.Sortierung,
			    Versetzung.Klassenart,
			    Versetzung.SommerSem,
			    Versetzung.NotenGesperrt,
			    Versetzung.AdrMerkmal,
			    Versetzung.KoopKlasse,
			    Versetzung.Ankreuzzeugnisse
			FROM Versetzung JOIN EigeneSchule
			""",
			Schema.tab_Klassen, Schema.tab_Versetzung, Schema.tab_EigeneSchule
		);
		add("Tabelle KlassenLehrer: Erstellung der aktuellen Einträge aus der Versetzungstabelle",
			"""
			INSERT INTO KlassenLehrer(Klassen_ID, Lehrer_ID, Reihenfolge)
			((
			    SELECT
			        Versetzung.ID AS Klassen_ID,
			        K_Lehrer.ID AS Lehrer_ID,
			        1 AS Reihenfolge
			    FROM
			        Versetzung JOIN K_Lehrer ON Versetzung.KlassenlehrerKrz = K_Lehrer.Kuerzel
			    WHERE
			        Versetzung.KlassenlehrerKrz IS NOT NULL
			) UNION (
			    SELECT
			        Versetzung.ID AS Klassen_ID,
			        K_Lehrer.ID AS Lehrer_ID,
			        2 AS Reihenfolge
			    FROM
			        Versetzung JOIN K_Lehrer
			            ON Versetzung.StvKlassenlehrerKrz = K_Lehrer.Kuerzel
			                AND Versetzung.StvKlassenlehrerKrz <> Versetzung.KlassenlehrerKrz
			    WHERE
			        Versetzung.StvKlassenlehrerKrz IS NOT NULL
			))
			""",
			Schema.tab_KlassenLehrer, Schema.tab_Versetzung, Schema.tab_K_Lehrer
		);
		add("Tabelle Klassen: Erstellung Einträge für die übrigen Schuljahresabschnitte basierend auf der Versetzungstabelle",
			"""
			INSERT INTO Klassen(Schuljahresabschnitts_ID, Bezeichnung, ASDKlasse, Klasse, Jahrgang_ID, FKlasse, VKlasse, OrgFormKrz, ASDSchulformNr,
			    Fachklasse_ID, PruefOrdnung, Sichtbar, Sortierung, Klassenart, SommerSem, NotenGesperrt, AdrMerkmal, KoopKlasse, Ankreuzzeugnisse)
			SELECT
			    Schuljahresabschnitte.ID,
			    Versetzung.Bezeichnung,
			    Versetzung.ASDKlasse,
			    Versetzung.Klasse,
			    Versetzung.Jahrgang_ID,
			    Versetzung.FKlasse,
			    Versetzung.VKlasse,
			    Versetzung.OrgFormKrz,
			    Versetzung.ASDSchulformNr,
			    Versetzung.Fachklasse_ID,
			    Versetzung.PruefOrdnung,
			    Versetzung.Sichtbar,
			    Versetzung.Sortierung,
			    Versetzung.Klassenart,
			    Versetzung.SommerSem,
			    Versetzung.NotenGesperrt,
			    Versetzung.AdrMerkmal,
			    Versetzung.KoopKlasse,
			    Versetzung.Ankreuzzeugnisse
			FROM
			    Versetzung, Schuljahresabschnitte
			WHERE
			    Schuljahresabschnitte.ID NOT IN (
			        SELECT Schuljahresabschnitts_ID
			        FROM EigeneSchule
			)
			""",
			Schema.tab_Klassen, Schema.tab_Versetzung, Schema.tab_Schuljahresabschnitte, Schema.tab_EigeneSchule
		);
		add("Tabelle KlassenLehrer: Erstellung Einträge für die übrigen Schuljahresabschnitte basierend auf den Schüler-Abschnittsdaten",
			"""
			INSERT INTO KlassenLehrer(Klassen_ID, Lehrer_ID, Reihenfolge)
			SELECT
			    Klassen_ID,
			    Lehrer_ID,
			    ROW_NUMBER() OVER (PARTITION BY Klassen_ID ORDER BY sum(Anzahl) DESC, Lehrer_ID) AS Reihenfolge
			FROM
			    ((
			        SELECT
			            Klassen.ID AS Klassen_ID,
			            K_Lehrer.ID AS Lehrer_ID,
			            count(*)*2 AS Anzahl
			        FROM SchuelerLernabschnittsdaten
			            JOIN Klassen ON SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID
			                AND SchuelerLernabschnittsdaten.Klasse = Klassen.Klasse
			            JOIN K_Lehrer ON SchuelerLernabschnittsdaten.Klassenlehrer = K_Lehrer.Kuerzel
			        WHERE SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID NOT IN (
			            SELECT Schuljahresabschnitts_ID
			            FROM EigeneSchule
			        )
			        GROUP BY Klassen.ID, K_Lehrer.ID
			    ) UNION (
			        SELECT
			            Klassen.ID AS Klassen_ID,
			            K_Lehrer.ID AS Lehrer_ID,
			            count(*) AS Anzahl
			        FROM SchuelerLernabschnittsdaten
			            JOIN Klassen ON SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID
			                AND SchuelerLernabschnittsdaten.Klasse = Klassen.Klasse
			            JOIN K_Lehrer ON SchuelerLernabschnittsdaten.StvKlassenlehrer_ID = K_Lehrer.ID
			        WHERE SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID NOT IN (
			            SELECT Schuljahresabschnitts_ID
			            FROM EigeneSchule
			        )
			        GROUP BY Klassen.ID, K_Lehrer.ID
			    )) a
			GROUP BY Klassen_ID, Lehrer_ID
			ORDER BY Klassen_ID, Anzahl DESC, Lehrer_ID
			""",
			Schema.tab_KlassenLehrer, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen, Schema.tab_EigeneSchule, Schema.tab_K_Lehrer
		);
		add("Tabelle KlassenLehrer: Erstellung Einträge für die übrigen Schuljahresabschnitte basierend auf den Schüler-Abschnittsdaten",
			"""
			INSERT INTO KlassenLehrer(Klassen_ID, Lehrer_ID, Reihenfolge)
			SELECT
			    Klassen_ID,
			    Lehrer_ID,
			    ROW_NUMBER() OVER (PARTITION BY Klassen_ID ORDER BY sum(Anzahl) DESC, Lehrer_ID) + 2 AS Reihenfolge
			FROM
			    ((
			        SELECT
			            Klassen.ID AS Klassen_ID,
			            K_Lehrer.ID AS Lehrer_ID,
			            count(*)*2 AS Anzahl
			        FROM SchuelerLernabschnittsdaten
			            JOIN Klassen ON SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID
			                AND SchuelerLernabschnittsdaten.Klasse = Klassen.Klasse
			            JOIN K_Lehrer ON SchuelerLernabschnittsdaten.Klassenlehrer = K_Lehrer.Kuerzel
			        WHERE SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID IN (
			            SELECT Schuljahresabschnitts_ID
			            FROM EigeneSchule
			        ) AND (Klassen.ID, K_Lehrer.ID) NOT IN (SELECT Klassen_ID, Lehrer_ID FROM KlassenLehrer)
			        GROUP BY Klassen.ID, K_Lehrer.ID
			    ) UNION (
			        SELECT
			            Klassen.ID AS Klassen_ID,
			            K_Lehrer.ID AS Lehrer_ID,
			            count(*) AS Anzahl
			        FROM SchuelerLernabschnittsdaten
			            JOIN Klassen ON SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID
			                AND SchuelerLernabschnittsdaten.Klasse = Klassen.Klasse
			            JOIN K_Lehrer ON SchuelerLernabschnittsdaten.StvKlassenlehrer_ID = K_Lehrer.ID
			        WHERE SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID IN (
			            SELECT Schuljahresabschnitts_ID
			            FROM EigeneSchule
			        ) AND (Klassen.ID, K_Lehrer.ID) NOT IN (SELECT Klassen_ID, Lehrer_ID FROM KlassenLehrer)
			        GROUP BY Klassen.ID, K_Lehrer.ID
			    )) a
			GROUP BY Klassen_ID, Lehrer_ID
			ORDER BY Klassen_ID, Anzahl DESC, Lehrer_ID
			""",
			Schema.tab_KlassenLehrer, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen, Schema.tab_EigeneSchule, Schema.tab_K_Lehrer
		);
	}

	private void passeLehrerTabelleAn() {
		add("Tabelle Kurse: Umstellen von Lehrer-Kürzel auf Lehrer-ID",
			"""
			UPDATE Kurse
			    JOIN K_Lehrer ON Kurse.LehrerKrz = K_Lehrer.Kuerzel
			SET Kurse.Lehrer_ID = K_Lehrer.ID
			""",
			Schema.tab_Kurse, Schema.tab_K_Lehrer
		);
		add("Tabelle SchuelerAbiFaecher: Umstellen von Lehrer-Kürzel auf Lehrer-ID",
			"""
			UPDATE SchuelerAbiFaecher
			    JOIN K_Lehrer ON SchuelerAbiFaecher.Fachlehrer = K_Lehrer.Kuerzel
			SET SchuelerAbiFaecher.Fachlehrer_ID = K_Lehrer.ID
			""",
			Schema.tab_SchuelerAbiFaecher, Schema.tab_K_Lehrer
		);
		add("Tabelle SchuelerBKFaecher: Umstellen von Lehrer-Kürzel auf Lehrer-ID",
			"""
			UPDATE SchuelerBKFaecher
			    JOIN K_Lehrer ON SchuelerBKFaecher.Fachlehrer = K_Lehrer.Kuerzel
			SET SchuelerBKFaecher.Fachlehrer_ID = K_Lehrer.ID
			""",
			Schema.tab_SchuelerBKFaecher, Schema.tab_K_Lehrer
		);
		add("Tabelle SchuelerLeistungsdaten: Umstellen von Lehrer-Kürzel auf Lehrer-ID (1)",
			"""
			UPDATE SchuelerLeistungsdaten
			    JOIN K_Lehrer ON SchuelerLeistungsdaten.Fachlehrer = K_Lehrer.Kuerzel
			SET SchuelerLeistungsdaten.Fachlehrer_ID = K_Lehrer.ID
			""",
			Schema.tab_SchuelerLeistungsdaten, Schema.tab_K_Lehrer
		);
		add("Tabelle SchuelerLeistungsdaten: Umstellen von Lehrer-Kürzel auf Lehrer-ID (2) - Zusatzkraft",
			"""
			UPDATE SchuelerLeistungsdaten
			    JOIN K_Lehrer ON SchuelerLeistungsdaten.Zusatzkraft = K_Lehrer.Kuerzel
			SET SchuelerLeistungsdaten.Zusatzkraft_ID = K_Lehrer.ID
			""",
			Schema.tab_SchuelerLeistungsdaten, Schema.tab_K_Lehrer
		);
		add("Tabelle Stundentafel_Faecher: Umstellen von Lehrer-Kürzel auf Lehrer-ID",
			"""
			UPDATE Stundentafel_Faecher
			    JOIN K_Lehrer ON Stundentafel_Faecher.LehrerKrz = K_Lehrer.Kuerzel
			SET Stundentafel_Faecher.Lehrer_ID = K_Lehrer.ID
			""",
			Schema.tab_Stundentafel_Faecher, Schema.tab_K_Lehrer
		);
		add("Tabelle EigeneSchule_Abteilungen: Umstellen von Lehrer-Kürzel auf Lehrer-ID",
			"""
			UPDATE EigeneSchule_Abteilungen
			    JOIN K_Lehrer ON EigeneSchule_Abteilungen.AbteilungsLeiter = K_Lehrer.Kuerzel
			SET EigeneSchule_Abteilungen.AbteilungsLeiter_ID = K_Lehrer.ID
			""",
			Schema.tab_EigeneSchule_Abteilungen, Schema.tab_K_Lehrer
		);
	}


	private void pruefeWeitereDaten2() {
		add("ZP10: Kopiere Daten aus SchuelerBKFaecher nach SchuelerZP10",
			"""
			INSERT INTO SchuelerZP10(ID, Schueler_ID, Schuljahresabschnitts_ID, Fach_ID, Vornote, NoteSchriftlich,
			    MdlPruefung, MdlPruefungFW, NoteMuendlich, NoteAbschluss, Fachlehrer_ID)
			SELECT
			    SchuelerBKFaecher.ID,
			    SchuelerBKFaecher.Schueler_ID,
			    SchuelerBKFaecher.Schuljahresabschnitts_ID,
			    SchuelerBKFaecher.Fach_ID,
			    SchuelerBKFaecher.Vornote,
			    SchuelerBKFaecher.NoteSchriftlich,
			    SchuelerBKFaecher.MdlPruefung,
			    SchuelerBKFaecher.MdlPruefungFW,
			    SchuelerBKFaecher.NoteMuendlich,
			    SchuelerBKFaecher.NoteAbschluss,
			    SchuelerBKFaecher.Fachlehrer_ID
			FROM SchuelerBKFaecher
			    JOIN EigeneSchule ON EigeneSchule.SchulformKrz NOT IN ('BK','SB')
			""",
			Schema.tab_SchuelerZP10, Schema.tab_SchuelerBKFaecher, Schema.tab_EigeneSchule
		);
		add("BKFaecher: Leere die Tabelle, falls die Schulform nicht BK oder SB ist",
			"""
			DELETE FROM SchuelerBKFaecher
			WHERE (
			    SELECT ID
			    FROM EigeneSchule
			    WHERE EigeneSchule.SchulformKrz NOT IN ('BK','SB')
			)
			""",
			Schema.tab_SchuelerBKFaecher, Schema.tab_EigeneSchule
		);
		add("BKAbschluss: Leere die Tabelle, falls die Schulform nicht BK oder SB ist",
			"""
			DELETE FROM SchuelerBKAbschluss
			WHERE (
			    SELECT ID
			    FROM EigeneSchule
			    WHERE EigeneSchule.SchulformKrz NOT IN ('BK','SB')
			)
			""",
			Schema.tab_SchuelerBKAbschluss, Schema.tab_EigeneSchule
		);
		// TODO verschiebe nach erstelleNeueKlassenTabelle
		add("Schüler-Klassen-Zuordnung: Entferne alle Einträge, die nicht in der neuen Klassentabelle vorhanden sind und wo daher keine Zuordnung machbar ist",
			"""
			UPDATE Schueler
			SET Klasse = null
			WHERE (Schuljahresabschnitts_ID, Klasse) NOT IN (
			    SELECT
			        Schuljahresabschnitts_ID,
			        Klasse
			    FROM
			        Klassen
			)
			""",
			Schema.tab_Schueler, Schema.tab_Klassen
		);

		// TODO verschiebe nach erstelleNeueKlassenTabelle
		add("Schüler-Klassen-Zuordnung: Bestimme die Klassen-ID des Schülers und setze diese",
			"""
			UPDATE Schueler
			    JOIN Klassen ON (Schueler.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID)
			        AND (Schueler.Klasse = Klassen.Klasse)
			SET
			    Schueler.Klassen_ID = Klassen.ID
			""",
			Schema.tab_Schueler, Schema.tab_Klassen
		);
		// TODO verschiebe nach erstelleNeueKlassenTabelle
		add("SchuelerFoerderempfehlungen-Klassen-Zuordnung: Entferne alle Einträge, die nicht in der neuen Klassentabelle vorhanden sind und wo daher keine Zuordnung machbar ist",
			"""
			UPDATE SchuelerFoerderempfehlungen
			    JOIN SchuelerLernabschnittsdaten ON SchuelerFoerderempfehlungen.Abschnitt_ID = SchuelerLernabschnittsdaten.ID
			SET
			    SchuelerFoerderempfehlungen.Klasse = null
			WHERE (SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID, SchuelerFoerderempfehlungen.Klasse) NOT IN (
			    SELECT
			        Schuljahresabschnitts_ID,
			        Klasse
			    FROM
			        Klassen
			    )
			""",
			Schema.tab_SchuelerFoerderempfehlungen, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);
		// TODO verschiebe nach erstelleNeueKlassenTabelle
		add("SchuelerFoerderempfehlungen-Klassen-Zuordnung: Bestimme die Klassen-ID der Schüler-Förderempfehlung und setze diese",
			"""
			UPDATE SchuelerFoerderempfehlungen
			    JOIN SchuelerLernabschnittsdaten ON SchuelerFoerderempfehlungen.Abschnitt_ID = SchuelerLernabschnittsdaten.ID
			    JOIN Klassen ON (SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID)
			        AND (SchuelerFoerderempfehlungen.Klasse = Klassen.Klasse)
			SET
			    SchuelerFoerderempfehlungen.Klassen_ID = Klassen.ID
			""",
			Schema.tab_SchuelerFoerderempfehlungen, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);
		// TODO verschiebe nach erstelleNeueKlassenTabelle
		add("Schüler-Lernabschnitt-Klassen-Zuordnung: Entferne alle Einträge, die nicht in der neuen Klassentabelle vorhanden sind und wo daher keine Zuordnung machbar ist",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Klasse = null
			WHERE (Schuljahresabschnitts_ID, Klasse) NOT IN (
			    SELECT
			        Schuljahresabschnitts_ID,
			        Klasse
			    FROM
			        Klassen
			    )
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);
		// TODO verschiebe nach erstelleNeueKlassenTabelle
		add("Schüler-Lernabschnitt-Klassen-Zuordnung: Bestimme die Klassen-ID des Schüler-Lernabschnitts und setze diese",
			"""
			UPDATE SchuelerLernabschnittsdaten
			    JOIN Klassen ON (SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID)
			        AND (SchuelerLernabschnittsdaten.Klasse = Klassen.Klasse)
			SET
			    SchuelerLernabschnittsdaten.Klassen_ID = Klassen.ID
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);

		add("Schuljahresabschnitte: Setzen der Abschnitts-ID des vorigen Abschnitts",
			"""
			UPDATE Schuljahresabschnitte s
			    JOIN Schuljahresabschnitte t
			        ON t.Jahr = CASE WHEN s.Abschnitt - 1 < 1 THEN s.Jahr - 1 ELSE s.Jahr END
			        AND t.Abschnitt = CASE WHEN s.Abschnitt - 1 < 1 THEN (SELECT AnzahlAbschnitte FROM EigeneSchule) ELSE s.Abschnitt - 1 END
			SET s.VorigerAbschnitt_ID = t.ID
			""",
			Schema.tab_Schuljahresabschnitte
		);
		add("Schuljahresabschnitte: Setzen der Folge-Abschnitts-ID",
			"""
			UPDATE Schuljahresabschnitte s
			    JOIN Schuljahresabschnitte t
			        ON t.Jahr = CASE WHEN s.Abschnitt + 1 > (SELECT AnzahlAbschnitte FROM EigeneSchule) THEN s.Jahr + 1 ELSE s.Jahr END
			        AND t.Abschnitt = CASE WHEN s.Abschnitt + 1 > (SELECT AnzahlAbschnitte FROM EigeneSchule) THEN 1 ELSE s.Abschnitt + 1 END
			SET s.FolgeAbschnitt_ID = t.ID
			""",
			Schema.tab_Schuljahresabschnitte
		);
		add("Schüler-Lernabschnitt-Folge-Klassen-Zuordnung: Entferne alle Einträge, die nicht in der neuen Klassentabelle vorhanden sind und wo daher keine Zuordnung machbar ist",
			"""
			UPDATE SchuelerLernabschnittsdaten
			SET Folgeklasse = null
			WHERE (Schuljahresabschnitts_ID, Folgeklasse) NOT IN (
			    SELECT
			        Schuljahresabschnitts_ID,
			        Klasse
			    FROM
			        Klassen
			    )
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);
		add("Schüler-Lernabschnitt-Folge-Klassen-Zuordnung: Entferne alle Einträge, die dem Standardeintrag in der neuen Klassentabelle entsprechen",
			"""
			UPDATE SchuelerLernabschnittsdaten
			    JOIN Klassen ON (SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID)
			        AND (SchuelerLernabschnittsdaten.Klasse = Klassen.Klasse)
			        AND (SchuelerLernabschnittsdaten.Folgeklasse = Klassen.FKlasse)
			SET Folgeklasse = null
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);
		add("Schüler-Lernabschnitt-Folge-Klassen-Zuordnung: Entferne den Folgeklassen-Eintrag, falls der nächste Lernabschnitt noch nicht definiert ist",
			"""
			UPDATE SchuelerLernabschnittsdaten
			    JOIN Schuljahresabschnitte ON (SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID)
			        AND (Schuljahresabschnitte.FolgeAbschnitt_ID IS NULL)
			        AND (SchuelerLernabschnittsdaten.Folgeklasse IS NOT NULL)
			SET Folgeklasse = NULL
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);
		add("Schüler-Lernabschnitt-Folge-Klassen-Zuordnung: Bestimme die FolgeKlassen-ID des Schüler-Lernabschnitts und setze diese",
			"""
			UPDATE SchuelerLernabschnittsdaten
			    JOIN Schuljahresabschnitte ON (SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID)
			        AND (SchuelerLernabschnittsdaten.Folgeklasse IS NOT NULL)
			        AND (Schuljahresabschnitte.FolgeAbschnitt_ID IS NOT NULL)
			    JOIN Klassen ON (Schuljahresabschnitte.FolgeAbschnitt_ID = Klassen.Schuljahresabschnitts_ID)
			        AND (SchuelerLernabschnittsdaten.Folgeklasse = Klassen.Klasse)
			SET Folgeklasse_ID = Klassen.ID
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Schuljahresabschnitte, Schema.tab_Klassen
		);
		add("Abteilungen: Ergänze das aktuelle Schuljahr der Schule, um die Gültigkeit der Abteilungsangaben festzulegen",
			"""
			UPDATE EigeneSchule_Abteilungen
			SET Schuljahresabschnitts_ID = (
			    SELECT Schuljahresabschnitts_ID
			    FROM EigeneSchule
			)
			""",
			Schema.tab_EigeneSchule_Abteilungen, Schema.tab_EigeneSchule
		);
		add("SchuelerLernabschnittsdaten: Erzeuge leere Abschnitte bei Schülern ohne aktuellen Lernabschnitt",
			"""
			INSERT INTO SchuelerLernabschnittsdaten(
			    Schueler_ID, Schuljahresabschnitts_ID, WechselNr, Schulbesuchsjahre, Hochrechnung, SemesterWertung, PruefOrdnung, Klassen_ID, Verspaetet,
			    NPV_Fach_ID, NPV_NoteKrz, NPV_Datum, NPAA_Fach_ID, NPAA_NoteKrz, NPAA_Datum, NPBQ_Fach_ID, NPBQ_NoteKrz, NPBQ_Datum, VersetzungKrz, AbschlussArt,
			    AbschlIstPrognose, Konferenzdatum, ZeugnisDatum, ASDSchulgliederung, ASDJahrgang, Jahrgang_ID, Fachklasse_ID, Schwerpunkt_ID, ZeugnisBem,
			    Schwerbehinderung, Foerderschwerpunkt_ID, OrgFormKrz, RefPaed, Klassenart, SumFehlStd, SumFehlStdU, Wiederholung, Gesamtnote_GS, Gesamtnote_NW, Folgeklasse_ID,
			    Foerderschwerpunkt2_ID, Abschluss, Abschluss_B, DSNote, AV_Leist, AV_Zuv, AV_Selbst, SV_Verant, SV_Konfl, SV_Koop, MoeglNPFaecher,
			    Zertifikate, DatumFHR, PruefAlgoErgebnis, Zeugnisart, DatumVon, DatumBis, FehlstundenGrenzwert, Sonderpaedagoge_ID, FachPraktAnteilAusr, BilingualerZweig,
			    Jahr, Abschnitt)
			SELECT
			    ID AS Schueler_ID,
			    Schuljahresabschnitts_ID,
			    NULL AS WechselNr,
			    Jahrgang AS Schulbesuchsjahre,
			    0 AS Hochrechnung,
			    '+' AS SemesterWertung,
			    PruefOrdnung,
			    Klassen_ID,
			    0 AS Verspaetet,
			    NULL AS NPV_Fach_ID,
			    NULL AS NPV_NoteKrz,
			    NULL AS NPV_Datum,
			    NULL AS NPAA_Fach_ID,
			    NULL AS NPAA_NoteKrz,
			    NULL AS NPAA_Datum,
			    NULL AS NPBQ_Fach_ID,
			    NULL AS NPBQ_NoteKrz,
			    NULL AS NPBQ_Datum,
			    NULL AS VersetzungKrz,
			    NULL AS AbschlussArt,
			    '-' AS AbschlIstPrognose,
			    NULL AS Konferenzdatum,
			    NULL AS ZeugnisDatum,
			    ASDSchulform AS ASDSchulgliederung,
			    ASDJahrgang,
			    Jahrgang_ID,
			    Fachklasse_ID,
			    Schwerpunkt_ID,
			    NULL AS ZeugnisBem,
			    Schwerbehinderung,
			    NULL AS Foerderschwerpunkt_ID,
			    OrgFormKrz,
			    RefPaed,
			    Klassenart,
			    NULL AS SumFehlStd,
			    NULL AS SumFehlStdU,
			    '-' AS Wiederholung,
			    NULL AS Gesamtnote_GS,
			    NULL AS Gesamtnote_NW,
			    NULL AS Folgeklasse_ID,
			    NULL AS Foerderschwerpunkt2_ID,
			    NULL AS Abschluss,
			    NULL AS Abschluss_B,
			    NULL AS DSNote,
			    NULL AS AV_Leist,
			    NULL AS AV_Zuv,
			    NULL AS AV_Selbst,
			    NULL AS SV_Verant,
			    NULL AS SV_Konfl,
			    NULL AS SV_Koop,
			    NULL AS MoeglNPFaecher,
			    NULL AS Zertifikate,
			    NULL AS DatumFHR,
			    NULL AS PruefAlgoErgebnis,
			    NULL AS Zeugnisart,
			    NULL AS DatumVon,
			    NULL AS DatumBis,
			    NULL AS FehlstundenGrenzwert,
			    NULL AS Sonderpaedagoge_ID,
			    '+' AS FachPraktAnteilAusr,
			    BilingualerZweig,
			    2000 AS Jahr,
			    1 AS Abschnitt
			FROM Schueler
			WHERE
			    (ID, Schuljahresabschnitts_ID) NOT IN (
			        SELECT Schueler_ID, Schuljahresabschnitts_ID FROM SchuelerLernabschnittsdaten)
			    AND Schuljahresabschnitts_ID IN (SELECT ID FROM Schuljahresabschnitte)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Schueler, Schema.tab_Schuljahresabschnitte
		);
		add("SchuelerLernabschnittsdaten: Setze das Feld Schulbesuchsjahre beim aktuellen Abschnitt",
			"""
			UPDATE SchuelerLernabschnittsdaten
			    JOIN Schueler ON (SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = Schueler.Schuljahresabschnitts_ID)
			        AND (SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID)
			SET SchuelerLernabschnittsdaten.Schulbesuchsjahre = Schueler.Jahrgang
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Schueler
		);
		add("SchuelerLernabschnittsdaten: Setze die Felder BilingualerZweig, AOSF, Autist, ZieldifferentesLernen bei allen Lernabschnitten eines Schülers",
			"""
			UPDATE SchuelerLernabschnittsdaten
			    JOIN Schueler ON (SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID)
			SET
			    SchuelerLernabschnittsdaten.BilingualerZweig = Schueler.BilingualerZweig,
			    SchuelerLernabschnittsdaten.AOSF = Schueler.AOSF,
			    SchuelerLernabschnittsdaten.Autist = Schueler.Autist,
			    SchuelerLernabschnittsdaten.ZieldifferentesLernen = Schueler.ZieldifferentesLernen
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Schueler
		);
		add("EigeneSchule_Abteilungen: Ergänzen der Abteilungsinformationen für die restlichen Schuljahresabschnitte",
			"""
			INSERT INTO EigeneSchule_Abteilungen(Bezeichnung, Schuljahresabschnitts_ID, AbteilungsLeiter_ID, Sichtbar, Raum, Email, Durchwahl, Sortierung)
			SELECT
			    abt.Bezeichnung,
			    abschnitt.ID AS Schuljahresabschnitts_ID,
			    abt.AbteilungsLeiter_ID,
			    abt.Sichtbar,
			    abt.Raum,
			    abt.Email,
			    abt.Durchwahl,
			    abt.Sortierung
			FROM
			    EigeneSchule_Abteilungen abt
			        JOIN (
			            SELECT *
			            FROM Schuljahresabschnitte
			            WHERE ID NOT IN (
			                SELECT Schuljahresabschnitts_ID
			                FROM EigeneSchule_Abteilungen
			            )
			        ) abschnitt
			""",
			Schema.tab_EigeneSchule_Abteilungen, Schema.tab_Schuljahresabschnitte
		);
		add("EigeneSchule_Abt_Kl: Ergänzen der Klasseninformation zu den Abteilungsinformationen für die restlichen Schuljahresabschnitte",
			"""
			INSERT INTO EigeneSchule_Abt_Kl(Abteilung_ID, Klasse, Klassen_ID, Sichtbar)
			SELECT
			    mapping.id_ersatz AS Abteilung_ID,
			    abt_kl.Klasse,
			    -1 AS Klassen_ID,
			    abt_kl.Sichtbar
			FROM EigeneSchule_Abt_Kl abt_kl
			    JOIN (
			        SELECT
			            a.ID AS id_orig,
			            b.ID AS id_ersatz
			        FROM (
			            (
			                SELECT * FROM EigeneSchule_Abteilungen WHERE ID IN (SELECT Abteilung_ID FROM EigeneSchule_Abt_Kl)
			            ) a JOIN (
			                SELECT * FROM EigeneSchule_Abteilungen WHERE ID NOT IN (SELECT Abteilung_ID FROM EigeneSchule_Abt_Kl)
			            ) b ON a.Bezeichnung = b.Bezeichnung
			        )
			    ) mapping ON abt_kl.Abteilung_ID = mapping.id_orig
			""",
			Schema.tab_EigeneSchule_Abt_Kl, Schema.tab_EigeneSchule_Abteilungen
		);
		add("EigeneSchule_Abt_Kl: Umstellung auf Klassen-IDs",
			"""
			UPDATE EigeneSchule_Abt_Kl
			    JOIN EigeneSchule_Abteilungen ON EigeneSchule_Abt_Kl.Abteilung_ID = EigeneSchule_Abteilungen.ID
			    JOIN Klassen ON EigeneSchule_Abt_Kl.Klasse = Klassen.Klasse
			        AND EigeneSchule_Abteilungen.Schuljahresabschnitts_ID = Klassen.Schuljahresabschnitts_ID
			SET
			    Klassen_ID = Klassen.ID
			""",
			Schema.tab_EigeneSchule_Abt_Kl, Schema.tab_EigeneSchule_Abteilungen, Schema.tab_Klassen
		);
		add("EigeneSchule_Faecher: Kopieren der Daten aus dem Feld Fachgruppe_ID in das Feld Zeugnisdatenquelle_ID",
			"""
			UPDATE EigeneSchule_Faecher
			SET Zeugnisdatenquelle_ID = Fachgruppe_ID
			WHERE Fachgruppe_ID IN (
			    SELECT SchildFgID
			    FROM Fachgruppen
			)
			""",
			Schema.tab_EigeneSchule_Faecher, Schema.tab_Fachgruppen
		);
		add("Korrigiere case bei Pseudonoten.",
			"""
			UPDATE SchuelerEinzelleistungen
			SET NotenKrz = upper(NotenKrz)
			""",
			Schema.tab_SchuelerEinzelleistungen
		);
	}


	private void passeBenutzerTabellenAn() {
		add("Users, Credentials: Übertrage die Password-Hashes in die Credentials-Tabelle",
			"""
			INSERT INTO Credentials(ID, Benutzername, PasswordHash)
			SELECT
			    ID,
			    US_LoginName,
			    US_PasswordHash
			FROM
			    Users
			""",
			Schema.tab_Credentials, Schema.tab_Users
		);
		add("BenutzerAllgemein: Kopiere alle nicht-Lehrer-Benutzer",
			"""
			INSERT INTO BenutzerAllgemein(ID, AnzeigeName, CredentialID)
			SELECT
			    ID,
			    US_Name,
			    ID
			FROM
			    Users
			WHERE
			    US_LoginName NOT IN (
			        SELECT Kuerzel
			        FROM K_Lehrer
			    )
			""",
			Schema.tab_BenutzerAllgemein, Schema.tab_Users, Schema.tab_K_Lehrer
		);
		add("K_Lehrer: Setze die Kennwortinformationen aus der Benutzertabelle",
			"""
			UPDATE K_Lehrer
			    JOIN Users ON Users.US_LoginName = K_Lehrer.Kuerzel
			SET
			    K_Lehrer.CredentialID = Users.ID
			""",
			Schema.tab_Users, Schema.tab_K_Lehrer
		);
		add("Benutzer: Erstelle Benutzer-Einträge für allgemeine Benutzer, die nicht in der Lehrer-Tabelle vorkommen",
			"""
			INSERT INTO Benutzer(ID, Typ, Allgemein_ID, IstAdmin)
			SELECT
			    ID,
			    0,
			    ID,
			    CASE WHEN INSTR(US_Privileges, '$') > 0 THEN 1 ELSE 0 END
			FROM
			    Users
			WHERE
			    US_LoginName NOT IN (
			        SELECT Kuerzel
			        FROM K_Lehrer
			    )
			""",
			Schema.tab_Benutzer, Schema.tab_Users, Schema.tab_K_Lehrer
		);
		add("Benutzer: Erstelle Benutzer-Einträge für Benutzer, die in der Lehrer-Tabelle vorkommen",
			"""
			INSERT INTO Benutzer(ID, Typ, Lehrer_ID, IstAdmin)
			SELECT
			    Users.ID,
			    1,
			    K_Lehrer.ID,
			    CASE WHEN INSTR(US_Privileges, '$') > 0 THEN 1 ELSE 0 END
			FROM Users
			    JOIN K_Lehrer ON Users.US_LoginName = K_Lehrer.Kuerzel
			""",
			Schema.tab_Benutzer, Schema.tab_Users, Schema.tab_K_Lehrer
		);
		add("BenutzerEmail: Kopiere Email-Informationen aus der Users-Tabelle",
			"""
			INSERT INTO BenutzerEmail(Benutzer_ID, Email, EmailName, SMTPUsername, SMTPPassword, EMailSignature, HeartbeatDate, ComputerName)
			SELECT
			    ID AS Benutzer_ID,
			    Email,
			    EmailName,
			    SMTPUsername,
			    SMTPPassword,
			    EmailSignature,
			    HeartbeatDate,
			    ComputerName
			FROM
			    Users
			WHERE
			    Email IS NOT NULL
			    AND EmailName IS NOT NULL
			""",
			Schema.tab_BenutzerEmail, Schema.tab_Users
		);
		add("Benutzergruppen: Übertrage die Benutzergruppen aus der Tabelle Usergroups",
			"""
			INSERT INTO Benutzergruppen(ID, Bezeichnung)
			SELECT
			    UG_ID,
			    UG_Bezeichnung
			FROM
			    Usergroups
			WHERE
			    UG_ID IS NOT NULL
			    AND UG_Bezeichnung IS NOT NULL
			""",
			Schema.tab_Benutzergruppen, Schema.tab_Usergroups
		);
		add("BenutzergruppenKompetenzen: Übertrage die Kompetenzen der Benutzergruppen aus der Tabelle Usergroups",
			"""
			INSERT INTO BenutzergruppenKompetenzen(Gruppe_ID, Kompetenz_ID)
			SELECT DISTINCT * FROM JSON_TABLE(
			    (
			        SELECT
			            CONCAT(
			                '[',
			                GROUP_CONCAT(
			                    '{"Gruppe_ID":',
			                    UG_ID,
			                    ',
			                    "Kompetenz_ID":',
			                    REPLACE(
			                        REPLACE(UG_Kompetenzen,'$','-1'),';',
			                        CONCAT(
			                            '}, {"Gruppe_ID":',
			                            UG_ID,
			                            ', "Kompetenz_ID":'
			                        )
			                    ),
			                    '}'
			                ),
			                ']'
			            )
			        FROM
			            Usergroups
			        WHERE
			            UG_Kompetenzen IS NOT NULL
			            AND TRIM(UG_Kompetenzen) <> ""
			    ),
			    '$[*]' columns(
			        Gruppe_ID bigint path '$.Gruppe_ID',
			        Kompetenz_ID bigint path '$.Kompetenz_ID'
			    )
			) AS GruppenKompetenzen
			""",
			Schema.tab_BenutzergruppenKompetenzen, Schema.tab_Usergroups
		);
		add("Benutzergruppen: Setze das Flag IstAdmin für administrative Gruppen",
			"""
			UPDATE Benutzergruppen
			    JOIN BenutzergruppenKompetenzen ON Benutzergruppen.ID = BenutzergruppenKompetenzen.Gruppe_ID
			        AND BenutzergruppenKompetenzen.Kompetenz_ID = -1
			SET Benutzergruppen.IstAdmin = 1
			""",
			Schema.tab_Benutzergruppen, Schema.tab_BenutzergruppenKompetenzen
		);
		final String strInsertBenutzerGruppenKompetenzen = "INSERT INTO " + Schema.tab_BenutzergruppenKompetenzen.name() + "(Gruppe_ID, Kompetenz_ID)";
        add(Schema.tab_BenutzergruppenKompetenzen.name() + ": Setze Default-Werte für die Stundenplanung anhand vorhandener Kompetenzen",
            strInsertBenutzerGruppenKompetenzen
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.LEHRERDATEN_ANSEHEN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.STUNDENPLAN_IMPORT.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.IMPORT_EXPORT_DATEN_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.STUNDENPLAN_EXPORT.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.STUNDENPLAN_ERSTELLEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.STUNDENPLAN_AKTIVIEREN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.ADMIN.daten.id + ")",
            Schema.tab_BenutzergruppenKompetenzen
        );
        add(Schema.tab_BenutzergruppenKompetenzen.name() + ": Setze Default-Werte für das Noten-Modul anhand der Kompetenzen zu den Schüler-Leistungsdaten",
            strInsertBenutzerGruppenKompetenzen
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.NOTENMODUL_ADMINISTRATION.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")",
            Schema.tab_BenutzergruppenKompetenzen
        );
        add(Schema.tab_BenutzergruppenKompetenzen.name() + ": Setze Default-Werte für das Datenbank-Management anhand der alten Kompetenz Backup durchführen",
            strInsertBenutzerGruppenKompetenzen
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.DATENBANK_SCHEMA_ERSTELLEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_BACKUP_DURCHFUEHREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.DATENBANK_SQLITE_IMPORT.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_BACKUP_DURCHFUEHREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.DATENBANK_SQLITE_EXPORT.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_BACKUP_DURCHFUEHREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")",
            Schema.tab_BenutzergruppenKompetenzen
        );
        add(Schema.tab_BenutzergruppenKompetenzen.name() + ": Setze Default-Werte für Oberstufenberechnungen anhand der alten Kompetenz zum Kurs42-Import",
            strInsertBenutzerGruppenKompetenzen
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_LUPO_IMPORT.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")",
            Schema.tab_BenutzergruppenKompetenzen
        );
        add(Schema.tab_BenutzergruppenKompetenzen.name() + ": Setze Default-Werte für Oberstufenberechnungen anhand der alten Kompetenz zum Kurs42-Import",
            strInsertBenutzerGruppenKompetenzen
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.ABITUR_AENDERN_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.ABITUR_AENDERN_FUNKTIONSBEZOGEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")"
            + " UNION "
            + "SELECT Gruppe_ID, " + BenutzerKompetenz.ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN.daten.id
            + " FROM " + Schema.tab_BenutzergruppenKompetenzen.name()
            + " WHERE Kompetenz_ID IN (" + BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN.daten.id + ", " + BenutzerKompetenz.ADMIN.daten.id + ")",
            Schema.tab_BenutzergruppenKompetenzen
        );
		add("BenutzergruppenKompetenzen: Entferne alte Admin-Einträge, da diese keine Verweise auf die Kompetenztabelle besitzen",
			"""
			DELETE FROM BenutzergruppenKompetenzen
			WHERE Kompetenz_ID = -1
			""",
			Schema.tab_BenutzergruppenKompetenzen
		);
		add("BenutzergruppenMitglieder: Füge die Benutzer aus der Tabelle Users zu den Benutzergruppen hinzu",
			"""
			INSERT INTO BenutzergruppenMitglieder(Gruppe_ID, Benutzer_ID)
			SELECT DISTINCT * FROM JSON_TABLE(
			    (
			        SELECT
			            CONCAT(
			                '[',
			                GROUP_CONCAT(
			                    '{"Gruppe_ID":',
			                    REPLACE(
			                        REPLACE(US_UserGroups,'$','-1'),
			                        ';',
			                        CONCAT(
			                            ', "Benutzer_ID": ',
			                            ID,
			                            '}, {"Gruppe_ID":'
			                        )
			                    ),
			                    ', "Benutzer_ID":',
			                    ID,
			                    '}'
			                ),
			                ']'
			            )
			        FROM
			            Users
			        WHERE
			            US_UserGroups IS NOT NULL
			            AND TRIM(US_UserGroups) <> ""
			    ),
			    '$[*]' columns(
			        Gruppe_ID bigint path '$.Gruppe_ID',
			        Benutzer_ID bigint path '$.Benutzer_ID'
			    )
			) AS GruppenMitglieder
			""",
			Schema.tab_BenutzergruppenMitglieder, Schema.tab_Users
		);
		add("BenutzergruppenMitglieder: Entferne fehlerhafte Einträge für Gruppen, die nicht existieren",
			"""
			DELETE FROM BenutzergruppenMitglieder
			WHERE Gruppe_ID NOT IN (
			    SELECT ID
			    FROM Benutzergruppen
			)
			""",
			Schema.tab_BenutzergruppenMitglieder, Schema.tab_Benutzergruppen
		);
		add("Benutzer: Setze das Flag IstAdmin für administrative Benutzer, falls es von einer Gruppenzuweisung geerbt wird",
			"""
			UPDATE Benutzer
			    JOIN BenutzergruppenMitglieder ON Benutzer.ID = BenutzergruppenMitglieder.Benutzer_ID
			    JOIN Benutzergruppen ON BenutzergruppenMitglieder.Gruppe_ID = Benutzergruppen.ID AND Benutzergruppen.IstAdmin = 1
			SET Benutzer.IstAdmin = 1
			""",
			Schema.tab_Benutzer, Schema.tab_BenutzergruppenMitglieder, Schema.tab_Benutzergruppen
		);
		add("BenutzerKompetenzen: Entferne alle Benutzerkompetenzen, die nicht in der Tabelle Kompetenzen definiert sind",
			"""
			DELETE FROM BenutzerKompetenzen
			WHERE Kompetenz_ID NOT IN (
			    SELECT KO_ID
			    FROM Kompetenzen
			)
			""",
			Schema.tab_BenutzerKompetenzen, Schema.tab_Kompetenzen
		);
		add("BenutzergruppenKompetenzen: Entferne alle Gruppenkompetenzen, die nicht in der Tabelle Kompetenzen definiert sind",
			"""
			DELETE FROM BenutzergruppenKompetenzen
			WHERE Kompetenz_ID NOT IN (
			    SELECT KO_ID
			    FROM Kompetenzen
			)
			""",
			Schema.tab_BenutzergruppenKompetenzen, Schema.tab_Kompetenzen
		);
	}

}
