import { test, expect } from '@playwright/test';

test.describe("navigation", () => {
	test("main navigation", async ({ page }) => {
		await page.goto('/');
		await Promise.all([
			page.getByText('Anmelden').click()
		]);
		const a = await page.getByLabel('Rufname').inputValue();
		expect(a).toContain("Lisa");

		await page.getByRole('button', { name: 'Erziehungsberechtigte' }).click();
		await page.waitForURL('#/schueler/9311/erziehungsberechtigte');

		await page.getByRole('button', { name: 'Ausbildungsbetriebe' }).click();
		await page.waitForURL('#/schueler/9311/ausbildungsbetriebe');

		await page.getByRole('button', { name: 'Schulbesuch' }).click();
		await page.waitForURL('#/schueler/9311/schulbesuch');

		await page.getByRole('button', { name: 'Lernabschnitte' }).click();
		await page.waitForURL('#/schueler/9311/abschnitt/1');

		await page.getByRole('button', { name: 'Leistungsdaten' }).click();
		await page.waitForURL('#/schueler/9311/leistungsdaten/1');

		//await page.getByRole('button', { name: 'Stundenplan' }).click();
		//await page.waitForURL('#/schueler/9311/stundenplan/1');

		await page.getByRole('button', { name: 'Individualdaten' }).click();
		await page.waitForURL('#/schueler/9311/daten');
	});
});

