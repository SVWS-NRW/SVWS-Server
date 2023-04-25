<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Straße" v-model="inputStrasse" type="text" :valid="eingabeStrasseOk" />
			</div>
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-filter="orte_filter"
				:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="mapOrtsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''"
				:item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
			<svws-ui-text-input placeholder="Telefon" :model-value="data().telefon"
				@update:model-value="doPatch({ telefon: String($event) })" type="tel" />
			<svws-ui-text-input placeholder="Mobil oder Fax" :model-value="data().telefonMobil"
				@update:model-value="doPatch({ telefonMobil: String($event) })" type="tel" />
			<svws-ui-text-input placeholder="Private E-Mail-Adresse" :model-value="data().emailPrivat"
				@update:model-value="doPatch({ emailPrivat: String($event) })" type="email" verify-email />
			<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :model-value="data().emailSchule"
				@update:model-value="doPatch({ emailSchule: String($event) })" type="email" verify-email />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed, ref } from "vue";
	import { orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/helfer";

	import type { OrtKatalogEintrag, OrtsteilKatalogEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core";
	import { AdressenUtils } from "@svws-nrw/svws-core";

	const props = defineProps<{
		data: () => SchuelerStammdaten;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const eingabeStrasseOk = ref(true);

	const inputStrasse: WritableComputedRef<string | undefined> = computed({
		get: () => {
			const ret = AdressenUtils.combineStrasse(props.data().strassenname || "", props.data().hausnummer || "", props.data().hausnummerZusatz || "");
			return ret ?? undefined;
		},
		set(value: string | undefined) {
			if (value) {
				const vals = AdressenUtils.splitStrasse(value);
				doPatch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
				eingabeStrasseOk.value = !!vals;
			}
		}
	});

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => {
			const id = props.data().wohnortID;
			return id === null ? undefined : props.mapOrte.get(id)
		},
		set: (value) => doPatch({ wohnortID: value === undefined ? null : value.id })
	});

	const inputOrtsteilID: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get: () => {
			const id = props.data().ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => doPatch({ ortsteilID: value === undefined ? null : value.id })
	});

</script>
