import {describe, expect, test} from "vitest";
import {getApiService} from "../../utils/RequestBuilder.js";
import {parse} from "../../utils/ENMApiDataParser.js";
import {ENMSchueler, ENMLeistung} from "@core";

const targetUrlENMServer: string = process.env.VITE_ENM_targetHost ?? "https://localhost";
const apiServiceAuth = getApiService('D.Berthold@lmail.de', 'uXkpaRLY', targetUrlENMServer)


describe("Leistung und Teilleistung können bearbeitet werden", () => {
	test("Leistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const testLisa = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3225;
		})

		expect(testLisa.nachname).toBe("Bill")
		expect(testLisa.vorname).toBe("Angelika")


		const leistungsId = testLisa.leistungsdaten.elementData[0].id

		expect(leistungsId).toBe(1390)

		const data = testLisa.leistungsdaten.elementData[0]
		const strippedData = [data.id, data.noteQuartal, data.note, data.istGemahnt, data.fehlstundenFach, data.fachbezogeneBemerkungen]

		expect(strippedData).toMatchSnapshot()

		// Diese Daten werden patched
		const bodyData = {
			id: leistungsId,
			noteQuartal: "NB",
			note: "6",
			istGemahnt: true,
			fehlstundenFach: 3,
			fachbezogeneBemerkungen: "ist ein test"
		}
		const responsePost = await apiServiceAuth.post(`/api/leistung`, {
			body: JSON.stringify(bodyData)
		});
		expect(responsePost.status).toBe(200);
		//
		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		const _dataAfterEdit = await parse(await responseAfterEdit.blob());

		const testLisaAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3225;
		})

		const dataAfterEdit = testLisaAfterEdit.leistungsdaten.elementData[0]
		const strippedDataAfterEdit = [dataAfterEdit.id, dataAfterEdit.noteQuartal, dataAfterEdit.note, dataAfterEdit.istGemahnt, dataAfterEdit.fehlstundenFach, dataAfterEdit.fachbezogeneBemerkungen]

		expect(strippedDataAfterEdit).toMatchSnapshot()
	});


	test("Teilleistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);

		// Request war erfolgreich
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const testSchueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3204;
		})

		// Überprüfe das die entsprechende ID zum Schüler passt
		expect(testSchueler.nachname).toBe("Boetius")
		expect(testSchueler.vorname).toBe("Ulrich")

		// Überprüfe den Snapshot der Leistungsdaten
		const strippedData = testSchueler.leistungsdaten.elementData[1].teilleistungen.elementData.map((ed: ENMLeistung) => {
			return {id: ed.id, note: ed.note}
		})

		expect(strippedData).toMatchSnapshot()

		// Diese TeilleistungsIds werden patched
		const patchedTeilleistungen = [10931, 10932, 10933]
		for (const patchID of patchedTeilleistungen) {
			// Füge jeder der Teileistungen die Note 6 hinzu
			const bodyData = {
				id: patchID,
				note: "6",
			}
			const postResponse = await apiServiceAuth.post(`/api/teilleistung`, {body: JSON.stringify(bodyData)});

			// Post war erfolgreich
			expect(postResponse.status).toBe(200);
		}

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit).toBeDefined();
		expect(responseAfterEdit!.status).toBe(200);
		const _dataAfterEdit = await parse(await responseAfterEdit!.blob());
		// expect(_dataAfterEdit).toMatchSnapshot()

		const testSchuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3204;
		})

		const strippedDataAfterEdit = testSchuelerAfterEdit.leistungsdaten.elementData[1].teilleistungen.elementData.map((ed: ENMLeistung) => {
			return {id: ed.id, note: ed.note}
		})

		expect(strippedDataAfterEdit).toMatchSnapshot()
	});
})

describe("Clientconfig können bearbeitet werden", () => {
	test("Clientconfig", async () => {
		const response = await apiServiceAuth.get(`/api/clientconfig`);
		expect(response.status).toBe(200);
		const _data = await response.text();
		expect(_data).toMatchSnapshot()

		// Diese Daten werden patched
		const bodyData = {
			"key": "testkey",
			"value": "testvalue"
		}

		const responsePUT = await apiServiceAuth.put(`/api/clientconfig`, {
			body: JSON.stringify(bodyData),
			headers: {"Content-Type": "application/json"}
		});

		expect(responsePUT.status).toBe(200);

		const responseAfterPUT = await apiServiceAuth.get(`/api/clientconfig`);
		expect(responseAfterPUT.status).toBe(200);
		const _responseContentAfterPut = await responseAfterPUT.text();
		expect(_responseContentAfterPut).toMatchSnapshot()

	});
})

// TODO: folgende Testfälle stehen aus:
describe.skip("SQL Injection", () => {})

describe.skip("Zeitbasierte Angriffe", () => {})

describe.skip("Bemerkungen können bearbeitet werden", () => {
	test("Bemerkungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3200;
		})

		// Überprüfe das die enstprechende ID zum Schüler passt
		expect(schueler.nachname).toBe("Edden")
		expect(schueler.vorname).toBe("Ralph")

		expect(schueler.bemerkungen).toMatchSnapshot()

		// Diese Daten werden patched
		const bodyData = {
			id: schueler.id,
			ASV: "Test12",
			AUE: "Test12",
			LELS: "Test12",
			ZB: "Test12",
			foerderbemerkungen: "Test12",
			individuelleVersetzungsbemerkungen: "Test12",
			schulformEmpf: "Test12",
			tsASV: "Test12",
			tsAUE: "Test12",
			tsFoerderbemerkungen: "Test12",
			tsIndividuelleVersetzungsbemerkungen: "Test12",
			tsLELS: "Test12",
			tsSchulformEmpf: "Test12",
			tsZB: "Test12",
		}
		const responsePost = await apiServiceAuth.post(`/api/bemerkungen`, {body: JSON.stringify(bodyData)});

		expect(responsePost.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		const _dataAfterEdit = await parse(await responseAfterEdit.blob());

		const testSchuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 9221;
		})

		expect(testSchuelerAfterEdit.bemerkungen).toMatchSnapshot()
		expect(testSchuelerAfterEdit).toMatchSnapshot()

	});
})

describe.skip("Ankreuzkompetenzen können bearbeitet werden", () => {
	test("Ankreuzkompetenzen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3267;
		})

		// Überprüfe das die enstprechende ID zum Schüler passt
		expect(schueler.nachname).toBe("Haats")
		expect(schueler.vorname).toBe("Mario")
	});
})
