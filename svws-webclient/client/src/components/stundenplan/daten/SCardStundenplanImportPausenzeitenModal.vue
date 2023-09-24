<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Pausenzeiten aus Katalog importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-table v-if="listPausenzeiten.size()" :items="listPausenzeiten" clickable :clicked="pausenzeit" selectable v-bind="selected" />
				<div v-else>Keine Einträge im Pausenzeiten-Katalog hinterlegt.</div>
				<div>Neue Einträge im Pausenzeiten-Katalog können unter Schule angelegt werden</div>
				<!-- TODO Link einfügen und Beschreibung anpassen -->
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="showModal().value = false" :disabled="selected.length === 0"> Ausgewählte importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { List, StundenplanPausenzeit } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		importPausenzeiten: (Pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
		listPausenzeiten: List<StundenplanPausenzeit>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const selected = ref<StundenplanPausenzeit[]>([]);
	const pausenzeit = ref<StundenplanPausenzeit>()

	const openModal = () => {
		showModal().value = true;
	}

</script>
