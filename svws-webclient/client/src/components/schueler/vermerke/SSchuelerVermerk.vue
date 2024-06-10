<template>
	<svws-ui-action-button class="actionButtonElement" @click="() => {isActive = !isActive;}" icon="i-ri-message-line"
		:title="getTitle()" :description="getDescription()" :is-active="isActive">
		<svws-ui-input-wrapper class="px-6">
			<svws-ui-textarea-input	v-model="schuelerVermerke().get(index).bemerkung" :autoresize="true" :rows="4" @change="(newVal) => patchInternal({bemerkung : String(newVal)}, schuelerVermerke().get(index).id)" />
			<div class="flex w-144">
				<p class="my-auto mr-4">Vermerkart:</p>
				<svws-ui-select v-model="aktuelleVermerkArt" :headless="false"	:items="[...props.mapVermerkArten.values()]" :item-text="(item) => item.bezeichnung"
					@update:model-value="(newVal: VermerkartEintrag) => {patch({idVermerkart: newVal.id}, schuelerVermerke().get(props.index).id)}" />
			</div>
			<div class="w-full flex">
				<div class="w-4/5">
					<p class="text-headline-md mb-1">{{ schuelerVermerke().get(index).angelegtVon }}</p>
					<div v-if="patching" class="subTextContainer">
						<svws-ui-spinner :spinning="true" />
					</div>
					<div v-else class="subTextContainer">
						<p v-if="schuelerVermerke().get(index).geaendertVon">
							Zuletzt bearbeitet von {{ schuelerVermerke().get(index).geaendertVon }} am {{ formatDate(String(schuelerVermerke().get(props.index).datum))}}
						</p>
						<p v-else>
							Erstellt am	{{ formatDate(String(schuelerVermerke().get(index).datum)) }}
						</p>
					</div>
				</div>
				<div class="w-1/5 mb-0 mt-auto">
					<svws-ui-button	type="danger" class="deleteButton" @click="remove(schuelerVermerke().get(index).id)">
						Löschen
					</svws-ui-button>
				</div>
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import type { List, SchuelerVermerke, VermerkartEintrag } from "@core";
	import { set } from "@vueuse/core";
	import { computed,  ref } from "vue";

	const isActive = ref<boolean>(false);

	const props = defineProps<{
		schuelerVermerke: () => List<SchuelerVermerke>;
		index: number;
		mapVermerkArten: Map<number, VermerkartEintrag>;
		patch: (data : Partial<SchuelerVermerke>, idVermerk : number) => Promise<void>;
		remove: (idVermerk: number) => Promise<void>;
	}>();

	const patching = ref<boolean>(false);

	async function patchInternal(data : Partial<SchuelerVermerke>, idVermerk : number) {
		patching.value = true;
		await props.patch(data, idVermerk);
		patching.value = false;
	}

	const formatDate = (date: string) => {
		return date.split("-").reverse().join(".");
	};

	const aktuelleVermerkArt = computed<VermerkartEintrag | undefined>({
		get: () => props.mapVermerkArten.get(props.schuelerVermerke().get(props.index).idVermerkart),
		set: () => {} // prop will be updated trough patch and commit
	});

	const getTitle = () => {
		let title = aktuelleVermerkArt.value?.bezeichnung || "";
		title += ': ' + ((props.schuelerVermerke().get(props.index).bemerkung?.length === 0) ? 'Neuer Vermerk' : props.schuelerVermerke().get(props.index).bemerkung);
		return title;
	}

	const getDescription = () => {
		return (props.schuelerVermerke().get(props.index).geaendertVon || props.schuelerVermerke().get(props.index).angelegtVon) + ' - ' + formatDate(String(props.schuelerVermerke().get(props.index).datum));
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
