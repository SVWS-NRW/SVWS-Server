package de.svws_nrw.repo.schule.kataloge.floskegruppe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import de.svws_nrw.repo.schule.kataloge.floskelgruppe.FloskelgruppeRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;

@ExtendWith(MockitoExtension.class)
class FloskelgruppeRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FloskelgruppeRepositoryImpl repository;

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final long id = 42L;
		final String kuerzel = "Kürzel";
		final String bezeichnungen = "Bezeichnung";
		final DTOFloskelgruppen entity = new DTOFloskelgruppen(id, kuerzel, bezeichnungen);

		when(conn.queryByKey(DTOFloskelgruppen.class, id)).thenReturn(entity);

		final DTOFloskelgruppen result = repository.getById(id);

		assertNotNull(result);
		assertEquals(kuerzel, result.Kuerzel);
		assertEquals(bezeichnungen, result.Bezeichnung);
		verify(conn).queryByKey(DTOFloskelgruppen.class, id);
	}


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOFloskelgruppen k = new DTOFloskelgruppen(999L, "Kürzel", "Bezeichnung");
		when(conn.queryAll(DTOFloskelgruppen.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOFloskelgruppen.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
