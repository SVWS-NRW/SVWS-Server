package de.svws_nrw.repo.gost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungenPK;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlenPK;
import de.svws_nrw.repo.RepositoryException;
import jakarta.validation.constraints.NotNull;


/**
 * Dieses Repository greift auf die Fachwahlen der Laufbahnplanung zu. Hat ein Schüler eine Wiederholung auf einen anderen
 * Abiturjahrgang, so gehören seine Planungsdaten in die Wiederholung-Tabelle, damit die Fachwahlen der aktuellen Planung
 * erhalten bleiben. Die Dienste der Laufbahnplanung arbeiten so unverändert mit {@link DTOGostSchuelerFachbelegungen}.
 * Die Einträge für Schüler mit Wiederholung sind Kopien und werden nur über {@link #update(DTOGostSchuelerFachbelegungen)} gespeichert.
 */
public final class GostSchuelerPlanungFachbelegungenRepositoryImpl implements GostSchuelerFachbelegungenRepository {

	private final GostSchuelerFachbelegungenRepository fachbelegungenRepository;
	private final GostSchuelerWiederholungFachwahlenRepository wiederholungFachwahlenRepository;
	private final GostSchuelerWiederholungRepository wiederholungRepository;

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param fachbelegungenRepository           das Repository für die Fachwahlen der aktuellen Planung
	 * @param wiederholungFachwahlenRepository   das Repository für die Fachwahlen einer Wiederholung
	 * @param wiederholungRepository             das Repository für die Wiederholungen
	 */
	protected GostSchuelerPlanungFachbelegungenRepositoryImpl(final GostSchuelerFachbelegungenRepository fachbelegungenRepository,
			final GostSchuelerWiederholungFachwahlenRepository wiederholungFachwahlenRepository, final GostSchuelerWiederholungRepository wiederholungRepository) {
		this.fachbelegungenRepository = fachbelegungenRepository;
		this.wiederholungFachwahlenRepository = wiederholungFachwahlenRepository;
		this.wiederholungRepository = wiederholungRepository;
	}


	/**
	 * Erstellt aus einer Fachwahl einer Wiederholung eine Fachwahl im Format der Laufbahnplanung.
	 *
	 * @param fw   die Fachwahl der Wiederholung
	 *
	 * @return die Fachwahl im Format der Laufbahnplanung
	 */
	public static @NotNull DTOGostSchuelerFachbelegungen toFachbelegung(final @NotNull DTOGostSchuelerWiederholungFachwahlen fw) {
		final DTOGostSchuelerFachbelegungen result = new DTOGostSchuelerFachbelegungen(fw.Schueler_ID, fw.Fach_ID);
		result.EF1_Kursart = fw.EF1_Kursart;
		result.EF2_Kursart = fw.EF2_Kursart;
		result.Q11_Kursart = fw.Q11_Kursart;
		result.Q12_Kursart = fw.Q12_Kursart;
		result.Q21_Kursart = fw.Q21_Kursart;
		result.Q22_Kursart = fw.Q22_Kursart;
		result.AbiturFach = fw.AbiturFach;
		result.Bemerkungen = fw.Bemerkungen;
		result.Referenzfach_ID = fw.Referenzfach_ID;
		return result;
	}


	/**
	 * Überträgt die Planungsdaten einer Fachwahl in eine Fachwahl einer Wiederholung.
	 *
	 * @param fb   die Fachwahl im Format der Laufbahnplanung
	 * @param fw   die Fachwahl der Wiederholung, in welche die Daten übertragen werden
	 */
	public static void copyToWiederholung(final @NotNull DTOGostSchuelerFachbelegungen fb, final @NotNull DTOGostSchuelerWiederholungFachwahlen fw) {
		fw.EF1_Kursart = fb.EF1_Kursart;
		fw.EF2_Kursart = fb.EF2_Kursart;
		fw.Q11_Kursart = fb.Q11_Kursart;
		fw.Q12_Kursart = fb.Q12_Kursart;
		fw.Q21_Kursart = fb.Q21_Kursart;
		fw.Q22_Kursart = fb.Q22_Kursart;
		fw.AbiturFach = fb.AbiturFach;
		fw.Bemerkungen = fb.Bemerkungen;
		fw.Referenzfach_ID = fb.Referenzfach_ID;
	}


	private boolean hatWiederholung(final long idSchueler) {
		return wiederholungRepository.findById(idSchueler).isPresent();
	}


	private Set<Long> getIdsMitWiederholung(final Collection<Long> idsSchueler) {
		return wiederholungRepository.findMapByIds(idsSchueler.stream().filter(Objects::nonNull).distinct().toList()).keySet();
	}


	@Override
	public @NotNull DTOGostSchuelerFachbelegungen getFirst() {
		throw new UnsupportedOperationException("Die Fachwahlen der Laufbahnplanung können nur schülerbezogen abgefragt werden.");
	}


	@Override
	public Optional<DTOGostSchuelerFachbelegungen> findFirst() {
		throw new UnsupportedOperationException("Die Fachwahlen der Laufbahnplanung können nur schülerbezogen abgefragt werden.");
	}


	@Override
	public @NotNull DTOGostSchuelerFachbelegungen getById(final DTOGostSchuelerFachbelegungenPK id) {
		return findById(id).orElseThrow(() -> new RepositoryException("Es konnte keine Fachwahl für die angegebene ID bestimmt werden."));
	}


	@Override
	public Optional<DTOGostSchuelerFachbelegungen> findById(final DTOGostSchuelerFachbelegungenPK id) {
		if (hatWiederholung(id.Schueler_ID)) {
			return wiederholungFachwahlenRepository.findById(new DTOGostSchuelerWiederholungFachwahlenPK(id.Schueler_ID, id.Fach_ID))
					.map(GostSchuelerPlanungFachbelegungenRepositoryImpl::toFachbelegung);
		}
		return fachbelegungenRepository.findById(id);
	}


	@Override
	public List<DTOGostSchuelerFachbelegungen> getAll() {
		throw new UnsupportedOperationException("Die Fachwahlen der Laufbahnplanung können nur schülerbezogen abgefragt werden.");
	}


	@Override
	public <K> Map<K, DTOGostSchuelerFachbelegungen> getMap(final Function<DTOGostSchuelerFachbelegungen, K> getKey) {
		throw new UnsupportedOperationException("Die Fachwahlen der Laufbahnplanung können nur schülerbezogen abgefragt werden.");
	}


	@Override
	public DTOGostSchuelerFachbelegungen create(final DTOGostSchuelerFachbelegungen entity) {
		return update(entity);
	}


	@Override
	public <C extends Collection<DTOGostSchuelerFachbelegungen>> C create(final C entities) {
		return update(entities);
	}


	@Override
	public DTOGostSchuelerFachbelegungen update(final DTOGostSchuelerFachbelegungen entity) {
		if (!hatWiederholung(entity.Schueler_ID)) {
			return fachbelegungenRepository.update(entity);
		}
		final DTOGostSchuelerWiederholungFachwahlen fw = wiederholungFachwahlenRepository
				.findById(new DTOGostSchuelerWiederholungFachwahlenPK(entity.Schueler_ID, entity.Fach_ID))
				.orElse(new DTOGostSchuelerWiederholungFachwahlen(entity.Schueler_ID, entity.Fach_ID));
		copyToWiederholung(entity, fw);
		wiederholungFachwahlenRepository.update(fw);
		return entity;
	}


	@Override
	public <C extends Collection<DTOGostSchuelerFachbelegungen>> C update(final C entities) {
		for (final DTOGostSchuelerFachbelegungen entity : entities) {
			update(entity);
		}
		return entities;
	}


	@Override
	public DTOGostSchuelerFachbelegungen delete(final DTOGostSchuelerFachbelegungen entity) {
		if (!hatWiederholung(entity.Schueler_ID)) {
			return fachbelegungenRepository.delete(entity);
		}
		wiederholungFachwahlenRepository.findById(new DTOGostSchuelerWiederholungFachwahlenPK(entity.Schueler_ID, entity.Fach_ID))
				.ifPresent(wiederholungFachwahlenRepository::delete);
		return entity;
	}


	@Override
	public <C extends Collection<DTOGostSchuelerFachbelegungen>> C delete(final C entities) {
		for (final DTOGostSchuelerFachbelegungen entity : entities) {
			delete(entity);
		}
		return entities;
	}


	@Override
	public void flush() {
		fachbelegungenRepository.flush();
	}


	@Override
	public HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> getMap2DBySchuelerIDAndFachID(final Collection<Long> idsSchueler) {
		final Set<Long> idsMitWiederholung = getIdsMitWiederholung(idsSchueler);
		final HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> result =
				fachbelegungenRepository.getMap2DBySchuelerIDAndFachID(idsSchueler.stream().filter(id -> !idsMitWiederholung.contains(id)).toList());
		for (final List<DTOGostSchuelerWiederholungFachwahlen> liste : wiederholungFachwahlenRepository.getMapBySchuelerID(idsMitWiederholung).values()) {
			for (final DTOGostSchuelerWiederholungFachwahlen fw : liste) {
				result.put(fw.Schueler_ID, fw.Fach_ID, toFachbelegung(fw));
			}
		}
		return result;
	}


	@Override
	public Map<Long, List<DTOGostSchuelerFachbelegungen>> getMapBySchuelerID(final Collection<Long> idsSchueler) {
		final Set<Long> idsMitWiederholung = getIdsMitWiederholung(idsSchueler);
		final Map<Long, List<DTOGostSchuelerFachbelegungen>> result =
				new HashMap<>(fachbelegungenRepository.getMapBySchuelerID(idsSchueler.stream().filter(id -> !idsMitWiederholung.contains(id)).toList()));
		for (final Map.Entry<Long, List<DTOGostSchuelerWiederholungFachwahlen>> e : wiederholungFachwahlenRepository.getMapBySchuelerID(idsMitWiederholung).entrySet()) {
			final List<DTOGostSchuelerFachbelegungen> liste = new ArrayList<>();
			for (final DTOGostSchuelerWiederholungFachwahlen fw : e.getValue()) {
				liste.add(toFachbelegung(fw));
			}
			result.put(e.getKey(), liste);
		}
		return result;
	}


	@Override
	public void deleteMultipleBySchuelerID(final Collection<Long> idsSchueler) {
		final Set<Long> idsMitWiederholung = getIdsMitWiederholung(idsSchueler);
		fachbelegungenRepository.deleteMultipleBySchuelerID(idsSchueler.stream().filter(id -> !idsMitWiederholung.contains(id)).toList());
		wiederholungFachwahlenRepository.deleteMultipleBySchuelerID(idsMitWiederholung);
	}

}
