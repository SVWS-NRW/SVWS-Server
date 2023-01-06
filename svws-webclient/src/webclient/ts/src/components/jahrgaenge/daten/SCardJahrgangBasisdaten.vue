<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="inputKuerzel"
					type="text"
					placeholder="Kürzel"
				/>
				<svws-ui-text-input
					v-model="inputBezeichnung"
					type="text"
					placeholder="Bezeichnung"
				/>
				<svws-ui-text-input
					v-model="inputKuerzelStatistik"
					placeholder="Bezeichnung in Statistik"
					type="text"
				/>
				<svws-ui-multi-select
					v-model="inputIdFolgejahrgang"
					:items="inputListJahrgaenge?.filter((e: JahrgangsListeEintrag) => e.id !== id)"
					:item-text="(e: JahrgangsListeEintrag) => e.bezeichnung?.toString() || ''"
					title="Folgejahrgang"
				/>
				<svws-ui-text-input
					v-model="inputKuerzelSchulgliederung"
					placeholder="Kürzel Schulgliederung"
					type="text"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.jahrgaenge;

	const inputListJahrgaenge: ComputedRef<JahrgangsListeEintrag[] | undefined> = computed(
		() => {
			return app.auswahl.liste;
		}
	);

	const id: ComputedRef<number | undefined> = computed(() => {
		return app.jahrgangsdaten.daten?.id.valueOf();
	});

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.jahrgangsdaten.daten?.kuerzel?.toString();
		},
		set(val: string | undefined) {
			app.jahrgangsdaten.patch({ kuerzel: val });
		}
	});

	const inputBezeichnung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.jahrgangsdaten.daten?.bezeichnung?.toString();
		},
		set(val: string | undefined) {
			app.jahrgangsdaten.patch({ bezeichnung: val });
		}
	});

	const inputKuerzelStatistik: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
					return app.jahrgangsdaten.daten?.kuerzelStatistik?.toString();
			},
			set(val: string | undefined) {
				app.jahrgangsdaten.patch({
					kuerzelStatistik: val
				});
			}
		});

		const inputKuerzelSchulgliederung: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
					return app.jahrgangsdaten.daten?.kuerzelSchulgliederung?.toString();
			},
			set(val: string | undefined) {
				app.jahrgangsdaten.patch({
					kuerzelSchulgliederung: val
				});
			}
		});

		const inputIdFolgejahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> =
		computed({
			get(): JahrgangsListeEintrag | undefined {
				const res = inputListJahrgaenge.value?.find((e: JahrgangsListeEintrag) => {
					return app.jahrgangsdaten.daten?.idFolgejahrgang === e.id;
				});
				return res;
			},
			set(val: JahrgangsListeEintrag | undefined) {
				app.jahrgangsdaten.patch({
					idFolgejahrgang: val?.id
				});
			}
		});
</script>
