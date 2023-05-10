<template>
	<svws-ui-content-card title="Schule auswählen">
		<div class="content-wrapper">
			<svws-ui-multi-select @update:model-value="runInitSchule" title="Auswahl Schule" autocomplete
				:items="listSchulkatalog" :item-text="(i: SchulenKatalogEintrag) => i.KurzBez || 'Schule ohne Name'"
				:item-filter="filter" required :disabled="loading" />
			<svws-ui-spinner :spinning="loading" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, SchulenKatalogEintrag } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		listSchulkatalog: List<SchulenKatalogEintrag>;
		initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	const filter = (items: SchulenKatalogEintrag[], search: string) =>
		items.filter(i =>
			i.SchulNr.includes(search.toLocaleLowerCase()) ||
			i.KurzBez?.toLocaleLowerCase().includes(search.toLocaleLowerCase()));

	async function runInitSchule(schule: SchulenKatalogEintrag) {
		loading.value = true;
		status.value = await props.initSchule(schule);
		loading.value = false;
	}
</script>

<style lang="postcss" scoped>
.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
