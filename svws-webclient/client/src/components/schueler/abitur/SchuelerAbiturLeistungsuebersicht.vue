<template>
	<div class="page page-flex-row pt-0">
		TODO
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { Schulform } from "@core";

	import type { SchuelerAbiturLeistungsuebersichtProps } from "./SchuelerAbiturLeistungsuebersichtProps";

	const props = defineProps<SchuelerAbiturLeistungsuebersichtProps>();

	const schulform = computed<Schulform>(() => Schulform.data().getWertByKuerzel(props.schule.schulform) ?? Schulform.G);

</script>
