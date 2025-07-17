import type { Page } from "@playwright/test";

export function useLoginUtils(targetHost: string, page: Page) {

	const loginAdmin = async () => {
		await page.goto(targetHost);
		await page.getByLabel('Benutzername').click();
		await page.getByLabel('Benutzername').fill('Admin');
		await page.getByRole('button', { name: 'Anmelden' }).click();
	}

	const loginBISZ = async () => {
		await page.goto(targetHost);
		await page.getByLabel('Benutzername').click();
		await page.getByLabel('Benutzername').fill('BISZ');
		await page.getByLabel('Passwort').click();
		await page.getByLabel('Passwort').fill('BISZ');
		await page.getByRole('button', { name: 'Anmelden' }).click();
	}

	return {
		loginAdmin,
		loginBISZ
	}
}
