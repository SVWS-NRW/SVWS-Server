package de.nrw.schule.svws.data.schild3.reporting;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.schild3.SchildReportingDatenquelle;
import de.nrw.schule.svws.core.data.schild3.SchildReportingDatenquelleAttribut;
import de.nrw.schule.svws.core.types.schild3.SchildReportingAttributTyp;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.utils.OperationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für 
 * Datenquellen von Schild-Reports.
 */
public abstract class DataSchildReportingDatenquelle {

    /** Eine Map mit der Zuordnung der Datenquellen zu dem Namen der Datenquelle */
    private static LinkedHashMap<String, DataSchildReportingDatenquelle> datenquellen = null;

    /** Die Definition der Datenquelle */
    final SchildReportingDatenquelle datenquelle; 

    /** Der Typ der Master-Datenquelle */
    private SchildReportingAttributTyp mastertyp = null;

    /** Enthält die Schulformen, auf welche die Datenquelle eingeschränkt ist. Ist die Menge leer, so steht die
     * Datenquelle an allen Schulformen zur Verfügung. */
    private HashSet<Schulform> schulformen = new HashSet<>();


    /**
     * Erstellt einen neue Datenquelle für Schild-Reports
     * 
     * @param clazz   die Klasse des Core-DTOs
     * @param path    der Pfad der Datenquelle
     */
    DataSchildReportingDatenquelle(Class<?> clazz) {
        this.datenquelle = new SchildReportingDatenquelle();
        this.datenquelle.name = clazz.getSimpleName().replace("SchildReporting", "");
        Schema schema = clazz.getAnnotation(Schema.class);
        if ((schema == null) || (schema.description() == null))
            throw new NullPointerException("Im Core-DTO musse eine Schema-Definition mit einer 'description' vorhanden sein");
        this.datenquelle.beschreibung = schema.description();
        this.addAttribute(clazz);
        DataSchildReportingDatenquelle.datenquellen.put(this.datenquelle.name, this);
    }

    
    /**
     * Setzt die Informationen zu der Master-Datenquelle dieser Datenquelle
     * 
     * @param master           der Name der Master-Datenquelle
     * @param masterattribut   das identifizierende Attribut der Master-Datenquelle
     * @param mastertyp        der Datentyp des identifzierenden Master-Attributes
     * @param linkattribut     der Name des Attributs dieser Datenquelle, welches für 
     *                         die Verbindung zu der Master-Datenquelle genutzt wird
     */
    void setMaster(@NotNull String linkattribut, @NotNull String master, @NotNull String masterattribut, @NotNull SchildReportingAttributTyp mastertyp) {
        this.mastertyp = mastertyp;
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
    private void addAttribut(@NotNull String name, @NotNull SchildReportingAttributTyp typ, @NotNull String beschreibung) {
        SchildReportingDatenquelleAttribut attr = new SchildReportingDatenquelleAttribut();
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
    private void addAttribute(Class<?> clazz) {
        for (Field field : clazz.getFields()) {
            SchildReportingAttributTyp typ = switch(field.getType().getSimpleName()) {
                case "long", "int", "short", "byte", "Long", "Integer", "Short", "Byte" -> SchildReportingAttributTyp.INT;
                case "float", "double", "Float", "Double" -> SchildReportingAttributTyp.NUMBER;
                case "boolean", "Boolean" -> SchildReportingAttributTyp.BOOLEAN;
                default -> SchildReportingAttributTyp.STRING;
            };
            Schema schema = field.getAnnotation(Schema.class);
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
    public void restrictTo(Schulform... schulformen) {
        for (Schulform sf : schulformen)
            this.schulformen.add(sf);
    }


    /**
     * Liefert eine Liste der Definitionen der im SVWS-Server vorhandenen Schild-Datenquellen zurück.
     * 
     * @param conn   die Datenbankverbindung des aktuellen SVWS-Benutzers
     * 
     * @return die HTTP-Response mit der Liste der Definitionen der im SVWS-Server vorhandenen Schild-Datenquellen 
     */
    public static Response getDatenquellen(DBEntityManager conn) {
        var datenquellen = getMapDatenquellen();
        DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
        if (schule == null)
            return OperationError.INTERNAL_SERVER_ERROR.getResponse("Kein gültiger Eintrag für die Schule in der Datenbank vorhanden");
        Vector<SchildReportingDatenquelle> result = new Vector<>();
        for (var datenquelle : datenquellen.values()) {
            if ((datenquelle.schulformen.size() == 0) || (datenquelle.schulformen.contains(schule.Schulform)))
                result.add(datenquelle.datenquelle);
        }
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
    }


    /**
     * Bestimmt die Datensätze der Datenquelle, sofern keine Master-Datenquelle 
     * angegeben wurde.
     * 
     * @param conn   die Datenbankverbindung des aktuellen SVWS-Benutzers
     * 
     * @return die Datensätze der Datenquelle
     */
    List<? extends Object> getDaten(DBEntityManager conn) {
        throw new UnsupportedOperationException();
    }


    /**
     * Bestimmt die Datensätze der Datenquelle, sofern der Attributtyp des
     * Attributes der Master-Datenquelle definiert wurde und "boolean" ist. 
     * 
     * @param conn     die Datenbankverbindung des aktuellen SVWS-Benutzers
     * @param params   die Parameter für das Attribut der Masterdatenquelle
     * 
     * @return die Datensätze der Datenquelle
     */
    List<? extends Object> getDatenBoolean(DBEntityManager conn, List<Boolean> params) {
        throw new UnsupportedOperationException();
    }


    /**
     * Bestimmt die Datensätze der Datenquelle, sofern der Attributtyp des
     * Attributes der Master-Datenquelle definiert wurde und "integer" ist. 
     * 
     * @param conn     die Datenbankverbindung des aktuellen SVWS-Benutzers
     * @param params   die Parameter für das Attribut der Masterdatenquelle
     * 
     * @return die Datensätze der Datenquelle
     */
    List<? extends Object> getDatenInteger(DBEntityManager conn, List<Long> params) {
        throw new UnsupportedOperationException();
    }


    /**
     * Bestimmt die Datensätze der Datenquelle, sofern der Attributtyp des
     * Attributes der Master-Datenquelle definiert wurde und "number" ist. 
     * 
     * @param conn     die Datenbankverbindung des aktuellen SVWS-Benutzers
     * @param params   die Parameter für das Attribut der Masterdatenquelle
     * 
     * @return die Datensätze der Datenquelle
     */
    List<? extends Object> getDatenNumber(DBEntityManager conn, List<Double> params) {
        throw new UnsupportedOperationException();
    }


    /**
     * Bestimmt die Datensätze der Datenquelle, sofern der Attributtyp des
     * Attributes der Master-Datenquelle definiert wurde und "string" ist. 
     * 
     * @param conn     die Datenbankverbindung des aktuellen SVWS-Benutzers
     * @param params   die Parameter für das Attribut der Masterdatenquelle
     * 
     * @return die Datensätze der Datenquelle
     */
    List<? extends Object> getDatenString(DBEntityManager conn, List<String> params) {
        throw new UnsupportedOperationException();
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
    public static Response getDaten(DBEntityManager conn, String name, List<? extends Object> params) {
        DataSchildReportingDatenquelle datenquelle = getMapDatenquellen().get(name);
        if (datenquelle == null)
            return OperationError.NOT_FOUND.getResponse("Keine Datenquelle \"" + name + "\" vorhanden.");
        List<? extends Object> result = null;
        switch (datenquelle.mastertyp) {
            case BOOLEAN -> {
                // Daten vorhanden?
                if (params.size() == 0)
                    return OperationError.NOT_FOUND.getResponse("Kein Parameter für das Attribut der Master-Datenquelle angegeben");
                // Prüfe, ob alle Parameter vom Typ Boolean sind
                for (Object p : params) {
                    if (!(p instanceof Boolean))
                        return OperationError.CONFLICT.getResponse("Ungültiger Parameter für das Attribut der Master-Datenquelle angegeben");
                }
                @SuppressWarnings("unchecked")
                List<Boolean> paramListe = (List<Boolean>) params;
                result = datenquelle.getDatenBoolean(conn, paramListe);
            }
            case INT -> {
                // Daten vorhanden?
                if (params.size() == 0)
                    return OperationError.NOT_FOUND.getResponse("Kein Parameter für das Attribut der Master-Datenquelle angegeben");
                // Prüfe, ob alle Parameter vom Typ Long sind
                Vector<Long> paramListe = new Vector<>();
                for (Object p : params) {
                    if (p instanceof Long l)
                        paramListe.add(l);
                    else if (p instanceof Integer i)
                        paramListe.add(i.longValue());
                    else if (p instanceof Short s)
                        paramListe.add(s.longValue());
                    else if (p instanceof Byte b)
                        paramListe.add(b.longValue());
                    else
                        return OperationError.CONFLICT.getResponse("Ungültiger Parameter für das Attribut der Master-Datenquelle angegeben");
                }
                result = datenquelle.getDatenInteger(conn, paramListe);
            }
            case NUMBER -> {
                // Daten vorhanden?
                if (params.size() == 0)
                    return OperationError.NOT_FOUND.getResponse("Kein Parameter für das Attribut der Master-Datenquelle angegeben");
                // Prüfe, ob alle Parameter vom Typ Double sind
                Vector<Double> paramListe = new Vector<>();
                for (Object p : params) {
                    if (p instanceof Double d)
                        paramListe.add(d);
                    else if (p instanceof Float f)
                        paramListe.add(f.doubleValue());
                    else
                        return OperationError.CONFLICT.getResponse("Ungültiger Parameter für das Attribut der Master-Datenquelle angegeben");
                }
                result = datenquelle.getDatenNumber(conn, paramListe);
            }
            case STRING -> {
                // Daten vorhanden?
                if (params.size() == 0)
                    return OperationError.NOT_FOUND.getResponse("Kein Parameter für das Attribut der Master-Datenquelle angegeben");
                // Prüfe, ob alle Parameter vom Typ String sind
                for (Object p : params) {
                    if (!(p instanceof String))
                        return OperationError.CONFLICT.getResponse("Üngültiger Parameter für das Attribut der Master-Datenquelle angegeben");
                }
                @SuppressWarnings("unchecked")
                List<String> paramListe = (List<String>) params;
                result = datenquelle.getDatenString(conn, paramListe);
            }
            default -> {
                if (params.size() > 0)
                    return OperationError.CONFLICT.getResponse("Eine Datenquelle ohne Master-Datenquelle kann keine Parameter entgegennehmen");
                result = datenquelle.getDaten(conn);
            }
        }
        if (result == null)
            return OperationError.NOT_FOUND.getResponse("Fehler beim Lesen der Daten aus der Datenquelle \"" + name + "\".");
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
    }


    /**
     * Erstellt die Liste der im SVWS-Server verfügbaren Datenquellen. An dieser Stelle
     * müssen einzelnen Data-Objekte des svws-openapi- Projektes erzeugt werden.  
     *  
     * @return die Liste der im SVWS-Server verfügbaren Datenquellen
     */
    private static LinkedHashMap<String, DataSchildReportingDatenquelle> getMapDatenquellen() {
        if (datenquellen == null) {
            datenquellen = new LinkedHashMap<>();
            new DataSchildReportingDatenquelleSchuelerlernabschnitte();
        }
        return datenquellen;
    }

}
