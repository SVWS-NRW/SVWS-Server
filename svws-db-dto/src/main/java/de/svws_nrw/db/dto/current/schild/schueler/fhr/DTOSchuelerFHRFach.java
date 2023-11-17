package de.svws_nrw.db.dto.current.schild.schueler.fhr;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFHRFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFHRFaecher")
@NamedQuery(name = "DTOSchuelerFHRFach.all", query = "SELECT e FROM DTOSchuelerFHRFach e")
@NamedQuery(name = "DTOSchuelerFHRFach.id", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.id.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.schueler_id", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.fach_id", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.Fach_ID = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.fach_id.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.kursartallg", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KursartAllg = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.kursartallg.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KursartAllg IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.fachkrz", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.FachKrz = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.fachkrz.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.FachKrz IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_2_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_2_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_2_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_2_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_2_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_2_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.psii_3_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.PSII_3_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.hsii_3_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.HSII_3_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.rsii_3_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.RSII_3_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_2_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_2_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_1", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_1 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_1.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_1 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_2", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_2 = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_2.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_2 IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_1_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_1_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_1_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_1_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_2_w", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_2_W = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.ksii_3_2_w.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.KSII_3_2_W IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.fsortierung", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.FSortierung = :value")
@NamedQuery(name = "DTOSchuelerFHRFach.fsortierung.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.FSortierung IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerFHRFach.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerFHRFach.all.migration", query = "SELECT e FROM DTOSchuelerFHRFach e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Fach_ID", "KursartAllg", "FachKrz", "PSII_2_1", "HSII_2_1", "RSII_2_1", "PSII_2_2", "HSII_2_2", "RSII_2_2", "PSII_2_1_W", "HSII_2_1_W", "RSII_2_1_W", "PSII_2_2_W", "HSII_2_2_W", "RSII_2_2_W", "PSII_3_1", "HSII_3_1", "RSII_3_1", "PSII_3_2", "HSII_3_2", "RSII_3_2", "PSII_3_1_W", "HSII_3_1_W", "RSII_3_1_W", "PSII_3_2_W", "HSII_3_2_W", "RSII_3_2_W", "KSII_2_1", "KSII_2_2", "KSII_2_1_W", "KSII_2_2_W", "KSII_3_1", "KSII_3_2", "KSII_3_1_W", "KSII_3_2_W", "FSortierung"})
public final class DTOSchuelerFHRFach {

	/** ID des Facheintrags für den FHR-Reiter */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Schuelers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** ID des Faches */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFHRFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerFHRFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFHRFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOSchuelerFHRFach(final long ID, final long Schueler_ID, final long Fach_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
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
		DTOSchuelerFHRFach other = (DTOSchuelerFHRFach) obj;
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
		return "DTOSchuelerFHRFach(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", FachKrz=" + this.FachKrz + ", PSII_2_1=" + this.PSII_2_1 + ", HSII_2_1=" + this.HSII_2_1 + ", RSII_2_1=" + this.RSII_2_1 + ", PSII_2_2=" + this.PSII_2_2 + ", HSII_2_2=" + this.HSII_2_2 + ", RSII_2_2=" + this.RSII_2_2 + ", PSII_2_1_W=" + this.PSII_2_1_W + ", HSII_2_1_W=" + this.HSII_2_1_W + ", RSII_2_1_W=" + this.RSII_2_1_W + ", PSII_2_2_W=" + this.PSII_2_2_W + ", HSII_2_2_W=" + this.HSII_2_2_W + ", RSII_2_2_W=" + this.RSII_2_2_W + ", PSII_3_1=" + this.PSII_3_1 + ", HSII_3_1=" + this.HSII_3_1 + ", RSII_3_1=" + this.RSII_3_1 + ", PSII_3_2=" + this.PSII_3_2 + ", HSII_3_2=" + this.HSII_3_2 + ", RSII_3_2=" + this.RSII_3_2 + ", PSII_3_1_W=" + this.PSII_3_1_W + ", HSII_3_1_W=" + this.HSII_3_1_W + ", RSII_3_1_W=" + this.RSII_3_1_W + ", PSII_3_2_W=" + this.PSII_3_2_W + ", HSII_3_2_W=" + this.HSII_3_2_W + ", RSII_3_2_W=" + this.RSII_3_2_W + ", KSII_2_1=" + this.KSII_2_1 + ", KSII_2_2=" + this.KSII_2_2 + ", KSII_2_1_W=" + this.KSII_2_1_W + ", KSII_2_2_W=" + this.KSII_2_2_W + ", KSII_3_1=" + this.KSII_3_1 + ", KSII_3_2=" + this.KSII_3_2 + ", KSII_3_1_W=" + this.KSII_3_1_W + ", KSII_3_2_W=" + this.KSII_3_2_W + ", FSortierung=" + this.FSortierung + ")";
	}

}
