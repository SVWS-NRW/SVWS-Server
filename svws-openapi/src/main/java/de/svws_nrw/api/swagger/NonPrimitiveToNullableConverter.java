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

	private static final Class<?>[] PrimitiveObjectTypes = {
			Boolean.class,
			Byte.class,
			Character.class,
			Float.class,
			Integer.class,
			Long.class,
			Short.class,
			Double.class
	};

	@Override
	public final Schema resolve(final AnnotatedType annotatedType, final ModelConverterContext modelConverterContext, final Iterator<ModelConverter> iterator) {
		if (iterator.hasNext()) {
			final ModelConverter converter = iterator.next();
			final Schema model = converter.resolve(annotatedType, modelConverterContext, iterator);

			if (model != null) {
				final var javaType = Json.mapper().constructType(annotatedType.getType());
				final var clazz = javaType.getRawClass();

				final var isPropertyDefinedAsNotNull =
						(annotatedType.getCtxAnnotations() != null) && Arrays.stream(annotatedType.getCtxAnnotations()).anyMatch(x -> x instanceof NotNull);

				if (ClassUtils.isPrimitiveWrapper(clazz) && (model.getNullable() == null) && !isPropertyDefinedAsNotNull) {
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
