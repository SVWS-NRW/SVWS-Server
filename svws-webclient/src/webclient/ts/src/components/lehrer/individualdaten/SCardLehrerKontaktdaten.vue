<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input v-model="inputStrasse" type="text" placeholder="Straße" required :valid="eingabeStrasseOk" />
			</div>
			<svws-ui-multi-select v-model="inputWohnortID" title="Wohnort" :items="inputKatalogOrte" :item-filter="orte_filter" :item-sort="orte_sort"
				:item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-multi-select v-model="inputOrtsteilID" title="Ortsteil" :items="inputKatalogOrtsteil" :item-sort="ortsteilSort"
				:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" />
			<svws-ui-text-input v-model="inputTelefon" type="tel" placeholder="Telefon" />
			<svws-ui-text-input v-model="inputTelefonMobil" type="tel" placeholder="Mobil oder Fax" />
			<svws-ui-text-input v-model="inputEmailPrivat" type="email" placeholder="private E-Mail-Adresse" verify-email />
			<svws-ui-text-input v-model="inputEmailDienstlich" type="email" placeholder="schulische E-Mail-Adresse" verify-email />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

	import { AdressenUtils, LehrerStammdaten, List, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { orte_filter, orte_sort, ortsteilSort } from "~/helfer";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";

	const eingabeStrasseOk: Ref<boolean> = ref(true);

	const main: Main = injectMainApp();

	const props = defineProps<{ stammdaten: DataLehrerStammdaten }>();

	const daten: ComputedRef<LehrerStammdaten> = computed(() => props.stammdaten.daten || new LehrerStammdaten());

	const inputKatalogOrte: ComputedRef<List<OrtKatalogEintrag>> = computed(() => {
		return main.kataloge.orte;
	});

	const inputKatalogOrtsteil: ComputedRef<List<OrtsteilKatalogEintrag>> = computed(() => {
		return main.kataloge.ortsteile;
	});

	const inputStrasse: WritableComputedRef<string | undefined> = computed({
		get(): string {
			const d = daten.value;
			const ret = AdressenUtils.combineStrasse(d.strassenname || "", d.hausnummer || "", d.hausnummerZusatz || "");
			return ret ?? "";
		},
		set(val: string | undefined) {
			if (val) {
				const vals = AdressenUtils.splitStrasse(val);
				void props.stammdaten.patch({ strassenname: vals?.[0] || val, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
				eingabeStrasseOk.value = !!vals;
			}
		}
	});

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get(): OrtKatalogEintrag | undefined {
			const id = daten.value.wohnortID;
			let o;
			for (const r of inputKatalogOrte.value) {
				if (r.id === id) {
					o = r;
					break;
				}
			}
			return o;
		},
		set(val: OrtKatalogEintrag | undefined) {
			void props.stammdaten.patch({ wohnortID: val?.id });
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
		set(val) {
			void props.stammdaten.patch({ ortsteilID: val?.id });
		}
	});

	const inputTelefon: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.telefon ?? undefined;
		},
		set(val: string | undefined) {
			void props.stammdaten.patch({ telefon: val });
		}
	});

	const inputTelefonMobil: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.telefonMobil ?? undefined;
		},
		set(val) {
			void props.stammdaten.patch({ telefonMobil: val });
		}
	});

	const inputEmailPrivat: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.emailPrivat ?? undefined;
		},
		set(val) {
			void props.stammdaten.patch({ emailPrivat: val });
		}
	});

	const inputEmailDienstlich: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.emailDienstlich ?? undefined;
		},
		set(val) {
			void props.stammdaten.patch({ emailDienstlich: val });
		}
	});

</script>
