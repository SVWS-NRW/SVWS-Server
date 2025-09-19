import { expect, type Page } from "@playwright/test";

export function useLoginUtils(targetHost: string, page: Page) {

	const loginAdmin = async () => {
		await page.goto(targetHost);
		await page.getByLabel('Benutzername').click();
		await page.getByLabel('Benutzername').fill('Admin');
		await page.getByRole('button', { name: 'Anmelden' }).click();
		await page.waitForURL('**/#/**/schueler/**/daten', { timeout: 30_000 });
	}

	const loginBISZ = async () => {
		await page.goto(targetHost);
		await page.getByLabel('Benutzername').click();
		await page.getByLabel('Benutzername').fill('BISZ');
		await page.getByLabel('Passwort').click();
		await page.getByLabel('Passwort').fill('BISZ');
		await page.getByRole('button', { name: 'Anmelden' }).click();
		await page.waitForURL('**/#/**/schueler/**/daten', { timeout: 30_000 });
	}

	const loginRoot = async () => {
		await page.goto(targetHost);
		await page.getByLabel('Benutzername').click();
		await page.getByLabel('Benutzername').fill('root');
		await page.getByLabel('Passwort').fill('root');
		await page.getByRole('button', { name: 'Anmelden' }).click();
	}

	const logout = async () => {
		await page.getByRole('link', { name: 'Abmelden' }).click();
		await expect(page.getByRole('button', { name: 'Anmelden' })).toContainText('Anmelden');
	}

	return {
		loginAdmin,
		loginBISZ,
		loginRoot,
		logout,
	}
}
