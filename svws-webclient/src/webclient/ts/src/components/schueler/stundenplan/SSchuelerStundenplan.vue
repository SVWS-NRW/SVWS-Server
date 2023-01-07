<template>
	<div v-if="visible" class="flex flex-row">
		<div class="w-[26rem] mr-2"> <router-view :key="$route.hash" name="stundenplanauswahl" /> </div>
		<div> <router-view :key="$route.hash" /> </div>
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";

	const props = defineProps<{ 
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
	}>();

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		return !(routeSchuelerStundenplan.hidden);
	});

</script>