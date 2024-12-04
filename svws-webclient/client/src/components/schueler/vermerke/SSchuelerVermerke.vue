<template>
	<div class="page--content">
		<svws-ui-content-card title="" class="col-span-full">
			<svws-ui-button id="contentFocusField" v-autofocus class="ml-auto mr-0 p-3 mb-8 min-h-10" @click="addWrapper">
				<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
				<span class="icon icon-lg i-ri-chat-new-line" />
			</svws-ui-button>
			<div v-for="vermerk of schuelerVermerke()" :key="vermerk.id">
				<svws-ui-action-button class="mb-5 bg-blue-100" @click="activeVermerk = (activeVermerk?.id === vermerk.id) ? undefined : vermerk" icon="i-ri-message-line"
					:title="getTitle(vermerk)" :description="getDescription(vermerk)" :is-active="activeVermerk?.id === vermerk.id">
					<svws-ui-input-wrapper class="px-6">
						<svws-ui-textarea-input	v-model="vermerk.bemerkung" :autoresize="true" :rows="4" @change="bemerkung => patch({ bemerkung: String(bemerkung) }, vermerk.id)" />
						<div class="flex w-144">
							<p class="my-auto mr-4">Vermerkart:</p>
							<svws-ui-select class="bg-white" title="Bitte wählen" headless :model-value="mapVermerkArten.get(vermerk.idVermerkart || -1)" :items="mapVermerkArten.values()" :item-text="item => item.bezeichnung"
								@update:model-value="art => (art !== null) && patch({ idVermerkart: art?.id ?? -1 }, vermerk.id)" />
						</div>
						<div class="w-full flex justify-between">
							<div class="">
								<p class="text-headline-md mb-1">{{ vermerk.angelegtVon }}</p>
								<div v-if="apiStatus.pending" class="min-h-8">
									<svws-ui-spinner :spinning="true" />
								</div>
								<div v-else class="subTextContainer">
									<p v-if="vermerk.geaendertVon">	Zuletzt bearbeitet von {{ vermerk.geaendertVon }} am {{ getDate(vermerk) }}	</p>
									<p v-else> Erstellt am	{{ getDate(vermerk) }} </p>
								</div>
							</div>
							<div class="mb-0 mt-auto pr-0">
								<svws-ui-button	type="danger" @click="remove(vermerk.id)">
									Löschen
								</svws-ui-button>
							</div>
						</div>
					</svws-ui-input-wrapper>
				</svws-ui-action-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerVermerkeProps } from "./SSchuelerVermerkeProps";

	const props = defineProps<SchuelerVermerkeProps>();

	import { ref } from "vue";
	import { DateUtils, type SchuelerVermerke } from "@core";

	const activeVermerk = ref<SchuelerVermerke>();

	function getDate(vermerk: SchuelerVermerke) {
		return DateUtils.gibDatumGermanFormat(vermerk.datum ?? new Date().toISOString());
	}

	async function addWrapper() {
		await props.add();
		activeVermerk.value = [... props.schuelerVermerke()][props.schuelerVermerke().size() -1];
	}

	function getTitle(vermerk: SchuelerVermerke) {
		return `${props.mapVermerkArten.get(vermerk.idVermerkart ?? -1)?.bezeichnung ?? "Neuer Vermerk"}: ${vermerk.bemerkung}`;
	}

	function getDescription(vermerk: SchuelerVermerke) : string {
		return `${vermerk.geaendertVon ?? vermerk.angelegtVon} - ${getDate(vermerk)}`;
	}

</script>


<style lang="postcss" scoped>

	:deep(.svws-title) {
		@apply text-ellipsis overflow-hidden whitespace-nowrap w-full;
	}

</style>
