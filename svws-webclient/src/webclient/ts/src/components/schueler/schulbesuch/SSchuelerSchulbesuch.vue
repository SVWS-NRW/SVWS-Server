<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-herkunft :data="props.data" />
		<s-card-schueler-entlassung :data="props.data" />
		<s-card-schueler-wechsel :data="props.data" />
		<s-card-schueler-grundschulbesuch :data="props.data" />
		<s-card-schueler-sekundarstufe1 :data="props.data" />
		<s-card-schueler-merkmale :data="props.data" />
		<s-card-schueler-schulen :data="props.data" />
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";
	import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/RouteSchuelerSchulbesuch";

	const props = defineProps<{ id?: number, item?: SchuelerListeEintrag, data: DataSchuelerSchulbesuchsdaten, routename: string }>();
	
	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerSchulbesuch.hidden) && (props.id !== undefined);
	});

</script>
