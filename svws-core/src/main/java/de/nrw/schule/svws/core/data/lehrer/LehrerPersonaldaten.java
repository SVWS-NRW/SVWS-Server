package de.nrw.schule.svws.core.data.lehrer;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Personaldaten eines Lehrers mit der übergebenen ID.  
 */
@XmlRootElement
@Schema(description="Die Personaldaten eines Lehrer-Eintrags.")
@TranspilerDTO
public class LehrerPersonaldaten {

	/** Die ID des Lehrers. */
	@Schema(required = true, description = "Die ID des Lehrers.", example="4711")
	public long id;

	/** Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen. */
	@Schema(required = false, description = "Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen.", example="1708593")
	public String identNrTeil1;
	
	/** Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben. */
	@Schema(required = false, description = "Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben.", example="")
	public String identNrTeil2SerNr;

	/** Die Personalaktennummer des Lehrers für den Export zu GPC. */
	@Schema(required = false, description = "Die Personalaktennummer des Lehrers für den Export zu GPC.", example="")
	public String personalaktennummer;

	/** Die Personalnummer des LBV. */
	@Schema(required = false, description = "Die Personalnummer des LBV.", example="")
	public String lbvPersonalnummer; 
	
	/** Der zur Personalnummer gehörige Vergütungsschlüssel. */
	@Schema(required = false, description = "Der zur Personalnummer gehörige Vergütungsschlüssel.", example="2")
	public String lbvVerguetungsschluessel; 
	
	/** Das Datum, wann der Lehrer an die Schule gekommen ist. */
	@Schema(required = false, description = "Das Datum, wann der Lehrer an die Schule gekommen ist.", example="1911-11-11")
	public String zugangsdatum;
	
	/** Der Grund für den Zugang des Lehrers - siehe Statistik-Katalog. */
	@Schema(required = false, description = "Der Grund für den Zugang des Lehrers - siehe Statistik-Katalog.", example="WIEDER")
	public String zugangsgrund;
	
	/** Das Datum, wann der Lehrer an die Schule verlassen hat. */
	@Schema(required = false, description = "Das Datum, wann der Lehrer an die Schule verlassen hat.", example="1920-11-12")
	public String abgangsdatum;
	
	/** Der Grund für den Abgang des Lehrers - siehe Statistik-Katalog. */
	@Schema(required = false, description = "Der Grund für den Abgang des Lehrers - siehe Statistik-Katalog.", example="RUHEST")
	public String abgangsgrund;
	
	/** Das Pflichtstundensoll des Lehrers. */
	@Schema(required = false, description = "Das Pflichtstundensoll des Lehrers.", example="18.5")
	public Double pflichtstundensoll;
	
	/** Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog. */
	@Schema(required = false, description = "Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog.", example="L")
	public String rechtsverhaeltnis;

	/** Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog. */
	@Schema(required = false, description = "Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog.", example="T")
	public String beschaeftigungsart;
	
	/** Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Statistik-Katalog. */
	@Schema(required = false, description = "Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Statistik-Katalog.", example="*")
	public String einsatzstatus;
	
	/** Die Schulnummer der Stammschule, sofern diese abweicht. */
	@Schema(required = false, description = "Die Schulnummer der Stammschule, sofern diese abweicht.", example="168890")
	public String stammschulnummer;

	/** Gibt an, ob ein Nachweis über die Masern-Impfung erbracht wurde. */
	@Schema(required = false, description = "Gibt an, ob ein Nachweis über die Masern-Impfung erbracht wurde.", example="true")
	public Boolean masernImpfnachweis;
	
	// TODO Lehrämter mit Array und weiteren DTO-Klassen
	
	// TODO Mehr- und Minder leistungen als Array mit weiteren DTO-Klassen
}
