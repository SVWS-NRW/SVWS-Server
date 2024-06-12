<template>
	<div class="page--content">
		<svws-ui-content-card title="" class="col-span-full">
			<svws-ui-button class="ml-auto mr-0 p-3 mb-8 min-h-10" @click="add">
				<p style="margin-right: 1rem">Neuen Vermerk hinzufügen</p>
				<span class="icon icon-lg i-ri-chat-new-line" />
			</svws-ui-button>
			<div v-for="(d, index) in schuelerVermerke()" :key="d.id">
				<svws-ui-action-button class="actionButtonElement" @click="() => {activeId = activeId === index ? -1 : index}" icon="i-ri-message-line"
					:title="getTitle(index)" :description="getDescription(index)" :is-active="activeId === index">
					<svws-ui-input-wrapper class="px-6">
						<svws-ui-textarea-input	v-model="schuelerVermerke().get(index).bemerkung" :autoresize="true" :rows="4" @change="(newVal) => patchInternal({bemerkung : String(newVal)}, schuelerVermerke().get(index).id)" />
						<div class="flex w-144">
							<p class="my-auto mr-4">Vermerkart:</p>
							<svws-ui-select :model-value="aktuelleVermerkArten[index]" :headless="false"	:items="props.mapVermerkArten.values()" :item-text="(item) => item.bezeichnung + ''"
								@update:model-value="(newVal: VermerkartEintrag) => {patch({idVermerkart: newVal.id}, schuelerVermerke().get(index).id)}" />
						</div>
						<div class="w-full flex">
							<div class="w-4/5">
								<p class="text-headline-md mb-1">{{ schuelerVermerke().get(index).angelegtVon }}</p>
								<div v-if="patching" class="subTextContainer">
									<svws-ui-spinner :spinning="true" />
								</div>
								<div v-else class="subTextContainer">
									<p v-if="schuelerVermerke().get(index).geaendertVon">
										Zuletzt bearbeitet von {{ schuelerVermerke().get(index).geaendertVon }} am {{ formatDate(String(schuelerVermerke().get(index).datum))}}
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
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerVermerkeProps } from "./SSchuelerVermerkeProps";

	const props = defineProps<SchuelerVermerkeProps>();

	import type { SchuelerVermerke, VermerkartEintrag } from "@core";
	import { computed,  ref } from "vue";

	const activeId = ref<number>(-1);

	const patching = ref<boolean>(false);

	async function patchInternal(data : Partial<SchuelerVermerke>, idVermerk : number) {
		patching.value = true;
		await props.patch(data, idVermerk);
		patching.value = false;
	}

	const formatDate = (date: string) => {
		return date.split("-").reverse().join(".");
	};

	// map Vermerkarten zu ihren entsprechenden Vermerken
	const aktuelleVermerkArten = computed<VermerkartEintrag[]>(
		() => {
			let vermerkArtEintraege : VermerkartEintrag[] = [];
			for (let index = 0; index < props.schuelerVermerke().size(); index++) {
				let schuelerVermerk = props.schuelerVermerke().get(index);
				let vermerArtEintrag = props.mapVermerkArten.get(schuelerVermerk.idVermerkart);
				if (vermerArtEintrag)
					vermerkArtEintraege.push(vermerArtEintrag)
			}
			return vermerkArtEintraege
		}
	)

	const getTitle = (index: number) : string => {
		let title = aktuelleVermerkArten.value[index].bezeichnung || "";
		title += ': ' + ((props.schuelerVermerke().get(index).bemerkung?.length === 0) ? 'Neuer Vermerk' : props.schuelerVermerke().get(index).bemerkung);
		return title;
	}

	const getDescription = (index: number) : string => {
		return (props.schuelerVermerke().get(index).geaendertVon || props.schuelerVermerke().get(index).angelegtVon) + ' - ' + formatDate(String(props.schuelerVermerke().get(index).datum));
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
