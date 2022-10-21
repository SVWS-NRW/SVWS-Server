package de.nrw.schule.svws.data.schild3.reporting;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Vector;

import de.nrw.schule.svws.core.data.schild3.SchildReportingDatenquelle;
import de.nrw.schule.svws.core.data.schild3.SchildReportingDatenquelleAttribut;
import de.nrw.schule.svws.core.types.schild3.SchildReportingAttributTyp;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
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

    /** Eine Map mit der Zuordnung der Datenquellen zu dem Pfad der Datenquelle */
    private static LinkedHashMap<String, DataSchildReportingDatenquelle> datenquellen = null;

    /** Die Definition der Datenquelle */
    final SchildReportingDatenquelle datenquelle; 


    /**
     * Erstellt einen neue Datenquelle für Schild-Reports
     * 
     * @param name           der Name der Datenquelle 
     * @param path           der Pfad der Datenquelle
     * @param beschreibung   eine textuelle Beschreibung der Datenquelle
     */
    DataSchildReportingDatenquelle(@NotNull String name, @NotNull String path, @NotNull String beschreibung) {
        this.datenquelle = new SchildReportingDatenquelle();
        this.datenquelle.name = name;
        this.datenquelle.path = path;
        this.datenquelle.beschreibung = beschreibung;
        DataSchildReportingDatenquelle.datenquellen.put(path, this);
    }

    
    /**
     * Setzt die Informationen zu der Master-Datenquelle dieser Datenquelle
     * 
     * @param master           der Name der Master-Datenquelle
     * @param masterattribut   das identifizierende Attribut der Master-Datenquelle
     * @param mastertyp        der Datentyp des identifzierenden Master-Attributes
     */
    void setMaster(@NotNull String master, @NotNull String masterattribut, @NotNull SchildReportingAttributTyp mastertyp) {
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
    void addAttribute(Class<?> clazz) {
        for (Field field : clazz.getFields()) {
            SchildReportingAttributTyp typ = switch(field.getType().getSimpleName()) {
                case "long", "int", "short", "byte", "Long", "Integer", "Short", "Byte" -> SchildReportingAttributTyp.INT;
                case "float", "double", "Float", "Double" -> SchildReportingAttributTyp.NUMBER;
                case "boolean", "Boolean" -> SchildReportingAttributTyp.BOOLEAN;
                default -> SchildReportingAttributTyp.STRING;
            };
            Schema schema = clazz.getAnnotation(Schema.class);
            if ((schema == null) || (schema.description() == null))
                throw new NullPointerException("Im Core-DTO musse eine Schema-Definition mit einer 'description' vorhanden sein");
            addAttribut(field.getName(), typ, schema.description());
        }
    }


    private static LinkedHashMap<String, DataSchildReportingDatenquelle> getMapDatenquellen() {
        if (datenquellen == null) {
            datenquellen = new LinkedHashMap<>();
            new DataSchildReportingDatenquelleSchuelerlernabschnitte();
        }
        return datenquellen;
    }
    
    
    public static Response getDatenquellen() {
        var datenquellen = getMapDatenquellen();
        Vector<SchildReportingDatenquelle> result = new Vector<>();
        for (var datenquelle : datenquellen.values())
            result.add(datenquelle.datenquelle);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
    }


    abstract Response getDaten(DBEntityManager conn, InputStream is);


    public static Response getDaten(DBEntityManager conn, String path, InputStream is) {
        DataSchildReportingDatenquelle datenquelle = datenquellen.get(path);
        if (datenquelle == null)
            return OperationError.NOT_FOUND.getResponse("Keine Datenquelle unter " + path + " vorhanden.");
        return datenquelle.getDaten(conn, is);
    }

}
