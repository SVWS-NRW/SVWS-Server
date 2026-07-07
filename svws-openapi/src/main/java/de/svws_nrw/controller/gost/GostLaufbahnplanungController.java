package de.svws_nrw.controller.gost;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1;
import de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für die Methoden zur Behandlung der API-Zugriffe im Bereich Laufbahnplanung der Gymnasialen Oberstufe.
 */
public interface GostLaufbahnplanungController {

	/**
	 * Gibt die Abiturdaten für den angegebenen Schüler zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Abiturdaten
	 */
	Response getBySchuelerID(long idSchueler);

	/**
	 * Gibt die Liste mit den Abiturdaten der Schüler eines Abiturjahrganges zurück.
	 *
	 * @param abiturjahrgang   der Abiturjahrgang
	 *
	 * @return die Response mit der Lister der Abiturdaten der Schüler
	 */
	Response getListByAbiturjahrgang(int abiturjahrgang);

	/**
	 * Gibt die Fachwahlen des angegebenen Schülers für das angebene Fach zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 *
	 * @return die Fachwahlen
	 */
	Response getFachwahl(long idSchueler, long idFach);


	/**
	 * Führt einen Patch auf die Fachwahlen des angegebenen Schülers für das angebene Fach durch.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 * @param is           der Patch
	 *
	 * @return die Response
	 */
	Response patchFachwahl(Long idSchueler, Long idFach, InputStream is);

	/**
	 * Gibt die Wahlen des angegebenen Schülers zu den Gleichwertig Komplexen Lernleistunen zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Wahlen des Schülers zu den Gleichwertig Komplexen Lernleistunen
	 */
	Response getGKLWahl(long idSchueler);

	/**
	 * Setzt die Wahlen eines Schülers zu den Gleichwertig Komplexen Lernleistunen (GKL).
	 *
	 * @param wahl         die Wahl des Schülers zu den GKLs
	 *
	 * @return die Response
	 */
	Response putGKLWahl(GostSchuelerGKLWahl wahl);

	/**
	 * Setzt die Fachwahlen für den angegebenen Schüler zurück.
	 * Liegen bereits bewertete Halbjahre vor, so werden die zukünftigen Fachwahlen entfernt.
	 * Ansonsten wir die Vorlage für die Fachwahlen des Abiturjahrgangs übernommen.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response
	 */
	Response reset(long idSchueler);

	/**
	 * Setzt die Fachwahlen bei allen (!) Schülern des angegebenen Abiturjahrgangs zurück.
	 *
	 * @param abijahr   der Abiturjahrgang
	 *
	 * @return die Response
	 */
	Response resetAbiturjahrgang(Integer abijahr);

	/**
	 * Löscht die Fachwahlen der mit ihren IDs angegebenen Schüler.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Response
	 */
	Response deleteFachwahlen(List<Long> idsSchueler);

	/**
	 * Erstellt eine Export-Datei mit den Laufbahnplanungsdaten des angegebenen Schülers zur Bearbeitung in einem externen Tool.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit der GZip-Komprimierten Laufbahnplanungs-Datei
	 */
	Response exportGZip(long idSchueler);

	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus den übergebenen Laufbahnplanungsdaten.
	 *
	 * @param multipart   die Laufbahnplanungsdaten als GZIP-Komprimierte JSONs
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	Response importGostLaufbahnplanungGZip(MultipartFormDataInput multipart);

	/**
	 * Erstellt Export-Dateien mit den Laufbahnplanungsdaten der angegebenen Schüler zur Bearbeitung in einem externen Tool.
	 * Die Dateien werden in einer ZIP-Datei gebündelt.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Response mit der ZIP-Datei mit den GZip-Komprimierten Laufbahnplanungs-Dateien
	 */
	Response exportGZip(Collection<Long> idsSchueler);

	/**
	 * Importiert die Daten eines Schülers aus der übergebenen Laufbahnplanungsdatei.
	 *
	 * @param data         die Laufbahnplanungsdatei als GZIP-Komprimiertes JSON
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	Response importGostLaufbahnplanungGZip(byte[] data);

	/**
	 * Erstellt den Export mit den Laufbahnplanungsdaten des angegebenen Schülers zur Bearbeitung in einem externen Tool.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Laufbahnplanungsdaten
	 */
	Response exportJSON(long idSchueler);

	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus den übergebenen
	 * Laufbahnplanungsdaten in der Version 1.
	 *
	 * @param laufbahnplanungsdaten   die Laufbahnplanungsdaten
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	Response importGostLaufbahnplanungV1(GostLaufbahnplanungExportV1 laufbahnplanungsdaten);


	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus den übergebenen
	 * Laufbahnplanungsdaten in der Version 2.
	 *
	 * @param laufbahnplanungsdaten   die Laufbahnplanungsdaten
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	Response importGostLaufbahnplanungV2(GostLaufbahnplanungExportV2 laufbahnplanungsdaten);


	/**
	 * Gibt die aggregierten Fachwahl-Informationen für den angegebenen Abiturjahrgang zurück.
	 *
	 * @param abiturjahrgang   der Abiturjahrgang
	 *
	 * @return die Response mit den Fachwahlen für den Abiturjahrgang
	 */
	Response getJahrgangsFachwahlen(int abiturjahrgang);

	/**
	 * Gibt die aggregierten Fachwahl-Informationen für den angegebenen Abiturjahrgang und das angegebene Halbjahr zurück.
	 *
	 * @param abiturjahrgang   der Abiturjahrgang
	 * @param idHalbjahr       die ID des Halbjahres der Gymnasialen Oberstufe
	 *
	 * @return die Response mit den Fachwahlen für das Halbjahr
	 */
	Response getJahrgangsFachwahlenForHalbjahr(int abiturjahrgang, int idHalbjahr);

	/**
	 * Gibt die aggregierte Fachwahl-Statistik für den angegebenen Abiturjahrgang zurück.
	 *
	 * @param abiturjahrgang   der Abiturjahrgang
	 *
	 * @return die Response mit der Fachwahl-Statistik
	 */
	Response getJahrgangFachwahlStatistik(int abiturjahrgang);

}
