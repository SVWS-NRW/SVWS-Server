<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input v-model="inputStrasse" type="text" placeholder="Straße" required :valid="eingabeStrasseOk" />
			</div>
			<svws-ui-multi-select v-model="inputWohnortID" title="Wohnort" :items="orte" :item-filter="orte_filter" :item-sort="orte_sort"
				:item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-multi-select v-model="inputOrtsteilID" title="Ortsteil" :items="ortsteile" :item-sort="ortsteilSort"
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
	import { orte_filter, orte_sort, ortsteilSort } from "~/helfer";

	const eingabeStrasseOk: Ref<boolean> = ref(true);


	const props = defineProps<{
		stammdaten: LehrerStammdaten;
		orte: List<OrtKatalogEintrag>;
		ortsteile: List<OrtsteilKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<LehrerStammdaten>): void;
	}>()

	function doPatch(data: Partial<LehrerStammdaten>) {
		emit('patch', data);
	}

	const inputStrasse: WritableComputedRef<string | undefined> = computed({
		get(): string {
			const d = props.stammdaten;
			const ret = AdressenUtils.combineStrasse(d.strassenname || "", d.hausnummer || "", d.hausnummerZusatz || "");
			return ret ?? "";
		},
		set(val: string | undefined) {
			if (val) {
				const vals = AdressenUtils.splitStrasse(val);
				doPatch({ strassenname: vals?.[0] || val, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
				eingabeStrasseOk.value = !!vals;
			}
		}
	});

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get(): OrtKatalogEintrag | undefined {
			const id = props.stammdaten.wohnortID;
			let o;
			for (const r of props.orte) {
				if (r.id === id) {
					o = r;
					break;
				}
			}
			return o;
		},
		set(val: OrtKatalogEintrag | undefined) {
			doPatch({ wohnortID: val?.id });
		}
	});

	const inputOrtsteilID: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get(): OrtsteilKatalogEintrag | undefined {
			const id = props.stammdaten.ortsteilID;
			let o;
			for (const r of props.ortsteile) {
				if (r.id === id) {
					o = r;
					break;
				}
			}
			return o;
		},
		set(val) {
			doPatch({ ortsteilID: val?.id });
		}
	});

	const inputTelefon: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.stammdaten.telefon ?? undefined;
		},
		set(val: string | undefined) {
			doPatch({ telefon: val });
		}
	});

	const inputTelefonMobil: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.stammdaten.telefonMobil ?? undefined;
		},
		set(val) {
			doPatch({ telefonMobil: val });
		}
	});

	const inputEmailPrivat: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.stammdaten.emailPrivat ?? undefined;
		},
		set(val) {
			doPatch({ emailPrivat: val });
		}
	});

	const inputEmailDienstlich: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.stammdaten.emailDienstlich ?? undefined;
		},
		set(val) {
			doPatch({ emailDienstlich: val });
		}
	});

</script>
