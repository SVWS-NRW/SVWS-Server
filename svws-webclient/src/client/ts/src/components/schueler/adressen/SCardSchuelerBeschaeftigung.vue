<template>
	<svws-ui-content-card>
		<div class="entry-wrapper">
			<h2 class="svws-ui-text-black col-span-3">Beschäftigung</h2>
			<div class="flex flex-col">
				<div class="overflow-x-auto sm:-mx-6 lg:-mx-8">
					<div class="inline-block min-w-full py-2 sm:px-6 lg:px-8">
						<div class="overflow-hidden">
							<table class="min-w-full">
								<thead class="border-b bg-gray-100">
									<tr>
										<th v-for="(data, index) in headerTags" :key="index" scope="col" class="px-6 py-4 text-left text-sm font-medium text-gray-900">
											{{ data }}
										</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="betrieb in listSchuelerbetriebe" :key="betrieb.id" @click="select(betrieb)" class="border-b bg-white transition duration-300 ease-in-out hover:bg-gray-400">
										<s-card-schueler-beschaeftigung-tabelle :betrieb="betrieb" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
											:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
											:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" />
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		patchSchuelerBetriebsdaten: (data : Partial<SchuelerBetriebsdaten>, id : number) => Promise<void>;
		setSchuelerBetrieb: (betrieb : SchuelerBetriebsdaten | undefined) => Promise<void>;
		listSchuelerbetriebe : List<SchuelerBetriebsdaten>;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapBetriebe: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const headerTags : ComputedRef<Array<string>> = computed(() => {
		return [ "Betrieb", "Ausbilder","Beschäftigungsart", "Beginn", "Ende", "Praktikum"];
	});

	async function select(betrieb : SchuelerBetriebsdaten) {
		await props.setSchuelerBetrieb(betrieb);
	}

</script>

<style scoped>

	.entry-content {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	h2 {
		@apply mb-2 text-sm font-bold;
	}

</style>
