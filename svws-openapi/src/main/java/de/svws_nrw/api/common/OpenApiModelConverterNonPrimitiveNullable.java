package de.svws_nrw.api.common;

import java.util.Arrays;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Mit diesem {@link ModelConverter} wird für alle Attribute in der OpenAPI-Spezifikation das Flag `nullable` automatisch
 * gesetzt, wenn...
 *
 * - der Datentyp nicht primitiv ist UND
 * - das `nullable` Flag nicht bereits gesetzt wurde UND
 * - das Attribute nicht mit {@link NotNull} annotiert ist.
 *
 */
public class OpenApiModelConverterNonPrimitiveNullable implements ModelConverter {

	@Override
	public final Schema<?> resolve(final AnnotatedType annotatedType, final ModelConverterContext modelConverterContext, final Iterator<ModelConverter> iterator) {
		if (!iterator.hasNext())
			return null;

		final ModelConverter converter = iterator.next();
		final Schema<?> model = converter.resolve(annotatedType, modelConverterContext, iterator);

		if (model == null)
			return null;

		// Prüfe, ob mit @NotNull annotiert wurde
		final boolean isPropertyDefinedAsNotNull =
				(annotatedType.getCtxAnnotations() != null) && Arrays.stream(annotatedType.getCtxAnnotations()).anyMatch(x -> x instanceof NotNull);

		// Prüfe, ob ein primitiver Datentyp vorliegt
		final JavaType javaType = Json.mapper().constructType(annotatedType.getType());
		final boolean isPrimitive = javaType.isPrimitive();

		/*
		 * Setze das Flag `nullable` auf `true`, falls...
		 * - der Datentyp nicht primitiv ist UND
		 * - das `nullable` Flag nicht bereits im zum Attribut gehörenden @Schema (egal mit welchem Wert) gesetzt wurde UND
		 * - das Attribut nicht mit @NotNull annotiert wurde
		 */
		if (!isPrimitive && (model.getNullable() == null) && !isPropertyDefinedAsNotNull)
			model.setNullable(true);

		return model;
	}

	@Override
	public final boolean isOpenapi31() {
		return ModelConverter.super.isOpenapi31();
	}
}
