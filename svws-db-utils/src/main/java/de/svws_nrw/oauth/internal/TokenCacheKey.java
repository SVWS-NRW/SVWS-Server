package de.svws_nrw.oauth.internal;

import de.svws_nrw.oauth.OAuthScope;
import de.svws_nrw.oauth.Schema;

/**
 * Schluessel fuer den Token-Cache in {@link CachingTokenProvider}, bestehend aus
 * {@code (schema, domain, scope)}.
 *
 * @param schema DB-Schema / Mandant
 * @param domain die OAuth-Domaene
 * @param scope  OAuth-Scope
 */
public record TokenCacheKey(Schema schema, OAuthDomain domain, OAuthScope scope) {
}
