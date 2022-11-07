<template>
	<svws-ui-content-card title="Statusdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select
				v-model="inputStatus"
				title="Status"
				:items="inputKatalogSchuelerStatus"
				:item-text="(i: SchuelerStatus) => i.bezeichnung"
			/>
			<svws-ui-checkbox v-model="inputIstDuplikat"
				>Ist Duplikat</svws-ui-checkbox
			>
			<svws-ui-multi-select
				v-model="inputFahrschuelerArtID"
				title="Fahrschüler"
				:items="inputKatalogFahrschuelerarten"
			/>
			<svws-ui-multi-select
				v-model="inputHaltestelleID"
				title="Haltestelle"
				:items="inputKatalogHaltestellen"
			/>
			<svws-ui-text-input
				v-model="inputAnmeldedatum"
				type="date"
				placeholder="Anmeldedatum"
			/>
			<svws-ui-text-input
				v-model="inputAufnahmedatum"
				type="date"
				placeholder="Aufnahmedatum"
			/>
			<svws-ui-checkbox v-model="inputIstVolljaehrig"
				>Volljährig</svws-ui-checkbox
			>
			<svws-ui-checkbox v-model="inputKeineAuskunftAnDritte"
				>Keine Auskunft an Dritte
			</svws-ui-checkbox>
			<svws-ui-checkbox v-model="inputIstSchulpflichtErfuellt"
				>Schulpflicht erfüllt</svws-ui-checkbox
			>
			<svws-ui-checkbox v-model="inputIstBerufsschulpflichtErfuellt"
				>Schulpflicht SII erfüllt</svws-ui-checkbox
			>
			<svws-ui-checkbox v-model="inputHatMasernimpfnachweis">
				Masern Impfnachweis</svws-ui-checkbox
			>
			<svws-ui-checkbox v-model="inputErhaeltSchuelerBAFOEG"
				>BAFöG</svws-ui-checkbox
			>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import {
		KatalogEintrag,
		List,
		SchuelerStammdaten,
		SchuelerStatus
	} from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => {
		return app.stammdaten.daten || new SchuelerStammdaten();
	});

	const inputKatalogFahrschuelerarten: ComputedRef<List<KatalogEintrag>> =
		computed(() => {
			return app.katalogFahrschuelerarten;
		});
	const inputKatalogHaltestellen: ComputedRef<List<KatalogEintrag>> = computed(
		() => {
			return main.kataloge.haltestellen;
		}
	);
	const inputKatalogSchuelerStatus: ComputedRef<SchuelerStatus[]> = computed(
		() => {
			return SchuelerStatus.values();
		}
	);
	const inputStatus: WritableComputedRef<SchuelerStatus | undefined> =
		computed({
			//TODO id verewndenden
			get(): SchuelerStatus | undefined {
				return (
					SchuelerStatus.fromBezeichnung(daten.value.status) ||
					undefined
				);
			},
			set(val: SchuelerStatus | undefined): void {
				app.stammdaten.patch({ status: val?.bezeichnung });
			}
		});
	const inputFahrschuelerArtID: WritableComputedRef<
		KatalogEintrag | undefined
	> = computed({
		get(): KatalogEintrag | undefined {
			const id = daten.value.fahrschuelerArtID;
			for (const art of inputKatalogFahrschuelerarten.value)
				if (art.id === id)
					return art
		},
		set(val) {
			app.stammdaten.patch({ fahrschuelerArtID: val?.id });
		}
	});
	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> =
		computed({
			get(): KatalogEintrag | undefined {
				const id = daten.value.haltestelleID;
				for (const r of inputKatalogHaltestellen.value) 
					if (r.id === id)
						return r;
			},
			set(val) {
				app.stammdaten.patch({ haltestelleID: val?.id });
			}
		});
	const inputAnmeldedatum: WritableComputedRef<string | undefined> = computed(
		{
			get(): string | undefined {
				return daten.value.anmeldedatum?.toString();
			},
			set(val) {
				app.stammdaten.patch({ anmeldedatum: val });
			}
		}
	);
	const inputAufnahmedatum: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
				return daten.value.aufnahmedatum?.toString();
			},
			set(val) {
				app.stammdaten.patch({ aufnahmedatum: val });
			}
		});
	const inputIstDuplikat: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.istDuplikat;
		},
		set(val) {
			app.stammdaten.patch({ istDuplikat: val });
		}
	});
	const inputIstSchulpflichtErfuellt: WritableComputedRef<
		boolean | undefined
	> = computed({
		get(): boolean | undefined {
			return daten.value.istSchulpflichtErfuellt?.valueOf();
		},
		set(val: boolean | undefined) {
			app.stammdaten.patch({ istSchulpflichtErfuellt: val });
		}
	});
	const inputHatMasernimpfnachweis: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.hatMasernimpfnachweis;
		},
		set(val) {
			app.stammdaten.patch({ hatMasernimpfnachweis: val });
		}
	});
	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.keineAuskunftAnDritte;
		},
		set(val) {
			app.stammdaten.patch({ keineAuskunftAnDritte: val });
		}
	});
	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.erhaeltSchuelerBAFOEG;
		},
		set(val) {
			app.stammdaten.patch({ erhaeltSchuelerBAFOEG: val });
		}
	});
	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<
		boolean | undefined
	> = computed({
		get(): boolean | undefined {
			return daten.value.istBerufsschulpflichtErfuellt?.valueOf();
		},
		set(val) {
			app.stammdaten.patch({
				istBerufsschulpflichtErfuellt: val
			});
		}
	});
	const inputIstVolljaehrig: WritableComputedRef<boolean | undefined> =
		computed({
			get(): boolean | undefined {
				return daten.value.istVolljaehrig?.valueOf();
			},
			set(val) {
				app.stammdaten.patch({ istVolljaehrig: val });
			}
		});
</script>
