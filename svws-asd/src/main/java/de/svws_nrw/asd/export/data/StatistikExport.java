package de.svws_nrw.asd.export.data;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class StatistikExport {

	/** Die Daten der Schule (B01). */
	@Schema(description = "Die Daten der Schule (B01)")
	public @NotNull SchuleStatistikExport schuleStatistikExport = new SchuleStatistikExport();

	/** Die Religionszugehörigkeiten der Schueler (S42). */
	@ArraySchema(
			schema = @Schema(implementation = ReligionszugehoerigkeitenStatistikExport.class, description = "Die Religionszugehörigkeiten der Schueler (S42)"))
	public @NotNull List<ReligionszugehoerigkeitenStatistikExport> religionszugehoerigkeitenStatistikExport = new ArrayList<>();

	/** Vom Schulbesuch zurueckgestellte Kinder (S43). */
	@Schema(description = "vom Schulbesuch zurueckgestellte Kinder (S43)")
	public @NotNull VomSchulbesuchZurueckgestelltStatistikExport vomSchulbesuchZurueckgestelltStatistikExport =
			new VomSchulbesuchZurueckgestelltStatistikExport();

	/** Die Summen der Abgänger (V51). */
	@ArraySchema(schema = @Schema(implementation = AbgaengerStatistikExport.class,
			description = "die Summen der Abgänger (V51)."))
	public @NotNull List<AbgaengerStatistikExport> abgaengerStatistikExport = new ArrayList<>();

	/** Die Daten der Lehrer (L61). */
	@ArraySchema(schema = @Schema(implementation = LehrerStatistikExport.class,
			description = "ein Array mit Daten der Lehrer (L61)"))
	public @NotNull List<LehrerStatistikExport> lehrerStatistikExport = new ArrayList<>();

	/** Die Daten zum Unterricht (U71). */
	@ArraySchema(schema = @Schema(implementation = UnterrichtsverteilungStatistikExport.class,
			description = "ein Array mit Daten der Unterrichtsverteilung (U71)"))
	public @NotNull List<UnterrichtsverteilungStatistikExport> unterrichtsverteilungStatistikExport = new ArrayList<>();

	/** Die Daten der (Teil-) Klassen (K81). */
	@ArraySchema(schema = @Schema(implementation = KlassenStatistikExport.class,
			description = "ein Array mit den Klassen"))
	public @NotNull List<KlassenStatistikExport> klassenStatistikExport = new ArrayList<>();

	/** Die Summendaten der Schüler (K84). */
	@Schema(description = "die Summendaten der Schüler (K84)")
	public @NotNull SchuelerZahlenStatistikExport schuelerZahlenStatistikExport = new SchuelerZahlenStatistikExport();

	/** Die durchschnittlichen Schülerzahlen der Klinikschule (K89). */
	@Schema(description = "die durchschnittlichen Schülerzahlen der Klinikschule (K89)")
	public @NotNull KlinikschuleStatistikExport klinikschuleStatistikExport = new KlinikschuleStatistikExport();

	/** Die Abiturprüfungsergebnisse (X93). */
	@ArraySchema(schema = @Schema(implementation = AbiturpruefungsergebnisseStatistikExport.class,
			description = "ein Array mit den Abiturprüfungsergebnissen"))
	public @NotNull List<AbiturpruefungsergebnisseStatistikExport> abiturpruefungsergebnisseStatistikExport = new ArrayList<>();

	/** Die Daten zu den Internatsplätzen der Schule (X97). */
	@Schema(description = "die Daten zu den Internatsplätzen der Schule (X97)")
	public @NotNull InternatsplaetzeStatistikExport internatsplaetzeStatistikExport = new InternatsplaetzeStatistikExport();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StatistikExport() {
		// leer
	}

}
