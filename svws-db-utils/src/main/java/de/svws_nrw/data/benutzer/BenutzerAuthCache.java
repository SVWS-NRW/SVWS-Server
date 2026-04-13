package de.svws_nrw.data.benutzer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;


/**
 * In-memory cache for authenticated user data to avoid repeated DB queries
 * for authentication on every REST request. Entries expire after a configurable TTL.
 *
 * This cache is keyed by (schema, username) and stores the password's SHA-256 hash
 * for fast credential verification on subsequent requests.
 */
public final class BenutzerAuthCache {

	/** Cache TTL in milliseconds (default: 5 minutes) */
	private static final long CACHE_TTL_MS = 5L * 60 * 1000;

	/** The cache storage */
	private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

	private BenutzerAuthCache() {
		throw new IllegalStateException("Instantiation of " + BenutzerAuthCache.class.getName() + " not allowed");
	}

	/**
	 * A cached authentication result containing all data needed to reconstruct
	 * an authenticated {@link de.svws_nrw.db.Benutzer} without DB queries.
	 */
	public record CacheEntry(
		String passwordSha256,
		String correctedUsername,
		Long userId,
		Long idLehrer,
		List<BenutzerKompetenz> kompetenzen,
		SchuleStammdaten stammdaten,
		Set<Long> klassenIDs,
		Set<LehrerLeitungsfunktion> leitungsfunktionen,
		Set<Integer> abiturjahrgaenge,
		long createdAt
	) {}

	/**
	 * Creates a cache key from schema and username (case-insensitive).
	 */
	private static String cacheKey(final String schema, final String username) {
		return schema + "\0" + username.toLowerCase(Locale.GERMAN);
	}

	/**
	 * Computes the SHA-256 hash of a password string for fast comparison.
	 */
	private static String hashPassword(final String password) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			final byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}

	/**
	 * Looks up a cached authentication entry. Returns the entry only if:
	 * - An entry exists for the given schema/username
	 * - The entry has not expired (TTL check)
	 * - The password's SHA-256 matches the cached hash
	 *
	 * @param schema    the database schema name
	 * @param username  the username
	 * @param password  the plaintext password to verify against cached hash
	 *
	 * @return the cached entry if found and valid, null otherwise
	 */
	public static CacheEntry get(final String schema, final String username, final String password) {
		if ((schema == null) || (username == null) || (password == null))
			return null;
		final String key = cacheKey(schema, username);
		final CacheEntry entry = cache.get(key);
		if (entry == null)
			return null;
		if (System.currentTimeMillis() - entry.createdAt > CACHE_TTL_MS) {
			cache.remove(key);
			return null;
		}
		if (!hashPassword(password).equals(entry.passwordSha256))
			return null;
		return entry;
	}

	/**
	 * Stores an authentication result in the cache. All collection parameters
	 * are defensively copied to ensure thread safety.
	 *
	 * @param schema              the database schema name
	 * @param username            the username (used for cache key)
	 * @param password            the plaintext password (SHA-256 is stored, not the plaintext)
	 * @param correctedUsername   the case-corrected username from the DB
	 * @param userId              the database ID of the user
	 * @param idLehrer            the teacher ID, or null
	 * @param kompetenzen         the user's competencies
	 * @param stammdaten          the school master data, or null
	 * @param klassenIDs          the class IDs for function-based access
	 * @param leitungsfunktionen  the leadership functions
	 * @param abiturjahrgaenge    the Abitur cohort years
	 */
	public static void put(final String schema, final String username, final String password,
			final String correctedUsername, final Long userId, final Long idLehrer,
			final List<BenutzerKompetenz> kompetenzen, final SchuleStammdaten stammdaten,
			final Collection<Long> klassenIDs, final Collection<LehrerLeitungsfunktion> leitungsfunktionen,
			final Collection<Integer> abiturjahrgaenge) {
		if ((schema == null) || (username == null) || (password == null))
			return;
		final String key = cacheKey(schema, username);
		cache.put(key, new CacheEntry(
				hashPassword(password),
				correctedUsername,
				userId,
				idLehrer,
				new ArrayList<>(kompetenzen),
				stammdaten,
				new HashSet<>(klassenIDs),
				new HashSet<>(leitungsfunktionen),
				new HashSet<>(abiturjahrgaenge),
				System.currentTimeMillis()
		));
	}

	/**
	 * Removes a cached entry for a specific schema and username.
	 *
	 * @param schema    the database schema name
	 * @param username  the username
	 */
	public static void invalidate(final String schema, final String username) {
		if ((schema != null) && (username != null))
			cache.remove(cacheKey(schema, username));
	}

	/**
	 * Clears all cached entries.
	 */
	public static void clear() {
		cache.clear();
	}

}
