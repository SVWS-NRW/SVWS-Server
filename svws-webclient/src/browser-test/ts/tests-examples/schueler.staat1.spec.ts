import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
	await page.goto('/');
	await page.getByRole('button', { name: 'Anmelden' }).click();
	await page.getByRole('row', { name: 'EF Addens Sophie' }).getByText('Addens').click();
	await page.getByLabel('Alle Vornamen').click();
	await page.getByLabel('Alle Vornamen').fill('Test');
	await page.locator('div:nth-child(3) > .mt-4 > .input-wrapper > div > .multiselect-input-component > .dropdown-icon > .icon > .pt-0\\.5').first().click();
	await page.getByRole('option', { name: 'der Demokratischen Volksrepublik Korea' }).click();
});
