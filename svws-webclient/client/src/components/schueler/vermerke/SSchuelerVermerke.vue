<template>
	<div class="page page-grid-cards">
		<div>
			<svws-ui-checkbox :model-value="filterNurSichtbare" @update:model-value="value => setFilterNurSichtbare(value)">Unsichtbare ausblenden</svws-ui-checkbox>
			<svws-ui-button v-autofocus class="contentFocusField mt-4" @click="addWrapper">
				<span class="icon i-ri-chat-new-line" />
				<span class="ml-2">Neuen Vermerk hinzufügen</span>
			</svws-ui-button>
			<div class="flex flex-col gap-4 mt-4">
				<template v-for="vermerk of listVermerke" :key="vermerk.id">
					<ui-card icon="i-ri-message-line" :title="getTitle(vermerk)" :subtitle="getDescription(vermerk)" :is-open="activeVermerk?.id === vermerk.id"
						@update:is-open="(isOpen) => setCurrentVermerk(vermerk, isOpen)">
						<svws-ui-input-wrapper class="px-6">
							<svws-ui-textarea-input	v-model="vermerk.bemerkung" :autoresize="true" :rows="4" @change="bemerkung => patch({ bemerkung: String(bemerkung) }, vermerk.id)" />
							<div class="flex w-144">
								<p class="my-auto mr-4">Vermerkart:</p>
								<svws-ui-select class="bg-ui-contrast-0" title="Bitte wählen" headless :model-value="mapVermerkArten.get(vermerk.idVermerkart || -1)" :items="mapVermerkArten.values()" :item-text="item => item.bezeichnung"
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

	import type { SchuelerVermerkeProps } from "./SSchuelerVermerkeProps";

	const props = defineProps<SchuelerVermerkeProps>();

	import { computed, ref } from "vue";
	import { ArrayList, DateUtils, type SchuelerVermerke } from "@core";

	const activeVermerk = ref<SchuelerVermerke>();
	const oldAction = ref<{ vermerk: SchuelerVermerke | undefined; open: boolean }>({
		vermerk: undefined,
		open: false,
	});

	const listVermerke = computed(() => {
		if (!props.filterNurSichtbare)
			return props.schuelerVermerke();
		const liste = new ArrayList<SchuelerVermerke>();
		for (const item of props.schuelerVermerke()) {
			if (item.idVermerkart === null)
				continue;
			const art = props.mapVermerkArten.get(item.idVermerkart);
			if (art !== undefined && art.istSichtbar)
				liste.add(item);
		}
		return liste;
	})

	function setCurrentVermerk(newAction: SchuelerVermerke | undefined, open: boolean) {
		if(newAction?.id === oldAction.value.vermerk?.id && !open)
			return;
		oldAction.value.vermerk = activeVermerk.value;
		oldAction.value.open = (activeVermerk.value === undefined) ? false : true;
		if(open === true)
			activeVermerk.value = newAction;
		else
			activeVermerk.value = undefined;
	}

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
