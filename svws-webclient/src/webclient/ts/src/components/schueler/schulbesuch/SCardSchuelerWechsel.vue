<template>
	<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
		<div class="input-wrapper">
			<svws-ui-text-input
				v-model="inputAufnehmdendSchulnummer"
				type="text"
				placeholder="Name der Schule"
			/>
			<svws-ui-text-input
				v-model="inputAufnehmdendWechseldatum"
				type="date"
				placeholder="Wechseldatum"
			/>
			<svws-ui-checkbox
				v-model="inputAufnehmdendBestaetigt"
				text="Aufnahme bestätigt"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const inputAufnehmdendSchulnummer = computed<string | undefined>({
		get(): string | undefined {
			if (app && app.schulbesuchsdaten?.daten?.aufnehmdendSchulnummer) {
				return app.schulbesuchsdaten?.daten?.aufnehmdendSchulnummer.toString();
			}
			return undefined;
		},
		set(val: string | undefined) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ aufnehmdendSchulnummer:  val });
		}
	});

	const inputAufnehmdendWechseldatum = computed<string | undefined>({
		get(): string | undefined {
			if (app && app.schulbesuchsdaten?.daten?.aufnehmdendWechseldatum) {
				return app.schulbesuchsdaten?.daten?.aufnehmdendWechseldatum.toString();
			}
			return undefined;
		},
		set(val: string | undefined) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ aufnehmdendWechseldatum:  val });
		}
	});
	const inputAufnehmdendBestaetigt = computed<boolean | null>({
		get(): boolean | null {
			if (
				app &&
				app.schulbesuchsdaten?.daten?.aufnehmdendBestaetigt &&
				app.schulbesuchsdaten?.daten?.aufnehmdendBestaetigt !== null
			) {
				return app.schulbesuchsdaten.daten.aufnehmdendBestaetigt.valueOf();
			}
			return null;
		},
		set(val: boolean | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ aufnehmdendBestaetigt:  val });
		}
	});
</script>
