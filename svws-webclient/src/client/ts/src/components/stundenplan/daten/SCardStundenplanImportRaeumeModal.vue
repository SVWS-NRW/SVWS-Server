<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Räume aus Katalog importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				Tabelle mit Select
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Ausgewählte importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { StundenplanRaum } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		importRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	}>();

	const modal = ref();
	const selected = ref<StundenplanRaum[]>([]);

	const openModal = () => {
		modal.value.openModal();
	}
</script>
