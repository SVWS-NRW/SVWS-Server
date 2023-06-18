<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name" v-model="name" type="text" />
			<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
			<svws-ui-text-input placeholder="Namensergänzung" v-model="namezusatz" type="text" />
			<svws-ui-text-input placeholder="Branche" v-model="branche" title="Branche" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { BetriebStammdaten, KatalogEintrag } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";



	const props = defineProps<{
		daten: BetriebStammdaten;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<BetriebStammdaten>): void;
	}>()

	function doPatch(data: Partial<BetriebStammdaten>) {
        emit('patch', data);
	}

	const name : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.name1 ?? undefined,
		set: (value) => doPatch({ name1: value})
	})

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => (props.daten.adressArt === null) ? undefined : props.mapBeschaeftigungsarten.get(props.daten.adressArt),
		set: (value) => doPatch({ adressArt: value?.id})
	})

	const namezusatz : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.name2 ?? undefined,
		set: (value) => doPatch({ name2: value })
	})

	const branche : WritableComputedRef<string | undefined> = computed({
		get: () => props.daten.branche ?? undefined,
		set: (value) => void doPatch({ branche: value } )
	})

</script>