import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis } from '../helpers/navigation';

// Bekannt NICHT verfügbar in GymAbiLite — direkte URL führt zu /error:
// Noten-Katalog (/schule/noten), Teilstandorte (/schule/teilstandorte).
// Bekannt: leiten in GymAbiLite still auf /schueler um, sind dort also keine
// echten Katalog-Ansichten: Schwerpunkte (/schule/schwerpunkte),
// Beschäftigungsarten (/schule/beschaeftigungsarten), Kindergärten
// (/schule/kindergaerten). Alle fünf sind deshalb bewusst nicht in KATALOGE
// enthalten — in dieser Umgebung nicht sinnvoll testbar, kein toter
// test.skip()-Platzhalter.
const KATALOGE = [
	{ name: 'Vermerkarten', path: '/schule/vermerkarten' },
	{ name: 'Telefonarten', path: '/schule/telefonarten' },
	{ name: 'Erzieherarten', path: '/schule/erzieherarten' },
	{ name: 'Entlassgründe', path: '/schule/entlassgruende' },
	{ name: 'Fahrschülerarten', path: '/schule/fahrschuelerarten' },
	{ name: 'Haltestellen', path: '/schule/haltestellen' },
	{ name: 'Förderschwerpunkte', path: '/schule/foerderschwerpunkte' },
	{ name: 'Betriebsarten', path: '/schule/betriebsarten' },
	{ name: 'Einwilligungsarten', path: '/schule/einwilligungsarten' },
	{ name: 'Jahrgänge', path: '/schule/jahrgaenge' },
	{ name: 'Konfessionen', path: '/schule/konfessionen' },
	{ name: 'Ankreuzkompetenzen', path: '/schule/ankreuzkompetenzen' },
	{ name: 'Abteilungen', path: '/schule/abteilungen' },
	{ name: 'Betriebe', path: '/schule/betriebe' },
	{ name: 'Floskeln', path: '/schule/floskeln' },
	{ name: 'Floskelgruppen', path: '/schule/floskelgruppen' },
	{ name: 'Lernplattformen', path: '/schule/lernplattformen' },
	{ name: 'Schulen', path: '/schule/schulen' },
	{ name: 'Orte', path: '/schule/orte' },
	{ name: 'Ortsteile', path: '/schule/ortsteile' },
	{ name: 'Leitungsfunktionen', path: '/schule/leitungsfunktionen' },
	{ name: 'Teilleistungsarten', path: '/schule/teilleistungsarten' },
	{ name: 'Fächer', path: '/schule/faecher' },
] as const;

test.describe('Kataloge — Smoke', () => {
	for (const { name, path } of KATALOGE) {
		const pfadSegment = path.replace(/^\//, '');

		// H1 ist bei Katalog-Routen durchgängig nur generisch "Schule" und
		// differenziert nichts Route-Spezifisches. Der URL-Segment-Check ist
		// pro Katalog eindeutig und bleibt daher die einzige Assertion neben
		// dem Fehler-/Login-Check.
		test(`Katalog ${name} öffnet ohne Fehler @smoke`, async ({ page }) => {
			await login(page);
			const errors = collectErrors(page);

			await page.goto(`${getAufgeloesteBasis(page)}${path}`, { waitUntil: 'networkidle' });

			await expect(page).not.toHaveURL(/\/error/);
			await expect(page).not.toHaveURL(/\/login/);
			await expect(page).toHaveURL(new RegExp(pfadSegment));

			expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
		});
	}

	// Die Zielüberschrift verhindert Zählungen während des Übergangs von der Login-Startseite.
	test('Vermerkartensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/vermerkarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Vermerkarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/vermerkarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Vermerkartenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/vermerkarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Vermerkarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/vermerkarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Telefonartensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/telefonarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Telefonarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/telefonarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Telefonartenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/telefonarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Telefonarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/telefonarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Erzieherartensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/erzieherarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Erzieherarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/erzieherarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Erzieherartenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/erzieherarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Erzieherarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/erzieherarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Entlassgründesuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/entlassgruende`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Entlassgründe', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/entlassgruende\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Entlassgründefilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/entlassgruende`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Entlassgründe', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/entlassgruende\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Fahrschülerartenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/fahrschuelerarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Fahrschülerarten', exact: true })).toBeVisible({ timeout: 8000 });

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Haltestellenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/haltestellen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Haltestellen', exact: true })).toBeVisible({ timeout: 8000 });

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Förderschwerpunktesuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/foerderschwerpunkte`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Förderschwerpunkte', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/foerderschwerpunkte\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Förderschwerpunktefilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/foerderschwerpunkte`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Förderschwerpunkte', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/foerderschwerpunkte\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Betriebsartensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/betriebsarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Betriebsarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/betriebsarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Betriebsartenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/betriebsarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Betriebsarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/betriebsarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Einwilligungsartensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/einwilligungsarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Einwilligungsarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/einwilligungsarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Einwilligungsartenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/einwilligungsarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Einwilligungsarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/einwilligungsarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Konfessionensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/konfessionen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Konfessionen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/konfessionen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Konfessionenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/konfessionen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Konfessionen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/konfessionen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Ankreuzkompetenzenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/ankreuzkompetenzen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Ankreuzkompetenzen', exact: true })).toBeVisible({ timeout: 8000 });

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		const vorher = await filterPanel.locator('[role="combobox"]').count();

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(600);

		const nachher = await filterPanel.locator('[role="combobox"]').count();
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Abteilungensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/abteilungen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Abteilungen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/abteilungen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('1');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Abteilungenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/abteilungen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Abteilungen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/abteilungen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Betriebefilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/betriebe`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Betriebe', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/betriebe\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Floskelnsuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/floskeln`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Floskel', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/floskeln\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('s');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Floskelnfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/floskeln`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Floskel', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/floskeln\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		const vorher = await filterPanel.locator('[role="combobox"]').count();

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(600);

		const nachher = await filterPanel.locator('[role="combobox"]').count();
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Floskelgruppensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/floskelgruppen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Floskelgruppe', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/floskelgruppen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Lernplattformensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/lernplattformen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Lernplattformen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/lernplattformen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schulensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/schulen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Schulen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/schulen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schulenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/schulen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Schulen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/schulen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Ortesuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/orte`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Orte', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/orte\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Ortefilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/orte`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Orte', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/orte\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Ortsteilesuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/ortsteile`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Ortsteile', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/ortsteile\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Ortsteilefilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/ortsteile`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Ortsteile', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/ortsteile\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Leitungsfunktionensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/leitungsfunktionen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Leitungsfunktionen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/leitungsfunktionen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('e');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Leitungsfunktionenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/leitungsfunktionen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Leitungsfunktionen', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/leitungsfunktionen\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Teilleistungsartensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/teilleistungsarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Teilleistungsarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/teilleistungsarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Teilleistungsartenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/teilleistungsarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Teilleistungsarten', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/teilleistungsarten\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Fächersuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/faecher`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Fächer', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/faecher\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Fächerfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/faecher`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Fächer', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/faecher\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Jahrgängesuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/jahrgaenge`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Jahrgänge', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/jahrgaenge\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('e');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Jahrgängefilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/jahrgaenge`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Jahrgänge', exact: true })).toBeVisible({ timeout: 8000 });

		// Auf die konkrete Detailansicht warten, bevor Suche/Filter bedient werden.
		await page.waitForURL(/\/schule\/jahrgaenge\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		// Live geprüft: keine Comboboxen; der Filter-Button blendet Filtertext aus/ein.
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter', exact: true }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Belegungsstand des Katalogs unterscheidet sich zwischen Umgebungen
	// (TestDB_GY: leer; GymAbi01 in der Pipeline: hat Einträge) — eine
	// "0 Einträge"-Annahme ist deshalb nicht umgebungsunabhängig haltbar.
	// Geprüft wird nur noch, dass die Seite lädt und das Suchfeld sichtbar
	// ist, nicht mehr der konkrete Leer-/Befüllt-Zustand.
	test('Fahrschülerartenseite lädt mit sichtbarem Suchfeld @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/fahrschuelerarten`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Fahrschülerarten', exact: true })).toBeVisible({ timeout: 8000 });

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Siehe Fahrschülerarten oben: Belegungsstand unterscheidet sich je
	// Umgebung, daher keine Leer-/Befüllt-Annahme mehr.
	test('Haltestellenseite lädt mit sichtbarem Suchfeld @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/haltestellen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Haltestellen', exact: true })).toBeVisible({ timeout: 8000 });

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Ankreuzkompetenzensuche bedient eine leere Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/ankreuzkompetenzen`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Ankreuzkompetenzen', exact: true })).toBeVisible({ timeout: 8000 });

		// Live in TestDB_GY: keine Einträge. Eine kleinere Trefferliste ist hier nicht möglich.
		const leererZustand = page.getByText('0 Einträge', { exact: true });
		await expect(leererZustand).toBeVisible({ timeout: 8000 });

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();
		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('a');
		await expect(suchfeld).toHaveValue('a');
		await page.waitForTimeout(500);
		await expect(leererZustand).toBeVisible();
		await expect(zeilen).toHaveCount(vorher);

		await suchfeld.fill('');
		await expect(suchfeld).toHaveValue('');
		await expect(leererZustand).toBeVisible();
		await expect(zeilen).toHaveCount(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Katalogumfang unterscheidet sich zwischen Umgebungen (TestDB_GY: 1
	// Betrieb; GymAbi01 in der Pipeline: mehrere Dutzend) — der Suchbegriff
	// "test" matcht in keiner der beiden Umgebungen einen echten Namen, die
	// Liste soll also stabil bleiben. Live in der Pipeline schwankt die
	// gerenderte Zeilenzahl dabei um bis zu 1 (Lazy-Loading/Rendering-Timing
	// bei größeren Listen) — die Prüfung toleriert das deshalb um -1, statt
	// exakte Gleichheit zu verlangen.
	test('Betriebesuche bleibt bei unverändertem Katalog stabil @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/betriebe`, { waitUntil: 'networkidle' });
		await expect(page.getByRole('heading', { name: 'Betriebe', exact: true })).toBeVisible({ timeout: 8000 });

		await page.waitForURL(/\/schule\/betriebe\/[^/]+\/daten$/, { timeout: 8000 });
		await page.waitForLoadState('networkidle');

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();
		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('test');
		await expect(suchfeld).toHaveValue('test');
		await page.waitForTimeout(500);
		expect(await zeilen.count()).toBeGreaterThanOrEqual(vorher - 1);

		await suchfeld.fill('');
		await expect(suchfeld).toHaveValue('');
		await page.waitForTimeout(500);
		expect(await zeilen.count()).toBeGreaterThanOrEqual(vorher - 1);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});
});
