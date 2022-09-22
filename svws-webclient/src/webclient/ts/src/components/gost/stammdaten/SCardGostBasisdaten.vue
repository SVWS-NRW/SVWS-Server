<template>
	<svws-ui-content-card :title="'Basisdaten Jahrgang: ' + bezeichnung">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="inputAbiturjahr"
					type="text"
					placeholder="Abiturjahr"
				/>
				<svws-ui-text-input
					v-model="inputJahrgang"
					type="text"
					placeholder="Jahrgang"
				/>
				<svws-ui-text-input
					v-model="inputZusatzGE"
					type="text"
					placeholder="Zusatzkursbeginn in GE"
				/>
				<svws-ui-text-input
					v-model="inputZusatzSW"
					type="text"
					placeholder="Zusatzkursbeginn in SW"
				/>
				<svws-ui-checkbox v-model="InputIstAbgeschlossen"
					>Abitur abgeschlossen</svws-ui-checkbox
				>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const bezeichnung: ComputedRef<string | undefined> = computed(() => {
		return app.dataJahrgang?.daten?.bezeichnung?.toString();
	});

	const inputAbiturjahr: ComputedRef<number | string> = computed(() => {
		return !app.dataJahrgang.daten?.abiturjahr ||
			app.dataJahrgang.daten?.abiturjahr < 0
			? ""
			: app.dataJahrgang.daten?.abiturjahr.valueOf();
	});

	const inputJahrgang: ComputedRef<string | undefined> = computed(() => {
		return app.dataJahrgang.daten?.jahrgang?.toString();
	});

	const inputZusatzGE: ComputedRef<string | undefined> = computed(() => {
		return app.dataJahrgang.daten?.beginnZusatzkursGE?.toString();
	});

	const inputZusatzSW: ComputedRef<string | undefined> = computed(() => {
		return app.dataJahrgang.daten?.beginnZusatzkursSW?.toString();
	});

	const InputIstAbgeschlossen: ComputedRef<boolean> = computed(() => {
		return !!app.dataJahrgang.daten?.istAbgeschlossen;
	});
</script>
