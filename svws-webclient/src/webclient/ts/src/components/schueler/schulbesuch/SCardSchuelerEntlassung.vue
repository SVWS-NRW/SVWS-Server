<template>
	<svws-ui-content-card title="Entlassung von eigener Schule">
		<div class="input-wrapper">
			<svws-ui-text-input
				v-model="inputEntlassungDatum"
				type="date"
				placeholder="Entlassung am"
			/>
			<svws-ui-text-input
				v-model="inputEntlassungJahrgang"
				type="date"
				placeholder="Entlassjahrgang"
			/>
			<svws-ui-text-input
				v-model="inputEntlassungGrundID"
				placeholder="Bemerkung / Entlassgrund"
			/>
			<svws-ui-text-input
				v-model="inputEntlassungAbschlussartID"
				placeholder="Art des Abschlusses"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const inputEntlassungDatum: WritableComputedRef<string | null> = computed<
		string | null
	>({
		get(): string | null {
			if (app && app.schulbesuchsdaten.daten?.entlassungDatum && app.schulbesuchsdaten.daten?.entlassungDatum !== null) {
				return String(app.schulbesuchsdaten.daten.entlassungDatum);
			}
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ entlassungDatum:  val });
		}
	});

	const inputEntlassungJahrgang: WritableComputedRef<string | null> =
		computed<string | null>({
			get(): string | null {
				if (app && app.schulbesuchsdaten.daten?.entlassungJahrgang) {
					return String(
						app.schulbesuchsdaten.daten.entlassungJahrgang
					);
				}
				return null;
			},
			set(val: string | null) {
				val;
				// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ entlassungJahrgang:  val });
			}
		});

	const inputEntlassungGrundID: WritableComputedRef<number | null> = computed<
		number | null
	>({
		get(): number | null {
			if (app && app.schulbesuchsdaten.daten?.entlassungGrundID) {
				return Number(app.schulbesuchsdaten.daten.entlassungGrundID);
			}
			return null;
		},
		set(val: number | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ entlassungGrundID:  val });
		}
	});

	const inputEntlassungAbschlussartID: WritableComputedRef<string | null> =
		computed<string | null>({
			get(): string | null {
				if (
					app &&
					app.schulbesuchsdaten.daten?.entlassungAbschlussartID
				) {
					return String(
						app.schulbesuchsdaten.daten.entlassungAbschlussartID
					);
				}
				return null;
			},
			set(val: string | null) {
				val;
				// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ entlassungAbschlussartID:  val });
			}
		});
</script>
