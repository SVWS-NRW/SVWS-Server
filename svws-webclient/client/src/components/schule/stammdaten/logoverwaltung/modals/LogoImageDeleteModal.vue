<template>
	<svws-ui-modal :show="isOpen"
		size="small" type="danger"
		@update:show="onUpdateOpen">
		<template #modalTitle>
			{{ logoModels.length === 1 ? 'Bild löschen' : 'Bilder löschen' }}
		</template>
		<template #modalDescription>
			<div class="text-left">
				<div class="mb-8 text-headline-md">Möchten Sie {{ logoModels.length === 1 ? 'das Bild dieses' : 'die Bilder dieser' }} Logos wirklich löschen:</div>
				<div v-for="(logoModel, index) in logoModels" :key="logoModel.proxy.kennung" class="p-3 m-2 grid grid-cols-[1fr_8rem] border-b border-ui-50 gap-2 w-full">
					<div class="flex items-center">{{ logoModel.proxy.bezeichnung }}</div>
					<div ref="previewContainerRef" class="flex items-center justify-center" :style="{ height: getPreviewDimensions(index, logoModel.proxy.kennung).height, width: getPreviewDimensions(index, logoModel.proxy.kennung).width }">
						<logo-image mode="tooltip"
							:logo-base64="logoModel.proxy.base64"
							:alt="`Vorschau des Bildes für das Logo '${logoModel.proxy.bezeichnung}'`" />
					</div>
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary"
				@click="emit('cancel')">
				Abbrechen
			</svws-ui-button>
			<svws-ui-button type="danger"
				:disabled="logoModels.length === 0"
				@click="emit('confirm', logoModels)">
				{{ logoModels.length === 1 ? 'Bild löschen' : 'Bilder löschen' }}
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { LogoModelProxy } from "../modelProxy/LogoModelProxy";
	import { useTemplateRef } from "vue";
	import { ReportingBildDefinition } from "@core";

	const { isOpen, logoModels } = defineProps<{
		isOpen: boolean;
		logoModels: LogoModelProxy[];
	}>();

	const emit = defineEmits<{
		(e: "cancel"): void;
		(e: "confirm", logos: LogoModelProxy[]): void;
	}>();


	const previewContainerRef = useTemplateRef("previewContainerRef");
	function getPreviewDimensions(index: number, kennung: string): { height: string; width: string } {
		const previewContainer = previewContainerRef.value?.[index];
		const maxHeight = 96;
		const definition = ReportingBildDefinition.getByKennung(kennung);

		if ((definition === null) || (previewContainer === undefined)) {
			return { height: `${maxHeight}px`, width: "100%" };
		}
		const availableWidth = previewContainer.clientWidth;
		const ratio = definition.getBreite() / definition.getHoehe();
		const heightFromWidth = availableWidth / ratio;

		if (heightFromWidth <= maxHeight) {
			return { height: `${heightFromWidth}px`, width: "100%" };
		}

		return { height: `${maxHeight}px`, width: `${maxHeight * ratio}px` };
	}

	function onUpdateOpen(value: boolean): void {
		if (!value) {
			emit("cancel");
		}
	}

</script>
