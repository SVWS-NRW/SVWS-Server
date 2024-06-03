package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.StringToIntegerConverter;
import de.svws_nrw.db.converter.current.statkue.SchulformKuerzelConverter;

import de.svws_nrw.core.types.schule.Schulform;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.StringToIntegerConverterSerializer;
import de.svws_nrw.csv.converter.current.StringToIntegerConverterDeserializer;
import de.svws_nrw.csv.converter.current.statkue.SchulformKuerzelConverterSerializer;
import de.svws_nrw.csv.converter.current.statkue.SchulformKuerzelConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule")
@JsonPropertyOrder({"ID", "SchulformNr", "Schulform", "SchulformBez", "SchultraegerArt", "SchultraegerNr", "SchulNr", "Bezeichnung1", "Bezeichnung2", "Bezeichnung3", "Strassenname", "HausNr", "HausNrZusatz", "PLZ", "Ort", "Telefon", "Fax", "Email", "Ganztags", "Schuljahresabschnitts_ID", "AnzahlAbschnitte", "AbschnittBez", "BezAbschnitt1", "BezAbschnitt2", "BezAbschnitt3", "BezAbschnitt4", "Fremdsprachen", "JVAZeigen", "RefPaedagogikZeigen", "AnzJGS_Jahr", "IstHauptsitz", "NotenGesperrt", "ZurueckgestelltAnzahl", "ZurueckgestelltWeibl", "ZurueckgestelltAuslaender", "ZurueckgestelltAuslaenderWeibl", "ZurueckgestelltAussiedler", "ZurueckgestelltAussiedlerWeibl", "TeamTeaching", "AbiGruppenprozess", "DauerUnterrichtseinheit", "Gruppen8Bis1", "Gruppen13Plus", "InternatsplaetzeM", "InternatsplaetzeW", "InternatsplaetzeNeutral", "WebAdresse", "Land"})
public final class DTOEigeneSchule {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOEigeneSchule e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOEigeneSchule e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOEigeneSchule e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOEigeneSchule e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOEigeneSchule e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOEigeneSchule e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulformNr */
	public static final String QUERY_BY_SCHULFORMNR = "SELECT e FROM DTOEigeneSchule e WHERE e.SchulformNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulformNr */
	public static final String QUERY_LIST_BY_SCHULFORMNR = "SELECT e FROM DTOEigeneSchule e WHERE e.SchulformNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulform */
	public static final String QUERY_BY_SCHULFORM = "SELECT e FROM DTOEigeneSchule e WHERE e.Schulform = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulform */
	public static final String QUERY_LIST_BY_SCHULFORM = "SELECT e FROM DTOEigeneSchule e WHERE e.Schulform IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulformBez */
	public static final String QUERY_BY_SCHULFORMBEZ = "SELECT e FROM DTOEigeneSchule e WHERE e.SchulformBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulformBez */
	public static final String QUERY_LIST_BY_SCHULFORMBEZ = "SELECT e FROM DTOEigeneSchule e WHERE e.SchulformBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchultraegerArt */
	public static final String QUERY_BY_SCHULTRAEGERART = "SELECT e FROM DTOEigeneSchule e WHERE e.SchultraegerArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchultraegerArt */
	public static final String QUERY_LIST_BY_SCHULTRAEGERART = "SELECT e FROM DTOEigeneSchule e WHERE e.SchultraegerArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchultraegerNr */
	public static final String QUERY_BY_SCHULTRAEGERNR = "SELECT e FROM DTOEigeneSchule e WHERE e.SchultraegerNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchultraegerNr */
	public static final String QUERY_LIST_BY_SCHULTRAEGERNR = "SELECT e FROM DTOEigeneSchule e WHERE e.SchultraegerNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulNr */
	public static final String QUERY_BY_SCHULNR = "SELECT e FROM DTOEigeneSchule e WHERE e.SchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulNr */
	public static final String QUERY_LIST_BY_SCHULNR = "SELECT e FROM DTOEigeneSchule e WHERE e.SchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung1 */
	public static final String QUERY_BY_BEZEICHNUNG1 = "SELECT e FROM DTOEigeneSchule e WHERE e.Bezeichnung1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung1 */
	public static final String QUERY_LIST_BY_BEZEICHNUNG1 = "SELECT e FROM DTOEigeneSchule e WHERE e.Bezeichnung1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung2 */
	public static final String QUERY_BY_BEZEICHNUNG2 = "SELECT e FROM DTOEigeneSchule e WHERE e.Bezeichnung2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung2 */
	public static final String QUERY_LIST_BY_BEZEICHNUNG2 = "SELECT e FROM DTOEigeneSchule e WHERE e.Bezeichnung2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung3 */
	public static final String QUERY_BY_BEZEICHNUNG3 = "SELECT e FROM DTOEigeneSchule e WHERE e.Bezeichnung3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung3 */
	public static final String QUERY_LIST_BY_BEZEICHNUNG3 = "SELECT e FROM DTOEigeneSchule e WHERE e.Bezeichnung3 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM DTOEigeneSchule e WHERE e.Strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM DTOEigeneSchule e WHERE e.Strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM DTOEigeneSchule e WHERE e.HausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM DTOEigeneSchule e WHERE e.HausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNrZusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM DTOEigeneSchule e WHERE e.HausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNrZusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM DTOEigeneSchule e WHERE e.HausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM DTOEigeneSchule e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM DTOEigeneSchule e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ort */
	public static final String QUERY_BY_ORT = "SELECT e FROM DTOEigeneSchule e WHERE e.Ort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ort */
	public static final String QUERY_LIST_BY_ORT = "SELECT e FROM DTOEigeneSchule e WHERE e.Ort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Telefon */
	public static final String QUERY_BY_TELEFON = "SELECT e FROM DTOEigeneSchule e WHERE e.Telefon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Telefon */
	public static final String QUERY_LIST_BY_TELEFON = "SELECT e FROM DTOEigeneSchule e WHERE e.Telefon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fax */
	public static final String QUERY_BY_FAX = "SELECT e FROM DTOEigeneSchule e WHERE e.Fax = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fax */
	public static final String QUERY_LIST_BY_FAX = "SELECT e FROM DTOEigeneSchule e WHERE e.Fax IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM DTOEigeneSchule e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM DTOEigeneSchule e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ganztags */
	public static final String QUERY_BY_GANZTAGS = "SELECT e FROM DTOEigeneSchule e WHERE e.Ganztags = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ganztags */
	public static final String QUERY_LIST_BY_GANZTAGS = "SELECT e FROM DTOEigeneSchule e WHERE e.Ganztags IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOEigeneSchule e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOEigeneSchule e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzahlAbschnitte */
	public static final String QUERY_BY_ANZAHLABSCHNITTE = "SELECT e FROM DTOEigeneSchule e WHERE e.AnzahlAbschnitte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzahlAbschnitte */
	public static final String QUERY_LIST_BY_ANZAHLABSCHNITTE = "SELECT e FROM DTOEigeneSchule e WHERE e.AnzahlAbschnitte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschnittBez */
	public static final String QUERY_BY_ABSCHNITTBEZ = "SELECT e FROM DTOEigeneSchule e WHERE e.AbschnittBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschnittBez */
	public static final String QUERY_LIST_BY_ABSCHNITTBEZ = "SELECT e FROM DTOEigeneSchule e WHERE e.AbschnittBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BezAbschnitt1 */
	public static final String QUERY_BY_BEZABSCHNITT1 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BezAbschnitt1 */
	public static final String QUERY_LIST_BY_BEZABSCHNITT1 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BezAbschnitt2 */
	public static final String QUERY_BY_BEZABSCHNITT2 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BezAbschnitt2 */
	public static final String QUERY_LIST_BY_BEZABSCHNITT2 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BezAbschnitt3 */
	public static final String QUERY_BY_BEZABSCHNITT3 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BezAbschnitt3 */
	public static final String QUERY_LIST_BY_BEZABSCHNITT3 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt3 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BezAbschnitt4 */
	public static final String QUERY_BY_BEZABSCHNITT4 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BezAbschnitt4 */
	public static final String QUERY_LIST_BY_BEZABSCHNITT4 = "SELECT e FROM DTOEigeneSchule e WHERE e.BezAbschnitt4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fremdsprachen */
	public static final String QUERY_BY_FREMDSPRACHEN = "SELECT e FROM DTOEigeneSchule e WHERE e.Fremdsprachen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fremdsprachen */
	public static final String QUERY_LIST_BY_FREMDSPRACHEN = "SELECT e FROM DTOEigeneSchule e WHERE e.Fremdsprachen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JVAZeigen */
	public static final String QUERY_BY_JVAZEIGEN = "SELECT e FROM DTOEigeneSchule e WHERE e.JVAZeigen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JVAZeigen */
	public static final String QUERY_LIST_BY_JVAZEIGEN = "SELECT e FROM DTOEigeneSchule e WHERE e.JVAZeigen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RefPaedagogikZeigen */
	public static final String QUERY_BY_REFPAEDAGOGIKZEIGEN = "SELECT e FROM DTOEigeneSchule e WHERE e.RefPaedagogikZeigen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RefPaedagogikZeigen */
	public static final String QUERY_LIST_BY_REFPAEDAGOGIKZEIGEN = "SELECT e FROM DTOEigeneSchule e WHERE e.RefPaedagogikZeigen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzJGS_Jahr */
	public static final String QUERY_BY_ANZJGS_JAHR = "SELECT e FROM DTOEigeneSchule e WHERE e.AnzJGS_Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzJGS_Jahr */
	public static final String QUERY_LIST_BY_ANZJGS_JAHR = "SELECT e FROM DTOEigeneSchule e WHERE e.AnzJGS_Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstHauptsitz */
	public static final String QUERY_BY_ISTHAUPTSITZ = "SELECT e FROM DTOEigeneSchule e WHERE e.IstHauptsitz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstHauptsitz */
	public static final String QUERY_LIST_BY_ISTHAUPTSITZ = "SELECT e FROM DTOEigeneSchule e WHERE e.IstHauptsitz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NotenGesperrt */
	public static final String QUERY_BY_NOTENGESPERRT = "SELECT e FROM DTOEigeneSchule e WHERE e.NotenGesperrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NotenGesperrt */
	public static final String QUERY_LIST_BY_NOTENGESPERRT = "SELECT e FROM DTOEigeneSchule e WHERE e.NotenGesperrt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZurueckgestelltAnzahl */
	public static final String QUERY_BY_ZURUECKGESTELLTANZAHL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAnzahl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZurueckgestelltAnzahl */
	public static final String QUERY_LIST_BY_ZURUECKGESTELLTANZAHL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAnzahl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZurueckgestelltWeibl */
	public static final String QUERY_BY_ZURUECKGESTELLTWEIBL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltWeibl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZurueckgestelltWeibl */
	public static final String QUERY_LIST_BY_ZURUECKGESTELLTWEIBL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltWeibl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZurueckgestelltAuslaender */
	public static final String QUERY_BY_ZURUECKGESTELLTAUSLAENDER = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaender = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZurueckgestelltAuslaender */
	public static final String QUERY_LIST_BY_ZURUECKGESTELLTAUSLAENDER = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaender IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZurueckgestelltAuslaenderWeibl */
	public static final String QUERY_BY_ZURUECKGESTELLTAUSLAENDERWEIBL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaenderWeibl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZurueckgestelltAuslaenderWeibl */
	public static final String QUERY_LIST_BY_ZURUECKGESTELLTAUSLAENDERWEIBL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaenderWeibl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZurueckgestelltAussiedler */
	public static final String QUERY_BY_ZURUECKGESTELLTAUSSIEDLER = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZurueckgestelltAussiedler */
	public static final String QUERY_LIST_BY_ZURUECKGESTELLTAUSSIEDLER = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZurueckgestelltAussiedlerWeibl */
	public static final String QUERY_BY_ZURUECKGESTELLTAUSSIEDLERWEIBL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedlerWeibl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZurueckgestelltAussiedlerWeibl */
	public static final String QUERY_LIST_BY_ZURUECKGESTELLTAUSSIEDLERWEIBL = "SELECT e FROM DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedlerWeibl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TeamTeaching */
	public static final String QUERY_BY_TEAMTEACHING = "SELECT e FROM DTOEigeneSchule e WHERE e.TeamTeaching = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TeamTeaching */
	public static final String QUERY_LIST_BY_TEAMTEACHING = "SELECT e FROM DTOEigeneSchule e WHERE e.TeamTeaching IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiGruppenprozess */
	public static final String QUERY_BY_ABIGRUPPENPROZESS = "SELECT e FROM DTOEigeneSchule e WHERE e.AbiGruppenprozess = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiGruppenprozess */
	public static final String QUERY_LIST_BY_ABIGRUPPENPROZESS = "SELECT e FROM DTOEigeneSchule e WHERE e.AbiGruppenprozess IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DauerUnterrichtseinheit */
	public static final String QUERY_BY_DAUERUNTERRICHTSEINHEIT = "SELECT e FROM DTOEigeneSchule e WHERE e.DauerUnterrichtseinheit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DauerUnterrichtseinheit */
	public static final String QUERY_LIST_BY_DAUERUNTERRICHTSEINHEIT = "SELECT e FROM DTOEigeneSchule e WHERE e.DauerUnterrichtseinheit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gruppen8Bis1 */
	public static final String QUERY_BY_GRUPPEN8BIS1 = "SELECT e FROM DTOEigeneSchule e WHERE e.Gruppen8Bis1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gruppen8Bis1 */
	public static final String QUERY_LIST_BY_GRUPPEN8BIS1 = "SELECT e FROM DTOEigeneSchule e WHERE e.Gruppen8Bis1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gruppen13Plus */
	public static final String QUERY_BY_GRUPPEN13PLUS = "SELECT e FROM DTOEigeneSchule e WHERE e.Gruppen13Plus = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gruppen13Plus */
	public static final String QUERY_LIST_BY_GRUPPEN13PLUS = "SELECT e FROM DTOEigeneSchule e WHERE e.Gruppen13Plus IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes InternatsplaetzeM */
	public static final String QUERY_BY_INTERNATSPLAETZEM = "SELECT e FROM DTOEigeneSchule e WHERE e.InternatsplaetzeM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes InternatsplaetzeM */
	public static final String QUERY_LIST_BY_INTERNATSPLAETZEM = "SELECT e FROM DTOEigeneSchule e WHERE e.InternatsplaetzeM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes InternatsplaetzeW */
	public static final String QUERY_BY_INTERNATSPLAETZEW = "SELECT e FROM DTOEigeneSchule e WHERE e.InternatsplaetzeW = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes InternatsplaetzeW */
	public static final String QUERY_LIST_BY_INTERNATSPLAETZEW = "SELECT e FROM DTOEigeneSchule e WHERE e.InternatsplaetzeW IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes InternatsplaetzeNeutral */
	public static final String QUERY_BY_INTERNATSPLAETZENEUTRAL = "SELECT e FROM DTOEigeneSchule e WHERE e.InternatsplaetzeNeutral = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes InternatsplaetzeNeutral */
	public static final String QUERY_LIST_BY_INTERNATSPLAETZENEUTRAL = "SELECT e FROM DTOEigeneSchule e WHERE e.InternatsplaetzeNeutral IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WebAdresse */
	public static final String QUERY_BY_WEBADRESSE = "SELECT e FROM DTOEigeneSchule e WHERE e.WebAdresse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WebAdresse */
	public static final String QUERY_LIST_BY_WEBADRESSE = "SELECT e FROM DTOEigeneSchule e WHERE e.WebAdresse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Land */
	public static final String QUERY_BY_LAND = "SELECT e FROM DTOEigeneSchule e WHERE e.Land = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Land */
	public static final String QUERY_LIST_BY_LAND = "SELECT e FROM DTOEigeneSchule e WHERE e.Land IN ?1";

	/** ID des Datensatzes der eigenen Schule */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Schulformnummer der eigenen Schule (Statkue IT.NRW) */
	@Column(name = "SchulformNr")
	@JsonProperty
	public String SchulformNr;

	/** Schulformkürzel der eigenen Schule (Statkue IT.NRW) */
	@Column(name = "SchulformKrz")
	@JsonProperty
	@Convert(converter = SchulformKuerzelConverter.class)
	@JsonSerialize(using = SchulformKuerzelConverterSerializer.class)
	@JsonDeserialize(using = SchulformKuerzelConverterDeserializer.class)
	public Schulform Schulform;

	/** Bezeichnender Text der Schulform */
	@Column(name = "SchulformBez")
	@JsonProperty
	public String SchulformBez;

	/** Art des Schulträgers */
	@Column(name = "SchultraegerArt")
	@JsonProperty
	public String SchultraegerArt;

	/** Schulträgernummer aus dem Katalog der Schulver IT.NRW */
	@Column(name = "SchultraegerNr")
	@JsonProperty
	public String SchultraegerNr;

	/** Eindeutige Schulnummer der eigenen Schule aus der Schulver IT.NRW */
	@Column(name = "SchulNr")
	@JsonProperty
	@Convert(converter = StringToIntegerConverter.class)
	@JsonSerialize(using = StringToIntegerConverterSerializer.class)
	@JsonDeserialize(using = StringToIntegerConverterDeserializer.class)
	public Integer SchulNr;

	/** 1. Text für die Bezeichnung der Schule */
	@Column(name = "Bezeichnung1")
	@JsonProperty
	public String Bezeichnung1;

	/** 2. Text für die Bezeichnung der Schule */
	@Column(name = "Bezeichnung2")
	@JsonProperty
	public String Bezeichnung2;

	/** 3. Text für die Bezeichnung der Schule */
	@Column(name = "Bezeichnung3")
	@JsonProperty
	public String Bezeichnung3;

	/** Straßenname der Schule */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Hausnummerzusatz wenn getrennt gespeichert */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** PLZ der eigenen Schule */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Ortsangabe der eigenen Schule */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Telefonnummer der eigenen Schule */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** Faxnummer der eigenen Schule */
	@Column(name = "Fax")
	@JsonProperty
	public String Fax;

	/** Email-Adresse der eigenen Schule */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Gibt an, ob die Schule Ganztagsbetrieb hat */
	@Column(name = "Ganztags")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Ganztags;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Anzahl der verfügbaren Abschnitte (meist Halbjahre oder Quartale) */
	@Column(name = "AnzahlAbschnitte")
	@JsonProperty
	public Integer AnzahlAbschnitte;

	/** Bezeichnung der Abschnitte */
	@Column(name = "AbschnittBez")
	@JsonProperty
	public String AbschnittBez;

	/** Bezeichnung des ersten Abschnitts */
	@Column(name = "BezAbschnitt1")
	@JsonProperty
	public String BezAbschnitt1;

	/** Bezeichnung des zweiten Abschnitts */
	@Column(name = "BezAbschnitt2")
	@JsonProperty
	public String BezAbschnitt2;

	/** Bezeichnung des dritten Abschnitts */
	@Column(name = "BezAbschnitt3")
	@JsonProperty
	public String BezAbschnitt3;

	/** Bezeichnung des vierten Abschnitts */
	@Column(name = "BezAbschnitt4")
	@JsonProperty
	public String BezAbschnitt4;

	/** Welche Fremdsprachen werden unterrichtet */
	@Column(name = "Fremdsprachen")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Fremdsprachen;

	/** Schule unterrichtet in der Justizvollzugsanstalt */
	@Column(name = "JVAZeigen")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean JVAZeigen;

	/** Schule hat Reformpädagogischen-Zweig */
	@Column(name = "RefPaedagogikZeigen")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RefPaedagogikZeigen;

	/** Anzahl der Jahrgangstufen pro Schuljahr (Semesterbetrieb WBK) */
	@Column(name = "AnzJGS_Jahr")
	@JsonProperty
	public Integer AnzJGS_Jahr;

	/** Gibt an, ob die Datenbank am Hauptsitzder Schule ist (Dependancebetrieb) */
	@Column(name = "IstHauptsitz")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstHauptsitz;

	/** Sperrt die Noteneingabe */
	@Column(name = "NotenGesperrt")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NotenGesperrt;

	/** Anzahl de Zurückgestellten Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAnzahl")
	@JsonProperty
	public Integer ZurueckgestelltAnzahl;

	/** Anzahl de Zurückgestellten weiblichen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltWeibl")
	@JsonProperty
	public Integer ZurueckgestelltWeibl;

	/** Anzahl de Zurückgestellten ausländischen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAuslaender")
	@JsonProperty
	public Integer ZurueckgestelltAuslaender;

	/** Anzahl de Zurückgestellten ausländischen weiblichen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAuslaenderWeibl")
	@JsonProperty
	public Integer ZurueckgestelltAuslaenderWeibl;

	/** DEPRECATED: Anzahl de Zurückgestellten ausgesiedelten Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAussiedler")
	@JsonProperty
	public Integer ZurueckgestelltAussiedler;

	/** DEPRECATED: Anzahl de Zurückgestellten ausgesiedelten weiblichen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAussiedlerWeibl")
	@JsonProperty
	public Integer ZurueckgestelltAussiedlerWeibl;

	/** Aktiviert das Teamteaching */
	@Column(name = "TeamTeaching")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean TeamTeaching;

	/** Sperrt oder erlaubt die Gruppenprozesse für das Abitur */
	@Column(name = "AbiGruppenprozess")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AbiGruppenprozess;

	/** Dauer der Unterrichtseinheit (bei NULL = 45 Minuten) */
	@Column(name = "DauerUnterrichtseinheit")
	@JsonProperty
	public Integer DauerUnterrichtseinheit;

	/** Schule hat Betreuung 8 bis 13 */
	@Column(name = "Gruppen8Bis1")
	@JsonProperty
	public Integer Gruppen8Bis1;

	/** Gruppen in der 13Plus-Betreuung */
	@Column(name = "Gruppen13Plus")
	@JsonProperty
	public Integer Gruppen13Plus;

	/** Internatsplätze männlilch */
	@Column(name = "InternatsplaetzeM")
	@JsonProperty
	public Integer InternatsplaetzeM;

	/** Internatsplätze weiblich */
	@Column(name = "InternatsplaetzeW")
	@JsonProperty
	public Integer InternatsplaetzeW;

	/** Internatsplätze divers ohne Angabe */
	@Column(name = "InternatsplaetzeNeutral")
	@JsonProperty
	public Integer InternatsplaetzeNeutral;

	/** Enthält die Homepage-Adresse URL der Schule */
	@Column(name = "WebAdresse")
	@JsonProperty
	public String WebAdresse;

	/** Land der Schule ??? Wird wahrscheinlich für Bundeswehr verwendet? */
	@Column(name = "Land")
	@JsonProperty
	public String Land;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneSchule ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneSchule() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneSchule ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOEigeneSchule(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEigeneSchule other = (DTOEigeneSchule) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOEigeneSchule(ID=" + this.ID + ", SchulformNr=" + this.SchulformNr + ", Schulform=" + this.Schulform + ", SchulformBez=" + this.SchulformBez + ", SchultraegerArt=" + this.SchultraegerArt + ", SchultraegerNr=" + this.SchultraegerNr + ", SchulNr=" + this.SchulNr + ", Bezeichnung1=" + this.Bezeichnung1 + ", Bezeichnung2=" + this.Bezeichnung2 + ", Bezeichnung3=" + this.Bezeichnung3 + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Telefon=" + this.Telefon + ", Fax=" + this.Fax + ", Email=" + this.Email + ", Ganztags=" + this.Ganztags + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", AnzahlAbschnitte=" + this.AnzahlAbschnitte + ", AbschnittBez=" + this.AbschnittBez + ", BezAbschnitt1=" + this.BezAbschnitt1 + ", BezAbschnitt2=" + this.BezAbschnitt2 + ", BezAbschnitt3=" + this.BezAbschnitt3 + ", BezAbschnitt4=" + this.BezAbschnitt4 + ", Fremdsprachen=" + this.Fremdsprachen + ", JVAZeigen=" + this.JVAZeigen + ", RefPaedagogikZeigen=" + this.RefPaedagogikZeigen + ", AnzJGS_Jahr=" + this.AnzJGS_Jahr + ", IstHauptsitz=" + this.IstHauptsitz + ", NotenGesperrt=" + this.NotenGesperrt + ", ZurueckgestelltAnzahl=" + this.ZurueckgestelltAnzahl + ", ZurueckgestelltWeibl=" + this.ZurueckgestelltWeibl + ", ZurueckgestelltAuslaender=" + this.ZurueckgestelltAuslaender + ", ZurueckgestelltAuslaenderWeibl=" + this.ZurueckgestelltAuslaenderWeibl + ", ZurueckgestelltAussiedler=" + this.ZurueckgestelltAussiedler + ", ZurueckgestelltAussiedlerWeibl=" + this.ZurueckgestelltAussiedlerWeibl + ", TeamTeaching=" + this.TeamTeaching + ", AbiGruppenprozess=" + this.AbiGruppenprozess + ", DauerUnterrichtseinheit=" + this.DauerUnterrichtseinheit + ", Gruppen8Bis1=" + this.Gruppen8Bis1 + ", Gruppen13Plus=" + this.Gruppen13Plus + ", InternatsplaetzeM=" + this.InternatsplaetzeM + ", InternatsplaetzeW=" + this.InternatsplaetzeW + ", InternatsplaetzeNeutral=" + this.InternatsplaetzeNeutral + ", WebAdresse=" + this.WebAdresse + ", Land=" + this.Land + ")";
	}

}
