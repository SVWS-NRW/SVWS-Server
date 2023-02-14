<template>
	<div class="app-container">
		<s-card-gost-fachwahlen :jahrgang="jahrgang" :fachwahlen="fachwahlen" />
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang, GostStatistikFachwahl, List } from "@svws-nrw/svws-core-ts";

	defineProps<{
		jahrgang: GostJahrgang;
		fachwahlen: List<GostStatistikFachwahl>;
	}>();

</script>
