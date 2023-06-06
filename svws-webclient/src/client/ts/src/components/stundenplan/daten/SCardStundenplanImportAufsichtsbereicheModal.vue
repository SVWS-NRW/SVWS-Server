<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Aufsichtsbereiche aus Katalog importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-data-table v-if="listAufsichtsbereiche.size()" :items="listAufsichtsbereiche" clickable :clicked="aufsichtsbereich" selectable v-bind="selected" />
				<div v-else>Keine Einträge im Aufsichtsbereiche-Katalog hinterlegt.</div>
				<div>Neue Einträge im Aufsichtsbereiche-Katalog können unter Schule angelegt werden</div>
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
	import type { List, StundenplanAufsichtsbereich } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		importAufsichtsbereiche: (s: StundenplanAufsichtsbereich[]) => Promise<void>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	}>();

	const modal = ref();
	const selected = ref<StundenplanAufsichtsbereich[]>([]);
	const aufsichtsbereich = ref<StundenplanAufsichtsbereich>()

	const openModal = () => {
		modal.value.openModal();
	}
</script>
