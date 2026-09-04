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
 * Die interne Schulnummer beginnt mit {@code "1"}. Die Schulform wird beim Setzen der Schule
 * automatisch aus {@code Schulform.json} vorausgefüllt und kann danach nicht mehr über diesen
 * Resolver geändert werden.
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
 *       <li>{@code LSSchulformSIM} = {@code null} (wird in einem separaten Patch über
 *           {@link #patchHerkunftbildungsgang} mit dem Schlüssel aus
 *           {@code HerkunftBildungsgang.json} befüllt)</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h2>2. Sonstige Schule</h2>
 * Die interne Schulnummer beginnt mit {@code "2"} (Statistik-Schulnummer beginnt mit {@code "9"}).
 * Beim Setzen der Schule über {@link #patchVorherigeSchuleAndSchulform} werden {@code LSSchulform}
 * und {@code LSSchulformSIM} auf {@code null} zurückgesetzt. Die Schulform wird anschließend
 * in einem separaten Patch über {@link #patchSchulformSonstigeVorherigeSchule} gesetzt und ist
 * damit nachträglich änderbar.
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
 *       <li>{@code LSSchulformSIM} = {@code null} (wird in einem separaten Patch über
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
 *   <li>Sonstige Schule: Wenn {@code LSSchulNr} mit {@code "2"} beginnt und
 *       {@code LSSchulformSIM} gesetzt ist, wird {@code LSSchulformSIM} als Kürzel
 *       aus {@code HerkunftSchulform.json} interpretiert.</li>
 *   <li>Kein Schulbesuch: Wenn {@code LSSchulNr} und {@code LSSchulform} {@code null} sind
 *       und {@code LSSchulformSIM} gesetzt ist, wird {@code LSSchulformSIM} als Kürzel
 *       aus {@code HerkunftSonstige.json} interpretiert.</li>
 * </ul>
 */
public final class SchuelerSchulbesuchResolver {

	private SchuelerSchulbesuchResolver() {
		/* This utility class should not be instantiated */
	}

	/**
	 * Setzt den Herkunftstyp "Kein Schulbesuch" für die vorherige Schule.
	 * Bei {@code id == null} werden {@code LSSchulform} und {@code LSSchulformSIM} auf {@code null} gesetzt.
	 *
	 * @param entity die Schüler-Entity, deren Felder gesetzt werden
	 * @param id     die ID des CoreType-Eintrags aus {@code HerkunftSonstige.json}; {@code null} löscht den Eintrag
	 * @throws ApiOperationException wenn kein Eintrag mit der angegebenen ID gefunden wurde
	 */
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

	/**
	 * Setzt die Schulgliederung (Herkunftbildungsgang) für die vorherige Schule.
	 * Wird nur für Schulformen BK, WB und SB verwendet, bei denen {@code LSSchulformSIM}
	 * nicht aus der Schulform selbst abgeleitet wird.
	 * Bei {@code id == null} wird {@code LSSchulformSIM} auf {@code null} gesetzt.
	 *
	 * @param entity die Schüler-Entity, deren Feld {@code LSSchulformSIM} gesetzt wird
	 * @param id     die ID des CoreType-Eintrags aus {@code HerkunftBildungsgang.json}; {@code null} löscht den Eintrag
	 * @throws ApiOperationException wenn kein Eintrag mit der angegebenen ID gefunden wurde
	 */
	static void patchHerkunftbildungsgang(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.LSSchulformSIM = null;
			return;
		}
		final var herkunftBildungsgang = Optional.ofNullable(HerkunftBildungsgang.data().getEintragByID(id))
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, "Kein HerkunftBildungsgang mit der ID %d gefunden.".formatted(id)));
		entity.LSSchulformSIM = herkunftBildungsgang.schluessel;
	}

	/**
	 * Setzt die vorherige Schule und leitet die Schulform aus der gewählten Schule ab.
	 * Bei {@code idSchule == null} werden alle drei Felder ({@code LSSchulNr}, {@code LSSchulform},
	 * {@code LSSchulformSIM}) auf {@code null} gesetzt.
	 *
	 * <p>Für NRW-Schulen (interne Schulnummer beginnt mit {@code "1"}) wird {@code LSSchulform}
	 * aus {@code Schulform.json} gesetzt. Für sonstige Schulen (interne Schulnummer beginnt mit
	 * {@code "2"}) werden {@code LSSchulform} und {@code LSSchulformSIM} auf {@code null} gesetzt;
	 * die Schulform wird in einem separaten Patch über {@link #patchSchulformSonstigeVorherigeSchule}
	 * nachgepflegt.
	 *
	 * <p>Ein Schulwechsel setzt eine ggf. vorhandene Schulgliederung ({@code LSSchulformSIM}) zurück.
	 * Sie muss danach über {@link #patchHerkunftbildungsgang} neu gesetzt werden.
	 *
	 * @param entity      die Schüler-Entity, deren Felder gesetzt werden
	 * @param idSchule    die ID der Schule aus {@code K_Schule}; {@code null} löscht die Zuordnung
	 * @param dataSchulen Datenzugriff auf den Schulkatalog
	 * @throws ApiOperationException wenn die Schule keine gültige Schulnummer oder Schulform hat
	 */
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

	/**
	 * Setzt die Schulform für eine sonstige vorherige Schule anhand der ID aus {@code HerkunftSchulform.json}.
	 * Diese Methode wird in einem separaten Patch nach {@link #patchVorherigeSchuleAndSchulform} aufgerufen.
	 * Bei {@code idSchulform == null} werden {@code LSSchulform} und {@code LSSchulformSIM} auf {@code null} gesetzt.
	 *
	 * <p>Sonderfall SF (Sonstige Schulform):
	 * {@code LSSchulform} wird auf {@code null} gesetzt, {@code LSSchulformSIM} erhält das Kürzel SF.
	 *
	 * <p>Für BK, WB und SB wird {@code LSSchulformSIM} auf {@code null} gesetzt; die Schulgliederung
	 * wird in einem weiteren Patch über {@link #patchHerkunftbildungsgang} gesetzt.
	 *
	 * @param entity      die Schüler-Entity, deren Felder {@code LSSchulform} und {@code LSSchulformSIM} gesetzt werden
	 * @param idSchulform die ID des CoreType-Eintrags aus {@code HerkunftSchulform.json}; {@code null} löscht den Eintrag
	 * @throws ApiOperationException wenn kein Eintrag mit der angegebenen ID gefunden wurde
	 */
	static void patchSchulformSonstigeVorherigeSchule(final DTOSchueler entity, final Long idSchulform) {
		if (idSchulform == null) {
			entity.LSSchulform = null;
			entity.LSSchulformSIM = null;
			return;
		}
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

	private static void validateSchule(final SchulEintrag schule) {
		if (schule.schulnummerIntern == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Schule %d ist keine Schulnummer zugeordnet".formatted(schule.id));
		}
		if (!isOeffentlicheOderErsatzschuleInNRW(schule.schulnummerIntern) && !isSonstigeSchule(schule.schulnummerIntern)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Schulnummer %s ist ungültig".formatted(schule.schulnummerStatistik));
		}
	}

	private static void patchSchulform(final DTOSchueler entity, final SchulEintrag schule) {
		if (isOeffentlicheOderErsatzschuleInNRW(schule.schulnummerIntern)) {
			patchSchulformNRW(entity, schule.idSchulform);
		} else if (isSonstigeSchule(schule.schulnummerIntern)) {
			patchSchulformSonstigeVorherigeSchule(entity, schule.idSchulform);
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

	private static boolean isOeffentlicheOderErsatzschuleInNRW(final String schulnummer) {
		if (schulnummer == null) {
			return false;
		}
		return schulnummer.startsWith("1");
	}

	private static boolean isSonstigeSchule(final String schulnummerIntern) {
		if (schulnummerIntern == null) {
			return false;
		}
		// Interne Schulnummern für sonstige Schulen beginnen mit "2" (generiert aus ID + 200000 in DataSchulen).
		// Die Statistik-Schulnummer beginnt dagegen mit "9" — diese Methode arbeitet ausschließlich auf der internen Nummer.
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
	 * Mappt die Schulgliederung der vorherigen Schule auf die ID des CoreType-Eintrags aus {@code HerkunftBildungsgang.json}.
	 *
	 * <p>Die Abbildung erfolgt nur, wenn die vorherige Schule eine BK-, SB- oder WB-Schulform hat
	 * und {@code LSSchulformSIM} gesetzt ist. Ist kein passender CoreType-Eintrag für das angegebene
	 * Schuljahr vorhanden, wird {@code idSchulgliederungVorherigeSchule} auf {@code null} gesetzt.
	 *
	 * @param entity    die Schüler-Entity mit den Quellfeldern {@code LSSchulNr}, {@code LSSchulform} und {@code LSSchulformSIM}
	 * @param target    das Zielobjekt, in dem {@code idSchulgliederungVorherigeSchule} gesetzt wird
	 * @param schuljahr das Schuljahr, für das der CoreType-Eintrag aufgelöst wird;
	 *                  ist {@code null}, wird kein Mapping vorgenommen
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
	 * Mappt den Herkunftstyp "Kein Schulbesuch" auf die ID des CoreType-Eintrags aus {@code HerkunftSonstige.json}.
	 *
	 * <p>Die Abbildung erfolgt nur, wenn keine Schulnummer ({@code LSSchulNr}) und keine Schulform
	 * ({@code LSSchulform}) gesetzt sind, aber {@code LSSchulformSIM} einen Wert enthält. Ist kein
	 * passender CoreType-Eintrag für das angegebene Schuljahr vorhanden, wird
	 * {@code idHerkunftSonstigeVorherigeSchule} auf {@code null} gesetzt.
	 *
	 * @param entity    die Schüler-Entity mit den Quellfeldern {@code LSSchulNr}, {@code LSSchulform} und {@code LSSchulformSIM}
	 * @param target    das Zielobjekt, in dem {@code idHerkunftSonstigeVorherigeSchule} gesetzt wird
	 * @param schuljahr das Schuljahr, für das der CoreType-Eintrag aufgelöst wird;
	 *                  ist {@code null}, wird kein Mapping vorgenommen
	 */
	public static void mapHerkunftSonstige(final DTOSchueler entity, final SchuelerSchulbesuchsdaten target, final Integer schuljahr) {
		if (keinSchulbesuch(entity) && (schuljahr != null)) {
			target.idHerkunftSonstigeVorherigeSchule = Optional.ofNullable(HerkunftSonstige.data().getWertByKuerzel(entity.LSSchulformSIM))
					.map(h -> h.daten(schuljahr))
					.map(h -> h.id)
					.orElse(null);
		}
	}

	/**
	 * Mappt die Schulform einer sonstigen vorherigen Schule auf die ID des CoreType-Eintrags aus {@code HerkunftSchulform.json}.
	 *
	 * <p>Die Abbildung erfolgt nur, wenn die interne Schulnummer ({@code LSSchulNr}) mit {@code "2"} beginnt
	 * und {@code LSSchulformSIM} gesetzt ist. Ist kein passender CoreType-Eintrag für das angegebene
	 * Schuljahr vorhanden, wird {@code idHerkunftSchulformVorherigeSchule} auf {@code null} gesetzt.
	 *
	 * @param entity    die Schüler-Entity mit den Quellfeldern {@code LSSchulNr}, {@code LSSchulform} und {@code LSSchulformSIM}
	 * @param target    das Zielobjekt, in dem {@code idHerkunftSchulformVorherigeSchule} gesetzt wird
	 * @param schuljahr das Schuljahr, für das der CoreType-Eintrag aufgelöst wird;
	 *                  ist {@code null}, wird kein Mapping vorgenommen
	 */
	public static void mapHerkunftSchulform(final DTOSchueler entity, final SchuelerSchulbesuchsdaten target, final Integer schuljahr) {
		// falls schulform == "SF" -> dann wird die Schulform in LSSchulformSIM gespeichert und nicht in LSSchulform
		final var schulform = (entity.LSSchulform == null) ? entity.LSSchulformSIM : entity.LSSchulform;
		if (isSonstigeSchule(entity.LSSchulNr) && (schuljahr != null) && (schulform != null)) {
			target.idHerkunftSchulformVorherigeSchule = Optional.ofNullable(HerkunftSchulform.data().getWertByKuerzel(schulform))
					.map(h -> h.daten(schuljahr))
					.map(h -> h.id)
					.orElse(null);
		}
	}

	private static boolean keinSchulbesuch(final DTOSchueler entity) {
		return (entity.LSSchulNr == null) && (entity.LSSchulform == null) && (entity.LSSchulformSIM != null);
	}

}
