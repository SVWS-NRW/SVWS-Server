package de.svws_nrw.base.untis;

import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.base.ResourceUtils;

/**
 * Diese Klasse prüft das Einlesen von Untis-GPU-Dateien.
 */
class TestGPU {

	private static final String PFAD_DATEN = "de/svws_nrw/base/untis/";

	/**
	 * Tests zum Einlesen von GPU001-Dateien
	 */
	@Test
	void testGPU001() {
		try {
			final Path path = ResourceUtils.getFile(PFAD_DATEN + "GPU001.txt");
			final String strGPU001 = Files.readString(path);
			final List<UntisGPU001> unterrichte = UntisGPU001.readCSV(strGPU001);
			for (final UntisGPU001 unterricht : unterrichte)
				System.out.println(unterricht.idUnterricht + " -> " + unterricht.klasseKuerzel + " " + unterricht.fachKuerzel + " " + unterricht.lehrerKuerzel);
		} catch (final Exception e) {
			fail(e);
		}
	}


	/**
	 * Tests zum Einlesen von GPU002-Dateien
	 */
	@Test
	void testGPU002() {
		try {
			final Path path = ResourceUtils.getFile(PFAD_DATEN + "GPU002.txt");
			final String strGPU002 = Files.readString(path);
			final List<UntisGPU002> unterrichte = UntisGPU002.readCSV(strGPU002);
			for (final UntisGPU002 unterricht : unterrichte)
				System.out.println(unterricht.idUnterricht + " -> " + unterricht.wochenTyp + " " + unterricht.klasseKuerzel + " " + unterricht.fachKuerzel + " " + unterricht.lehrerKuerzel);
		} catch (final Exception e) {
			fail(e);
		}
	}


}
