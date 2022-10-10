package de.nrw.schule.svws.db.converter.current;

import de.nrw.schule.svws.core.types.dav.DavRessourceCollectionTyp;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;

import jakarta.persistence.Converter;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dem Konvertieren von DavRessourceCollectionTypen.
 */
@Converter(autoApply = true)
public class DavRessourceCollectionTypConverter extends DBAttributeConverter<DavRessourceCollectionTyp, Integer> {

	/** Die Instanz des Konverters */
	public final static DavRessourceCollectionTypConverter instance = new DavRessourceCollectionTypConverter();
	
	@Override
	public Integer convertToDatabaseColumn(DavRessourceCollectionTyp typ) {
		return typ.id;
	}

	@Override
	public @NotNull DavRessourceCollectionTyp convertToEntityAttribute(Integer dbData) {
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
