<template>
	<div class="modal--content">
		<div class="init-form-header mb-8 px-8 py-4 mt-6">
			<h1 class="leading-none text-center w-full">
				<span class="font-normal">Schulauswahl</span>
			</h1>
		</div>
		<div class="w-full mt-1 flex flex-col gap-2 items-center px-8" />
		<svws-ui-multi-select @update:model-value="initSchule" title="Auswahl Schule" autocomplete
			:items="listSchulkatalog" :item-text="(i: SchulenKatalogEintrag) => i.KurzBez || 'Schule ohne Name'"
			:item-filter="filter" required />
	</div>
</template>

<script setup lang="ts">

	import type { InitSchulkatalogProps } from "./SInitSchulkatalogProps";
	import type { SchulenKatalogEintrag } from "@svws-nrw/svws-core";

	const props = defineProps<InitSchulkatalogProps>();

	/** Filter für Staatsangehörigkeiten */
	const filter = (items: SchulenKatalogEintrag[], search: string) => {
		return items.filter(i =>
			i.SchulNr.includes(search.toLocaleLowerCase()) ||
			i.KurzBez?.toLocaleLowerCase()
				.includes(search.toLocaleLowerCase())
		);
	};
</script>

<style lang="postcss" scoped>
.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
