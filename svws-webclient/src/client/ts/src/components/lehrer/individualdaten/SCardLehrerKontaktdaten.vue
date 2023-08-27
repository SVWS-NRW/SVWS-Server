<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input v-model="inputStrasse" type="text" placeholder="Straße" required span="full" />
			<svws-ui-multi-select v-model="inputWohnortID" title="Wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort"
				:item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-multi-select v-model="inputOrtsteilID" title="Ortsteil" :items="mapOrtsteile" :item-sort="ortsteilSort"
				:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" />
			<svws-ui-spacing />
			<svws-ui-text-input v-model="inputTelefon" type="tel" placeholder="Telefon" />
			<svws-ui-text-input v-model="inputTelefonMobil" type="tel" placeholder="Mobil oder Fax" />
			<svws-ui-text-input v-model="inputEmailPrivat" type="email" placeholder="Private E-Mail-Adresse" verify-email />
			<svws-ui-text-input v-model="inputEmailDienstlich" type="email" placeholder="Schulische E-Mail-Adresse" verify-email />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import type { WritableComputedRef } from "vue";
	import { orte_filter, orte_sort, ortsteilSort } from "~/helfer";
	import { AdressenUtils } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		stammdaten: LehrerStammdaten;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<LehrerStammdaten>): void;
	}>()

	function doPatch(data: Partial<LehrerStammdaten>) {
		emit('patch', data);
	}

	const inputStrasse = computed<string | undefined>({
		get(): string {
			const d = props.stammdaten;
			const ret = AdressenUtils.combineStrasse(d.strassenname || "", d.hausnummer || "", d.hausnummerZusatz || "");
			return ret ?? "";
		},
		set(val: string | undefined) {
			if (val) {
				const vals = AdressenUtils.splitStrasse(val);
				doPatch({ strassenname: vals?.[0] || val, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
			}
		}
	});

	const inputWohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () =>props.stammdaten.wohnortID ? props.mapOrte.get(props.stammdaten.wohnortID) : undefined,
		set: (val) =>	doPatch({ wohnortID: val?.id })
	});

	const inputOrtsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => props.stammdaten.ortsteilID ? props.mapOrtsteile.get(props.stammdaten.ortsteilID) : undefined,
		set: (val) =>	doPatch({ ortsteilID: val?.id })
	});

	const inputTelefon = computed<string | undefined>({
		get: () => props.stammdaten.telefon ?? undefined,
		set: (val) => doPatch({ telefon: val })
	});

	const inputTelefonMobil: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.stammdaten.telefonMobil ?? undefined;
		},
		set(val) {
			doPatch({ telefonMobil: val });
		}
	});

	const inputEmailPrivat = computed<string | undefined>({
		get(): string | undefined {
			return props.stammdaten.emailPrivat ?? undefined;
		},
		set(val) {
			doPatch({ emailPrivat: val });
		}
	});

	const inputEmailDienstlich = computed<string | undefined>({
		get(): string | undefined {
			return props.stammdaten.emailDienstlich ?? undefined;
		},
		set(val) {
			doPatch({ emailDienstlich: val });
		}
	});

</script>
