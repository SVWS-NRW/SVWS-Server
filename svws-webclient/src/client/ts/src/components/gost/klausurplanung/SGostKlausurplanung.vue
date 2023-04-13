<template>
	<div class="router-tab-bar--subnav">
		<!--TODO: "Ansicht" auswahl aus Sidebar hier anzeigen-->
		<svws-ui-radio-group id="ansicht" :row="true">
			<!-- Statisches Beispiel:
			<svws-ui-radio-option name="ansicht" v-model="ansicht" label="Klausurdaten" value="klausurdaten" icon-type="view"/>
			<svws-ui-radio-option name="ansicht" v-model="ansicht" label="Schienen" value="schienen" icon-type="view" />
			<svws-ui-radio-option name="ansicht" v-model="ansicht" label="Kalender" value="kalender" icon-type="view" />-->
		</svws-ui-radio-group>
	</div>
	<router-view />
</template>

<script setup lang="ts">

	import { RouterView } from "vue-router";
	import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';
	import {ref} from "vue";

	const props = defineProps<GostKlausurplanungAuswahlProps>();

	const ansicht = ref("klausurdaten");

</script>
