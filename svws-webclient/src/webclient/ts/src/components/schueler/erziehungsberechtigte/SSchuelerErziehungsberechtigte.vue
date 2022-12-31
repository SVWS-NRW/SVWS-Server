<template>
	<div v-if="visible" class="app-container relative">
		<div class="svws-ui-bg-white sticky top-0 z-50 col-span-3 flex justify-end py-4">
			<svws-ui-button>Erziehungsberechtigten hinzufügen</svws-ui-button>
		</div>
		<div v-for="(e, i) in erzieher" :key="i" class="col-span-3">
			<s-card-schueler-erziehungsberechtigte :erzieher="e" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ErzieherStammdaten, List, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataSchuelerErzieherStammdaten } from "~/apps/schueler/DataSchuelerErzieherStammdaten";
	import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";

	const props = defineProps<{ id?: number, item?: SchuelerListeEintrag, data: DataSchuelerErzieherStammdaten, routename: string }>();

	const erzieher: ComputedRef<List<ErzieherStammdaten> | undefined> = computed(() => props.data.daten);

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerErziehungsberechtigte.hidden);
	});

</script>
