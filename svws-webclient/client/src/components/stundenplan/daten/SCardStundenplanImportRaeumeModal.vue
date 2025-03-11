<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" :type="listRaeume.size() < 1 ? 'danger' : 'default'" size="medium">
		<template #modalTitle>Räume aus Vorlage importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-table v-if="listRaeume.size()" :items="listRaeume" clickable :clicked="raum" selectable v-model="selected" :columns />
				<div v-else>Importieren nicht möglich, keine (zusätzlichen) Einträge in der Raum-Vorlage hinterlegt.</div>
				<div>Neue Einträge in der Raum-Vorlage können unter Schule angelegt werden</div>
				<!-- TODO Link einfügen und Beschreibung anpassen -->
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="selected.length === 0"> Ausgewählte importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { List, Raum } from "@core";

	const props = defineProps<{
		importRaeume: (raeume: Raum[]) => Promise<void>;
		listRaeume: List<Raum>;
	}>();

	const show = ref<boolean>(false);

	const selected = ref<Raum[]>([]);
	const raum = ref<Raum>()

	const columns = [
		{key: 'kuerzel', label: 'Kürzel', span: 1},
		{key: 'beschreibung', label: 'Beschreibung', span: 3},
		{key: 'groesse', label: 'Größe', span: 1},
	]

	function openModal() {
		selected.value = [...props.listRaeume];
		show.value = true;
	}

	async function importer() {
		await props.importRaeume(selected.value);
		show.value = false;
		selected.value = [];
	}

</script>
