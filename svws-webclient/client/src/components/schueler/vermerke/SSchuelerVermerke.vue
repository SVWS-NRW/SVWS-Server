<template>
	<div class="page--content">
		<s-card-schueler-vermerke :data="data" :patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerVermerkeProps } from './SSchuelerVermerkeProps';
	import SCardSchuelerVermerke from "~/components/schueler/vermerke/SCardSchuelerVermerke.vue";

	const props = defineProps<SchuelerVermerkeProps>();

</script>
