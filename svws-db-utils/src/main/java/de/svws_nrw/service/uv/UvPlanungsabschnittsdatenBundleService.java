package de.svws_nrw.service.uv;

import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerService;
import de.svws_nrw.service.uv.kurse.UvKursService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenLehrerService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenSchieneService;
import de.svws_nrw.service.uv.klassen.UvKlasseService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeService;
import de.svws_nrw.service.uv.lehrer.UvPlanungsabschnittLehrerService;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerService;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittService;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeSchuelerService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeService;
import de.svws_nrw.service.uv.lerngruppen.UvUnterrichtLerngruppenlehrerService;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtRaumService;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtService;
import de.svws_nrw.service.uv.schienen.UvSchieneService;

/**
 * Ein Service zum Bündeln der Daten eines UV-Planungsabschnitts.
 */
public final class UvPlanungsabschnittsdatenBundleService {

	private final UvPlanungsabschnittService uvPlanungsabschnittService;
	private final UvPlanungsabschnittLehrerService uvPlanungsabschnittLehrerService;
	private final UvPlanungsabschnittSchuelerService uvPlanungsabschnittSchuelerService;
	private final UvPlanungsabschnittZeitrasterService uvPlanungsabschnittZeitrasterService;
	private final UvSchieneService uvSchieneService;
	private final UvKlasseService uvKlasseService;
	private final UvKlassenLehrerService uvKlassenLehrerService;
	private final UvKursService uvKursService;
	private final UvSchuelergruppeService uvSchuelergruppeService;
	private final UvLerngruppenLehrerService uvLerngruppenLehrerService;
	private final UvLerngruppenSchieneService uvLerngruppenSchieneService;
	private final UvLerngruppeService uvLerngruppeService;
	private final UvSchuelergruppeSchuelerService uvSchuelergruppeSchuelerService;
	private final UvUnterrichtService uvUnterrichtService;
	private final UvUnterrichtRaumService uvUnterrichtRaumService;
	private final UvUnterrichtLerngruppenlehrerService uvUnterrichtLerngruppenlehrerService;

	/**
	 * Erstellt einen neuen Service zum Bündeln der UV-Planungsabschnittsdaten.
	 *
	 * @param uvPlanungsabschnittService der Service für UV-Planungsabschnitte
	 * @param uvPlanungsabschnittLehrerService der Service für UV-Planungsabschnitt-Lehrer-Zuordnungen
	 * @param uvPlanungsabschnittSchuelerService der Service für UV-Planungsabschnitt-Schüler-Zuordnungen
	 * @param uvPlanungsabschnittZeitrasterService der Service für UV-Planungsabschnitt-Zeitraster-Zuordnungen
	 * @param uvSchieneService der Service für UV-Schienen
	 * @param uvKlasseService der Service für UV-Klassen
	 * @param uvKlassenLehrerService der Service für UV-Klassen-Lehrer-Zuordnungen
	 * @param uvKursService der Service für UV-Kurse
	 * @param uvSchuelergruppeService der Service für UV-Schülergruppen
	 * @param uvLerngruppenLehrerService der Service für UV-Lerngruppen-Lehrer-Zuordnungen
	 * @param uvLerngruppenSchieneService der Service für UV-Lerngruppen-Schienen-Zuordnungen
	 * @param uvLerngruppeService der Service für UV-Lerngruppen
	 * @param uvSchuelergruppeSchuelerService der Service für UV-Schülergruppe-Schüler-Zuordnungen
	 * @param uvUnterrichtService der Service für UV-Unterrichte
	 * @param uvUnterrichtRaumService der Service für UV-Unterricht-Raum-Zuordnungen
	 * @param uvUnterrichtLerngruppenlehrerService der Service für UV-Unterricht-Lerngruppenlehrer-Zuordnungen
	 */
	public UvPlanungsabschnittsdatenBundleService(
			final UvPlanungsabschnittService uvPlanungsabschnittService,
			final UvPlanungsabschnittLehrerService uvPlanungsabschnittLehrerService,
			final UvPlanungsabschnittSchuelerService uvPlanungsabschnittSchuelerService,
			final UvPlanungsabschnittZeitrasterService uvPlanungsabschnittZeitrasterService,
			final UvSchieneService uvSchieneService,
			final UvKlasseService uvKlasseService,
			final UvKlassenLehrerService uvKlassenLehrerService,
			final UvKursService uvKursService,
			final UvSchuelergruppeService uvSchuelergruppeService,
			final UvLerngruppenLehrerService uvLerngruppenLehrerService,
			final UvLerngruppenSchieneService uvLerngruppenSchieneService,
			final UvLerngruppeService uvLerngruppeService,
			final UvSchuelergruppeSchuelerService uvSchuelergruppeSchuelerService,
			final UvUnterrichtService uvUnterrichtService,
			final UvUnterrichtRaumService uvUnterrichtRaumService,
			final UvUnterrichtLerngruppenlehrerService uvUnterrichtLerngruppenlehrerService) {
		this.uvPlanungsabschnittService = uvPlanungsabschnittService;
		this.uvPlanungsabschnittLehrerService = uvPlanungsabschnittLehrerService;
		this.uvPlanungsabschnittSchuelerService = uvPlanungsabschnittSchuelerService;
		this.uvPlanungsabschnittZeitrasterService = uvPlanungsabschnittZeitrasterService;
		this.uvSchieneService = uvSchieneService;
		this.uvKlasseService = uvKlasseService;
		this.uvKlassenLehrerService = uvKlassenLehrerService;
		this.uvKursService = uvKursService;
		this.uvSchuelergruppeService = uvSchuelergruppeService;
		this.uvLerngruppenLehrerService = uvLerngruppenLehrerService;
		this.uvLerngruppenSchieneService = uvLerngruppenSchieneService;
		this.uvLerngruppeService = uvLerngruppeService;
		this.uvSchuelergruppeSchuelerService = uvSchuelergruppeSchuelerService;
		this.uvUnterrichtService = uvUnterrichtService;
		this.uvUnterrichtRaumService = uvUnterrichtRaumService;
		this.uvUnterrichtLerngruppenlehrerService = uvUnterrichtLerngruppenlehrerService;
	}

	/**
	 * Ermittelt ein vollständiges Bundle der Daten eines UV-Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt die ID des UV-Planungsabschnitts
	 *
	 * @return das Bundle mit dem Planungsabschnitt und allen zugehörigen Zuordnungen
	 */
	public UvPlanungsabschnittsdatenBundle getPlanungsabschnittsdatenBundle(final long idPlanungsabschnitt) {
		final UvPlanungsabschnittsdatenBundle result = new UvPlanungsabschnittsdatenBundle();
		result.planungsabschnitt = uvPlanungsabschnittService.get(idPlanungsabschnitt);
		result.planungsabschnittlehrer = uvPlanungsabschnittLehrerService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.planungsabschnittschueler = uvPlanungsabschnittSchuelerService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.planungsabschnittzeitraster = uvPlanungsabschnittZeitrasterService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.schienen = uvSchieneService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.klassen = uvKlasseService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.klassenlehrer = uvKlassenLehrerService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.kurse = uvKursService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.schuelergruppen = uvSchuelergruppeService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.lerngruppenlehrer = uvLerngruppenLehrerService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.lerngruppenschienen = uvLerngruppenSchieneService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.lerngruppen = uvLerngruppeService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.schuelergruppenschueler = uvSchuelergruppeSchuelerService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.unterrichte = uvUnterrichtService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.unterrichtraeume = uvUnterrichtRaumService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		result.unterrichtlerngruppenlehrer = uvUnterrichtLerngruppenlehrerService.getListByPlanungsabschnitt(idPlanungsabschnitt);
		return result;
	}
}
