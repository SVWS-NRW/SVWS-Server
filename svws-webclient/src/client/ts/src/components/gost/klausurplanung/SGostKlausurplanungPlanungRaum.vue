<template>
	<svws-ui-content-card title="Kausurraum">
		<svws-ui-multi-select title="Raum auswählen"
			v-model="stundenplanRaumSelected"
			@update:model-value="patchKlausurraum(raum.id, { idStundenplanRaum: stundenplanRaumSelected?.id }, manager)"
			:item-text="(item: StundenplanRaum) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
			:items="stundenplanmanager.getListRaum()" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanRaum, StundenplanManager, GostKlausurraumManager } from '@core';
	import type { GostKlausurraum } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		stundenplanmanager: StundenplanManager;
		raum: GostKlausurraum;
		manager: GostKlausurraumManager;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
	}>();

	const stundenplanRaumSelected = ref<StundenplanRaum | null>(props.raum.idStundenplanRaum === null ? null : props.stundenplanmanager.getRaum(props.raum.idStundenplanRaum));
	const getStundenplanraum = () => props.raum.idStundenplanRaum !== null ? props.stundenplanmanager.getRaum(props.raum.idStundenplanRaum) : null;

</script>
