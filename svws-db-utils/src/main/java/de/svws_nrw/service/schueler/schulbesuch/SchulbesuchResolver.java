package de.svws_nrw.service.schueler.schulbesuch;

import java.util.Optional;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgang;
import de.svws_nrw.asd.types.schueler.HerkunftSchulform;
import de.svws_nrw.asd.types.schueler.HerkunftSonstige;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * Hilfsmethoden zur Auflösung und Persistierung der Felder {@code LSSchulNr}, {@code LSSchulform}
 * und {@code LSSchulformSIM} in {@link DTOSchueler} anhand der gewählten Toggle-Option
 * "Vorherige Schule" im Tab Schüler → Schulbesuch.
 *
 * <p>Es gibt drei Toggle-Optionen:
 *
 * <h2>1. Öffentliche oder Ersatzschule in NRW</h2>
 * Die Schulnummer beginnt intern mit "1". Die Schulform wird aus {@code Schulform.json} ermittelt.
 *
 * <ul>
 *   <li>Schulform != BK, WB, SB:
 *     <ul>
 *       <li>{@code LSSchulNr} = interne Schulnummer aus {@code K_Schule.SchulNr}</li>
 *       <li>{@code LSSchulform} = Kürzel aus CoreType {@code Schulform.json}</li>
 *       <li>{@code LSSchulformSIM} = Kürzel aus CoreType {@code Schulform.json}</li>
 *     </ul>
 *   </li>
 *   <li>Schulform == BK, WB oder SB:
 *     <ul>
 *       <li>{@code LSSchulNr} = interne Schulnummer aus {@code K_Schule.SchulNr}</li>
 *       <li>{@code LSSchulform} = Kürzel aus CoreType {@code Schulform.json}</li>
 *       <li>{@code LSSchulformSIM} = {@code null} (wird ggf. durch
 *           {@link #patchHerkunftbildungsgang} mit dem Schlüssel aus
 *           {@code HerkunftBildungsgang.json} befüllt)</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h2>2. Sonstige Schule</h2>
 * Die Schulnummer beginnt intern mit "2". Die Schulform wird aus {@code HerkunftSchulform.json} ermittelt.
 *
 * <ul>
 *   <li>Schulform != BK, WB, SB und != SF:
 *     <ul>
 *       <li>{@code LSSchulNr} = interne Schulnummer aus {@code K_Schule.SchulNr}</li>
 *       <li>{@code LSSchulform} = Kürzel aus CoreType {@code HerkunftSchulform.json}</li>
 *       <li>{@code LSSchulformSIM} = Kürzel aus CoreType {@code HerkunftSchulform.json}</li>
 *     </ul>
 *   </li>
 *   <li>Schulform == BK, WB oder SB:
 *     <ul>
 *       <li>{@code LSSchulNr} = interne Schulnummer aus {@code K_Schule.SchulNr}</li>
 *       <li>{@code LSSchulform} = Kürzel aus CoreType {@code HerkunftSchulform.json}</li>
 *       <li>{@code LSSchulformSIM} = {@code null} (wird ggf. durch
 *           {@link #patchHerkunftbildungsgang} mit dem Schlüssel aus
 *           {@code HerkunftBildungsgang.json} befüllt)</li>
 *     </ul>
 *   </li>
 *   <li>Schulform == SF (Sonstige Schulform):
 *     <ul>
 *       <li>{@code LSSchulNr} = interne Schulnummer aus {@code K_Schule.SchulNr}</li>
 *       <li>{@code LSSchulform} = {@code null}</li>
 *       <li>{@code LSSchulformSIM} = Kürzel aus CoreType {@code HerkunftSchulform.json}</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h2>3. Kein Schulbesuch</h2>
 * Es ist keine Schule zugeordnet.
 *
 * <ul>
 *   <li>{@code LSSchulNr} = {@code null}</li>
 *   <li>{@code LSSchulform} = {@code null}</li>
 *   <li>{@code LSSchulformSIM} = Kürzel aus CoreType {@code HerkunftSonstige.json}</li>
 * </ul>
 *
 * <h2>Rückmapping (toApi)</h2>
 * Beim Lesen der Entity werden die gespeicherten Kürzel zurück auf CoreType-IDs gemappt:
 * <ul>
 *   <li>Schulgliederung: Wenn {@code LSSchulform} BK, WB oder SB ist und
 *       {@code LSSchulformSIM} gesetzt ist, wird {@code LSSchulformSIM} als Schlüssel
 *       aus {@code HerkunftBildungsgang.json} interpretiert.</li>
 *   <li>Kein Schulbesuch: Wenn {@code LSSchulNr} und {@code LSSchulform} {@code null} sind
 *       und {@code LSSchulformSIM} gesetzt ist, wird {@code LSSchulformSIM} als Kürzel
 *       aus {@code HerkunftSonstige.json} interpretiert.</li>
 * </ul>
 */
public final class SchulbesuchResolver {


	private SchulbesuchResolver() {
		/* This utility class should not be instantiated */
	}

	static void patchHerkunftSonstigeVorherigeSchule(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.LSSchulform = null;
			entity.LSSchulformSIM = null;
			return;
		}
		final var herkunftSonstige = Optional.ofNullable(HerkunftSonstige.data().getEintragByID(id))
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, "Keine HerkunftSonstige mit der ID %d gefunden.".formatted(id)));
		entity.LSSchulform = null;
		entity.LSSchulformSIM = herkunftSonstige.kuerzel;
	}

	static void patchHerkunftbildungsgang(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.LSSchulformSIM = null;
			return;
		}
		final var herkunftBildungsgang = Optional.ofNullable(HerkunftBildungsgang.data().getEintragByID(id))
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, "Kein HerkunftBildungsgang mit der ID %d gefunden.".formatted(id)));
		entity.LSSchulformSIM = herkunftBildungsgang.schluessel;
	}

	static void patchVorherigeSchuleAndSchulform(final DTOSchueler entity, final Long idSchule, final DataSchulen dataSchulen) {
		if (idSchule == null) {
			entity.LSSchulNr = null;
			entity.LSSchulform = null;
			entity.LSSchulformSIM = null;
			return;
		}
		final var schule = dataSchulen.getById(idSchule);
		validateSchule(schule);
		entity.LSSchulNr = schule.schulnummerIntern;
		entity.LSSchulform = null;
		entity.LSSchulformSIM = null;
		patchSchulform(entity, schule);
	}

	private static void validateSchule(final SchulEintrag schule) {
		if (schule.schulnummerIntern == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Schule %d ist keine Schulnummer zugeordnet".formatted(schule.id));
		}
		if (!isOeffentlicheOderErsatzschuleInNRW(schule.schulnummerIntern) && !isSonstigeSchule(schule.schulnummerIntern)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Schulnummer %s ist ungültig".formatted(schule.schulnummerStatistik));
		}
		if (schule.idSchulform == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Schule %d ist keine Schulform zugeordnet".formatted(schule.id));
		}
	}

	private static void patchSchulform(final DTOSchueler entity, final SchulEintrag schule) {
		if (isOeffentlicheOderErsatzschuleInNRW(schule.schulnummerIntern)) {
			patchSchulformNRW(entity, schule.idSchulform);
		} else if (isSonstigeSchule(schule.schulnummerIntern)) {
			patchSchulformSonstige(entity, schule.idSchulform);
		}
	}

	private static void patchSchulformNRW(final DTOSchueler entity, final Long idSchulform) {
		final String kuerzel = Optional.ofNullable(Schulform.data().getEintragByID(idSchulform))
				.map(s -> s.kuerzel)
				.filter(StringUtils::isNotBlank)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, "Keine Schulform mit der ID %d gefunden.".formatted(idSchulform)));

		entity.LSSchulform = kuerzel;
		entity.LSSchulformSIM = isBKorWBorSB(kuerzel) ? null : kuerzel;
	}

	private static void patchSchulformSonstige(final DTOSchueler entity, final Long idSchulform) {
		final String kuerzel = Optional.ofNullable(HerkunftSchulform.data().getEintragByID(idSchulform))
				.map(s -> s.kuerzel)
				.filter(StringUtils::isNotBlank)
				.orElseThrow(
						() -> new ApiOperationException(Response.Status.BAD_REQUEST, "Keine HerkunftSchulform mit der ID %d gefunden.".formatted(idSchulform)));

		if (isSonstigeSchulform(kuerzel)) {
			entity.LSSchulform = null;
			entity.LSSchulformSIM = kuerzel;
			return;
		}
		entity.LSSchulform = kuerzel;
		entity.LSSchulformSIM = isBKorWBorSB(kuerzel) ? null : kuerzel;
	}

	private static boolean isOeffentlicheOderErsatzschuleInNRW(final String schulnummer) {
		return schulnummer.startsWith("1");
	}

	private static boolean isSonstigeSchule(final String schulnummerIntern) {
		// Interne Schulnummern für sonstige Schulen werden in DataSchulen:updateSchulnummer() generiert
		return schulnummerIntern.startsWith("2");
	}

	private static boolean isSonstigeSchulform(final String kuerzelSchulform) {
		return ("SF").equals(kuerzelSchulform);
	}

	private static boolean isBKorWBorSB(final String kuerzelSchulform) {
		return ("BK").equals(kuerzelSchulform) || ("SB").equals(kuerzelSchulform) || ("WB").equals(kuerzelSchulform);
	}


	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	/**
	 * Falls es sich bei der Auswahl der vorherigen Schule um BK, SB oder WB handelt und eine Schulgliederung hinterlegt ist,
	 * wird die id des CoreTypes HerkunftBildungsgang.json gemapped
	 *
	 * <p>Die Abbildung erfolgt nur, wenn die vorherige Schule eine BK-, SB- oder
	 * WB-Schulform hat und {@code LSSchulformSIM} gesetzt ist. Ist kein passender
	 * CoreType-Eintrag für das angegebene Schuljahr vorhanden, wird
	 * {@code idSchulgliederungVorherigeSchule} auf {@code null} gesetzt.
	 *
	 * @param entity    die Schüler-Entity mit den Quellfeldern {@code LSSchulNr},
	 *                  {@code LSSchulform} und {@code LSSchulformSIM}
	 * @param target    das Zielobjekt, in dem {@code idSchulgliederungVorherigeSchule}
	 *                  gesetzt wird
	 * @param schuljahr das Schuljahr, für das der CoreType-Eintrag aufgelöst wird
	 */
	public static void mapSchulgliederung(final DTOSchueler entity, final SchuelerSchulbesuchsdaten target, final Integer schuljahr) {
		if (schulgliederungGesetzt(entity) && (schuljahr != null)) {
			target.idSchulgliederungVorherigeSchule = Optional.ofNullable(HerkunftBildungsgang.data().getWertByKuerzel(entity.LSSchulformSIM))
					.map(h -> h.daten(schuljahr))
					.map(h -> h.id)
					.orElse(null);
		}
	}

	private static boolean schulgliederungGesetzt(final DTOSchueler entity) {
		if ((entity.LSSchulNr == null) || (entity.LSSchulform == null) || (entity.LSSchulformSIM == null)) {
			return false;
		}
		return isBKorWBorSB(entity.LSSchulform);
	}

	/**
	 * Falls es sich bei der Auswahl der vorherigen Schule um "Kein Schulbesuch" handelt (Keine Schulnummer in der entity hinterlegt)
	 * wird die id des CoreTypes HerkunftSonstige.json gemapped
	 *
	 * <p>Die Abbildung erfolgt nur, wenn keine Schulnummer ({@code LSSchulNr}) und
	 * keine Schulform ({@code LSSchulform}) gesetzt sind, aber {@code LSSchulformSIM}
	 * einen Wert enthält. Ist kein passender CoreType-Eintrag für das angegebene
	 * Schuljahr vorhanden, wird {@code idHerkunftSonstigeVorherigeSchule} auf
	 * {@code null} gesetzt.
	 *
	 * @param entity    die Schüler-Entity mit den Quellfeldern {@code LSSchulNr},
	 *                  {@code LSSchulform} und {@code LSSchulformSIM}
	 * @param target    das Zielobjekt, in dem {@code idHerkunftSonstigeVorherigeSchule}
	 *                  gesetzt wird
	 * @param schuljahr das Schuljahr, für das der CoreType-Eintrag aufgelöst wird
	 */
	public static void mapHerkunftSonstige(final DTOSchueler entity, final SchuelerSchulbesuchsdaten target, final Integer schuljahr) {
		if (keinSchulbesuch(entity) && (schuljahr != null)) {
			target.idHerkunftSonstigeVorherigeSchule = Optional.ofNullable(HerkunftSonstige.data().getWertByKuerzel(entity.LSSchulformSIM))
					.map(h -> h.daten(schuljahr))
					.map(h -> h.id)
					.orElse(null);
		}
	}

	private static boolean keinSchulbesuch(final DTOSchueler entity) {
		return (entity.LSSchulNr == null) && (entity.LSSchulform == null) && (entity.LSSchulformSIM != null);
	}

}
