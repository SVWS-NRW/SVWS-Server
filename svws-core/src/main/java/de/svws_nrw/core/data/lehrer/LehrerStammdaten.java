package de.svws_nrw.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Stammdaten eines Lehrers mit der übergebenen ID.  
 */
@XmlRootElement
@Schema(description="Die Stammdaten eines Lehrer-Eintrags.")
@TranspilerDTO
public class LehrerStammdaten {

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example="4711")
	public long id;
	
	/** Das Kürzel des Lehrers. */
	@Schema(description = "Das Kürzel des Lehrers.", example="MUS")
	public @NotNull String kuerzel = "";
	
	/** Die Bezeichnung des Personals-Typs des Lehrers. */
	@Schema(description = "Die Bezeichnung des Personals-Typs des Lehrers.", example="SEKRETARIAT")
	public @NotNull String personalTyp = "";
	
	/** Ggf. die Anrede des Lehrers. */
	@Schema(description = "Ggf. die Anrede des Lehrers.", example="Herr")
	public String anrede;
	
	/** Ggf. ein akademischer Grad des Lehrers. */
	@Schema(description = "Ggf. ein akademischer Grad des Lehrers.", example="Dr.")
	public String titel;
	
	/** Ggf. die Amtsbezeichnung des Lehrers. */
	@Schema(description = "Ggf. die Amtsbezeichnung des Lehrers.", example="OStD")
	public String amtsbezeichnung;
	
	/** Der Nachname des Lehrers. */
	@Schema(description = "Der Nachname des Lehrers.", example="Mustermann")
	public @NotNull String nachname = "";
	
	/** Der Vorname des Lehrers. */
	@Schema(description = "Der Vorname des Lehrers.", example="Max")
	public @NotNull String vorname = "";
	
	/** Die ID des Geschlechtes */
	@Schema(description = "die ID des Geschlechtes", example="3")
	public int geschlecht;
	
	/** Das Geburtsdatum des Lehrers. */
	@Schema(description = "Das Geburtsdatum des Lehrers.", example="1911-11-11")
	public String geburtsdatum;
	
	/** Ggf. die ID für die Staatsangehörigkeit des Lehrers. */
	@Schema(description = "Ggf. die ID für die Staatsangehörigkeit des Lehrers.", example="000")
	public String staatsangehoerigkeitID;

	/** Ggf. der Straßenname im Wohnort des Lehrers. */
	@Schema(description = "Ggf. der Straßenname im Wohnort des Lehrers.", example="Musterweg")
	public String strassenname;
	
	/** Ggf. die Hausnummer zur Straße im Wohnort des Lehrers. */
	@Schema(description = "Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.", example="4711")
	public String hausnummer;
	
	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers. */
	@Schema(description = "Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.", example="a-d")
	public String hausnummerZusatz;
	
	/** Ggf. die ID des Wohnortes des Lehrers. */
	@Schema(description = "Ggf. die ID des Wohnortes des Lehrers.", example="4711")
	public Long wohnortID;

	/** Ggf. die ID des Ortsteils im Wohnort des Lehrers. */
	@Schema(description = "Ggf. die ID des Ortsteils im Wohnort des Lehrers.", example="Muster")	
	public Long ortsteilID;
	
	/** Ggf. die Telefonnummer des Lehrers. */
	@Schema(description = "Ggf. die Telefonnummer des Lehrers.", example="00007-4711")
	public String telefon;
	
	/** Ggf. die Mobilnummer des Lehrers. */
	@Schema(description = "Ggf. die Mobilnummer des Lehrers.", example="0007-47114711")
	public String telefonMobil;
	
	/** Ggf. die private Email-Adresse des Lehrers. */
	@Schema(description = "Ggf. die private Email-Adresse des Lehrers.", example="max.mustermann@home")
	public String emailPrivat;
	
	/** Ggf. die dienstliche Email-Adresse des Lehrers. */
	@Schema(description = "Ggf. die dienstliche Email-Adresse des Lehrers.", example="max.mustermann@dienst")
	public String emailDienstlich;

	/** Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.) */
	@Schema(description = "Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.)", example="ein Bild")
	public String foto;

}
