package de.svws_nrw.mapper.schueler.foto;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.service.schueler.foto.SchuelerFoto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SchuelerFotoMapper {

	/** Singleton-Instanz des MapStruct-Mappers. */
	SchuelerFotoMapper INSTANCE = Mappers.getMapper(SchuelerFotoMapper.class);

	/**
	 * Konvertiert ein {@link DTOSchuelerFoto} in ein Domain-Objekt.
	 *
	 * @param entity das zu konvertierende DTO
	 * @return das gemappte {@link SchuelerFoto}
	 */
	SchuelerFoto toDomain(DTOSchuelerFoto entity);

}
