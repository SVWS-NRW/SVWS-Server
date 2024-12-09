<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" :type="listAufsichtsbereiche.size() < 1 ? 'danger' : 'default'" size="medium">
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
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="selected.length === 0"> Ausgewählte importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { List, StundenplanAufsichtsbereich } from "@core";

	const props = defineProps<{
		importAufsichtsbereiche: (s: StundenplanAufsichtsbereich[]) => Promise<void>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	}>();

	const show = ref<boolean>(false);

	const selected = ref<StundenplanAufsichtsbereich[]>([]);
	const aufsichtsbereich = ref<StundenplanAufsichtsbereich>()

	function openModal() {
		selected.value = [...props.listAufsichtsbereiche];
		show.value = true;
	}

	async function importer() {
		await props.importAufsichtsbereiche(selected.value);
		show.value = false;
		selected.value = [];
	}
</script>
