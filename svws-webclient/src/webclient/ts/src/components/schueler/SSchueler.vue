/* eslint-disable vue/no-parsing-error */
<template>
	<Teleport to="#auswahl">
		<s-schueler-auswahl />
	</Teleport>
	<Teleport to="#app">
		<s-schueler-app />
	</Teleport>
	<Teleport to="#gruppenaktionen">
		<s-schueler-gruppenaktionen />
	</Teleport>
</template>

<script setup lang="ts">
	import {defineAsyncComponent } from "vue";

	const SSchuelerApp = defineAsyncComponent(
		() => import("~/components/schueler/SSchuelerApp.vue")
	);
	const SSchuelerAuswahl = defineAsyncComponent(
		() => import("~/components/schueler/SSchuelerAuswahl.vue")
	);
	const SSchuelerGruppenaktionen = defineAsyncComponent(
		() =>
			import("~/components/schueler/SSchuelerGruppenaktionen.vue")
	);
</script>
