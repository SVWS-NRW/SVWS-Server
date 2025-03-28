import {describe, expect, test} from "vitest";
import {getApiService} from "../../utils/RequestBuilder.js";
import {parse} from "../../utils/ENMApiDataParser.js";
import {ENMSchueler, ENMLeistung, ENMSchuelerAnkreuzkompetenz} from "@core";

const targetUrlENMServer: string = process.env.VITE_ENM_targetHost ?? "https://localhost";
const apiServiceAuth = getApiService('T.Giesen@lmail.de', 'UD73Js0Uro', targetUrlENMServer)
const apiServiceAuthWrongTeacher = getApiService('D.Berthold@lmail.de', 'uXkpaRLY', targetUrlENMServer)

describe("Das Bearbeiten von Bemerkungen führt zu keinen Redundanzen im Child Array", async () => {
	test("Keine Duplikate in Leistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);

		// Extrahier einen Schüler aus den Daten
		const _data = await parse(await response.blob());
		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		// Überprüfe das die entsprechenden Daten vom Schüler passen
		expect(schueler.nachname).toBe("Lindemann");
		expect(schueler.vorname).toBe("Stefanie");

		// Bemerkung wird manipuliert
		const bodyDataPost = {
			id: schueler.id,
			patch: {
				ASV: "Test",
				AUE: "Test",
				LELS: "Test",
				ZB: "Test",
				foerderbemerkungen: "Test",
				individuelleVersetzungsbemerkungen: "Test",
				schulformEmpf: "Test"
			}
		}
		const postResponse = await apiServiceAuth.post(`/api/bemerkungen`, {body: JSON.stringify(bodyDataPost)});
		expect(postResponse.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);
		const _dataAfterEdit = await parse(await responseAfterEdit.blob());
		// Extrahier einen Schüler aus den Daten
		const schuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		const leistungsDaten = schuelerAfterEdit.leistungsdaten.elementData;

		const prevalences: number [] = []
		leistungsDaten.forEach((ld: ENMLeistung) => {
			const prevalence = leistungsDaten.filter((ldd: ENMLeistung) => {
				return ldd.id === ld.id;
			}).length
			prevalences.push(prevalence)
		})

		// Alle Element sollen nur ein mal vorkommen dürfen
		expect(prevalences.filter((p) => p !== 1)).toStrictEqual([])
	});

	test("Keine Duplikate in Ankreuzkompetenzen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);

		// Extrahier einen Schüler aus den Daten
		const _data = await parse(await response.blob());
		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		// Überprüfe das die entsprechenden Daten vom Schüler passen
		expect(schueler.nachname).toBe("Lindemann");
		expect(schueler.vorname).toBe("Stefanie");

		// Bemerkung wird manipuliert
		const bodyDataPost = {
			id: schueler.id,
			patch: {
				ASV: "Test",
				AUE: "Test",
				LELS: "Test",
				ZB: "Test",
				foerderbemerkungen: "Test",
				individuelleVersetzungsbemerkungen: "Test",
				schulformEmpf: "Test"
			}
		}
		const postResponse = await apiServiceAuth.post(`/api/bemerkungen`, {body: JSON.stringify(bodyDataPost)});
		expect(postResponse.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);
		const _dataAfterEdit = await parse(await responseAfterEdit.blob());
		// Extrahier einen Schüler aus den Daten
		const schuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		const ankreuzkompetenzenDaten = schuelerAfterEdit.ankreuzkompetenzen.elementData;

		const prevalences: number [] = []
		ankreuzkompetenzenDaten.forEach((ak: ENMLeistung) => {
			const prevalence = ankreuzkompetenzenDaten.filter((akk: ENMLeistung) => {
				return akk.id === ak.id;
			}).length
			prevalences.push(prevalence)
		})

		// Alle Elemente sollen nur ein mal vorkommen dürfen
		expect(prevalences.filter((p) => p !== 1)).toStrictEqual([])
	});
})

describe("Das Bearbeiten von Leistungen führt zu keinen Redundanzen im Child Array", async () => {
	test("Keine Duplikate in Leistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);

		// Extrahier einen Schüler aus den Daten
		const _data = await parse(await response.blob());
		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3014;
		})

		// Überprüfe das die entsprechenden Daten vom Schüler passen
		expect(schueler.nachname).toBe("Steuber");
		expect(schueler.vorname).toBe("Andreas");

		const leistungsId = schueler.leistungsdaten.elementData[0].id
		expect(leistungsId).toBe(4048)

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

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);
		const _dataAfterEdit = await parse(await responseAfterEdit.blob());

		// Extrahier einen Schüler aus den Daten
		const schuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3014;
		})

		const teilLeistungsDaten = schuelerAfterEdit.leistungsdaten.elementData[0].teilleistungen.elementData;

		const prevalences: number [] = []
		teilLeistungsDaten.forEach((ld: ENMLeistung) => {
			const prevalence = teilLeistungsDaten.filter((ldd: ENMLeistung) => {
				return ldd.id === ld.id;
			}).length
			prevalences.push(prevalence)
		})

		// Alle Element sollen nur ein mal vorkommen dürfen
		expect(prevalences.filter((p) => p !== 1)).toStrictEqual([])
	});
})

describe("Leistung und Teilleistung können bearbeitet werden", () => {
	test("Leistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 2889;
		})

		expect(schueler.nachname).toBe("Winter")
		expect(schueler.vorname).toBe("Jessika")

		const leistungsId = schueler.leistungsdaten.elementData[0].id
		expect(leistungsId).toBe(4060)

		const data = schueler.leistungsdaten.elementData[0]
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
			return s.id === 2889;
		})

		const dataAfterEdit = testLisaAfterEdit.leistungsdaten.elementData[0]
		const strippedDataAfterEdit = [dataAfterEdit.id, dataAfterEdit.noteQuartal, dataAfterEdit.note, dataAfterEdit.istGemahnt, dataAfterEdit.fehlstundenFach, dataAfterEdit.fachbezogeneBemerkungen]

		expect(strippedDataAfterEdit).toMatchSnapshot()
	});


	test("Doppelte IDs für Teilleistungen tauchen nicht auf", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);

		// Request war erfolgreich
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const testSchueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		console.log(testSchueler)
	})


	test("Teilleistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);

		// Request war erfolgreich
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const testSchueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		// Überprüfe das die entsprechende ID zum Schüler passt
		expect(testSchueler.nachname).toBe("Lindemann")
		expect(testSchueler.vorname).toBe("Stefanie")

		// Überprüfe den Snapshot der Leistungsdaten
		const strippedData = testSchueler.leistungsdaten.elementData[8].teilleistungen.elementData.map((ed: ENMLeistung) => {
			return {id: ed.id, note: ed.note}
		})

		expect(strippedData).toMatchSnapshot()

		// Diese TeilleistungsIds werden patched
		const patchedTeilleistungen = [15776, 15777, 15778]
		for (const patchID of patchedTeilleistungen) {
			// Füge jeder der Teileistungen die Note 6 hinzu
			const bodyData = {
				id: patchID,
				note: "6",
			}
			const postResponse = await apiServiceAuth.post(`/api/teilleistung`, {body: JSON.stringify(bodyData)});

			console.log(postResponse)
			console.log(await postResponse.text())
			// Post war erfolgreich
			expect(postResponse.status).toBe(200);
		}

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit).toBeDefined();
		expect(responseAfterEdit!.status).toBe(200);
		const _dataAfterEdit = await parse(await responseAfterEdit!.blob());

		const testSchuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		const strippedDataAfterEdit = testSchuelerAfterEdit.leistungsdaten.elementData[8].teilleistungen.elementData.map((ed: ENMLeistung) => {
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

describe("Bemerkungen können bearbeitet werden", () => {
	test("Anpassung von Bemerkungen von Schueler der nicht der gleichen Klasse wie Lehrer zugeordnet ist ist verboten", async () => {
		const response = await apiServiceAuthWrongTeacher.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3029;
		})

		// Diese Daten werden patched
		const bodyData = {
			id: schueler.id,
			patch: {
				ASV: "Test",
				AUE: "Test",
				LELS: "Test",
				ZB: "Test",
				foerderbemerkungen: "Test",
				individuelleVersetzungsbemerkungen: "Test",
				schulformEmpf: "Test"
			}
		}
		const responsePost = await apiServiceAuthWrongTeacher.post(`/api/bemerkungen`, {body: JSON.stringify(bodyData)});

		expect(responsePost.status).toBe(403);
	})

	test("Bemerkungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3074;
		})

		// Überprüfe das die entsprechende ID zum Schüler passt
		expect(schueler.nachname).toBe("Fusenig")
		expect(schueler.vorname).toBe("Kristin")

		const data = schueler.bemerkungen
		const strippedData = [data.ASV, data.AUE, data.LELS, data.ZB, data.foerderbemerkungen, data.individuelleVersetzungsbemerkungen, data.schulformEmpf]
		expect(strippedData).toMatchSnapshot()

		// Diese Daten werden patched
		const bodyData = {
			id: schueler.id,
			patch: {
				ASV: "Test",
				AUE: "Test",
				LELS: "Test",
				ZB: "Test",
				foerderbemerkungen: "Test",
				individuelleVersetzungsbemerkungen: "Test",
				schulformEmpf: "Test"
			}
		}
		const responsePost = await apiServiceAuth.post(`/api/bemerkungen`, {body: JSON.stringify(bodyData)});
		expect(responsePost.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		const _dataAfterEdit = await parse(await responseAfterEdit.blob());

		const testSchuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3074;
		})

		const dataAfterEdit = testSchuelerAfterEdit.bemerkungen;
		const strippedDataAfterEdit = [dataAfterEdit.ASV, dataAfterEdit.AUE, dataAfterEdit.LELS, dataAfterEdit.ZB, dataAfterEdit.foerderbemerkungen, dataAfterEdit.individuelleVersetzungsbemerkungen, dataAfterEdit.schulformEmpf]
		expect(strippedDataAfterEdit).toMatchSnapshot()
	});
})

describe("Ankreuzkompetenzen können bearbeitet werden", () => {
	const targetAnkreuzKompetenzId = 18153;

	test("Ankreuzkompetenzen mit unbekannter ID", async () => {
		const bodyData = {
			id: 9999999999,
			stufen: [true, true, true, true, true]
		}

		const responsePost = await apiServiceAuth.post(`/api/ankreuzkompetenz`, {body: JSON.stringify(bodyData)});
		expect(responsePost.status).toBe(404);
	})

	test("Anpassung von Ankreuzkompetenzen von Schueler der nicht der gleichen Klasse wie Lehrer zugeordnet ist ist verboten", async () => {
		const bodyData = {
			id: targetAnkreuzKompetenzId,
			stufen: [true, true, true, true, true]
		}
		const responsePost = await apiServiceAuthWrongTeacher.post(`/api/ankreuzkompetenz`, {body: JSON.stringify(bodyData)});
		expect(responsePost.status).toBe(403);
	})

	test("Ankreuzkompetenzen GET", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);

		expect(response.status).toBe(200);

		const _data = await parse(await response.blob());

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3074;
		})

		// Überprüfe das die entsprechenden Daten vom Schüler passen
		expect(schueler.nachname).toBe("Fusenig");
		expect(schueler.vorname).toBe("Kristin");

		const data = schueler.ankreuzkompetenzen.elementData.find((ak: ENMSchuelerAnkreuzkompetenz) => ak.id === targetAnkreuzKompetenzId);

		const strippedData = [data.id, data.kompetenzID, data.stufen];
		expect(strippedData).toMatchSnapshot()

		// Diese Daten werden patched
		const bodyData = {
			id: targetAnkreuzKompetenzId,
			stufen: [true, true, true, true, true]
		}

		const responsePost = await apiServiceAuth.post(`/api/ankreuzkompetenz`, {body: JSON.stringify(bodyData)});
		expect(responsePost.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);

		const _dataAfterEdit = await parse(await responseAfterEdit.blob());

		const schuelerAfterEdit = _dataAfterEdit.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === 3074;
		})

		const dataAfterEdit = schuelerAfterEdit.ankreuzkompetenzen.elementData.find((ak: ENMSchuelerAnkreuzkompetenz) => ak.id === targetAnkreuzKompetenzId);
		const strippedDataAfterEdit = [dataAfterEdit.id, dataAfterEdit.kompetenzID, dataAfterEdit.stufen];

		expect(strippedDataAfterEdit).toMatchSnapshot()
	});
})
