<template>
	<svws-ui-content-card title="Sekundarstufe I">
		<div class="input-wrapper">
			<svws-ui-text-input
				v-model="inputSekIWechsel"
				type="text"
				placeholder="Jahr Wechsel Sek I"
			/>
			<svws-ui-text-input
				v-model="inputSekIErsteSchulform"
				type="text"
				placeholder="Erste Schulform Sek I"
			/>
			<svws-ui-text-input
				v-model="inputSekIIWechsel"
				type="text"
				placeholder="Jahr Wechsel Sek II"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const inputSekIWechsel = computed<string>({
		get(): string {
			return String(app.schulbesuchsdaten?.daten?.sekIWechsel);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ sekIWechsel:  val });
		}
	});
	const inputSekIErsteSchulform = computed<string>({
		get(): string {
			return String(app.schulbesuchsdaten?.daten?.sekIErsteSchulform);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ sekIErsteSchulform:  val });
		}
	});
	const inputSekIIWechsel = computed<string>({
		get(): string {
			return String(app.schulbesuchsdaten?.daten?.sekIIWechsel);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ sekIIWechsel:  val });
		}
	});
</script>
