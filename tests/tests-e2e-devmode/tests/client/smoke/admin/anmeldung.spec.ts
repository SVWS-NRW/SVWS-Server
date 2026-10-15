/**
 * Anmeldung — Layer 1 Smoke-Tests
 */

import { expect, test } from '@playwright/test';

import { login } from '../helpers/auth';
import { ENVIRONMENTS, getBaseUrl, getEnv } from '../kataloge.config';

// Docker-Container liefert ein Self-Signed-Zertifikat aus — gleiches Muster
// wie im übrigen Repo (siehe z.B. root/basic_smoketest.spec.ts,
// bisz/alle_tabs.spec.ts, gruppenprozesse/admin/*.spec.ts). Diese Datei
// importiert `test` direkt aus '@playwright/test' statt aus
// '../helpers/fixtures', braucht den Override deshalb hier separat.
test.use({
	ignoreHTTPSErrors: true,
});

test.describe('Anmeldung — Smoke', () => {
	test('Login-Seite zeigt Schema-Dropdown @smoke', async ({ page }) => {
		await page.goto(getBaseUrl(), { waitUntil: 'networkidle', timeout: 60000 });
		const schemaDropdown = page.getByLabel('Datenbank-Schema');
		await expect(schemaDropdown).toBeVisible({ timeout: 10000 });
		await expect(page).not.toHaveURL(/\/error/);
	});

	// getEnv() liefert bei gesetztem VITE_targetHost 'docker' (Schema
	// GymAbi01), obwohl real gegen den in VITE_targetHost benannten Host/
	// Schema navigiert wird. Schema wird deshalb bevorzugt aus VITE_targetHost
	// selbst gelesen (Hash-Segment nach '#/'), Fallback auf ENVIRONMENTS nur
	// ohne gesetztes VITE_targetHost.
	test('Anmeldung mit korrekten Daten leitet weiter @nightly-only @smoke', async ({ page }) => {
		await login(page);
		const schema = process.env.VITE_targetHost?.match(/#\/([^/]+)/)?.[1]
      ?? ENVIRONMENTS[getEnv()].schema;
		await expect(page).toHaveURL(new RegExp(schema));
		await expect(page).not.toHaveURL(/\/error/);
	});

	// Die Landing-Seite nach Login ist nicht fix (hängt von der zuletzt
	// bekannten Route ab), daher keine erratene Ziel-Überschrift — stattdessen
	// zwei konkrete, prüfbare Belege: eine echte, NICHT-leere H1-Überschrift
	// UND eine tatsächlich befüllte Sidebar-Navigation.
	test('Nach Anmeldung ist App-Inhalt sichtbar @smoke', async ({ page }) => {
		await login(page);
		await expect(page).not.toHaveURL(/\/error/);

		const heading = page.locator('main h1, h1').first();
		await expect(heading, 'Nach Login muss eine echte Seitenüberschrift sichtbar sein').toBeVisible({ timeout: 10000 });
		const headingText = (await heading.textContent())?.trim() ?? '';
		expect(headingText.length, 'Überschrift darf nicht leer sein').toBeGreaterThan(0);

		const navLinks = page.locator('a.sidebar--menu-item');
		await expect(navLinks.first(), 'Sidebar-Navigation muss nach Login sichtbar sein').toBeVisible({ timeout: 10000 });
	});
});
