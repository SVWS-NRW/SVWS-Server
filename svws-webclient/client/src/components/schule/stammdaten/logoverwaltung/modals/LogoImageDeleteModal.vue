<template>
	<svws-ui-modal :show="isOpen"
		size="small" type="danger"
		@update:show="onUpdateOpen">
		<template #modalTitle>
			{{ logos.length === 1 ? 'Bild löschen' : 'Bilder löschen' }}
		</template>
		<template #modalDescription>
			<div class="text-left">
				<div class="mb-8 text-headline-md">Möchten Sie {{ logos.length === 1 ? 'das Bild dieses' : 'die Bilder dieser' }} Logos wirklich löschen:</div>
				<div v-for="logo in logos" :key="logo.proxy.kennung" class="p-3 m-2 grid grid-cols-[1fr_8rem] border-b border-ui-50 gap-2">
					<div class="flex items-center">{{ logo.proxy.bezeichnung }}</div>
					<div class="flex items-center justify-center max-h-25">
						<logo-image mode="tooltip"
							:logo-base64="logo.proxy.base64"
							:alt="`Vorschau des Bildes für das Logo '${logo.proxy.bezeichnung}'`" />
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
				:disabled="logos.length === 0"
				@click="emit('confirm', logos)">
				{{ logos.length === 1 ? 'Bild löschen' : 'Bilder löschen' }}
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { LogoModelProxy } from "../modelProxy/LogoModelProxy";

	const { isOpen, logos } = defineProps<{
		isOpen: boolean;
		logos: LogoModelProxy[];
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
