package de.svws_nrw.oauth.internal;


import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit-Tests fuer {@link AccessToken}.
 */
class AccessTokenTest {

	@Test
	@DisplayName("<init> | defaults token type to Bearer when null")
	void constructorDefaultsTokenTypeToBearerWhenNull() {
		final Instant expiresAt = Instant.parse("2030-01-01T00:00:00Z");
		final AccessToken token = new AccessToken("token-value", null, expiresAt);
		assertEquals("Bearer", token.tokenType());
	}

	@Test
	@DisplayName("<init> | defaults token type to Bearer when blank")
	void constructorDefaultsTokenTypeToBearerWhenBlank() {
		final Instant expiresAt = Instant.parse("2030-01-01T00:00:00Z");
		final AccessToken token = new AccessToken("token-value", " \t", expiresAt);
		assertEquals("Bearer", token.tokenType());
	}

	@Test
	@DisplayName("asAuthorizationHeader | returns Bearer header format")
	void asAuthorizationHeaderReturnsBearerHeaderFormat() {
		final Instant expiresAt = Instant.parse("2030-01-01T00:00:00Z");
		final AccessToken token = new AccessToken("abc123", "Bearer", expiresAt);
		assertEquals("Bearer abc123", token.asAuthorizationHeader());
	}

	@Test
	@DisplayName("of | sets expiresAt based on now plus expiresInSeconds")
	void ofSetsExpiresAtBasedOnNowPlusExpiresInSeconds() {
		final Instant now = Instant.parse("2026-06-18T12:00:00Z");
		final AccessToken token = AccessToken.of("v", "Bearer", 90, now);
		assertEquals(Instant.parse("2026-06-18T12:01:30Z"), token.expiresAt());
	}

	@Test
	@DisplayName("isValidAt | returns true when clock instant is before expiresAt")
	void isValidAtReturnsTrueWhenClockInstantIsBeforeExpiresAt() {
		final Instant expiresAt = Instant.parse("2026-06-18T12:00:10Z");
		final AccessToken token = new AccessToken("v", "Bearer", expiresAt);
		final Clock clock = Clock.fixed(Instant.parse("2026-06-18T12:00:09Z"), ZoneOffset.UTC);
		assertTrue(token.isValidAt(clock));
	}

	@Test
	@DisplayName("isValidAt | returns false when clock instant is after expiresAt")
	void isValidAtReturnsFalseWhenClockInstantIsAfterExpiresAt() {
		final Instant expiresAt = Instant.parse("2026-06-18T12:00:10Z");
		final AccessToken token = new AccessToken("v", "Bearer", expiresAt);
		final Clock clock = Clock.fixed(Instant.parse("2026-06-18T12:00:11Z"), ZoneOffset.UTC);
		assertFalse(token.isValidAt(clock));
	}

	@Test
	@DisplayName("<init> | throws when value is null")
	void constructorThrowsWhenValueIsNull() {
		final Instant expiresAt = Instant.parse("2030-01-01T00:00:00Z");
		assertThrows(NullPointerException.class, () -> new AccessToken(null, "Bearer", expiresAt));
	}

	@Test
	@DisplayName("<init> | throws when expiresAt is null")
	void constructorThrowsWhenExpiresAtIsNull() {
		assertThrows(NullPointerException.class, () -> new AccessToken("v", "Bearer", null));
	}
}
