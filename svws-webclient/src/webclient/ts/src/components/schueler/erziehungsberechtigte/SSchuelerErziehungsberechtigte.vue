<template>
	<div v-if="visible" class="app-container relative">
		<div v-for="(e, i) in erzieher" :key="i" class="col-span-full">
			<s-card-schueler-erziehungsberechtigte :data="props.data" :erzieher="e" :erzieherarten="props.erzieherarten" />
		</div>
		<div class="flex justify-start">
			<svws-ui-button>Erziehungsberechtigten hinzufügen</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ErzieherStammdaten, List, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataKatalogErzieherarten } from "~/apps/schueler/DataKatalogErzieherarten";
	import { DataSchuelerErzieherStammdaten } from "~/apps/schueler/DataSchuelerErzieherStammdaten";
	import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		data: DataSchuelerErzieherStammdaten,
		erzieherarten: DataKatalogErzieherarten
	}>();

	const erzieher: ComputedRef<List<ErzieherStammdaten> | undefined> = computed(() => props.data.daten);

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerErziehungsberechtigte.hidden());
	});

</script>
