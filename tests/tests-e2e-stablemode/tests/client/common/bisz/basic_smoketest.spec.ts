import { test, expect } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";
import { frontendURL } from "../../../../../utils/APIUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Smoke-Test - Basic', async ({ page }) => {
	const { loginBISZ, logout } = useLoginUtils(targetHost, page);

	await loginBISZ();

	// Default App ist Schüler
	await expect(page.locator('header')).toContainText('Eleonora Externa');

	await page.getByRole('link', { name: 'Schule' }).click();
	await expect(page.locator('header')).toContainText('Albert Zweistein Gymnasium');
	await page.getByRole('link', { name: 'Schüler', exact: true }).click();
	await expect(page.locator('header')).toContainText('Eleonora Externa');
	await page.getByRole('link', { name: 'Lehrkräfte' }).click();
	await expect(page.locator('header')).toContainText('Mike Albers ID: 1');
	await page.getByRole('link', { name: 'Klassen' }).click();
	await expect(page.locator('header')).toContainText('Klasse 05a ID: 22');
	await page.getByRole('link', { name: 'Kurse' }).click();
	await expect(page.locator('header')).toContainText('N-F-1');
	await page.getByRole('link', { name: 'Stundenplan' }).click();
	await expect(page.locator('header')).toContainText('Stundenplan 2. Halbjahr ID: 1');

	await logout();
});
