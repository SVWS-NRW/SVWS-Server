<template>
	<svws-ui-content-card title="Migrationshintergrund">
		<div class="input-wrapper">
			<svws-ui-checkbox v-model="inputHatMigrationshintergrund"
				>Migrationshintergrund vorhanden</svws-ui-checkbox
			>
			<svws-ui-text-input
				v-model="inputZuzugsjahr"
				type="text"
				placeholder="Zuzugsjahr"
				:disabled="!inputHatMigrationshintergrund"
			/>
			<svws-ui-multi-select
				v-model="inputGeburtsland"
				title="Geburtsland"
				:items="inputKatalogNationalitaeten"
				:item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`"
				:item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter"
				:disabled="!inputHatMigrationshintergrund"
			/>
			<svws-ui-multi-select
				v-model="inputVerkehrspracheFamilie"
				autocomplete
				title="Verkehrssprache"
				:items="inputKatalogVerkehrssprachen"
				:item-text="(i: Verkehrssprache) => `${i.daten.bezeichnung} (${i.daten.kuerzel})`"
				:item-sort="verkehrsspracheKatalogEintragSort"
				:item-filter="verkehrsspracheKatalogEintragFilter"
				:disabled="!inputHatMigrationshintergrund"
			/>
			<svws-ui-multi-select
				v-model="inputGeburtslandMutter"
				autocomplete
				title="Geburtsland Mutter"
				:items="inputKatalogNationalitaeten"
				:item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`"
				:item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter"
				:disabled="!inputHatMigrationshintergrund"
			/>
			<svws-ui-multi-select
				v-model="inputGeburtslandVater"
				title="Geburtsland Vater"
				:items="inputKatalogNationalitaeten"
				:item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`"
				:item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter"
				:disabled="!inputHatMigrationshintergrund"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import {
		Nationalitaeten,
		Verkehrssprache,
		SchuelerStammdaten
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	import {
		verkehrsspracheKatalogEintragFilter,
		verkehrsspracheKatalogEintragSort,
		nationalitaetenKatalogEintragFilter,
		nationalitaetenKatalogEintragSort
	} from "~/helfer";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => {
		return app.stammdaten.daten || new SchuelerStammdaten();
	});

	const inputKatalogNationalitaeten: ComputedRef<Nationalitaeten[]> = computed(() => 
		Nationalitaeten.values()
	);

	const inputKatalogVerkehrssprachen: ComputedRef<Verkehrssprache[]> = computed(() => 
		Verkehrssprache.values()
	);

	const inputGeburtsland: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(daten.value.geburtsland) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			app.stammdaten.patch({ geburtsland: val.daten.iso3 });
		}
	});

	const inputZuzugsjahr: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.zuzugsjahr?.toString();
		},
		set(val: string | undefined) {
			app.stammdaten.patch({ zuzugsjahr: val });
		}
	});

	const inputGeburtslandMutter: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(daten.value.geburtslandMutter) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			app.stammdaten.patch({ geburtslandMutter: val.daten.iso3 });
		}
	});

	const inputGeburtslandVater: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(daten.value.geburtslandVater) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			app.stammdaten.patch({ geburtslandVater: val.daten.iso3 });
		}
	});

	const inputVerkehrspracheFamilie: WritableComputedRef<Verkehrssprache> =
		computed({
			get(): Verkehrssprache {
				return Verkehrssprache.getByKuerzelAuto(daten.value.verkehrspracheFamilie) || Verkehrssprache.DEU
			},
			set(val: Verkehrssprache) {
				app.stammdaten.patch({ verkehrspracheFamilie: val.daten.kuerzel });
			}
		});

	const inputHatMigrationshintergrund: WritableComputedRef<
		boolean | undefined
	> = computed({
		//TODO Reset Sprachen Familie bzw. Abhängigkeiten
		get(): boolean | undefined {
			return daten.value.hatMigrationshintergrund;
		},
		set(val: boolean | undefined) {
			app.stammdaten.patch({
				hatMigrationshintergrund: val
			});
		}
	});
</script>
