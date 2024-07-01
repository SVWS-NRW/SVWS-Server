<template>
	<div class="page--content">
		<svws-ui-content-card title="" class="col-span-full">
			<svws-ui-button class="ml-auto mr-0 p-3 mb-8 min-h-10" @click="add">
				<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
				<span class="icon icon-lg i-ri-chat-new-line" />
			</svws-ui-button>
			<div v-for="vermerk of schuelerVermerke()" :key="vermerk.id">
				<svws-ui-action-button class="actionButtonElement" @click="activeVermerk = (activeVermerk?.id === vermerk.id) ? undefined : vermerk" icon="i-ri-message-line"
					:title="getTitle(vermerk)" :description="getDescription(vermerk)" :is-active="activeVermerk?.id === vermerk.id">
					<svws-ui-input-wrapper class="px-6">
						<svws-ui-textarea-input	v-model="vermerk.bemerkung" :autoresize="true" :rows="4" @change="bemerkung => patch({ bemerkung }, vermerk.id)" />
						<div class="flex w-144">
							<p class="my-auto mr-4">Vermerkart:</p>
							<svws-ui-select :model-value="mapVermerkArten.get(vermerk.idVermerkart)" :headless="false"	:items="mapVermerkArten.values()" :item-text="item => item.bezeichnung"
								@update:model-value="art => (art !== null) && patch({ idVermerkart: art?.id ?? -1 }, vermerk.id)" />
						</div>
						<div class="w-full flex">
							<div class="w-4/5">
								<p class="text-headline-md mb-1">{{ vermerk.angelegtVon }}</p>
								<div v-if="apiStatus.pending" class="subTextContainer">
									<svws-ui-spinner :spinning="true" />
								</div>
								<div v-else class="subTextContainer">
									<p v-if="vermerk.geaendertVon">	Zuletzt bearbeitet von {{ vermerk.geaendertVon }} am {{ getDate(vermerk) }}	</p>
									<p v-else> Erstellt am	{{ getDate(vermerk) }} </p>
								</div>
							</div>
							<div class="w-1/5 mb-0 mt-auto">
								<svws-ui-button	type="danger" class="deleteButton" @click="remove(vermerk.id)">
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

	import { DateUtils, type SchuelerVermerke } from "@core";
	import { ref } from "vue";

	const activeVermerk = ref<SchuelerVermerke>();

	function getDate(vermerk: SchuelerVermerke) {
		return DateUtils.gibDatumGermanFormat(vermerk.datum || new Date().toISOString());
	}

	function getTitle(vermerk: SchuelerVermerke) {
		return `${props.mapVermerkArten.get(vermerk.idVermerkart)?.bezeichnung}: ${vermerk.bemerkung || '⎯⎯⎯⎯⎯⎯⎯⎯'}`;
	}

	function getDescription(vermerk: SchuelerVermerke) : string {
		return (vermerk.geaendertVon || vermerk.angelegtVon) + ' - ' + getDate(vermerk);
	}

</script>


<style lang="postcss" scoped>

	:deep(.svws-title) {
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
		width: 100rem;
	}

	.actionButtonElement {
		@apply mb-5 bg-blue-100;
	}

	.deleteButton {
		@apply ml-auto mr-0;
	}

	.subTextContainer {
		@apply min-h-8;
	}

</style>
