<template>
	<div v-if="visible" class="flex flex-row">
		<div class="w-32 mr-2"> <router-view :key="$route.hash" name="lernabschnittauswahl" /> </div>
		<div> <router-view :key="$route.hash" /> </div>
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
	
	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, stammdaten: DataSchuelerStammdaten, routename: string }>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerLeistungen.hidden);
	});

</script>
