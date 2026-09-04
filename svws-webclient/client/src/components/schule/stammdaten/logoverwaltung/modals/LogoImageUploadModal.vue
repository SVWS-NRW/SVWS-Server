<template>
	<svws-ui-modal :show="isOpen"
		size="small"
		@update:show="closeModal"
		:auto-close="false">
		<template #modalTitle>
			<span>{{ logoModel()?.proxy.bezeichnung }}</span>
		</template>
		<template #modalDescription>
			<div class="flex justify-center">
				<div class="flex flex-col gap-2 w-full">
					<div ref="previewContainerRef" class="w-full flex justify-center items-center">
						<div class="flex items-center justify-center rounded-md border border-ui-50 bg-ui-neutral-25 p-2"
							:style="{ height: previewHeight, width: previewWidth }">
							<logo-image :logo-base64="uploadedFileSrc"
								alt="Vorschau des neuen Bildes"
								mode="inline" />
						</div>
					</div>
					<input type="file"
						:accept="SUPPORTED_IMAGE_TYPES.map(type => type.mimeType).join(', ')"
						@change="onFileChanged">
					<div class="grid grid-cols-[auto_1fr_auto] gap-x-4 gap-y-2 items-start text-left my-5">
						<span class="font-bold">Hinzugefügt am:</span>
						<span class="font-normal">{{ hinzugefuegtAm }}</span>
						<span />

						<span class="font-bold">Dateityp:</span>
						<span class="font-normal">{{ SUPPORTED_IMAGE_TYPES.map(type => type.extensions.join(', ')).join(', ') }}</span>
						<ui-validation-tooltip v-if="hasImage && fileTypeValidationResult.hasFehler"
							:validation-result="fileTypeValidationResult"
							class="mt-1" />
						<span v-else-if="hasImage"
							class="icon icon i-ri-checkbox-circle-fill icon-ui-success mt-1" />
						<span v-else />

						<span class="font-bold">Größe in MB:</span>
						<span class="font-normal">max. {{ imageRestrictions.maxGroesseInMB }} MB</span>
						<ui-validation-tooltip v-if="hasImage && fileSizeValidationResult.hasFehler"
							:validation-result="fileSizeValidationResult"
							class="mt-1" />
						<span v-else-if="hasImage"
							class="icon icon i-ri-checkbox-circle-fill icon-ui-success mt-1" />
						<span v-else />

						<span class="font-bold">Auflösung:</span>
						<span class="font-normal">min. {{ imageRestrictions.aufloesungInDPI }} DPI</span>
						<ui-validation-tooltip v-if="hasImage && resolutionValidationResult.hasFehler"
							:validation-result="resolutionValidationResult"
							class="mt-1" />
						<span v-else-if="hasImage"
							class="icon icon i-ri-checkbox-circle-fill icon-ui-success mt-1" />
						<span v-else />

						<span class="font-bold">Seitenverhältnis:</span>
						<span class="font-normal">{{ getRatioText }}</span>
						<ui-validation-tooltip v-if="hasImage && aspectRatioValidationResult.hasFehler"
							:validation-result="aspectRatioValidationResult"
							class="mt-1" />
						<span v-else-if="hasImage"
							class="icon icon i-ri-checkbox-circle-fill icon-ui-success mt-1" />
						<span v-else />
					</div>
					<div v-if="backendValidationResult !== null">
						<span class="icon icon-ui-danger i-ri-alert-fill align-middle mr-2" />
						<span class="text-ui-danger align-middle">{{ backendValidationResult }}</span>
					</div>
				</div>
			</div>
		</template>
		<template #modalActions>
			<div class="mt-7 flex gap-4 justify-end">
				<svws-ui-button type="secondary" @click="closeModal">
					Abbrechen
				</svws-ui-button>
				<svws-ui-tooltip :indicator="false" :disabled="!saveDisabled">
					<template #default>
						<svws-ui-button :disabled="saveDisabled" @click="uploadLogoImage()">
							Speichern
						</svws-ui-button>
					</template>
					<template #content>
						<span v-if="!newImageUploaded">Es wurde noch kein Bild zum Speichern ausgewählt</span>
						<span v-else>Das ausgewählte Bild entspricht nicht den Mindestanforderungen</span>
					</template>
				</svws-ui-tooltip>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, useTemplateRef } from "vue";
	import { formatToLocalDate } from "~/utils/date";
	import { getImageRestrictions, parseBase64, readFileAsBase64, setModelImageInfo, SUPPORTED_IMAGE_TYPES } from "../LogoUtils";
	import LogoImage from "../LogoImage.vue";
	import type { LogoModelProxy } from "../modelProxy/LogoModelProxy";
	import { ValidatorLogoImageFileType } from "../modelProxy/ValidatorLogoImageFileType";
	import { ValidatorLogoImageFileSize } from "../modelProxy/ValidatorLogoImageFileSize";
	import { ValidatorLogoImageAspectRatio } from "../modelProxy/ValidatorLogoImageAspectRatio";
	import { ValidatorLogoImageResolution } from "../modelProxy/ValidatorLogoImageResolution";
	import { OpenApiError } from "@core/api/OpenApiError";
	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import type { Logo } from "@core/core/data/schule/Logo";
	import { ReportingBildDefinition } from "@core/core/types/reporting/ReportingBildDefinition";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { ValidationResult } from "@ui/validation/ValidationResult";

	const props = defineProps<{
		isOpen: boolean;
		logoModel: () => LogoModelProxy | undefined;
		add: (logo: Partial<Logo>) => Promise<Logo>;
	}>();

	const emit = defineEmits<{
		(e: "closeModal"): void;
	}>();

	const hasImage = computed(() =>
		(props.logoModel()?.proxy.base64 !== '') && (props.logoModel()?.proxy.base64 !== undefined)
	);

	const newImageUploaded = computed(() =>
		'base64' in (props.logoModel()?.pending ?? {})
	);

	const imageRestrictions = computed(() => getImageRestrictions(props.logoModel()?.proxy.kennung ?? ""));

	const getRatioText = computed(() => {
		const hasSize = (imageRestrictions.value.hoeheInMM > -1) && (imageRestrictions.value.breiteInMM > -1);
		const sizeText = hasSize ? ` (${imageRestrictions.value.breiteInMM}mm x ${imageRestrictions.value.hoeheInMM}mm)` : "";
		return imageRestrictions.value.seitenverhaeltnis + sizeText;
	});

	const saveDisabled = computed(() =>
		(!newImageUploaded.value)
		|| (backendValidationResult.value !== null)
		|| fileTypeValidationResult.value.hasFehler
		|| fileSizeValidationResult.value.hasFehler
	);

	const uploadedFileSrc = computed(() => {
		return props.logoModel()?.proxy.base64 ?? "";
	});

	const hinzugefuegtAm = computed(() => {
		return (newImageUploaded.value) ? "jetzt" : formatToLocalDate(props.logoModel()?.proxy.hinzugefuegtAm ?? null, "/");
	});

	async function onFileChanged(event: Event): Promise<void> {
		backendValidationResult.value = null;
		const target = event.target as HTMLInputElement;
		const logoModel = props.logoModel();
		if ((!target.files) || (logoModel === undefined)) {
			return;
		}
		const file = target.files[0];
		const base64 = await readFileAsBase64(file);
		await setModelImageInfo(logoModel, base64, file.type, file.size);
		logoModel.proxy.base64 = base64;
	}

	async function uploadLogoImage(): Promise<void> {
		backendValidationResult.value = null;
		const logoModel = props.logoModel();
		if ((logoModel === undefined)) {
			return;
		}
		try {
			if (logoModel.proxy.id === -1) {
				await props.add({ kennung: logoModel.proxy.kennung, logoBase64: logoModel.proxy.base64 });
			} else {
				await logoModel.patch();
			}
			await closeModal();
		} catch (ex: unknown) {
			if (ex instanceof OpenApiError && ex.response !== null) {
				backendValidationResult.value = await ex.response.text();
			}
		}
	}

	async function closeModal(): Promise<void> {
		const logoModel = props.logoModel();
		if (logoModel === undefined) {
			emit("closeModal");
			return;
		}
		const fileType = parseBase64(logoModel.data.base64)?.mimeType ?? null;
		await setModelImageInfo(logoModel, logoModel.data.base64, fileType, null);
		logoModel.pending = {};
		emit("closeModal");
	}

	/**
	 * Preview
	 */

	const MAX_PREVIEW_HEIGHT_PX = 200;
	const previewContainerRef = useTemplateRef("previewContainerRef");
	const previewDimensions = computed(() => {
		const kennung = props.logoModel()?.proxy.kennung;
		const definition = (kennung !== undefined) ? ReportingBildDefinition.getByKennung(kennung) : null;
		if (definition === null || previewContainerRef.value === null) {
			return { height: `${MAX_PREVIEW_HEIGHT_PX}px`, width: '100%' };
		}
		const availableWidth = previewContainerRef.value.clientWidth;
		const ratio = definition.getBreite() / definition.getHoehe();
		const heightFromWidth = availableWidth / ratio;
		if (heightFromWidth <= MAX_PREVIEW_HEIGHT_PX) {
			return { height: `${heightFromWidth}px`, width: '100%' };
		}
		return { height: `${MAX_PREVIEW_HEIGHT_PX}px`, width: `${MAX_PREVIEW_HEIGHT_PX * ratio}px` };
	});

	const previewHeight = computed(() => previewDimensions.value.height);
	const previewWidth = computed(() => previewDimensions.value.width);

	/**
	 * Validierung
	 */

	const backendValidationResult = ref<string | null>(null);
	const validierungsFehler = computed(() => {
		return props.logoModel()?.getAlleFehler() ?? new ArrayList<ValidatorFehler>();
	});

	const fileTypeValidationResult = computed(() => {
		const fehler = new ArrayList<ValidatorFehler>();
		for (const f of validierungsFehler.value) {
			if (f.getValidator() instanceof ValidatorLogoImageFileType) {
				fehler.add(f);
			}
		}
		return new ValidationResult(fehler);
	});

	const fileSizeValidationResult = computed(() => {
		const fehler = new ArrayList<ValidatorFehler>();
		for (const f of validierungsFehler.value) {
			if (f.getValidator() instanceof ValidatorLogoImageFileSize) {
				fehler.add(f);
			}
		}
		return new ValidationResult(fehler);
	});

	const resolutionValidationResult = computed(() => {
		const fehler = new ArrayList<ValidatorFehler>();
		for (const f of validierungsFehler.value) {
			if (f.getValidator() instanceof ValidatorLogoImageResolution) {
				fehler.add(f);
			}
		}
		return new ValidationResult(fehler);
	});

	const aspectRatioValidationResult = computed(() => {
		const fehler = new ArrayList<ValidatorFehler>();
		for (const f of validierungsFehler.value) {
			if (f.getValidator() instanceof ValidatorLogoImageAspectRatio) {
				fehler.add(f);
			}
		}
		return new ValidationResult(fehler);
	});

</script>
