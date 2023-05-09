<template>
	<svws-ui-content-card title="MDB-Datei auswählen">
		<div class="content-wrapper">
			<svws-ui-multi-select @update:model-value="initSchule" title="Auswahl Schule" autocomplete
				:items="listSchulkatalog" :item-text="(i: SchulenKatalogEintrag) => i.KurzBez || 'Schule ohne Name'"
				:item-filter="filter" required />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, SchulenKatalogEintrag } from "@svws-nrw/svws-core";

	const props = defineProps<{
		listSchulkatalog: List<SchulenKatalogEintrag>;
		initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	}>();

	const filter = (items: SchulenKatalogEintrag[], search: string) =>
		items.filter(i =>
			i.SchulNr.includes(search.toLocaleLowerCase()) ||
			i.KurzBez?.toLocaleLowerCase().includes(search.toLocaleLowerCase()));
</script>

<style lang="postcss" scoped>
.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
