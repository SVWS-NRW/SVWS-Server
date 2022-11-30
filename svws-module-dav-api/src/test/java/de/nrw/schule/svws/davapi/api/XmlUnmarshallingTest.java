package de.nrw.schule.svws.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import de.nrw.schule.svws.davapi.model.dav.Getetag;
import de.nrw.schule.svws.davapi.model.dav.Prop;
import de.nrw.schule.svws.davapi.model.dav.SyncCollection;
import de.nrw.schule.svws.davapi.model.dav.cal.CalendarMultiget;
import de.nrw.schule.svws.davapi.model.dav.cal.CalendarQuery;
import de.nrw.schule.svws.davapi.model.dav.card.AddressbookMultiget;
import de.nrw.schule.svws.davapi.model.dav.card.CardAddressData;
import de.nrw.schule.svws.davapi.util.XmlUnmarshallingUtil;

/**
 * Testklasse für die {@link XmlUnmarshallingUtil}
 *
 */
public class XmlUnmarshallingTest {

	private final static String ADDRESSBOOK_MULTIGET = """
			        <card:addressbook-multiget xmlns:card="urn:ietf:params:xml:ns:carddav" xmlns:cs="http://calendarserver.org/ns/" xmlns:d="DAV:">
			  <d:prop>
			    <d:getetag/>
			    <card:address-data/>
			  </d:prop>
			        <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1107.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1192.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1202.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1206.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1208.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1214.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1223.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1226.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1233.vcf</d:href>
			  <d:href>/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1256.vcf</d:href>
			</card:addressbook-multiget>
			        """;
	private static final Collection<String> HREF_VALUES = Arrays
			.asList(new String[] { "/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1107.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1192.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1202.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1206.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1208.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1214.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1223.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1226.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1233.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1256.vcf" });

	/**
	 * Test a bug: Beim Unmarshalling des gegebenen XML-Strings wurde nur das letzte
	 * <d:href> element der Liste deserialisiert, die anderen verworfen.
	 */
	@Test
	void testUnmarshallingAddressbookMultiget() {
		Optional<AddressbookMultiget> tryUnmarshal = XmlUnmarshallingUtil.tryUnmarshal(ADDRESSBOOK_MULTIGET,
				AddressbookMultiget.class);
		// unmarshall war erfolgreich
		assertTrue(tryUnmarshal.isPresent());
		AddressbookMultiget addressbookMultiget = tryUnmarshal.get();
		// Listenelemente sind vollständig
		assertEquals(10, addressbookMultiget.getHref().size());
		assertTrue(addressbookMultiget.getHref().containsAll(HREF_VALUES));
		assertTrue(HREF_VALUES.containsAll(addressbookMultiget.getHref()));
		// Props sind gesetzt
		assertNotNull(addressbookMultiget.getProp());
		assertNotNull(addressbookMultiget.getProp().getAddressData());
		assertNotNull(addressbookMultiget.getProp().getGetetag());
	}

	/**
	 * testet das Unmarshalling eines Multiget-Requests
	 * 
	 * @throws JsonProcessingException beim Lesen und Schreiben von Werten über den
	 *                                 {@link ObjectMapper}
	 */
	@Test
	void testMarshallingUnmarshallingAddressbookMultiget() throws JsonProcessingException {
		AddressbookMultiget mg = prepareCustomMultiget();
		ObjectMapper mapper = getCustomMapper();
		String xmlFromCustomMapper = mapper.writeValueAsString(mg);
		AddressbookMultiget customMapperMultiget = mapper.readValue(xmlFromCustomMapper, AddressbookMultiget.class);

		// vom custom mapper erzeugtes Multiget gleiche Liste an HRefs enthalten
		assertEquals(mg.getHref().size(), customMapperMultiget.getHref().size());
		assertTrue(mg.getHref().containsAll(customMapperMultiget.getHref()));
		assertTrue(customMapperMultiget.getHref().containsAll(mg.getHref()));

		// XmlUnmarshallingUtil unmarshalled Multiget soll vorhanden und gleiche HREFs
		// enthalten
		Optional<AddressbookMultiget> tryUnmarshal = XmlUnmarshallingUtil.tryUnmarshal(xmlFromCustomMapper,
				AddressbookMultiget.class);
		assertTrue(tryUnmarshal.isPresent());
		AddressbookMultiget mgUnmarshalled = tryUnmarshal.get();

		assertEquals(mg.getHref().size(), mgUnmarshalled.getHref().size());
		assertTrue(mg.getHref().containsAll(mgUnmarshalled.getHref()));
		assertTrue(mgUnmarshalled.getHref().containsAll(mg.getHref()));

	}

	private static ObjectMapper getCustomMapper() {
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		ObjectMapper mapper = new XmlMapper(module);
		mapper.registerModule(new JakartaXmlBindAnnotationModule());
		return mapper;
	}

	private static AddressbookMultiget prepareCustomMultiget() {
		AddressbookMultiget mg = new AddressbookMultiget();
		Prop prop = new Prop();
		prop.setGetetag(new Getetag());
		prop.setAddressData(new CardAddressData());
		mg.setProp(prop);
		mg.getHref().addAll(HREF_VALUES);
		return mg;
	}

	/**
	 * Test a bug: XmlUnmarshaller instantiiert Klassen, die nicht zum xml passen
	 * 
	 * @throws IOException beim Einlesen des InputStreams
	 */
	@Test
	void testUnmarshallingIntoObjects() throws IOException {
		// mit sync-collection
		String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<sync-collection xmlns=\"DAV:\"><sync-token/><sync-level>1</sync-level><prop><getcontenttype/><getetag/></prop></sync-collection>";
		Optional<CalendarMultiget> multiget;
		Optional<CalendarQuery> calendarQuery;
		Optional<SyncCollection> syncCollection;
		try (InputStream inputStream = stringToInputStream(inputString)) {
			multiget = XmlUnmarshallingUtil.tryUnmarshal(inputStream, CalendarMultiget.class);
			assertFalse(multiget.isPresent());
		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			calendarQuery = XmlUnmarshallingUtil.tryUnmarshal(inputStream, CalendarQuery.class);
			assertFalse(calendarQuery.isPresent());

		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			syncCollection = XmlUnmarshallingUtil.tryUnmarshal(inputStream, SyncCollection.class);
			assertTrue(syncCollection.isPresent());

			// mit calendar-multiget
			inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<C:calendar-multiget xmlns:D=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\"><D:prop><D:getetag/><C:calendar-data/></D:prop><D:href>/db/gymabi/dav/kalender/5/6.ics</D:href><D:href>/db/gymabi/dav/kalender/5/5.ics</D:href><D:href>/db/gymabi/dav/kalender/5/4.ics</D:href><D:href>/db/gymabi/dav/kalender/5/3.ics</D:href><D:href>/db/gymabi/dav/kalender/5/2.ics</D:href><D:href>/db/gymabi/dav/kalender/5/1.ics</D:href></C:calendar-multiget>";
		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			multiget = XmlUnmarshallingUtil.tryUnmarshal(inputStream, CalendarMultiget.class);
			assertTrue(multiget.isPresent());

		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			calendarQuery = XmlUnmarshallingUtil.tryUnmarshal(inputStream, CalendarQuery.class);
			assertFalse(calendarQuery.isPresent());

		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			syncCollection = XmlUnmarshallingUtil.tryUnmarshal(inputStream, SyncCollection.class);
			assertFalse(syncCollection.isPresent());
			inputStream.close();
		}
		// gleiche tests mit inputString anstelle von Stream
		// mit sync-collection
		inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<sync-collection xmlns=\"DAV:\"><sync-token/><sync-level>1</sync-level><prop><getcontenttype/><getetag/></prop></sync-collection>";
		multiget = XmlUnmarshallingUtil.tryUnmarshal(inputString, CalendarMultiget.class);
		assertFalse(multiget.isPresent());

		calendarQuery = XmlUnmarshallingUtil.tryUnmarshal(inputString, CalendarQuery.class);
		assertFalse(calendarQuery.isPresent());

		syncCollection = XmlUnmarshallingUtil.tryUnmarshal(inputString, SyncCollection.class);
		assertTrue(syncCollection.isPresent());

		// mit calendar-multiget
		inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<C:calendar-multiget xmlns:D=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\"><D:prop><D:getetag/><C:calendar-data/></D:prop><D:href>/db/gymabi/dav/kalender/5/6.ics</D:href><D:href>/db/gymabi/dav/kalender/5/5.ics</D:href><D:href>/db/gymabi/dav/kalender/5/4.ics</D:href><D:href>/db/gymabi/dav/kalender/5/3.ics</D:href><D:href>/db/gymabi/dav/kalender/5/2.ics</D:href><D:href>/db/gymabi/dav/kalender/5/1.ics</D:href></C:calendar-multiget>";
		multiget = XmlUnmarshallingUtil.tryUnmarshal(inputString, CalendarMultiget.class);
		assertTrue(multiget.isPresent());

		calendarQuery = XmlUnmarshallingUtil.tryUnmarshal(inputString, CalendarQuery.class);
		assertFalse(calendarQuery.isPresent());

		syncCollection = XmlUnmarshallingUtil.tryUnmarshal(inputString, SyncCollection.class);
		assertFalse(syncCollection.isPresent());
	}

	private static InputStream stringToInputStream(String input) {
		return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
	}
}
