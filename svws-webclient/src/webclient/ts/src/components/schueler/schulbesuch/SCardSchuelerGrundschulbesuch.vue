// //
<template>
	<svws-ui-content-card title="Grundschulbesuch">
		<div class="input-wrapper">
			<svws-ui-text-input
				v-model="inputGrundschuleEinschulungsjahr"
				type="text"
				placeholder="Einschulung"
			/>
			<svws-ui-text-input
				v-model="inputGrundschuleEinschulungsartID"
				placeholder="Einschulungsart"
			/>
			<svws-ui-text-input
				v-model="inputGrundschuleJahreEingangsphase"
				placeholder="EP-Jahre"
			/>
			<svws-ui-text-input
				v-model="inputGrundschuleUebergangsempfehlungID"
				type="text"
				placeholder="Übergangsempfehlung Jg. 5"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const inputGrundschuleEinschulungsjahr: WritableComputedRef<number | null> =
		computed<number | null>({
			get(): number | null {
				if (
					app &&
					app.schulbesuchsdaten.daten?.grundschuleEinschulungsjahr !== null
				) {
					return Number(
						app.schulbesuchsdaten.daten?.grundschuleEinschulungsjahr
					);
				}
				return null;
			},
			set(val: number | null) {
				val;
				// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ grundschuleEinschulungsjahr:  val });
			}
		});

	const inputGrundschuleEinschulungsartID: WritableComputedRef<
		number | null
	> = computed<number | null>({
		get(): number | null {
			if (
				app &&
				app.schulbesuchsdaten.daten?.grundschuleEinschulungsartID !== null
			) {
				return Number(
					app.schulbesuchsdaten.daten?.grundschuleEinschulungsartID
				);
			}
			return null;
		},
		set(val: number | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ grundschuleEinschulungsartID:  val });
		}
	});

	const inputGrundschuleJahreEingangsphase: WritableComputedRef<
		number | null
	> = computed<number | null>({
		get(): number | null {
			if (
				app &&
				app.schulbesuchsdaten.daten?.grundschuleJahreEingangsphase &&
				app.schulbesuchsdaten.daten?.grundschuleJahreEingangsphase !== null
			) {
				return Number(
					app.schulbesuchsdaten.daten.grundschuleJahreEingangsphase
				);
			}
			return null;
		},
		set(val: number | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ grundschuleJahreEingangsphase:  val });
		}
	});

	const inputGrundschuleUebergangsempfehlungID: WritableComputedRef<
		number | null
	> = computed({
		get(): number | null {
			if (
				app &&
				app.schulbesuchsdaten.daten?.grundschuleUebergangsempfehlungID &&
				app.schulbesuchsdaten.daten?.grundschuleUebergangsempfehlungID !== null
			) {
				return Number(
					app.schulbesuchsdaten.daten
						.grundschuleUebergangsempfehlungID
				);
			}
			return null;
		},
		set(val: number | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ grundschuleUebergangsempfehlungID:  val });
		}
	});
</script>
