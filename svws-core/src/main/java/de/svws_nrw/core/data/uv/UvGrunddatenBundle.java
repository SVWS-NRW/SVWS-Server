package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Sammlung aller zur Unterrichtsverteilung gehörigen Grunddaten.
 */
@XmlRootElement
@Schema(description = "die Sammlung aller zur Unterrichtsverteilung gehörigen Grunddaten.")
@TranspilerDTO
public class UvGrunddatenBundle {

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvLehrer.class, description = "Ein Array mit "))
	public @NotNull List<UvLehrer> lehrer = new ArrayList<>();

	/** Ein Array mit den Unterrichtsfächern der Lehrer */
	@ArraySchema(schema = @Schema(implementation = LehrerUnterrichtsfach.class, description = "Ein Array mit den Unterrichtsfächern der Lehrer"))
	public @NotNull List<LehrerUnterrichtsfach> lehrerUnterrichtsfaecher = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvRaum.class, description = "Ein Array mit "))
	public @NotNull List<UvRaum> raeume = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvRaumgruppe.class, description = "Ein Array mit "))
	public @NotNull List<UvRaumgruppe> raumgruppen = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvStundentafel.class, description = "Ein Array mit "))
	public @NotNull List<UvStundentafel> stundentafeln = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvStundentafelFach.class, description = "Ein Array mit "))
	public @NotNull List<UvStundentafelFach> stundentafelfaecher = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvZeitraster.class, description = "Ein Array mit "))
	public @NotNull List<UvZeitraster> zeitraster = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvZeitrasterEintrag.class, description = "Ein Array mit "))
	public @NotNull List<UvZeitrasterEintrag> zeitrastereintraege = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvFach.class, description = "Ein Array mit "))
	public @NotNull List<UvFach> faecher = new ArrayList<>();

	/** Ein Array mit den Anrechnungsstunden der Lehrer */
	@ArraySchema(schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class, description = "Ein Array mit den Anrechnungsstunden der Lehrer"))
	public @NotNull List<UvLehrerAnrechnungsstunden> lehrerAnrechnungsstunden = new ArrayList<>();

	/** Ein Array mit dem Pflichtstundensoll der Lehrer */
	@ArraySchema(schema = @Schema(implementation = UvLehrerPflichtstundensoll.class, description = "Ein Array mit dem Pflichtstundensoll der Lehrer"))
	public @NotNull List<UvLehrerPflichtstundensoll> lehrerPflichtstundensoll = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public UvGrunddatenBundle() {
		super();
	}

}
