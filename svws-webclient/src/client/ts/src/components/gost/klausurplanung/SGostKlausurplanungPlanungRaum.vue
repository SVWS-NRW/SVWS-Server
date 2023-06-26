<template>
	<svws-ui-content-card title="">
		<SvwsUiMultiSelect title="Raum auswählen"
			v-model="stundenplanRaumSelected"
			:item-text="(item) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
			:items="stundenplanmanager.getListRaum()" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostJahrgangsdaten, GostKursklausurManager, GostFaecherManager, StundenplanRaum, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager } from '@core';
	import type { GostKlausurraum } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		stundenplanmanager: StundenplanManager;
		raum: GostKlausurraum;
	}>();

	const stundenplanRaumSelected = ref<StundenplanRaum | null>(null);
	const getStundenplanraum = () => props.raum.idStundenplanRaum !== null ? props.stundenplanmanager.getRaum(props.raum.idStundenplanRaum) : null;

</script>
