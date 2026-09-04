package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgang;
import de.svws_nrw.asd.types.schueler.HerkunftSchulform;
import de.svws_nrw.asd.types.schueler.HerkunftSonstige;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerSchulbesuchResolverTest {

	// -------------------------------------------------------------------------
	// Stabile CoreType-IDs aus den JSON-Katalogen
	// -------------------------------------------------------------------------

	/** Schulform.json: GY, id=6000, kuerzel="GY" – NRW, kein BK/WB/SB */
	private static final long ID_SCHULFORM_GY_NRW = 6000L;
	private static final String KUERZEL_GY = "GY";

	/** Schulform.json: BK, id=1000, kuerzel="BK" – NRW, BK-Schulform */
	private static final long ID_SCHULFORM_BK_NRW = 1000L;
	private static final String KUERZEL_BK = "BK";

	/** Schulform.json: WB, id=18000, kuerzel="WB" – NRW, WB-Schulform */
	private static final long ID_SCHULFORM_WB_NRW = 18000L;
	private static final String KUERZEL_WB = "WB";

	/** Schulform.json: SB, id=13000, kuerzel="SB" – NRW, SB-Schulform */
	private static final long ID_SCHULFORM_SB_NRW = 13000L;
	private static final String KUERZEL_SB = "SB";

	/** HerkunftSchulform.json: GY, id=6001, kuerzel="GY" – sonstige Schule, kein BK/WB/SB/SF */
	private static final long ID_HERKUNFT_SCHULFORM_GY = 6001L;

	/** HerkunftSchulform.json: BK, id=1000, kuerzel="BK" – sonstige Schule, BK */
	private static final long ID_HERKUNFT_SCHULFORM_BK = 1000L;

	/** HerkunftSchulform.json: WB, id=18000, kuerzel="WB" – sonstige Schule, WB */
	private static final long ID_HERKUNFT_SCHULFORM_WB = 18000L;
	private static final String KUERZEL_WB_HERKUNFT = "WB";

	/** HerkunftSchulform.json: SF, id=25001, kuerzel="SF" – sonstige Schulform */
	private static final long ID_HERKUNFT_SCHULFORM_SF = 25001L;
	private static final String KUERZEL_SF = "SF";

	@Mock
	private DataSchulen dataSchulen;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	// =========================================================================
	// patchHerkunftSonstigeVorherigeSchule
	// =========================================================================

	@Nested
	@DisplayName("patchHerkunftSonstigeVorherigeSchule")
	class PatchHerkunftSonstigeVorherigeSchule {

		@Test
		@DisplayName("id null - setzt LSSchulform=null und LSSchulformSIM=null")
		void idNull_setztBeideNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulform = "vorher";
			entity.LSSchulformSIM = "vorher";

			SchuelerSchulbesuchResolver.patchHerkunftSonstigeVorherigeSchule(entity, null);

			assertThat(entity.LSSchulform).isNull();
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("unbekannte id - wirft BAD_REQUEST")
		void unbekannteId_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchHerkunftSonstigeVorherigeSchule(entity, 99999L))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Keine HerkunftSonstige mit der ID 99999 gefunden.")
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("gueltige id - setzt LSSchulform=null und LSSchulformSIM=kuerzel")
		void gueltigeId_setztFelder() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulform = "vorher";
			entity.LSSchulformSIM = "vorher";
			final var eintrag = HerkunftSonstige.data()
					.getWerte().getFirst().historie().getFirst();

			SchuelerSchulbesuchResolver.patchHerkunftSonstigeVorherigeSchule(entity, eintrag.id);

			assertThat(entity.LSSchulform).isNull();
			assertThat(entity.LSSchulformSIM).isEqualTo(eintrag.kuerzel);
		}
	}

	// =========================================================================
	// patchHerkunftbildungsgang
	// =========================================================================

	@Nested
	@DisplayName("patchHerkunftbildungsgang")
	class PatchHerkunftbildungsgang {

		@Test
		@DisplayName("id null - setzt LSSchulformSIM=null")
		void idNull_setztNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulformSIM = "vorher";

			SchuelerSchulbesuchResolver.patchHerkunftbildungsgang(entity, null);

			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("unbekannte id - wirft BAD_REQUEST")
		void unbekannteId_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchHerkunftbildungsgang(entity, 99999L))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Kein HerkunftBildungsgang mit der ID 99999 gefunden.")
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("gueltige id - setzt LSSchulformSIM=schluessel")
		void gueltigeId_setztSchluessel() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulformSIM = "vorher";
			final var eintrag = HerkunftBildungsgang.data()
					.getWerte().getFirst().historie().getFirst();

			SchuelerSchulbesuchResolver.patchHerkunftbildungsgang(entity, eintrag.id);

			assertThat(entity.LSSchulformSIM).isEqualTo(eintrag.schluessel);
		}
	}

	// =========================================================================
	// patchVorherigeSchuleAndSchulform
	// =========================================================================

	@Nested
	@DisplayName("patchVorherigeSchuleAndSchulform")
	class PatchVorherigeSchuleAndSchulform {

		@Test
		@DisplayName("idSchule null - setzt alle drei Felder auf null")
		void idSchuleNull_setztAlleNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "vorher";
			entity.LSSchulform = "vorher";
			entity.LSSchulformSIM = "vorher";

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, null, dataSchulen);

			assertThat(entity.LSSchulNr).isNull();
			assertThat(entity.LSSchulform).isNull();
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("Schule ohne Schulnummer - wirft BAD_REQUEST")
		void schuleOhneSchulnummer_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW(null, ID_SCHULFORM_GY_NRW);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("Schule mit ungültiger Schulnummer (nicht 1... oder 2...) - wirft BAD_REQUEST")
		void schuleUngueltigeSchulnummer_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("999999", ID_SCHULFORM_GY_NRW);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("Schule ohne Schulform - wirft BAD_REQUEST")
		void schuleOhneSchulform_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("100001", null);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}

		// ---------------------------------------------------------------------
		// NRW-Schule (Schulnummer beginnt mit "1")
		// ---------------------------------------------------------------------

		@Test
		@DisplayName("NRW-Schule, Schulform GY (kein BK/WB/SB) - LSSchulform=GY, LSSchulformSIM=GY")
		void nrwSchule_schulformGY_setztBeideFelder() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("100001", ID_SCHULFORM_GY_NRW);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen);

			assertThat(entity.LSSchulNr).isEqualTo("100001");
			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_GY);
			assertThat(entity.LSSchulformSIM).isEqualTo(KUERZEL_GY);
		}

		@Test
		@DisplayName("NRW-Schule, Schulform BK - LSSchulform=BK, LSSchulformSIM=null")
		void nrwSchule_schulformBK_setztSimNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("100001", ID_SCHULFORM_BK_NRW);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen);

			assertThat(entity.LSSchulNr).isEqualTo("100001");
			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_BK);
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("NRW-Schule, Schulform WB - LSSchulform=WB, LSSchulformSIM=null")
		void nrwSchule_schulformWB_setztSimNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("100001", ID_SCHULFORM_WB_NRW);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen);

			assertThat(entity.LSSchulNr).isEqualTo("100001");
			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_WB);
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("NRW-Schule, Schulform SB - LSSchulform=SB, LSSchulformSIM=null")
		void nrwSchule_schulformSB_setztSimNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("100001", ID_SCHULFORM_SB_NRW);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen);

			assertThat(entity.LSSchulNr).isEqualTo("100001");
			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_SB);
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("NRW-Schule, unbekannte Schulform-ID - wirft BAD_REQUEST")
		void nrwSchule_unbekannteSchulformId_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("100001", 99999L);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}

		// ---------------------------------------------------------------------
		// Sonstige Schule (Schulnummer beginnt mit "2")
		// ---------------------------------------------------------------------

		@Test
		@DisplayName("Sonstige Schule, Schulform GY (kein BK/WB/SB/SF) - LSSchulform=GY, LSSchulformSIM=GY")
		void sonstigeSchule_schulformGY_setztBeideFelder() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("200001", ID_HERKUNFT_SCHULFORM_GY);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen);

			assertThat(entity.LSSchulNr).isEqualTo("200001");
			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_GY);
			assertThat(entity.LSSchulformSIM).isEqualTo(KUERZEL_GY);
		}

		@Test
		@DisplayName("Sonstige Schule, Schulform BK - LSSchulform=BK, LSSchulformSIM=null")
		void sonstigeSchule_schulformBK_setztSimNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("200001", ID_HERKUNFT_SCHULFORM_BK);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen);

			assertThat(entity.LSSchulNr).isEqualTo("200001");
			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_BK);
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("Sonstige Schule, Schulform SF - LSSchulform=null, LSSchulformSIM=SF")
		void sonstigeSchule_schulformSF_setztSchulformNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("200001", ID_HERKUNFT_SCHULFORM_SF);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen);

			assertThat(entity.LSSchulNr).isEqualTo("200001");
			assertThat(entity.LSSchulform).isNull();
			assertThat(entity.LSSchulformSIM).isEqualTo(KUERZEL_SF);
		}

		@Test
		@DisplayName("Sonstige Schule, unbekannte Schulform-ID - wirft BAD_REQUEST")
		void sonstigeSchule_unbekannteSchulformId_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);
			final var schule = schuleNRW("200001", 99999L);
			when(dataSchulen.getById(42L)).thenReturn(schule);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, 42L, dataSchulen))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}
	}

	// =========================================================================
	// patchSchulformSonstigeVorherigeSchule
	// =========================================================================

	@Nested
	@DisplayName("patchSchulformSonstigeVorherigeSchule")
	class PatchSchulformSonstigeVorherigeSchule {

		@Test
		@DisplayName("idSchulform null - setzt LSSchulform=null und LSSchulformSIM=null")
		void idSchulformNull_setztBeideNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulform = "vorher";
			entity.LSSchulformSIM = "vorher";

			SchuelerSchulbesuchResolver.patchSchulformSonstigeVorherigeSchule(entity, null);

			assertThat(entity.LSSchulform).isNull();
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("unbekannte id - wirft BAD_REQUEST")
		void unbekannteId_wirftBadRequest() {
			final var entity = new DTOSchueler(1L, "123", true);

			assertThatException()
					.isThrownBy(() -> SchuelerSchulbesuchResolver.patchSchulformSonstigeVorherigeSchule(entity, 99999L))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Keine HerkunftSchulform mit der ID 99999 gefunden.")
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("Schulform GY (kein BK/WB/SB/SF) - LSSchulform=GY, LSSchulformSIM=GY")
		void schulformGY_setztBeideFelder() {
			final var entity = new DTOSchueler(1L, "123", true);

			SchuelerSchulbesuchResolver.patchSchulformSonstigeVorherigeSchule(entity, ID_HERKUNFT_SCHULFORM_GY);

			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_GY);
			assertThat(entity.LSSchulformSIM).isEqualTo(KUERZEL_GY);
		}

		@Test
		@DisplayName("Schulform BK - LSSchulform=BK, LSSchulformSIM=null")
		void schulformBK_setztSimNull() {
			final var entity = new DTOSchueler(1L, "123", true);

			SchuelerSchulbesuchResolver.patchSchulformSonstigeVorherigeSchule(entity, ID_HERKUNFT_SCHULFORM_BK);

			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_BK);
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("Schulform WB - LSSchulform=WB, LSSchulformSIM=null")
		void schulformWB_setztSimNull() {
			final var entity = new DTOSchueler(1L, "123", true);

			SchuelerSchulbesuchResolver.patchSchulformSonstigeVorherigeSchule(entity, ID_HERKUNFT_SCHULFORM_WB);

			assertThat(entity.LSSchulform).isEqualTo(KUERZEL_WB_HERKUNFT);
			assertThat(entity.LSSchulformSIM).isNull();
		}

		@Test
		@DisplayName("Schulform SF - LSSchulform=null, LSSchulformSIM=SF")
		void schulformSF_setztSchulformNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulform = "vorher";

			SchuelerSchulbesuchResolver.patchSchulformSonstigeVorherigeSchule(entity, ID_HERKUNFT_SCHULFORM_SF);

			assertThat(entity.LSSchulform).isNull();
			assertThat(entity.LSSchulformSIM).isEqualTo(KUERZEL_SF);
		}
	}

	// =========================================================================
	// mapSchulgliederung
	// =========================================================================

	@Nested
	@DisplayName("mapSchulgliederung")
	class MapSchulgliederung {

		@Test
		@DisplayName("LSSchulNr null - kein Mapping")
		void lsSchulNrNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = null;
			entity.LSSchulform = KUERZEL_BK;
			entity.LSSchulformSIM = "BK1";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idSchulgliederungVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapSchulgliederung(entity, target, 2024);

			assertThat(target.idSchulgliederungVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulform null - kein Mapping")
		void lsSchulformNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulform = null;
			entity.LSSchulformSIM = "BK1";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idSchulgliederungVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapSchulgliederung(entity, target, 2024);

			assertThat(target.idSchulgliederungVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulformSIM null - kein Mapping")
		void lsSchulformSIMNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulform = KUERZEL_BK;
			entity.LSSchulformSIM = null;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idSchulgliederungVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapSchulgliederung(entity, target, 2024);

			assertThat(target.idSchulgliederungVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulform GY (kein BK/WB/SB) - kein Mapping")
		void lsSchulformGY_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulform = KUERZEL_GY;
			entity.LSSchulformSIM = KUERZEL_GY;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idSchulgliederungVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapSchulgliederung(entity, target, 2024);

			assertThat(target.idSchulgliederungVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulform BK, LSSchulformSIM unbekannter Schluessel - setzt null")
		void lsSchulformBK_unbekannterSIM_setztNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulform = KUERZEL_BK;
			entity.LSSchulformSIM = "UNBEKANNT";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idSchulgliederungVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapSchulgliederung(entity, target, 2024);

			assertThat(target.idSchulgliederungVorherigeSchule).isNull();
		}

		@Test
		@DisplayName("LSSchulform BK, LSSchulformSIM gültiger HerkunftBildungsgang-Schluessel - setzt id")
		void lsSchulformBK_gueltigerSIM_setztId() {
			final var eintrag = HerkunftBildungsgang.data()
					.getWerte().getFirst().historie().getFirst();
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulform = KUERZEL_BK;
			entity.LSSchulformSIM = eintrag.schluessel;
			final var target = new SchuelerSchulbesuchsdaten();

			SchuelerSchulbesuchResolver.mapSchulgliederung(entity, target, (eintrag.gueltigVon != null) ? eintrag.gueltigVon : 2024);

			assertThat(target.idSchulgliederungVorherigeSchule).isEqualTo(eintrag.id);
		}

		@Test
		@DisplayName("schuljahr null - kein Mapping")
		void schuljahrNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulform = KUERZEL_BK;
			entity.LSSchulformSIM = "BK1";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idSchulgliederungVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapSchulgliederung(entity, target, null);

			assertThat(target.idSchulgliederungVorherigeSchule).isEqualTo(99L);
		}
	}

	// =========================================================================
	// mapHerkunftSonstige
	// =========================================================================

	@Nested
	@DisplayName("mapHerkunftSonstige")
	class MapHerkunftSonstige {

		@Test
		@DisplayName("LSSchulNr gesetzt - kein Mapping (kein Kein-Schulbesuch)")
		void lsSchulNrGesetzt_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulform = null;
			entity.LSSchulformSIM = "AS";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSonstigeVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSonstige(entity, target, 2024);

			assertThat(target.idHerkunftSonstigeVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulform gesetzt - kein Mapping (kein Kein-Schulbesuch)")
		void lsSchulformGesetzt_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = null;
			entity.LSSchulform = KUERZEL_GY;
			entity.LSSchulformSIM = "AS";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSonstigeVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSonstige(entity, target, 2024);

			assertThat(target.idHerkunftSonstigeVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulformSIM null - kein Mapping")
		void lsSchulformSIMNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = null;
			entity.LSSchulform = null;
			entity.LSSchulformSIM = null;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSonstigeVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSonstige(entity, target, 2024);

			assertThat(target.idHerkunftSonstigeVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("Kein Schulbesuch, unbekannter SIM-Schluessel - setzt null")
		void keinSchulbesuch_unbekannterSIM_setztNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = null;
			entity.LSSchulform = null;
			entity.LSSchulformSIM = "UNBEKANNT";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSonstigeVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSonstige(entity, target, 2024);

			assertThat(target.idHerkunftSonstigeVorherigeSchule).isNull();
		}

		@Test
		@DisplayName("Kein Schulbesuch, gültiger HerkunftSonstige-Schluessel - setzt id")
		void keinSchulbesuch_gueltigerSIM_setztId() {
			final var schuljahr = 2024;
			final var wert = HerkunftSonstige.data().getWerte().getFirst();
			final var eintrag = wert.historie().getFirst();
			final var erwartetId = wert.daten(schuljahr).id;

			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = null;
			entity.LSSchulform = null;
			entity.LSSchulformSIM = eintrag.kuerzel;
			final var target = new SchuelerSchulbesuchsdaten();

			SchuelerSchulbesuchResolver.mapHerkunftSonstige(entity, target, schuljahr);

			assertThat(target.idHerkunftSonstigeVorherigeSchule).isEqualTo(erwartetId);
		}

		@Test
		@DisplayName("schuljahr null - kein Mapping")
		void schuljahrNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = null;
			entity.LSSchulform = null;
			entity.LSSchulformSIM = "AS";
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSonstigeVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSonstige(entity, target, null);

			assertThat(target.idHerkunftSonstigeVorherigeSchule).isEqualTo(99L);
		}
	}

	// =========================================================================
	// mapHerkunftSchulform
	// =========================================================================

	@Nested
	@DisplayName("mapHerkunftSchulform")
	class MapHerkunftSchulform {

		@Test
		@DisplayName("LSSchulNr null - kein Mapping (keine sonstige Schule)")
		void lsSchulNrNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = null;
			entity.LSSchulformSIM = KUERZEL_GY;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSchulformVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSchulform(entity, target, 2024);

			assertThat(target.idHerkunftSchulformVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulNr beginnt mit '1' (NRW) - kein Mapping")
		void lsSchulNrNRW_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "100001";
			entity.LSSchulformSIM = KUERZEL_GY;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSchulformVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSchulform(entity, target, 2024);

			assertThat(target.idHerkunftSchulformVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulNr beginnt mit '2', LSSchulform und LSSchulformSIM null - kein Mapping")
		void sonstigeSchule_beideNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "200001";
			entity.LSSchulform = null;
			entity.LSSchulformSIM = null;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSchulformVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSchulform(entity, target, 2024);

			assertThat(target.idHerkunftSchulformVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("LSSchulNr beginnt mit '2', unbekannter LSSchulform-Schluessel - setzt null")
		void sonstigeSchule_unbekannterSchulform_setztNull() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "200001";
			entity.LSSchulform = "UNBEKANNT";
			entity.LSSchulformSIM = null;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSchulformVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSchulform(entity, target, 2024);

			assertThat(target.idHerkunftSchulformVorherigeSchule).isNull();
		}

		@Test
		@DisplayName("LSSchulNr beginnt mit '2', gültiger LSSchulform-Schluessel - setzt id")
		void sonstigeSchule_gueltigerSchulform_setztId() {
			final var schuljahr = 2024;
			final var wert = HerkunftSchulform.data().getWerte().getFirst();
			final var eintrag = wert.historie().getFirst();
			final var erwartetId = wert.daten(schuljahr).id;

			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "200001";
			entity.LSSchulform = eintrag.kuerzel;
			entity.LSSchulformSIM = null;
			final var target = new SchuelerSchulbesuchsdaten();

			SchuelerSchulbesuchResolver.mapHerkunftSchulform(entity, target, schuljahr);

			assertThat(target.idHerkunftSchulformVorherigeSchule).isEqualTo(erwartetId);
		}

		@Test
		@DisplayName("Sonderfall SF: LSSchulform null, LSSchulformSIM=SF - setzt id via Fallback auf LSSchulformSIM")
		void sonstigeSchule_schulformSF_fallbackAufLsSchulformSIM_setztId() {
			final var schuljahr = 2024;
			final var wert = HerkunftSchulform.data().getWertByKuerzel(KUERZEL_SF);
			final var erwartetId = wert.daten(schuljahr).id;

			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "200001";
			entity.LSSchulform = null;
			entity.LSSchulformSIM = KUERZEL_SF;
			final var target = new SchuelerSchulbesuchsdaten();

			SchuelerSchulbesuchResolver.mapHerkunftSchulform(entity, target, schuljahr);

			assertThat(target.idHerkunftSchulformVorherigeSchule).isEqualTo(erwartetId);
		}

		@Test
		@DisplayName("schuljahr null - kein Mapping")
		void schuljahrNull_keinMapping() {
			final var entity = new DTOSchueler(1L, "123", true);
			entity.LSSchulNr = "200001";
			entity.LSSchulform = KUERZEL_GY;
			entity.LSSchulformSIM = null;
			final var target = new SchuelerSchulbesuchsdaten();
			target.idHerkunftSchulformVorherigeSchule = 99L;

			SchuelerSchulbesuchResolver.mapHerkunftSchulform(entity, target, null);

			assertThat(target.idHerkunftSchulformVorherigeSchule).isEqualTo(99L);
		}
	}

	// =========================================================================
	// Hilfsmethoden
	// =========================================================================

	/**
	 * Erstellt einen {@link SchulEintrag} mit der angegebenen internen Schulnummer und Schulform-ID.
	 *
	 * @param schulnummerIntern die interne Schulnummer (z. B. "100001" für NRW, "200001" für sonstige)
	 * @param idSchulform       die ID der Schulform aus dem jeweiligen CoreType
	 * @return befüllter {@link SchulEintrag}
	 */
	private static SchulEintrag schuleNRW(final String schulnummerIntern, final Long idSchulform) {
		final var schule = new SchulEintrag();
		schule.id = 42L;
		schule.schulnummerIntern = schulnummerIntern;
		schule.schulnummerStatistik = schulnummerIntern;
		schule.idSchulform = idSchulform;
		return schule;
	}
}
