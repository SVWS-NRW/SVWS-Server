package de.svws_nrw.db.converter.current;

import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.persistence.Converter;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dem Konvertieren von DavRessourceCollectionTypen.
 */
@Converter(autoApply = true)
public final class DavRessourceCollectionTypConverter extends DBAttributeConverter<DavRessourceCollectionTyp, Integer> {

	/** Die Instanz des Konverters */
	public static final DavRessourceCollectionTypConverter instance = new DavRessourceCollectionTypConverter();

	@Override
	public Integer convertToDatabaseColumn(final DavRessourceCollectionTyp typ) {
		return typ.id;
	}

	@Override
	public @NotNull DavRessourceCollectionTyp convertToEntityAttribute(final Integer dbData) {
		return DavRessourceCollectionTyp.getByID(dbData);
	}

	@Override
	public Class<DavRessourceCollectionTyp> getResultType() {
		return DavRessourceCollectionTyp.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}
