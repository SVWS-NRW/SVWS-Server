package de.svws_nrw.abschluesse.ge.test;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.svws_nrw.core.data.abschluss.AbschlussErgebnis;
import de.svws_nrw.core.data.abschluss.GEAbschlussFaecher;

/** Diese Klasse stelt die Testfälle für die Prognoseberechnung an der Gesamtschule zur Verfügung. */
@XmlRootElement(name = "GEAbschlussTestfall")
public class GEAbschlussTestfall {

	/** Die Fachinformationen für die Abschlussberechnung. */
	@JsonProperty("input")
	public GEAbschlussFaecher input;

	/** Das erwartete Ergebnis bei der Prognoseberechnung. */
	@JsonProperty("Prognose")
	public AbschlussErgebnis prognose;

	/** Das erwartete Ergebnis bei der Berechnung des Hauptschulabschlusses nach Klasse 9. */
	@JsonProperty("HA9")
	public AbschlussErgebnis ha9;

	/** Das erwartete Ergebnis bei der Berechnung des Hauptschulabschlusses nach Klasse 10. */
	@JsonProperty("HA10")
	public AbschlussErgebnis ha10;

	/** Das erwartete Ergebnis bei der Berechnung des Mittleren Schulabschlusses nach Klasse 10. */
	@JsonProperty("MSA")
	public AbschlussErgebnis msa;

	/** Das erwartete Ergebnis bei der Berechnung der Berechtigung zum Besuch der gymnasialen Oberstufe mit dem Mittleren Schulabschlusses nach Klasse 10. */
	@JsonProperty("MSA_Q")
	public AbschlussErgebnis msa_q;



	/**
	 * Vergleicht die beiden Listen mit Nachprüfungsfächern, auf Identität
	 *
	 * @param faecherListe1   die erste Liste
	 * @param faecherListe2   die zweite Liste
	 *
	 * @return true, falls die beiden Listen übereinstimmen
	 */
	@JsonIgnore
    public static boolean vergleicheNachpruefungsfaecher(final List<String> faecherListe1, final List<String> faecherListe2) {
        if ((faecherListe1 == null) || faecherListe1.isEmpty()) {
            return (faecherListe2 == null) || faecherListe2.isEmpty();
        } else if ((faecherListe2 == null) || faecherListe2.isEmpty())
            return faecherListe1.isEmpty();
        return faecherListe1.containsAll(faecherListe2) && faecherListe2.containsAll(faecherListe1);
    }

}

