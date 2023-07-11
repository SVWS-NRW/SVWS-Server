import { test, expect } from '@playwright/test';

const testdata = [
	{ field: 'Kürzel', data: 'TESTKUE' },
	{ field: 'Akad.Grad', data: 'TestDr' },
	{ field: 'Nachname', data: 'TestName' },
	{ field: 'Vorname', data: 'TestVorname' },
	{ field: 'Amtsbezeichnung', data: 'TestOStR' },
	{ field: 'Straße', data: 'Teststraße' },
	{ field: 'Telefon', data: '555TestTelefon' },
	{ field: 'Mobil oder Fax', data: '555TestMobil' },
	{ field: 'Private E-Mail-Adresse', data: 'test@mail' },
	{ field: 'Schulische E-Mail-Adresse', data: 'test@schulemail' },
	{ field: 'Geburtsdatum', data: '1983-12-31' },
]

test('test', async ({ page }) => {
	await page.goto('/');
	await page.getByRole('button', { name: 'Anmelden' }).click();
	await page.getByRole('link', { name: 'Lehrkräfte' }).click();


	for (let i = 0; i<testdata.length; i++) {
		await page.getByLabel(testdata[i].field).click();
		await page.getByLabel(testdata[i].field).fill(testdata[i].data);
		await page.waitForTimeout(1000);
	}

});