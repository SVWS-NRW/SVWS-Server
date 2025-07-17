import type { Page } from "@playwright/test";

export function useLoginUtils(targetHost: string, page: Page) {

	const loginAdmin = async () => {
		await page.goto(targetHost);
		await page.getByLabel('Benutzername').click();
		await page.getByLabel('Benutzername').fill('Admin');
		await page.getByRole('button', { name: 'Anmelden' }).click();
	}

	return {
		loginAdmin,
	}
}
