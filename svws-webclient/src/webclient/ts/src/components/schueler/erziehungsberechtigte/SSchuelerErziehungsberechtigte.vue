<template>
	<div v-if="visible" class="app-container relative">
		<div v-for="(e, i) in erzieher" :key="i" class="col-span-full">
			<s-card-schueler-erziehungsberechtigte :data="data" :erzieher="e" :erzieherarten="erzieherarten" :orte="orte" :ortsteile="ortsteile" />
		</div>
		<div class="flex justify-start">
			<svws-ui-button>Erziehungsberechtigten hinzufügen</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ErzieherStammdaten, List, OrtKatalogEintrag, OrtsteilKatalogEintrag, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataKatalogErzieherarten } from "~/apps/schueler/DataKatalogErzieherarten";
	import { DataSchuelerErzieherStammdaten } from "~/apps/schueler/DataSchuelerErzieherStammdaten";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		data: DataSchuelerErzieherStammdaten;
		erzieherarten: DataKatalogErzieherarten;
		orte: List<OrtKatalogEintrag>;
		ortsteile: List<OrtsteilKatalogEintrag>;
	}>();

	const erzieher: ComputedRef<List<ErzieherStammdaten> | undefined> = computed(() => props.data.daten);

	const visible: ComputedRef<boolean> = computed(() => {
		return true;
	});

</script>
