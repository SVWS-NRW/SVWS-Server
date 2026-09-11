package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird für die Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Anrechnungsstunden eines Lehrers innerhalb des Unterrichtsverteilungsmoduls.
 */
@XmlRootElement
@Schema(description = "die Daten einer Anrechnungsstunde eines Lehrers innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvLehrerAnrechnungsstunden {

	/** Die ID des Anrechnungsstunden-Eintrags. */
	@Schema(description = "die ID des Anrechnungsstunden-Eintrags", example = "4711")
	public long id = -1;

	/** Die ID des Lehrers innerhalb des Unterrichtsverteilungsmoduls. */
	@Schema(description = "die ID des Lehrers innerhalb des UV-Moduls", example = "1001")
	public long idLehrer = -1;

	/** Das Kürzel des Anrechnungsgrundes (z. B. 'AG' für Arbeitsgemeinschaft). */
	@Schema(description = "das Kürzel des Anrechnungsgrundes", example = "AG")
	public @NotNull String anrechnungsgrundKrz = "";

	/** Die Anzahl der angerechneten Stunden. */
	@Schema(description = "die Anzahl der angerechneten Stunden", example = "2.5")
	public double anzahlStunden;

	/** Das Datum, ab dem die Anrechnungsstunde gültig ist. */
	@Schema(description = "das Datum, ab dem die Anrechnungsstunde gültig ist", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann die Anrechnungsstunde gültig ist. */
	@Schema(description = "das Datum, bis wann die Anrechnungsstunde gültig ist", example = "2026-07-31")
	public String gueltigBis = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvLehrerAnrechnungsstunden() {
		// leer
	}

	/**
	 * Liefert eine String-Repräsentation des UvLehrerAnrechnungsstunden-Objekts.
	 *
	 * @return die String-Repräsentation des Objekts
	 */
	@Override
	public String toString() {
		return "UvLehrerAnrechnungsstunden{id=" + id
				+ ", idLehrer=" + idLehrer
				+ ", anrechnungsgrundKrz=" + anrechnungsgrundKrz
				+ ", anzahlStunden=" + anzahlStunden
				+ ", gueltigVon=" + gueltigVon
				+ ", gueltigBis=" + gueltigBis
				+ "}";
	}

	/**
	 * Vergleicht dieses Objekt mit einem anderen auf Gleichheit.
	 * Der Vergleich erfolgt ausschließlich über die ID.
	 *
	 * @param obj das zu vergleichende Objekt
	 * @return {@code true}, falls beide Objekte dieselbe ID besitzen
	 */
	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof final UvLehrerAnrechnungsstunden other)
				&& (this.id == other.id);
	}

	/**
	 * Erzeugt einen Hashcode auf Basis der ID.
	 *
	 * @return der Hashcode des Objekts
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
