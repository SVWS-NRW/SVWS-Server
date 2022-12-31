<template>
	<s-card-schueler-stundenplan v-if="visible" />
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, routename: string }>();

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		return !(routeSchuelerStundenplan.hidden);
	});

</script>