import { test, expect } from '@playwright/test';

test.describe("navigation", () => {

	test("main navigation", async ({ page }) => {
		await page.goto('/');
		await Promise.all([
			page.getByText('Anmelden').click()
		]);
		await page.goto('#/schueler/9300/daten');

		const a = await page.getByLabel('Rufname').inputValue();
		expect(a).toContain("Dominik");

		await page.getByLabel('Aufnahmedatum').fill('2018-08-24');

		await page.locator('a:has-text("Lehrkräfte")').click();
		await page.waitForURL('#/lehrkraefte/2/daten');
		await page.locator('text=Berg').nth(1).click();
		const b = await page.getByLabel('private E-Mail-Adresse').inputValue();
		expect(b).toContain("Berg");
	});

});
