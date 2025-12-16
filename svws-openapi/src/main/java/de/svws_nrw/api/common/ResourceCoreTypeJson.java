package de.svws_nrw.api.common;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse dient dem Zugriff auf die einzelnen Json-Ressourcen der Core-Types
 */
public class ResourceCoreTypeJson {

	/** Eine Map mit der Zuordnung der einzelnen Namen von Core-Types zu deren Pfaden in den Json-Ressourcen */
	private static Map<String, String> mapNameToResourceFile = Map.ofEntries(
			Map.entry("Schulform", "de/svws_nrw/asd/types/schule/Schulform.json"),
			Map.entry("BerufskollegAnlage", "de/svws_nrw/asd/types/schule/BerufskollegAnlage.json"),
			Map.entry("AllgemeinbildendOrganisationsformen", "de/svws_nrw/asd/types/schule/AllgemeinbildendOrganisationsformen.json"),
			Map.entry("BerufskollegOrganisationsformen", "de/svws_nrw/asd/types/schule/BerufskollegOrganisationsformen.json"),
			Map.entry("WeiterbildungskollegOrganisationsformen", "de/svws_nrw/asd/types/schule/WeiterbildungskollegOrganisationsformen.json"),
			Map.entry("SchulabschlussAllgemeinbildend", "de/svws_nrw/asd/types/schule/SchulabschlussAllgemeinbildend.json"),
			Map.entry("SchulabschlussBerufsbildend", "de/svws_nrw/asd/types/schule/SchulabschlussBerufsbildend.json"),
			Map.entry("Einschulungsart", "de/svws_nrw/asd/types/schueler/Einschulungsart.json"),
			Map.entry("HerkunftBildungsgang", "de/svws_nrw/asd/types/schueler/HerkunftBildungsgang.json"),
			Map.entry("HerkunftBildungsgangTyp", "de/svws_nrw/asd/types/schueler/HerkunftBildungsgangTyp.json"),
			Map.entry("Jahrgaenge", "de/svws_nrw/asd/types/jahrgang/Jahrgaenge.json"),
			Map.entry("PrimarstufeSchuleingangsphaseBesuchsjahre", "de/svws_nrw/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre.json"),
			Map.entry("Religion", "de/svws_nrw/asd/types/schule/Religion.json"),
			Map.entry("Kindergartenbesuch", "de/svws_nrw/asd/types/schule/Kindergartenbesuch.json"),
			Map.entry("SchuelerStatus", "de/svws_nrw/asd/types/schueler/SchuelerStatus.json"),
			Map.entry("Note", "de/svws_nrw/asd/types/Note.json"),
			Map.entry("Sprachreferenzniveau", "de/svws_nrw/asd/types/fach/Sprachreferenzniveau.json"),
			Map.entry("BerufskollegBildungsgangTyp", "de/svws_nrw/asd/types/schule/BerufskollegBildungsgangTyp.json"),
			Map.entry("WeiterbildungskollegBildungsgangTyp", "de/svws_nrw/asd/types/schule/WeiterbildungskollegBildungsgangTyp.json"),
			Map.entry("Schulgliederung", "de/svws_nrw/asd/types/schule/Schulgliederung.json"),
			Map.entry("Verkehrssprache", "de/svws_nrw/asd/types/schule/Verkehrssprache.json"),
			Map.entry("Fachgruppe", "de/svws_nrw/asd/types/fach/Fachgruppe.json"),
			Map.entry("Fach", "de/svws_nrw/asd/types/fach/Fach.json"),
			Map.entry("LehrerAbgangsgrund", "de/svws_nrw/asd/types/lehrer/LehrerAbgangsgrund.json"),
			Map.entry("LehrerBeschaeftigungsart", "de/svws_nrw/asd/types/lehrer/LehrerBeschaeftigungsart.json"),
			Map.entry("LehrerEinsatzstatus", "de/svws_nrw/asd/types/lehrer/LehrerEinsatzstatus.json"),
			Map.entry("LehrerFachrichtung", "de/svws_nrw/asd/types/lehrer/LehrerFachrichtung.json"),
			Map.entry("LehrerLehrbefaehigung", "de/svws_nrw/asd/types/lehrer/LehrerLehrbefaehigung.json"),
			Map.entry("LehrerFachrichtungAnerkennung", "de/svws_nrw/asd/types/lehrer/LehrerFachrichtungAnerkennung.json"),
			Map.entry("LehrerLehramt", "de/svws_nrw/asd/types/lehrer/LehrerLehramt.json"),
			Map.entry("LehrerLehramtAnerkennung", "de/svws_nrw/asd/types/lehrer/LehrerLehramtAnerkennung.json"),
			Map.entry("LehrerLehrbefaehigungAnerkennung", "de/svws_nrw/asd/types/lehrer/LehrerLehrbefaehigungAnerkennung.json"),
			Map.entry("LehrerLeitungsfunktion", "de/svws_nrw/asd/types/lehrer/LehrerLeitungsfunktion.json"),
			Map.entry("LehrerRechtsverhaeltnis", "de/svws_nrw/asd/types/lehrer/LehrerRechtsverhaeltnis.json"),
			Map.entry("LehrerZugangsgrund", "de/svws_nrw/asd/types/lehrer/LehrerZugangsgrund.json"),
			Map.entry("BilingualeSprache", "de/svws_nrw/asd/types/fach/BilingualeSprache.json"),
			Map.entry("KAOABerufsfeld", "de/svws_nrw/asd/types/kaoa/KAOABerufsfeld.json"),
			Map.entry("KAOAMerkmaleOptionsarten", "de/svws_nrw/asd/types/kaoa/KAOAMerkmaleOptionsarten.json"),
			Map.entry("KAOAZusatzmerkmaleOptionsarten", "de/svws_nrw/asd/types/kaoa/KAOAZusatzmerkmaleOptionsarten.json"),
			Map.entry("KAOAEbene4", "de/svws_nrw/asd/types/kaoa/KAOAEbene4.json"),
			Map.entry("KAOAZusatzmerkmal", "de/svws_nrw/asd/types/kaoa/KAOAZusatzmerkmal.json"),
			Map.entry("KAOAAnschlussoptionen", "de/svws_nrw/asd/types/kaoa/KAOAAnschlussoptionen.json"),
			Map.entry("KAOAKategorie", "de/svws_nrw/asd/types/kaoa/KAOAKategorie.json"),
			Map.entry("KAOAMerkmal", "de/svws_nrw/asd/types/kaoa/KAOAMerkmal.json"),
			Map.entry("Klassenart", "de/svws_nrw/asd/types/klassen/Klassenart.json"),
			Map.entry("Uebergangsempfehlung", "de/svws_nrw/asd/types/schueler/Uebergangsempfehlung.json"),
			Map.entry("ZulaessigeKursart", "de/svws_nrw/asd/types/kurse/ZulaessigeKursart.json"),
			Map.entry("Foerderschwerpunkt", "de/svws_nrw/asd/types/schule/Foerderschwerpunkt.json"),
			Map.entry("Termin", "de/svws_nrw/asd/types/schule/Termin.json"),
			Map.entry("Betreuungsart", "de/svws_nrw/asd/types/schueler/Betreuungsart.json"),
			Map.entry("FormOffenerGanztag", "de/svws_nrw/asd/types/schule/FormOffenerGanztag.json"),
			Map.entry("LehrerAnrechnungsgrund", "de/svws_nrw/asd/types/lehrer/LehrerAnrechnungsgrund.json"),
			Map.entry("LehrerMehrleistungsarten", "de/svws_nrw/asd/types/lehrer/LehrerMehrleistungsarten.json"),
			Map.entry("LehrerMinderleistungsarten", "de/svws_nrw/asd/types/lehrer/LehrerMinderleistungsarten.json"),
			Map.entry("LehrerPflichtstundensollVollzeit", "de/svws_nrw/asd/types/lehrer/LehrerPflichtstundensollVollzeit.json"),
			Map.entry("Nationalitaeten", "de/svws_nrw/asd/types/schule/Nationalitaeten.json"),
			Map.entry("ValidatorenFehlerartKontext", "de/svws_nrw/asd/validate/ValidatorenFehlerartKontext.json"),
			Map.entry("Floskelgruppenart", "de/svws_nrw/asd/types/schule/Floskelgruppenart.json"),
			Map.entry("Einwilligungsschluessel", "de/svws_nrw/asd/types/schule/Einwilligungsschluessel.json"),
			Map.entry("Bildungsstufe", "de/svws_nrw/asd/types/schule/Bildungsstufe.json"),
			Map.entry("Herkunftsarten", "de/svws_nrw/asd/types/schueler/Herkunftsarten.json"),
			Map.entry("HerkunftSonstige", "de/svws_nrw/asd/types/schueler/HerkunftSonstige.json"),
			Map.entry("HerkunftSchulform", "de/svws_nrw/asd/types/schueler/HerkunftSchulform.json"),
			Map.entry("BerufskollegBerufsebene1", "de/svws_nrw/asd/types/schule/BerufskollegBerufsebene1.json"),
			Map.entry("BerufskollegBerufsebene2", "de/svws_nrw/asd/types/schule/BerufskollegBerufsebene2.json"),
			Map.entry("BerufskollegBerufsebene3", "de/svws_nrw/asd/types/schule/BerufskollegBerufsebene3.json"),
			Map.entry("Herkunftsschulnummer", "de/svws_nrw/asd/types/schule/Herkunftsschulnummer.json"),
			Map.entry("Reformpaedagogik", "de/svws_nrw/asd/types/schule/Reformpaedagogik.json")
	);


	/**
	 * Ermittelt die JSON-Daten für den Core-Type mit dem übergebenen Namen.
	 *
	 * @param name   der Name des Core-Types
	 *
	 * @return die JSON-Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static @NotNull String get(final @NotNull String name) throws ApiOperationException {
		if ("allinone".equals(name))
			return getAllInOne();
		try {
			final String path = mapNameToResourceFile.get(name);
			if (path == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Der Name des Core-Types ist nicht bekannt.");
			final String json = JsonReader.fromResource(path);
			if (json == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Es konnte keine JSON-Datei für den angebenen Core-Type gefunden werden.");
			return json;
		} catch (final IOException e) {
			Logger.global().logLn("Fehler beim Einlesen der Core-Type-JSON-Kataloge!");
			Logger.global().logLn(e.getMessage());
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Einlesen von Core-Type-JSON-Katalogen.");
		}
	}


	/**
	 * Ermittelt die JSON-Daten für alle Core-Type und gibt diese gebündelt in einem großen JSON zurück.
	 *
	 * @return die JSON-Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static @NotNull String getAllInOne() throws ApiOperationException {
		final ObjectNode nodeResult = JsonReader.mapper.createObjectNode();
		try {
			for (final var entry : mapNameToResourceFile.entrySet()) {
				final String name = entry.getKey();
				final String path = entry.getValue();
				final String json = JsonReader.fromResource(path);
				if (json == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Es konnte keine JSON-Datei für den Core-Type " + name + " gefunden werden.");
				final JsonNode node = JsonReader.mapper.readTree(json);
				nodeResult.putPOJO(name, node);
			}
			return JsonReader.mapper.writeValueAsString(nodeResult);
		} catch (final IOException e) {
			Logger.global().logLn("Fehler beim Einlesen der Core-Type-JSON-Kataloge!");
			Logger.global().logLn(e.getMessage());
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Einlesen von Core-Type-JSON-Katalogen.");
		}
	}

}
