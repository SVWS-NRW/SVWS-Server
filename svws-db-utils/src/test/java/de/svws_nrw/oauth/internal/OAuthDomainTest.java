package de.svws_nrw.oauth.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("OAuthDomain")
class OAuthDomainTest {

	@ParameterizedTest(name = "{0}")
	@EnumSource(OAuthDomain.class)
	@DisplayName("fromName | liefert die Domaene zum Namen")
	void fromNameBekannt(final OAuthDomain domain) {
		assertThat(OAuthDomain.fromName(domain.name())).contains(domain);
	}

	@ParameterizedTest(name = "name = ''{0}''")
	@NullSource
	@ValueSource(strings = {"", "UNBEKANNT", "it_nrw"})
	@DisplayName("fromName | leeres Optional bei unbekanntem oder fehlendem Namen")
	void fromNameUnbekannt(final String name) {
		assertThat(OAuthDomain.fromName(name)).isEmpty();
	}
}
