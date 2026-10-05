package de.svws_nrw.core.types.schule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("Diese Testklasse testet die Enum-Klasse StatusSchulwechselErfolgreicheBewerbung")
@ExtendWith(MockitoExtension.class)
class StatusSchulwechselErfolgreicheBewerbungTest {

	@Test
	@DisplayName("getId | gibt die korrekte Id zurück")
	void testGetId() {
		assertEquals(1, StatusSchulwechselErfolgreicheBewerbung.NEU.getId());
		assertEquals(2, StatusSchulwechselErfolgreicheBewerbung.AUFGENOMMEN.getId());
	}

	@Test
	@DisplayName("getBezeichnung | gibt die korrekte Bezeichnung zurück")
	void testGetBezeichnung() {
		assertEquals("neu", StatusSchulwechselErfolgreicheBewerbung.NEU.getBezeichnung());
		assertEquals("aufgenommen", StatusSchulwechselErfolgreicheBewerbung.AUFGENOMMEN.getBezeichnung());
	}

	@Test
	@DisplayName("getById | gibt den korrekten Status zurück")
	void testGetById() {
		assertSame(StatusSchulwechselErfolgreicheBewerbung.NEU, StatusSchulwechselErfolgreicheBewerbung.getByIdOrNull(1));
		assertSame(StatusSchulwechselErfolgreicheBewerbung.AUFGENOMMEN, StatusSchulwechselErfolgreicheBewerbung.getByIdOrNull(2));
	}

	@Test
	@DisplayName("getById | unbekannte Id gibt null zurück")
	void testGetByIdUnknown() {
		assertNull(StatusSchulwechselErfolgreicheBewerbung.getByIdOrNull(-1));
	}

	@Test
	@DisplayName("getByBezeichnung | gibt den korrekten Status zurück")
	void testGetByBezeichnung() {
		assertSame(StatusSchulwechselErfolgreicheBewerbung.NEU, StatusSchulwechselErfolgreicheBewerbung.getByBezeichnungOrNull("neu"));
		assertSame(StatusSchulwechselErfolgreicheBewerbung.AUFGENOMMEN, StatusSchulwechselErfolgreicheBewerbung.getByBezeichnungOrNull("aufgenommen"));
	}

	@Test
	@DisplayName("getByBezeichnung | unbekannte Bezeichnung gibt null zurück")
	void testGetByBezeichnungUnknown() {
		assertNull(StatusSchulwechselErfolgreicheBewerbung.getByBezeichnungOrNull("unbekannt"));
	}

	@Test
	@DisplayName("toString | gibt die Bezeichnung zurück")
	void testToString() {
		assertEquals("neu", StatusSchulwechselErfolgreicheBewerbung.NEU.toString());
		assertEquals("aufgenommen", StatusSchulwechselErfolgreicheBewerbung.AUFGENOMMEN.toString());
	}

}
