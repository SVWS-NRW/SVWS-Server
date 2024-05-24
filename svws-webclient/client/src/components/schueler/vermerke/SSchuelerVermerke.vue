<template>
	<div class="page--content">
		<svws-ui-content-card title="" class="col-span-full">

			<div style="display: flex; width: 100%; justify-content: space-between; margin-bottom: 1rem">
				<p class="header">Vermerke</p>
				<svws-ui-button style="padding: 0.5rem 1rem" @click="() => {addNew = true}">
					<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
					<span class="icon i-ri-chat-new-line"/></svws-ui-button>
			</div>

			<div v-if="addNew">
				<p>Neuer Vermerk</p>
				<s-card-schueler-vermerke
					v-if="newVermerk"
					:data="newVermerk"
					:newVermerk="true"
					:mapVermerkArten="mapVermerkArten"
					:patch="patch"
				/>

			</div>



			<div v-for="(d, index) in data">
				<s-card-schueler-vermerke
					:data="d"
					:new="false"
					:newVermerk="false"
					:mapVermerkArten="mapVermerkArten"
					:patch="patch"
				/>
			</div>

			<div style="display: flex; width: 100%; justify-content: end; margin-bottom: 1rem">
				<svws-ui-button style="padding: 0.5rem 1rem">
					<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
					<span class="icon i-ri-chat-new-line"/></svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
import type {SchuelerVermerkeProps} from "./SSchuelerVermerkeProps";
import SCardSchuelerVermerke from "~/components/schueler/vermerke/SCardSchuelerVermerke.vue";
import {ref} from "vue";
import {SchuelerVermerke, VermerkartEintrag} from "@core";

const props = defineProps<SchuelerVermerkeProps>();
const addNew = ref<boolean>(false);

const newVermerk = ref<SchuelerVermerke>(new SchuelerVermerke())


</script>


<style scoped>
.icon {
	width: 1.3rem;
	height: 1.3rem;
	display: block;
}

.header {
	font-size: 1.125rem;
	line-height: 1.33;
	font-weight: 700;
}
</style>
