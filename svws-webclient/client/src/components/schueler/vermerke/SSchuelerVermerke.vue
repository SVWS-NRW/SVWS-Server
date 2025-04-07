<template>
	<div class="page page-grid-cards">
		<div>
			<div class="flex justify-between">
				<svws-ui-button v-autofocus class="contentFocusField min-h-[32px]" @click="addWrapper">
					<span class="icon i-ri-chat-new-line" />
					<span class="ml-2">Neuen Vermerk hinzufügen</span>
				</svws-ui-button>
				<svws-ui-checkbox class="self-center" :model-value="filterNurSichtbare" @update:model-value="value => setFilterNurSichtbare(value)">Nur <b>sichtbare</b> Vermerkarten anzeigen</svws-ui-checkbox>
			</div>
			<div class="flex flex-col gap-4 mt-4">
				<template v-for="vermerk of listVermerke" :key="vermerk.id">
					<ui-card icon="i-ri-message-line" :title="getTitle(vermerk)" :subtitle="getDescription(vermerk)" :is-open="activeVermerk?.id === vermerk.id">
						<svws-ui-input-wrapper class="px-6">
							<svws-ui-textarea-input	v-model="vermerk.bemerkung" :autoresize="true" :rows="4" @change="bemerkung => patch({ bemerkung: String(bemerkung) }, vermerk.id)" />
							<div class="flex w-200">
								<p class="my-auto mr-4">Vermerkart:</p>
								<svws-ui-select class="bg-ui-100 mr-4" title="Bitte wählen" headless :model-value="mapVermerkArten.get(vermerk.idVermerkart || -1)" :items="mapVermerkArten.values()" :item-text="item => getItemText(item)"
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
							</div>
						</svws-ui-input-wrapper>
						<template #buttonFooterRight>
							<svws-ui-button type="danger" title="Löschen" @click="remove(vermerk.id)" class="mt-4">
								Löschen
							</svws-ui-button>
						</template>
					</ui-card>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuelerVermerkeProps } from "./SSchuelerVermerkeProps";
	import { ArrayList, DateUtils, type SchuelerVermerke, type VermerkartEintrag} from "@core";

	const props = defineProps<SchuelerVermerkeProps>();

	const activeVermerk = ref<SchuelerVermerke>();

	const listVermerke = computed(() => {
		if (!props.filterNurSichtbare)
			return props.schuelerVermerke();
		const liste = new ArrayList<SchuelerVermerke>();
		for (const item of props.schuelerVermerke()) {
			if (item.idVermerkart === null) {
				liste.add(item);
				continue;
			}
			const art = props.mapVermerkArten.get(item.idVermerkart);
			if ((art !== undefined) && art.istSichtbar)
				liste.add(item);
		}
		return liste;
	})

	function getDate(vermerk: SchuelerVermerke) {
		return DateUtils.gibDatumGermanFormat(vermerk.datum ?? new Date().toISOString());
	}

	async function addWrapper() {
		await props.add();
		activeVermerk.value = props.schuelerVermerke().getLast();
	}

	function getTitle(vermerk: SchuelerVermerke) {
		const title = `${props.mapVermerkArten.get(vermerk.idVermerkart ?? -1)?.bezeichnung ?? "Neuer Vermerk"}: ${vermerk.bemerkung}`
		return title.length > 50 ? title.substring(0, 50) + "..." : title;
	}

	function getItemText(item: VermerkartEintrag) {
		const vermerkArtIsVisible = !item.istSichtbar;
		return `${item.bezeichnung} ${vermerkArtIsVisible ? '(nicht sichtbar)' : ''}`
	}

	function getDescription(vermerk: SchuelerVermerke) : string {
		return `${vermerk.geaendertVon ?? vermerk.angelegtVon} - ${getDate(vermerk)}`;
	}

</script>
