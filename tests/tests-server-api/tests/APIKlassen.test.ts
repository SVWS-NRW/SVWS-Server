import { describe, test, expect } from 'vitest';
import { privilegedApiServer, handleRequest } from "../../utils/APIUtils";
import { ArrayList } from '../../../svws-webclient/core/src/java/util/ArrayList';

const allowDestructiveTests = process.env.MODE === 'allowDestructiveTests'

describe("APIKlassen Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test('GET: Alle Klassen zum Schuljahresabschnitt 1', async () => {
			const result = await handleRequest(api, api.getKlassenFuerAbschnitt, schema, 1);
			expect(result.content).toBeTruthy();
			expect(result.content?.size()).toMatchSnapshot();
		})

		test('GET: Alle Klassen zu einem Schuljahresabschnitt, der nicht existiert', async () => {
			const result = await handleRequest(api, api.getKlassenFuerAbschnitt, schema, 0);
			expect(result.content).toMatchSnapshot();
		})

		test('GET: Klasse', async () => {
			const result = await handleRequest(api, api.getKlasse, schema, 13);
			expect(result.content).toMatchSnapshot();
		})

		test('GET: Klasse, die nicht existiert', async () => {
			const result = await handleRequest(api, api.getKlasse, schema, 99999);
			expect(result.error?.message).toMatchSnapshot();
			expect(result.response?.status).toMatchSnapshot();
		})

		test('GET: Alle Katalog Klassenarten', async () => {
			const result = await handleRequest(api, api.getKatalogKlassenarten, schema);
			expect(result.content).toMatchSnapshot();
		})

		test.runIf(allowDestructiveTests)('POST: Neue Klasse (nur Plichtfelder)', async () => {
			const result = await handleRequest(api, api.addKlasse, {idSchuljahresabschnitt: 2, idJahrgang: 4, kuerzel: '123abc'}, schema);
			expect(result.content).toMatchSnapshot({
				id: expect.any(Number),
			});
		})

		test.runIf(allowDestructiveTests)('PATCH: Alle erlaubten Attribute einer Klasse', async () => {
			const result = await handleRequest(api, api.patchKlasse, {
						kuerzel: '06dNeu',
						idJahrgang: 2,
						parallelitaet: 'K',
						sortierung: 999,
						istSichtbar: false,
						teilstandort: 'A',
						beschreibung: 'Test Test',
						idVorgaengerklasse: 81,
						idFolgeklasse: 95,
						idAllgemeinbildendOrganisationsform: 3002000,
						idSchulgliederung: 60001000,
						idKlassenart: 6000,
						noteneingabeGesperrt: true,
						verwendungAnkreuzkompetenzen: true,
						beginnSommersemester: true,
					},
					schema, 3);

			expect(result).toBeTruthy();
			expect((await handleRequest(api, api.getKlasse, schema, 3)).content).toMatchSnapshot();
		})

		test.runIf(allowDestructiveTests)('DELETE: Mehrere Klassen mit unterschiedlichen Löschergebnissen', async () => {
			const klassenIdsToDelete = new ArrayList<number>();
			klassenIdsToDelete.add(2); // Klasse kann erfolgreich gelöscht werden
			klassenIdsToDelete.add(348); // Klasse kann nicht erfolgreich gelöscht werden, weil noch Schüler verknüpft sind
			klassenIdsToDelete.add(9999); // Klasse kann nicht erfolgreich gelöscht werden, weil sie nicht existiert

			const result = await handleRequest(api, api.deleteKlassen, klassenIdsToDelete, schema);
			expect(result.content).toMatchSnapshot();

			expect((await handleRequest(api, api.getKlasse, schema, 2)).response?.status).toEqual(404);
			expect((await handleRequest(api, api.getKlasse, schema, 348)).content).toBeDefined();
		})
	})
});
