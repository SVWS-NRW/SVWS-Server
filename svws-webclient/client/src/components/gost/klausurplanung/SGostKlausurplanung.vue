<template>
	<router-view />
</template>

<script setup lang="ts">

	import { RouterView } from "vue-router";
	// import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';
	import {ref} from "vue";

	// const props = defineProps<GostKlausurplanungAuswahlProps>();

	const ansicht = ref("klausurvorlagen");

</script>

<style lang="postcss">
.router--tab--gost\.klausurplanung {
  > .router-tab-bar--wrapper {
    &:after {
      @apply border-dashed;
    }

    .router-tab-bar-button--active:after {
      @apply border-transparent;
    }
  }
}
</style>
