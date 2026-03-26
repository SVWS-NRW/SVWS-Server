package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem UV-Planungsabschnitt-Schüler.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem UV-Planungsabschnitt-Schüler.")
@TranspilerDTO
public class UvSchueler {

    /** Die ID des Eintrags. */
    @Schema(description = "die ID des Planungsabschnitts", example = "815")
    public long idPlanungsabschnitt = -1;

    /** Die ID des Schülers. */
    @Schema(description = "die ID des Schülers", example = "1234")
    public long idSchueler = -1;

    /** Die ID des Jahrgangs. */
    @Schema(description = "die ID des Jahrgangs", example = "10")
    public Long idJahrgang;

    /** Die ID der Klasse. */
    @Schema(description = "die ID der Klasse", example = "5")
    public Long idKlasse;

    /**
     * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
     *
     * @param another das zu vergleichende Objekt
     * @return true, falls die Objekte identisch sind, sonst false
     */
    @Override
    public boolean equals(final Object another) {
        return (another instanceof final UvSchueler ano) && (this.idPlanungsabschnitt == ano.idPlanungsabschnitt) && (this.idSchueler == ano.idSchueler);
    }

    /**
     * Erzeugt den Hashcode zu Objekt auf Basis der idVorgabe.
     *
     * @return den HashCode
     */
    @Override
	public int hashCode() {
		return (31 * Long.hashCode(idPlanungsabschnitt)) + Long.hashCode(idSchueler);
	}

    /**
     * Gibt eine String-Repräsentation des Objekts zurück.
     */
    @Override
    public String toString() {
        return idPlanungsabschnitt + "-" + idSchueler;
    }

    /**
     * Default-Konstruktor
     */
    public UvSchueler() {
        super();
    }

	/**
	 * Erstellt aus einer Liste von UvSchueler-Objekten eine Liste von LongPair-Objekten zur Kommunikation über die OpenAPI-Schnittstelle.
	 *
	 * @param list die Liste von UvSchueler-Objekten
	 * @return die Liste von LongPair-Objekten
	 */
	public static List<LongPair> idsFromList(final List<UvSchueler> list) {
		final List<LongPair> result = new ArrayList<>();
		if (list == null) {
			return result;
		}
		for (final UvSchueler s : list) {
			result.add(new LongPair(s.idPlanungsabschnitt, s.idSchueler));
		}
		return result;
	}
}
