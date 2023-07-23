<template>
	<router-view />
</template>

<script setup lang="ts">

	import { RouterView } from "vue-router";
	// import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';
	import {ref} from "vue";

	// const props = defineProps<GostKlausurplanungAuswahlProps>();

	const ansicht = ref("klausurdaten");

</script>
