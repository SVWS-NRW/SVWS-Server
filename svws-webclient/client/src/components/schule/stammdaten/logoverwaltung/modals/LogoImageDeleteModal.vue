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
				<div v-for="logoModel in logoModels" :key="logoModel.proxy.kennung" class="p-3 m-2 grid grid-cols-[1fr_8rem] border-b border-ui-50 gap-2 w-full">
					<div class="flex items-center">{{ logoModel.proxy.bezeichnung }}</div>
					<div class="flex items-center justify-center">
						<div class="flex w-full items-center justify-center @container">
							<logo-image mode="tooltip"
								:style="getCssLogoSize(logoModel.proxy.kennung, '100cqw', `${LOGO_PREVIEW_MAX_HEIGHT_REM}rem`)"
								:logo-base64="logoModel.proxy.base64"
								:alt="`Vorschau des Bildes für das Logo '${logoModel.proxy.bezeichnung}'`" />
						</div>
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

	import LogoImage from "../LogoImage.vue";
	import { getCssLogoSize } from "../LogoUtils";
	import type { LogoModelProxy } from "../modelProxy/LogoModelProxy";

	const LOGO_PREVIEW_MAX_HEIGHT_REM = 10;

	const { isOpen, logoModels } = defineProps<{
		isOpen: boolean;
		logoModels: LogoModelProxy[];
	}>();

	const emit = defineEmits<{
		(e: "cancel"): void;
		(e: "confirm", logos: LogoModelProxy[]): void;
	}>();

	function onUpdateOpen(value: boolean): void {
		if (!value) {
			emit("cancel");
		}
	}

</script>
