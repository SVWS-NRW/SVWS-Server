<template>
	<div v-if="visible" class="flex flex-row">
		<div class="w-32 mr-2"> <router-view :key="$route.hash" name="lernabschnittauswahl" /> </div>
		<div class="grow"> <router-view :key="$route.hash" /> </div>
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerLeistungen.hidden());
	});

</script>
