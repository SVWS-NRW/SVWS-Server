<template>
	<div class="page--content">
		<svws-ui-content-card title="Vermerke" class="col-span-full">

			<div v-for="(d, index) in data">
				<s-card-schueler-vermerke :data="d" :mapVermerkArten="mapVermerkArten" :patch="patch"/>
			</div>
		</svws-ui-content-card>

	</div>
</template>

<script setup lang="ts">
import type {SchuelerVermerkeProps} from './SSchuelerVermerkeProps';
import SCardSchuelerVermerke from "~/components/schueler/vermerke/SCardSchuelerVermerke.vue";
import {ref} from "vue";

const props = defineProps<SchuelerVermerkeProps>();
const accordionOpen = ref<number>(-1);

</script>
