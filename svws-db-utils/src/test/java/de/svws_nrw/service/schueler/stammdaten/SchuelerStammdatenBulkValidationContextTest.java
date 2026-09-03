package de.svws_nrw.service.schueler.stammdaten;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.repo.schule.kataloge.fahrschuelerart.FahrschuelerartRepository;
import de.svws_nrw.repo.schule.kataloge.haltestelle.HaltestelleRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SchuelerStammdatenBulkValidationContext")
class SchuelerStammdatenBulkValidationContextTest {

	@Mock
	private OrtRepository ortRepository;

	@Mock
	private OrtsteilRepository ortsteilRepository;

	@Mock
	private ReligionRepository religionRepository;

	@Mock
	private FahrschuelerartRepository fahrschuelerartRepository;

	@Mock
	private HaltestelleRepository haltestelleRepository;

	@BeforeEach
	void setUp() {
		when(ortRepository.existsByIds(anyCollection())).thenReturn(Set.of());
		when(ortsteilRepository.findListByIds(anyCollection())).thenReturn(List.of());
		when(religionRepository.existsByIds(anyCollection())).thenReturn(Set.of());
		when(fahrschuelerartRepository.existsByIds(anyCollection())).thenReturn(Set.of());
		when(haltestelleRepository.existsByIds(anyCollection())).thenReturn(Set.of());
	}

	private SchuelerStammdatenBulkValidationContext load(final List<? extends SchuelerStammdatenPatchRequest> dtos) {
		return SchuelerStammdatenBulkValidationContext.load(
				dtos,
				ortRepository,
				ortsteilRepository,
				religionRepository,
				fahrschuelerartRepository,
				haltestelleRepository
		);
	}

	// =========================================================================
	// leere DTOs
	// =========================================================================

	@Nested
	@DisplayName("leere DTOs")
	class LeereDtos {

		@Test
		@DisplayName("existingOrtIds ist leer")
		void leereDtos_ortIds() {
			assertThat(load(List.of()).existingOrtIds()).isEmpty();
		}

		@Test
		@DisplayName("ortsteilById ist leer")
		void leereDtos_ortsteilById() {
			assertThat(load(List.of()).ortsteilById()).isEmpty();
		}

		@Test
		@DisplayName("existingReligionIds ist leer")
		void leereDtos_religionIds() {
			assertThat(load(List.of()).existingReligionIds()).isEmpty();
		}

		@Test
		@DisplayName("existingFahrschuelerartIds ist leer")
		void leereDtos_fahrschuelerartIds() {
			assertThat(load(List.of()).existingFahrschuelerartIds()).isEmpty();
		}

		@Test
		@DisplayName("existingHaltestelleIds ist leer")
		void leereDtos_haltestelleIds() {
			assertThat(load(List.of()).existingHaltestelleIds()).isEmpty();
		}
	}

	// =========================================================================
	// collectPresent — undefined und null werden ignoriert
	// =========================================================================

	@Nested
	@DisplayName("collectPresent")
	class CollectPresent {

		@Test
		@DisplayName("undefined wohnortID wird nicht gesammelt")
		void collectPresent_undefinedWirdIgnoriert() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.wohnortID = JsonNullable.undefined();

			when(ortRepository.existsByIds(Set.of())).thenReturn(Set.of());

			assertThat(load(List.of(dto)).existingOrtIds()).isEmpty();
		}

		@Test
		@DisplayName("JsonNullable.of(null) wohnortID wird nicht gesammelt")
		void collectPresent_nullWirdIgnoriert() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.wohnortID = JsonNullable.of(null);

			when(ortRepository.existsByIds(Set.of())).thenReturn(Set.of());

			assertThat(load(List.of(dto)).existingOrtIds()).isEmpty();
		}

		@Test
		@DisplayName("gesetzte wohnortID wird gesammelt")
		void collectPresent_gesetzteIdWirdGesammelt() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.wohnortID = JsonNullable.of(100L);

			when(ortRepository.existsByIds(Set.of(100L))).thenReturn(Set.of(100L));

			assertThat(load(List.of(dto)).existingOrtIds()).containsExactly(100L);
		}

		@Test
		@DisplayName("Duplikate über mehrere DTOs werden dedupliziert")
		void collectPresent_duplikateWerdenDedupliziert() {
			final var dto1 = new SchuelerStammdatenPatchRequest();
			dto1.wohnortID = JsonNullable.of(100L);
			final var dto2 = new SchuelerStammdatenPatchRequest();
			dto2.wohnortID = JsonNullable.of(100L);

			when(ortRepository.existsByIds(Set.of(100L))).thenReturn(Set.of(100L));

			assertThat(load(List.of(dto1, dto2)).existingOrtIds()).containsExactly(100L);
		}
	}

	// =========================================================================
	// ortsteilById
	// =========================================================================

	@Nested
	@DisplayName("ortsteilById")
	class OrtsteilById {

		@Test
		@DisplayName("gefundene Ortsteile werden als Map geliefert")
		void ortsteilById_wirdBefuellt() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.ortsteilID = JsonNullable.of(20L);

			final var ortsteil = new DTOOrtsteil(20L, "Musterortsteil");
			when(ortsteilRepository.findListByIds(Set.of(20L))).thenReturn(List.of(ortsteil));

			assertThat(load(List.of(dto)).ortsteilById()).containsKey(20L);
		}

		@Test
		@DisplayName("nicht gefundene Ortsteil-ID ergibt leere Map")
		void ortsteilById_nichtGefunden() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.ortsteilID = JsonNullable.of(20L);

			when(ortsteilRepository.findListByIds(Set.of(20L))).thenReturn(List.of());

			assertThat(load(List.of(dto)).ortsteilById()).isEmpty();
		}
	}

	// =========================================================================
	// Felder werden unabhängig voneinander gesammelt
	// =========================================================================

	@Nested
	@DisplayName("Feldisolation")
	class Feldisolation {

		@Test
		@DisplayName("religionID wird unabhängig von wohnortID gesammelt")
		void feldisolation_religion() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.religionID = JsonNullable.of(5L);

			when(religionRepository.existsByIds(Set.of(5L))).thenReturn(Set.of(5L));

			assertThat(load(List.of(dto)).existingReligionIds()).containsExactly(5L);
		}

		@Test
		@DisplayName("fahrschuelerArtID wird unabhängig gesammelt")
		void feldisolation_fahrschuelerart() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.fahrschuelerArtID = JsonNullable.of(3L);

			when(fahrschuelerartRepository.existsByIds(Set.of(3L))).thenReturn(Set.of(3L));

			assertThat(load(List.of(dto)).existingFahrschuelerartIds()).containsExactly(3L);
		}

		@Test
		@DisplayName("haltestelleID wird unabhängig gesammelt")
		void feldisolation_haltestelle() {
			final var dto = new SchuelerStammdatenPatchRequest();
			dto.haltestelleID = JsonNullable.of(7L);

			when(haltestelleRepository.existsByIds(Set.of(7L))).thenReturn(Set.of(7L));

			assertThat(load(List.of(dto)).existingHaltestelleIds()).containsExactly(7L);
		}
	}
}
