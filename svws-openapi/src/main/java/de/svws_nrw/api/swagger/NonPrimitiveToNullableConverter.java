package de.svws_nrw.api.swagger;

import java.util.Arrays;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.ClassUtils;

public class NonPrimitiveToNullableConverter implements ModelConverter {

	@Override
	public final Schema resolve(final AnnotatedType annotatedType, final ModelConverterContext modelConverterContext, final Iterator<ModelConverter> iterator) {
		if (iterator.hasNext()) {
			final ModelConverter converter = iterator.next();
			final Schema model = converter.resolve(annotatedType, modelConverterContext, iterator);

			if (model != null) {
				final boolean isPropertyDefinedAsNotNull =
						(annotatedType.getCtxAnnotations() != null) && Arrays.stream(annotatedType.getCtxAnnotations()).anyMatch(x -> x instanceof NotNull);

				final JavaType javaType = Json.mapper().constructType(annotatedType.getType());

				final boolean isPrimitive = javaType.isPrimitive();

				if (!isPrimitive && (model.getNullable() == null) && !isPropertyDefinedAsNotNull) {
					model.setNullable(true);
				}

				return model;
			}
		}

		return null;
	}

	@Override
	public final boolean isOpenapi31() {
		return ModelConverter.super.isOpenapi31();
	}
}
