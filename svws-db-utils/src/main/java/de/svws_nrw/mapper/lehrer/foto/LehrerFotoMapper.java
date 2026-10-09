package de.svws_nrw.mapper.lehrer.foto;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.service.lehrer.foto.LehrerFoto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LehrerFotoMapper {

	/** Singleton-Instanz des MapStruct-Mappers. */
	LehrerFotoMapper INSTANCE = Mappers.getMapper(LehrerFotoMapper.class);

	/**
	 * Konvertiert ein {@link DTOLehrerFoto} in ein Domain-Objekt.
	 *
	 * @param entity das zu konvertierende DTO
	 * @return das gemappte {@link LehrerFoto}
	 */
	LehrerFoto toDomain(DTOLehrerFoto entity);

}
