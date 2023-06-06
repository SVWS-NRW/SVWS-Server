<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Räume aus Katalog importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-data-table v-if="listRaeume.size()" :items="listRaeume" clickable :clicked="raum" selectable v-bind="selected" />
				<div v-else>Keine Einträge im Raum-Katalog hinterlegt.</div>
				<div>Neue Einträge im Raum-Katalog können unter Schule angelegt werden</div>
				<!-- TODO Link einfügen und Beschreibung anpassen -->
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="modal.closeModal" :disabled="selected.length === 0"> Ausgewählte importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { List, Raum } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		importRaeume: (raeume: Raum[]) => Promise<void>;
		listRaeume: List<Raum>;
	}>();

	const modal = ref();
	const selected = ref<Raum[]>([]);
	const raum = ref<Raum>()

	const openModal = () => {
		modal.value.openModal();
	}
</script>
