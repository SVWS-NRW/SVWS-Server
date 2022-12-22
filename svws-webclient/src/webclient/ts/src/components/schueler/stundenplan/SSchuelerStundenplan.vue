<template>
	<s-card-schueler-stundenplan v-if="visible" />
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, routename: string }>();

	const visible: ComputedRef<boolean> = computed<boolean>(() =>
		true
		// !!app.dataStundenplan?.daten
	);

</script>