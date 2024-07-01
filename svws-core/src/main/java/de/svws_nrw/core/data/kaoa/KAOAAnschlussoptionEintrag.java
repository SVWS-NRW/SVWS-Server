package de.svws_nrw.core.data.kaoa;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.core.types.schule.Schulstufe;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA
 * gültigen Anschlussoptionen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der KAoA-Anschlussoptionen.")
@TranspilerDTO
public class KAOAAnschlussoptionEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "1")
	public long id;

	/** Das Kürzel der Anschlussoption. */
	@Schema(description = "das Kürzel der Anschlussoption", example = "STUD")
	public @NotNull String kuerzel = "";

	/** Die Beschreibung der Anschlussoption. */
	@Schema(description = "die Beschreibung der Anschlussoption", example = "Hochschulstudium")
	public @NotNull String beschreibung = "";

	/** Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII) */
	@Schema(description = "Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII)")
	public @NotNull List<String> stufen = new ArrayList<>();

	/** Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden */
	@Schema(description = "Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden")
	public @NotNull List<String> anzeigeZusatzmerkmal = new ArrayList<>();

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2020")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen KAoA-Anschlussoption-Eintrag mit Standardwerten
	 */
	public KAOAAnschlussoptionEintrag() {
	}

	/**
	 * Erstellt einen KAoA-Anschlussoption-Eintrag mit den angegebenen Werten
	 *
	 * @param id             die ID
	 * @param kuerzel        das Kürzel
	 * @param beschreibung   die Beschreibung
	 * @param stufen         die Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII)
	 * @param anzeigeZusatzmerkmal
	 *                       gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public KAOAAnschlussoptionEintrag(final long id, final @NotNull String kuerzel, final @NotNull String beschreibung,
			final @NotNull List<Schulstufe> stufen, final @NotNull List<KAOAZusatzmerkmal> anzeigeZusatzmerkmal,
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		for (final Schulstufe stufe : stufen)
			this.stufen.add(stufe.daten.kuerzel);
		for (final KAOAZusatzmerkmal m : anzeigeZusatzmerkmal)
			this.anzeigeZusatzmerkmal.add(m.daten.kuerzel);
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
