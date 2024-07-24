package de.svws_nrw.data;

import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DataManagerRevisedTest {

	@InjectMocks
	private DataKlassendaten cut;

	@Mock
	private DBEntityManager dbEntityManager;


	@Test
	void constructor_DBEntityManagerIsNull() {
		final Throwable result = catchThrowable(() -> new DataManagerRevised<>(null) {
			@Override
			protected Object map(final Object o) {
				return null;
			}
		});

		assertThat(result).isInstanceOf(NullPointerException.class).hasMessage("DBEntityManager is required");
	}

	@Test
	void constructor_ClassDTOKlassen() {
		final DBEntityManager connMock = mock(DBEntityManager.class);
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> result = new DataManagerRevised<>(connMock) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		assertThat(result).isNotNull().hasFieldOrPropertyWithValue("classDatabaseDTO", DTOKlassen.class);
	}

	@Test
	void setAttributesRequiredOnCreation_Initial() {
		cut.setAttributesRequiredOnCreation("bla", "test");

		assertThat(cut).extracting("attributesRequiredOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla", "test");
	}

	@Test
	void setAttributesRequiredOnCreation_Reset() {
		cut.setAttributesRequiredOnCreation("bla", "test");
		cut.setAttributesRequiredOnCreation("bla2", "test2", "test3");
		assertThat(cut).extracting("attributesRequiredOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla2", "test2", "test3");
	}

	@Test
	void setAttributesNotPatchable_Initial() {
		cut.setAttributesNotPatchable("bla", "test");

		assertThat(cut).extracting("attributesNotPatchable", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla", "test");
	}

	@Test
	void setAttributesNotPatchable_Reset() {
		cut.setAttributesNotPatchable("bla", "test");
		cut.setAttributesNotPatchable("bla2", "test2", "test3");
		assertThat(cut).extracting("attributesNotPatchable", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla2", "test2", "test3");
	}

	@Test
	void setAttributesDelayedOnCreation_Initial() {
		cut.setAttributesDelayedOnCreation("bla", "test");

		assertThat(cut).extracting("attributesDelayedOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla", "test");
	}

	@Test
	void setAttributesDelayedOnCreation_Reset() {
		cut.setAttributesDelayedOnCreation("bla", "test");
		cut.setAttributesDelayedOnCreation("bla2", "test2", "test3");
		assertThat(cut).extracting("attributesDelayedOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla2", "test2", "test3");
	}

}
