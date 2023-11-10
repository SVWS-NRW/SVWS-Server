<template>
	<svws-ui-content-card title="Schule auswählen">
		<div class="flex items-start gap-3">
			<svws-ui-select :model-value="model" @update:model-value="runInitSchule" title="Auswahl Schule" autocomplete
				:items="listSchulkatalog" :item-text="i => i.KurzBez || 'Schule ohne Name'"
				:item-filter="filter" required :disabled="loading" />
			<svws-ui-spinner :spinning="loading" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, SchulenKatalogEintrag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		listSchulkatalog: List<SchulenKatalogEintrag>;
		initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const model = ref<SchulenKatalogEintrag>();

	const filter = (items: Iterable<SchulenKatalogEintrag>, search: string) => {
		const list = [];
		for (const i of items)
			if (i.SchulNr.includes(search.toLocaleLowerCase())
				|| i.KurzBez?.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	async function runInitSchule(schule: SchulenKatalogEintrag | null | undefined) {
		if (schule === null || schule === undefined)
			return;
		loading.value = true;
		status.value = await props.initSchule(schule);
		loading.value = false;
	}
</script>

