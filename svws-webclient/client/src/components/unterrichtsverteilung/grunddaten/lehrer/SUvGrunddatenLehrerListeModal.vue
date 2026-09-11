<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" size="medium">
		<template #modalTitle>Lehrer hinzufügen</template>
		<template #modalContent>
			<svws-ui-table :items="listLehrer" :columns selectable scroll v-model="selectedLehrer" count>
				<template #filter />
				<template #cell(kuerzel)="{ value }">
					{{ value }}
				</template>
				<template #cell(nachname)="{ value }">
					{{ value }}
				</template>
				<template #cell(vorname)="{ value }">
					{{ value }}
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="false"> Lehrer hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import type { List } from "@core/java/util/List";

	const props = defineProps<{
		add: (lehrer: UvLehrer[]) => Promise<void>;
		listLehrer: List<UvLehrer>;
	}>();

	const show = ref<boolean>(false);

	const openModal = () => {
		show.value = true;
	};

	const selectedLehrer = ref<UvLehrer[]>([]);

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc', span: 0.3 },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
	];

	async function importer() {
		await props.add(selectedLehrer.value);
		selectedLehrer.value = [];
		show.value = false;
	}

</script>
