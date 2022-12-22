<template>
	<svws-ui-content-card title="Sekundarstufe I">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Jahr Wechsel Sek I" v-model="inputSekIWechsel" type="text" />
			<svws-ui-text-input placeholder="Erste Schulform Sek I" v-model="inputSekIErsteSchulform" type="text" />
			<svws-ui-text-input placeholder="Jahr Wechsel Sek II" v-model="inputSekIIWechsel" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const inputSekIWechsel = computed<string | null>({
		get(): string | null {
			if (app.schulbesuchsdaten?.daten?.sekIWechsel !== null)
				return String(app.schulbesuchsdaten?.daten?.sekIWechsel);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ sekIWechsel:  val });
		}
	});

	const inputSekIErsteSchulform = computed<string | null>({
		get(): string | null {
			if (app.schulbesuchsdaten?.daten?.sekIErsteSchulform !== null)
				return String(app.schulbesuchsdaten?.daten?.sekIErsteSchulform);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ sekIErsteSchulform:  val });
		}
	});

	const inputSekIIWechsel = computed<string | null>({
		get(): string | null {
			if (app.schulbesuchsdaten?.daten?.sekIIWechsel !== null)
				return String(app.schulbesuchsdaten?.daten?.sekIIWechsel);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ sekIIWechsel:  val });
		}
	});

</script>
