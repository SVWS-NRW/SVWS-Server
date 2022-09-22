<template>
	<div v-if="app.dataJahrgang.daten && visible" class="app-container">
		<s-card-gost-basisdaten />
		<div v-if="enabled">
			<s-card-gost-beratungslehrer />
			<s-card-gost-text-beratungsbogen />
			<s-card-gost-text-mailversand />
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const SCardGostBasisdaten = defineAsyncComponent(
		() => import("~/components/gost/stammdaten/SCardGostBasisdaten.vue")
	);

	const SCardGostBeratungslehrer = defineAsyncComponent(
		() =>
			import("~/components/gost/stammdaten/SCardGostBeratungslehrer.vue")
	);

	const SCardGostTextBeratungsbogen = defineAsyncComponent(
		() =>
			import(
				"~/components/gost/stammdaten/SCardGostTextBeratungsbogen.vue"
			)
	);

	const SCardGostTextMailversand = defineAsyncComponent(
		() =>
			import("~/components/gost/stammdaten/SCardGostTextMailversand.vue")
	);

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const enabled: ComputedRef<boolean> = computed(() => {
		return (
			!!app.dataJahrgang.daten?.abiturjahr &&
			app.dataJahrgang.daten?.abiturjahr > 0
		);
	});

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.gostStammdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
