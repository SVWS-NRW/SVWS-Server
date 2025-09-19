import { describe, expect, test } from "vitest";
import { getApiService } from "./utils/RequestBuilder.js"

describe("Adressbuch Dav", () => {
	const apiService = getApiService('Admin', '')

	// Verschiedene Bodies die bei Webdav anfragen verwendet werden
	const bodyDataDav: string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<D:propfind\n" +
			"\txmlns:D='DAV:'\n" +
			"\txmlns:A='http://apple.com/ns/ical/'\n" +
			"\txmlns:C='urn:ietf:params:xml:ns:caldav'>\n" +
			"\t<D:prop>\n" +
			"\t\t<D:resourcetype/>\n" +
			"\t\t<D:owner/>\n" +
			"\t\t<D:displayname/>\n" +
			"\t\t<D:current-user-principal/>\n" +
			"\t\t<D:current-user-privilege-set/>\n" +
			"\t\t<A:calendar-color/>\n" +
			"\t\t<C:calendar-home-set/>\n" +
			"\t</D:prop>\n" +
			"</D:propfind>\n"

	const bodyDataAdressbuecher: string = "<propfind xmlns=\"DAV:\"\n" +
			"\t\t  xmlns:card='urn:ietf:params:xml:ns:carddav'>\n" +
			"\t<prop>\n" +
			"\t\t<resourcetype/>\n" +
			"\t\t<displayname/>\n" +
			"\t\t<card:supported-address-data/>\n" +
			"\t</prop>\n" +
			"</propfind>"

	const bodyDataAdressbuchSchueler: string = "<propfind xmlns=\"DAV:\"\n" +
			"\txmlns:card=\"urn:ietf:params:xml:ns:carddav\"\n" +
			"\txmlns:cs=\"http://calendarserver.org/ns/\" xmlns:d=\"DAV:\">\n" +
			"\t<prop>\n" +
			"\t\t<resourcetype />\n" +
			"\t\t<getetag />\n" +
			"\t\t<cs:getctag />\n" +
			"\t</prop>\n" +
			"</propfind>\n"

	const bodyreportDataAdressbuchSchueler: string = "<card:addressbook-multiget\n" +
			"\txmlns:card=\"urn:ietf:params:xml:ns:carddav\"\n" +
			"\txmlns:cs=\"http://calendarserver.org/ns/\"\n" +
			"\txmlns:d=\"DAV:\">\n" +
			"\t<d:prop>\n" +
			"\t\t<d:getetag/>\n" +
			"\t\t<card:address-data/>\n" +
			"\t</d:prop>\n" +
			"\t<d:href>/dav/gymabi/adressbuecher/schueler/Schueler_1001.vcf</d:href>\n" +
			"\t<d:href>/dav/gymabi/adressbuecher/schueler/Schueler_1002.vcf</d:href>\n" +
			"</card:addressbook-multiget>\n"

	const bodySyncCollection: string = "<sync-collection xmlns=\"DAV:\" xmlns:card=\"urn:ietf:params:xml:ns:carddav\" xmlns:cs=\"http://calendarserver.org/ns/\" xmlns:d=\"DAV:\">\n" +
			"<sync-token>0</sync-token>\n" +
			"<sync-level>1</sync-level>\n" +
			"<prop>\n" +
			"<getetag/>\n" +
			"<card:address-data/>\n" +
			"</prop>\n" +
			"</sync-collection>"

	const bodyAdressbuecherErzieherIssue1283 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<C:addressbook-multiget xmlns:D=\"DAV:\"\n" +
			"\txmlns:C=\"urn:ietf:params:xml:ns:carddav\">\n" +
			"\t<D:prop>\n" +
			"\t\t<D:getetag />\n" +
			"\t\t<C:address-data Content-Type='text/vcard' version='3.0' />\n" +
			"\t</D:prop>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2095.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2098.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2105.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2107.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2114.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2288.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2289.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2292.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2481.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2294.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2295.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2298.vcf</D:href>\n" +
			"\t<D:href>/dav/gymabi/adressbuecher/erzieher/Erzieher_2307.vcf</D:href>\n" +
			"</C:addressbook-multiget>\n"

	describe.each([{schema: "GymAbiDav"}])('Adressbücher Dav Tests gegen %s', ({schema}) => {
		test("Prüft das XML Format gleich geblieben ist.", async () => {
			const response = await apiService.propfind(`/dav/${schema}`, {body: bodyDataDav})
			const xmlAsString = await response!.text()

			expect(response).toBeDefined();
			expect(response!.status).toBe(207);
			expect(xmlAsString).toMatchSnapshot()
		});

		test("Prüft das XML Format für Adressbücher gleich geblieben ist.", async () => {
			const response = await apiService.propfind(`/dav/${schema}/adressbuecher`, {body: bodyDataAdressbuecher})
			const xmlAsString = await response!.text()

			expect(response).toBeDefined();
			expect(response!.status).toBe(207);
			expect(xmlAsString).toMatchSnapshot()
		});

		// Derzeit deaktiviert da sehr großes Output in den Snapshots
		test.skip("Prüft das XML Format für Adressbücher der Schüler gleich geblieben ist.", async () => {
			const response = await apiService.propfind(`/dav/${schema}/adressbuecher/schueler`, {body: bodyDataAdressbuchSchueler})
			const xmlAsString = await response!.text()

			expect(response).toBeDefined();
			expect(response!.status).toBe(207);
			expect(xmlAsString).toMatchSnapshot()
		});

		test("Testet Report-Anfrage auf notwendige Informationen.", async () => {
			const response = await apiService.report(`/dav/${schema}/adressbuecher/schueler`, {body: bodyreportDataAdressbuchSchueler})
			const xmlAsString = await response!.text()

			expect(response).toBeDefined();
			expect(response!.status).toBe(207);
			expect(xmlAsString).toMatchSnapshot()
		});

		// Derzeit deaktiviert da sehr großes Output in den Snapshots
		test.skip("Prüft das XML Format für Sync-Collection-Anfrage.", async () => {
			const response = await apiService.report(`/dav/${schema}/adressbuecher/schueler`, {body: bodySyncCollection})
			const xmlAsString = await response!.text()

			expect(response).toBeDefined();
			expect(response!.status).toBe(207);
			expect(xmlAsString).toMatchSnapshot()
		});

		/**
		 * Test zum Issue 1283 - Unmarshalling funktionierte nicht, wenn das
		 * ContentType-Attribut im CardAddressData großgeschrieben wurde.
		 */
		// Dieser Test schlägt fehl
		test.skip("Issue 1283 - Unmarshalling funktionierte nicht", async () => {
			const response = await apiService.propfind(`/dav/${schema}/adressbuecher/schueler`, {body: bodyAdressbuecherErzieherIssue1283})
			const xmlAsString = await response!.text()

			expect(response).toBeDefined();
			expect(response!.status).toBe(207);
			expect(xmlAsString).toMatchSnapshot()
		});
	});

	describe("Kalender Dav", () => {
		const apiServiceAnde = getApiService('Ande', '')
		const apiServiceBagi = getApiService('BAGI', '')

		const bodyDataDavCalCollection: string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<D:propfind xmlns:D='DAV:'\n" +
				"\txmlns:A='http://apple.com/ns/ical/'>\n" +
				"\t<D:prop>\n" +
				"\t\t<D:resourcetype />\n" +
				"\t\t<D:displayname />\n" +
				"\t\t<D:current-user-privilege-set />\n" +
				"\t\t<A:calendar-color />\n" +
				"\t</D:prop>\n" +
				"</D:propfind>\n"

		const bodyDataDavCal: string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<D:propfind\n" +
				"\txmlns:D='DAV:'\n" +
				"\txmlns:C='urn:ietf:params:xml:ns:caldav'\n" +
				"\txmlns:CS='http://calendarserver.org/ns/'>\n" +
				"\t<D:prop>\n" +
				"\t\t<D:resourcetype/>\n" +
				"\t\t<D:owner/>\n" +
				"\t\t<D:current-user-principal/>\n" +
				"\t\t<D:current-user-privilege-set/>\n" +
				"\t\t<D:supported-report-set/>\n" +
				"\t\t<C:supported-calendar-component-set/>\n" +
				"\t\t<CS:getctag/>\n" +
				"\t</D:prop>\n" +
				"</D:propfind>\n"

		describe.each([{schema: "GymAbiDav"}])('Kalender Dav Tests gegen %s', ({schema}) => {
			test("Testet Property-Suche auf die Liste von  mit Account Ande", async () => {
				const response = await apiServiceAnde.propfind(`/dav/${schema}/kalender`, {body: bodyDataDavCalCollection})
				const xmlAsString = await response!.text()

				expect(response).toBeDefined();
				expect(response!.status).toBe(207);
				expect(xmlAsString).toMatchSnapshot()
			});

			test("Testet Property-Suche auf die Liste von Kalendern mit Account Bagi", async () => {
				const response = await apiServiceBagi.propfind(`/dav/${schema}/kalender`, {body: bodyDataDavCalCollection})
				const xmlAsString = await response!.text()

				expect(response).toBeDefined();
				expect(response!.status).toBe(207);
				expect(xmlAsString).toMatchSnapshot()
			});

			test("Ruft ein Propfind auf den Gemeinsamen Kalender auf.", async () => {
				const response = await apiServiceAnde.propfind(`/dav/${schema}/kalender/oeffentlich_1`, {body: bodyDataDavCal})
				const xmlAsString = await response!.text()

				expect(response).toBeDefined();
				expect(response!.status).toBe(207);
				expect(xmlAsString).toMatchSnapshot()
			});
		});
	});
});
