package de.svws_nrw.service.schueler.foto;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.mapper.schueler.foto.SchuelerFotoMapper;
import de.svws_nrw.repo.schueler.foto.SchuelerFotoRepository;
import org.apache.commons.lang3.Strings;

public class SchuelerFotoService {

	private final SchuelerFotoRepository repo;
	private final SchuelerFotoMapper mapper;

	/**
	 * Erstellt eine neue Instanz des Services mit den erforderlichen Abhängigkeiten.
	 *
	 * @param repo   das zu verwendende {@link SchuelerFotoRepository}
	 * @param mapper der zu verwendende {@link SchuelerFotoMapper}
	 */
	public SchuelerFotoService(final SchuelerFotoRepository repo, final SchuelerFotoMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	/**
	 * Sucht ein Schülerfoto anhand der Schüler-ID.
	 *
	 * @param idSchueler die ID des Schülers
	 * @return ein {@link Optional} mit dem gefundenen {@link SchuelerFoto}, oder leer wenn nicht gefunden
	 */
	public Optional<SchuelerFoto> findByIdSchueler(final long idSchueler) {
		return this.repo.findById(idSchueler)
				.map(mapper::toDomain);
	}

	/**
	 * Gibt alle Schülerfotos für die angegebenen Schüler-IDs zurück.
	 *
	 * @param schuelerIds Liste der Schüler-IDs
	 * @return Liste der zugehörigen {@link SchuelerFoto}-Objekte
	 */
	public List<SchuelerFoto> getBySchuelerIds(final List<Long> schuelerIds) {
		return this.repo.findListByIds(schuelerIds)
				.stream()
				.map(this.mapper::toDomain)
				.toList();
	}

	/**
	 * Legt ein Schüler-Foto an, aktualisiert es oder löscht es — abhängig vom aktuellen Zustand
	 * und dem übergebenen Wert:
	 * <ul>
	 *   <li>Foto vorhanden + {@code newFotoBase64 == null} → Foto wird gelöscht</li>
	 *   <li>Foto nicht vorhanden + {@code newFotoBase64 == null} → keine Aktion</li>
	 *   <li>Foto nicht vorhanden + {@code newFotoBase64 != null} → Foto wird angelegt</li>
	 *   <li>Foto vorhanden + Inhalt geändert → Foto wird aktualisiert</li>
	 *   <li>Foto vorhanden + Inhalt unverändert → keine Aktion</li>
	 * </ul>
	 *
	 * @param idSchueler    die ID des Schülers
	 * @param newFotoBase64 das neue Foto in Base64-Kodierung, oder {@code null} zum Löschen
	 */
	public void upsertOrDelete(final long idSchueler, final String newFotoBase64) {
		TransactionSupport.transactional(() -> {
			final var existing = this.repo.findById(idSchueler);
			if (existing.isPresent()) {
				updateOrDelete(existing.get(), newFotoBase64);
			} else {
				createIfNotNull(idSchueler, newFotoBase64);
			}
			return null;
		});
	}

	private void updateOrDelete(final DTOSchuelerFoto existing, final String newFotoBase64) {
		if (newFotoBase64 == null) {
			this.repo.delete(existing);
		} else if (!Strings.CS.equals(existing.fotoBase64, newFotoBase64)) {
			existing.fotoBase64 = newFotoBase64;
		}
	}

	private void createIfNotNull(final long idSchueler, final String newFotoBase64) {
		if (newFotoBase64 == null) {
			return;
		}
		final var newEntity = new DTOSchuelerFoto(idSchueler);
		newEntity.fotoBase64 = newFotoBase64;
		this.repo.create(newEntity);
	}

}
