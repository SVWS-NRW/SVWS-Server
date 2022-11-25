<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="flex-shrink-0">
				<svws-ui-avatar
					:src="'data:image/png;base64, ' + foto"
					:alt="'Foto ' + vorname + ' ' + nachname"
				/>
			</div>
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="nachname"
					type="text"
					placeholder="Nachname"
				/>
				<svws-ui-text-input
					v-model="zusatzNachname"
					type="text"
					placeholder="Zusatz"
				/>
				<svws-ui-text-input
					v-model="vorname"
					type="text"
					placeholder="Rufname"
				/>
				<svws-ui-text-input
					v-model="alleVornamen"
					type="text"
					placeholder="Alle Vornamen"
				/>
				<svws-ui-multi-select
					v-model="geschlecht"
					title="Geschlecht"
					:items="inputKatalogGeschlecht"
					statistics
				/>
				<svws-ui-text-input
					v-model="inputGeburtsdatum"
					type="date"
					placeholder="Geburtsdatum"
					required
					statistics
				/>
				<svws-ui-text-input
					v-model="inputGeburtsort"
					type="text"
					placeholder="Geburtsort"
					statistics
				/>
				<svws-ui-text-input
					v-model="inputGeburtsname"
					type="text"
					placeholder="Geburtsname"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Geschlecht, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { UseSchuelerStammdaten } from "~/utils/composables/stammdaten"

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const use = new UseSchuelerStammdaten(app.stammdaten)

	const {vorname, alleVornamen, nachname, zusatzNachname, geschlecht} = use
	const daten: ComputedRef<SchuelerStammdaten> = computed(() => {
		return app.stammdaten.daten || new SchuelerStammdaten();
	});

	const inputKatalogGeschlecht: ComputedRef<Geschlecht[]> = computed(() => {
		return Geschlecht.values();
	});

	const id: ComputedRef<number | undefined> = computed(() => {
		return daten.value.id.valueOf();
	});

	const foto: ComputedRef<string | undefined> = computed(() => {
		return daten.value.foto?.toString();
	});



	const inputGeburtsdatum: WritableComputedRef<string | undefined> = computed(
		{
			get(): string | undefined {
				return daten.value.geburtsdatum?.toString();
			},
			set(val: string | undefined) {
				app.stammdaten.patch({ geburtsdatum: val });
			}
		}
	);

	const inputGeburtsort: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.geburtsort?.toString();
		},
		set(val: string | undefined) {
			app.stammdaten.patch({ geburtsort: val });
		}
	});

	const inputGeburtsname: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.geburtsname?.toString();
		},
		set(val: string | undefined) {
			app.stammdaten.patch({ geburtsname: val });
		}
	});
</script>
