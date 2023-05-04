<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="init-wrapper">
				<div class="init-container">
					<div class="init-form modal modal--sm">
						<div class="modal--content-wrapper">
							<div class="modal--content">
								<div class="init-form-header mb-8 px-8 py-4 mt-6">
									<h1 class="leading-none text-center w-full">
										<span class="font-normal">Schulauswahl</span>
									</h1>
								</div>
								<div class="w-full mt-1 flex flex-col gap-2 items-center px-8" />
								<svws-ui-multi-select @update:model-value="initSchule" title="Auswahl Schule" autocomplete
									:items="listSchulkatalog" :item-text="(i: SchulenKatalogEintrag) => i.ABez1 || i.ABez2 || i.ABez3 || 'Schule ohne Name'"
									:item-filter="filter"
									required />
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import type { InitSchulkatalogProps } from "./SInitSchulkatalogProps";
	import type { SchulenKatalogEintrag } from "@svws-nrw/svws-core";
	import {ref} from "vue";

	const props = defineProps<InitSchulkatalogProps>();

	/** Filter für Staatsangehörigkeiten */
	const filter = (items: SchulenKatalogEintrag[], search: string) => {
		return items.filter(i => {
			i.SchulNr.includes(search.toLocaleLowerCase()) ||
				i.ABez1?.toLocaleLowerCase()
					.includes(search.toLocaleLowerCase())
		});
	};
</script>

<style lang="postcss" scoped>
.init-wrapper {
	@apply flex h-full flex-col justify-between;
}

.init-container {
	@apply bg-cover bg-top rounded-t-2xl h-full flex flex-col justify-center items-center px-4;
	/*background-image: radial-gradient(rgba(0,0,0,0.25), rgba(0,0,0,0.3)), url("/images/init-background-1.jpg");*/
	/*background-image: radial-gradient(rgba(0,0,0,0.15), rgba(0,0,0,0.2)), url("/images/placeholder-background.jpg");*/
	background-image: url("/images/placeholder-background.jpg");
}

.modal {
	@apply shadow-black/25 rounded-xl;
}

.init-form .modal--content {
	@apply p-0;
}

.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
