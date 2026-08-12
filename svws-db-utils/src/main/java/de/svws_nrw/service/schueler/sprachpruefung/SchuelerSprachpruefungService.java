package de.svws_nrw.service.schueler.sprachpruefung;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.fach.SprachreferenzniveauKatalogEintrag;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.schueler.DataSchuelerSprachpruefung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.repo.schueler.sprachpruefung.SchuelerSprachpruefungRepository;


/**
 * Ein Service für den Zugriff auf die Sprachprüfungen der Schüler
 */
public final class SchuelerSprachpruefungService {

	/** Das Repository für die Sprachprüfungen der Schüler. */
	private final SchuelerSprachpruefungRepository schuelerSprachpruefungRepository;


	/**
	 * Erstellt einen neuen Service mit Repository-Anbindung.
	 *
	 * @param schuelerSprachpruefungRepository   das Repository für die Sprachprüfungen
	 */
	public SchuelerSprachpruefungService(final SchuelerSprachpruefungRepository schuelerSprachpruefungRepository) {
		this.schuelerSprachpruefungRepository = schuelerSprachpruefungRepository;
	}


	/**
	 * Bestimmt die Sprachprüfungen mehrerer Schüler.
	 *
	 * @param idsSchueler   die ID des Schülers, für welche die Sprachprüfungen bestimmt werden sollen
	 * @param abschnitt     der Schuljahresabschnitt für die Konvertierung
	 *
	 * @return eine Map mit den Sprachprüfungen der Schüler zugeordnet zu deren IDs
	 */
	public Map<Long, List<Sprachpruefung>> getMapSprachenfolgen(final Collection<Long> idsSchueler, final Schuljahresabschnitt abschnitt) {
		final Map<Long, List<DTOSchuelerSprachpruefungen>> map = schuelerSprachpruefungRepository.getMapBySchuelerIDs(idsSchueler);
		final Map<Long, List<Sprachpruefung>> result = new HashMap<>();
		for (final var entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().stream().map(dto -> toApi(dto, abschnitt)).toList());
		}
		return result;
	}


	/**
	 * Konvertiert die übergebenen DTO-Objekte in die API-Objekte.
	 *
	 * @param dto         das DTO-Objekt, welches konvertiert werden soll
	 * @param abschnitt   der Schuljahresabschnitt, welcher für die Konvertierung benötigt wird
	 *
	 * @return das API-Objekt, welches aus dem DTO-Objekt konvertiert wurde
	 */
	public static Sprachpruefung toApi(final DTOSchuelerSprachpruefungen dto, final Schuljahresabschnitt abschnitt) {
		final var daten = new Sprachpruefung();
		daten.id = dto.ID;
		daten.sprache = dto.Sprache;
		daten.jahrgang = dto.ASDJahrgang;
		daten.anspruchsniveauId = (dto.Anspruchsniveau == null) ? null : dto.Anspruchsniveau.daten.id;
		daten.pruefungsdatum = dto.Pruefungsdatum;
		daten.ersetzteSprache = SprachendatenUtils.getErsetzeSprache(dto.Sprache);
		daten.istHSUPruefung = Boolean.TRUE.equals(dto.IstHSUPruefung);
		daten.istFeststellungspruefung = Boolean.TRUE.equals(dto.IstFeststellungspruefung);
		daten.kannErstePflichtfremdspracheErsetzen = Boolean.TRUE.equals(dto.KannErstePflichtfremdspracheErsetzen);
		daten.kannZweitePflichtfremdspracheErsetzen = Boolean.TRUE.equals(dto.KannZweitePflichtfremdspracheErsetzen);
		daten.kannWahlpflichtfremdspracheErsetzen = Boolean.TRUE.equals(dto.KannWahlpflichtfremdspracheErsetzen);
		daten.kannBelegungAlsFortgefuehrteSpracheErlauben = Boolean.TRUE.equals(dto.KannBelegungAlsFortgefuehrteSpracheErlauben);
		final Sprachreferenzniveau niveau = (dto.Referenzniveau == null) ? null : Sprachreferenzniveau.data().getWertBySchluessel(dto.Referenzniveau);
		final SprachreferenzniveauKatalogEintrag niveauEintrag = (niveau == null) ? null : niveau.daten(abschnitt.schuljahr);
		daten.referenzniveau = (niveauEintrag == null) ? null : niveauEintrag.schluessel;
		final Note note = Note.fromNoteSekI(dto.NotePruefung);
		final NoteKatalogEintrag noteEintrag = (note == null) ? null : note.daten(abschnitt.schuljahr);
		daten.note = (noteEintrag == null) ? null : dto.NotePruefung;
		daten.zeugnisbezeichnung = DataSchuelerSprachpruefung.mapZeugnisbezeichnung(dto.Zeugnisbezeichnung, dto.Sprache);
		return daten;
	}

}
