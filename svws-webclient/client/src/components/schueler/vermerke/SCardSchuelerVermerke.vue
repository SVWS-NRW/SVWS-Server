<template>
	<svws-ui-action-button class="actionButtonElement" @click="() => {isActive = !isActive;	updateDataFromPRops();}" icon="i-ri-message-line"
		:title="getTitle()" :description="getDescription()" :is-active="isActive">
		<svws-ui-input-wrapper class="card">
			<svws-ui-textarea-input	v-model="innerData.Bemerkung" :autoresize="true" :rows="4" @change="(newVal) => innerPatch(String(newVal), innerData.id)" />
			<div class="selectElement">
				<p class="labelVermerkart">Vermerkart:</p>
				<svws-ui-select v-model="aktuelleVermerkArt" :headless="false"	:items="[...props.mapVermerkArten.values()]" :item-text="(item) => item.bezeichnung"
					@update:model-value="(newVal: VermerkartEintrag) => {patch({VermerkArt_ID: newVal.id}, innerData.id)}" />
			</div>
			<div style="width: 100%; display:flex; ">
				<div style="width: 80%">
					<p class="profileName">{{ data.AngelegtVon }}</p>
					<div v-if="patching" class="subTextContainer">
						<svws-ui-spinner :spinning="true" />
					</div>
					<div v-else class="subTextContainer">
						<p v-if="data.GeaendertVon">
							Zuletzt bearbeitet von {{ data.GeaendertVon }} am {{ formatDate(String(data.Datum))}}
						</p>
						<p v-else>
							Erstellt am	{{ formatDate(String(data.Datum)) }}
						</p>
					</div>
				</div>
				<div style="width: 20%; margin-bottom: 0px; margin-top: auto;">
					<svws-ui-button	type="danger" class="deleteButton"	@click="deleteVermerk(innerData.id)">
						Löschen
					</svws-ui-button>
				</div>
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import type { SchuelerVermerke, VermerkartEintrag } from "@core";
	import { computed,  ref } from "vue";

	const isActive = ref<boolean>(false);

	const props = defineProps<{
		data: SchuelerVermerke;
		mapVermerkArten: Map<number, VermerkartEintrag>;
		patch: (data: Partial<SchuelerVermerke>, idVermerk: number) => { };
		deleteVermerk: (idVermerk: number) => { };
	}>();

	const innerData = ref<SchuelerVermerke>(props.data);

	const updateDataFromPRops = () => {
		innerData.value = props.data
	};

	const patching = ref<boolean>(false);

	const innerPatch = (newVal: string, id: number) => {
		patching.value = true
		props.patch({ Bemerkung: newVal || "" }, id)
	};

	const formatDate = (date: string) => {
		return date.split("-").reverse().join(".")
	};

	const aktuelleVermerkArt = computed({
		get: () => {
			patching.value = false;
			return [...props.mapVermerkArten.values()].find(
				(elem) => (elem.id === props.data.VermerkArt_ID)
			);
		},
		set: (newVal) => {
			return newVal;
		},
	});

	const getTitle = () => {
		let title = aktuelleVermerkArt.value?.bezeichnung || ""
		title += ': ' + ((props.data.Bemerkung?.length === 0) ? 'Neuer Vermerk' : props.data.Bemerkung)
		return title
	}

	const getDescription = () => {
		return (props.data.GeaendertVon || props.data.AngelegtVon) + ' - ' + formatDate(String(props.data.Datum))
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

	.icon-xxl {
		@apply m-auto inline-block;
	}

	.profileName {
		@apply text-headline-md mb-1;
	}

	.card {
		@apply px-6;
	}

	.innerLaylout {
		@apply flex w-full;
	}

	.selectElement {
		@apply flex w-144;
	}

	.subTextContainer {
		@apply min-h-8;
	}

	.labelVermerkart {
		@apply my-auto mr-4;
	}

	.deleteButton {
		@apply ml-auto mr-0;
	}

</style>
