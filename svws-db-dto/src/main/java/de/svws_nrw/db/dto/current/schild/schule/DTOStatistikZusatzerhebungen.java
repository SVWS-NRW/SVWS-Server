package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Statistik_Zusatzerhebungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statistik_Zusatzerhebungen")
@JsonPropertyOrder({"idSchuljahresabschnitt", "statistikAnsprechpartnerName", "statistikAnsprechpartnerTelefon", "statistikAnsprechpartnerEmail", "hatInternationaleKontakte", "anzahlSchulanmeldungen", "anzahlZurueckstellungsverfahren", "anzahlZurueckstellungenInsgesamt", "anzahlZurueckstellungenWeiblich", "anzahlZurueckstellungenAuslaenderZusammen", "anzahlZurueckstellungenAuslaenderWeiblich", "anzahlZurueckstellungenInsgesamtQuartal4", "anzahlZurueckstellungenInsgesamtQuartal1", "anzahlZurueckstellungenInsgesamtQuartal2", "anzahlZurueckstellungenInsgesamtQuartal3", "idInternatArt", "anzahlInternatplaetzeJungenMoeglich", "anzahlInternatplaetzeJungenBelegt", "anzahlInternatplaetzeMaedchenMoeglich", "anzahlInternatplaetzeMaedchenBelegt", "anzahlInternatplaetzeNeutralMoeglich", "anzahlInternatplaetzeNeutralBelegt", "durchschnittKlinikschuleAllgemeinbildendInsgesamt", "durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte", "durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt", "durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte", "durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt", "durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte"})
public final class DTOStatistikZusatzerhebungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStatistikZusatzerhebungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.idSchuljahresabschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.idSchuljahresabschnitt IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.idSchuljahresabschnitt IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchuljahresabschnitt */
	public static final String QUERY_BY_IDSCHULJAHRESABSCHNITT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.idSchuljahresabschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchuljahresabschnitt */
	public static final String QUERY_LIST_BY_IDSCHULJAHRESABSCHNITT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.idSchuljahresabschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes statistikAnsprechpartnerName */
	public static final String QUERY_BY_STATISTIKANSPRECHPARTNERNAME = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.statistikAnsprechpartnerName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes statistikAnsprechpartnerName */
	public static final String QUERY_LIST_BY_STATISTIKANSPRECHPARTNERNAME = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.statistikAnsprechpartnerName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes statistikAnsprechpartnerTelefon */
	public static final String QUERY_BY_STATISTIKANSPRECHPARTNERTELEFON = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.statistikAnsprechpartnerTelefon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes statistikAnsprechpartnerTelefon */
	public static final String QUERY_LIST_BY_STATISTIKANSPRECHPARTNERTELEFON = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.statistikAnsprechpartnerTelefon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes statistikAnsprechpartnerEmail */
	public static final String QUERY_BY_STATISTIKANSPRECHPARTNEREMAIL = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.statistikAnsprechpartnerEmail = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes statistikAnsprechpartnerEmail */
	public static final String QUERY_LIST_BY_STATISTIKANSPRECHPARTNEREMAIL = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.statistikAnsprechpartnerEmail IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes hatInternationaleKontakte */
	public static final String QUERY_BY_HATINTERNATIONALEKONTAKTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.hatInternationaleKontakte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes hatInternationaleKontakte */
	public static final String QUERY_LIST_BY_HATINTERNATIONALEKONTAKTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.hatInternationaleKontakte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlSchulanmeldungen */
	public static final String QUERY_BY_ANZAHLSCHULANMELDUNGEN = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlSchulanmeldungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlSchulanmeldungen */
	public static final String QUERY_LIST_BY_ANZAHLSCHULANMELDUNGEN = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlSchulanmeldungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungsverfahren */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGSVERFAHREN = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungsverfahren = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungsverfahren */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGSVERFAHREN = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungsverfahren IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenInsgesamt */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenInsgesamt */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenWeiblich */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENWEIBLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenWeiblich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenWeiblich */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENWEIBLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenWeiblich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenAuslaenderZusammen */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENAUSLAENDERZUSAMMEN = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenAuslaenderZusammen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenAuslaenderZusammen */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENAUSLAENDERZUSAMMEN = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenAuslaenderZusammen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenAuslaenderWeiblich */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENAUSLAENDERWEIBLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenAuslaenderWeiblich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenAuslaenderWeiblich */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENAUSLAENDERWEIBLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenAuslaenderWeiblich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenInsgesamtQuartal4 */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL4 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenInsgesamtQuartal4 */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL4 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenInsgesamtQuartal1 */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL1 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenInsgesamtQuartal1 */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL1 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenInsgesamtQuartal2 */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL2 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenInsgesamtQuartal2 */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL2 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlZurueckstellungenInsgesamtQuartal3 */
	public static final String QUERY_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL3 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlZurueckstellungenInsgesamtQuartal3 */
	public static final String QUERY_LIST_BY_ANZAHLZURUECKSTELLUNGENINSGESAMTQUARTAL3 = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlZurueckstellungenInsgesamtQuartal3 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idInternatArt */
	public static final String QUERY_BY_IDINTERNATART = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.idInternatArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idInternatArt */
	public static final String QUERY_LIST_BY_IDINTERNATART = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.idInternatArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlInternatplaetzeJungenMoeglich */
	public static final String QUERY_BY_ANZAHLINTERNATPLAETZEJUNGENMOEGLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeJungenMoeglich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlInternatplaetzeJungenMoeglich */
	public static final String QUERY_LIST_BY_ANZAHLINTERNATPLAETZEJUNGENMOEGLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeJungenMoeglich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlInternatplaetzeJungenBelegt */
	public static final String QUERY_BY_ANZAHLINTERNATPLAETZEJUNGENBELEGT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeJungenBelegt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlInternatplaetzeJungenBelegt */
	public static final String QUERY_LIST_BY_ANZAHLINTERNATPLAETZEJUNGENBELEGT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeJungenBelegt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlInternatplaetzeMaedchenMoeglich */
	public static final String QUERY_BY_ANZAHLINTERNATPLAETZEMAEDCHENMOEGLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeMaedchenMoeglich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlInternatplaetzeMaedchenMoeglich */
	public static final String QUERY_LIST_BY_ANZAHLINTERNATPLAETZEMAEDCHENMOEGLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeMaedchenMoeglich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlInternatplaetzeMaedchenBelegt */
	public static final String QUERY_BY_ANZAHLINTERNATPLAETZEMAEDCHENBELEGT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeMaedchenBelegt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlInternatplaetzeMaedchenBelegt */
	public static final String QUERY_LIST_BY_ANZAHLINTERNATPLAETZEMAEDCHENBELEGT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeMaedchenBelegt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlInternatplaetzeNeutralMoeglich */
	public static final String QUERY_BY_ANZAHLINTERNATPLAETZENEUTRALMOEGLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeNeutralMoeglich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlInternatplaetzeNeutralMoeglich */
	public static final String QUERY_LIST_BY_ANZAHLINTERNATPLAETZENEUTRALMOEGLICH = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeNeutralMoeglich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahlInternatplaetzeNeutralBelegt */
	public static final String QUERY_BY_ANZAHLINTERNATPLAETZENEUTRALBELEGT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeNeutralBelegt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahlInternatplaetzeNeutralBelegt */
	public static final String QUERY_LIST_BY_ANZAHLINTERNATPLAETZENEUTRALBELEGT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.anzahlInternatplaetzeNeutralBelegt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes durchschnittKlinikschuleAllgemeinbildendInsgesamt */
	public static final String QUERY_BY_DURCHSCHNITTKLINIKSCHULEALLGEMEINBILDENDINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleAllgemeinbildendInsgesamt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes durchschnittKlinikschuleAllgemeinbildendInsgesamt */
	public static final String QUERY_LIST_BY_DURCHSCHNITTKLINIKSCHULEALLGEMEINBILDENDINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleAllgemeinbildendInsgesamt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte */
	public static final String QUERY_BY_DURCHSCHNITTKLINIKSCHULEALLGEMEINBILDENDSCHWERSTBEHINDERTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte */
	public static final String QUERY_LIST_BY_DURCHSCHNITTKLINIKSCHULEALLGEMEINBILDENDSCHWERSTBEHINDERTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt */
	public static final String QUERY_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDTEILZEITINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt */
	public static final String QUERY_LIST_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDTEILZEITINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte */
	public static final String QUERY_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDTEILZEITSCHWERSTBEHINDERTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte */
	public static final String QUERY_LIST_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDTEILZEITSCHWERSTBEHINDERTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt */
	public static final String QUERY_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDVOLLZEITINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt */
	public static final String QUERY_LIST_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDVOLLZEITINSGESAMT = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte */
	public static final String QUERY_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDVOLLZEITSCHWERSTBEHINDERTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte */
	public static final String QUERY_LIST_BY_DURCHSCHNITTKLINIKSCHULEBERUFSBILDENDVOLLZEITSCHWERSTBEHINDERTE = "SELECT e FROM DTOStatistikZusatzerhebungen e WHERE e.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte IN ?1";

	/** Die ID des Schuljahresabschnitts, zu welchem die Daten der Zusatzerhebungen gehören */
	@Id
	@Column(name = "idSchuljahresabschnitt")
	@JsonProperty
	public long idSchuljahresabschnitt;

	/** Der Name des Statistik-Ansprechpartners für IT.NRW */
	@Column(name = "statistikAnsprechpartnerName")
	@JsonProperty
	public String statistikAnsprechpartnerName;

	/** Die Telefonnummer des Statistik-Ansprechpartners für IT.NRW */
	@Column(name = "statistikAnsprechpartnerTelefon")
	@JsonProperty
	public String statistikAnsprechpartnerTelefon;

	/** Die E-Mail-Adresse des Statistik-Ansprechpartners für IT.NRW */
	@Column(name = "statistikAnsprechpartnerEmail")
	@JsonProperty
	public String statistikAnsprechpartnerEmail;

	/** Gibt an, ob die Schule internationale Kontakte pflegt oder nicht */
	@Column(name = "hatInternationaleKontakte")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean hatInternationaleKontakte;

	/** Die Anzahl der Schulanmeldungen nach § 35 SchulG */
	@Column(name = "anzahlSchulanmeldungen")
	@JsonProperty
	public Integer anzahlSchulanmeldungen;

	/** Die Anzahl der Zurückstellungsverfahren im Zuge der Anmeldungen */
	@Column(name = "anzahlZurueckstellungsverfahren")
	@JsonProperty
	public Integer anzahlZurueckstellungsverfahren;

	/** Die Anzahl der tatsächlichen Zurückstellungen insgesamt */
	@Column(name = "anzahlZurueckstellungenInsgesamt")
	@JsonProperty
	public Integer anzahlZurueckstellungenInsgesamt;

	/** Die Anzahl der tatsächlichen Zurückstellungen weiblich */
	@Column(name = "anzahlZurueckstellungenWeiblich")
	@JsonProperty
	public Integer anzahlZurueckstellungenWeiblich;

	/** Die Anzahl der tatsächlichen Zurückstellungen Ausländer zusammen */
	@Column(name = "anzahlZurueckstellungenAuslaenderZusammen")
	@JsonProperty
	public Integer anzahlZurueckstellungenAuslaenderZusammen;

	/** Die Anzahl der tatsächlichen Zurückstellungen Ausländer weiblich */
	@Column(name = "anzahlZurueckstellungenAuslaenderWeiblich")
	@JsonProperty
	public Integer anzahlZurueckstellungenAuslaenderWeiblich;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 4. Quartal des Erhebungsvorjahres 6 Jahre alt geworden sind */
	@Column(name = "anzahlZurueckstellungenInsgesamtQuartal4")
	@JsonProperty
	public Integer anzahlZurueckstellungenInsgesamtQuartal4;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 1. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Column(name = "anzahlZurueckstellungenInsgesamtQuartal1")
	@JsonProperty
	public Integer anzahlZurueckstellungenInsgesamtQuartal1;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 2. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Column(name = "anzahlZurueckstellungenInsgesamtQuartal2")
	@JsonProperty
	public Integer anzahlZurueckstellungenInsgesamtQuartal2;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 3. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Column(name = "anzahlZurueckstellungenInsgesamtQuartal3")
	@JsonProperty
	public Integer anzahlZurueckstellungenInsgesamtQuartal3;

	/** Die ID der Art des Internats (Core-Type InternatArt) */
	@Column(name = "idInternatArt")
	@JsonProperty
	public Long idInternatArt;

	/** Die Anzahl der möglichen Internatsplätze für Jungen */
	@Column(name = "anzahlInternatplaetzeJungenMoeglich")
	@JsonProperty
	public Integer anzahlInternatplaetzeJungenMoeglich;

	/** Die Anzahl der belegten Internatsplätze für Jungen */
	@Column(name = "anzahlInternatplaetzeJungenBelegt")
	@JsonProperty
	public Integer anzahlInternatplaetzeJungenBelegt;

	/** Die Anzahl der möglichen Internatsplätze für Mädchen */
	@Column(name = "anzahlInternatplaetzeMaedchenMoeglich")
	@JsonProperty
	public Integer anzahlInternatplaetzeMaedchenMoeglich;

	/** Die Anzahl der belegten Internatsplätze für Mädchen */
	@Column(name = "anzahlInternatplaetzeMaedchenBelegt")
	@JsonProperty
	public Integer anzahlInternatplaetzeMaedchenBelegt;

	/** Die Anzahl der möglichen geschlechtsneutralen Internatsplätze */
	@Column(name = "anzahlInternatplaetzeNeutralMoeglich")
	@JsonProperty
	public Integer anzahlInternatplaetzeNeutralMoeglich;

	/** Die Anzahl der belegten geschlechtsneutralen Internatsplätze */
	@Column(name = "anzahlInternatplaetzeNeutralBelegt")
	@JsonProperty
	public Integer anzahlInternatplaetzeNeutralBelegt;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend insgesamt */
	@Column(name = "durchschnittKlinikschuleAllgemeinbildendInsgesamt")
	@JsonProperty
	public Double durchschnittKlinikschuleAllgemeinbildendInsgesamt;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend mit Schwerstbehinderung */
	@Column(name = "durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte")
	@JsonProperty
	public Double durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit insgesamt */
	@Column(name = "durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt")
	@JsonProperty
	public Double durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit mit Schwerstbehinderung */
	@Column(name = "durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte")
	@JsonProperty
	public Double durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit insgesamt */
	@Column(name = "durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt")
	@JsonProperty
	public Double durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit mit Schwerstbehinderung */
	@Column(name = "durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte")
	@JsonProperty
	public Double durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatistikZusatzerhebungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatistikZusatzerhebungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatistikZusatzerhebungen ohne eine Initialisierung der Attribute.
	 * @param idSchuljahresabschnitt   der Wert für das Attribut idSchuljahresabschnitt
	 * @param hatInternationaleKontakte   der Wert für das Attribut hatInternationaleKontakte
	 */
	public DTOStatistikZusatzerhebungen(final long idSchuljahresabschnitt, final Boolean hatInternationaleKontakte) {
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.hatInternationaleKontakte = hatInternationaleKontakte;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOStatistikZusatzerhebungen other = (DTOStatistikZusatzerhebungen) obj;
		return idSchuljahresabschnitt == other.idSchuljahresabschnitt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(idSchuljahresabschnitt);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStatistikZusatzerhebungen(idSchuljahresabschnitt=" + this.idSchuljahresabschnitt + ", statistikAnsprechpartnerName=" + this.statistikAnsprechpartnerName + ", statistikAnsprechpartnerTelefon=" + this.statistikAnsprechpartnerTelefon + ", statistikAnsprechpartnerEmail=" + this.statistikAnsprechpartnerEmail + ", hatInternationaleKontakte=" + this.hatInternationaleKontakte + ", anzahlSchulanmeldungen=" + this.anzahlSchulanmeldungen + ", anzahlZurueckstellungsverfahren=" + this.anzahlZurueckstellungsverfahren + ", anzahlZurueckstellungenInsgesamt=" + this.anzahlZurueckstellungenInsgesamt + ", anzahlZurueckstellungenWeiblich=" + this.anzahlZurueckstellungenWeiblich + ", anzahlZurueckstellungenAuslaenderZusammen=" + this.anzahlZurueckstellungenAuslaenderZusammen + ", anzahlZurueckstellungenAuslaenderWeiblich=" + this.anzahlZurueckstellungenAuslaenderWeiblich + ", anzahlZurueckstellungenInsgesamtQuartal4=" + this.anzahlZurueckstellungenInsgesamtQuartal4 + ", anzahlZurueckstellungenInsgesamtQuartal1=" + this.anzahlZurueckstellungenInsgesamtQuartal1 + ", anzahlZurueckstellungenInsgesamtQuartal2=" + this.anzahlZurueckstellungenInsgesamtQuartal2 + ", anzahlZurueckstellungenInsgesamtQuartal3=" + this.anzahlZurueckstellungenInsgesamtQuartal3 + ", idInternatArt=" + this.idInternatArt + ", anzahlInternatplaetzeJungenMoeglich=" + this.anzahlInternatplaetzeJungenMoeglich + ", anzahlInternatplaetzeJungenBelegt=" + this.anzahlInternatplaetzeJungenBelegt + ", anzahlInternatplaetzeMaedchenMoeglich=" + this.anzahlInternatplaetzeMaedchenMoeglich + ", anzahlInternatplaetzeMaedchenBelegt=" + this.anzahlInternatplaetzeMaedchenBelegt + ", anzahlInternatplaetzeNeutralMoeglich=" + this.anzahlInternatplaetzeNeutralMoeglich + ", anzahlInternatplaetzeNeutralBelegt=" + this.anzahlInternatplaetzeNeutralBelegt + ", durchschnittKlinikschuleAllgemeinbildendInsgesamt=" + this.durchschnittKlinikschuleAllgemeinbildendInsgesamt + ", durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte=" + this.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte + ", durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt=" + this.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt + ", durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte=" + this.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte + ", durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt=" + this.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt + ", durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte=" + this.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte + ")";
	}

}
