import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
	await page.goto('/');
	await page.getByRole('button', { name: 'Anmelden' }).click();
	await page.getByRole('link', { name: 'Lehrkräfte' }).click();
	await page.getByText('Anders').click();
	await page.getByRole('button', { name: 'Personaldaten' }).click();
	await page.getByRole('button', { name: 'Unterricht' }).click();
	await page.getByRole('button', { name: 'Individualdaten', exact: true }).click();
});