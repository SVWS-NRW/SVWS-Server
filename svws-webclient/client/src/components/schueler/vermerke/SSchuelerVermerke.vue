<template>
	<div class="page--content">
		<svws-ui-content-card title="" class="col-span-full">
			<svws-ui-button class="newVermerkContainer" @click="createVermerk">
				<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
				<span class="icon i-ri-chat-new-line"/>
			</svws-ui-button>

			<div v-for="(d, index) in data" :key="d.id" >
				<s-card-schueler-vermerke :data="d" :mapVermerkArten="mapVermerkArten" :patch="patch" :deleteVermerk="deleteChain" :key= "index + forceUpdateKey" />
			</div>

			<svws-ui-button v-if="data.size() > 3" class="newVermerkContainer">
				<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
				<span class="icon i-ri-chat-new-line"/>
			</svws-ui-button>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type {SchuelerVermerkeProps} from "./SSchuelerVermerkeProps";
	import { ref, watch } from "vue";

	const props = defineProps<SchuelerVermerkeProps>();

	const forceUpdateKey = ref<number>(0);

	// Falls sich die Props ändern muss die Liste neu gerändert werden.
	watch(() => props.data, (first, second) => {
		if (first.size() != second.size()) {
			forceUpdateKey.value += 1;
		}
    });

	const deleteChain = (idVermerk: number) => {
		props.deleteVermerk(idVermerk);
	}

	const createVermerk = () => {
		props.create()
	}

</script>

<style scoped>

	.icon {
		@apply w-6;
		@apply h-6;
		@apply block;
	}

	.newVermerkContainer {
		@apply ml-auto;
		@apply mr-0;
		@apply p-3;
		@apply mb-8;
		@apply min-h-10;
	}

</style>
