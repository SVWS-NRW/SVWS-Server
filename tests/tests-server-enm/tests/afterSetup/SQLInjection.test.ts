import {describe, expect, test} from "vitest";
import {getApiService} from "../../utils/RequestBuilder.js"
import {parse} from "../../utils/ENMApiDataParser.js";
import {ENMSchueler} from "@core";

const targetUrlENMServer: string = process.env.VITE_ENM_targetHost ?? "https://localhost";

const apiServiceAuth = getApiService('T.Giesen@lmail.de', 'UD73Js0Uro', targetUrlENMServer)
const apiServiceAuthInjected = getApiService('M.Gehring@lmail.de', 'uTdNE7EUIb', targetUrlENMServer)

// Durch gezieltes Setzen von Anführungszeichen kann außerhalb der JSON in der DB-Spalte daten weitere SQL-Befehle ausgeführt werden.
// Diese SQL-Befehle werden auf Ebene des Admins ausgeführt und können z. B. Tabellen löschen oder Daten manipuliert werden.
// Als Beispiel werden immer Daten von Schülern manipuliert, die eigentlich nicht für den Lehrer (durch php-Code) erreichbar sind.
// Die Sicherheitslücken wurden behoben.
describe(`SQL-Injections der POST-Endpunkte des ENM-Servers.`,  () => {

	test("Eine SQL-Injection auf POST Leistungen um Daten einer anderen Id zu manipulieren. Der Angriff ist nicht erfolgreich.", async () => {
		const response = await apiServiceAuthInjected.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		// aktueller Lehrer (auth) M.Gehring@lmail.de ist für diesen Schüler zuständig
		const schuelerSQLInjectionTargetID = 2832

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		expect(schueler.nachname).toBe("Kröchler")
		expect(schueler.vorname).toBe("Christin")

		const sqlInjectionTargetId = 4763
		expect(schueler.leistungsdaten.getFirst().id).toBe(sqlInjectionTargetId)
		// Hinweis: nach Ausführen des Tests stimmt diese Abfrage nicht mehr, ggf. auskommentieren oder DB neu aufsetzen bei lokalen Tests
		expect(schueler.leistungsdaten.getFirst().note).toBe(null)

		const legalLeistungsId = 4061 // LeistungsId für zuständigen Lehrer T.Giesen@lmail.de

		// Die SQL-Injection besteht aus 2 Teilen.
		// Der erste Teil sorgt für eine defekte JSON in der Datenbank-Spalte daten. Dadurch sind weiter SQL-Befehle möglich, wie eine andere ID zu nutzen oder weitere SQL-Befehle auszuführen
		let SQL_INJECTION = `Injection}' WHERE id = ${sqlInjectionTargetId};`
		// Der zweite Teil führt ein UPDATE auf die Daten aus den Leistungsdaten für die legalLeistungsId und ersetzen diese in der Leistungsdaten mit sqlInjectionTargetId.
		// So wird sichergestellt, dass die vorher zerstörte JSON-Struktur wieder repariert wird und dekodiert bzw. kodiert werden kann.
		// Zusätzlich werden alle null-Werte mit 1337 ersetzt für die sqlInjectionTargetId sowie die LerngruppenId durch die richtige Id ersetzt. So kann M.Gehring@lmail.de auch den Schüler finden.
		// Der letzte Bereich setzt einen Kommentar in dem SQL-Befehl, so wird der Rest des eigentlichen SQL-Befehls ignoriert. Im eigentlichen SQL-Befehl würde noch WHERE id = ${legalLeistungsId} kommen
		let SQL_INJECTION_UPDATE = ` UPDATE Leistungsdaten SET daten = REPLACE(REPLACE(REPLACE((SELECT daten FROM Leistungsdaten WHERE id = ${legalLeistungsId}), ${legalLeistungsId}, ${sqlInjectionTargetId}), 'null', 1337), '68', 26) WHERE id = ${sqlInjectionTargetId};--`;

		let SQL_GESAMT = SQL_INJECTION + SQL_INJECTION_UPDATE;

		// bereite Data für die Injection vor, vollständiger Body wird nicht benötigt
		const bodyData = {
			id: legalLeistungsId,
			note: SQL_GESAMT,
		}

		// sende LeistungsDaten als Lehrer T.Giesen@lmail.de mit der SQL-Injection
		const responseOfInjection = await apiServiceAuth.post(`/api/leistung`, {
			body: JSON.stringify(bodyData)
		});
		expect(responseOfInjection.status).toBe(200)

		// rufe Daten als M.Gehring@lmail.de ab
		const responseAfterInjection = await apiServiceAuthInjected.get(`/api/daten`);
		expect(responseAfterInjection.status).toBe(200);
		const _dataAfterInjection = await parse(await responseAfterInjection.blob());

		const schuelerAfterInjection = _dataAfterInjection.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		// überprüfe Daten
		expect(schuelerAfterInjection.nachname).toBe("Kröchler")
		expect(schuelerAfterInjection.vorname).toBe("Christin")

		expect(schuelerAfterInjection.leistungsdaten.getFirst().id).toBe(sqlInjectionTargetId)
		expect(schuelerAfterInjection.leistungsdaten.getFirst().note).toBe(null) // Injection war nicht erfolgreich, sollte eigentlich 1337 sein
	});

	test("Eine SQL Injection auf POST TeilLeistungen um Daten einer anderen Id zu manipulieren. Der Angriff ist nicht erfolgreich.", async () => {
		const response = await apiServiceAuthInjected.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		// aktueller Lehrer (auth) M.Gehring@lmail.de ist für diesen Schüler zuständig
		const schuelerSQLInjectionTargetID = 2884

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		expect(schueler.nachname).toBe("Bödefeld")
		expect(schueler.vorname).toBe("Martin")

		const sqlInjectionTargetId = 23512
		expect(schueler.leistungsdaten.getFirst().teilleistungen.getFirst().id).toBe(sqlInjectionTargetId)
		// Hinweis: nach Ausführen des Tests stimmt diese Abfrage nicht mehr, ggf. auskommentieren oder DB neu aufsetzen bei lokalen Tests
		expect(schueler.leistungsdaten.getFirst().teilleistungen.getFirst().note).toBe(null)

		const legalTeileistungsId = 21884 // TeileistungsId für zuständigen Lehrer T.Giesen@lmail.de

		// Die SQL-Injection besteht aus 2 Teilen.
		// Der erste Teil sorgt für eine defekte JSON in der Datenbank-Spalte daten. Dadurch sind weiter SQL-Befehle möglich, wie eine andere ID zu nutzen oder weitere SQL-Befehle auszuführen
		let SQL_INJECTION = `Injection}' WHERE id = ${sqlInjectionTargetId};`
		// Der zweite Teil führt ein UPDATE auf die Daten aus den Teilleistungen für die legalTeileistungsId und ersetzen diese in der Teilleistungen mit sqlInjectionTargetId.
		// So wird sichergestellt, dass die vorher zerstörte JSON-Struktur wieder repariert wird und dekodiert bzw. kodiert werden kann.
		// Zusätzlich werden alle null-Werte mit 1337 ersetzt für die sqlInjectionTargetId sowie die LerngruppenId durch die richtige Id ersetzt. So kann M.Gehring@lmail.de auch den Schüler finden.
		// Der letzte Bereich setzt einen Kommentar in dem SQL-Befehl, so wird der Rest des eigentlichen SQL-Befehls ignoriert. Im eigentlichen SQL-Befehl würde noch WHERE id = ${legalTeileistungsId} kommen.
		let SQL_INJECTION_UPDATE = ` UPDATE Teilleistungen SET daten = REPLACE(REPLACE(REPLACE((SELECT daten FROM Teilleistungen WHERE id = ${legalTeileistungsId}), ${legalTeileistungsId}, ${sqlInjectionTargetId}), 'null', 1337), '68', 26) WHERE id = ${sqlInjectionTargetId};--`;

		let SQL_GESAMT = SQL_INJECTION + SQL_INJECTION_UPDATE;

		// bereite Data für die Injection vor, vollständiger Body wird nicht benötigt
		const bodyData = {
			id: legalTeileistungsId,
			note: SQL_GESAMT,
		}

		// sende TeilleistungsDaten als Lehrer T.Giesen@lmail.de mit der SQL-Injection
		const responseOfInjection = await apiServiceAuth.post(`/api/teilleistung`, {
			body: JSON.stringify(bodyData)
		});

		expect(responseOfInjection.status).toBe(200)

		// rufe Daten als M.Gehring@lmail.de ab
		const responseAfterInjection = await apiServiceAuthInjected.get(`/api/daten`);
		expect(responseAfterInjection.status).toBe(200);
		const _dataAfterInjection = await parse(await responseAfterInjection.blob());

		const schuelerAfterInjection = _dataAfterInjection.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		// überprüfe Daten
		expect(schuelerAfterInjection.nachname).toBe("Bödefeld")
		expect(schuelerAfterInjection.vorname).toBe("Martin")

		expect(schuelerAfterInjection.leistungsdaten.getFirst().teilleistungen.getFirst().id).toBe(sqlInjectionTargetId)
		expect(schuelerAfterInjection.leistungsdaten.getFirst().teilleistungen.getFirst().note).toBe(null) // Injection war nicht erfolgreich, sollte eigentlich 1337 sein
	});

	test("Eine SQL-Injection auf POST Ankreuzkompetenzen um Daten einer anderen Id zu manipulieren. Der Angriff ist nicht erfolgreich.", async () => {
		const response = await apiServiceAuthInjected.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		// aktueller Lehrer (auth) M.Gehring@lmail.de ist für diesen Schüler zuständig
		const schuelerSQLInjectionTargetID = 3029

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		expect(schueler.nachname).toBe("Lindemann")
		expect(schueler.vorname).toBe("Stefanie")

		const sqlInjectionTargetId = 18622
		expect(schueler.ankreuzkompetenzen.get(1).id).toBe(sqlInjectionTargetId)
		// Hinweis: nach Ausführen des Tests stimmt diese Abfrage nicht mehr, ggf. auskommentieren oder DB neu aufsetzen bei lokalen Tests
		expect(schueler.ankreuzkompetenzen.get(1).stufen).toStrictEqual([false,false,false,false,false])

		const legalAnkreuzkompetenzenId = 18621 // AnkreuzkompetenzenId für zuständigen Lehrer T.Giesen@lmail.de, SchülerID = 3029, KlassenID = 5

		// Die SQL-Injection besteht aus 2 Teilen.
		// Der erste Teil sorgt für eine defektes Array der Stufen in der Datenbank-Spalte daten. Dadurch sind weiter SQL-Befehle möglich, wie eine andere ID zu nutzen oder weitere SQL-Befehle auszuführen
		let SQL_INJECTION = `Injection}' WHERE id = ${sqlInjectionTargetId};`
		// Der zweite Teil führt ein UPDATE auf die Daten aus den Ankreuzkompetenzen für die legalAnkreuzkompetenzenId und ersetzen diese in der Ankreuzkompetenzen mit sqlInjectionTargetId.
		// So wird sichergestellt, dass die vorher zerstörte JSON-Struktur wieder repariert wird und dekodiert bzw. kodiert werden kann.
		// Zusätzlich werden alle false-Werte im Array mit 1337 ersetzt für die sqlInjectionTargetId.
		// Der letzte Bereich setzt einen Kommentar in dem SQL-Befehl, so wird der Rest des eigentlichen SQL-Befehls ignoriert. Im eigentlichen SQL-Befehl würde noch ]WHERE id = ${legalAnkreuzkompetenzenId} kommen.
		let SQL_INJECTION_UPDATE = ` UPDATE Ankreuzkompetenzen SET daten = REPLACE(REPLACE(REPLACE((SELECT daten FROM Ankreuzkompetenzen WHERE id = ${legalAnkreuzkompetenzenId}), ${legalAnkreuzkompetenzenId}, ${sqlInjectionTargetId}), 'false', 1337), '68', 24) WHERE id = ${sqlInjectionTargetId};--`;

		let SQL_GESAMT = SQL_INJECTION + SQL_INJECTION_UPDATE;

		// bereite Data für die Injection vor, vollständiger Body wird nicht benötigt
		const bodyData = {
			id: legalAnkreuzkompetenzenId,
			stufen: [SQL_GESAMT],
		}

		// sende AnkreuzkompetenzDaten als Lehrer T.Giesen@lmail.de mit der SQL-Injection
		const responseOfInjection = await apiServiceAuth.post(`/api/ankreuzkompetenz`, {
			body: JSON.stringify(bodyData)
		});
		expect(responseOfInjection.status).toBe(500) // SQL-Injection war nicht erfolgreich, da der Index 0 des Arrays kein Boolean-Wert ist
	});

	test("Eine SQL-Injection auf POST SchuelerBemerkung um Daten einer anderen Id zu manipulieren. Der Angriff ist nicht erfolgreich", async () => {
		const response = await apiServiceAuthInjected.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		// aktueller Lehrer (auth) M.Gehring@lmail.de ist für diesen Schüler zuständig
		const schuelerSQLInjectionTargetID = 2934

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		expect(schueler.nachname).toBe("Meyworm")
		expect(schueler.vorname).toBe("Marco")

		const legalSchuelerId = 3118 // SchuelerId für zuständigen Lehrer T.Giesen@lmail.de, KlassenId = 5

		// Die SQL-Injection besteht aus 2 Teilen.
		// Der erste Teil sorgt für eine defekte JSON in der Datenbank-Spalte daten. Dadurch sind weiter SQL-Befehle möglich, wie eine andere ID zu nutzen oder weitere SQL-Befehle auszuführen
		let SQL_INJECTION = `Injection}' WHERE id = ${schuelerSQLInjectionTargetID};`
		// Der zweite Teil führt ein UPDATE auf die Daten aus den Bemerkungen für die legalSchuelerId aus und ersetzen diese in der Teilleistungen mit sqlInjectionTargetId.
		// So wird sichergestellt, dass die vorher zerstörte JSON-Struktur wieder repariert wird und dekodiert bzw. kodiert werden kann.
		// Zusätzlich werden gesamten Daten von der legalSchuelerId in die schuelerSQLInjectionTargetID kopiert sowie die Id richtig gesetzt. Dadurch ist unter der schuelerSQLInjectionTargetID ein anderer Schüler aufzufinden.
		// Der letzte Bereich setzt einen Kommentar in dem SQL-Befehl, so wird der Rest des eigentlichen SQL-Befehls ignoriert. Im eigentlichen SQL-Befehl würde noch WHERE id = ${legalSchuelerId} kommen.
		let SQL_INJECTION_UPDATE = ` UPDATE Schueler SET daten = REPLACE((SELECT daten FROM Schueler WHERE id = ${legalSchuelerId}), ${legalSchuelerId}, ${schuelerSQLInjectionTargetID}) WHERE id = ${schuelerSQLInjectionTargetID};--`;

		let SQL_GESAMT = SQL_INJECTION + SQL_INJECTION_UPDATE;

		// bereite Data für die Injection vor, vollständiger Body wird nicht benötigt
		const bodyData = {
			id: legalSchuelerId,
			patch: {
				ASV: SQL_GESAMT
			}
		}

		// sende BemerkungenDaten als Lehrer T.Giesen@lmail.de mit der SQL-Injection
		const responseOfInjection = await apiServiceAuth.post(`/api/bemerkungen`, {
			body: JSON.stringify(bodyData)
		});
		expect(responseOfInjection.status).toBe(200)

		// rufe Daten als M.Gehring@lmail.de ab
		const responseAfterInjection = await apiServiceAuthInjected.get(`/api/daten`);
		expect(responseAfterInjection.status).toBe(200);
		const _dataAfterInjection = await parse(await responseAfterInjection.blob());

		const schuelerAfterInjection = _dataAfterInjection.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		// überprüfe Daten als anderen Schüler
		expect(schuelerAfterInjection.nachname).toBe("Meyworm") //SQL Injection nicht erfolgreich, sollte eigentlich Necker sein
		expect(schuelerAfterInjection.vorname).toBe("Marco") //SQL Injection nicht erfolgreich, sollte eigentlich Lukas sein
	});

	test("Eine SQL-Injection auf POST SchuelerLernabschnitt um Daten einer anderen Id zu manipulieren. Der Angriff ist nicht erfolgreich", async () => {
		const response = await apiServiceAuthInjected.get(`/api/daten`);
		expect(response.status).toBe(200);
		const _data = await parse(await response.blob());

		// aktueller Lehrer (auth) M.Gehring@lmail.de ist für diesen Schüler zuständig
		const schuelerSQLInjectionTargetID = 2939

		const schueler = _data.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		expect(schueler.nachname).toBe("Lahusen")
		expect(schueler.vorname).toBe("Christin")

		const legalSchuelerId = 3141  // SchuelerID für zuständigen Lehrer T.Giesen@lmail.de, KlassenId = 5
		const legalSchuelerLernabschnitt = 12511 // SchuelerLernabschnittId für zuständigen Lehrer T.Giesen@lmail.de, KlassenId = 5

		// Die SQL-Injection besteht aus 2 Teilen.
		// Der erste Teil sorgt für eine defekte JSON in der Datenbank-Spalte daten. Dadurch sind weiter SQL-Befehle möglich, wie eine andere ID zu nutzen oder weitere SQL-Befehle auszuführen
		let SQL_INJECTION = `Injection}' WHERE id = ${schuelerSQLInjectionTargetID};`
		// Der zweite Teil führt ein UPDATE auf die Daten aus den Bemerkungen für die legalSchuelerId aus und ersetzen diese in dem Lernabschnitt mit sqlInjectionTargetId.
		// So wird sichergestellt, dass die vorher zerstörte JSON-Struktur wieder repariert wird und dekodiert bzw. kodiert werden kann.
		// Zusätzlich werden gesamten Daten von der legalSchuelerId in die schuelerSQLInjectionTargetID kopiert sowie die Id richtig gesetzt. Dadurch ist unter der schuelerSQLInjectionTargetID ein anderer Schüler aufzufinden.
		// Der letzte Bereich setzt einen Kommentar in dem SQL-Befehl, so wird der Rest des eigentlichen SQL-Befehls ignoriert. Im eigentlichen SQL-Befehl würde noch WHERE id = ${legalSchuelerLernabschnittId} kommen.
		let SQL_INJECTION_UPDATE = ` UPDATE Schueler SET daten = REPLACE((SELECT daten FROM Schueler WHERE id = ${legalSchuelerId}), ${legalSchuelerId}, ${schuelerSQLInjectionTargetID}) WHERE id = ${schuelerSQLInjectionTargetID};--`;

		let SQL_GESAMT = SQL_INJECTION + SQL_INJECTION_UPDATE;

		// bereite Data für die Injection vor, vollständiger Body wird nicht benötigt
		const bodyData = {
			id: legalSchuelerLernabschnitt,
			fehlstundenGesamt: SQL_GESAMT,
		}

		// sende BemerkungenDaten als Lehrer T.Giesen@lmail.de mit der SQL-Injection
		const responseOfInjection = await apiServiceAuth.post(`/api/lernabschnitt`, {
			body: JSON.stringify(bodyData)
		});
		expect(responseOfInjection.status).toBe(200)

		// rufe Daten als M.Gehring@lmail.de ab
		const responseAfterInjection = await apiServiceAuthInjected.get(`/api/daten`);
		expect(responseAfterInjection.status).toBe(200);
		const _dataAfterInjection = await parse(await responseAfterInjection.blob());

		const schuelerAfterInjection = _dataAfterInjection.schueler.elementData.find((s: ENMSchueler) => {
			return s.id === schuelerSQLInjectionTargetID;
		})

		// überprüfe Daten als anderen Schüler
		expect(schuelerAfterInjection.nachname).toBe("Lahusen") //SQL Injection nicht erfolgreich, sollte eigentlich Wolefsen sein
		expect(schuelerAfterInjection.vorname).toBe("Christin") //SQL Injection nicht erfolgreich, sollte eigentlich Antje sein
	});

})
