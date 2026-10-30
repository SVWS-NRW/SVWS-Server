package de.svws_nrw.service.schueler.create;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungService;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittCreateRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittService;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenService;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Service zum Neuanlegen eines Schülers. Der Service orchestriert die Services für die Stammdaten, den Lernabschnitt,
 * den Schulbesuch, die Einwilligungen und die Lernplattformen.
 */
public final class SchuelerCreateService {

	private final SchuelerStammdatenService schuelerStammdatenService;
	private final SchuelerLernabschnittService schuelerLernabschnittService;
	private final SchuelerSchulbesuchService schuelerSchulbesuchService;
	private final SchuelerLernplattformService schuelerLernplattformService;
	private final SchuelerEinwilligungService schuelerEinwilligungService;

	/**
	 * Erstellt einen neuen {@code SchuelerCreateService}.
	 *
	 * @param schuelerStammdatenService       der Service für die Stammdaten eines Schülers
	 * @param schuelerLernabschnittService    der Service für die Lernabschnitte eines Schülers
	 * @param schuelerSchulbesuchService      der Service für den Schulbesuch eines Schülers
	 * @param schuelerLernplattformService    der Service für die Lernplattformen eines Schülers
	 * @param schuelerEinwilligungService     der Service für die Einwilligungen eines Schülers
	 */
	public SchuelerCreateService(
			final SchuelerStammdatenService schuelerStammdatenService,
			final SchuelerLernabschnittService schuelerLernabschnittService,
			final SchuelerSchulbesuchService schuelerSchulbesuchService,
			final SchuelerLernplattformService schuelerLernplattformService,
			final SchuelerEinwilligungService schuelerEinwilligungService) {
		this.schuelerStammdatenService = schuelerStammdatenService;
		this.schuelerLernabschnittService = schuelerLernabschnittService;
		this.schuelerSchulbesuchService = schuelerSchulbesuchService;
		this.schuelerLernplattformService = schuelerLernplattformService;
		this.schuelerEinwilligungService = schuelerEinwilligungService;
	}

	/**
	 * Legt einen neuen Schüler an, zusammen mit dem Lernabschnitt, den Einwilligungen und den Lernplattformen für diesen Schüler.
	 * Ist eine Einschulungsart für die Grundschule angegeben, so wird diese im Schulbesuch des Schülers gesetzt.
	 * Alle Schritte laufen in einer Transaktion. Tritt in einem Schritt ein Fehler auf, wird der Schüler nicht angelegt.
	 *
	 * @param dto   der {@link SchuelerCreateRequest} mit den Daten des neuen Schülers
	 *
	 * @return die {@link SchuelerStammdaten} des neu angelegten Schülers
	 *
	 * @throws ApiOperationException   wenn die Daten ungültig sind oder das Anlegen in einem der Schritte fehlschlägt
	 */
	public SchuelerStammdaten create(final SchuelerCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final SchuelerStammdaten created = schuelerStammdatenService.create(dto);
			createLernabschnitt(dto, created.id);
			patchSchulbesuch(dto, created.id);
			schuelerEinwilligungService.addForSchueler(created.id);
			schuelerLernplattformService.addForSchueler(created.id);
			return created;
		});
	}

	private void createLernabschnitt(final SchuelerCreateRequest dto, final long idSchueler) {
		final var lernabschnitt = new SchuelerLernabschnittCreateRequest();
		lernabschnitt.schuelerID = idSchueler;
		lernabschnitt.schuljahresabschnitt = dto.idSchuljahresabschnitt;
		if (dto.idKlasse != null) {
			lernabschnitt.klassenID = JsonNullable.of(dto.idKlasse);
		}
		if (dto.idJahrgang != null) {
			lernabschnitt.jahrgangID = JsonNullable.of(dto.idJahrgang);
		}
		schuelerLernabschnittService.create(lernabschnitt);
	}

	private void patchSchulbesuch(final SchuelerCreateRequest dto, final long idSchueler) {
		if (dto.idGrundschuleEinschulungsart == null) {
			return;
		}
		final var patch = new SchuelerSchulbesuchPatchRequest();
		patch.idEinschulungsartGrundschule = JsonNullable.of(dto.idGrundschuleEinschulungsart);
		schuelerSchulbesuchService.patch(idSchueler, patch);
	}

}
