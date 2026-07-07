package de.svws_nrw.mapper.schueler.schulbesuch;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;


public record SchulbesuchMappingContext(
		Map<String, DTOEntlassarten> entlassartenByBezeichnung,
		Map<String, DTOSchuleNRW> schulenBySchulnummer,
		List<SchuelerSchulbesuchMerkmal> merkmale,
		List<SchuelerSchulbesuchSchule> bisherigeSchulen,
		Integer jahrEntlassungVorherigeSchule
) { }
