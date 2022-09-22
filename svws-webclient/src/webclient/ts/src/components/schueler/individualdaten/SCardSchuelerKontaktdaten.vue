<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input
					v-model="inputStrasse"
					type="text"
					placeholder="Straße"
					:valid="eingabeStrasseOk"
				/>
			</div>
			<svws-ui-multi-select
				v-model="inputWohnortID"
				title="Wohnort"
				:items="inputKatalogOrte"
				:item-filter="orte_filter"
				:item-sort="orte_sort"
				:item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`"
				autocomplete
			/>
			<svws-ui-multi-select
				v-model="inputOrtsteilID"
				title="Ortsteil"
				:items="inputKatalogOrtsteil"
				:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil"
				:item-sort="ortsteilSort"
				:item-filter="ortsteilFilter"
			/>
			<svws-ui-text-input
				v-model="inputTelefon"
				type="tel"
				placeholder="Telefon"
			/>
			<svws-ui-text-input
				v-model="inputTelefonMobil"
				type="tel"
				placeholder="Mobil oder Fax"
			/>
			<svws-ui-text-input
				v-model="inputEmailPrivat"
				type="email"
				placeholder="private E-Mail-Adresse"
				verify-email
			/>
			<svws-ui-text-input
				v-model="inputEmailSchule"
				type="email"
				placeholder="schulische E-Mail-Adresse"
				verify-email
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";
	import {
		orte_filter,
		orte_sort,
		ortsteilFilter,
		ortsteilSort
	} from "~/helfer";

	import {
		AdressenUtils,
		OrtKatalogEintrag,
		OrtsteilKatalogEintrag,
		SchuelerStammdaten
	} from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const eingabeStrasseOk = ref(true);

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => {
		return app.stammdaten.daten || new SchuelerStammdaten();
	});

	const inputKatalogOrte: ComputedRef<OrtKatalogEintrag[]> = computed(() => {
		return main.kataloge.orte;
	});

	const inputKatalogOrtsteil: ComputedRef<OrtsteilKatalogEintrag[]> =
		computed(() => {
			return main.kataloge.ortsteile;
		});

	const inputStrasse: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			const d = app.stammdaten.daten;
			const ret = AdressenUtils.combineStrasse(
				d?.strassenname || "",
				d?.hausnummer || "",
				d?.hausnummerZusatz || ""
			);
			return ret ? ret.toString() : undefined;
		},
		set(val: string | undefined) {
			if (val) {
				const vals = AdressenUtils.splitStrasse(val);
				app.stammdaten.patch({
					strassenname: vals?.[0] || val,
					hausnummer: vals?.[1] || "",
					hausnummerZusatz: vals?.[2] || ""
				});
				eingabeStrasseOk.value = !!vals;
			}
		}
	});

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> =
		computed({
			get(): OrtKatalogEintrag | undefined {
				return inputKatalogOrte.value.find(
					o => o.id === daten.value.wohnortID
				);
			},
			set(val: OrtKatalogEintrag | undefined) {
				app.stammdaten.patch({ wohnortID: val?.id });
			}
		});

	const inputOrtsteilID: WritableComputedRef<
		OrtsteilKatalogEintrag | undefined
	> = computed({
		get(): OrtsteilKatalogEintrag | undefined {
			return inputKatalogOrtsteil.value.find(
				o => o.id === daten.value.ortsteilID
			);
		},
		set(val: OrtsteilKatalogEintrag | undefined) {
			app.stammdaten.patch({ ortsteilID: val?.id });
		}
	});

	const inputTelefon: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.telefon?.toString();
		},
		set(val) {
			app.stammdaten.patch({ telefon: val });
		}
	});

	const inputTelefonMobil: WritableComputedRef<string | undefined> = computed(
		{
			get(): string | undefined {
				return daten.value.telefonMobil?.toString();
			},
			set(val) {
				app.stammdaten.patch({ telefonMobil: val });
			}
		}
	);

	const inputEmailPrivat: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.emailPrivat?.toString();
		},
		set(val) {
			app.stammdaten.patch({ emailPrivat: val });
		}
	});

	const inputEmailSchule: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.emailSchule?.toString();
		},
		set(val) {
			app.stammdaten.patch({ emailSchule: val });
		}
	});
</script>
