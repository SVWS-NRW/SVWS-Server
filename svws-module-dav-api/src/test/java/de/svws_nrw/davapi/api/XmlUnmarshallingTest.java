package de.svws_nrw.davapi.api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import de.svws_nrw.davapi.model.dav.Getetag;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.SyncCollection;
import de.svws_nrw.davapi.model.dav.cal.CalendarMultiget;
import de.svws_nrw.davapi.model.dav.cal.CalendarQuery;
import de.svws_nrw.davapi.model.dav.card.AddressbookMultiget;
import de.svws_nrw.davapi.model.dav.card.CardAddressData;
import de.svws_nrw.davapi.util.XmlUnmarshallingUtil;

/**
 * Testklasse für die {@link XmlUnmarshallingUtil}
 *
 */
public class XmlUnmarshallingTest {

	private static final String ADDRESSBOOK_MULTIGET = """
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
			.asList(new String[]{"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1107.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1192.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1202.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1206.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1208.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1214.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1223.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1226.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1233.vcf",
					"/db/gymabi/carddav/adressbuecher/Kurs-L-GK1-2018-2/Schueler_1256.vcf"});

	/**
	 * Test a bug: Beim Unmarshalling des gegebenen XML-Strings wurde nur das
	 * letzte <d:href> element der Liste deserialisiert, die anderen verworfen.
	 */
	@Test
	void testUnmarshallingAddressbookMultiget() {
		final Optional<AddressbookMultiget> tryUnmarshal = XmlUnmarshallingUtil.tryUnmarshal(ADDRESSBOOK_MULTIGET,
				AddressbookMultiget.class);
		// unmarshall war erfolgreich
		assertTrue(tryUnmarshal.isPresent());
		final AddressbookMultiget addressbookMultiget = tryUnmarshal.get();
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
	 * @throws JsonProcessingException beim Lesen und Schreiben von Werten über
	 *                                 den {@link ObjectMapper}
	 */
	@Test
	void testMarshallingUnmarshallingAddressbookMultiget() throws JsonProcessingException {
		final AddressbookMultiget mg = prepareCustomMultiget();
		final ObjectMapper mapper = getCustomMapper();
		final String xmlFromCustomMapper = mapper.writeValueAsString(mg);
		final AddressbookMultiget customMapperMultiget = mapper.readValue(xmlFromCustomMapper,
				AddressbookMultiget.class);

		// vom custom mapper erzeugtes Multiget gleiche Liste an HRefs enthalten
		assertEquals(mg.getHref().size(), customMapperMultiget.getHref().size());
		assertTrue(mg.getHref().containsAll(customMapperMultiget.getHref()));
		assertTrue(customMapperMultiget.getHref().containsAll(mg.getHref()));

		// XmlUnmarshallingUtil unmarshalled Multiget soll vorhanden und gleiche
		// HREFs
		// enthalten
		final Optional<AddressbookMultiget> tryUnmarshal = XmlUnmarshallingUtil.tryUnmarshal(xmlFromCustomMapper,
				AddressbookMultiget.class);
		assertTrue(tryUnmarshal.isPresent());
		final AddressbookMultiget mgUnmarshalled = tryUnmarshal.get();

		assertEquals(mg.getHref().size(), mgUnmarshalled.getHref().size());
		assertTrue(mg.getHref().containsAll(mgUnmarshalled.getHref()));
		assertTrue(mgUnmarshalled.getHref().containsAll(mg.getHref()));

	}

	private static ObjectMapper getCustomMapper() {
		final JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		final ObjectMapper mapper = new XmlMapper(module);
		mapper.registerModule(new JakartaXmlBindAnnotationModule());
		return mapper;
	}

	private static AddressbookMultiget prepareCustomMultiget() {
		final AddressbookMultiget mg = new AddressbookMultiget();
		final Prop prop = new Prop();
		prop.setGetetag(new Getetag());
		prop.setAddressData(new CardAddressData());
		mg.setProp(prop);
		mg.getHref().addAll(HREF_VALUES);
		return mg;
	}

	/**
	 * Test a bug: XmlUnmarshaller instantiiert Klassen, die nicht zum xml
	 * passen
	 *
	 * @throws IOException beim Einlesen des InputStreams
	 */
	@Test
	void testUnmarshallingIntoObjects() throws IOException {
		// mit sync-collection
		String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<sync-collection xmlns=\"DAV:\"><sync-token/><sync-level>1</sync-level><prop><getcontenttype/><getetag/></prop></sync-collection>";
		try (InputStream inputStream = stringToInputStream(inputString)) {
			assertThrows(IOException.class, () -> {
				XmlUnmarshallingUtil.unmarshal(inputStream, CalendarMultiget.class);
			});
		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			assertThrows(IOException.class, () -> XmlUnmarshallingUtil.unmarshal(inputStream, CalendarQuery.class));
		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			assertDoesNotThrow(() -> XmlUnmarshallingUtil.unmarshal(inputStream, SyncCollection.class));
		}
		// mit calendar-multiget
		inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<C:calendar-multiget xmlns:D=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\"><D:prop><D:getetag/><C:calendar-data/></D:prop><D:href>/dav/gymabi/kalender/5/6.ics</D:href><D:href>/dav/gymabi/kalender/5/5.ics</D:href><D:href>/dav/gymabi/kalender/5/4.ics</D:href><D:href>/dav/gymabi/kalender/5/3.ics</D:href><D:href>/dav/gymabi/kalender/5/2.ics</D:href><D:href>/dav/gymabi/kalender/5/1.ics</D:href></C:calendar-multiget>";
		try (InputStream inputStream = stringToInputStream(inputString)) {
			assertDoesNotThrow(() -> XmlUnmarshallingUtil.unmarshal(inputStream, CalendarMultiget.class));
		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			assertThrows(IOException.class, () -> XmlUnmarshallingUtil.unmarshal(inputStream, CalendarQuery.class));
		}
		try (InputStream inputStream = stringToInputStream(inputString)) {
			assertThrows(IOException.class, () -> XmlUnmarshallingUtil.unmarshal(inputStream, SyncCollection.class));
		}
		// gleiche tests mit inputString anstelle von Stream
		// mit sync-collection
		final String syncCollectionString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<sync-collection xmlns=\"DAV:\"><sync-token/><sync-level>1</sync-level><prop><getcontenttype/><getetag/></prop></sync-collection>";
		assertThrows(IOException.class, () -> XmlUnmarshallingUtil.unmarshal(syncCollectionString, CalendarMultiget.class));
		assertThrows(IOException.class, () -> XmlUnmarshallingUtil.unmarshal(syncCollectionString, CalendarQuery.class));
		assertDoesNotThrow(() -> XmlUnmarshallingUtil.unmarshal(syncCollectionString, SyncCollection.class));

		// mit calendar-multiget
		final String multigetString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<C:calendar-multiget xmlns:D=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\"><D:prop><D:getetag/><C:calendar-data/></D:prop><D:href>/dav/gymabi/kalender/5/6.ics</D:href><D:href>/dav/gymabi/kalender/5/5.ics</D:href><D:href>/dav/gymabi/kalender/5/4.ics</D:href><D:href>/dav/gymabi/kalender/5/3.ics</D:href><D:href>/dav/gymabi/kalender/5/2.ics</D:href><D:href>/dav/gymabi/kalender/5/1.ics</D:href></C:calendar-multiget>";
		assertDoesNotThrow(() -> XmlUnmarshallingUtil.unmarshal(multigetString, CalendarMultiget.class));
		assertThrows(IOException.class, () -> XmlUnmarshallingUtil.unmarshal(multigetString, CalendarQuery.class));
		assertThrows(IOException.class, () -> XmlUnmarshallingUtil.unmarshal(multigetString, SyncCollection.class));
	}

	private static InputStream stringToInputStream(final String input) {
		return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
	}
}
