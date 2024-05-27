<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" :type="listAufsichtsbereiche.size() < 1 ? 'danger' : 'default'" size="medium">
		<template #modalTitle>Aufsichtsbereiche aus Katalog importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-table v-if="listAufsichtsbereiche.size()" :items="listAufsichtsbereiche" clickable :clicked="aufsichtsbereich" selectable v-model="selected" />
				<div v-else>Importieren nicht möglich, keine (zusätzlichen) Einträge im Aufsichtsbereiche-Katalog hinterlegt.</div>
				<div>Neue Einträge im Aufsichtsbereiche-Katalog können unter Schule angelegt werden</div>
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

	import type { List, StundenplanAufsichtsbereich } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		importAufsichtsbereiche: (s: StundenplanAufsichtsbereich[]) => Promise<void>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	// eslint-disable-next-line vue/no-setup-props-destructure
	const selected = ref<StundenplanAufsichtsbereich[]>([...props.listAufsichtsbereiche]);
	const aufsichtsbereich = ref<StundenplanAufsichtsbereich>()

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.importAufsichtsbereiche(selected.value);
		showModal().value = false;
		selected.value = [];
	}
</script>
