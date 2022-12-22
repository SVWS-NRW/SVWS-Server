<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Straße" v-model="inputStrasse" type="text" :valid="eingabeStrasseOk" />
			</div>
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="inputKatalogOrte" :item-filter="orte_filter"
				:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil"
				:item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
			<svws-ui-text-input placeholder="Telefon" v-model="inputTelefon" type="tel" />
			<svws-ui-text-input placeholder="Mobil oder Fax" v-model="inputTelefonMobil" type="tel" />
			<svws-ui-text-input placeholder="private E-Mail-Adresse" v-model="inputEmailPrivat" type="email" verify-email />
			<svws-ui-text-input placeholder="schulische E-Mail-Adresse" v-model="inputEmailSchule" type="email" verify-email />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";
	import { orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/helfer";

	import { AdressenUtils, List, OrtKatalogEintrag, OrtsteilKatalogEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());


	const main: Main = injectMainApp();


	const inputKatalogOrte: ComputedRef<Array<OrtKatalogEintrag>> = computed(() => {
		return main.kataloge.orte.toArray() as Array<OrtKatalogEintrag>;
	});

	const inputKatalogOrtsteil: ComputedRef<List<OrtsteilKatalogEintrag>> = computed(() => {
		return main.kataloge.ortsteile;
	});

	const eingabeStrasseOk = ref(true);

	const inputStrasse: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			const ret = AdressenUtils.combineStrasse(daten.value.strassenname || "", daten.value.hausnummer || "", daten.value.hausnummerZusatz || "");
			return ret ? ret.toString() : undefined;
		},
		set(val: string | undefined) {
			if (val) {
				const vals = AdressenUtils.splitStrasse(val);
				props.stammdaten.patch({ strassenname: vals?.[0] || val, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
				eingabeStrasseOk.value = !!vals;
			}
		}
	});

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get(): OrtKatalogEintrag | undefined {
			// FIXME: Suche implementieren sobald Funktionalität in List umgesetzt wurde
			return (inputKatalogOrte.value as OrtKatalogEintrag[]).find(o => o?.id == daten.value.wohnortID);
		},
		set(val: OrtKatalogEintrag | undefined) {
			props.stammdaten.patch({ wohnortID: val?.id });
		}
	});

	const inputOrtsteilID: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get(): OrtsteilKatalogEintrag | undefined {
			const id = daten.value.ortsteilID;
			let o;
			for (const r of inputKatalogOrtsteil.value) { 
				if (r.id === id) { 
					o = r;
					break;
				}
			}
			return o;
		},
		set(val: OrtsteilKatalogEintrag | undefined) {
			props.stammdaten.patch({ ortsteilID: val?.id });
		}
	});

	const inputTelefon: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.telefon?.toString();
		},
		set(val) {
			props.stammdaten.patch({ telefon: val });
		}
	});

	const inputTelefonMobil: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.telefonMobil?.toString();
		},
		set(val) {
			props.stammdaten.patch({ telefonMobil: val });
		}
	});

	const inputEmailPrivat: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.emailPrivat?.toString();
		},
		set(val) {
			props.stammdaten.patch({ emailPrivat: val });
		}
	});

	const inputEmailSchule: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.emailSchule?.toString();
		},
		set(val) {
			props.stammdaten.patch({ emailSchule: val });
		}
	});

</script>
