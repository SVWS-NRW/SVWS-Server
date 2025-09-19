import { test, expect } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";
import { frontendURL } from '../../../../../utils/APIUtils'

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Admins können entsprechende Bereiche im STABLE Mode einsehen', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();

	await expect(page.getByRole('heading', { name: 'Schüler' })).toBeVisible();
	await page.getByRole('cell', { name: 'Triebel' }).click();
	await page.waitForURL('**/#/**/schueler/1010/daten', { timeout: 30_000 });
	await expect(page.locator('header')).toContainText('ID: 1010');


	await expect(page.getByRole('button', { name: 'Individualdaten' })).toBeVisible();
	await page.getByRole('button', { name: 'Individualdaten' }).click();
	await expect(page.getByText('Staatsangehörigkeit und Konfession')).toBeVisible();
	await expect(page.getByText('Migrationshintergrund', { exact: true })).toBeVisible();
	await expect(page.getByRole('combobox', { name: 'Fahrschüler' })).toBeVisible();
	expect(await page.getByRole('combobox', { name: 'Konfession' }).inputValue()).toBe("evangelisch");


	await expect(page.getByRole('button', { name: 'Lernabschnitte' })).toBeVisible();
	await page.getByRole('button', { name: 'Lernabschnitte' }).click();
	await page.waitForURL('**/#/**/schueler/1010/lernabschnitt/**/**/leistungen', { timeout: 30_000 });
	await expect(page.getByRole('button', { name: 'Allgemein' })).toBeVisible();
	await expect(page.getByRole('button', { name: 'Leistungsdaten', exact: true })).toBeVisible();
	await expect(page.getByRole('button', { name: 'Klausuren' })).toBeVisible();
	await expect(page.getByRole('button', { name: 'Versetzung/Abschluss' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Konferenz' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Zeugnisdruck' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Nachprüfung' })).toHaveCount(0);

	await page.getByRole('button', { name: 'Allgemein' }).click();
	await page.waitForURL('**/#/**/schueler/1010/lernabschnitt/**/**/allgemein', { timeout: 30_000 });
	await expect(page.getByText('Klassenlehrer')).toBeVisible();

	await page.getByRole('button', { name: 'Leistungsdaten' }).click();
	await page.waitForURL('**/#/**/schueler/1010/lernabschnitt/**/**/leistungen', { timeout: 30_000 });
	await expect(page.getByText('Fehlstunden (Summe)')).toBeVisible();

	await page.getByRole('button', { name: 'Klausuren' }).click();
	await page.waitForURL('**/#/**/schueler/1010/lernabschnitt/**/**/gostklausuren', { timeout: 30_000 });
	await expect(page.getByText('Abschnitt', { exact: true })).toBeVisible();


	await expect(page.getByRole('button', { name: 'Sprachen' })).toBeVisible();
	await page.getByRole('button', { name: 'Sprachen' }).click();
	await page.waitForURL('**/#/**/schueler/1010/sprachen', { timeout: 30_000 });
	await expect(page.getByText('Sprachenfolge')).toBeVisible();
	await expect(page.getByText('Sprachprüfungen – Herkunftsprachlicher Unterricht')).toBeVisible();
	await expect(page.getByText('Sprachprüfungen – Feststellungsprüfungen')).toBeVisible()


	await expect(page.getByRole('button', { name: 'Laufbahnplanung' })).toBeVisible();
	await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	await page.waitForURL('**/#/**/schueler/1010/laufbahnplanung', { timeout: 30_000 });
	await expect(page.getByText('Belegprüfungsergebnisse')).toBeVisible();
	await expect(page.getByText('Wochenstunden')).toBeVisible();
	await expect(page.getByText('Projektkurs Informatik')).toBeVisible();


	await expect(page.getByRole('button', { name: 'Stundenplan' })).toBeVisible();
	await page.getByRole('button', { name: 'Stundenplan' }).click();
	await page.waitForURL('**/#/**/schueler/1010/stundenplan/**', { timeout: 30_000 });
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();


	await expect(page.getByRole('button', { name: 'Sonstiges' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Erziehungsberechtigte' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Ausbildungsbetriebe' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'KAoA' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Schulbesuch' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Abitur' })).toHaveCount(0);
});

test('Admins können entsprechende Bereiche bei den Lehrern im STABLE Mode sehen', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();
	await page.getByRole('link', { name: 'Lehrkräfte' }).click();
	await expect(page.locator('header').first()).toContainText('ALBE');

	await expect(page.getByRole('button', { name: 'Individualdaten' })).toBeVisible();
	await page.getByRole('button', { name: 'Individualdaten' }).click();
	await expect(page.getByText('Wohnort und Kontaktdaten')).toBeVisible();


	await expect(page.getByRole('button', { name: 'Stundenplan' })).toBeVisible();
	await page.getByRole('button', { name: 'Stundenplan' }).click();
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();


	await expect(page.getByRole('button', { name: 'Personaldaten' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Unterricht' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Einwilligungen' })).toHaveCount(0);
	await expect(page.getByRole('button', { name: 'Lernplattform' })).toHaveCount(0);
})

test('Admins können Oberstufe bearbeiten', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();
	await expect(page.getByRole('link', { name: 'Oberstufe' })).toBeVisible();
});

test('Admins können Einstellung vornehmen', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();
	await expect(page.getByRole('link', { name: 'Einstellungen' })).toBeVisible();
});
