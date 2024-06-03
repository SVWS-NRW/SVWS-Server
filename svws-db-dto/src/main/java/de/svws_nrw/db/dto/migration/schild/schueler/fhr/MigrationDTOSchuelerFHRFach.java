package de.svws_nrw.db.dto.migration.schild.schueler.fhr;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFHRFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFHRFaecher")
@JsonPropertyOrder({"ID", "Schueler_ID", "Fach_ID", "KursartAllg", "FachKrz", "PSII_2_1", "HSII_2_1", "RSII_2_1", "PSII_2_2", "HSII_2_2", "RSII_2_2", "PSII_2_1_W", "HSII_2_1_W", "RSII_2_1_W", "PSII_2_2_W", "HSII_2_2_W", "RSII_2_2_W", "PSII_3_1", "HSII_3_1", "RSII_3_1", "PSII_3_2", "HSII_3_2", "RSII_3_2", "PSII_3_1_W", "HSII_3_1_W", "RSII_3_1_W", "PSII_3_2_W", "HSII_3_2_W", "RSII_3_2_W", "KSII_2_1", "KSII_2_2", "KSII_2_1_W", "KSII_2_2_W", "KSII_3_1", "KSII_3_2", "KSII_3_1_W", "KSII_3_2_W", "FSortierung", "SchulnrEigner"})
public final class MigrationDTOSchuelerFHRFach {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerFHRFach e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllg */
	public static final String QUERY_BY_KURSARTALLG = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KursartAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllg */
	public static final String QUERY_LIST_BY_KURSARTALLG = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KursartAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachKrz */
	public static final String QUERY_BY_FACHKRZ = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.FachKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachKrz */
	public static final String QUERY_LIST_BY_FACHKRZ = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.FachKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_2_1 */
	public static final String QUERY_BY_PSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_2_1 */
	public static final String QUERY_LIST_BY_PSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_2_1 */
	public static final String QUERY_BY_HSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_2_1 */
	public static final String QUERY_LIST_BY_HSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_2_1 */
	public static final String QUERY_BY_RSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_2_1 */
	public static final String QUERY_LIST_BY_RSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_2_2 */
	public static final String QUERY_BY_PSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_2_2 */
	public static final String QUERY_LIST_BY_PSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_2_2 */
	public static final String QUERY_BY_HSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_2_2 */
	public static final String QUERY_LIST_BY_HSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_2_2 */
	public static final String QUERY_BY_RSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_2_2 */
	public static final String QUERY_LIST_BY_RSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_2_1_W */
	public static final String QUERY_BY_PSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_2_1_W */
	public static final String QUERY_LIST_BY_PSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_2_1_W */
	public static final String QUERY_BY_HSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_2_1_W */
	public static final String QUERY_LIST_BY_HSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_2_1_W */
	public static final String QUERY_BY_RSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_2_1_W */
	public static final String QUERY_LIST_BY_RSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_2_2_W */
	public static final String QUERY_BY_PSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_2_2_W */
	public static final String QUERY_LIST_BY_PSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_2_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_2_2_W */
	public static final String QUERY_BY_HSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_2_2_W */
	public static final String QUERY_LIST_BY_HSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_2_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_2_2_W */
	public static final String QUERY_BY_RSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_2_2_W */
	public static final String QUERY_LIST_BY_RSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_2_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_3_1 */
	public static final String QUERY_BY_PSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_3_1 */
	public static final String QUERY_LIST_BY_PSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_3_1 */
	public static final String QUERY_BY_HSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_3_1 */
	public static final String QUERY_LIST_BY_HSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_3_1 */
	public static final String QUERY_BY_RSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_3_1 */
	public static final String QUERY_LIST_BY_RSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_3_2 */
	public static final String QUERY_BY_PSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_3_2 */
	public static final String QUERY_LIST_BY_PSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_3_2 */
	public static final String QUERY_BY_HSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_3_2 */
	public static final String QUERY_LIST_BY_HSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_3_2 */
	public static final String QUERY_BY_RSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_3_2 */
	public static final String QUERY_LIST_BY_RSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_3_1_W */
	public static final String QUERY_BY_PSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_3_1_W */
	public static final String QUERY_LIST_BY_PSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_3_1_W */
	public static final String QUERY_BY_HSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_3_1_W */
	public static final String QUERY_LIST_BY_HSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_3_1_W */
	public static final String QUERY_BY_RSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_3_1_W */
	public static final String QUERY_LIST_BY_RSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PSII_3_2_W */
	public static final String QUERY_BY_PSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PSII_3_2_W */
	public static final String QUERY_LIST_BY_PSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.PSII_3_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HSII_3_2_W */
	public static final String QUERY_BY_HSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HSII_3_2_W */
	public static final String QUERY_LIST_BY_HSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.HSII_3_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RSII_3_2_W */
	public static final String QUERY_BY_RSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RSII_3_2_W */
	public static final String QUERY_LIST_BY_RSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.RSII_3_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_2_1 */
	public static final String QUERY_BY_KSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_2_1 */
	public static final String QUERY_LIST_BY_KSII_2_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_2_2 */
	public static final String QUERY_BY_KSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_2_2 */
	public static final String QUERY_LIST_BY_KSII_2_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_2_1_W */
	public static final String QUERY_BY_KSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_2_1_W */
	public static final String QUERY_LIST_BY_KSII_2_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_2_2_W */
	public static final String QUERY_BY_KSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_2_2_W */
	public static final String QUERY_LIST_BY_KSII_2_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_2_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_3_1 */
	public static final String QUERY_BY_KSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_3_1 */
	public static final String QUERY_LIST_BY_KSII_3_1 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_3_2 */
	public static final String QUERY_BY_KSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_3_2 */
	public static final String QUERY_LIST_BY_KSII_3_2 = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_3_1_W */
	public static final String QUERY_BY_KSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_3_1_W */
	public static final String QUERY_LIST_BY_KSII_3_1_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KSII_3_2_W */
	public static final String QUERY_BY_KSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KSII_3_2_W */
	public static final String QUERY_LIST_BY_KSII_3_2_W = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.KSII_3_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FSortierung */
	public static final String QUERY_BY_FSORTIERUNG = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.FSortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FSortierung */
	public static final String QUERY_LIST_BY_FSORTIERUNG = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.FSortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerFHRFach e WHERE e.SchulnrEigner IN ?1";

	/** ID des Facheintrags für den FHR-Reiter */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schuelers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID des Faches */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Kursart des Faches im Klartext */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Kürzel des Faches */
	@Column(name = "FachKrz")
	@JsonProperty
	public String FachKrz;

	/** P: Punkte im betreffenden Abschnitt */
	@Column(name = "PSII_2_1")
	@JsonProperty
	public String PSII_2_1;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK.  */
	@Column(name = "HSII_2_1")
	@JsonProperty
	public Integer HSII_2_1;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt)  */
	@Column(name = "RSII_2_1")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_2_1;

	/** P: Punkte im betreffenden Abschnitt  */
	@Column(name = "PSII_2_2")
	@JsonProperty
	public String PSII_2_2;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK.  */
	@Column(name = "HSII_2_2")
	@JsonProperty
	public Integer HSII_2_2;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt)  */
	@Column(name = "RSII_2_2")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_2_2;

	/** P: Punkte im betreffenden Abschnitt W=wiederholt */
	@Column(name = "PSII_2_1_W")
	@JsonProperty
	public String PSII_2_1_W;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt  */
	@Column(name = "HSII_2_1_W")
	@JsonProperty
	public Integer HSII_2_1_W;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt */
	@Column(name = "RSII_2_1_W")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_2_1_W;

	/** P: Punkte im betreffenden Abschnitt W=wiederholt */
	@Column(name = "PSII_2_2_W")
	@JsonProperty
	public String PSII_2_2_W;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt  */
	@Column(name = "HSII_2_2_W")
	@JsonProperty
	public Integer HSII_2_2_W;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt */
	@Column(name = "RSII_2_2_W")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_2_2_W;

	/** Punkte im betreffenden Abschnitt */
	@Column(name = "PSII_3_1")
	@JsonProperty
	public String PSII_3_1;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK.  */
	@Column(name = "HSII_3_1")
	@JsonProperty
	public Integer HSII_3_1;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt)  */
	@Column(name = "RSII_3_1")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_3_1;

	/** P: Punkte im betreffenden Abschnitt */
	@Column(name = "PSII_3_2")
	@JsonProperty
	public String PSII_3_2;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK.  */
	@Column(name = "HSII_3_2")
	@JsonProperty
	public Integer HSII_3_2;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt)  */
	@Column(name = "RSII_3_2")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_3_2;

	/** P: Punkte im betreffenden Abschnitt W=wiederholt */
	@Column(name = "PSII_3_1_W")
	@JsonProperty
	public String PSII_3_1_W;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt  */
	@Column(name = "HSII_3_1_W")
	@JsonProperty
	public Integer HSII_3_1_W;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt */
	@Column(name = "RSII_3_1_W")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_3_1_W;

	/** P: Punkte im betreffenden Abschnitt W=wiederholt */
	@Column(name = "PSII_3_2_W")
	@JsonProperty
	public String PSII_3_2_W;

	/** H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt  */
	@Column(name = "HSII_3_2_W")
	@JsonProperty
	public Integer HSII_3_2_W;

	/** R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt */
	@Column(name = "RSII_3_2_W")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RSII_3_2_W;

	/** K: Kursart im betreffenden Abschnitt  */
	@Column(name = "KSII_2_1")
	@JsonProperty
	public String KSII_2_1;

	/** K: Kursart im betreffenden Abschnitt  */
	@Column(name = "KSII_2_2")
	@JsonProperty
	public String KSII_2_2;

	/** K: Kursart im betreffenden Abschnitt W: Wiederholter Abschnitt */
	@Column(name = "KSII_2_1_W")
	@JsonProperty
	public String KSII_2_1_W;

	/** K: Kursart im betreffenden Abschnitt  */
	@Column(name = "KSII_2_2_W")
	@JsonProperty
	public String KSII_2_2_W;

	/** K: Kursart im betreffenden Abschnitt  */
	@Column(name = "KSII_3_1")
	@JsonProperty
	public String KSII_3_1;

	/** K: Kursart im betreffenden Abschnitt  */
	@Column(name = "KSII_3_2")
	@JsonProperty
	public String KSII_3_2;

	/** K: Kursart im betreffenden Abschnitt W: Wiederholter Abschnitt */
	@Column(name = "KSII_3_1_W")
	@JsonProperty
	public String KSII_3_1_W;

	/** K: Kursart im betreffenden Abschnitt W: Wiederholter Abschnitt */
	@Column(name = "KSII_3_2_W")
	@JsonProperty
	public String KSII_3_2_W;

	/** K: Kursart im betreffenden Abschnitt  */
	@Column(name = "FSortierung")
	@JsonProperty
	public Integer FSortierung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFHRFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerFHRFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFHRFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOSchuelerFHRFach(final Long ID, final Long Schueler_ID, final Long Fach_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Fach_ID == null) {
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerFHRFach other = (MigrationDTOSchuelerFHRFach) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerFHRFach(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", FachKrz=" + this.FachKrz + ", PSII_2_1=" + this.PSII_2_1 + ", HSII_2_1=" + this.HSII_2_1 + ", RSII_2_1=" + this.RSII_2_1 + ", PSII_2_2=" + this.PSII_2_2 + ", HSII_2_2=" + this.HSII_2_2 + ", RSII_2_2=" + this.RSII_2_2 + ", PSII_2_1_W=" + this.PSII_2_1_W + ", HSII_2_1_W=" + this.HSII_2_1_W + ", RSII_2_1_W=" + this.RSII_2_1_W + ", PSII_2_2_W=" + this.PSII_2_2_W + ", HSII_2_2_W=" + this.HSII_2_2_W + ", RSII_2_2_W=" + this.RSII_2_2_W + ", PSII_3_1=" + this.PSII_3_1 + ", HSII_3_1=" + this.HSII_3_1 + ", RSII_3_1=" + this.RSII_3_1 + ", PSII_3_2=" + this.PSII_3_2 + ", HSII_3_2=" + this.HSII_3_2 + ", RSII_3_2=" + this.RSII_3_2 + ", PSII_3_1_W=" + this.PSII_3_1_W + ", HSII_3_1_W=" + this.HSII_3_1_W + ", RSII_3_1_W=" + this.RSII_3_1_W + ", PSII_3_2_W=" + this.PSII_3_2_W + ", HSII_3_2_W=" + this.HSII_3_2_W + ", RSII_3_2_W=" + this.RSII_3_2_W + ", KSII_2_1=" + this.KSII_2_1 + ", KSII_2_2=" + this.KSII_2_2 + ", KSII_2_1_W=" + this.KSII_2_1_W + ", KSII_2_2_W=" + this.KSII_2_2_W + ", KSII_3_1=" + this.KSII_3_1 + ", KSII_3_2=" + this.KSII_3_2 + ", KSII_3_1_W=" + this.KSII_3_1_W + ", KSII_3_2_W=" + this.KSII_3_2_W + ", FSortierung=" + this.FSortierung + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
