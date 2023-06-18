<template>
	<svws-ui-content-card title="Adresse">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Straße / Hausnummer" v-model="strassenname" type="text" />
			</div>
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Zusatz" v-model="hausnummerzusatz" type="text" />
			</div>
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<!-- <svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="mapOrtsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''"
            :item-sort="ortsteilSort" :item-filter="ortsteilFilter" /> -->

			<svws-ui-text-input placeholder="1. Telefon-Nr." v-model="telefon1" type="text" />
			<svws-ui-text-input placeholder="2. Telefon-Nr." v-model="telefon2" type="text" />
			<svws-ui-text-input placeholder="Fax-Nr." v-model="fax" type="text" />
			<svws-ui-text-input placeholder="E-Mail Adresse" v-model="email" type="email" verify-email />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { BetriebStammdaten, KatalogEintrag, OrtKatalogEintrag } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";



	const props = defineProps<{
		daten: BetriebStammdaten;
		mapOrte: Map<number, OrtKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<BetriebStammdaten>): void;
	}>()

	function doPatch(data: Partial<BetriebStammdaten>) {
		emit('patch', data);
	}

	const telefon1 : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.telefon1 ?? undefined,
		set: (value) => void doPatch({ telefon1: value })
	})

	const telefon2 : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.telefon2 ?? undefined,
		set: (value) => void doPatch({ telefon2: value } )
	})

	const fax : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.fax ?? undefined,
		set: (value) => void doPatch({ fax: value } )
	})

	const email : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.email ?? undefined,
		set: (value) => void doPatch({ email: value } )
	})

	const strassenname : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.strassenname ?? undefined,
		set: (value) => void doPatch({ strassenname: value } )
	})

	const hausnummerzusatz : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.hausnrzusatz ?? undefined,
		set: (value) => void doPatch({ hausnrzusatz: value } )
	})

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () =>props.daten.ort_id ? props.mapOrte.get(props.daten.ort_id) : undefined,
		set: (val) =>	doPatch({ ort_id : val?.id })
	});
</script>
