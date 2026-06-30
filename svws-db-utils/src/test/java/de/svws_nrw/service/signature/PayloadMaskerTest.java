package de.svws_nrw.service.signature;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PayloadMaskerTest {

	private SignatureServiceImpl.PayloadMasker cut;

	@BeforeEach
	void setUp() {
		cut = SignatureServiceImpl.PayloadMasker.newInstance();
	}

	// -----------------------------------------------------------------------
	// newInstance
	// -----------------------------------------------------------------------
	@Nested
	@DisplayName("newInstance()")
	class NewInstance {

		@Test
		@DisplayName("returns a non-null PayloadMasker")
		void returnsNonNull() {
			assertThat(SignatureServiceImpl.PayloadMasker.newInstance()).isNotNull();
		}

		@Test
		@DisplayName("every call returns a distinct instance")
		void returnsDistinctInstances() {
			final SignatureServiceImpl.PayloadMasker instance1 = SignatureServiceImpl.PayloadMasker.newInstance();
			final SignatureServiceImpl.PayloadMasker instance2 = SignatureServiceImpl.PayloadMasker.newInstance();
			assertThat(instance1).isNotSameAs(instance2);
		}
	}

	// -----------------------------------------------------------------------
	// mask()
	// -----------------------------------------------------------------------
	@Nested
	@DisplayName("mask()")
	class Mask {

		@Test
		@DisplayName("empty input produces empty output")
		void emptyInput() {
			final Map<Long, byte[]> result = cut.mask(new HashMap<>());
			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("result has same number of entries as input")
		void sizeMatchesInput() {
			final Map<Object, byte[]> input = new HashMap<>();
			input.put("id-1", new byte[] { 1 });
			input.put("id-2", new byte[] { 2 });
			input.put("id-3", new byte[] { 3 });

			assertThat(cut.mask(input)).hasSize(3);
		}

		@Test
		@DisplayName("mask ids are positive longs starting at 1")
		void maskIdsStartAtOne() {
			final Map<Object, byte[]> input = new HashMap<>();
			input.put("a", new byte[] { 10 });
			input.put("b", new byte[] { 20 });

			final Map<Long, byte[]> result = cut.mask(input);

			assertThat(result.keySet()).isNotEmpty().allMatch(k -> k >= 1L);
		}

		@Test
		@DisplayName("all original payloads are present in the masked map")
		void payloadsArePreserved() {
			final byte[] payload1 = { 1, 2, 3 };
			final byte[] payload2 = { 4, 5, 6 };

			final Map<Object, byte[]> input = new HashMap<>();
			input.put("key-1", payload1);
			input.put("key-2", payload2);

			final Map<Long, byte[]> result = cut.mask(input);

			assertThat(result.values()).containsExactlyInAnyOrder(payload1, payload2);
		}

		@Test
		@DisplayName("mask ids are unique (no collisions)")
		void maskIdsAreUnique() {
			final Map<Object, byte[]> input = new HashMap<>();
			for (int i = 0; i < 100; i++) {
				input.put("key-" + i, new byte[] { (byte) i });
			}

			final Map<Long, byte[]> result = cut.mask(input);

			assertThat(result).hasSize(100);
		}

		@Test
		@DisplayName("works with non-String original ids (e.g. Long, Integer)")
		void worksWithNonStringIds() {
			final Map<Object, byte[]> input = new HashMap<>();
			input.put(42L, new byte[] { 7 });
			input.put(99, new byte[] { 8 });

			final Map<Long, byte[]> result = cut.mask(input);

			assertThat(result).hasSize(2);
		}

		@Test
		@DisplayName("returned map is mutable (not unmodifiable)")
		void returnedMapIsMutable() {
			final Map<Object, byte[]> input = new HashMap<>();
			input.put("x", new byte[] { 0 });

			final Map<Long, byte[]> result = cut.mask(input);

			assertThatCode(() -> result.put(999L, new byte[] { 9 })).doesNotThrowAnyException();
		}
	}

	// -----------------------------------------------------------------------
	// unmask()
	// -----------------------------------------------------------------------
	@Nested
	@DisplayName("unmask()")
	class Unmask {

		@Test
		@DisplayName("empty input produces empty output")
		void emptyInput() throws SignatureServiceImpl.MaskerException {
			cut.mask(new HashMap<>()); // initialize internal state
			final Map<Object, Signature> result = cut.unmask(new HashMap<>());
			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("unmask reverses mask for a single entry")
		void roundTripSingleEntry() throws SignatureServiceImpl.MaskerException {
			final String originalId = "order-42";
			final byte[] payload = { 1, 2, 3 };
			final Signature signature = new Signature(new byte[] { 9, 8, 7 }, SignatureStatus.OK, "");

			final Map<Object, byte[]> toMask = new HashMap<>();
			toMask.put(originalId, payload);
			final Map<Long, byte[]> masked = cut.mask(toMask);

			final Long maskId = masked.keySet().iterator().next();
			final Map<Long, Signature> signatureByMaskId = Map.of(maskId, signature);

			final Map<Object, Signature> result = cut.unmask(signatureByMaskId);

			assertThat(result)
					.hasSize(1)
					.containsEntry(originalId, signature);
		}

		@Test
		@DisplayName("unmask reverses mask for multiple entries")
		void roundTripMultipleEntries() throws SignatureServiceImpl.MaskerException {
			final String id1 = "alpha";
			final String id2 = "beta";
			final String id3 = "gamma";

			final Map<Object, byte[]> toMask = new HashMap<>();
			toMask.put(id1, new byte[] { 1 });
			toMask.put(id2, new byte[] { 2 });
			toMask.put(id3, new byte[] { 3 });

			final Map<Long, byte[]> masked = cut.mask(toMask);

			final Signature sig1 = new Signature(new byte[] { 10 }, SignatureStatus.OK, "");
			final Signature sig2 = new Signature(new byte[] { 20 }, SignatureStatus.OK, "");
			final Signature sig3 = new Signature(new byte[] { 30 }, SignatureStatus.OK, "");

			// build signature map using the mask ids we got back
			final Map<Long, Signature> signatureByMaskId = new HashMap<>();
			for (Map.Entry<Long, byte[]> maskedEntry : masked.entrySet()) {
				final byte[] p = maskedEntry.getValue();
				if (p[0] == 1) {
					signatureByMaskId.put(maskedEntry.getKey(), sig1);
				} else if (p[0] == 2) {
					signatureByMaskId.put(maskedEntry.getKey(), sig2);
				} else {
					signatureByMaskId.put(maskedEntry.getKey(), sig3);
				}
			}

			final Map<Object, Signature> result = cut.unmask(signatureByMaskId);

			assertThat(result)
					.hasSize(3)
					.containsEntry(id1, sig1)
					.containsEntry(id2, sig2)
					.containsEntry(id3, sig3);
		}

		@Test
		@DisplayName("unmask preserves non-String original ids")
		void roundTripNonStringIds() throws SignatureServiceImpl.MaskerException {
			final Long originalId = 777L;
			final Signature signature = new Signature(new byte[] { 5 }, SignatureStatus.OK, "");

			final Map<Object, byte[]> toMask = new HashMap<>();
			toMask.put(originalId, new byte[] { 0 });
			final Map<Long, byte[]> masked = cut.mask(toMask);

			final Long maskId = masked.keySet().iterator().next();
			final Map<Object, Signature> result = cut.unmask(Map.of(maskId, signature));

			assertThat(result).containsEntry(originalId, signature);
		}

		@Test
		@DisplayName("unmask with unknown mask id throws MaskerException")
		void unknownMaskIdThrows() {
			cut.mask(Map.of("x", new byte[] { 1 })); // populate internal state

			final Map<Long, Signature> badInput = Map.of(9999L, new Signature(new byte[] { 0 }, SignatureStatus.OK, ""));

			assertThatThrownBy(() -> cut.unmask(badInput))
					.isInstanceOf(SignatureServiceImpl.MaskerException.class)
					.hasMessageContaining("9999");
		}

		@Test
		@DisplayName("unmask on a fresh masker (no prior mask call) throws MaskerException")
		void unmaskOnFreshMaskerThrows() {
			final Map<Long, Signature> input = Map.of(1L, new Signature(new byte[] { 0 }, SignatureStatus.OK, ""));

			assertThatThrownBy(() -> cut.unmask(input))
					.isInstanceOf(SignatureServiceImpl.MaskerException.class);
		}
	}

	// -----------------------------------------------------------------------
	// MaskerException
	// -----------------------------------------------------------------------
	@Nested
	@DisplayName("MaskerException")
	class MaskerExceptionTest {

		@Test
		@DisplayName("carries the provided message")
		void messageIsPreserved() {
			final SignatureServiceImpl.MaskerException ex = new SignatureServiceImpl.MaskerException("test error");
			assertThat(ex.getMessage()).isEqualTo("test error");
		}

		@Test
		@DisplayName("is a RuntimeException")
		void isRuntimeException() {
			assertThat(new SignatureServiceImpl.MaskerException("x")).isInstanceOf(Exception.class);
		}
	}
}
