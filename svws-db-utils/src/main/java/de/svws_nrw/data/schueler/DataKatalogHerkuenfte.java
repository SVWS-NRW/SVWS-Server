package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.ArrayList;

import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangKatalogEintrag;
import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangTypKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulformSchulgliederung;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgang;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgangTyp;
import de.svws_nrw.core.data.schule.HerkunftKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftSchulformKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftSonstigeKatalogEintrag;
import de.svws_nrw.core.types.schueler.HerkunftSchulform;
import de.svws_nrw.core.types.schueler.HerkunftSonstige;
import de.svws_nrw.data.DataManager;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link HerkunftKatalogEintrag}.
 */
public final class DataKatalogHerkuenfte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link HerkunftKatalogEintrag}.
	 */
	public DataKatalogHerkuenfte() {
		super(null);
	}

	@Override
	public Response getAll() {
		final ArrayList<HerkunftKatalogEintrag> daten = new ArrayList<>();
		for (final HerkunftSonstige eintrag : HerkunftSonstige.values()) {
			for (final HerkunftSonstigeKatalogEintrag ke : eintrag.historie) {
				final HerkunftKatalogEintrag hke = new HerkunftKatalogEintrag();
				hke.id = ke.id + 1000000000L;
				hke.kuerzel = ke.kuerzel;
				hke.schulformen = ke.schulformen;
				hke.beschreibung = ke.beschreibung;
				hke.gueltigVon = ke.gueltigVon;
				hke.gueltigBis = ke.gueltigBis;
				daten.add(hke);
			}
		}
		for (final HerkunftBildungsgang eintrag : HerkunftBildungsgang.values()) {
			for (final HerkunftBildungsgangKatalogEintrag ke : eintrag.historie()) {
				final HerkunftKatalogEintrag hke = new HerkunftKatalogEintrag();
				hke.id = ke.id + 2000000000L;
				hke.kuerzel = ke.kuerzel;
				for (final @NotNull SchulformSchulgliederung sfsgl : ke.zulaessig)
					if (!hke.schulformen.contains(sfsgl.schulform))
						hke.schulformen.add(sfsgl.schulform);
				hke.beschreibung = ke.text;
				hke.gueltigVon = ke.gueltigVon;
				hke.gueltigBis = ke.gueltigBis;
				daten.add(hke);
			}
		}
		for (final HerkunftBildungsgangTyp eintrag : HerkunftBildungsgangTyp.values()) {
			for (final HerkunftBildungsgangTypKatalogEintrag ke : eintrag.historie()) {
				final HerkunftKatalogEintrag hke = new HerkunftKatalogEintrag();
				hke.id = ke.id + 3000000000L;
				hke.kuerzel = ke.kuerzel;
				for (final @NotNull SchulformSchulgliederung sfsgl : ke.zulaessig)
					if (!hke.schulformen.contains(sfsgl.schulform))
						hke.schulformen.add(sfsgl.schulform);
				hke.beschreibung = ke.text;
				hke.gueltigVon = ke.gueltigVon;
				hke.gueltigBis = ke.gueltigBis;
				daten.add(hke);
			}
		}
		for (final HerkunftSchulform eintrag : HerkunftSchulform.values()) {
			for (final HerkunftSchulformKatalogEintrag ke : eintrag.historie) {
				final HerkunftKatalogEintrag hke = new HerkunftKatalogEintrag();
				hke.id = ke.id + 4000000000L;
				hke.kuerzel = ke.kuerzel;
				hke.schulformen = ke.schulformen;
				hke.beschreibung = ke.beschreibung;
				hke.gueltigVon = ke.gueltigVon;
				hke.gueltigBis = ke.gueltigBis;
				daten.add(hke);
			}
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
