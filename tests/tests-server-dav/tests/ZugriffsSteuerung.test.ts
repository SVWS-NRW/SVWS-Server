import { describe, expect, test } from "vitest";
import { getApiService } from "./utils/RequestBuilder.js"

const allowDestructiveTests = process.env.MODE === 'allowDestructiveTests'

describe("Dav Api, prüfe ob mit falschen Username/Password alle Endpunkte geschlossen sind", () => {
	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

	// Credentials welche keine Auth bekommen sollten
	const apiService = getApiService('Admin', 'wrongPassword')

	describe.each([{schema: "GymAbiDav"}])('gegen %s', ({schema}) => {
		test("Einfacher Request gegen die DAV API ohne Pfad", async () => {
			const response = await apiService.propfind(`/dav/${schema}`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("Request gegen die DAV API mit Benutzer -1", async () => {
			const response = await apiService.propfind(`/dav/${schema}/benutzer/-1`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("Request gegen die DAV API Adressbücher", async () => {
			const response = await apiService.propfind(`/dav/${schema}/adressbuecher`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("Request gegen die DAV API Adressbücher -1", async () => {
			const response = await apiService.propfind(`/dav/${schema}/adressbuecher/-1`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("Request gegen die DAV API Kalender", async () => {
			const response = await apiService.propfind(`/dav/${schema}/kalender`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("Request gegen die DAV API Kalender -1", async () => {
			const response = await apiService.propfind(`/dav/${schema}/kalender/-1`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("REPORT Request gegen die DAV API Adressbücher -1", async () => {
			const response = await apiService.report(`/dav/${schema}/adressbuecher/-1`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("REPORT Request gegen die DAV API Adressbücher -1/-1.vcf", async () => {
			const response = await apiService.report(`/dav/${schema}/adressbuecher/-1/-1.vcf`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});


		test("REPORT Request gegen die DAV API Kalender -1", async () => {
			const response = await apiService.report(`/dav/${schema}/kalender/-1`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("PUT Request mit If-None-Match Header", async () => {
			const response = await apiService.put(`/dav/${schema}/kalender/-1/something-something.ics`, {
				headers: {
					"Content-Type": "Text/Calendar",
					"If-None-Match": "",
				},
			});
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test("PUT Request mit If-Match Header", async () => {
			const response = await apiService.put(`/dav/${schema}/kalender/-1/something-something.ics`, {
				headers: {
					"Content-Type": "Text/Calendar",
					"If-Match": "*",
				},
			}
			);
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test.runIf(allowDestructiveTests)("DELETE Request mit If-Match Header", async () => {
			const response = await apiService.delete(`/dav/${schema}/kalender/-1/something-something.ics`, {
				headers: {
					"Content-Type": "Text/Calendar",
					"If-Match": "*",
				},
			});
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});

		test.runIf(allowDestructiveTests)("DELETE Request gegen Kalender -1", async () => {
			const response = await apiService.delete(`/dav/${schema}/kalender/-1`, {headers: {"Content-Type": "Text/Calendar"}});
			expect(response).toBeDefined();
			expect(response!.status).toBe(401);
		});
	});
});

describe("Dav Api, prüfe ob privilegierter Benutzer auf Daten anderer Benutzer zugreift", () => {
	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
	const calender_collection_body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<D:propfind xmlns:D='DAV:'\n" +
			"\txmlns:A='http://apple.com/ns/ical/'>\n" +
			"\t<D:prop>\n" +
			"\t\t<D:resourcetype />\n" +
			"\t\t<D:displayname />\n" +
			"\t\t<D:current-user-privilege-set />\n" +
			"\t\t<A:calendar-color />\n" +
			"\t</D:prop>\n" +
			"</D:propfind>\n"

	test("Zugriff auf privaten Kalender eines anderen Benutzers nicht gestattet", async () => {
		// Admin kann seine eigenen Kalender ansehen.
		const apiServiceAdmin = getApiService('Admin', '');
		const response = await apiServiceAdmin.propfind("/dav/GymAbiDav/kalender", {body: calender_collection_body});
		const content = await response!.text()
		expect(response!.status).toBe(207)
		expect(content).toContain('Eigener Kalender')
		expect(content).toContain('/dav/GymAbiDav/kalender/persoenlich_2')

		// Ande Account kann ebenfalls Kalender enthalten, dort ist nicht der des Admins mit drin
		const apiServiceAnde = getApiService('Ande', '');
		const responseAnde = await apiServiceAnde.propfind("/dav/GymAbiDav/kalender", {body: calender_collection_body});
		const contentAnde = await responseAnde!.text()
		expect(responseAnde!.status).toBe(207)
		expect(contentAnde).toContain('Eigener Kalender')
		expect(contentAnde).not.toContain('/dav/GymAbiDav/kalender/persoenlich_2')

		// Admin Account kann seinen eigenen Kalender ansehen
		const responseAllowed = await apiServiceAdmin.propfind("/dav/GymAbiDav/kalender/persoenlich_2");
		expect(responseAllowed!.status).toBe(207);

		// Ande Account kann seinen eigenen Kalender ansehen
		const responseNotAllowed = await apiServiceAnde.propfind("/dav/GymAbiDav/kalender/persoenlich_2");
		const contentNotAllowed = await responseNotAllowed!.text()
		expect(responseNotAllowed!.status).toBe(404);
		expect(contentNotAllowed).toContain('<d:error')
	});

	test("Testet, ob der Nutzer mit Lese aber ohne Schreibrechte die entsprechenden Priviliges erhält.", async () => {
		// Admin kann seine eigenen Kalender ansehen.
		const apiServiceAdmin = getApiService('Bagi', '');
		const response = await apiServiceAdmin.propfind("/dav/GymAbiDav/kalender", {body: calender_collection_body});
		const content = await response!.text()
		expect(response!.status).toBe(207)

		// Erwarte 2 Kalender, einen gemeinsamen und einen eigenen.
		expect((content.match(/Gemeinsamer Kalender/g) || []).length).toBe(1)
		expect((content.match(/Eigener Kalender/g) || []).length).toBe(1)

		const expectedEigenerKalender: string = '<d:prop><d:displayname>Eigener Kalender</d:displayname><d:resourcetype><d:collection/><cal:calendar/></d:resourcetype><d:current-user-privilege-set><d:privilege><d:read-current-user-privilege-set/></d:privilege><d:privilege><d:read/></d:privilege><d:privilege><d:read-acl/></d:privilege><d:privilege><d:all/></d:privilege><d:privilege><d:write/></d:privilege></d:current-user-privilege-set></d:prop>'
		const expectedGemeinsamerKalender: string = '<d:prop><d:displayname>Gemeinsamer Kalender</d:displayname><d:resourcetype><d:collection/><cal:calendar/></d:resourcetype><d:current-user-privilege-set><d:privilege><d:read-current-user-privilege-set/></d:privilege><d:privilege><d:read/></d:privilege><d:privilege><d:read-acl/></d:privilege></d:current-user-privilege-set></d:prop><d:status>HTTP/1.1 200 OK</d:status></d:propstat><d:propstat><d:prop><ical:calendar-color/></d:prop>'

		expect(content).toContain(expectedEigenerKalender)
		expect(content).toContain(expectedGemeinsamerKalender)
	})

	test("Testet, ob der Gemeinsame Kalender nicht zugeordneten Nutzern nicht angezeigt wird.", async () => {
		// Admin kann seine eigenen Kalender ansehen.
		const apiServiceAdmin = getApiService('Admin', '');
		const response = await apiServiceAdmin.propfind("/dav/GymAbiDav/kalender", {body: calender_collection_body});
		const content = await response!.text()
		expect(response!.status).toBe(207)

		// Erwarte 2 Kalender, einen gemeinsamen und einen eigenen.
		expect((content.match(/Gemeinsamer Kalender/g) || []).length).toBe(0)
	})
});
