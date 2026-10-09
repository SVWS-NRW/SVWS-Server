package de.svws_nrw.service.lehrer.foto;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.mapper.lehrer.foto.LehrerFotoMapper;
import de.svws_nrw.repo.lehrer.foto.LehrerFotoRepository;
import org.apache.commons.lang3.Strings;

/**
 * Service für die Verwaltung von Lehrerfotos.
 * <p>
 * Kapselt die Geschäftslogik zum Lesen, Anlegen, Aktualisieren und Löschen
 * von {@link LehrerFoto}-Objekten. Schreiboperationen werden transaktional ausgeführt.
 */
public class LehrerFotoService {

	private final LehrerFotoRepository lehrerFotoRepository;
	private final LehrerFotoMapper lehrerFotoMapper;

	/**
	 * Erstellt eine neue Instanz des Services.
	 *
	 * @param lehrerFotoRepository das Repository für den Datenbankzugriff auf Lehrerfotos
	 * @param lehrerFotoMapper     der Mapper zur Konvertierung zwischen DTO und Domain-Objekt
	 */
	public LehrerFotoService(
			final LehrerFotoRepository lehrerFotoRepository,
			final LehrerFotoMapper lehrerFotoMapper
	) {
		this.lehrerFotoRepository = lehrerFotoRepository;
		this.lehrerFotoMapper = lehrerFotoMapper;
	}

	/**
	 * Sucht ein Lehrerfoto anhand der idLehrer.
	 *
	 * @param idLehrer die ID des Lehrers
	 * @return ein {@link Optional} mit dem gefundenen {@link LehrerFoto}, oder leer wenn nicht gefunden
	 */
	public Optional<LehrerFoto> findByIdLehrer(final long idLehrer) {
		return this.lehrerFotoRepository.findById(idLehrer)
				.map(lehrerFotoMapper::toDomain);
	}

	/**
	 * Gibt alle LehrerFotos für die angegebenen Lehrer-IDs zurück.
	 *
	 * @param lehrerIds Liste der Lehrer-IDs
	 * @return Liste der zugehörigen {@link LehrerFoto}-Objekte
	 */
	public List<LehrerFoto> getByLehrerIds(final List<Long> lehrerIds) {
		return this.lehrerFotoRepository.findListByIds(lehrerIds)
				.stream()
				.map(lehrerFotoMapper::toDomain)
				.toList();
	}

	/**
	 * Legt ein Lehrer-Foto an, aktualisiert es oder löscht es — abhängig vom aktuellen Zustand
	 * und dem übergebenen Wert:
	 * <ul>
	 *   <li>Foto vorhanden + {@code newFotoBase64 == null} → Foto wird gelöscht</li>
	 *   <li>Foto nicht vorhanden + {@code newFotoBase64 == null} → keine Aktion</li>
	 *   <li>Foto nicht vorhanden + {@code newFotoBase64 != null} → Foto wird angelegt</li>
	 *   <li>Foto vorhanden + Inhalt geändert → Foto wird aktualisiert</li>
	 *   <li>Foto vorhanden + Inhalt unverändert → keine Aktion</li>
	 * </ul>
	 *
	 * @param idLehrer      die ID des Lehrers
	 * @param newFotoBase64 das neue Foto in Base64-Kodierung, oder {@code null} zum Löschen
	 */
	public void upsertOrDelete(final long idLehrer, final String newFotoBase64) {
		TransactionSupport.transactional(() -> {
			final var existing = this.lehrerFotoRepository.findById(idLehrer);
			if (existing.isPresent()) {
				updateOrDelete(existing.get(), newFotoBase64);
			} else {
				createIfNotNull(idLehrer, newFotoBase64);
			}
			return null;
		});
	}

	/**
	 * Aktualisiert ein vorhandenes Lehrerfoto oder löscht es.
	 * <p>
	 * Ist {@code newFotoBase64 == null}, wird das Foto gelöscht.
	 * Andernfalls wird der Inhalt nur dann aktualisiert, wenn er sich vom bisherigen unterscheidet.
	 *
	 * @param existing      das vorhandene {@link DTOLehrerFoto}
	 * @param newFotoBase64 der neue Base64-Inhalt, oder {@code null} zum Löschen
	 */
	private void updateOrDelete(final DTOLehrerFoto existing, final String newFotoBase64) {
		if (newFotoBase64 == null) {
			this.lehrerFotoRepository.delete(existing);
		} else if (!Strings.CS.equals(existing.fotoBase64, newFotoBase64)) {
			existing.fotoBase64 = newFotoBase64;
		}
	}

	/**
	 * Legt ein neues Lehrerfoto an, sofern {@code newFotoBase64} nicht {@code null} ist.
	 *
	 * @param idLehrer      die ID des Lehrers, für den das Foto angelegt werden soll
	 * @param newFotoBase64 der Base64-Inhalt des neuen Fotos, oder {@code null} — dann keine Aktion
	 */
	private void createIfNotNull(final long idLehrer, final String newFotoBase64) {
		if (newFotoBase64 == null) {
			return;
		}
		final var newEntity = new DTOLehrerFoto(idLehrer);
		newEntity.fotoBase64 = newFotoBase64;
		this.lehrerFotoRepository.create(newEntity);
	}

}
