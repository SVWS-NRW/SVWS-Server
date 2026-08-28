package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import de.svws_nrw.service.schueler.foto.SchuelerFoto;
import de.svws_nrw.service.schueler.foto.SchuelerFotoService;

/**
 * Prüft, dass das Schüler-Repository die Fotos getrennt von den Stammdaten lädt. Der Vertrag: Ein Foto wird höchstens einmal geholt, die Fotos aller bereits
 * bekannten Schüler kommen in einer Abfrage, und ein Schüler ohne hinterlegtes Foto liefert einen leeren String, ohne weitere Abfragen auszulösen.
 * <p>Ersetzt wird die Naht zum Foto-Service, den das Repository über die statische Factory holt. Die Meldung eines Ladefehlers prüft
 * {@code TestReportingRepositorySchueler} gemeinsam mit den übrigen Teildaten.</p>
 */
class TestReportingRepositorySchuelerFotos {

	/** Die ID des Schülers, zu dem ein Foto hinterlegt ist. */
	private static final long ID_SCHUELER = 42L;

	/** Die ID eines zweiten bekannten Schülers, dessen Foto in derselben Abfrage mitkommt. */
	private static final long ID_SCHUELER_ZWEI = 43L;

	/** Die ID eines Schülers ohne hinterlegtes Foto. */
	private static final long ID_SCHUELER_OHNE_FOTO = 44L;

	/** Das Foto im Base64-Format. */
	private static final String FOTO_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	/** Der gemockte Context, über den das Repository Ausgabeprobleme meldet. */
	private ReportingContext reportingContext;

	/** Der Foto-Service, den das Repository über die Factory erhält. */
	private SchuelerFotoService fotoService;

	/** Die Naht für die statische Factory des Schüler-Service. */
	private MockedStatic<SchuelerServiceFactory> serviceFactoryStatisch;

	/** Das Repository unter Test. */
	private ReportingRepositorySchueler repository;


	@BeforeEach
	void setUp() {
		fotoService = mock(SchuelerFotoService.class);

		final SchuelerServiceFactory factory = mock(SchuelerServiceFactory.class);
		when(factory.getSchuelerFotoService()).thenReturn(fotoService);

		serviceFactoryStatisch = mockStatic(SchuelerServiceFactory.class);
		serviceFactoryStatisch.when(SchuelerServiceFactory::getNewInstance).thenReturn(factory);

		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerList());

		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);

		repository = new ReportingRepositorySchueler(reportingContext);
		registriere(ID_SCHUELER);
	}


	@AfterEach
	void tearDown() {
		serviceFactoryStatisch.close();
	}

	/**
	 * Legt die Stammdaten eines Schülers in den Cache, so wie es der Proxy bei seiner Konstruktion tut.
	 *
	 * @param idSchueler Die ID des Schülers.
	 */
	private void registriere(final long idSchueler) {
		final SchuelerStammdaten stammdaten = new SchuelerStammdaten();
		stammdaten.id = idSchueler;
		repository.registriereStammdaten(idSchueler, stammdaten);
	}

	/**
	 * Legt fest, welche Fotos der Service zurückgibt.
	 *
	 * @param fotos Die Fotos, die der Service liefert.
	 */
	private void gebeFotosVor(final List<SchuelerFoto> fotos) {
		when(fotoService.getBySchuelerIds(anyList())).thenReturn(fotos);
	}


	/** Zu einem Schüler mit hinterlegtem Foto liefert das Repository dessen Base64-Daten. */
	@Test
	void schuelerFotoLiefertDasHinterlegteFoto() {
		gebeFotosVor(List.of(new SchuelerFoto(ID_SCHUELER, FOTO_BASE64)));

		assertEquals(FOTO_BASE64, repository.schuelerFoto(ID_SCHUELER));
	}

	/** Fehlt das Foto, ist das kein Fehler: Das Repository liefert einen leeren String. */
	@Test
	void schuelerOhneFotoLiefertLeerenString() {
		gebeFotosVor(List.of());

		assertEquals("", repository.schuelerFoto(ID_SCHUELER_OHNE_FOTO));
	}

	/** Ein Eintrag ohne Bilddaten zählt wie ein fehlendes Foto. */
	@Test
	void eintragOhneBilddatenLiefertLeerenString() {
		gebeFotosVor(List.of(new SchuelerFoto(ID_SCHUELER, null)));

		assertEquals("", repository.schuelerFoto(ID_SCHUELER));
	}

	/** Ein einmal geladenes Foto kommt aus dem Cache. */
	@Test
	void zweiterZugriffFragtDenServiceNichtErneut() {
		gebeFotosVor(List.of(new SchuelerFoto(ID_SCHUELER, FOTO_BASE64)));

		repository.schuelerFoto(ID_SCHUELER);
		repository.schuelerFoto(ID_SCHUELER);

		verify(fotoService, times(1)).getBySchuelerIds(anyList());
	}

	/** Auch das Fehlen eines Fotos wird gemerkt, sonst fragte jeder weitere Zugriff den Service erneut. */
	@Test
	void zweiterZugriffOhneFotoFragtDenServiceNichtErneut() {
		gebeFotosVor(List.of());

		repository.schuelerFoto(ID_SCHUELER_OHNE_FOTO);
		repository.schuelerFoto(ID_SCHUELER_OHNE_FOTO);

		verify(fotoService, times(1)).getBySchuelerIds(anyList());
	}

	/** Der erste Zugriff holt die Fotos aller bekannten Schüler in einer Abfrage; der zweite Schüler löst danach keine weitere aus. */
	@Test
	void bekannteSchuelerWerdenGemeinsamGeladen() {
		registriere(ID_SCHUELER_ZWEI);
		gebeFotosVor(List.of(new SchuelerFoto(ID_SCHUELER, FOTO_BASE64), new SchuelerFoto(ID_SCHUELER_ZWEI, FOTO_BASE64)));

		repository.schuelerFoto(ID_SCHUELER);

		final ArgumentCaptor<List<Long>> ids = ArgumentCaptor.captor();
		verify(fotoService, times(1)).getBySchuelerIds(ids.capture());
		assertTrue(ids.getValue().containsAll(List.of(ID_SCHUELER, ID_SCHUELER_ZWEI)), "Die Abfrage umfasst beide bekannten Schüler.");

		assertEquals(FOTO_BASE64, repository.schuelerFoto(ID_SCHUELER_ZWEI));
		verify(fotoService, times(1)).getBySchuelerIds(anyList());
	}

	/** Ein Ladefehler beendet die Ausgabe nicht; der Schüler erscheint dann ohne Foto. */
	@Test
	void ladefehlerLiefertLeerenString() {
		when(fotoService.getBySchuelerIds(anyList())).thenThrow(new IllegalStateException("Verbindung verloren"));

		assertEquals("", repository.schuelerFoto(ID_SCHUELER));
	}

	/** Ein erfolgreicher Zugriff meldet kein Problem, auch wenn zu dem Schüler gar kein Foto hinterlegt ist. */
	@Test
	void erfolgreicherZugriffMeldetKeinProblem() {
		gebeFotosVor(List.of());

		repository.schuelerFoto(ID_SCHUELER);

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

}
