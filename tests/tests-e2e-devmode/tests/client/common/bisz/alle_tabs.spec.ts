import { test, expect } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";
import { frontendURL } from '../../../../../utils/APIUtils'

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Nicht privilegierte Nutzer können nur entsprechende Bereiche im DEV Mode einsehen', async ({page}) => {
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();

	await expect(page.getByRole('heading', {name: 'Schüler'})).toBeVisible();
	await page.getByRole('cell', {name: 'Triebel'}).click();
	await expect(page.locator('header')).toContainText('ID: 1010');


	await expect(page.getByRole('button', {name: 'Individualdaten'})).toBeVisible();
	await page.getByRole('button', {name: 'Individualdaten'}).click();
	await expect(page.getByText('Staatsangehörigkeit und Konfession')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Sonstiges'})).toBeVisible();
	await page.getByRole('button', {name: 'Sonstiges'}).click();
	await expect(page.getByRole('button', {name: 'Vermerke'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Einwilligungen'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Lernplattformen'})).toBeVisible();
	await page.getByRole('button', {name: 'Vermerke'}).click();
	await expect(page.getByText('Nur sichtbare Vermerkarten anzeigen')).toBeVisible();
	await page.getByRole('button', {name: 'Einwilligungen'}).click();
	await expect(page.getByText('Nicht abgefragt')).toBeVisible();
	await page.getByRole('button', {name: 'Lernplattformen'}).click();
	await expect(page.getByText('Einwilligung Abgefragt')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Erziehungsberechtigte'})).toBeVisible();
	await page.getByRole('button', {name: 'Erziehungsberechtigte'}).click();
	await expect(page.getByText('Erziehungsberechtigte')).toBeVisible();
	await expect(page.getByText('Daten zu Caroline Triebel')).toBeVisible();

	// Test schlug fehl, weil Playwright ohne Timeout den Tabwechsel nicht registrieren konnte
	await page.waitForTimeout(300);
	await expect(page.getByRole('button', {name: 'Ausbildungsbetriebe'})).toBeVisible();
	await page.getByRole('button', {name: 'Ausbildungsbetriebe'}).click();
	await expect(page.getByText('Noch kein Schülerbetrieb vorhanden.')).toBeVisible();


	await expect(page.getByRole('button', {name: 'KAoA'})).toBeVisible();
	await page.getByRole('button', {name: 'KAoA'}).click();
	await expect(page.getByRole('button', {name: 'Neuer Eintrag'})).toBeVisible();


	await expect(page.getByRole('button', {name: 'Schulbesuch'})).toBeVisible();
	await page.getByRole('button', {name: 'Schulbesuch'}).click();
	await expect(page.getByText('Vor der Aufnahme besucht')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Lernabschnitte'})).toBeVisible();
	await page.getByRole('button', {name: 'Lernabschnitte'}).click();
	await expect(page.getByRole('button', {name: 'Allgemein'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Leistungsdaten', exact: true})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Versetzung/Abschluss'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Konferenz'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Zeugnisdruck'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Nachprüfung'})).toBeVisible();

	await page.getByRole('button', {name: 'Allgemein'}).click();
	await expect(page.getByText('Klassenlehrer')).toBeVisible();

	await page.getByRole('button', {name: 'Leistungsdaten'}).click();
	await expect(page.getByText('Fehlstunden (Summe)')).toBeVisible();

	await page.getByRole('button', {name: 'Versetzung/Abschluss'}).click();
	await expect(page.getByText('TODO: istAbschlussPrognose')).toBeVisible()

	await page.getByRole('button', {name: 'Konferenz'}).click();
	await expect(page.getByText('Abschnitt', {exact: true})).toBeVisible()

	await page.getByRole('button', {name: 'Zeugnisdruck'}).click();
	await expect(page.getByText('TODO: nur Anzeige - abschluss', {exact: true})).toBeVisible()

	await page.getByRole('button', {name: 'Nachprüfung'}).click();
	await expect(page.getByText(' Dieser Bereich ist noch in Entwicklung.')).toBeVisible()


	await expect(page.getByRole('button', {name: 'Sprachen'})).toBeVisible();
	await page.getByRole('button', {name: 'Sprachen'}).click();
	await expect(page.getByText('Sprachenfolge')).toBeVisible();
	await expect(page.getByText('Sprachprüfungen – Herkunftsprachlicher Unterricht')).toBeVisible();
	await expect(page.getByText('Sprachprüfungen – Feststellungsprüfungen')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Stundenplan'})).toBeVisible();
	await page.getByRole('button', {name: 'Stundenplan'}).click();
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Laufbahnplanung'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Abitur'})).toHaveCount(0);

});

test('Nicht privilegierte Nutzer können nur entsprechende Bereiche bei den Lehrern im DEV Mode sehen', async ({page}) => {
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();
	await page.getByRole('link', {name: 'Lehrkräfte'}).click();
	await expect(page.locator('header').first()).toContainText('ALBE');


	await expect(page.getByRole('button', {name: 'Individualdaten'})).toBeVisible();
	await page.getByRole('button', {name: 'Individualdaten'}).click();
	await expect(page.getByText('Wohnort und Kontaktdaten')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Stundenplan'})).toBeVisible();
	await page.getByRole('button', {name: 'Stundenplan'}).click();
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Unterricht'})).toBeVisible();
	await page.getByRole('button', {name: 'Unterricht'}).click();
	await expect(page.getByText('Unterrichtsdaten', {exact: true})).toBeVisible();

	await expect(page.getByRole('button', {name: 'Personaldaten'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Einwilligungen'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Lernplattformen'})).toHaveCount(0);
})

test('Nicht privilegierter User können Oberstufe nicht bearbeiten', async ({page}) => {
	test.setTimeout(60_000)
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();
	await expect(page.getByRole('link', {name: 'Oberstufe'})).not.toBeVisible();
});

test('Nicht privilegierter User können Einstellung nicht vornehmen', async ({page}) => {
	test.setTimeout(60_000)
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();
	await expect(page.getByRole('link', {name: 'Einstellung'})).not.toBeVisible();
});
