import { beforeAll, describe, expect, test } from "vitest";
import { getApiService } from "../../utils/RequestBuilder.js";
import type { ENMv1Leistung } from "@core";
import { ENMv1Daten, ENMv1Schueler, ENMv1SchuelerAnkreuzkompetenz } from "@core";
import { enmURL } from "../../../utils/APIUtils";

const targetUrlENMServer: string = enmURL;

const apiServiceAuth = getApiService('T.Giesen@lmail.de', 'UD73Js0Uro', targetUrlENMServer);
const apiServiceAuthWrongTeacher = getApiService('D.Berthold@lmail.de', 'uXkpaRLY', targetUrlENMServer);

function findSchueler(data: ENMv1Daten, id: number): ENMv1Schueler {
	let schueler = new ENMv1Schueler();
	for (const s of data.schueler) {
		if (s.id === id) {
			schueler = s;
			break;
		}
	}
	return schueler;
}

beforeAll(async () => {
	await apiServiceAuth.login();
	await apiServiceAuthWrongTeacher.login();
});

describe("Das Bearbeiten von Bemerkungen führt zu keinen Redundanzen im Child Array", async () => {

	test("Keine Duplikate in Leistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);

		// Extrahier einen Schüler aus den Daten
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());
		let schueler = new ENMv1Schueler();
		for (const s of _data.schueler) {
			if (s.id === 3029) {
				schueler = s;
				break;
			}
		}

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
				schulformEmpf: "Test",
			},
		};
		const postResponse = await apiServiceAuth.post(`/api/bemerkungen`, { body: JSON.stringify(bodyDataPost) });
		expect(postResponse.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);
		const _dataAfterEdit = ENMv1Daten.transpilerFromJSON(await (await responseAfterEdit.blob()).text());
		// Extrahier einen Schüler aus den Daten
		let schuelerAfterEdit = new ENMv1Schueler();
		for (const s of _dataAfterEdit.schueler) {
			if (s.id === 3029) {
				schuelerAfterEdit = s;
				break;
			}
		}

		const leistungsDaten = [...schuelerAfterEdit.leistungsdaten];

		const prevalences: number [] = [];
		leistungsDaten.forEach((ld: ENMv1Leistung) => {
			const prevalence = leistungsDaten.filter((ldd: ENMv1Leistung) => {
				return ldd.id === ld.id;
			}).length;
			prevalences.push(prevalence);
		});

		// Alle Element sollen nur ein mal vorkommen dürfen
		expect(prevalences.filter((p) => p !== 1)).toStrictEqual([]);
	});

	test("Keine Duplikate in Ankreuzkompetenzen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);

		// Extrahier einen Schüler aus den Daten
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());
		const schueler = findSchueler(_data, 3029);

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
				schulformEmpf: "Test",
			},
		};
		const postResponse = await apiServiceAuth.post(`/api/bemerkungen`, { body: JSON.stringify(bodyDataPost) });
		expect(postResponse.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);
		const _dataAfterEdit = ENMv1Daten.transpilerFromJSON(await (await responseAfterEdit.blob()).text());
		// Extrahier einen Schüler aus den Daten
		const schuelerAfterEdit = findSchueler(_dataAfterEdit, 3029);

		const ankreuzkompetenzenDaten = [...schuelerAfterEdit.ankreuzkompetenzen];

		const prevalences: number[] = [];
		ankreuzkompetenzenDaten.forEach(ak => {
			const prevalence = ankreuzkompetenzenDaten.filter(akk => {
				return akk.id === ak.id;
			}).length;
			prevalences.push(prevalence);
		});

		// Alle Elemente sollen nur ein mal vorkommen dürfen
		expect(prevalences.filter((p) => p !== 1)).toStrictEqual([]);
	});
});

describe("Das Bearbeiten von Leistungen führt zu keinen Redundanzen im Child Array", async () => {
	test("Keine Duplikate in Leistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);

		// Extrahier einen Schüler aus den Daten
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());
		const schueler = findSchueler(_data, 3014);

		// Überprüfe das die entsprechenden Daten vom Schüler passen
		expect(schueler.nachname).toBe("Steuber");
		expect(schueler.vorname).toBe("Andreas");

		const leistungsId = schueler.leistungsdaten.getFirst().id;
		expect(leistungsId).toBe(4048);

		const bodyData = {
			id: leistungsId,
			noteQuartal: "NB",
			note: "6",
			istGemahnt: true,
			fehlstundenFach: 3,
			fachbezogeneBemerkungen: "ist ein test",
		};

		const responsePost = await apiServiceAuth.post(`/api/leistung`, {
			body: JSON.stringify(bodyData),
		});

		expect(responsePost.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);
		const _dataAfterEdit = ENMv1Daten.transpilerFromJSON(await (await responseAfterEdit.blob()).text());

		// Extrahier einen Schüler aus den Daten
		const schuelerAfterEdit = findSchueler(_dataAfterEdit, 3014);

		const teilLeistungsDaten = [...schuelerAfterEdit.leistungsdaten.getFirst().teilleistungen];

		const prevalences: number [] = [];
		teilLeistungsDaten.forEach(ld => {
			const prevalence = teilLeistungsDaten.filter(ldd => {
				return ldd.id === ld.id;
			}).length;
			prevalences.push(prevalence);
		});

		// Alle Element sollen nur ein mal vorkommen dürfen
		expect(prevalences.filter((p) => p !== 1)).toStrictEqual([]);
	});
});

describe("Leistung und Teilleistung können bearbeitet werden", () => {
	test("Leistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());

		const schueler = findSchueler(_data, 2889);

		expect(schueler.nachname).toBe("Winter");
		expect(schueler.vorname).toBe("Jessika");

		const leistungsId = schueler.leistungsdaten.getFirst().id;
		expect(leistungsId).toBe(4060);

		const data = schueler.leistungsdaten.getFirst();
		const strippedData = [data.id, data.noteQuartal, data.note, data.istGemahnt, data.fehlstundenFach, data.fachbezogeneBemerkungen];

		expect(strippedData).toMatchSnapshot();

		// Diese Daten werden patched
		const bodyData = {
			id: leistungsId,
			noteQuartal: "NB",
			note: "6",
			istGemahnt: true,
			fehlstundenFach: 3,
			fachbezogeneBemerkungen: "ist ein test",
		};
		const responsePost = await apiServiceAuth.post(`/api/leistung`, {
			body: JSON.stringify(bodyData),
		});
		expect(responsePost.status).toBe(200);
		//
		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		const _dataAfterEdit = ENMv1Daten.transpilerFromJSON(await (await responseAfterEdit.blob()).text());

		const testLisaAfterEdit = findSchueler(_dataAfterEdit, 2889);

		const dataAfterEdit = testLisaAfterEdit.leistungsdaten.getFirst();
		const strippedDataAfterEdit = [dataAfterEdit.id, dataAfterEdit.noteQuartal, dataAfterEdit.note, dataAfterEdit.istGemahnt, dataAfterEdit.fehlstundenFach, dataAfterEdit.fachbezogeneBemerkungen];

		expect(strippedDataAfterEdit).toMatchSnapshot();
	});


	test("Teilleistungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);

		// Request war erfolgreich
		expect(response.status).toBe(200);
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());

		const testSchueler = findSchueler(_data, 3029);

		// Überprüfe das die entsprechende ID zum Schüler passt
		expect(testSchueler.nachname).toBe("Lindemann");
		expect(testSchueler.vorname).toBe("Stefanie");

		// Überprüfe den Snapshot der Leistungsdaten
		const strippedData: { id: number; note: string | null }[] = [];
		for (const tl of testSchueler.leistungsdaten.get(8).teilleistungen) {
			strippedData.push({ id: tl.id, note: tl.note });
		}

		expect(strippedData).toMatchSnapshot();

		// Diese TeilleistungsIds werden patched
		const patchedTeilleistungen = [15776, 15777, 15778];
		for (const patchID of patchedTeilleistungen) {
			// Füge jeder der Teileistungen die Note 6 hinzu
			const bodyData = {
				id: patchID,
				note: "6",
			};
			const postResponse = await apiServiceAuth.post(`/api/teilleistung`, { body: JSON.stringify(bodyData) });

			// Post war erfolgreich
			expect(postResponse.status).toBe(200);
		}

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit).toBeDefined();
		expect(responseAfterEdit.status).toBe(200);
		const _dataAfterEdit = ENMv1Daten.transpilerFromJSON(await (await responseAfterEdit.blob()).text());

		const testSchuelerAfterEdit = findSchueler(_dataAfterEdit, 3029);

		const strippedDataAfterEdit: { id: number; note: string | null }[] = [];
		for (const tl of testSchuelerAfterEdit.leistungsdaten.get(8).teilleistungen) {
			strippedDataAfterEdit.push({ id: tl.id, note: tl.note });
		}

		expect(strippedDataAfterEdit).toMatchSnapshot();
	});
});

describe("Clientconfig können bearbeitet werden", () => {
	test("Clientconfig", async () => {
		const response = await apiServiceAuth.get(`/api/clientconfig`);
		expect(response.status).toBe(200);
		const _data = await response.text();
		expect(_data).toMatchSnapshot();

		// Diese Daten werden patched
		const bodyData = {
			"key": "testkey",
			"value": "testvalue",
		};

		const responsePUT = await apiServiceAuth.put(`/api/clientconfig`, {
			body: JSON.stringify(bodyData),
			headers: { "Content-Type": "application/json" },
		});

		expect(responsePUT.status).toBe(200);

		const responseAfterPUT = await apiServiceAuth.get(`/api/clientconfig`);
		expect(responseAfterPUT.status).toBe(200);
		const _responseContentAfterPut = await responseAfterPUT.text();
		expect(_responseContentAfterPut).toMatchSnapshot();
	});
});

describe("Bemerkungen können bearbeitet werden", () => {
	test("Anpassung von Bemerkungen von Schueler der nicht der gleichen Klasse wie Lehrer zugeordnet ist, ist verboten", async () => {
		const response = await apiServiceAuthWrongTeacher.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());

		const schueler = findSchueler(_data, 3029);

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
				schulformEmpf: "Test",
			},
		};
		const responsePost = await apiServiceAuthWrongTeacher.post(`/api/bemerkungen`, { body: JSON.stringify(bodyData) });

		expect(responsePost.status).toBe(403);
	});

	test("Bemerkungen", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());

		const schueler = findSchueler(_data, 3074);

		// Überprüfe das die entsprechende ID zum Schüler passt
		expect(schueler.nachname).toBe("Fusenig");
		expect(schueler.vorname).toBe("Kristin");

		const data = schueler.bemerkungen;
		const strippedData = [data.ASV, data.AUE, data.LELS, data.ZB, data.foerderbemerkungen, data.individuelleVersetzungsbemerkungen, data.schulformEmpf];
		expect(strippedData).toMatchSnapshot();

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
				schulformEmpf: "Test",
			},
		};
		const responsePost = await apiServiceAuth.post(`/api/bemerkungen`, { body: JSON.stringify(bodyData) });
		expect(responsePost.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		const _dataAfterEdit = ENMv1Daten.transpilerFromJSON(await (await responseAfterEdit.blob()).text());

		const testSchuelerAfterEdit = findSchueler(_dataAfterEdit, 3074);

		const dataAfterEdit = testSchuelerAfterEdit.bemerkungen;
		const strippedDataAfterEdit = [dataAfterEdit.ASV, dataAfterEdit.AUE, dataAfterEdit.LELS, dataAfterEdit.ZB, dataAfterEdit.foerderbemerkungen, dataAfterEdit.individuelleVersetzungsbemerkungen, dataAfterEdit.schulformEmpf];
		expect(strippedDataAfterEdit).toMatchSnapshot();
	});
});

describe("Ankreuzkompetenzen können bearbeitet werden", () => {
	const targetAnkreuzKompetenzId = 18153;

	test("Ankreuzkompetenzen mit unbekannter ID", async () => {
		const bodyData = {
			id: 9999999999,
			stufen: [true, true, true, true, true],
		};

		const responsePost = await apiServiceAuth.post(`/api/ankreuzkompetenz`, { body: JSON.stringify(bodyData) });
		expect(responsePost.status).toBe(404);
	});

	test("Anpassung von Ankreuzkompetenzen von Schueler der nicht der gleichen Klasse wie Lehrer zugeordnet ist ist verboten", async () => {
		const bodyData = {
			id: targetAnkreuzKompetenzId,
			stufen: [true, true, true, true, true],
		};
		const responsePost = await apiServiceAuthWrongTeacher.post(`/api/ankreuzkompetenz`, { body: JSON.stringify(bodyData) });
		expect(responsePost.status).toBe(403);
	});

	test.skip("Ankreuzkompetenzen GET", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);

		expect(response.status).toBe(200);

		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());

		const schueler = findSchueler(_data, 3074);

		// Überprüfe das die entsprechenden Daten vom Schüler passen
		expect(schueler.nachname).toBe("Fusenig");
		expect(schueler.vorname).toBe("Kristin");

		let data = new ENMv1SchuelerAnkreuzkompetenz();
		for (const ak of schueler.ankreuzkompetenzen) {
			if (ak.id === targetAnkreuzKompetenzId) {
				data = ak;
				break;
			}
		}

		const strippedData = [data.id, data.kompetenzID, data.stufen];
		expect(strippedData).toMatchSnapshot();

		// Diese Daten werden patched
		const bodyData = {
			id: targetAnkreuzKompetenzId,
			stufen: [true, true, true, true, true],
		};

		const responsePost = await apiServiceAuth.post(`/api/ankreuzkompetenz`, { body: JSON.stringify(bodyData) });
		expect(responsePost.status).toBe(200);

		const responseAfterEdit = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterEdit.status).toBe(200);

		const _dataAfterEdit = ENMv1Daten.transpilerFromJSON(await (await responseAfterEdit.blob()).text());

		const schuelerAfterEdit = findSchueler(_dataAfterEdit, 3074);

		let dataAfterEdit = new ENMv1SchuelerAnkreuzkompetenz();
		for (const ak of schuelerAfterEdit.ankreuzkompetenzen) {
			if (ak.id === targetAnkreuzKompetenzId) {
				dataAfterEdit = ak;
				break;
			}
		}
		const strippedDataAfterEdit = [dataAfterEdit.id, dataAfterEdit.kompetenzID, dataAfterEdit.stufen];

		expect(strippedDataAfterEdit).toMatchSnapshot();
	});
});

describe("Passwort Management durch create_pwt", () => {
	test("Fehlender Parameter im JSON führt zu Fehler", async () => {
		// Diese Daten werden patched
		const bodyData = {
			notUsed: "Die Dienst-E-Mail ist erforderlich.",
		};

		const responsePost = await apiServiceAuth.post(`/api/create_pwt`, { body: JSON.stringify(bodyData) });
		expect(await responsePost.text()).toContain("Die Dienst-E-Mail ist erforderlich");
		expect(responsePost.status).toBe(400);
	});

	test("Unbekannte Email erzeugt Fehler", async () => {
		// Diese Daten werden patched
		const bodyData = {
			eMailDienstlich: "notused@email.de",
		};

		const responsePost = await apiServiceAuth.post(`/api/create_pwt`, { body: JSON.stringify(bodyData) });
		expect(await responsePost.text()).toContain("Mehrere Lehrer mit dieser E-Mail-Adresse gefunden");
		expect(responsePost.status).toBe(409);
	});

	test("Korrekte Email -> 204", async () => {
		// Diese Daten werden patched
		const bodyData = {
			eMailDienstlich: "D.Berthold@lmail.de",
		};

		const responsePost = await apiServiceAuth.post(`/api/create_pwt`, { body: JSON.stringify(bodyData) });
		console.log(await responsePost.text());
		expect(responsePost.status).toBe(204);
	});

	test("Korrekte Email doppelt führt zu einem Fehler", async () => {
		const bodyData = {
			eMailDienstlich: "D.Berthold@lmail.de",
		};

		await apiServiceAuth.post(`/api/create_pwt`, { body: JSON.stringify(bodyData) });

		const responsePost = await apiServiceAuth.post(`/api/create_pwt`, { body: JSON.stringify(bodyData) });
		expect(await responsePost.text()).toContain("Bitte warten Sie, bevor Sie es erneut versuchen.");
		expect(responsePost.status).toBe(429);
	});
});

describe("Test Lernabschnitte", () => {
	test("Post Lernabschnitte", async () => {
		const response = await apiServiceAuth.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = ENMv1Daten.transpilerFromJSON(await (await response.blob()).text());

		const schuelerID = 3029;

		const schueler = findSchueler(_data, schuelerID);

		const lernabschnittID = 12452;
		expect(schueler.lernabschnitt.id).toBe(lernabschnittID);

		const bodyData = {
			id: lernabschnittID,
			fehlstundenGesamt: 1337,

		};

		const responseOfPost = await apiServiceAuth.post(`/api/lernabschnitt`, {
			body: JSON.stringify(bodyData),
		});
		console.log(await responseOfPost.text());
		expect(responseOfPost.status).toBe(200);

		const responseAfterPost = await apiServiceAuth.get(`/api/daten`);
		expect(responseAfterPost.status).toBe(200);
		const _dataAfterPost = ENMv1Daten.transpilerFromJSON(await (await responseAfterPost.blob()).text());

		const schuelerAfterPost = findSchueler(_dataAfterPost, schuelerID);

		expect(schuelerAfterPost.lernabschnitt.fehlstundenGesamt).toBe(1337);

	});
});
