package de.svws_nrw.utils;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mockStatic;

/**
 * Basis Klasse für mocked DbConnectionProvider (perspektivisch evtl extension?)
 */
@ExtendWith(MockitoExtension.class)
public abstract class DbConnectionProviderStaticMockTest {

	/**
	 * Mocked conn
	 */
	@Mock
	protected DBEntityManager conn;

	/**
	 * Mocked DbConnectionProvider
	 */
	protected MockedStatic<DbConnectionProvider> dbConnectionProviderMock;

	@BeforeEach
	final void setUpDbConnectionProviderMock() {
		dbConnectionProviderMock = mockStatic(DbConnectionProvider.class);
		dbConnectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
	}

	@AfterEach
	final void tearDownDbConnectionProviderMock() {
		dbConnectionProviderMock.close();
	}
}
