<template>
	<div class="page--content">
		<svws-ui-content-card title="" class="col-span-full">
			<svws-ui-button class="ml-auto mr-0 p-3 mb-8 min-h-10" @click="add">
				<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
				<span class="icon icon-lg i-ri-chat-new-line" />
			</svws-ui-button>
			<div v-for="(d, index) in schuelerVermerke()" :key="d.id">
				<s-schueler-vermerk :schueler-vermerke="schuelerVermerke" :index="index" :map-vermerk-arten="mapVermerkArten" :patch="patch" :remove="remove" />
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerVermerkeProps } from "./SSchuelerVermerkeProps";
	import { ref, watch } from "vue";

	const props = defineProps<SchuelerVermerkeProps>();

</script>
