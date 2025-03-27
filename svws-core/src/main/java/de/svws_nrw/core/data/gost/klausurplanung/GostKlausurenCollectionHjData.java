package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Sammlung aller zur Klausurplanung eines Gost-Halbjahres gehörigen Klausurdaten.
 */
@XmlRootElement
@Schema(description = "die Sammlung aller zur Klausurplanung eines Gost-Halbjahres gehörigen Klausurdaten.")
@TranspilerDTO
public class GostKlausurenCollectionHjData {

	/** Der Abiturjahrgang. */
	public int abiturjahrgang = -1;

	/** Das Gost-Halbjahr. */
	public int gostHalbjahr = -1;

	/** Der Schuljahresabschnitt. */
	public long schuljahresabschnitt = -1;

	/** Die zu den Klausurdaten gehörenden Meta-Informationen wie Fachdaten, Kursdaten, Lehrerdaten, Schülerdaten. */
	@Schema(implementation = GostKlausurenCollectionData.class,
			description = "Die zu den Klausurdaten gehörenden Meta-Informationen wie Fachdaten, Kursdaten, Lehrerdaten, Schülerdaten.")
	public @NotNull GostKlausurenCollectionData data = new GostKlausurenCollectionData();

	/** Ein Array mit den Daten der Fächer. */
	@ArraySchema(schema = @Schema(implementation = GostFach.class, description = "Ein Array mit den Daten der Fächer."))
	public List<GostFach> faecher = null;

	/** Ein Array mit den Daten der Schüler. */
	@ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class, description = "Ein Array mit den Daten der Schüler."))
	public List<SchuelerListeEintrag> schueler = null;

	/** Ein Array mit den Daten der Kurse. */
	@ArraySchema(schema = @Schema(implementation = KursDaten.class, description = "Ein Array mit den Daten der Kurse."))
	public @NotNull List<KursDaten> kurse = new ArrayList<>();

	/** Die zu den Klausurdaten gehörenden Raumdaten. */
	@Schema(implementation = GostKlausurenCollectionRaumData.class,
			description = "Die zu den Klausurdaten gehörenden Raumdaten.")
	public @NotNull GostKlausurenCollectionRaumData raumdata = new GostKlausurenCollectionRaumData();

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenCollectionHjData() {
		super();
	}

	/**
	 * Konstruktor für die Angabe des Abiturjahrgangs und des Gost-Halbjahres.
	 * @param abiturjahrgang der Abiturjahrgang
	 * @param gostHalbjahr das Gost-Halbjahr
	 */
	public GostKlausurenCollectionHjData(final int abiturjahrgang, final int gostHalbjahr) {
		this.abiturjahrgang = abiturjahrgang;
		this.gostHalbjahr = gostHalbjahr;
	}

}
