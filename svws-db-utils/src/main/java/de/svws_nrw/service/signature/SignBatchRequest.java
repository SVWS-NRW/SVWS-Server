package de.svws_nrw.service.signature;

import java.util.List;

record SignBatchRequest(int schulnummer, String policyId, List<SignRequestPayload> items) {
}
