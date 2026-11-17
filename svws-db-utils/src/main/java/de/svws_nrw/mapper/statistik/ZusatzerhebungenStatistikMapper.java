package de.svws_nrw.mapper.statistik;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.schule.DTOStatistikZusatzerhebungen;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.statistik.ZusatzerhebungenStatistikPatchRequest;

/**
 * Mapper für die Daten der Zusatzerhebungen zur amtlichen Schulstatistik. Die Durchschnittswerte der
 * Klinikschule werden dabei auf eine Nachkommastelle gerundet, da die Datenbank diese als FLOAT speichert.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface ZusatzerhebungenStatistikMapper {

	/** mapper */
	ZusatzerhebungenStatistikMapper INSTANCE = Mappers.getMapper(ZusatzerhebungenStatistikMapper.class);

	/**
	 * Mappt eine {@link DTOStatistikZusatzerhebungen}-Entity auf das API-Modell {@link ZusatzerhebungenStatistikGesamt}.
	 *
	 * @param entity   die Quell-Entity
	 *
	 * @return das API-Modell
	 */
	@Mapping(target = "durchschnittKlinikschuleAllgemeinbildendInsgesamt", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte", qualifiedByName = "runden")
	ZusatzerhebungenStatistikGesamt toApi(DTOStatistikZusatzerhebungen entity);

	/**
	 * Wendet die Änderungen aus einem {@link ZusatzerhebungenStatistikPatchRequest} auf eine bestehende Entity an.
	 * Nur Felder, die im Request definiert sind, werden aktualisiert. Leere Texte werden als {@code null} gespeichert.
	 *
	 * @param dto      der Patch-Request
	 * @param entity   die zu aktualisierende Entity
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "idSchuljahresabschnitt", ignore = true)
	@Mapping(target = "statistikAnsprechpartnerName", qualifiedByName = "blankToNull")
	@Mapping(target = "statistikAnsprechpartnerTelefon", qualifiedByName = "blankToNull")
	@Mapping(target = "statistikAnsprechpartnerEmail", qualifiedByName = "blankToNull")
	@Mapping(target = "durchschnittKlinikschuleAllgemeinbildendInsgesamt", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt", qualifiedByName = "runden")
	@Mapping(target = "durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte", qualifiedByName = "runden")
	void patch(ZusatzerhebungenStatistikPatchRequest dto, @MappingTarget DTOStatistikZusatzerhebungen entity);

	/**
	 * Rundet einen Durchschnittswert kaufmännisch auf eine Nachkommastelle.
	 *
	 * @param value   der Wert
	 *
	 * @return der gerundete Wert oder {@code null}, wenn kein Wert vorhanden ist
	 */
	@Named("runden")
	default Double runden(final Double value) {
		return (value == null) ? null : BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * Entpackt einen Durchschnittswert aus einem {@link JsonNullable} und rundet diesen kaufmännisch auf eine Nachkommastelle.
	 *
	 * @param value   der Wert im {@link JsonNullable}
	 *
	 * @return der gerundete Wert oder {@code null}, wenn kein Wert vorhanden ist
	 */
	@Named("runden")
	default Double runden(final JsonNullable<Double> value) {
		return ((value == null) || !value.isPresent()) ? null : runden(value.get());
	}

	/**
	 * Entpackt einen Text aus einem {@link JsonNullable} und bildet einen leeren Text auf {@code null} ab.
	 *
	 * @param value   der Text im {@link JsonNullable}
	 *
	 * @return der Text oder {@code null}, wenn er nicht definiert, {@code null} oder leer ist
	 */
	@Named("blankToNull")
	default String blankToNull(final JsonNullable<String> value) {
		if ((value == null) || !value.isPresent()) {
			return null;
		}
		final String text = value.get();
		return ((text == null) || text.isBlank()) ? null : text;
	}

}
