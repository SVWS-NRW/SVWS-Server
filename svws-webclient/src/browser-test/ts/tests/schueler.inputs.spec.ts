import {  test, expect  } from '@playwright/test';

const testdata = [
	{  field: 'Rufname', data: 'TestRufname'  },
	{  field: 'Rufname', data: 'Dominik'  },
	{  field: 'Nachname', data: 'TestName' },
	{ field: 'Nachname', data: 'Batta' },
	{ field: 'Alle Vornamen', data: 'TestVornamen' },
	{ field: 'Zusatz', data: 'TestZusatz' },
	{ field: 'Geburtsname', data: 'TestGeburtsname' },
	{ field: 'Geburtsort', data: 'TestGeburtsort' },
	{ field: 'Straße', data: 'Teststraße' },
	{ field: 'Telefon', data: '555TestTelefon' },
	{ field: 'Mobil oder Fax', data: '555TestMobil' },
	{ field: 'Private E-Mail-Adresse', data: 'test@mail' },
	{ field: 'Schulische E-Mail-Adresse', data: 'test@schulemail' },
	{ field: 'Bemerkungen', data: 'Testtext! Und noch ein Testtext?' },
	{ field: 'Geburtsdatum', data: '2005-12-31' },
	{ field: 'Anmeldedatum', data: '2015-12-31' },
	{ field: 'Aufnahmedatum', data: '2015-12-31' },

]

test('test', async ({  page  }) => {
	await page.goto('/');
	await page.getByRole('button', {  name: 'Anmelden'  }).click();
	await page.getByRole('row', {  name: '09a Batta Dominik'  }).getByText('Batta').click();
	//await page.getByLabel('Migrationshintergrund vorhanden').click();
	//expect(await page.getByLabel('Migrationshintergrund vorhanden').isChecked());

	for (let i = 0; i < testdata.length; i++) {
		await page.getByLabel(testdata[i].field).click();
		await page.getByLabel(testdata[i].field).fill(testdata[i].data);
		await page.waitForTimeout(900);
	}

});