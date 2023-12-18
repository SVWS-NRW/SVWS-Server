<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" :type="listRaeume.size() < 1 ? 'danger' : 'default'" size="medium">
		<template #modalTitle>Räume aus Katalog importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-table v-if="listRaeume.size()" :items="listRaeume" clickable :clicked="raum" selectable v-model="selected" />
				<div v-else>Importieren nicht möglich, keine (zusätzlichen) Einträge im Raum-Katalog hinterlegt.</div>
				<div>Neue Einträge im Raum-Katalog können unter Schule angelegt werden</div>
				<!-- TODO Link einfügen und Beschreibung anpassen -->
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="selected.length === 0"> Ausgewählte importieren </svws-ui-button>
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

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	// eslint-disable-next-line vue/no-setup-props-destructure
	const selected = ref<Raum[]>([...props.listRaeume]);
	const raum = ref<Raum>()

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.importRaeume(selected.value);
		showModal().value = false;
		selected.value = [];
	}

</script>
