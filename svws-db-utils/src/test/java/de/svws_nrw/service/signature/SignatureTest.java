package de.svws_nrw.service.signature;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignatureTest {

	// -------------------------------------------------------------------------
	// Testdaten
	// -------------------------------------------------------------------------

	private static final byte[] CONTENT = { 1, 2, 3 };
	private static final byte[] CONTENT_COPY = { 1, 2, 3 };
	private static final byte[] CONTENT_OTHER = { 4, 5, 6 };

	private static final SignatureStatus STATUS = SignatureStatus.OK;
	private static final SignatureStatus STATUS_OTHER = SignatureStatus.ERROR;

	private static final String MESSAGE = "Kein Fehler";
	private static final String MESSAGE_OTHER = "Fehler aufgetreten";

	private Signature base() {
		return new Signature(CONTENT, STATUS, MESSAGE);
	}

	// -------------------------------------------------------------------------
	// equals
	// -------------------------------------------------------------------------

	@Test
	void equals_gleichesObjekt_returnsTrue() {
		final Signature sig = base();
		assertEquals(sig, sig);
	}

	@Test
	void equals_gleicheWerte_returnsTrue() {
		final Signature sig1 = new Signature(CONTENT, STATUS, MESSAGE);
		final Signature sig2 = new Signature(CONTENT_COPY, STATUS, MESSAGE);
		assertEquals(sig1, sig2);
	}

	@Test
	void equals_unterschiedlicherContent_returnsFalse() {
		final Signature sig1 = base();
		final Signature sig2 = new Signature(CONTENT_OTHER, STATUS, MESSAGE);
		assertNotEquals(sig1, sig2);
	}

	@Test
	void equals_unterschiedlicherStatus_returnsFalse() {
		final Signature sig1 = base();
		final Signature sig2 = new Signature(CONTENT, STATUS_OTHER, MESSAGE);
		assertNotEquals(sig1, sig2);
	}

	@Test
	void equals_unterschiedlicheErrorMessage_returnsFalse() {
		final Signature sig1 = base();
		final Signature sig2 = new Signature(CONTENT, STATUS, MESSAGE_OTHER);
		assertNotEquals(sig1, sig2);
	}

	@Test
	void equals_null_returnsFalse() {
		assertNotEquals(null, base());
	}

	@Test
	void equals_anderesObjektTyp_returnsFalse() {
		assertNotEquals("kein Signature-Objekt", base());
	}

	@Test
	void equals_contentNull_beideNull_returnsTrue() {
		final Signature sig1 = new Signature(null, STATUS, MESSAGE);
		final Signature sig2 = new Signature(null, STATUS, MESSAGE);
		assertEquals(sig1, sig2);
	}

	@Test
	void equals_errorMessageNull_beideNull_returnsTrue() {
		final Signature sig1 = new Signature(CONTENT, STATUS, null);
		final Signature sig2 = new Signature(CONTENT_COPY, STATUS, null);
		assertEquals(sig1, sig2);
	}

	@Test
	void equals_errorMessageNullVsNichtNull_returnsFalse() {
		final Signature sig1 = new Signature(CONTENT, STATUS, null);
		final Signature sig2 = new Signature(CONTENT, STATUS, MESSAGE);
		assertNotEquals(sig1, sig2);
	}

	// -------------------------------------------------------------------------
	// hashCode
	// -------------------------------------------------------------------------

	@Test
	void hashCode_gleicheWerte_gleicherHash() {
		final Signature sig1 = new Signature(CONTENT, STATUS, MESSAGE);
		final Signature sig2 = new Signature(CONTENT_COPY, STATUS, MESSAGE);
		assertEquals(sig1.hashCode(), sig2.hashCode());
	}

	@Test
	void hashCode_unterschiedlicheWerte_unterschiedlicherHash() {
		final Signature sig1 = base();
		final Signature sig2 = new Signature(CONTENT_OTHER, STATUS_OTHER, MESSAGE_OTHER);
		assertNotEquals(sig1.hashCode(), sig2.hashCode());
	}

	@Test
	void hashCode_konsistentBeiWiederholtemAufruf() {
		final Signature sig = base();
		assertEquals(sig.hashCode(), sig.hashCode());
	}

	// -------------------------------------------------------------------------
	// toString
	// -------------------------------------------------------------------------

	@Test
	void toString_enthaeltContent() {
		final String result = base().toString();
		System.out.println(result);
		assertTrue(result.contains(Arrays.toString(CONTENT)));
	}

	@Test
	void toString_enthaeltStatus() {
		final String result = base().toString();
		assertTrue(result.contains(STATUS.toString()));
	}

	@Test
	void toString_enthaeltErrorMessage() {
		final String result = base().toString();
		assertTrue(result.contains(MESSAGE));
	}

	@Test
	void toString_enthaeltSignaturePrefix() {
		assertTrue(base().toString().contains("Signature{"));
	}
}
