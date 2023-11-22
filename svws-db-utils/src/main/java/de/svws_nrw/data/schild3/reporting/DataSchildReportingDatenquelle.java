package de.svws_nrw.data.schild3.reporting;

import de.svws_nrw.base.annotations.SchildReportingDate;
import de.svws_nrw.base.annotations.SchildReportingMemo;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingDatenquelle;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingDatenquelleAttribut;
import de.svws_nrw.core.types.schild3.SchildReportingAttributTyp;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für
 * Datenquellen von Schild-Reports.
 *
 * @param <DTO> der Typ der zugehörigen DTO-Klasse
 * @param <JMT> der Typ der Masterquelle, im Falle keiner MasterDatenquelle beliebig - z.B. String
 */
public abstract class DataSchildReportingDatenquelle<DTO, JMT> {

    /** Eine Map mit der Zuordnung der Datenquellen zu dem Namen der Datenquelle */
    private static LinkedHashMap<String, DataSchildReportingDatenquelle<?, ?>> datenquellen = null;

    /** Die Definition der Datenquelle */
    final SchildReportingDatenquelle datenquelle;

    /** Der Typ der Master-Datenquelle */
    private SchildReportingAttributTyp mastertyp = null;

    /** Der Java-Typ für das genutzte Attribut der Master-Datenquelle */
    private Class<JMT> masterclass = null;

    /** Enthält die Schulformen, auf welche die Datenquelle eingeschränkt ist. Ist die Menge leer, so steht die
     * Datenquelle an allen Schulformen zur Verfügung. */
    private final HashSet<Schulform> schulformen = new HashSet<>();

	private static final String meldungKeinParameter = "Kein Parameter für das Attribut der Master-Datenquelle angegeben";
	private static final String meldungUngueltigerParameter = "Ungültiger Parameter für das Attribut der Master-Datenquelle angegeben";



    /**
     * Erstellt eine neue Datenquelle für Schild-Reports
     *
     * @param dtoClass   die Klasse des Core-DTOs
     */
    DataSchildReportingDatenquelle(final Class<DTO> dtoClass) {
        this.datenquelle = new SchildReportingDatenquelle();
        this.datenquelle.name = this.getClass().getSimpleName().replace("DataSchildReportingDatenquelle", "");
        this.datenquelle.datenart = dtoClass.getSimpleName().replace("SchildReporting", "");
        final Schema schema = dtoClass.getAnnotation(Schema.class);
        if ((schema == null) || (schema.description() == null))
            throw new NullPointerException("Im Core-DTO musse eine Schema-Definition mit einer 'description' vorhanden sein");
        this.datenquelle.beschreibung = schema.description();
        this.addAttribute(dtoClass);
        DataSchildReportingDatenquelle.datenquellen.put(this.datenquelle.name, this);
    }


    /**
     * Setzt die Informationen zu der Master-Datenquelle dieser Datenquelle
     *
     * @param linkattribut     der Name des Attributs dieser Datenquelle, welches für
     *                         die Verbindung zu der Master-Datenquelle genutzt wird
     * @param master           der Name der Master-Datenquelle
     * @param masterattribut   das identifizierende Attribut der Master-Datenquelle
     * @param mastertyp        der Datentyp des identifzierenden Master-Attributes
     * @param masterclass      die Java-Klasse für den Datentyp des zu identifzierenden Master-Attributes
     */
    void setMaster(@NotNull final String linkattribut, @NotNull final String master, @NotNull final String masterattribut,
    		@NotNull final SchildReportingAttributTyp mastertyp, @NotNull final Class<JMT> masterclass) {
        this.mastertyp = mastertyp;
        this.masterclass = masterclass;
        this.datenquelle.linkattribut = linkattribut;
        this.datenquelle.master = master;
        this.datenquelle.masterattribut = masterattribut;
        this.datenquelle.mastertyp = mastertyp.toString();
    }


    /**
     * Fügt ein Attribut zu der Definition der Datenquelle hinzu
     *
     * @param name           der Name des Attributes
     * @param typ            der Datentyp des Attributes
     * @param beschreibung   eine erläuternde Erklärung zu dem Attribut
     */
    private void addAttribut(@NotNull final String name, @NotNull final SchildReportingAttributTyp typ, @NotNull final String beschreibung) {
        final SchildReportingDatenquelleAttribut attr = new SchildReportingDatenquelleAttribut();
        attr.name = name;
        attr.typ = typ.toString();
        attr.beschreibung = beschreibung;
        datenquelle.attribute.add(attr);
    }


    /**
     * Fügt die Attribute aus dem übergebenen Core-DTO zu dieser Datenquelle hinzu.
     *
     * @param clazz   die Klasse des Core-DTOs
     */
    private void addAttribute(final Class<?> clazz) {
        for (final Field field : clazz.getFields()) {
            final SchildReportingAttributTyp typ = switch (field.getType().getSimpleName()) {
                case "long", "int", "short", "byte", "Long", "Integer", "Short", "Byte" -> SchildReportingAttributTyp.INT;
                case "float", "double", "Float", "Double" -> SchildReportingAttributTyp.NUMBER;
                case "boolean", "Boolean" -> SchildReportingAttributTyp.BOOLEAN;
                default -> {
                	if (field.getAnnotation(SchildReportingDate.class) != null)
                		yield SchildReportingAttributTyp.DATE;
                	if (field.getAnnotation(SchildReportingMemo.class) != null)
                		yield SchildReportingAttributTyp.MEMO;
                	yield SchildReportingAttributTyp.STRING;
                }
            };
            final Schema schema = field.getAnnotation(Schema.class);
            if ((schema == null) || (schema.description() == null))
                throw new NullPointerException("Im Core-DTO musse eine Schema-Definition mit einer 'description' vorhanden sein");
            addAttribut(field.getName(), typ, schema.description());
        }
    }


    /**
     * Beschränkt die Gültigkeit der Datenquelle auf die übergebenen Schulformen.
     * Sollte die Methode bereits vorher aufgerufen worden sein,
     * so werden die Schulformen zu den vorher übergebenen Schulformen ergänzt.
     *
     * @param schulformen   die Schulformen, für welche die Datenquelle zulässig ist
     */
    public void restrictTo(final Schulform... schulformen) {
        for (final Schulform sf : schulformen)
            this.schulformen.add(sf);
    }


    /**
     * Bestimmt die Datensätze der Datenquelle, sofern keine Master-Datenquelle
     * angegeben wurde.
     *
     * @param conn   die Datenbankverbindung des aktuellen SVWS-Benutzers
     *
     * @return die Datensätze der Datenquelle
     */
    List<DTO> getDaten(final DBEntityManager conn) {
        throw new UnsupportedOperationException();
    }


    /**
     * Bestimmt die Datensätze der Datenquelle, sofern der Attributtyp des
     * Attributes der Master-Datenquelle definiert wurde und BOOLEAN, INT,
     * NUMBER, STRING oder MEMO ist.
     *
     * @param conn     die Datenbankverbindung des aktuellen SVWS-Benutzers
     * @param params   die Parameter für das Attribut der Masterdatenquelle
     *
     * @return die Datensätze der Datenquelle
     */
    List<DTO> getDaten(final DBEntityManager conn, final List<JMT> params) {
        throw new UnsupportedOperationException();
    }


	private List<DTO> getDatenBoolean(final DBEntityManager conn, final List<? extends Object> params) {
        // Daten vorhanden?
        if (params.isEmpty())
            throw OperationError.NOT_FOUND.exception(meldungKeinParameter);
        if (this.masterclass != Boolean.class)
            throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        // Prüfe, ob alle Parameter vom Typ Boolean sind
        for (final Object p : params)
            if (!(p instanceof Boolean))
                throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        @SuppressWarnings("unchecked")
		final List<JMT> paramListe = (List<JMT>) params;
        return getDaten(conn, paramListe);
    }


	private List<DTO> getDatenInteger(final DBEntityManager conn, final List<? extends Object> params) {
        // Daten vorhanden?
        if (params.isEmpty())
            throw OperationError.NOT_FOUND.exception(meldungKeinParameter);
        if (this.masterclass != Long.class)
            throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        // Prüfe, ob alle Parameter vom Typ Long sind
        final ArrayList<Long> paramListe = new ArrayList<>();
        for (final Object p : params) {
            if (p instanceof final Long l)
                paramListe.add(l);
            else if (p instanceof final Integer i)
                paramListe.add(i.longValue());
            else if (p instanceof final Short s)
                paramListe.add(s.longValue());
            else if (p instanceof final Byte b)
                paramListe.add(b.longValue());
            else
                throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        }
        @SuppressWarnings("unchecked")
		final List<JMT> paramListeJMT = (List<JMT>) paramListe;
        return getDaten(conn, paramListeJMT);
	}


	private List<DTO> getDatenNumber(final DBEntityManager conn, final List<? extends Object> params) {
        // Daten vorhanden?
        if (params.isEmpty())
            throw OperationError.NOT_FOUND.exception(meldungKeinParameter);
        if (this.masterclass != Double.class)
            throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        // Prüfe, ob alle Parameter vom Typ Double sind
        final ArrayList<Double> paramListe = new ArrayList<>();
        for (final Object p : params) {
            if (p instanceof final Double d)
                paramListe.add(d);
            else if (p instanceof final Float f)
                paramListe.add(f.doubleValue());
            else
                throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        }
        @SuppressWarnings("unchecked")
		final List<JMT> paramListeJMT = (List<JMT>) paramListe;
        return getDaten(conn, paramListeJMT);
	}


	private List<DTO> getDatenString(final DBEntityManager conn, final List<? extends Object> params) {
        // Daten vorhanden?
        if (params.isEmpty())
            throw OperationError.NOT_FOUND.exception(meldungKeinParameter);
        if (this.masterclass != String.class)
            throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        // Prüfe, ob alle Parameter vom Typ String sind
        for (final Object p : params)
            if (!(p instanceof String))
                throw OperationError.CONFLICT.exception(meldungUngueltigerParameter);
        @SuppressWarnings("unchecked")
		final List<JMT> paramListeJMT = (List<JMT>) params;
        return getDaten(conn, paramListeJMT);
	}


    /**
     * Bestimmt mithilfe des registrierten Datenobjektes die Daten für die
     * über den Pfad spezifizierten Datenquelle.
     *
     * @param conn     die aktuelle Datenverbindung (des SVWS-Benutzers)
     * @param name     der Name der Datenquelle
     * @param params   die Parameter der Datenquelle, d.h. die Werte für das Attribut der
     *                 Master-Datenquelle, welche bei dieser Datenquelle berücksichtigt
     *                 werden sollen - ist keine Masterquelle definiert, so muss diese Liste leer sein
     *
     * @return die HTTP-Response, im Erfolgsfall mit den Daten der Datenquelle
     */
    public static Response getDaten(final DBEntityManager conn, final String name, final List<? extends Object> params) {
    	try {
	        final DataSchildReportingDatenquelle<?, ?> datenquelle = getMapDatenquellen().get(name);
	        if (datenquelle == null)
	            throw OperationError.NOT_FOUND.exception("Keine Datenquelle \"" + name + "\" vorhanden.");
	        List<? extends Object> result = null;
	        result = switch (datenquelle.mastertyp) {
	            case BOOLEAN -> datenquelle.getDatenBoolean(conn, params);
	            case INT -> datenquelle.getDatenInteger(conn, params);
	            case NUMBER -> datenquelle.getDatenNumber(conn, params);
				case STRING, MEMO -> datenquelle.getDatenString(conn, params);
	            default -> {
	                if (!params.isEmpty())
	                    throw OperationError.CONFLICT.exception("Eine Datenquelle ohne Master-Datenquelle kann keine Parameter entgegennehmen");
	                yield datenquelle.getDaten(conn);
	            }
	        };
	        if (result == null)
	            throw OperationError.NOT_FOUND.exception("Fehler beim Lesen der Daten aus der Datenquelle \"" + name + "\".");
	        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
    	} catch (final WebApplicationException wae) {
    		return wae.getResponse();
    	}
    }


    /**
     * Liefert eine Liste der Definitionen der im SVWS-Server vorhandenen Schild-Datenquellen zurück.
     *
     * @param conn   die Datenbankverbindung des aktuellen SVWS-Benutzers
     *
     * @return die HTTP-Response mit der Liste der Definitionen der im SVWS-Server vorhandenen Schild-Datenquellen
     */
    public static Response getDatenquellen(final DBEntityManager conn) {
        final var datenquellen = getMapDatenquellen();
        final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
        if (schule == null)
            return OperationError.INTERNAL_SERVER_ERROR.getResponse("Kein gültiger Eintrag für die Schule in der Datenbank vorhanden");
        final ArrayList<SchildReportingDatenquelle> result = new ArrayList<>();
        for (final var datenquelle : datenquellen.values()) {
            if ((datenquelle.schulformen.isEmpty()) || (datenquelle.schulformen.contains(schule.Schulform)))
                result.add(datenquelle.datenquelle);
        }
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
    }


    /**
     * Erstellt die Liste der im SVWS-Server verfügbaren Datenquellen. An dieser Stelle
     * müssen einzelnen Data-Objekte des svws-openapi- Projektes erzeugt werden.
     *
     * @return die Liste der im SVWS-Server verfügbaren Datenquellen
     */
    @SuppressWarnings("unused")
	private static LinkedHashMap<String, DataSchildReportingDatenquelle<?, ?>> getMapDatenquellen() {
        if (datenquellen == null) {
            datenquellen = new LinkedHashMap<>();
            new DataSchildReportingDatenquelleSchuelerLernabschnitte();
            new DataSchildReportingDatenquelleSchuelerLeistungsdaten();
			new DataSchildReportingDatenquelleSchuelerSprachpruefungen();
        }
        return datenquellen;
    }

}
